package com.example.tabletvox03f;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.tabletvox03f.dal.CarregarImagensComandos;
import com.example.tabletvox03f.dal.CarregarImagensTelas;
import com.example.tabletvox03f.dal.xml.XmlUtilsTelas;
import com.example.tabletvox03f.management.Opcoes;

public class ModoTouchCategoriasActivity extends ModoTouchActivity
{
	
	private OnItemClickListener carregarCategoriaEvento = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int arg2,
				long arg3)
		{
			carregarCategoriaModoTouch((ImgItem) v);
		}
	};
	
	// vai para a proxima pagina
	protected OnClickListener proximaPaginaEvento = new OnClickListener()
	{
		public void onClick(View v)
		{
			CarregarImagensTelas cit = new CarregarImagensTelas()
			{
				// metodo que roda na UI Thread antes da atividade em background
				@Override
				protected void onPreExecute()
				{
					super.onPreExecute();
					activeContext = ModoTouchCategoriasActivity.this;
					gridview 	= (GridView) ModoTouchCategoriasActivity.this.findViewById(R.id.gridview);
					pgrbar		= (ProgressBar) ModoTouchCategoriasActivity.this.findViewById(R.id.progressBar1);
					
					pgrbar.setVisibility(View.VISIBLE);
				}
				
			};
			vaiParaProximaPagina(cit, 1);
		}
	};	
	
	// mostra comandos
	private OnClickListener mostraComandosEvento = new OnClickListener()
	{
		public void onClick(View v)
		{
			Button btnShowHideCommands 	= (Button) findViewById(R.id.btnShowHideCommands);
			Button btnNext 				= (Button) findViewById(R.id.btnNext);
			GridView gridview = (GridView) ModoTouchCategoriasActivity.this.findViewById(R.id.gridview);
			
			// associa o click no grid principal a funcionalidade de acionar comando 
			gridview.setOnItemClickListener(startImagemComando);
			// associa o click no botao a funcionalidade de esconder os comandos do grid principal
			btnShowHideCommands.setOnClickListener(escondeComandosEvento);
			// desativar evento click no botao de paginacao
			btnNext.setOnClickListener(null);
			
			
			CarregarImagensComandos cic = new CarregarImagensComandos()
			{

				// metodo que roda na UI Thread antes da atividade em background
				@Override
				protected void onPreExecute()
				{
					super.onPreExecute();
					activeContext = ModoTouchCategoriasActivity.this;
					gridview 	= (GridView) ModoTouchCategoriasActivity.this.findViewById(R.id.gridview);
					pgrbar 		= (ProgressBar) ModoTouchCategoriasActivity.this.findViewById(R.id.progressBar1);
					
					pgrbar.setVisibility(View.VISIBLE);
				}
			};
			cic.execute(false); // false: mostrar todos os comandos
		}
	};	
	
	// esconde comandos
	private OnClickListener escondeComandosEvento = new OnClickListener()
	{
		public void onClick(View v)
		{
			Button btnShowHideCommands 	= (Button) findViewById(R.id.btnShowHideCommands);
			Button btnNext 				= (Button) findViewById(R.id.btnNext);
			GridView gridview = (GridView) ModoTouchCategoriasActivity.this.findViewById(R.id.gridview);
			
			// associa o click no grid principal a funcionalidade de adicionar imagem a frase
			gridview.setOnItemClickListener(carregarCategoriaEvento);
			// associa o click no botao a funcionalidade de mostrar comandos no grid principal
			btnShowHideCommands.setOnClickListener(mostraComandosEvento);
			// reativar evento click no botao de paginacao
			btnNext.setOnClickListener(proximaPaginaEvento);
			
			CarregarImagensTelas cixml = new CarregarImagensTelas()
			{
				// metodo que roda na UI Thread antes da atividade em background
				@Override
				protected void onPreExecute()
				{
					super.onPreExecute();
					activeContext = ModoTouchCategoriasActivity.this;
					gridview 	= (GridView) ModoTouchCategoriasActivity.this.findViewById(R.id.gridview);
					pgrbar		= (ProgressBar) ModoTouchCategoriasActivity.this.findViewById(R.id.progressBar1);
					
					pgrbar.setVisibility(View.VISIBLE);					
				}
				
			};
			cixml.execute(current_page, 1);
		}
	};	

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		onCreateSuper(savedInstanceState);
		setContentView(R.layout.telas_interface);
		
		// caso o usuário girar a tela, ocorre a recriação desse activity, então é preciso
		// inicializar a variavel global TELAS_NOME_ARQUIVO_XML_ATIVO aqui e não em MainMenuActivity
		Utils.TELAS_NOME_ARQUIVO_XML_ATIVO = Utils.PERFIL_ATIVO.getNome() + "_categorias";
		
		// inicializa paginação
		current_page = init_page = 1;
		//final_page = (new XmlUtilsTelas(this, Utils.TELAS_NOME_ARQUIVO_XML_ATIVO, "root")).getLastPage();
		final_page = 1;
		
		GridView gridview = (GridView) findViewById(R.id.gridview);
		GridView gridview_frase = (GridView) findViewById(R.id.gridview_frase);
		GridView gridview_atalhos = (GridView) findViewById(R.id.gridview_atalhos);
		
		Button btnNext 				= (Button) findViewById(R.id.btnNext);
		Button btnShowHideCommands 	= (Button) findViewById(R.id.btnShowHideCommands);
		
		if (!(copiarFraseGlobalParaFraseLocal()))
			lista_imagens_frase = new ArrayList<ImgItem>();
		else // transferindo as imagens globais para o gridview frase 
			gridview_frase.setAdapter(new ImageAdapterFrase(lista_imagens_frase));		
		
		// carrega categorias do perfil
		CarregarImagensTelas cit = new CarregarImagensTelas()
		{
			// metodo que roda na UI Thread antes da atividade em background
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				activeContext = ModoTouchCategoriasActivity.this;
				gridview 	= (GridView) ModoTouchCategoriasActivity.this.findViewById(R.id.gridview);
				pgrbar		= (ProgressBar) ModoTouchCategoriasActivity.this.findViewById(R.id.progressBar1);
				
				pgrbar.setVisibility(View.VISIBLE);
			}			
		};
		cit.execute(init_page, 1);
		
		// aqui carregam-se as imagens-comandos que sao atalhos
		CarregarImagensComandos cixmlc = new CarregarImagensComandos()
		{

			// metodo que roda na UI Thread antes da atividade em background
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				activeContext = ModoTouchCategoriasActivity.this;
				gridview 	= (GridView) ModoTouchCategoriasActivity.this.findViewById(R.id.gridview_atalhos);
			}
			
		};
		cixmlc.execute(true); // true: mostrar atalhos
		
		//GridView gridview = (GridView) findViewById(R.id.gridview);
		
		gridview.setOnItemClickListener(carregarCategoriaEvento);
		
		// aqui carrega-se o service de reprodução de som
		sservice_intent = new Intent(this, SoundService.class);

		// carregando evento proxima pagina
		btnNext.setOnClickListener(proximaPaginaEvento);
		
		// carregando evento mostrar comandos
		btnShowHideCommands.setOnClickListener(mostraComandosEvento);
		
		// carregando evento remover a imagem da frase
		gridview_frase.setOnItemClickListener(removeImagemFraseEvento);
		
		// carregando evento acionar imagem-comando
		gridview_atalhos.setOnItemClickListener(startImagemComando);		
		
	}
	
	@Override
	protected void onRestart()
	{
		super.onRestart();
		
		Utils.TELAS_NOME_ARQUIVO_XML_ATIVO = Utils.PERFIL_ATIVO.getNome() + "_categorias";
		
		if (!(copiarFraseGlobalParaFraseLocal()))
			lista_imagens_frase = new ArrayList<ImgItem>();
		else // transferindo as imagens globais para o gridview frase 
			((GridView) findViewById(R.id.gridview_frase))
			.setAdapter(new ImageAdapterFrase(lista_imagens_frase));
	}
	

}
