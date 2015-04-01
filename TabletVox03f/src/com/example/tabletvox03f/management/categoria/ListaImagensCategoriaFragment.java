package com.example.tabletvox03f.management.categoria;


import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.management.ListaSimplesFragment;
import com.example.tabletvox03f.management.assocImagemSom.ItemDeleteAssocImagemSomAdapter;

public class ListaImagensCategoriaFragment extends ListaSimplesFragment
{
	public static final String KEY_IMAGENS = "imagens";
	
	private ArrayList<AssocImagemSom> imagens;
	
	@Override
	protected void onCreateViewFilho()
	{
		Bundle args = getArguments();
		
		imagens = args.getParcelableArrayList(KEY_IMAGENS);
		
		carregarListaPai();
	}

	@Override
	protected BaseAdapter carregarLista()
	{
		ArrayList<AssocImagemSom> lista = imagens;
		
		return new ItemDeleteAssocImagemSomAdapter(getActivity(), (ArrayList<AssocImagemSom>) lista.clone());
	}

	@Override
	protected void acaoDoEventoItemClick(AdapterView<?> parent, View v,
			int position, long arg3)
	{
		// Auto-generated method stub

	}
	
}
