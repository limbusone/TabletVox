package com.example.tabletvox03f.management.categoria;

import java.util.ArrayList;

import android.content.Intent;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.categoria.Categoria;
import com.example.tabletvox03f.dal.categoria.CategoriaDAO;
import com.example.tabletvox03f.management.ListaComBuscaManageActivity;

public class SelecionarCategoriasActivity extends ListaComBuscaManageActivity
{

	public static final int RC_ADD_CAT_SUCESSO 		= 3;
	public static final int RC_ADD_CAT_CANCELADO 	= 4;
	
	@Override
	protected void onCreateFilho()
	{
		// habilita up back
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	// recarregar/carregar a lista ao voltar para esse activity
	@Override
	protected void onResume()
	{
		onResumeSuper();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected BaseAdapter carregarLista()
	{
		CategoriaDAO dao_cat = new CategoriaDAO(this);
		
		dao_cat.open();
		ArrayList<Categoria> lista = dao_cat.getAll();
		dao_cat.close();
		
		return (new ItemSelectCategoriaAdapter(this, (ArrayList<Categoria>) lista.clone()));
	}
	
	private void carregarLista(ArrayList<Categoria> categorias)
	{
		lv.setAdapter(new ItemSelectCategoriaAdapter(this, categorias));
	}

	@Override
	protected void acaoDoEventoItemClick(AdapterView<?> parent, View v,
			int position, long arg3)
	{
		// selecionar/desselecionar checkbox
		CheckBox checkbox = (CheckBox) v.findViewById(R.id.checkBox);
		checkbox.performClick();
	}

	@Override
	protected void acaoDosEventosDoMenu(MenuItem item)
	{
		switch (item.getItemId())
		{
			// resgata array de selecionados do adapter e manda uma cópia para o formulario
			case R.id.action_concluir:
				ArrayList<Categoria> selecionados = ((ItemSelectCategoriaAdapter)lv.getAdapter()).getSelecionados();
				Intent intent = new Intent();
				intent.putParcelableArrayListExtra("selecionados", (ArrayList<Categoria>) selecionados.clone());
				this.setResult(RC_ADD_CAT_SUCESSO, intent);
				finish();			
				break;
			// cancela a ação e volta pro formulario			
			case R.id.action_cancelar:
				this.setResult(RC_ADD_CAT_CANCELADO);
				finish();
				break;
			case R.id.action_gerenciar:
				Intent intent_lca = new Intent(this, ListaCategoriasActivity.class);
				intent_lca.putExtra("isSCA", true);
				startActivityForResult(intent_lca, 1);			
				break;
		    // Respond to the action bar's Up/Home button
		    case android.R.id.home:
		    	finish();
		        break;				
		}
	}

	@Override
	protected void acaoDoEventoBuscar(Editable s)
	{
		CategoriaDAO dao_cat = new CategoriaDAO(this);
		
		String texto_para_pesquisa = s.toString();
		
		dao_cat.open();
		carregarLista(dao_cat.getCategoriasByNome(texto_para_pesquisa));
		dao_cat.close();
	}

	@Override
	protected String getOptionMenuTitle()
	{
		return "";
	}

	@Override
	protected int getMenuID()
	{
		return R.menu.action_concluir_cancelar_manage;
	}
	
	// callback ao voltar da tela listar categorias
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == ListaCategoriasActivity.RC_CATS_GERENCIADAS);
	}			

}
