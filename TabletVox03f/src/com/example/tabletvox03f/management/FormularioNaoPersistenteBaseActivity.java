package com.example.tabletvox03f.management;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.tabletvox03f.R;

public abstract class FormularioNaoPersistenteBaseActivity extends FormularioBaseActivity
{

	
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
			case FORM_INCLUIR: // form do tipo 'criar'
				setTitle(dados[2]);
				initCriarForm();
				break;
			case FORM_ALTERAR: // form do tipo 'editar'
				setTitle(dados[3]);
				initEditarForm(intent);				
				break;
			case FORM_ALTERAR_NP: // form do tipo 'editar' não persistente
				setTitle(dados[3]);
				initEditarNPForm(intent);
				break;
		}
		
	}
	
	// NP: Não Persistente
	
	protected abstract void initEditarNPForm(Intent intent);
	
	protected abstract void editarNP();


	// trata os eventos ligados ao menu do action bar
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.action_salvar)
		{
			switch (tipo_form)
			{
				case FORM_INCLUIR: // form do tipo 'criar'
					incluir(); // aciona incluir
					break;
				case FORM_ALTERAR: // form do tipo 'editar'
					editar(); // aciona editar
					break;
				case FORM_ALTERAR_NP: // form do tipo 'editar' não persistente
					editarNP(); // aciona editar não persistente
					break;
			}			
				
		}
		else
			acaoDosEventosDoMenu(item);
		return false;
		
	}
	
}
