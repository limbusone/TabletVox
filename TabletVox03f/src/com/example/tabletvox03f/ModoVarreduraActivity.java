package com.example.tabletvox03f;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.AssocImagemSom;
import com.example.tabletvox03f.dal.CarregarImagensComandos;
import com.example.tabletvox03f.dal.CarregarImagensTelas;
import com.example.tabletvox03f.dal.XmlUtilsTelas;
import com.example.tabletvox03f.management.Opcoes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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
	
	private int estadoAtual;
	
	private Timer animationTimer;
	private int delayVarredura;
	private int indiceItemPrincipal;
	private int indiceItemFrase;
	private boolean iteracaoExternaAtiva;
	private boolean iteracaoInternaAtiva;
	
		
	private boolean iteracaoInternaPrincipalAtiva;
	private boolean iteracaoInternaFraseAtiva;
	private TimerTask taskVarreduraExterna;
	private TimerTask taskVarreduraInterna;
	
	
	private boolean mostrarComandos;
	private boolean esconderComandos;
	
	private boolean adicionarImagemFrase;
	private boolean	acionarComando;
	
	// evento principal relativo � localiza��o da borda vermelha
	private OnClickListener principalEvento = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			
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
				if (cmd == 1) // tocar som frase
					//Comandos.tocarSomFrase();
					tocarSomFrase();
			
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
//							wview 		= (WebView) ModoVarreduraActivity.this.findViewById(R.id.webview);
							pgrbar		= (ProgressBar) ModoVarreduraActivity.this.findViewById(R.id.progressBar1);
							
							pgrbar.setVisibility(View.VISIBLE);							
							//WebView view = (WebView) MainActivity.this.findViewById(R.id.webview);
//							wview.loadUrl("file:///android_asset/loading.gif");
//							wview.setVisibility(View.VISIBLE);
							//gridview = (GridView) MainActivity.this.findViewById(R.id.gridview);
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
//							wview 		= (WebView) ModoVarreduraActivity.this.findViewById(R.id.webview);
							pgrbar		= (ProgressBar) ModoVarreduraActivity.this.findViewById(R.id.progressBar1);
							
							pgrbar.setVisibility(View.VISIBLE);									
							//WebView view = (WebView) MainActivity.this.findViewById(R.id.webview);
//							wview.loadUrl("file:///android_asset/loading.gif");
//							wview.setVisibility(View.VISIBLE);
							//gridview = (GridView) MainActivity.this.findViewById(R.id.gridview);
						}
				
					};
					
					cixml.execute(current_page);					
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
//						wview 		= (WebView) ModoVarreduraActivity.this.findViewById(R.id.webview);
						pgrbar		= (ProgressBar) ModoVarreduraActivity.this.findViewById(R.id.progressBar1);
						
						pgrbar.setVisibility(View.VISIBLE);								
						//WebView view = (WebView) MainActivity.this.findViewById(R.id.webview);
//						wview.loadUrl("file:///android_asset/loading.gif");
//						wview.setVisibility(View.VISIBLE);
						//gridview = (GridView) MainActivity.this.findViewById(R.id.gridview);
					}
			
				};
				//Comandos.vaiParaProximaPagina(ModoTouchActivity.this, cixml);
				//Comandos.vaiParaProximaPagina(cixml);
				vaiParaProximaPagina(cixml);
			}
			else if (estadoAtual == 6) // adicionar imagem a frase / acionar comando
			{
				GridView gv = (GridView) findViewById(R.id.gridview);
				GridView gvf = (GridView) findViewById(R.id.gridview_frase);
				
				int indiceAtual = indiceItemPrincipal - 1;
				indiceAtual = (indiceAtual < 0) ? 0 : indiceAtual;
				ImgItem imgi = (ImgItem) gv.getChildAt(indiceAtual);
				
				if (adicionarImagemFrase)
				{
					lista_imagens_frase.add(new ImgItem(imgi));
					Utils.lista_imagens_frase_global.add(new ImgItem(imgi));
					gvf.setAdapter(new ImageAdapterFrase(lista_imagens_frase));
					
					sservice_intent.putExtra("titulo_som", imgi.getAssocImagemSom().getTituloSom());
					imgi.tocarSom(sservice_intent);
					sservice_intent.removeExtra("titulo_som");
				} 
				else if (acionarComando)
				{
					int cmd = imgi.getAssocImagemSom().getCmd();
					if (cmd == 1) // tocar som frase
						//Comandos.tocarSomFrase();
						tocarSomFrase();
				}
			}
			else if (estadoAtual == 7) // remover imagem da frase
			{
				GridView gvf = (GridView) findViewById(R.id.gridview_frase);
				int indiceAtual = indiceItemFrase - 1;
				indiceAtual = (indiceAtual < 0) ? 0 : indiceAtual;
				
				lista_imagens_frase.remove(indiceAtual);
				Utils.lista_imagens_frase_global.remove(indiceAtual);
				gvf.setAdapter(new ImageAdapterFrase(lista_imagens_frase));
				// se n�o houver mais imagens, vai para varredura externa
				if (lista_imagens_frase.isEmpty())
					sair();
				resetIndices();
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_modo_varredura);
		setContentView(R.layout.telas_interface);
		
		Utils.MODO_ATIVO = "modo_varredura";
		
		// inicializa pagina��o
		current_page = init_page = 1;
		final_page = (new XmlUtilsTelas(this, Utils.TELAS_NOME_ARQUIVO_XML_ATIVO, "root")).getLastPage();
		
		delayVarredura = Opcoes.getIntervalo_tempo_varredura();
		
		alternarVarredura();
		alternarEventoBtnShowHideCommands();
		alternarEventoAoSelecionarImagemDeGridViewPrincipal();
		
		setEstadoVarredura(1);
		
		RelativeLayout rLayoutPrincipal = (RelativeLayout) findViewById(R.id.layoutPrincipal);
		
//		LinearLayout lLayoutCabecalho 	= (LinearLayout) findViewById(R.id.cabecalho);
		LinearLayout lLayoutFrase 		= (LinearLayout) findViewById(R.id.frase);
		
		//findViewById(R.id.gridview);
		//findViewById(R.id.gridview_frase);
		
		Button btnNext 					= (Button) findViewById(R.id.btnNext);
		Button btnSHC 					= (Button) findViewById(R.id.btnShowHideCommands);
		
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
				// TODO Auto-generated method stub
				// o timer tem que rodar na UI Thread porque se n�o n�o funciona!
				runOnUiThread(new Task1());
			}
			
		};
		
		taskVarreduraInterna = new TimerTask()
		{

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				// o timer tem que rodar na UI Thread porque se n�o n�o funciona!
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
//				wview 		= (WebView) ModoVarreduraActivity.this.findViewById(R.id.webview);
				pgrbar		= (ProgressBar) ModoVarreduraActivity.this.findViewById(R.id.progressBar1);
				
				pgrbar.setVisibility(View.VISIBLE);		
//				wview.loadUrl("file:///android_asset/loading.gif");
//				wview.setVisibility(View.VISIBLE);
			}
			
			// metodo que roda na UI Thread depois da atividade em background
			@Override
			protected void onPostExecute(ArrayList<AssocImagemSom> ais_list)
			{
				if (ais_list != null)
					gridview.setAdapter(new ImageAdapter(ModoVarreduraActivity.this, ais_list));
				//wview.setVisibility(View.INVISIBLE);
				pgrbar.setVisibility(View.GONE);
				// ativa o timer de varredura
				ModoVarreduraActivity.this.animationTimer.schedule(ModoVarreduraActivity.this.taskVarreduraExterna, 
						ModoVarreduraActivity.this.delayVarredura, ModoVarreduraActivity.this.delayVarredura);				

			}			
		};
		
		cixml.execute(init_page);
		
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
				//wview 		= (WebView) ModoVarreduraActivity.this.findViewById(R.id.webview);
				
				//WebView view = (WebView) MainActivity.this.findViewById(R.id.webview);
				//wview.loadUrl("file:///android_asset/loading.gif");
				//wview.setVisibility(View.VISIBLE);
				//gridview = (GridView) MainActivity.this.findViewById(R.id.gridview);
			}
			
		};
		
		cixmlc.execute(true); // true: mostrar atalhos
		
		// aqui carrega-se o service de reprodu��o de som
		sservice_intent = new Intent(this, SoundService.class);
		
		// associar os cliques dos controles ao evento principal
		
		rLayoutPrincipal.setOnClickListener(principalEvento);
		lLayoutFrase.setOnClickListener(principalEvento);
//		lLayoutCabecalho.setOnClickListener(principalEvento);
		
//		gridview.setOnClickListener(principalEvento);
//		gridview_frase.setOnClickListener(principalEvento);
		

		btnNext.setOnClickListener(principalEvento);
		btnSHC.setOnClickListener(principalEvento);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.modo_varredura, menu);
		return true;
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		//stopService(sservice_intent);
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
			 * Se n�o, pula o botao proxima tela e 
			 * verifica se o gridview frase est� vazio, se estiver ent�o pula deste
			 * para atalhos
			 * */
			if (esconderComandos)
				estado = ((lista_imagens_frase.isEmpty())) ? 3 : 2;
			else
				estado = 5;
		}
		else if (partida == 5) // partindo de botao proxima tela
			/*
			 * Verifica se o gridview frase est� vazio, se estiver ent�o pula deste
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
	private void setEstadoVarredura(int estado)
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
			
//			iteracaoExternaPrincipalAtiva = iteracaoExternaFraseAtiva = 
//			iteracaoExternaAtalhosAtiva = iteracaoExternaComandosAtiva = 
//			iteracaoExternaProximaTelaAtiva = iteracaoInternaPrincipalAtiva =
//			iteracaoInternaFraseAtiva = iteracaoInternaAtalhosAtiva = false;
			iteracaoInternaPrincipalAtiva = iteracaoInternaFraseAtiva = false;
		}
		else if (estado == 1) // gridview principal ativo
		{
			setBorda(gridview, true);
			setBorda(llFrase, false);
			setBorda(llAtalhos, false);
			setBorda(llNext, false);
			setBorda(llShowHideCommands, false);
			
//			iteracaoExternaPrincipalAtiva = true;
//			iteracaoExternaFraseAtiva = iteracaoExternaAtalhosAtiva = 
//			iteracaoExternaComandosAtiva = iteracaoExternaProximaTelaAtiva = false;			
		}
		else if (estado == 2) // gridview frase ativo
		{
			setBorda(llFrase, true);
			setBorda(gridview, false);
			setBorda(llAtalhos, false);
			setBorda(llNext, false);
			setBorda(llShowHideCommands, false);
			
//			iteracaoExternaFraseAtiva = true;
//			iteracaoExternaPrincipalAtiva = iteracaoExternaAtalhosAtiva = 
//			iteracaoExternaComandosAtiva = iteracaoExternaProximaTelaAtiva = false;
		}
		else if (estado == 3) // gridview atalhos ativo
		{
			setBorda(llAtalhos, true);
			setBorda(gridview, false);
			setBorda(llFrase, false);
			setBorda(llNext, false);
			setBorda(llShowHideCommands, false);
			
//			iteracaoExternaAtalhosAtiva = true;
//			iteracaoExternaPrincipalAtiva = iteracaoExternaFraseAtiva = 
//			iteracaoExternaComandosAtiva = iteracaoExternaProximaTelaAtiva = false;
		}
		else if (estado == 4) // botao mostrar/esconder comandos ativo
		{
			setBorda(llShowHideCommands, true);
			setBorda(gridview, false);
			setBorda(llFrase, false);
			setBorda(llAtalhos, false);
			setBorda(llNext, false);
			
//			iteracaoExternaComandosAtiva = true;
//			iteracaoExternaPrincipalAtiva = iteracaoExternaFraseAtiva = 
//			iteracaoExternaAtalhosAtiva = iteracaoExternaProximaTelaAtiva = false;			
		}
		else if (estado == 5) // botao ir para proxima tela ativo
		{
			setBorda(llNext, true);
			setBorda(gridview, false);
			setBorda(llFrase, false);
			setBorda(llAtalhos, false);
			setBorda(llShowHideCommands, false);
			
//			iteracaoExternaProximaTelaAtiva = true;
//			iteracaoExternaPrincipalAtiva = iteracaoExternaFraseAtiva = 
//			iteracaoExternaAtalhosAtiva = iteracaoExternaComandosAtiva = false;			
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
	
	private void iteracaoTimerGridViewPrincipal()
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
	
	private void resetIndices()
	{
		indiceItemPrincipal = indiceItemFrase = 0;
	}
	
	// alternar varredura de externa pra interna ou vice versa
	private void alternarVarredura()
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
	private void alternarEventoBtnShowHideCommands()
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
	private void alternarEventoAoSelecionarImagemDeGridViewPrincipal()
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
	private void entrar()
	{
		GridView gvf = (GridView) findViewById(R.id.gridview_frase);
		
		// se gridview frase n�o contiver itens, n�o entra
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
				// TODO Auto-generated method stub
				runOnUiThread(new Task1());
			}
			
		};		
	}

	// transi��o da varredura interna para externa
	private void sair()
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
				// TODO Auto-generated method stub
				runOnUiThread(new Task2());
			}
			
		};		
	}
	
	private class Task1 implements Runnable // codigo do timer task externo
	{

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
//			if (iteracaoExternaPrincipalAtiva)
//				setEstadoAtividadeVarredura(transicaoIteracaoExternaPartindoDe(1));
//			else if (iteracaoExternaFraseAtiva)
//				setEstadoAtividadeVarredura(transicaoIteracaoExternaPartindoDe(2));
//			else if (iteracaoExternaAtalhosAtiva)
//				setEstadoAtividadeVarredura(transicaoIteracaoExternaPartindoDe(3));
//			else if (iteracaoExternaComandosAtiva)
//				setEstadoAtividadeVarredura(transicaoIteracaoExternaPartindoDe(4));
//			else if (iteracaoExternaProximaTelaAtiva)
//				setEstadoAtividadeVarredura(transicaoIteracaoExternaPartindoDe(5));
			setEstadoVarredura(transicaoIteracaoExternaPartindoDe(estadoAtual));
		}
		
	}
	
	private class Task2 implements Runnable // codigo do timer task interno
	{

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			GridView gv = (GridView) findViewById(R.id.gridview);
			GridView gvf = (GridView) findViewById(R.id.gridview_frase);
			int cc;
			
			// a iteracao ocorre entre o conte�do do controle e o pr�prio controle
			
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
		
}
	
	
