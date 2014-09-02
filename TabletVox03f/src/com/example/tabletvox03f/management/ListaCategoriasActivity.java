package com.example.tabletvox03f.management;

import com.example.tabletvox03f.R;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

public class ListaCategoriasActivity extends ListaManageActivity
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
	
	@Override
	protected BaseAdapter carregarLista()
	{
		return null;

	}

	@Override
	protected void acaoDoEventoItemClick(AdapterView<?> parent, View v,
			int position, long arg3)
	{

	}

	@Override
	protected void acaoDosEventosDoMenu(MenuItem item)
	{

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
		return R.menu.um_action_add;
	}

}
