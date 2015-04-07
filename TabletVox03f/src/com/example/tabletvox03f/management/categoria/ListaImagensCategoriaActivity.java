package com.example.tabletvox03f.management.categoria;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.categoria.Categoria;
import com.example.tabletvox03f.dal.categoria.CategoriaDAO;
import com.example.tabletvox03f.management.OnImagemSelectedListener;
import com.example.tabletvox03f.management.PaginacaoAdapter;
import com.example.tabletvox03f.management.assocImagemSom.SelecionarImagensActivity;

public class ListaImagensCategoriaActivity extends ActionBarActivity implements ActionBar.TabListener, OnImagemSelectedListener
{
	
	public static final int RC_DEFINIR_IMGS_SUCESSO 	= 3;
	public static final int RC_DEFINIR_IMGS_CANCELADO 	= 4;

	private PaginacaoAdapter mPaginacaoAdapter;
	
	private ViewPager mViewPager;
	
	private ArrayList<AssocImagemSom> imagens;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pager);
		
		// receber categoria por intent
		Intent intent = getIntent();
		Categoria categoria = (Categoria) intent.getParcelableExtra("categoria");
	
		imagens = (categoria.getImagens() == null) ? new ArrayList<AssocImagemSom>() : categoria.getImagens();
			
		// criar adapter que conterá os fragments
		mPaginacaoAdapter = new PaginacaoAdapter(getSupportFragmentManager(), imagens);
		mPaginacaoAdapter.setPageCount(getNumeroDePaginas());
		
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
		
		switch(item.getItemId())
		{
			case R.id.action_concluir:
//				ArrayList<AssocImagemSom> imagens = ((ItemDeleteAssocImagemSomAdapter)lv.getAdapter()).getItems();
				intent.putParcelableArrayListExtra("imagens", (ArrayList<AssocImagemSom>) imagens.clone());			
				this.setResult(RC_DEFINIR_IMGS_SUCESSO, intent);
				finish();
				break;
			case R.id.action_add:
				intent = new Intent(this, SelecionarImagensActivity.class);
				startActivityForResult(intent, 1);
				break;
			case R.id.action_add_2:
            	mPaginacaoAdapter.addPageCount();
                final ActionBar actionBar = getSupportActionBar();
                actionBar.addTab(
                        actionBar.newTab()
                                .setText(mPaginacaoAdapter.getPageTitle(mPaginacaoAdapter.getCount() - 1))
                                .setTabListener(this)); 
				break;
			case R.id.action_cancelar:
				this.setResult(RC_DEFINIR_IMGS_CANCELADO);
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
		getMenuInflater().inflate(R.menu.action_concluir_add_add_cancelar, menu);
		menu.findItem(R.id.action_add).setTitle(R.string.imagem);
		menu.findItem(R.id.action_add_2).setTitle(R.string.pagina);
		return true;
	}
	
	// callback ao voltar da tela adicionar imagens
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		// adicionar imagem com sucesso
		if (resultCode == SelecionarImagensActivity.RC_ADD_IMG_SUCESSO)
		{
			ArrayList<AssocImagemSom> imagens = data.getParcelableArrayListExtra("selecionados"); 
			addNovasImagens(imagens);
		}
			
		// adicionar imagem cancelado
		if (resultCode == SelecionarImagensActivity.RC_ADD_IMG_CANCELADO)
			Toast.makeText(this, "Cancelado!", Toast.LENGTH_SHORT).show();
	}

	private void addNovasImagens(ArrayList<AssocImagemSom> imagens)
	{
//		ItemDeleteAssocImagemSomAdapter isaisa = (ItemDeleteAssocImagemSomAdapter) lv.getAdapter();
		int paginaAtual = getSupportActionBar().getSelectedTab().getPosition() + 1;
		
		for (int i = 0, length = imagens.size(); i < length; i++)
		{
			AssocImagemSom imagem = imagens.get(i);
			imagem.setPagina(paginaAtual);
			this.imagens.add(imagem);
		}
//			isaisa.addItem(imagens.get(i));
//
//		isaisa.refresh();
		mPaginacaoAdapter.refresh();
		
	}

	@Override
	public void onDeleteItem(int id)
	{
		// Auto-generated method stub
		for (AssocImagemSom imagem : imagens)
		{
			if (id == imagem.getId())
			{
				imagens.remove(imagem);
				return;
			}
		}
		
	}

	@Override
	public void onDeleteItem(AssocImagemSom imagem)
	{
		// Auto-generated method stub
		imagens.remove(imagem);
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1)
	{
		// Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction)
	{
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1)
	{
		// Auto-generated method stub
		
	}
	
	private int getNumeroDePaginas(int categoriaId)
	{
		int num;
		
		if (categoriaId != 0)
		{
			CategoriaDAO dao_cat = new CategoriaDAO(this);
			
			dao_cat.open();
			num = dao_cat.getNumeroDePaginas(categoriaId);
			dao_cat.close();
		}
		else
			return 1;
		
		if (num == 0)
			num = 1;
		
		return num;
	}
	
	private int getNumeroDePaginas()
	{
		if (imagens.isEmpty())
			return 1;
		else
		{
			int maxPage = imagens.get(0).getPagina();
			for (int i = 1, length = imagens.size(); i < length; i++)
			{
				AssocImagemSom imagem = imagens.get(i);
				if (imagem.getPagina() > maxPage)
					maxPage = imagem.getPagina();
			}
			
			return maxPage;
		}
	}

}
