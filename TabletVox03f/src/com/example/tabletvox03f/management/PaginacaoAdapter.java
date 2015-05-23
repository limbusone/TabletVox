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
import com.example.tabletvox03f.management.perfil.ListaCategoriasPerfilFragment;

public class PaginacaoAdapter extends FragmentStatePagerAdapter
{
	//private Categoria categoria;
	private Perfil perfil;
	private ArrayList<AssocImagemSom> imagens;
	private ArrayList<Categoria> categorias;
	
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
	
	public void subPageCount()
	{
		pageCount--;
		refresh();
	}
	
	public void setImagens(ArrayList<AssocImagemSom> imagens)
	{
		this.imagens = imagens;
	}
	
	public void setCategorias(ArrayList<Categoria> categorias)
	{
		this.categorias = categorias;
	}
	
	public PaginacaoAdapter(FragmentManager fm)
	{
		super(fm);
	}
	
//	public PaginacaoAdapter(FragmentManager fm, Categoria categoria)
//	{
//		super(fm);
//		this.categoria = categoria;
//	}
	
	public PaginacaoAdapter(FragmentManager fm, Perfil perfil)
	{
		super(fm);
		this.perfil = perfil;
	}
	
	public PaginacaoAdapter(FragmentManager fm, ArrayList<?> lista)
	{
		super(fm);
		if (!(lista.isEmpty()))
		{
			if (lista.get(0) instanceof AssocImagemSom)
				this.imagens = (ArrayList<AssocImagemSom>) lista;
			else if (lista.get(0) instanceof Categoria)
				this.categorias = (ArrayList<Categoria>) lista;
		}
		else
		{
			this.imagens = null;
			this.categorias = null;
		}
	}
	
	@Override
	public Fragment getItem(int position)
	{
		Fragment fragment = null;
		if (!(imagens == null))
		{
			// chamar fragment relativo as imagens da categoria
	        fragment = new ListaImagensCategoriaFragment();
	        Bundle args = new Bundle();
	        args.putParcelableArrayList
	        (
	        		ListaImagensCategoriaFragment.KEY_IMAGENS, 
	        		(imagens.isEmpty()) ? new ArrayList<AssocImagemSom>() : getImagensPorPagina(position + 1)
	        );
	        
	        fragment.setArguments(args);
		} 
		else if (categorias != null)
		{
			// chamar fragment relativo as categorias do perfil
			fragment = new ListaCategoriasPerfilFragment();
			Bundle args = new Bundle();
			args.putParcelableArrayList
			(
					ListaCategoriasPerfilFragment.KEY_CATEGORIAS, 
					(categorias.isEmpty()) ? new ArrayList<Categoria>() : getCategoriasPorPagina(position + 1)
			);
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

	private ArrayList<Categoria> getCategoriasPorPagina(int pagina)
	{
		ArrayList<Categoria> lista = new ArrayList<Categoria>();
		for (Categoria categoria : categorias)
			if (pagina == categoria.getPagina())
				lista.add(categoria);
		
		return lista;
	}
	
	@Override
	public int getCount()
	{
		return pageCount;
	}
	
    @Override
    public CharSequence getPageTitle(int position) 
    {
        return "P�gina " + (position + 1);
    }
	
    // metodo que informa para o adapter recriar os fragments toda vez
    // que o m�todo notifyDataSetChanged for chamado
    @Override
    public int getItemPosition(Object item)
    {
    	return POSITION_NONE;
    }
    
	public void refresh()
	{
		// informar o adapter que houve uma mudan�a na lista de paginas (tipo um refresh na lista)
		notifyDataSetChanged();
	}	

}
