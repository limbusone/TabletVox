package com.example.tabletvox03f.management;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.categoria.Categoria;
import com.example.tabletvox03f.dal.perfil.Perfil;
import com.example.tabletvox03f.management.categoria.ListaImagensCategoriaFragment;

public class PaginacaoAdapter extends FragmentStatePagerAdapter
{
	private Categoria categoria;
	private Perfil perfil;
	private ArrayList<AssocImagemSom> imagens;
	
	private int pageCount;
	
	public void setPageCount(int pages)
	{
		this.pageCount = pages;
	}
	
	public void addPageCount()
	{
		pageCount++;
		refresh();
	}
	
	public PaginacaoAdapter(FragmentManager fm)
	{
		super(fm);
	}
	
	public PaginacaoAdapter(FragmentManager fm, Categoria categoria)
	{
		super(fm);
		this.categoria = categoria;
	}
	
	public PaginacaoAdapter(FragmentManager fm, Perfil perfil)
	{
		super(fm);
		this.perfil = perfil;
	}
	
	public PaginacaoAdapter(FragmentManager fm, ArrayList<AssocImagemSom> imagens)
	{
		super(fm);
		this.imagens = imagens;
	}

	@Override
	public Fragment getItem(int position)
	{
		Fragment fragment = null;
		if (!(imagens == null))
		{
	        fragment = new ListaImagensCategoriaFragment();
	        Bundle args = new Bundle();
	        args.putParcelableArrayList
	        (
	        		ListaImagensCategoriaFragment.KEY_IMAGENS, 
	        		(imagens.isEmpty()) ? new ArrayList<AssocImagemSom>() : getImagensPorPagina(position + 1)
	        );
	        
	        fragment.setArguments(args);
		} 
		else if (perfil != null)
		{
			// chamar fragment relativo as categorias do perfil
			fragment = new Fragment();
		}
		else
			fragment = new Fragment();
        
        return fragment;
	}
	
	private ArrayList<AssocImagemSom> getImagensPorPagina(int pagina)
	{
		ArrayList<AssocImagemSom> lista = new ArrayList<AssocImagemSom>();
		for (AssocImagemSom imagem : imagens)
			if (pagina == imagem.getPagina())
				lista.add(imagem);
		
		return lista;
	}

	@Override
	public int getCount()
	{
		return pageCount;
	}
	
    @Override
    public CharSequence getPageTitle(int position) {
        return "Página " + (position + 1);
    }
	
    // metodo que informa para o adapter recriar os fragments toda vez
    // que o método notifyDataSetChanged for chamado
    @Override
    public int getItemPosition(Object item)
    {
    	return POSITION_NONE;
    }
    
	public void refresh()
	{
		// informar o adapter que houve uma mudança na lista de paginas (tipo um refresh na lista)
		notifyDataSetChanged();
	}	

}
