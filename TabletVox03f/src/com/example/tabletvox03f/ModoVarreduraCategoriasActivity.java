package com.example.tabletvox03f;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.AssocImagemSom;
import com.example.tabletvox03f.dal.CarregarImagensTelas;
import com.example.tabletvox03f.dal.XmlUtilsTelas;
import com.example.tabletvox03f.management.Opcoes;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class ModoVarreduraCategoriasActivity extends TelaBaseActivity
{

	private Timer animationTimer;
	private int delayVarredura;
	private TimerTask taskVarredura;
	private int indiceItemPrincipal;
	
	private void acaoDoEvento()
	{
		GridView gv = (GridView) findViewById(R.id.gridview);
		
		int indiceAtual = indiceItemPrincipal - 1;
		indiceAtual = (indiceAtual < 0) ? 0 : indiceAtual;
		ImgItem imgi = (ImgItem) gv.getChildAt(indiceAtual);
		
		imgi.tocarSom(this);
		carregarCategoriaModoVarredura(imgi.getAssocImagemSom().getDesc());
	}
	
	private OnItemClickListener carregarCategoriaEventoItem = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			acaoDoEvento();
		}
		
	};
	private OnClickListener carregarCategoriaEvento = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			acaoDoEvento();
		}
	};	
	
	private class Task implements Runnable // codigo do timer task interno
	{

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			GridView gv = (GridView) findViewById(R.id.gridview);
			int cc;
			
			// Varre GridView Principal
			if 	(   	
					((cc = gv.getChildCount()) > 0) 
					&& 	(indiceItemPrincipal <= cc) 
				)
			{
				
				iteracaoTimerGridViewPrincipal();
				if (indiceItemPrincipal == (cc + 1))
					indiceItemPrincipal = 0;
			}
		
		}			
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
	
	private void setBorda(View v, boolean s)
	{
		v.setBackgroundResource((s) ? R.drawable.borda : 0);
	}	

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
		
		delayVarredura = Opcoes.getIntervalo_tempo_varredura();
		
		animationTimer = new Timer();

		indiceItemPrincipal = 0;
		
		// task do timer da varredura
		taskVarredura = new TimerTask()
		{

			@Override
			public void run()
			{	
				// TODO Auto-generated method stub
				// o timer tem que rodar na UI Thread porque se não não funciona!
				runOnUiThread(new Task());
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
				ModoVarreduraCategoriasActivity.this.animationTimer.schedule(ModoVarreduraCategoriasActivity.this.taskVarredura, 
						ModoVarreduraCategoriasActivity.this.delayVarredura, ModoVarreduraCategoriasActivity.this.delayVarredura);				

			}			
		};
		cixml.execute(init_page);
		
		GridView gridview = (GridView) findViewById(R.id.gridview);
		
		RelativeLayout rl = (RelativeLayout) gridview.getParent();
		
		gridview.setOnItemClickListener(carregarCategoriaEventoItem);
		rl.setOnClickListener(carregarCategoriaEvento);
		
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		// quando a interface desta activity sair do foco, parar o timer
		taskVarredura.cancel();
	}
	
	@Override
	protected void onRestart()
	{
		super.onRestart();
		
		// quando a interface desta activity voltar ao foco, reiniciar/recriar o timer
		
		taskVarredura = new TimerTask()
		{

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				runOnUiThread(new Task());
			}
			
		};
		
		animationTimer.schedule(taskVarredura, 0, delayVarredura);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		taskVarredura.cancel();
		animationTimer.cancel();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.modo_varredura_categorias, menu);
		return true;
	}

}
