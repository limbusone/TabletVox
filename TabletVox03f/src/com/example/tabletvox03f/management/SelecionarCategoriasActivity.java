package com.example.tabletvox03f.management;

import java.util.ArrayList;

import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.Categoria;
import com.example.tabletvox03f.dal.CategoriaDAOSingleton;

public class SelecionarCategoriasActivity extends ListaManageActivity
{

	@Override
	protected void onCreateFilho()
	{

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
		ArrayList<Categoria> lista = CategoriaDAOSingleton.getInstance().getCategorias();
		
		return (new ItemSelectCategoriaAdapter(this, (ArrayList<Categoria>) lista.clone()));
	}

	@Override
	protected void acaoDoEventoItemClick(AdapterView<?> parent, View v,
			int position, long arg3)
	{

	}

	@Override
	protected void acaoDosEventosDoMenu(MenuItem item)
	{
		if (item.getItemId() == R.id.action_concluir)
		{
			
		}
		else if (item.getItemId() == R.id.action_cancelar)
		{
			
		}
	}

	@Override
	protected void acaoDoEventoBuscar(Editable s)
	{

	}

	@Override
	protected String getOptionMenuTitle()
	{
		return "Categorias";
	}

	@Override
	protected int getMenuID()
	{
		return R.menu.action_concluir_cancelar;
	}

}
