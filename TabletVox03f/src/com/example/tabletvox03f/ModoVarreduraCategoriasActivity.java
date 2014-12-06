package com.example.tabletvox03f;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.tabletvox03f.dal.CarregarImagensComandos;
import com.example.tabletvox03f.dal.CarregarImagensTelas;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.management.Opcoes;

public class ModoVarreduraCategoriasActivity extends ModoVarreduraActivity
{

	private boolean carregarCategoria;

	@Override
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
						activeContext = ModoVarreduraCategoriasActivity.this;
						gridview 	= (GridView) ModoVarreduraCategoriasActivity.this.findViewById(R.id.gridview);
						pgrbar		= (ProgressBar) ModoVarreduraCategoriasActivity.this.findViewById(R.id.progressBar1);
						
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
						activeContext = ModoVarreduraCategoriasActivity.this;
						gridview 	= (GridView) ModoVarreduraCategoriasActivity.this.findViewById(R.id.gridview);
						pgrbar		= (ProgressBar) ModoVarreduraCategoriasActivity.this.findViewById(R.id.progressBar1);
						
						pgrbar.setVisibility(View.VISIBLE);									
					}
			
				};
				
				cixml.execute(current_page, 1);					
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
					activeContext = ModoVarreduraCategoriasActivity.this;
					gridview 	= (GridView) ModoVarreduraCategoriasActivity.this.findViewById(R.id.gridview);
					pgrbar		= (ProgressBar) ModoVarreduraCategoriasActivity.this.findViewById(R.id.progressBar1);
					
					pgrbar.setVisibility(View.VISIBLE);								
				}
		
			};
			vaiParaProximaPagina(cixml, 1);
		}
		else if (estadoAtual == 6) // carregar categoria / acionar comando
		{
			GridView gv = (GridView) findViewById(R.id.gridview);
			
			int indiceAtual = indiceItemPrincipal - 1;
			indiceAtual = (indiceAtual < 0) ? 0 : indiceAtual;
			ImgItem imgi = (ImgItem) gv.getChildAt(indiceAtual);
			
			if (carregarCategoria)
				carregarCategoriaModoVarredura(imgi);
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		onCreateSuper(savedInstanceState);

		// caso o usuário girar a tela, ocorre a recriação desse activity, então é preciso
		// inicializar a variavel global TELAS_NOME_ARQUIVO_XML_ATIVO aqui e não em MainMenuActivity
		Utils.TELAS_NOME_ARQUIVO_XML_ATIVO = Utils.PERFIL_ATIVO.getNome() + "_categorias";
		
		// inicializa paginação
		current_page = init_page = 1;
		//final_page = (new XmlUtilsTelas(this, Utils.TELAS_NOME_ARQUIVO_XML_ATIVO, "root")).getLastPage();
		final_page = 1;
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		delayVarredura = Integer.parseInt(sp.getString("intervalo_tempo_varredura", "" + Opcoes.INTERVALO_TEMPO_VARREDURA_DEFAULT));;
		
		// muda titulo conforme perfil
		setCurrentTitle("Categorias de " + Utils.PERFIL_ATIVO.getNome());		
		
		// inicializando variaveis booleanas com valores default
		alternarVarredura();
		alternarEventoBtnShowHideCommands();
		alternarEventoAoSelecionarImagemDeGridViewPrincipal();		
		

		resetIndices();
		
		setEstadoVarredura(6);
		
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
		
		// carrega categorias do perfil
		CarregarImagensTelas cixml = new CarregarImagensTelas()
		{
			// metodo que roda na UI Thread antes da atividade em background
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				activeContext = ModoVarreduraCategoriasActivity.this;
				gridview 	= (GridView) ModoVarreduraCategoriasActivity.this.findViewById(R.id.gridview);
				pgrbar		= (ProgressBar) ModoVarreduraCategoriasActivity.this.findViewById(R.id.progressBar1);
				
				pgrbar.setVisibility(View.VISIBLE);
			}
			
			// metodo que roda na UI Thread depois da atividade em background
			@Override
			protected void onPostExecute(ArrayList<AssocImagemSom> ais_list)
			{
				if (ais_list != null)
					gridview.setAdapter(new ImageAdapter(ModoVarreduraCategoriasActivity.this, ais_list));
				//wview.setVisibility(View.INVISIBLE);
				pgrbar.setVisibility(View.GONE);
				// ativa o timer de varredura
				ModoVarreduraCategoriasActivity.this.animationTimer.schedule(ModoVarreduraCategoriasActivity.this.taskVarreduraInterna, 
				ModoVarreduraCategoriasActivity.this.delayVarredura, ModoVarreduraCategoriasActivity.this.delayVarredura);				

			}			
		};
		cixml.execute(init_page, 1);
		
		// aqui carregam-se as imagens-comandos que sao atalhos
		CarregarImagensComandos cixmlc = new CarregarImagensComandos()
		{

			// metodo que roda na UI Thread antes da atividade em background
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				activeContext = ModoVarreduraCategoriasActivity.this;
				gridview 	= (GridView) ModoVarreduraCategoriasActivity.this.findViewById(R.id.gridview_atalhos);
			}
			
		};
		cixmlc.execute(true); // true: mostrar atalhos		
	}
	
	@Override
	protected void alternarEventoAoSelecionarImagemDeGridViewPrincipal()
	{
		if (carregarCategoria)
		{
			acionarComando		= true;
			carregarCategoria 	= false;
		}
		else if (acionarComando)
		{
			carregarCategoria	= true;
			acionarComando 		= false;
		}
		else // default
		{
			carregarCategoria 	= true;
			acionarComando 		= false;
		}
			
	}
	
	
	// alternar varredura de externa pra interna ou vice versa
	@Override
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
			iteracaoExternaAtiva = false;
			iteracaoInternaAtiva = true;			
		}
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		// quando a interface desta activity sair do foco, parar o timer
		taskVarreduraInterna.cancel();
		taskVarreduraExterna.cancel();
		
	}
	
	@Override
	protected void onRestart()
	{
		onRestartSuper();
		
		Utils.TELAS_NOME_ARQUIVO_XML_ATIVO = Utils.PERFIL_ATIVO.getNome() + "_categorias";
		
		if (!(copiarFraseGlobalParaFraseLocal()))
			lista_imagens_frase = new ArrayList<ImgItem>();
		else // transferindo as imagens globais para o gridview frase 
			((GridView) findViewById(R.id.gridview_frase))
			.setAdapter(new ImageAdapterFrase(lista_imagens_frase));		
		
		// quando a interface desta activity voltar ao foco, reiniciar/recriar o timer
	
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
				runOnUiThread(new Task2());
			}
			
		};
		
		
		if (iteracaoInternaAtiva)
			animationTimer.schedule(taskVarreduraInterna, 0, delayVarredura);
		else if (iteracaoExternaAtiva)
			animationTimer.schedule(taskVarreduraExterna, 0, delayVarredura);
	}
	
}
