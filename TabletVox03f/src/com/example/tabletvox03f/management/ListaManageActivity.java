package com.example.tabletvox03f.management;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.example.tabletvox03f.R;

public abstract class ListaManageActivity extends Activity
{
	protected ListView lv;
	protected EditText txtBusca;
	
	private OnKeyListener buscarKeyEvent = new OnKeyListener()
	{
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event)
		{
			ListaManageActivity.this.acaoDoEventoBuscar(v, keyCode, event);
			return false;
		}
	};
	
	private OnItemClickListener itemClick = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			// TODO Auto-generated method stub
			ListaManageActivity.this.acaoDoEventoItemClick(arg0, arg1, arg2, arg3);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_c_busca_interface);
		
		// carregar evento do click em um item da lista
		lv = (ListView) findViewById(R.id.listView1);
		lv.setOnItemClickListener(itemClick);
		
		// carregar evento do txtBusca
		txtBusca = (EditText) findViewById(R.id.txtBusca);
		txtBusca.setOnKeyListener(buscarKeyEvent);
		
		onCreateFilho();
	}
	
	protected abstract void onCreateFilho();

	protected void onResumeSuper()
	{
		super.onResume();
		carregarLista();
	}

	protected abstract void carregarLista();
	
	// trata os eventos ligados ao menu do action bar
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		acaoDosEventosDoMenu(item);
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.		
		getMenuInflater().inflate(getMenuID(), menu);
		// setar titulo do menu
		menu.getItem(0).setTitle(getOptionMenuTitle());
		return true;
	}	
	
	protected abstract void acaoDoEventoItemClick(AdapterView<?> parent, View v, int position, long arg3);
	
	protected abstract void acaoDosEventosDoMenu(MenuItem item);
	
	protected abstract void acaoDoEventoBuscar(View v, int keyCode, KeyEvent event);
	
	protected abstract String getOptionMenuTitle();
	
	protected abstract int getMenuID();
	
	
}
