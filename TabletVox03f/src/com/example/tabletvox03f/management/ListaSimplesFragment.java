package com.example.tabletvox03f.management;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.tabletvox03f.R;

public abstract class ListaSimplesFragment extends Fragment
{
	protected ListView lv;
	
	private OnItemClickListener itemClick = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			ListaSimplesFragment.this.acaoDoEventoItemClick(arg0, arg1, arg2, arg3);
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.lista_interface, container, false);
		
		// carregar evento do click em um item da lista
		lv = (ListView) rootView.findViewById(R.id.listView1);
		lv.setOnItemClickListener(itemClick);
		
		onCreateViewFilho();
		
		return rootView;
	}	
	
	protected abstract void onCreateViewFilho();
	
	protected void carregarListaPai()
	{
		lv.setAdapter(carregarLista());
	}
	
	protected ListAdapter getAdapter()
	{
		return lv.getAdapter();
	}
	
	protected abstract BaseAdapter carregarLista();
	
	protected abstract void acaoDoEventoItemClick(AdapterView<?> parent, View v, int position, long arg3);
}
