package com.example.tabletvox03f.management.assocImagemSom;

import java.util.ArrayList;

import android.content.Intent;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.AssocImagemSom;
import com.example.tabletvox03f.dal.AssocImagemSomDAO;
import com.example.tabletvox03f.management.ListaComBuscaManageActivity;

public class ListaImagensActivity extends ListaComBuscaManageActivity
{

	@Override
	protected void onCreateFilho()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected BaseAdapter carregarLista()
	{
		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(this);  
		
		dao_ais.open();
		ArrayList<AssocImagemSom> lista = dao_ais.getAll();
		dao_ais.close();
		
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
			intent.putExtra("tipo_form", 0); // 0, para form do tipo 'criar' e 1 para form do tipo 'editar'
			startActivityForResult(intent, 1);
		}
	}

	@Override
	protected void acaoDoEventoBuscar(Editable s)
	{
		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(this); 
		String texto_para_pesquisa = s.toString();
		
		dao_ais.open();
		carregarLista(dao_ais.getAISListbyDesc(texto_para_pesquisa));
		dao_ais.close();
	}

	@Override
	protected String getOptionMenuTitle()
	{
		return "Imagem";
	}

	@Override
	protected int getMenuID()
	{
		return R.menu.um_action_add;
	}

	// callback ao voltar da tela adicionar / editar imagem
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == 1)
			Toast.makeText(this, "Imagem incluida com sucesso!", Toast.LENGTH_SHORT).show();
		else if (resultCode == 2)
			Toast.makeText(this, "Imagem editada com sucesso", Toast.LENGTH_SHORT).show();
	}		
}
