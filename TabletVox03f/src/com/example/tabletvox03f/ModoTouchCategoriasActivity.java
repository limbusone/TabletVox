package com.example.tabletvox03f;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.CarregarImagensTelas;
import com.example.tabletvox03f.dal.XmlUtilsTelas;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;

public class ModoTouchCategoriasActivity extends TelaBaseActivity
{
	
	private OnItemClickListener carregarCategoriaEvento = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int arg2,
				long arg3)
		{
			ImgItem imgi = (ImgItem) v;
			imgi.tocarSom(ModoTouchCategoriasActivity.this);
			carregarCategoriaModoTouch(imgi.getAssocImagemSom().getDesc());
		}
	};	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.telas_categorias_interface);
		
		// caso o usuário girar a tela, ocorre a recriação desse activity, então é preciso
		// inicializar a variavel global TELAS_NOME_ARQUIVO_XML_ATIVO aqui e não em MainMenuActivity
		Utils.TELAS_NOME_ARQUIVO_XML_ATIVO = Utils.PERFIL_ATIVO.getNome() + "_categorias";
		
		// inicializa paginação
		current_page = init_page = 1;
		final_page = (new XmlUtilsTelas(this, Utils.TELAS_NOME_ARQUIVO_XML_ATIVO, "root")).getLastPage();
		
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
		cit.execute(init_page);
		
		GridView gridview = (GridView) findViewById(R.id.gridview);
		
		gridview.setOnItemClickListener(carregarCategoriaEvento);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.modo_touch_categorias, menu);
		return true;
	}

}
