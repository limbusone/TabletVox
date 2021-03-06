package com.example.tabletvox03f;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.tabletvox03f.dal.CarregarImagensComandos;
import com.example.tabletvox03f.dal.CarregarImagensTelas;
import com.example.tabletvox03f.dal.perfil.PerfilDAO;

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
					gridview 	= ModoTouchCategoriasActivity.this.gridview;
					pgrbar		= (ProgressBar) ModoTouchCategoriasActivity.this.findViewById(R.id.progressBar1);
					
					pgrbar.setVisibility(View.VISIBLE);
				}
				
			};
			vaiParaProximaPagina(cit, CarregarImagensTelas.OPCAO_CARREGAR_CATEGORIAS);
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
			ModoTouchCategoriasActivity.this.gridview
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
					activeContext = ModoTouchCategoriasActivity.this;
					gridview 	= ModoTouchCategoriasActivity.this.gridview;
					pgrbar 		= (ProgressBar) ModoTouchCategoriasActivity.this.findViewById(R.id.progressBar1);
					
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
			ModoTouchCategoriasActivity.this.gridview
			.setOnItemClickListener(carregarCategoriaEvento);
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
					activeContext = ModoTouchCategoriasActivity.this;
					gridview 	= ModoTouchCategoriasActivity.this.gridview;
					pgrbar		= (ProgressBar) ModoTouchCategoriasActivity.this.findViewById(R.id.progressBar1);
					
					pgrbar.setVisibility(View.VISIBLE);					
				}
				
			};
			cit.execute(current_page, CarregarImagensTelas.OPCAO_CARREGAR_CATEGORIAS);
		}
	};	

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		onCreateSuper(savedInstanceState);
		
		// caso o usu�rio girar a tela, ocorre a recria��o desse activity, ent�o � preciso
		// inicializar a variavel global TELAS_NOME_ARQUIVO_XML_ATIVO aqui e n�o em MainMenuActivity
		Utils.TELAS_NOME_ARQUIVO_XML_ATIVO = Utils.PERFIL_ATIVO.getNome() + "_categorias";
		
		// inicializa pagina��o
		current_page = init_page = 1;
		PerfilDAO pfl_dao = new PerfilDAO(this);
		pfl_dao.open();
		int final_page = pfl_dao.getNumeroDePaginas(Utils.PERFIL_ATIVO.getId());
		this.final_page = (final_page > 0) ? final_page : 1;
		pfl_dao.close();
		
		// muda titulo conforme perfil
		setCurrentTitle("Categorias de " + Utils.PERFIL_ATIVO.getNome());
		
		// habilita up back
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
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
				gridview 	= ModoTouchCategoriasActivity.this.gridview;
				pgrbar		= (ProgressBar) ModoTouchCategoriasActivity.this.findViewById(R.id.progressBar1);
				
				pgrbar.setVisibility(View.VISIBLE);
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
				activeContext = ModoTouchCategoriasActivity.this;
				gridview 	= ModoTouchCategoriasActivity.this.gridview_atalhos;
			}
			
		};
		cic.execute(CarregarImagensComandos.OPCAO_CARREGAR_ATALHOS); // true: mostrar atalhos
		
		//GridView gridview = (GridView) findViewById(R.id.gridview);
		
		gridview.setOnItemClickListener(carregarCategoriaEvento);
		
		// aqui carrega-se o service de reprodu��o de som
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
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) 
	    {
		    // Respond to the action bar's Up/Home button
		    case android.R.id.home:
		        NavUtils.navigateUpFromSameTask(this);
		        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}	
	

}
