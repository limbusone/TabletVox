package com.example.tabletvox03f;

import java.util.ArrayList;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.CarregarImagensComandos;
import com.example.tabletvox03f.dal.CarregarImagensTelas;
import com.example.tabletvox03f.dal.XmlUtilsTelas;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;

public class ModoTouchActivity extends TelaBaseActivity 
{
	
	// evento adicionar a imagem para a frase
	private OnItemClickListener addImagemFraseEvento = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> parent, View v, int position, long id)
		{
			ImgItem imgi = (ImgItem) v;
			lista_imagens_frase.add(new ImgItem(imgi));
			Utils.lista_imagens_frase_global.add(new ImgItem(imgi));
			((GridView) findViewById(R.id.gridview_frase))
			.setAdapter(new ImageAdapterFrase(lista_imagens_frase));
			sservice_intent.putExtra("titulo_som", imgi.getAssocImagemSom().getTituloSom());
			imgi.tocarSom(sservice_intent);
			sservice_intent.removeExtra("titulo_som");

		}
	};

	// evento remover a imagem da frase
	private OnItemClickListener removeImagemFraseEvento = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> parent, View v, int position, long id)
		{
			//ImgItem imgi = (ImgItem) v;
			lista_imagens_frase.remove(position);
			Utils.lista_imagens_frase_global.remove(position);
			((GridView) findViewById(R.id.gridview_frase))
					.setAdapter(new ImageAdapterFrase(lista_imagens_frase));			
		}
	};
	
	// evento acionar comando da lista de imagens-comandos
	private OnItemClickListener startImagemComando = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> parent, View v, int position, long id)
		{
			ImgItem imgi = (ImgItem) v;
			int cmd = imgi.getAssocImagemSom().getCmd();
			acionarComando(cmd);
		}
	};
	
	// vai para a proxima pagina
	private OnClickListener proximaPaginaEvento = new OnClickListener()
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
					gridview 	= (GridView) ModoTouchActivity.this.findViewById(R.id.gridview);
					//wview 		= (WebView) ModoTouchActivity.this.findViewById(R.id.webview);
					pgrbar		= (ProgressBar) ModoTouchActivity.this.findViewById(R.id.progressBar1);
					
					pgrbar.setVisibility(View.VISIBLE);
					//WebView view = (WebView) MainActivity.this.findViewById(R.id.webview);
					//wview.loadUrl("file:///android_asset/loading.gif");
					//wview.setVisibility(View.VISIBLE);
					//gridview = (GridView) MainActivity.this.findViewById(R.id.gridview);
				}
				
			};
			//Comandos.vaiParaProximaPagina(ModoTouchActivity.this, cixml);
			//Comandos.vaiParaProximaPagina(cixml);
			vaiParaProximaPagina(cit);
		}
	};
	
	// mostra comandos
	private OnClickListener mostraComandosEvento = new OnClickListener()
	{
		public void onClick(View v)
		{
			Button btnShowHideCommands 	= (Button) findViewById(R.id.btnShowHideCommands);
			Button btnNext 				= (Button) findViewById(R.id.btnNext);
			GridView gridview = (GridView) ModoTouchActivity.this.findViewById(R.id.gridview);
			
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
					activeContext = ModoTouchActivity.this;
					gridview 	= (GridView) ModoTouchActivity.this.findViewById(R.id.gridview);
//					wview 		= (WebView) ModoTouchActivity.this.findViewById(R.id.webview);
					pgrbar 		= (ProgressBar) ModoTouchActivity.this.findViewById(R.id.progressBar1);
					
					pgrbar.setVisibility(View.VISIBLE);
					//WebView view = (WebView) MainActivity.this.findViewById(R.id.webview);
//					wview.loadUrl("file:///android_asset/loading.gif");
//					wview.setVisibility(View.VISIBLE);
					//gridview = (GridView) MainActivity.this.findViewById(R.id.gridview);
				}
			};
			//Comandos.mostrarComandos(cixmlc);
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
			GridView gridview = (GridView) ModoTouchActivity.this.findViewById(R.id.gridview);
			
			// associa o click no grid principal a funcionalidade de adicionar imagem a frase
			gridview.setOnItemClickListener(addImagemFraseEvento);
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
					activeContext = ModoTouchActivity.this;
					gridview 	= (GridView) ModoTouchActivity.this.findViewById(R.id.gridview);
					//wview 		= (WebView) ModoTouchActivity.this.findViewById(R.id.webview);
					pgrbar		= (ProgressBar) ModoTouchActivity.this.findViewById(R.id.progressBar1);
					
					pgrbar.setVisibility(View.VISIBLE);					
					//WebView view = (WebView) MainActivity.this.findViewById(R.id.webview);
//					wview.loadUrl("file:///android_asset/loading.gif");
//					wview.setVisibility(View.VISIBLE);
					//gridview = (GridView) MainActivity.this.findViewById(R.id.gridview);
				}
				
			};
			cixml.execute(current_page);
			//Comandos.mostrarComandos(cixmlc);
		}
	};	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_modo_touch);
		setContentView(R.layout.telas_interface);

		//Utils.MODO_ATIVO = "modo_touch";
		//Utils.TELAS_NOME_ARQUIVO_XML_ATIVO = "perfil01";
		
		//Utils.TELAS_NOME_ARQUIVO_XML_ATIVO = Utils.PERFIL_ATIVO;
		
		// inicializa paginação
		current_page = init_page = 1;
		final_page = (new XmlUtilsTelas(this, Utils.TELAS_NOME_ARQUIVO_XML_ATIVO, "root")).getLastPage();
		
		GridView gridview = (GridView) findViewById(R.id.gridview);
		GridView gridview_frase = (GridView) findViewById(R.id.gridview_frase);
		GridView gridview_atalhos = (GridView) findViewById(R.id.gridview_atalhos);
		
		Button btnNext 				= (Button) findViewById(R.id.btnNext);
		Button btnShowHideCommands 	= (Button) findViewById(R.id.btnShowHideCommands);
		
		if (!(copiarFraseGlobalParaFraseLocal()))
			lista_imagens_frase = new ArrayList<ImgItem>();
		else // transferindo as imagens globais para o gridview frase 
			((GridView) findViewById(R.id.gridview_frase))
			.setAdapter(new ImageAdapterFrase(lista_imagens_frase));			

		// aqui carregam-se as imagens da primeira pagina
		CarregarImagensTelas cixml = new CarregarImagensTelas()
		{
			// metodo que roda na UI Thread antes da atividade em background
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				activeContext = ModoTouchActivity.this;
				gridview 	= (GridView) ModoTouchActivity.this.findViewById(R.id.gridview);
//				wview 		= (WebView) ModoTouchActivity.this.findViewById(R.id.webview);
				pgrbar		= (ProgressBar) ModoTouchActivity.this.findViewById(R.id.progressBar1);
				
				pgrbar.setVisibility(View.VISIBLE);				
				//WebView view = (WebView) MainActivity.this.findViewById(R.id.webview);
//				wview.loadUrl("file:///android_asset/loading.gif");
//				wview.setVisibility(View.VISIBLE);
				//gridview = (GridView) MainActivity.this.findViewById(R.id.gridview);
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
				activeContext = ModoTouchActivity.this;
				gridview 	= (GridView) ModoTouchActivity.this.findViewById(R.id.gridview_atalhos);
				//wview 		= (WebView) ModoTouchActivity.this.findViewById(R.id.webview);
				
				//WebView view = (WebView) MainActivity.this.findViewById(R.id.webview);
				//wview.loadUrl("file:///android_asset/loading.gif");
				//wview.setVisibility(View.VISIBLE);
				//gridview = (GridView) MainActivity.this.findViewById(R.id.gridview);
			}
			
		};
		cixmlc.execute(true); // true: mostrar atalhos
		
		// especifica o objeto de TelaBaseActivity para classe Comandos
		//Comandos.actb = this;
		
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
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.modo_touch, menu);
		return true;
	}
	
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		stopService(sservice_intent);
	}

}
