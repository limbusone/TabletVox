package com.example.tabletvox03f.management.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.categoria.Categoria;
import com.example.tabletvox03f.dal.categoria.CategoriaDAO;
import com.example.tabletvox03f.dal.categoria.ListaCategoria;
import com.example.tabletvox03f.management.FormularioBaseActivity;
import com.example.tabletvox03f.management.OnCategoriaSelectedListener;
import com.example.tabletvox03f.management.PaginacaoAdapter;
import com.example.tabletvox03f.management.categoria.FormularioCategoriaActivity;
import com.example.tabletvox03f.management.categoria.SelecionarCategoriasActivity;

public class ListaCategoriasPerfilActivity extends ActionBarActivity implements ActionBar.TabListener, OnCategoriaSelectedListener
{

	public static final int RC_DEFINIR_CATS_SUCESSO 	= 3;
	public static final int RC_DEFINIR_CATS_CANCELADO 	= 4;
	
	private PaginacaoAdapter mPaginacaoAdapter;
	
	private ListaCategoria categorias;
	
	private ListaCategoria novasCategorias;
	
	private ListaCategoria antigasCategorias;
	
	private ViewPager mViewPager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pager);
		
		// receber categorias por intent
		Intent intent = getIntent();
		ListaCategoria antigasCategorias 	= intent.getParcelableExtra("antigasCategorias");
		ListaCategoria novasCategorias 		= intent.getParcelableExtra("novasCategorias");
		
		this.novasCategorias 	= (novasCategorias == null) 	? new ListaCategoria() : novasCategorias;
		this.antigasCategorias 	= (antigasCategorias == null) 	? new ListaCategoria() : antigasCategorias;
		
		categorias = new ListaCategoria();

		if (!(this.novasCategorias.isEmpty() && this.antigasCategorias.isEmpty()))
		{
			categorias.addAll(this.antigasCategorias);
			categorias.addAll(this.novasCategorias);
		}
		
		carregarCategoriasComImagens(categorias);
		
		// criar adapter que conter� os fragments
		mPaginacaoAdapter = new PaginacaoAdapter(getSupportFragmentManager(), categorias);
		mPaginacaoAdapter.setPageCount(categorias.getNumeroDePaginas());		
		
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        
        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mPaginacaoAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() 
        {
            @Override
            public void onPageSelected(int position) 
            {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });
        
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mPaginacaoAdapter.getCount(); i++) 
        {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mPaginacaoAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        
        // habilita up back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent = new Intent();
		
		final ActionBar actionBar = getSupportActionBar();
		switch(item.getItemId())
		{
			// concluir definicao de categorias
			case R.id.action_concluir: 
				intent.putExtra("categorias", (Parcelable) categorias);
				intent.putExtra("novasCategorias", (Parcelable) novasCategorias);
				intent.putExtra("antigasCategorias", (Parcelable) antigasCategorias);
				this.setResult(RC_DEFINIR_CATS_SUCESSO, intent);
				finish();
				break;
			// adicionar categorias a pagina atual
			case R.id.action_add:
				intent = new Intent(this, SelecionarCategoriasActivity.class);
				startActivityForResult(intent, 1);
				break;
			// adicionar pagina
			case R.id.action_add_2: 
            	mPaginacaoAdapter.addPageCount();
                actionBar.addTab(
                        actionBar.newTab()
                                .setText(mPaginacaoAdapter.getPageTitle(mPaginacaoAdapter.getCount() - 1))
                                .setTabListener(this)); 
				break;
			// deletar pagina atual
			case R.id.action_del:
				deletarPaginaAtual();
				break;
			case R.id.action_cancelar:
				this.setResult(RC_DEFINIR_CATS_CANCELADO);
				finish();
				break;
		    // Respond to the action bar's Up/Home button
		    case android.R.id.home:
		    	finish();
		        break;					
		}
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.		
		getMenuInflater().inflate(R.menu.action_concluir_add_add_deletar_cancelar, menu);
		menu.findItem(R.id.action_add).setTitle(R.string.categoria);
		menu.findItem(R.id.action_add_2).setTitle(R.string.pagina);
		menu.findItem(R.id.action_del).setTitle(R.string.pagina);
		return true;
	}
	
	// callback ao voltar da tela adicionar categorias ou da tela do formulario de categoria
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(resultCode)
		{
			// adicionar categoria com sucesso
			case SelecionarCategoriasActivity.RC_ADD_CAT_SUCESSO: 
				ListaCategoria categorias = data.getParcelableExtra("selecionados"); 
				addNovasCategorias(categorias);
				break;
			// adicionar categoria cancelado
			case SelecionarCategoriasActivity.RC_ADD_CAT_CANCELADO: 
				Toast.makeText(this, "Cancelado!", Toast.LENGTH_SHORT).show();
				break;
			// edi��o n�o persistente sucedida
			case FormularioCategoriaActivity.RC_CAT_EDITADA_NP_SUCESSO: 
				atualizarCategoriaEditadaNP((Categoria) data.getParcelableExtra("categoria"));
				break;
		}
	}	
	
	private void addNovasCategorias(ListaCategoria categorias)
	{
		int paginaAtual = getSupportActionBar().getSelectedTab().getPosition() + 1;
		
		for (int i = 0, length = categorias.size(); i < length; i++)
		{
			Categoria categoria = categorias.get(i);
			categoria.setPagina(paginaAtual);
			
			carregarCategoriaComImagens(categoria);
			
			this.categorias.add(categoria);
			novasCategorias.add(categoria);
		}

		mPaginacaoAdapter.refresh();
		
	}
	
	private void atualizarCategoriaEditadaNP(Categoria categoria)
	{
		categorias.getCategoriaById(categoria.getId()).setAll(categoria);
		mPaginacaoAdapter.refresh();
	}

	private void deletarPaginaAtual()
	{
		if (!(categorias.isEmpty()))
		{
			ActionBar actionBar = getSupportActionBar();
			int numeroDePaginas = mPaginacaoAdapter.getCount();
			Tab paginaAtual = actionBar.getSelectedTab();
			
			deletarCategorias(paginaAtual.getPosition() + 1);
			
			if (numeroDePaginas > 1)
			{
				redefinirNumeracaoPaginacaoParaCategorias(paginaAtual.getPosition() + 1, numeroDePaginas);			
				actionBar.removeTab(paginaAtual);
				// resetar os items do adapter porque a referencia antiga n�o vale mais
				mPaginacaoAdapter.setCategorias(categorias);
				mPaginacaoAdapter.subPageCount();
				redefinirTitulosDasPaginas();
			}
			else
				mPaginacaoAdapter.refresh();
		}
	}	
	
	private void deletarCategorias(int pagina)
	{
		ListaCategoria categoriasARemover = new ListaCategoria();
		int i, length, id;
		for (i = 0, length = categorias.size(); i < length; i++)
		{
			Categoria categoria = categorias.get(i);
			if (pagina == categoria.getPagina())
				categoriasARemover.add(categoria);
		}
		
		if (categoriasARemover.size() > 0)
		{
			for (i = 0, length = categoriasARemover.size(); i < length; i++)
			{
				Categoria categoria = categoriasARemover.get(i);
				id = categoria.getId();
				categorias.remove(categoria);
				novasCategorias.removeById(id);
				antigasCategorias.removeById(id);
			}
		}

	}
	
	private void redefinirNumeracaoPaginacaoParaCategorias(int paginaRemovida, int numeroDePaginas)
	{
		ListaCategoria categorias_repaginadas 			= new ListaCategoria();
		ListaCategoria novas_categorias_repaginadas 	= new ListaCategoria();
		ListaCategoria categorias_antigas_repaginadas 	= new ListaCategoria();
	
		int id;
		
		// adicionar as categorias anteriores � pagina removida
		for (int i = 0, length = categorias.size(); i < length; i++)
		{
			Categoria categoria = categorias.get(i);
			if (categoria.getPagina() < paginaRemovida)
			{
				categorias_repaginadas.add(categoria);
				
				id = categoria.getId();
				if (novasCategorias.getCategoriaById(id) != null)
					novas_categorias_repaginadas.add(categoria);
				else if (antigasCategorias.getCategoriaById(id) != null)
					categorias_antigas_repaginadas.add(categoria);
			}
		}

		
		// alterar a pagina das categorias a partir da pagina removida
		// subtraindo 1
		for (int nPagina = paginaRemovida + 1; nPagina <= numeroDePaginas; nPagina++ )
		{
			for (int j = 0, length = categorias.size(); j < length; j++)
			{
				Categoria categoria = categorias.get(j);
				if (nPagina == categoria.getPagina())
				{
					Categoria categoriaCopia = new Categoria(categoria);
					categoriaCopia.setPagina(categoria.getPagina() - 1);
					categorias_repaginadas.add(categoriaCopia);
					
					id = categoria.getId();
					if (novasCategorias.getCategoriaById(id) != null)
						novas_categorias_repaginadas.add(categoriaCopia);
					else if (antigasCategorias.getCategoriaById(id) != null)
						categorias_antigas_repaginadas.add(categoriaCopia);					
				}
			}
		}
		
		categorias 			= categorias_repaginadas;
		novasCategorias 	= novas_categorias_repaginadas;
		antigasCategorias 	= categorias_antigas_repaginadas;
	}
	
	private void redefinirTitulosDasPaginas()
	{
		ActionBar actionBar = getSupportActionBar();
        for (int i = 0, length = mPaginacaoAdapter.getCount(); i < length; i++) 
        {
    		Tab tab = actionBar.getTabAt(i);
    		tab.setText(mPaginacaoAdapter.getPageTitle(i));
        }
	}
	
	private void carregarCategoriasComImagens(ListaCategoria categorias)
	{
		Categoria categoria;
		CategoriaDAO cat_dao = new CategoriaDAO(this);
		cat_dao.open();
		for (int i = 0, length = categorias.size(); i < length; i++)
		{
			categoria = categorias.get(i);
			categoria.setImagens(cat_dao.getImagens(categoria.getId()));
		}
		cat_dao.close();
	}
	
	private void carregarCategoriaComImagens(Categoria categoria)
	{
		CategoriaDAO cat_dao = new CategoriaDAO(this);
		cat_dao.open();
		categoria.setImagens(cat_dao.getImagens(categoria.getId()));
		cat_dao.close();
	}	
	
	@Override
	public void onDeleteItem(int id)
	{
		// Auto-generated method stub
		for (Categoria categoria : categorias)
		{
			if (id == categoria.getId())
			{
				categorias.remove(categoria);
				novasCategorias.remove(categoria);
				antigasCategorias.remove(categoria);
				return;
			}
		}		
	}

	@Override
	public boolean onDeleteItem(Categoria categoria)
	{
		// Auto-generated method stub
		int id = categoria.getId();
		categorias.remove(categoria);
		novasCategorias.removeById(id);
		antigasCategorias.removeById(id);
		
		return true;
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1)
	{
		// Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1)
	{
		// Auto-generated method stub
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1)
	{
		// Auto-generated method stub
		
	}

	@Override
	public boolean onDeleteItem(Categoria categoria, int num_encontrados)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onEditItem(Categoria categoria)
	{
		Toast.makeText(this, "Voc� clicou no bot�o Editar!", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, FormularioCategoriaActivity.class);
		
		
		
		// popular imagens na categoria
		CategoriaDAO dao_cat = new CategoriaDAO(this);
		
		dao_cat.open();
		categoria.setImagens(dao_cat.getImagens(categoria.getId()));
		dao_cat.close();
		
		intent.putExtra("categoria", categoria);
		
		intent.putExtra("tipo_form", FormularioBaseActivity.FORM_ALTERAR_NP);
		startActivityForResult(intent, 3);
	}
	
}
