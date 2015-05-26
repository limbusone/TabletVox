package com.example.tabletvox03f.management.perfil;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.example.tabletvox03f.dal.categoria.ListaCategoria;
import com.example.tabletvox03f.management.ListaSimplesFragment;
import com.example.tabletvox03f.management.categoria.ItemCategoriaAdapter;

public class ListaCategoriasPerfilFragment extends ListaSimplesFragment
{
	public static final String KEY_CATEGORIAS = "categorias";
	
	private ListaCategoria categorias;
	
	@Override
	protected void onCreateViewFilho()
	{
		Bundle args = getArguments();
		
		categorias = args.getParcelable(KEY_CATEGORIAS);
		
		carregarListaPai();
	}

	@Override
	protected BaseAdapter carregarLista()
	{
		ListaCategoria lista = categorias;
		
		return new ItemCategoriaAdapter(getActivity(), (ListaCategoria) lista.clone());
	}

	@Override
	protected void acaoDoEventoItemClick(AdapterView<?> parent, View v,
			int position, long arg3)
	{
		// Auto-generated method stub

	}

}
