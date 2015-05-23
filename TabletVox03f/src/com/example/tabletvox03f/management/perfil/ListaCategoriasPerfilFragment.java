package com.example.tabletvox03f.management.perfil;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.example.tabletvox03f.dal.categoria.Categoria;
import com.example.tabletvox03f.management.ListaSimplesFragment;
import com.example.tabletvox03f.management.categoria.ItemCategoriaAdapter;

public class ListaCategoriasPerfilFragment extends ListaSimplesFragment
{
	public static final String KEY_CATEGORIAS = "categorias";
	
	private ArrayList<Categoria> categorias;
	
	@Override
	protected void onCreateViewFilho()
	{
		Bundle args = getArguments();
		
		categorias = args.getParcelableArrayList(KEY_CATEGORIAS);
		
		carregarListaPai();
	}

	@Override
	protected BaseAdapter carregarLista()
	{
		ArrayList<Categoria> lista = categorias;
		
		return new ItemCategoriaAdapter(getActivity(), (ArrayList<Categoria>) lista.clone());
	}

	@Override
	protected void acaoDoEventoItemClick(AdapterView<?> parent, View v,
			int position, long arg3)
	{
		// Auto-generated method stub

	}

}
