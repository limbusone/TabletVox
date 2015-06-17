package com.example.tabletvox03f;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.tabletvox03f.dal.CarregarImagensComandos;
import com.example.tabletvox03f.dal.CarregarImagensTelas;
import com.example.tabletvox03f.dal.categoria.CategoriaDAO;

public class ModoTouchActivity extends TelaBaseActivity 
{
	
	// evento adicionar a imagem para a frase
	private OnItemClickListener addImagemFraseEvento = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> parent, View v, int position, long id)
		{
			addImagemFrase((ImgItem) v);
		}
	};

	// evento remover a imagem da frase
	protected OnItemClickListener removeImagemFraseEvento = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> parent, View v, int position, long id)
		{
			removerImagemDaFrase(position);
		}
	};
	
	// evento acionar comando da lista de imagens-comandos
	protected OnItemClickListener startImagemComando = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> parent, View v, int position, long id)
		{
			acionarComando((ImgItem) v);
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
					activeContext = ModoTouchActivity.this;
					gridview 	= ModoTouchActivity.this.gridview;
					pgrbar		= (ProgressBar) ModoTouchActivity.this.findViewById(R.id.progressBar1);
					
					pgrbar.setVisibility(View.VISIBLE);
				}
				
			};
			vaiParaProximaPagina(cit, CarregarImagensTelas.OPCAO_CARREGAR_IMAGENS, current_categoriaId);
		}
	};
	
	// mostra comandos
	private OnClickListener mostraComandosEvento = new OnClickListener()
	{
		public void onClick(View v)
		{
			Button btnShowHideCommands 	= (Button) findViewById(R.id.btnShowHideCommands);
			Button btnNext 				= (Button) findViewById(R.id.btnNext);
			
			// associa o click no grid principal a funcionalidade de acionar comando
			ModoTouchActivity.this.gridview
			.setOnItemClickListener(startImagemComando);
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
					activeContext = ModoTouchActivity.this;
					gridview 	= ModoTouchActivity.this.gridview;
					pgrbar 		= (ProgressBar) ModoTouchActivity.this.findViewById(R.id.progressBar1);
					
					pgrbar.setVisibility(View.VISIBLE);
				}
			};
			cic.execute(CarregarImagensComandos.OPCAO_CARREGAR_TODOS_COMANDOS); // false: mostrar todos os comandos
		}
	};
	
	// esconde comandos
	private OnClickListener escondeComandosEvento = new OnClickListener()
	{
		public void onClick(View v)
		{
			Button btnShowHideCommands 	= (Button) findViewById(R.id.btnShowHideCommands);
			Button btnNext 				= (Button) findViewById(R.id.btnNext);
			
			
			// associa o click no grid principal a funcionalidade de adicionar imagem a frase
			ModoTouchActivity.this.gridview
			.setOnItemClickListener(addImagemFraseEvento);
			// associa o click no botao a funcionalidade de mostrar comandos no grid principal
			btnShowHideCommands.setOnClickListener(mostraComandosEvento);
			// reativar evento click no botao de paginacao
			btnNext.setOnClickListener(proximaPaginaEvento);
			
			CarregarImagensTelas cit = new CarregarImagensTelas()
			{
				// metodo que roda na UI Thread antes da atividade em background
				@Override
				protected void onPreExecute()
				{
					super.onPreExecute();
					activeContext = ModoTouchActivity.this;
					gridview 	= ModoTouchActivity.this.gridview;
					pgrbar		= (ProgressBar) ModoTouchActivity.this.findViewById(R.id.progressBar1);
					
					pgrbar.setVisibility(View.VISIBLE);					
				}
				
			};
			cit.execute(current_page, CarregarImagensTelas.OPCAO_CARREGAR_IMAGENS, current_categoriaId);
		}
	};	
	
	
	protected void onCreateSuper(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		current_categoriaId = (int) getIntent().getLongExtra("categoriaId", 0);
		
		// inicializa paginação
		current_page = init_page = 1;
		//final_page = (new XmlUtilsTelas(this, Utils.TELAS_NOME_ARQUIVO_XML_ATIVO, "root")).getLastPage();
		CategoriaDAO dao_cat = new CategoriaDAO(this);
		dao_cat.open();
		int final_page = dao_cat.getNumeroDePaginas(current_categoriaId);
		this.final_page = (final_page > 0) ? final_page : 1;
		dao_cat.close();
		
		// muda titulo conforme categoria
		setCurrentTitle(Utils.PERFIL_ATIVO.getNome() + " - Categoria " + 
		Utils.PERFIL_ATIVO.getCategoriaById(current_categoriaId).getNome());
		
		// habilita up back
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		Button btnNext 				= (Button) findViewById(R.id.btnNext);
		Button btnShowHideCommands 	= (Button) findViewById(R.id.btnShowHideCommands);
		
		if (!(copiarFraseGlobalParaFraseLocal()))
			lista_imagens_frase = new ArrayList<ImgItem>();
		else // transferindo as imagens globais para o gridview frase 
			gridview_frase.setAdapter(new ImageAdapterFrase(lista_imagens_frase));			

		// aqui carregam-se as imagens da primeira pagina
		CarregarImagensTelas cit = new CarregarImagensTelas()
		{
			// metodo que roda na UI Thread antes da atividade em background
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				activeContext = ModoTouchActivity.this;
				gridview 	= ModoTouchActivity.this.gridview;
//				wview 		= (WebView) ModoTouchActivity.this.findViewById(R.id.webview);
				pgrbar		= (ProgressBar) ModoTouchActivity.this.findViewById(R.id.progressBar1);
				
				pgrbar.setVisibility(View.VISIBLE);				
				//WebView view = (WebView) MainActivity.this.findViewById(R.id.webview);
//				wview.loadUrl("file:///android_asset/loading.gif");
//				wview.setVisibility(View.VISIBLE);
				//gridview = (GridView) MainActivity.this.findViewById(R.id.gridview);
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
				activeContext = ModoTouchActivity.this;
				gridview 	= ModoTouchActivity.this.gridview_atalhos;
			}
			
		};
		cic.execute(CarregarImagensComandos.OPCAO_CARREGAR_ATALHOS); // true: mostrar atalhos
		
		// aqui carrega-se o service de reprodução de som
		sservice_intent = new Intent(this, SoundService.class);

		// carregando evento proxima pagina
		btnNext.setOnClickListener(proximaPaginaEvento);
		
		// carregando evento mostrar comandos
		btnShowHideCommands.setOnClickListener(mostraComandosEvento);
		
		// carregando evento adicionar a imagem para a frase
		gridview.setOnItemClickListener(addImagemFraseEvento);

		// carregando evento remover a imagem da frase
		gridview_frase.setOnItemClickListener(removeImagemFraseEvento);
		
		// carregando evento acionar imagem-comando
		gridview_atalhos.setOnItemClickListener(startImagemComando);

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    switch (item.getItemId()) 
	    {
		    // Respond to the action bar's Up/Home button
		    case android.R.id.home:
		        finish();
		        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		
		if (!(copiarFraseGlobalParaFraseLocal()))
			lista_imagens_frase = new ArrayList<ImgItem>();
		
		// transferindo as imagens globais para o gridview frase 
		gridview_frase.setAdapter(new ImageAdapterFrase(lista_imagens_frase));
		
		
	}	
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		stopService(sservice_intent);
	}

}
