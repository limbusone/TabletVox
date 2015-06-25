package com.example.tabletvox03f;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ProgressBar;

import com.example.tabletvox03f.dal.CarregarImagensComandos;
import com.example.tabletvox03f.dal.CarregarImagensTelas;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.perfil.PerfilDAO;
import com.example.tabletvox03f.management.Opcoes;

public class ModoVarreduraCategoriasActivity extends ModoVarreduraActivity
{

	private boolean carregarCategoria;
	
	private static final int ESTADO_CARREGAR_CATEGORIA_ACIONAR_COMANDO 	= 6;
	private static final int ESTADO_REMOVER_IMAGEM_FRASE 				= 7;

	@Override
	protected void acaoDoEventoPrincipal()
	{
		
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
						activeContext = ModoVarreduraCategoriasActivity.this;
						gridview 	= ModoVarreduraCategoriasActivity.this.gridview;
						pgrbar		= (ProgressBar) ModoVarreduraCategoriasActivity.this.findViewById(R.id.progressBar1);
						
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
						activeContext = ModoVarreduraCategoriasActivity.this;
						gridview 	= ModoVarreduraCategoriasActivity.this.gridview;
						pgrbar		= (ProgressBar) ModoVarreduraCategoriasActivity.this.findViewById(R.id.progressBar1);
						
						pgrbar.setVisibility(View.VISIBLE);									
					}
			
				};
				
				cit.execute(current_page, CarregarImagensTelas.OPCAO_CARREGAR_CATEGORIAS);					
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
					activeContext = ModoVarreduraCategoriasActivity.this;
					gridview 	= ModoVarreduraCategoriasActivity.this.gridview;
					pgrbar		= (ProgressBar) ModoVarreduraCategoriasActivity.this.findViewById(R.id.progressBar1);
					
					pgrbar.setVisibility(View.VISIBLE);								
				}
		
			};
			vaiParaProximaPagina(cit, CarregarImagensTelas.OPCAO_CARREGAR_CATEGORIAS);
		}
		else if (estadoAtual == ESTADO_CARREGAR_CATEGORIA_ACIONAR_COMANDO) // carregar categoria / acionar comando
		{
			int indiceAtual = indiceItemPrincipal - 1;
			indiceAtual = (indiceAtual < 0) ? 0 : indiceAtual;
			ImgItem imgi = (ImgItem) gridview.getChildAt(indiceAtual);
			
			if (carregarCategoria)
				carregarCategoriaModoVarredura(imgi);
			else if (acionarComando)
				acionarComando(imgi);
		}
		else if (estadoAtual == ESTADO_REMOVER_IMAGEM_FRASE) // remover imagem da frase
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
		PerfilDAO pfl_dao = new PerfilDAO(this);
		pfl_dao.open();
		int final_page = pfl_dao.getNumeroDePaginas(Utils.PERFIL_ATIVO.getId());
		this.final_page = (final_page > 0) ? final_page : 1;
		pfl_dao.close();

		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		delayVarredura = Integer.parseInt(sp.getString(Opcoes.INTERVALO_TEMPO_VARREDURA_KEY, "" + Opcoes.INTERVALO_TEMPO_VARREDURA_DEFAULT));;
		
		// muda titulo conforme perfil
		setCurrentTitle("Categorias de " + Utils.PERFIL_ATIVO.getNome());		
		
		// inicializando variaveis booleanas com valores default
		alternarVarredura();
		alternarEventoBtnShowHideCommands();
		alternarEventoAoSelecionarImagemDeGridViewPrincipal();		
		

		resetIndices();
		
		armazenarBackgroundDosComponentes();
		
		setEstadoVarredura(ESTADO_CARREGAR_CATEGORIA_ACIONAR_COMANDO);
		
		if (!(copiarFraseGlobalParaFraseLocal()))
			lista_imagens_frase = new ArrayList<ImgItem>();
		else // transferindo as imagens globais para o gridview frase 
			gridview_frase.setAdapter(new ImageAdapterFrase(lista_imagens_frase));		
		
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
		CarregarImagensTelas cit = new CarregarImagensTelas()
		{
			// metodo que roda na UI Thread antes da atividade em background
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				activeContext = ModoVarreduraCategoriasActivity.this;
				gridview 	= ModoVarreduraCategoriasActivity.this.gridview;
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
		cit.execute(init_page, CarregarImagensTelas.OPCAO_CARREGAR_CATEGORIAS);
		
		// aqui carregam-se as imagens-comandos que sao atalhos
		CarregarImagensComandos cic = new CarregarImagensComandos()
		{

			// metodo que roda na UI Thread antes da atividade em background
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				activeContext 	= ModoVarreduraCategoriasActivity.this;
				gridview 		= ModoVarreduraCategoriasActivity.this.gridview_atalhos;
			}
			
		};
		cic.execute(CarregarImagensComandos.OPCAO_CARREGAR_ATALHOS); // true: mostrar atalhos		
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

		// transferindo as imagens globais para o gridview frase 
		gridview_frase.setAdapter(new ImageAdapterFrase(lista_imagens_frase));		
		
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
