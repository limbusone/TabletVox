package com.example.tabletvox03f;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.tabletvox03f.dal.AssocImagemSom;
import com.example.tabletvox03f.dal.CarregarImagensComandos;
import com.example.tabletvox03f.dal.CarregarImagensTelas;
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
	
	protected void acaoDoEventoPrincipal()
	{
		
		if (estadoAtual == 1 || estadoAtual == 2 /*|| estadoAtual == 3*/)
		{
			
			if (iteracaoExternaAtiva)
				entrar();
			else if (iteracaoInternaAtiva)
				sair();			
		
		}
		else if (estadoAtual == 3) // atalho
		{
			GridView gva = (GridView) findViewById(R.id.gridview_atalhos);
			ImgItem imgi = (ImgItem) gva.getChildAt(0);
			
			int cmd = imgi.getAssocImagemSom().getCmd();
			// tocar som frase
			acionarComando(cmd);
		
		}
		else if (estadoAtual == 4) // mostrar comandos / esconder comandos
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
				cixmlc.execute(false); // false: mostrar todos os comandos
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
				
				cixml.execute(current_page, 0, current_categoriaId);					
			}
			alternarEventoBtnShowHideCommands();
			alternarEventoAoSelecionarImagemDeGridViewPrincipal();				
		}
		else if (estadoAtual == 5) // proxima pagina
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
			vaiParaProximaPagina(cixml, 0, current_categoriaId);
		}
		else if (estadoAtual == 6) // adicionar imagem a frase / acionar comando
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
		else if (estadoAtual == 7) // remover imagem da frase
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
		//setContentView(R.layout.activity_modo_varredura);
		setContentView(R.layout.telas_interface);
		
		Utils.MODO_ATIVO = "modo_varredura";
		
		current_categoriaId = (int) getIntent().getLongExtra("categoriaId", 0);
		
		// inicializa paginação
		current_page = init_page = 1;
		//final_page = (new XmlUtilsTelas(this, Utils.TELAS_NOME_ARQUIVO_XML_ATIVO, "root")).getLastPage();
		final_page = 1;
		
		delayVarredura = Opcoes.getIntervalo_tempo_varredura();
		
		// inicializando variaveis booleanas com valores default
		alternarVarredura();
		alternarEventoBtnShowHideCommands();
		alternarEventoAoSelecionarImagemDeGridViewPrincipal();
		
		setEstadoVarredura(1);
		
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
		
		cixml.execute(init_page, 0, current_categoriaId);
		
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
		
		cixmlc.execute(true); // true: mostrar atalhos
		
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
		int estado = 0;
		
		if (partida == 1) // partindo de gridview principal
		{	
			/*
			 * se o botao mostrar/esconder comandos estiver em modo mostrar comandos,
			 * seguir o caminho normalmente.
			 * Se não, pula o botao proxima tela e 
			 * verifica se o gridview frase está vazio, se estiver então pula deste
			 * para atalhos
			 * */
			if (esconderComandos)
				estado = ((lista_imagens_frase.isEmpty())) ? 3 : 2;
			else
				estado = 5;
		}
		else if (partida == 5) // partindo de botao proxima tela
			/*
			 * Verifica se o gridview frase está vazio, se estiver então pula deste
			 * para atalhos
			 * */
			estado = ((lista_imagens_frase.isEmpty())) ? 3 : 2;
		else if (partida == 2) // partindo de gridview frase
			estado = 3;
		else if (partida == 3) // partindo de gridview atalhos
			estado = 4;
		else if (partida == 4) // partindo de botao mostrar/esconder comandos
			estado = 1;

		return estado;
		
	}
	
	// essa funcao vai de um estado para outro, aqui especificamente na varredura interna
	private int transicaoIteracaoInternaPartindoDe(int partida)
	{
		resetIndices();
		int estado = 0;
		
		if (partida == 6)
			estado = 1;
		else if (partida == 1)
			estado = 6;
		
		if (partida == 7)
			estado = 2;
		else if (partida == 2)
			estado = 7;
		
		return estado;
	}
	
	// ir da iteracao externa para interna
	private int transicaoEntrarPartindoDe(int partida)
	{
		int estado = 0;
		
		if (partida == 1)
			estado = 6;
		if (partida == 2)
			estado = 7;
		
		return estado;
	}

	// ir da iteracao interna para externa
	private int transicaoSairPartindoDe(int partida)
	{
		int estado = 1;
		
		if (partida == 6)
			estado = 1;
		else if (partida == 1)
			estado = partida;
		
		if (partida == 7)
			estado = 2;
		else if (partida == 2)
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
		if (estado == 0) // todos desativados
		{
			setBorda(gridview, false);
			setBorda(llFrase, false);
			setBorda(llAtalhos, false);
			setBorda(llNext, false);
			setBorda(llShowHideCommands, false);
			
			iteracaoInternaPrincipalAtiva = iteracaoInternaFraseAtiva = false;
		}
		else if (estado == 1) // gridview principal ativo
		{
			setBorda(gridview, true);
			setBorda(llFrase, false);
			setBorda(llAtalhos, false);
			setBorda(llNext, false);
			setBorda(llShowHideCommands, false);
		}
		else if (estado == 2) // gridview frase ativo
		{
			setBorda(llFrase, true);
			setBorda(gridview, false);
			setBorda(llAtalhos, false);
			setBorda(llNext, false);
			setBorda(llShowHideCommands, false);
		}
		else if (estado == 3) // gridview atalhos ativo
		{
			setBorda(llAtalhos, true);
			setBorda(gridview, false);
			setBorda(llFrase, false);
			setBorda(llNext, false);
			setBorda(llShowHideCommands, false);
		}
		else if (estado == 4) // botao mostrar/esconder comandos ativo
		{
			setBorda(llShowHideCommands, true);
			setBorda(gridview, false);
			setBorda(llFrase, false);
			setBorda(llAtalhos, false);
			setBorda(llNext, false);			
		}
		else if (estado == 5) // botao ir para proxima tela ativo
		{
			setBorda(llNext, true);
			setBorda(gridview, false);
			setBorda(llFrase, false);
			setBorda(llAtalhos, false);
			setBorda(llShowHideCommands, false);			
		}
		else if (estado == 6) // iterando em gridview Principal 
		{
			setEstadoVarredura(0);
			iteracaoInternaPrincipalAtiva = true;
		}
		else if (estado == 7) // iterando em gridview Frase
		{
			setEstadoVarredura(0);
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
		v.setBackgroundResource((s) ? R.drawable.borda : 0);
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
		if (estadoAtual == 2 && !(hasItens(gvf)))
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
			
			if (estadoAtual == 1 || estadoAtual == 2 || estadoAtual == 3)
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
					setEstadoVarredura(transicaoIteracaoInternaPartindoDe(6));
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
					setEstadoVarredura(transicaoIteracaoInternaPartindoDe(7));
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
	
	

