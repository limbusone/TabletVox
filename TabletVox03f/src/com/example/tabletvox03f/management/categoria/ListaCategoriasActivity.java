package com.example.tabletvox03f.management.categoria;

import java.util.ArrayList;

import android.content.Intent;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.Categoria;
import com.example.tabletvox03f.dal.CategoriaDAOSingleton;
import com.example.tabletvox03f.management.ListaComBuscaManageActivity;

public class ListaCategoriasActivity extends ListaComBuscaManageActivity
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
		ArrayList<Categoria> lista = CategoriaDAOSingleton.getInstance().getCategorias();
		
		return (new ItemCategoriaAdapter(this, (ArrayList<Categoria>) lista.clone()));

	}
	
	private void carregarLista(ArrayList<Categoria> categorias)
	{
		lv.setAdapter(new ItemCategoriaAdapter(this, (ArrayList<Categoria>) categorias.clone()));
	}

	@Override
	protected void acaoDoEventoItemClick(AdapterView<?> parent, View v,
			int position, long arg3)
	{

	}

	@Override
	protected void acaoDosEventosDoMenu(MenuItem item)
	{
		if (item.getItemId() == R.id.action_criar)
		{
			// chamar activity criar categoria
			Intent intent = new Intent(this, FormularioCategoriaActivity.class);
			intent.putExtra("tipo_form", 0); // 0, para form do tipo 'criar' e 1 para form do tipo 'editar'
			startActivityForResult(intent, 1);
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
		return "Categoria";
	}

	@Override
	protected int getMenuID()
	{
		return R.menu.um_action_add;
	}
	
	// callback ao voltar da tela adicionar / editar categoria
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == 1)
			Toast.makeText(this, "Categoria incluida com sucesso!", Toast.LENGTH_SHORT).show();
		else if (resultCode == 2)
			Toast.makeText(this, "Categoria editada com sucesso", Toast.LENGTH_SHORT).show();
	}	

}
