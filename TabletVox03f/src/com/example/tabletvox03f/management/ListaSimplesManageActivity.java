package com.example.tabletvox03f.management;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tabletvox03f.R;

public abstract class ListaSimplesManageActivity extends Activity
{
	protected ListView lv;
	
	private OnItemClickListener itemClick = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			ListaSimplesManageActivity.this.acaoDoEventoItemClick(arg0, arg1, arg2, arg3);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_interface);
		
		// carregar evento do click em um item da lista
		lv = (ListView) findViewById(R.id.listView1);
		lv.setOnItemClickListener(itemClick);
		
		onCreateFilho();
	}
	
	protected abstract void onCreateFilho();

	protected void onResumeSuper()
	{
		super.onResume();
		carregarListaPai();
	}
	
	protected void carregarListaPai()
	{
		lv.setAdapter(carregarLista());
	}
	
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
		// setar titulo do menu caso não for null ou branco
		if (!(getOptionMenuTitle().equals("") || getOptionMenuTitle() == null))
			menu.getItem(0).setTitle(getOptionMenuTitle());
		return true;
	}	

	protected abstract BaseAdapter carregarLista();
	
	protected abstract void acaoDoEventoItemClick(AdapterView<?> parent, View v, int position, long arg3);
	
	protected abstract void acaoDosEventosDoMenu(MenuItem item);
	
	protected abstract String getOptionMenuTitle();
	
	protected abstract int getMenuID();
	
	
}
