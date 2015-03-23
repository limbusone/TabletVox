package com.example.tabletvox03f;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.SharedPreferences;
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
 * 		percorrimento da borda vermelha pelo conteúdo dos controles:
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
		// obs: a comparação estadoAtual == 3 está comentada porque a ideia também era que
		// houvesse mais de um atalho porém foi decidido somente um atalho
		if (estadoAtual == ESTADO_GRIDVIEW_PRINCIPAL_ATIVO || estadoAtual == ESTADO_GRIDVIEW_FRASE_ATIVO /*|| estadoAtual == 3*/)
		{
			
			if (iteracaoExternaAtiva)
				entrar();
			else if (iteracaoInternaAtiva)
				sair();			
		
		}
		else if (estadoAtual == ESTADO_GRIDVIEW_ATALHOS_ATIVO) // atalho
		{
			GridView gva = (GridView) findViewById(R.id.gridview_atalhos);
			ImgItem imgi = (ImgItem) gva.getChildAt(0);
			
			int cmd = imgi.getAssocImagemSom().getCmd();
			// tocar som frase
			acionarComando(cmd);
		
		}
		else if (estadoAtual == ESTADO_BUTTON_MOSTRAR_ESCONDER_COMANDOS_ATIVO) // mostrar comandos / esconder comandos
		{
			if (mostrarComandos)
			{
				CarregarImagensComandos cixmlc = new CarregarImagensComandos()
				{

					// metodo que roda na UI Thread antes da atividade em background
					@Override
					protected void onPreExecute()
					{
						super.onPreExecute();
						activeContext = ModoVarreduraActivity.this;
						gridview 	= (GridView) ModoVarreduraActivity.this.findViewById(R.id.gridview);
						pgrbar		= (ProgressBar) ModoVarreduraActivity.this.findViewById(R.id.progressBar1);
						
						pgrbar.setVisibility(View.VISIBLE);							
					}
					
				};
				cixmlc.execute(CarregarImagensComandos.OPCAO_CARREGAR_TODOS_COMANDOS); // false: mostrar todos os comandos
			} 
			else if (esconderComandos)
			{
				CarregarImagensTelas cixml = new CarregarImagensTelas()
				{
					// metodo que roda na UI Thread antes da atividade em background
					@Override
					protected void onPreExecute()
					{
						super.onPreExecute();
						activeContext = ModoVarreduraActivity.this;
						gridview 	= (GridView) ModoVarreduraActivity.this.findViewById(R.id.gridview);
						pgrbar		= (ProgressBar) ModoVarreduraActivity.this.findViewById(R.id.progressBar1);
						
						pgrbar.setVisibility(View.VISIBLE);									
					}
			
				};
				
				cixml.execute(current_page, CarregarImagensTelas.OPCAO_CARREGAR_IMAGENS, current_categoriaId);					
			}
			alternarEventoBtnShowHideCommands();
			alternarEventoAoSelecionarImagemDeGridViewPrincipal();				
		}
		else if (estadoAtual == ESTADO_BUTTON_PROXIMA_TELA_ATIVO) // proxima pagina
		{
			CarregarImagensTelas cixml = new CarregarImagensTelas()
			{
				// metodo que roda na UI Thread antes da atividade em background
				@Override
				protected void onPreExecute()
				{
					super.onPreExecute();
					activeContext = ModoVarreduraActivity.this;
					gridview 	= (GridView) ModoVarreduraActivity.this.findViewById(R.id.gridview);
					pgrbar		= (ProgressBar) ModoVarreduraActivity.this.findViewById(R.id.progressBar1);
					
					pgrbar.setVisibility(View.VISIBLE);								
				}
		
			};
			vaiParaProximaPagina(cixml, CarregarImagensTelas.OPCAO_CARREGAR_IMAGENS, current_categoriaId);
		}
		else if (estadoAtual == ESTADO_ITERANDO_ITENS_GRIDVIEW_PRINCIPAL) // adicionar imagem a frase / acionar comando
		{
			GridView gv = (GridView) findViewById(R.id.gridview);
			
			int indiceAtual = indiceItemPrincipal - 1;
			indiceAtual = (indiceAtual < 0) ? 0 : indiceAtual;
			ImgItem imgi = (ImgItem) gv.getChildAt(indiceAtual);
			
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
			
			// se não houver mais imagens, vai para varredura externa
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
		
		// inicializa paginação
		current_page = init_page = 1;
		//final_page = (new XmlUtilsTelas(this, Utils.TELAS_NOME_ARQUIVO_XML_ATIVO, "root")).getLastPage();
		final_page = 1;
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		delayVarredura = Integer.parseInt(sp.getString("intervalo_tempo_varredura", "" + Opcoes.INTERVALO_TEMPO_VARREDURA_DEFAULT));;
		
		// muda titulo conforme categoria
		setCurrentTitle(Utils.PERFIL_ATIVO.getNome() + " - Categoria " + 
		Utils.PERFIL_ATIVO.getCategoriaById(current_categoriaId).getNome());		
		
		// inicializando variaveis booleanas com valores default
		alternarVarredura();
		alternarEventoBtnShowHideCommands();
		alternarEventoAoSelecionarImagemDeGridViewPrincipal();
		
		setEstadoVarredura(ESTADO_GRIDVIEW_PRINCIPAL_ATIVO);
		
		if (!(copiarFraseGlobalParaFraseLocal()))
			lista_imagens_frase = new ArrayList<ImgItem>();
		else // transferindo as imagens globais para o gridview frase 
			((GridView) findViewById(R.id.gridview_frase))
			.setAdapter(new ImageAdapterFrase(lista_imagens_frase));

		animationTimer = new Timer();
		
		// task do timer de varredura externa
		taskVarreduraExterna = new TimerTask()
		{

			@Override
			public void run()
			{	
				// o timer tem que rodar na UI Thread porque se não não funciona!
				runOnUiThread(new Task1());
			}
			
		};
		
		taskVarreduraInterna = new TimerTask()
		{

			@Override
			public void run()
			{
				// o timer tem que rodar na UI Thread porque se não não funciona!
				runOnUiThread(new Task2());				
			}

		};
		
		// aqui carregam-se as imagens da primeira pagina
		CarregarImagensTelas cixml = new CarregarImagensTelas()
		{
			// metodo que roda na UI Thread antes da atividade em background
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				activeContext = ModoVarreduraActivity.this;
				gridview 	= (GridView) ModoVarreduraActivity.this.findViewById(R.id.gridview);
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
		
		cixml.execute(init_page, CarregarImagensTelas.OPCAO_CARREGAR_IMAGENS, current_categoriaId);
		
		// aqui carregam-se as imagens-comandos que sao atalhos
		CarregarImagensComandos cixmlc = new CarregarImagensComandos()
		{

			// metodo que roda na UI Thread antes da atividade em background
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				activeContext = ModoVarreduraActivity.this;
				gridview 	= (GridView) ModoVarreduraActivity.this.findViewById(R.id.gridview_atalhos);
			}
			
		};
		
		cixmlc.execute(CarregarImagensComandos.OPCAO_CARREGAR_ATALHOS); // true: mostrar atalhos
		
		// aqui carrega-se o service de reprodução de som
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
			 * Se não, pula o botao proxima tela e 
			 * verifica se o gridview frase está vazio, se estiver então pula deste
			 * para atalhos
			 * */
			if (esconderComandos)
				estado = ((lista_imagens_frase.isEmpty())) ? ESTADO_GRIDVIEW_ATALHOS_ATIVO : ESTADO_GRIDVIEW_FRASE_ATIVO;
			else
				estado = ESTADO_BUTTON_PROXIMA_TELA_ATIVO;
		}
		else if (partida == ESTADO_BUTTON_PROXIMA_TELA_ATIVO) // partindo de botao proxima tela
			/*
			 * Verifica se o gridview frase está vazio, se estiver então pula deste
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
		
		
		GridView gridview = (GridView) findViewById(R.id.gridview);
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
		GridView gv  = (GridView) findViewById(R.id.gridview);
		if (!(indiceItemPrincipal == gv.getChildCount()))
		{
			
			// ir removendo a borda da imagem anterior
			if (indiceItemPrincipal > 0)
			{
				ImgItem iii = (ImgItem) gv.getChildAt(indiceItemPrincipal - 1);
				setBorda(iii, false);
			}
			
			ImgItem ii = (ImgItem) gv.getChildAt(indiceItemPrincipal++);
				
			setBorda(ii, true);
		}
		else
		{
			// quando chegar no ultimo item, tirar borda dele
			ImgItem ii = (ImgItem) gv.getChildAt(indiceItemPrincipal++ - 1);
			setBorda(ii, false);
		}
			
	}
	
	private void iteracaoTimerGridViewFrase()
	{
		GridView gv  = (GridView) findViewById(R.id.gridview_frase);
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
	
	private void setBorda(View v, boolean s)
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		int cor = Integer.parseInt(sp.getString("cor_borda", "" + Opcoes.BORDA_VERMELHA));
		
		switch (cor)
		{
			case Opcoes.BORDA_PRETA:
				v.setBackgroundResource((s) ? R.drawable.borda_preta : 0);
				break;
		
			case Opcoes.BORDA_VERMELHA:
			default:
				v.setBackgroundResource((s) ? R.drawable.borda : 0);
				
		}
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
	
	// alterna entre evento mostrar comandos e evento esconder comandos no botão
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
	
	// transição da varredura externa para interna
	protected void entrar()
	{
		GridView gvf = (GridView) findViewById(R.id.gridview_frase);
		
		// se gridview frase não contiver itens, não entra
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

	// transição da varredura interna para externa
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
			GridView gv = (GridView) findViewById(R.id.gridview);
			GridView gvf = (GridView) findViewById(R.id.gridview_frase);
			int cc;
			
			// a iteracao ocorre entre o conteúdo do controle e o próprio controle
			
			if (estadoAtual == ESTADO_GRIDVIEW_PRINCIPAL_ATIVO || estadoAtual == ESTADO_GRIDVIEW_FRASE_ATIVO || estadoAtual == ESTADO_GRIDVIEW_ATALHOS_ATIVO)
				setEstadoVarredura(transicaoIteracaoInternaPartindoDe(estadoAtual));
			
			// Varre GridView Principal
			if 	(   	
					(iteracaoInternaPrincipalAtiva) 
					&& 	((cc = gv.getChildCount()) > 0) 
					&& 	(indiceItemPrincipal <= cc) 
				)
			{
				
				iteracaoTimerGridViewPrincipal();
				if (indiceItemPrincipal == (cc + 1))
					setEstadoVarredura(transicaoIteracaoInternaPartindoDe(ESTADO_ITERANDO_ITENS_GRIDVIEW_PRINCIPAL));
			}

			// Varre GridView Frase							
			if 	( 		
					(iteracaoInternaFraseAtiva) 
					&& 	((cc = gvf.getChildCount()) > 0) 
					&& 	(indiceItemFrase <= cc) 
				)
			{
				iteracaoTimerGridViewFrase();
				// transicao para GridViewComandos
				if (indiceItemFrase == (cc + 1))
					setEstadoVarredura(transicaoIteracaoInternaPartindoDe(ESTADO_ITERANDO_ITENS_GRIDVIEW_FRASE));
			}		
			
		}			
	}
	
	
	protected void onRestartSuper()
	{
		super.onRestart();
	}
	
	@Override
	protected void onRestart()
	{
		super.onRestart();
		
		if (!(copiarFraseGlobalParaFraseLocal()))
			lista_imagens_frase = new ArrayList<ImgItem>();
		else // transferindo as imagens globais para o gridview frase 
			((GridView) findViewById(R.id.gridview_frase))
			.setAdapter(new ImageAdapterFrase(lista_imagens_frase));
	}		
	
	// metodo que intercepta os clicks na tela
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		// verifica se acao do touch é aquela quando "solta-se" o dedo da tela.
		if (ev.getAction() == MotionEvent.ACTION_UP)
			acaoDoEventoPrincipal();
		return true;
	}	
		
}
	
	

