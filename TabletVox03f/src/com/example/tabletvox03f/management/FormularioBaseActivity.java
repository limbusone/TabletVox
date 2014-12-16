package com.example.tabletvox03f.management;

import com.example.tabletvox03f.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public abstract class FormularioBaseActivity extends Activity
{
	
	public static final int FORM_INCLUIR = 0;
	
	public static final int FORM_ALTERAR = 1;
	
	public static final int FORM_ALTERAR_NP = 2; // NP : Não Persistente (em memória)

	protected int tipo_form; // 0 para 'criar'; 1 para 'editar'
	
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
		tipo_form = intent.getIntExtra("tipo_form", 0); // default: form do tipo 'criar'
	
		menu_action_id = dados[1];
		
		onCreateFilho();
		
		// inicializar form - 'criar' ou 'editar'
		switch (tipo_form)
		{
			case 0: // form do tipo 'criar'
				setTitle(dados[2]);
				initCriarForm();
				break;
			case 1: // form do tipo 'editar'
				setTitle(dados[3]);
				initEditarForm(intent);				
				break;
		}
		
	}
	
	
	// trata os eventos ligados ao menu do action bar
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.action_salvar)
		{
			switch (tipo_form)
			{
				case 0: // form do tipo 'criar'
					incluir(); // aciona incluir
					break;
				case 1: // form do tipo 'editar'
					editar(); // aciona editar
					break;
			}			
				
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
