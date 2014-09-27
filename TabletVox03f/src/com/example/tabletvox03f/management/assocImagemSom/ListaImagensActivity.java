package com.example.tabletvox03f.management.assocImagemSom;

import java.util.ArrayList;

import android.content.Intent;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.AssocImagemSom;
import com.example.tabletvox03f.dal.AssocImagemSomDAOSingleton;
import com.example.tabletvox03f.management.ListaManageActivity;

public class ListaImagensActivity extends ListaManageActivity
{

	@Override
	protected void onCreateFilho()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected BaseAdapter carregarLista()
	{
		ArrayList<AssocImagemSom> lista = AssocImagemSomDAOSingleton.getInstance().getAssocImagemSoms();
		
		return (new ItemAssocImagemSomAdapter(this, (ArrayList<AssocImagemSom>) lista.clone()));
	}
	
	protected void carregarLista(ArrayList<AssocImagemSom> imagens)
	{
		lv.setAdapter(new ItemAssocImagemSomAdapter(this, (ArrayList<AssocImagemSom>) imagens.clone()));
	}	

	@Override
	protected void acaoDoEventoItemClick(AdapterView<?> parent, View v,
			int position, long arg3)
	{
		// TODO Auto-generated method stub

	}

	// recarregar/carregar a lista ao voltar para esse activity
	@Override
	protected void onResume()
	{
		onResumeSuper();
	}	
	
	@Override
	protected void acaoDosEventosDoMenu(MenuItem item)
	{
		if (item.getItemId() == R.id.action_criar)
		{
			// chamar activity criar perfil
			Intent intent = new Intent(this, FormularioAssocImagemSomActivity.class);
			intent.putExtra("tipo_form", true); // true, para form do tipo 'criar' e false para form do tipo 'editar'
			startActivityForResult(intent, 1);
		}
	}

	@Override
	protected void acaoDoEventoBuscar(Editable s)
	{
		String texto_para_pesquisa = s.toString();
		carregarLista(AssocImagemSomDAOSingleton.getInstance().getImagensByDesc(texto_para_pesquisa));
	}

	@Override
	protected String getOptionMenuTitle()
	{
		return "Criar Imagem";
	}

	@Override
	protected int getMenuID()
	{
		return R.menu.um_action_add;
	}

}
