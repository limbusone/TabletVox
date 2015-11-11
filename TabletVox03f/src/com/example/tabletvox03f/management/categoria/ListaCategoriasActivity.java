package com.example.tabletvox03f.management.categoria;

import android.content.Intent;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.categoria.Categoria;
import com.example.tabletvox03f.dal.categoria.CategoriaDAO;
import com.example.tabletvox03f.dal.categoria.ListaCategoria;
import com.example.tabletvox03f.management.FormularioBaseActivity;
import com.example.tabletvox03f.management.ListaComBuscaManageActivity;
import com.example.tabletvox03f.management.OnCategoriaSelectedListener;

public class ListaCategoriasActivity extends ListaComBuscaManageActivity implements OnCategoriaSelectedListener
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
		ListaCategoria lista = dao_cat.getAll();
		dao_cat.close();
		
		return (new ItemCategoriaAdapter(this, (ListaCategoria) lista.clone()));

	}
	
	private void carregarLista(ListaCategoria categorias)
	{
		lv.setAdapter(new ItemCategoriaAdapter(this, (ListaCategoria) categorias.clone()));
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

	@Override
	public void onDeleteItem(int id)
	{
		// Auto-generated method stub
		
	}

	@Override
	public boolean onDeleteItem(Categoria categoria)
	{
		// Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDeleteItem(Categoria categoria, int num_encontrados)
	{
		
		Toast.makeText(this, 
		"Excluido com sucesso! ID: " + Integer.toString(categoria.getId()), 
		Toast.LENGTH_SHORT).show();
		
		CategoriaDAO dao_cat = new CategoriaDAO(this);
		dao_cat.open();
		//exclui efetivamente a categoria
		dao_cat.delete(categoria.getId());
		dao_cat.close();
		//CategoriaDAOSingleton.getInstance().excluirCategoria(categoria.getId());
		
		// atualiza o label dos registros encontrados
		atualizarLblNumEncontrados(--num_encontrados);
		
		return true;
	}

	@Override
	public void onEditItem(Categoria categoria)
	{
		
		Toast.makeText(this, "Você clicou no botão Editar!", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, FormularioCategoriaActivity.class);
		
		
		
		// popular imagens na categoria
		CategoriaDAO dao_cat = new CategoriaDAO(this);
		
		dao_cat.open();
		categoria.setImagens(dao_cat.getImagens(categoria.getId()));
		dao_cat.close();
		
		intent.putExtra("categoria", categoria);
		
		intent.putExtra("tipo_form", FormularioBaseActivity.FORM_ALTERAR);
		startActivityForResult(intent, 2);
		
	}	

}
