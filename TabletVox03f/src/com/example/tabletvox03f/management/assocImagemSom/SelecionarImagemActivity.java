package com.example.tabletvox03f.management.assocImagemSom;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.AssocImagemSom;

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
