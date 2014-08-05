package com.example.tabletvox03f.management;

import com.example.tabletvox03f.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public abstract class FormularioBaseActivity extends Activity
{

	protected boolean tipo_form; // true para 'criar' e false para 'editar'
	
	protected int menu_action_id;
	
	protected abstract void onCreateFilho();
	
	protected abstract int[] getDadosForm();
	
	protected abstract void initCriarForm();
	
	protected abstract void initEditarForm(Intent intent);
	
	protected abstract void incluir();
	
	protected abstract void editar();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		int[] dados = getDadosForm();
		setContentView(dados[0]);
		
		Intent intent = getIntent(); // intent carregando dados extras
		tipo_form = intent.getBooleanExtra("tipo_form", true);
	
		menu_action_id = dados[1];
		
		onCreateFilho();
		
		// inicializar form - 'criar' ou 'editar'
		if (tipo_form)
			// form do tipo 'criar'
		{
			setTitle(dados[2]);
			initCriarForm();
		}
		else
			// form do tipo 'editar'
		{
			setTitle(dados[3]);
			initEditarForm(intent);
		}
		
	}
	
	
//	protected void onCreate(Bundle savedInstanceState, int layout, int menu, int string_incluir_title, int string_editar_title)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(layout);
//		
//		Intent intent = getIntent(); // intent carregando dados extras
//		tipo_form = intent.getBooleanExtra("tipo_form", true);
//	
//		menu_action_id = menu;
//		
//		// inicializar form - 'criar' ou 'editar'
//		if (tipo_form)
//			// form do tipo 'criar'
//		{
//			setTitle(string_incluir_title);
//			initCriarForm();
//		}
//		else
//			// form do tipo 'editar'
//		{
//			setTitle(string_editar_title);
//			initEditarForm(intent);
//		}
//	}
	
	// trata os eventos ligados ao menu do action bar
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.action_salvar)
		{
			if (tipo_form)
				// aciona incluir
				incluir();
			else
				// aciona editar	
				editar();
				
		}
		return false;
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(menu_action_id, menu);
		return true;
	}
	
}
