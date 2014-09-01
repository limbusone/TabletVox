package com.example.tabletvox03f.management;

import java.util.ArrayList;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.Utils;
import com.example.tabletvox03f.dal.Perfil;
import com.example.tabletvox03f.dal.PerfilDAOSingleton;

public class SelecionarPerfilActivity extends ListaManageActivity
{

	@Override
	protected void onCreateFilho()
	{
		
	}
	
	@Override
	protected void acaoDoEventoItemClick(AdapterView<?> parent, View v,
			int position, long arg3)
	{
		// TODO Auto-generated method stub
		Perfil pfl = (Perfil) parent.getItemAtPosition(position);
		Toast.makeText(SelecionarPerfilActivity.this, "item: " + pfl.getNome(), Toast.LENGTH_SHORT).show();
		// selecionar o perfil
		selecionarPerfil(pfl);
	}

	// recarregar/carregar a lista ao voltar para esse activity
	@Override
	protected void onResume()
	{
		onResumeSuper();
	}

	// carregar todos os perfis
	@SuppressWarnings("unchecked")
	@Override
	protected void carregarLista()
	{
		ArrayList<Perfil> lista = PerfilDAOSingleton.getInstance().getPerfis();
		
		lv.setAdapter(new ItemPerfilAdapter(this, (ArrayList<Perfil>) lista.clone()));		
	}
	
	private void selecionarPerfil(Perfil perfil)
	{
		Utils.PERFIL_ATIVO = perfil;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == 1)
			Toast.makeText(this, "Perfil incluido com sucesso!", Toast.LENGTH_SHORT).show();
		else if (resultCode == 2)
			Toast.makeText(this, "Perfil editado com sucesso", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void acaoDosEventosDoMenu(MenuItem item)
	{
		if (item.getItemId() == R.id.action_criar)
		{
			// chamar activity criar perfil
			Intent intent = new Intent(this, FormularioPerfilActivity.class);
			intent.putExtra("tipo_form", true); // true, para form do tipo 'criar' e false para form do tipo 'editar'
			startActivityForResult(intent, 1);
		}
		
	}

	@Override
	protected String getOptionMenuTitle()
	{
		return "Criar Perfil";
	}

	@Override
	protected int getMenuID()
	{
		return R.menu.um_action_add;
	}


	@Override
	protected void acaoDoEventoBuscar(View v, int keyCode, KeyEvent event)
	{
		
	}

}
