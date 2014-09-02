package com.example.tabletvox03f.management;

import java.util.ArrayList;

import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.Utils;
import com.example.tabletvox03f.dal.Categoria;
import com.example.tabletvox03f.dal.Perfil;

public class ListaCategoriasDoPerfilActivity extends ListaManageActivity
{

	private Perfil perfil; 
	
	@Override
	protected void onCreateFilho()
	{
		perfil = Utils.PERFIL_ATIVO;
		if (perfil.getCategorias() == null)
			perfil.setCategorias(new ArrayList<Categoria>());
	}
	
	@Override
	protected void acaoDoEventoItemClick(AdapterView<?> parent, View v,
			int position, long arg3)
	{
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void carregarLista()
	{
		// TODO Auto-generated method stub
		ArrayList<Categoria> lista = perfil.getCategorias();
		
		lv.setAdapter(new ItemCategoriaAdapter(this, (ArrayList<Categoria>) lista.clone()));
	}

	@Override
	protected void acaoDosEventosDoMenu(MenuItem item)
	{
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.action_criar)
		{
			// chamar activity categoria manage

		}		
	}

	// recarregar/carregar a lista ao voltar para esse activity
	@Override
	protected void onResume()
	{
		onResumeSuper();
	}
	
	@Override
	protected String getOptionMenuTitle()
	{
		return "Categoria";
	}
	
	@Override
	protected int getMenuID()
	{
		return R.menu.um_action_add;
	}

	@Override
	protected void acaoDoEventoBuscar(Editable s)
	{
		
	}

}
