package com.example.tabletvox03f.management;

import java.util.ArrayList;

import android.content.Intent;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

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
		if (item.getItemId() == R.id.action_concluir)
		{
			ArrayList<Categoria> selecionados = ((ItemSelectCategoriaAdapter)lv.getAdapter()).getSelecionados();
			Intent intent = new Intent();
			intent.putParcelableArrayListExtra("selecionados", (ArrayList<Categoria>) selecionados.clone());
			this.setResult(1, intent);
			finish();
			
		}
		else if (item.getItemId() == R.id.action_cancelar)
		{
			this.setResult(2);
			finish();
		}
	}

	@Override
	protected void acaoDoEventoBuscar(Editable s)
	{
		String texto_para_pesquisa = s.toString();
		carregarLista(CategoriaDAOSingleton.getInstance().getCategoriasByNome(texto_para_pesquisa));
	}

	@Override
	protected String getOptionMenuTitle()
	{
		return "";
	}

	@Override
	protected int getMenuID()
	{
		return R.menu.action_concluir_cancelar;
	}

}
