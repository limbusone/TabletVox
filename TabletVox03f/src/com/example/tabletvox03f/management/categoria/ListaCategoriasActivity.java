package com.example.tabletvox03f.management.categoria;

import java.util.ArrayList;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.categoria.Categoria;
import com.example.tabletvox03f.dal.categoria.CategoriaDAO;
import com.example.tabletvox03f.management.FormularioBaseActivity;
import com.example.tabletvox03f.management.ListaComBuscaManageActivity;

public class ListaCategoriasActivity extends ListaComBuscaManageActivity
{

	public static final int RC_CATS_GERENCIADAS = 1;
	
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
	
	@Override
	protected BaseAdapter carregarLista()
	{
		CategoriaDAO dao_cat = new CategoriaDAO(this);
		
		dao_cat.open();
		ArrayList<Categoria> lista = dao_cat.getAll();
		dao_cat.close();
		
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
		switch (item.getItemId())
		{
			case R.id.action_criar:
				// chamar activity criar categoria
				Intent intent = new Intent(this, FormularioCategoriaActivity.class);
				intent.putExtra("tipo_form", FormularioBaseActivity.FORM_INCLUIR); // 0, para form do tipo 'criar' e 1 para form do tipo 'editar'
				startActivityForResult(intent, 1);
				break;
		    // Respond to the action bar's Up/Home button
		    case android.R.id.home:
	    		this.setResult(RC_CATS_GERENCIADAS);
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
		
		if (resultCode == FormularioCategoriaActivity.RC_CAT_INCLUIDA_SUCESSO)
			Toast.makeText(this, "Categoria incluida com sucesso!", Toast.LENGTH_SHORT).show();
		else if (resultCode == FormularioCategoriaActivity.RC_CAT_EDITADA_SUCESSO)
			Toast.makeText(this, "Categoria editada com sucesso", Toast.LENGTH_SHORT).show();
	}	

}
