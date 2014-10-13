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

public abstract class ListaComBuscaManageActivity extends Activity
{
	protected ListView lv;
	protected EditText txtBusca;
	protected TextView lblNumEncontrados;
	protected TextView lblEncontrados;
	
	private TextWatcher buscarEvent = new TextWatcher()
	{
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after)
		{

			
		}
		
		@Override
		public void afterTextChanged(Editable s)
		{
			ListaComBuscaManageActivity.this.acaoDoEventoBuscar(s);
			atualizarLblNumEncontrados(lv.getAdapter().getCount());
		}
	};	
	
	private OnItemClickListener itemClick = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			ListaComBuscaManageActivity.this.acaoDoEventoItemClick(arg0, arg1, arg2, arg3);
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
		txtBusca.addTextChangedListener(buscarEvent);
		
		lblEncontrados 		= (TextView) findViewById(R.id.lblEncontrados);
		lblNumEncontrados 	= (TextView) findViewById(R.id.lblNumEncontrados);
		
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
		showLblNumEncontrados(lv.getAdapter().getCount());
	}
	
	protected void showLblNumEncontrados(int num)
	{
		lblNumEncontrados.setVisibility(View.VISIBLE);
		lblEncontrados.setVisibility(View.VISIBLE);
		
		atualizarLblNumEncontrados(num);
		
	}
	
	protected void hideLblNumEncontrados()
	{
		lblNumEncontrados.setVisibility(View.INVISIBLE);
		lblEncontrados.setVisibility(View.INVISIBLE);
	}
	
	public void atualizarLblNumEncontrados(int num)
	{
		lblNumEncontrados.setText(Integer.toString(num));
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
	
	protected abstract void acaoDoEventoBuscar(Editable s);
	
	protected abstract String getOptionMenuTitle();
	
	protected abstract int getMenuID();
	
	
}
