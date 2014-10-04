package com.example.tabletvox03f.management.assocImagemSom;

import java.util.ArrayList;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.AssocImagemSom;
import com.example.tabletvox03f.dal.AssocImagemSomDAOSingleton;

public class SelecionarImagemActivity extends ListaImagensActivity
{
	
	// resgatar imagem e mand�-la para o formulario de categorias
	@Override
	protected void acaoDoEventoItemClick(AdapterView<?> parent, View v,
			int position, long arg3)
	{
		AssocImagemSom ais = (AssocImagemSom) parent.getItemAtPosition(position);
		Intent data = new Intent();
		data.putExtra("ais", ais);
		this.setResult(1, data);
		
		finish();
	}
	
	@Override
	protected BaseAdapter carregarLista()
	{
		ArrayList<AssocImagemSom> lista = AssocImagemSomDAOSingleton.getInstance().getImagens();
		
		return (new ItemSimplesAssocImagemSomAdapter(this, (ArrayList<AssocImagemSom>) lista.clone()));
	}
	
	@Override
	protected void carregarLista(ArrayList<AssocImagemSom> imagens)
	{
		lv.setAdapter(new ItemSimplesAssocImagemSomAdapter(this, (ArrayList<AssocImagemSom>) imagens.clone()));
	}	
	
	@Override
	protected void acaoDosEventosDoMenu(MenuItem item)
	{
		// cancela a a��o e volta pro formulario
		if (item.getItemId() == R.id.action_cancelar)
		{
			this.setResult(2);
			finish();
		}
	}
	
	@Override
	protected String getOptionMenuTitle()
	{
		return "";
	}

	@Override
	protected int getMenuID()
	{
		return R.menu.action_cancelar;
	}
	
}
