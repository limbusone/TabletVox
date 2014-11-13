package com.example.tabletvox03f.management.assocImagemSom;

import java.util.ArrayList;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSomDAO;

public class SelecionarImagemActivity extends ListaImagensActivity
{
	
	// resgatar imagem e mandá-la para o formulario de categorias
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
		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(this);  
		
		dao_ais.open();
		ArrayList<AssocImagemSom> lista = dao_ais.getImagens();
		dao_ais.close();
		
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
		// cancela a ação e volta pro formulario
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
