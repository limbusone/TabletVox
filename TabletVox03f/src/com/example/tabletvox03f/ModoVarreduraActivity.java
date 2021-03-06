package com.example.tabletvox03f;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.tabletvox03f.dal.CarregarImagensComandos;
import com.example.tabletvox03f.dal.CarregarImagensTelas;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.categoria.CategoriaDAO;
import com.example.tabletvox03f.management.Opcoes;

/*
 * Varredura Externa: 
 * 		percorrimento da borda vermelha pelos controles:
 * 		- gridview principal
 * 		- btnNext
 * 		- gridview frase
 * 		- gridview atalhos
 * 		- btnShowHideCommands
 * Varredura Interna:
 * 		percorrimento da borda vermelha pelo conte�do dos controles:
 * 		- gridview principal
 * 		- gridview frase
 * 		- gridview atalhos
 *  
 * 
 * */
public class ModoVarreduraActivity extends TelaBaseActivity
{
	
	protected int estadoAtual;
	
	protected Timer animationTimer;
	protected int delayVarredura;
	protected int indiceItemPrincipal;
	protected int indiceItemFrase;
	protected boolean iteracaoExternaAtiva;
	protected boolean iteracaoInternaAtiva;
	
		
	protected boolean iteracaoInternaPrincipalAtiva;
	protected boolean iteracaoInternaFraseAtiva;
	protected TimerTask taskVarreduraExterna;
	protected TimerTask taskVarreduraInterna;
	
	
	protected boolean mostrarComandos;
	protected boolean esconderComandos;
	
	protected boolean adicionarImagemFrase;
	protected boolean acionarComando;
	
	protected static final int ESTADO_TODOS_DESATIVADOS 						= 0;
	protected static final int ESTADO_GRIDVIEW_PRINCIPAL_ATIVO 					= 1;
	protected static final int ESTADO_GRIDVIEW_FRASE_ATIVO 						= 2;
	protected static final int ESTADO_GRIDVIEW_ATALHOS_ATIVO					= 3;
	protected static final int ESTADO_BUTTON_MOSTRAR_ESCONDER_COMANDOS_ATIVO 	= 4;
	protected static final int ESTADO_BUTTON_PROXIMA_TELA_ATIVO					= 5;
	private static final int ESTADO_ITERANDO_ITENS_GRIDVIEW_PRINCIPAL			= 6;
	private static final int ESTADO_ITERANDO_ITENS_GRIDVIEW_FRASE				= 7;
	
	protected void acaoDoEventoPrincipal()
	{
		// obs: a compara��o estadoAtual == 3 est� comentada porque a ideia tamb�m era que
		// houvesse mais de um atalho por�m foi decidido somente um atalho
		if (estadoAtual == ESTADO_GRIDVIEW_PRINCIPAL_ATIVO || estadoAtual == ESTADO_GRIDVIEW_FRASE_ATIVO /*|| estadoAtual == 3*/)
		{
			
			if (iteracaoExternaAtiva)
				entrar();
			else if (iteracaoInternaAtiva)
				sair();			
		
		}
		else if (estadoAtual == ESTADO_GRIDVIEW_ATALHOS_ATIVO) // atalho
		{
			ImgItem imgi = (ImgItem) gridview_atalhos.getChildAt(0);
			
			int cmd = imgi.getAssocImagemSom().getCmd();
			// tocar som frase
			acionarComando(cmd);
		
		}
		else if (estadoAtual == ESTADO_BUTTON_MOSTRAR_ESCONDER_COMANDOS_ATIVO) // mostrar comandos / esconder comandos
		{
			if (mostrarComandos)
			{
				CarregarImagensComandos cic = new CarregarImagensComandos()
				{

					// metodo que roda na UI Thread antes da atividade em background
					@Override
					protected void onPreExecute()
					{
						super.onPreExecute();
						activeContext = ModoVarreduraActivity.this;
						gridview 	= ModoVarreduraActivity.this.gridview;
						pgrbar		= (ProgressBar) ModoVarreduraActivity.this.findViewById(R.id.progressBar1);
						
						pgrbar.setVisibility(View.VISIBLE);							
					}
					
				};
				cic.execute(CarregarImagensComandos.OPCAO_CARREGAR_TODOS_COMANDOS); // false: mostrar todos os comandos
			} 
			else if (esconderComandos)
			{
				CarregarImagensTelas cit = new CarregarImagensTelas()
				{
					// metodo que roda na UI Thread antes da atividade em background
					@Override
					protected void onPreExecute()
					{
						super.onPreExecute();
						activeContext = ModoVarreduraActivity.this;
						gridview 	= ModoVarreduraActivity.this.gridview;
						pgrbar		= (ProgressBar) ModoVarreduraActivity.this.findViewById(R.id.progressBar1);
						
						pgrbar.setVisibility(View.VISIBLE);									
					}
			
				};
				
				cit.execute(current_page, CarregarImagensTelas.OPCAO_CARREGAR_IMAGENS, current_categoriaId);					
			}
			alternarEventoBtnShowHideCommands();
			alternarEventoAoSelecionarImagemDeGridViewPrincipal();				
		}
		else if (estadoAtual == ESTADO_BUTTON_PROXIMA_TELA_ATIVO) // proxima pagina
		{
			CarregarImagensTelas cit = new CarregarImagensTelas()
			{
				// metodo que roda na UI Thread antes da atividade em background
				@Override
				protected void onPreExecute()
				{
					super.onPreExecute();
					activeContext = ModoVarreduraActivity.this;
					gridview 	= ModoVarreduraActivity.this.gridview;
					pgrbar		= (ProgressBar) ModoVarreduraActivity.this.findViewById(R.id.progressBar1);
					
					pgrbar.setVisibility(View.VISIBLE);								
				}
		
			};
			vaiParaProximaPagina(cit, CarregarImagensTelas.OPCAO_CARREGAR_IMAGENS, current_categoriaId);
		}
		else if (estadoAtual == ESTADO_ITERANDO_ITENS_GRIDVIEW_PRINCIPAL) // adicionar imagem a frase / acionar comando
		{
			int indiceAtual = indiceItemPrincipal - 1;
			indiceAtual = (indiceAtual < 0) ? 0 : indiceAtual;
			ImgItem imgi = (ImgItem) gridview.getChildAt(indiceAtual);
			
			if (adicionarImagemFrase)
				addImagemFrase(imgi);
			else if (acionarComando)
				acionarComando(imgi);
		}
		else if (estadoAtual == ESTADO_ITERANDO_ITENS_GRIDVIEW_FRASE) // remover imagem da frase
		{
			int indiceAtual = indiceItemFrase - 1;
			indiceAtual = (indiceAtual < 0) ? 0 : indiceAtual;
			
			removerImagemDaFrase(indiceAtual);
			
			// se n�o houver mais imagens, vai para varredura externa
			if (lista_imagens_frase.isEmpty())
				sair();
			resetIndices();
		}		
	}
	
	
	protected void onCreateSuper(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Utils.MODO_ATIVO = "modo_varredura";
		
		current_categoriaId = (int) getIntent().getLongExtra("categoriaId", 0);
		
		// inicializa pagina��o
		current_page = init_page = 1;
		//final_page = (new XmlUtilsTelas(this, Utils.TELAS_NOME_ARQUIVO_XML_ATIVO, "root")).getLastPage();
		CategoriaDAO dao_cat = new CategoriaDAO(this);
		dao_cat.open();
		int final_page = dao_cat.getNumeroDePaginas(current_categoriaId);
		this.final_page = (final_page > 0) ? final_page : 1;
		dao_cat.close();
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		delayVarredura = Integer.parseInt(sp.getString(Opcoes.INTERVALO_TEMPO_VARREDURA_KEY, "" + Opcoes.INTERVALO_TEMPO_VARREDURA_DEFAULT));;
		
		// muda titulo conforme categoria
		setCurrentTitle(Utils.PERFIL_ATIVO.getNome() + " - Categoria " + 
		Utils.PERFIL_ATIVO.getCategoriaById(current_categoriaId).getNome());		
		
		// inicializando variaveis booleanas com valores default
		alternarVarredura();
		alternarEventoBtnShowHideCommands();
		alternarEventoAoSelecionarImagemDeGridViewPrincipal();
		
		resetIndices();
		
		armazenarBackgroundDosComponentes();
		
		setEstadoVarredura(ESTADO_GRIDVIEW_PRINCIPAL_ATIVO);
		
		if (copiarFraseGlobalParaFraseLocal())
			// transferindo as imagens globais para o gridview frase 
			gridview_frase.setAdapter(new ImageAdapterFrase(lista_imagens_frase));
		else
			lista_imagens_frase = new ArrayList<ImgItem>();

		animationTimer = new Timer();
		
		// task do timer de varredura externa
		taskVarreduraExterna = new TimerTask()
		{

			@Override
			public void run()
			{	
				// o timer tem que rodar na UI Thread porque se n�o n�o funciona!
				runOnUiThread(new Task1());
			}
			
		};
		
		taskVarreduraInterna = new TimerTask()
		{

			@Override
			public void run()
			{
				// o timer tem que rodar na UI Thread porque se n�o n�o funciona!
				runOnUiThread(new Task2());				
			}

		};
		
		// aqui carregam-se as imagens da primeira pagina
		CarregarImagensTelas cit = new CarregarImagensTelas()
		{
			// metodo que roda na UI Thread antes da atividade em background
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				activeContext = ModoVarreduraActivity.this;
				gridview 	= ModoVarreduraActivity.this.gridview;
				pgrbar		= (ProgressBar) ModoVarreduraActivity.this.findViewById(R.id.progressBar1);
				
				pgrbar.setVisibility(View.VISIBLE);		
			}
			
			// metodo que roda na UI Thread depois da atividade em background
			@Override
			protected void onPostExecute(ArrayList<AssocImagemSom> ais_list)
			{
				if (ais_list != null)
					gridview.setAdapter(new ImageAdapter(ModoVarreduraActivity.this, ais_list));
				pgrbar.setVisibility(View.GONE);
				// ativa o timer de varredura
				ModoVarreduraActivity.this.animationTimer.schedule(ModoVarreduraActivity.this.taskVarreduraExterna, 
						ModoVarreduraActivity.this.delayVarredura, ModoVarreduraActivity.this.delayVarredura);				

			}			
		};
		
		cit.execute(init_page, CarregarImagensTelas.OPCAO_CARREGAR_IMAGENS, current_categoriaId);
		
		// aqui carregam-se as imagens-comandos que sao atalhos
		CarregarImagensComandos cic = new CarregarImagensComandos()
		{

			// metodo que roda na UI Thread antes da atividade em background
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				activeContext = ModoVarreduraActivity.this;
				gridview 	= ModoVarreduraActivity.this.gridview_atalhos;
			}
			
		};
		
		cic.execute(CarregarImagensComandos.OPCAO_CARREGAR_ATALHOS); // true: mostrar atalhos
		
		// aqui carrega-se o service de reprodu��o de som
		sservice_intent = new Intent(this, SoundService.class);
		
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		taskVarreduraExterna.cancel();
		taskVarreduraInterna.cancel();
		animationTimer.cancel();
	}
	
	// essa funcao vai de um estado para outro, aqui especificamente na varredura externa
	private int transicaoIteracaoExternaPartindoDe(int partida)
	{
		resetIndices(); // ao realizar a transicao, reseta-se os indices
		int estado = ESTADO_TODOS_DESATIVADOS;
		
		if (partida == ESTADO_GRIDVIEW_PRINCIPAL_ATIVO) // partindo de gridview principal
		{	
			/*
			 * se o botao mostrar/esconder comandos estiver em modo mostrar comandos,
			 * seguir o caminho normalmente.
			 * Se n�o, pula o botao proxima tela e 
			 * verifica se o gridview frase est� vazio, se estiver ent�o pula deste
			 * para atalhos
			 * */
			if (esconderComandos)
				estado = ((lista_imagens_frase.isEmpty())) ? ESTADO_GRIDVIEW_ATALHOS_ATIVO : ESTADO_GRIDVIEW_FRASE_ATIVO;
			else
				estado = ESTADO_BUTTON_PROXIMA_TELA_ATIVO;
		}
		else if (partida == ESTADO_BUTTON_PROXIMA_TELA_ATIVO) // partindo de botao proxima tela
			/*
			 * Verifica se o gridview frase est� vazio, se estiver ent�o pula deste
			 * para atalhos
			 * */
			estado = ((lista_imagens_frase.isEmpty())) ? ESTADO_GRIDVIEW_ATALHOS_ATIVO : ESTADO_GRIDVIEW_FRASE_ATIVO;
		else if (partida == ESTADO_GRIDVIEW_FRASE_ATIVO) // partindo de gridview frase
			estado = ESTADO_GRIDVIEW_ATALHOS_ATIVO;
		else if (partida == ESTADO_GRIDVIEW_ATALHOS_ATIVO) // partindo de gridview atalhos
			estado = ESTADO_BUTTON_MOSTRAR_ESCONDER_COMANDOS_ATIVO;
		else if (partida == ESTADO_BUTTON_MOSTRAR_ESCONDER_COMANDOS_ATIVO) // partindo de botao mostrar/esconder comandos
			estado = ESTADO_GRIDVIEW_PRINCIPAL_ATIVO;

		return estado;
		
	}
	
	// essa funcao vai de um estado para outro, aqui especificamente na varredura interna
	private int transicaoIteracaoInternaPartindoDe(int partida)
	{
		resetIndices();
		int estado = ESTADO_TODOS_DESATIVADOS;
		
		if (partida == ESTADO_ITERANDO_ITENS_GRIDVIEW_PRINCIPAL)
			estado = ESTADO_GRIDVIEW_PRINCIPAL_ATIVO;
		else if (partida == ESTADO_GRIDVIEW_PRINCIPAL_ATIVO)
			estado = ESTADO_ITERANDO_ITENS_GRIDVIEW_PRINCIPAL;
		
		if (partida == ESTADO_ITERANDO_ITENS_GRIDVIEW_FRASE)
			estado = ESTADO_GRIDVIEW_FRASE_ATIVO;
		else if (partida == ESTADO_GRIDVIEW_FRASE_ATIVO)
			estado = ESTADO_ITERANDO_ITENS_GRIDVIEW_FRASE;
		
		return estado;
	}
	
	// ir da iteracao externa para interna
	private int transicaoEntrarPartindoDe(int partida)
	{
		int estado = ESTADO_TODOS_DESATIVADOS;
		
		if (partida == ESTADO_GRIDVIEW_PRINCIPAL_ATIVO)
			estado = ESTADO_ITERANDO_ITENS_GRIDVIEW_PRINCIPAL;
		if (partida == ESTADO_GRIDVIEW_FRASE_ATIVO)
			estado = ESTADO_ITERANDO_ITENS_GRIDVIEW_FRASE;
		
		return estado;
	}

	// ir da iteracao interna para externa
	private int transicaoSairPartindoDe(int partida)
	{
		int estado = ESTADO_GRIDVIEW_PRINCIPAL_ATIVO;
		
		if (partida == ESTADO_ITERANDO_ITENS_GRIDVIEW_PRINCIPAL)
			estado = ESTADO_GRIDVIEW_PRINCIPAL_ATIVO;
		else if (partida == ESTADO_GRIDVIEW_PRINCIPAL_ATIVO)
			estado = partida;
		
		if (partida == ESTADO_ITERANDO_ITENS_GRIDVIEW_FRASE)
			estado = ESTADO_GRIDVIEW_FRASE_ATIVO;
		else if (partida == ESTADO_GRIDVIEW_FRASE_ATIVO)
			estado = partida;
		
		return estado;
	}
	
	// metodo que descreve os estados
	protected void setEstadoVarredura(int estado)
	{
		LinearLayout llFrase = (LinearLayout) findViewById(R.id.frase);
		LinearLayout llAtalhos = (LinearLayout) findViewById(R.id.atalhos);
		
		LinearLayout llShowHideCommands = (LinearLayout) findViewById(R.id.show_hide_commands);
		LinearLayout llNext				= (LinearLayout) findViewById(R.id.next);
		
		// estados de 1 a 5 	 : varredura externa
		// estados maiores que 5 : varredura interna
		if (estado == ESTADO_TODOS_DESATIVADOS) // todos desativados
		{
			setBorda(gridview, false);
			setBorda(llFrase, false);
			setBorda(llAtalhos, false);
			setBorda(llNext, false);
			setBorda(llShowHideCommands, false);
			
			iteracaoInternaPrincipalAtiva = iteracaoInternaFraseAtiva = false;
		}
		else if (estado == ESTADO_GRIDVIEW_PRINCIPAL_ATIVO) // gridview principal ativo
		{
			setBorda(gridview, true);
			setBorda(llFrase, false);
			setBorda(llAtalhos, false);
			setBorda(llNext, false);
			setBorda(llShowHideCommands, false);
		}
		else if (estado == ESTADO_GRIDVIEW_FRASE_ATIVO) // gridview frase ativo
		{
			setBorda(llFrase, true);
			setBorda(gridview, false);
			setBorda(llAtalhos, false);
			setBorda(llNext, false);
			setBorda(llShowHideCommands, false);
		}
		else if (estado == ESTADO_GRIDVIEW_ATALHOS_ATIVO) // gridview atalhos ativo
		{
			setBorda(llAtalhos, true);
			setBorda(gridview, false);
			setBorda(llFrase, false);
			setBorda(llNext, false);
			setBorda(llShowHideCommands, false);
		}
		else if (estado == ESTADO_BUTTON_MOSTRAR_ESCONDER_COMANDOS_ATIVO) // botao mostrar/esconder comandos ativo
		{
			setBorda(llShowHideCommands, true);
			setBorda(gridview, false);
			setBorda(llFrase, false);
			setBorda(llAtalhos, false);
			setBorda(llNext, false);			
		}
		else if (estado == ESTADO_BUTTON_PROXIMA_TELA_ATIVO) // botao ir para proxima tela ativo
		{
			setBorda(llNext, true);
			setBorda(gridview, false);
			setBorda(llFrase, false);
			setBorda(llAtalhos, false);
			setBorda(llShowHideCommands, false);			
		}
		else if (estado == ESTADO_ITERANDO_ITENS_GRIDVIEW_PRINCIPAL) // iterando em gridview Principal 
		{
			setEstadoVarredura(ESTADO_TODOS_DESATIVADOS);
			iteracaoInternaPrincipalAtiva = true;
		}
		else if (estado == ESTADO_ITERANDO_ITENS_GRIDVIEW_FRASE) // iterando em gridview Frase
		{
			setEstadoVarredura(ESTADO_TODOS_DESATIVADOS);
			iteracaoInternaFraseAtiva = true;
		}
		
		estadoAtual = estado;
			
	}
	
	protected void iteracaoTimerGridViewPrincipal()
	{
		if (!(indiceItemPrincipal == gridview.getChildCount()))
		{
			
			// ir removendo a borda da imagem anterior
			if (indiceItemPrincipal > 0)
			{
				ImgItem iii = (ImgItem) gridview.getChildAt(indiceItemPrincipal - 1);
				setBorda(iii, false);
			}
			
			ImgItem ii = (ImgItem) gridview.getChildAt(indiceItemPrincipal++);
				
			setBorda(ii, true);
		}
		else
		{
			// quando chegar no ultimo item, tirar borda dele
			ImgItem ii = (ImgItem) gridview.getChildAt(indiceItemPrincipal++ - 1);
			setBorda(ii, false);
		}
			
	}
	
	private void iteracaoTimerGridViewFrase()
	{
		GridView gv  = this.gridview_frase;
		if (!(indiceItemFrase == gv.getChildCount()))
		{
			// ir removendo a borda da imagem anterior
			if (indiceItemFrase > 0)
			{
				ImgItem iii = (ImgItem) gv.getChildAt(indiceItemFrase - 1);
				setBorda(iii, false);
			}
			
			ImgItem ii = (ImgItem) gv.getChildAt(indiceItemFrase++);
				
			setBorda(ii, true);
		}
		else
		{
			// quando chegar no ultimo item, tirar borda dele
			ImgItem ii = (ImgItem) gv.getChildAt(indiceItemFrase++ - 1);
			setBorda(ii, false);			
		}
	}	
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void setBorda(View v, boolean s)
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		int cor = Integer.parseInt(sp.getString(Opcoes.COR_BORDA_KEY, "" + Opcoes.COR_BORDA_DEFAULT));
		
		switch (cor)
		{
			case Opcoes.BORDA_PRETA:
				
				if (s)
				{
					GradientDrawable borda_preta_dg = (GradientDrawable) getResources().getDrawable(R.drawable.borda_preta);
					borda_preta_dg.setColor(getColor((ColorDrawable) v.getTag()));
					if (Build.VERSION.SDK_INT < 16)
						v.setBackgroundDrawable(borda_preta_dg);
					else 
						v.setBackground(borda_preta_dg);
				}
				else
				{
					if (Build.VERSION.SDK_INT < 16)
						v.setBackgroundDrawable((Drawable) v.getTag());
					else 
						v.setBackground((Drawable) v.getTag());
				}
				break;
		
			case Opcoes.BORDA_VERMELHA:
			default:
				if (s)
				{
					GradientDrawable borda_vermelha_dg = (GradientDrawable) getResources().getDrawable(R.drawable.borda);
					borda_vermelha_dg.setColor(getColor((ColorDrawable) v.getTag()));
					if (Build.VERSION.SDK_INT < 16)
						v.setBackgroundDrawable(borda_vermelha_dg);
					else 
						v.setBackground(borda_vermelha_dg);
				}
				else
				{
					if (Build.VERSION.SDK_INT < 16)
						v.setBackgroundDrawable((Drawable) v.getTag());
					else 
						v.setBackground((Drawable) v.getTag());
				}				
		}
	}
	
	private void setBorda(ImgItem imgi, boolean s)
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		int cor = Integer.parseInt(sp.getString(Opcoes.COR_BORDA_KEY, "" + Opcoes.COR_BORDA_DEFAULT));
		
		switch (cor)
		{
			case Opcoes.BORDA_PRETA:
				imgi.setBackgroundResource((s) ? R.drawable.borda_preta : 0);
				break;
		
			case Opcoes.BORDA_VERMELHA:
			default:
				imgi.setBackgroundResource((s) ? R.drawable.borda : 0);
				
		}
	}
	
	protected void armazenarBackgroundDosComponentes()
	{
		LinearLayout llFrase = (LinearLayout) findViewById(R.id.frase);
		LinearLayout llAtalhos = (LinearLayout) findViewById(R.id.atalhos);
		
		LinearLayout llShowHideCommands = (LinearLayout) findViewById(R.id.show_hide_commands);
		LinearLayout llNext				= (LinearLayout) findViewById(R.id.next);
		
		llFrase.setTag(llFrase.getBackground());
		
		llAtalhos.setTag(llAtalhos.getBackground());
		llShowHideCommands.setTag(llShowHideCommands.getBackground());
		llNext.setTag(llNext.getBackground());
		gridview.setTag(gridview.getBackground());

	}
	
	@SuppressLint("NewApi")
	protected int getColor(ColorDrawable colorDrawable)
	{
		int corDoDrawable = 0;
		
		if (Build.VERSION.SDK_INT < 11)
		{
			Bitmap bitmap = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			colorDrawable.draw(canvas);
			corDoDrawable = bitmap.getPixel(0, 0);
			bitmap.recycle();
		}
		else
			corDoDrawable = colorDrawable.getColor();
		
		return corDoDrawable;
	}
	
	protected void resetIndices()
	{
		indiceItemPrincipal = indiceItemFrase = 0;
	}
	
	// alternar varredura de externa pra interna ou vice versa
	protected void alternarVarredura()
	{
		if (iteracaoExternaAtiva)
		{
			iteracaoInternaAtiva = true;
			iteracaoExternaAtiva = false;
		}
		else if (iteracaoInternaAtiva)
		{
			iteracaoExternaAtiva = true;
			iteracaoInternaAtiva = false;
		}
		else // default
		{
			iteracaoExternaAtiva = true;
			iteracaoInternaAtiva = false;			
		}
	}
	
	// alterna entre evento mostrar comandos e evento esconder comandos no bot�o
	// btnShowHideCommands
	protected void alternarEventoBtnShowHideCommands()
	{
		if (mostrarComandos)
		{
			esconderComandos = true;
			mostrarComandos  = false;
		}
		else if (esconderComandos)
		{
			mostrarComandos  = true;
			esconderComandos = false;
		}
		else // default
		{
			mostrarComandos  = true;
			esconderComandos = false;
		}
	}
	
	// alterna entre evento adicionar imagem a frase e evento acionar comando no
	// gridview principal	
	protected void alternarEventoAoSelecionarImagemDeGridViewPrincipal()
	{
		if (adicionarImagemFrase)
		{
			acionarComando		 = true;
			adicionarImagemFrase = false;
		}
		else if (acionarComando)
		{
			adicionarImagemFrase = true;
			acionarComando 		 = false;
		}
		else // default
		{
			adicionarImagemFrase = true;
			acionarComando 		 = false;
		}
			
	}
	
	// transi��o da varredura externa para interna
	protected void entrar()
	{
		GridView gvf = gridview_frase;
		
		// se gridview frase n�o contiver itens, n�o entra
		if (estadoAtual == ESTADO_GRIDVIEW_FRASE_ATIVO && !(hasItens(gvf)))
			return;
		
		setEstadoVarredura(transicaoEntrarPartindoDe(estadoAtual));
		alternarVarredura();
		taskVarreduraExterna.cancel();
		animationTimer.schedule(taskVarreduraInterna, 0, delayVarredura);
		
		// recriando task
		taskVarreduraExterna = new TimerTask()
		{

			@Override
			public void run()
			{
				runOnUiThread(new Task1());
			}
			
		};		
	}

	// transi��o da varredura interna para externa
	protected void sair()
	{
		setEstadoVarredura(transicaoSairPartindoDe(estadoAtual));
		alternarVarredura();
		taskVarreduraInterna.cancel();
		animationTimer.schedule(taskVarreduraExterna, 0, delayVarredura);
		// recriando task
		taskVarreduraInterna = new TimerTask()
		{

			@Override
			public void run()
			{
				runOnUiThread(new Task2());
			}
			
		};		
	}
	
	protected class Task1 implements Runnable // codigo do timer task externo
	{

		@Override
		public void run()
		{
			setEstadoVarredura(transicaoIteracaoExternaPartindoDe(estadoAtual));
		}
		
	}
	
	protected class Task2 implements Runnable // codigo do timer task interno
	{

		@Override
		public void run()
		{
			int gridview_items_count;
			
			// a iteracao ocorre entre o conte�do do controle e o pr�prio controle
			
			if (
					estadoAtual == ESTADO_GRIDVIEW_PRINCIPAL_ATIVO 
					|| estadoAtual == ESTADO_GRIDVIEW_FRASE_ATIVO 
					|| estadoAtual == ESTADO_GRIDVIEW_ATALHOS_ATIVO
				)
				setEstadoVarredura(transicaoIteracaoInternaPartindoDe(estadoAtual));
			
			// Varre GridView Principal
			if 	(   	
					(iteracaoInternaPrincipalAtiva) 
					&& 	((gridview_items_count = gridview.getChildCount()) > 0) 
					&& 	(indiceItemPrincipal <= gridview_items_count) 
				)
			{
				
				iteracaoTimerGridViewPrincipal();
				if (indiceItemPrincipal == (gridview_items_count + 1))
					setEstadoVarredura(transicaoIteracaoInternaPartindoDe(ESTADO_ITERANDO_ITENS_GRIDVIEW_PRINCIPAL));
			}

			// Varre GridView Frase							
			if 	( 		
					(iteracaoInternaFraseAtiva) 
					&& 	((gridview_items_count = gridview_frase.getChildCount()) > 0) 
					&& 	(indiceItemFrase <= gridview_items_count) 
				)
			{
				iteracaoTimerGridViewFrase();
				if (indiceItemFrase == (gridview_items_count + 1))
					setEstadoVarredura(transicaoIteracaoInternaPartindoDe(ESTADO_ITERANDO_ITENS_GRIDVIEW_FRASE));
			}		
			
		}			
	}
	
	
	protected void onRestartSuper()
	{
		super.onRestart();
	}
	
	// metodo que intercepta os clicks na tela
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		// verifica se acao do touch � aquela quando "solta-se" o dedo da tela.
		if (ev.getAction() == MotionEvent.ACTION_UP)
			acaoDoEventoPrincipal();
		return true;
	}	
		
}
	
	

