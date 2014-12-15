package com.example.tabletvox03f.management.perfil;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.Utils;
import com.example.tabletvox03f.dal.perfil.Perfil;
import com.example.tabletvox03f.dal.perfil.PerfilDAO;
import com.example.tabletvox03f.management.ListaComBuscaManageActivity;
import com.example.tabletvox03f.management.Opcoes;

public class SelecionarPerfilActivity extends ListaComBuscaManageActivity
{

	@Override
	protected void onCreateFilho()
	{
		// habilita up back
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	protected void acaoDoEventoItemClick(AdapterView<?> parent, View v,
			int position, long arg3)
	{
		Perfil pfl = (Perfil) parent.getItemAtPosition(position);
//		Toast.makeText(SelecionarPerfilActivity.this, "item: " + pfl.getNome(), Toast.LENGTH_SHORT).show();
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
	protected BaseAdapter carregarLista()
	{
		PerfilDAO dao_pfl = new PerfilDAO(this);  
		
		dao_pfl.open();
		ArrayList<Perfil> lista = dao_pfl.getAll();
		dao_pfl.close();
		
		return (new ItemPerfilAdapter(this, (ArrayList<Perfil>) lista.clone()));
	}
	
	protected void carregarLista(ArrayList<Perfil> perfis)
	{
		lv.setAdapter(new ItemPerfilAdapter(this, (ArrayList<Perfil>) perfis.clone()));
	}
	
	private void selecionarPerfil(Perfil perfil)
	{
		Utils.PERFIL_ATIVO = perfil;
		SharedPreferences settings = getSharedPreferences(Opcoes.SETTINGS_NAME, 0);
		settings.edit().putInt(Opcoes.PERFIL_DEFAULT_KEY, perfil.getId()).commit();
		setResult(1);
		finish();
	}
	
	// callback ao voltar da tela adicionar /editar perfil
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == 1)
			Toast.makeText(this, "Perfil incluido com sucesso!", Toast.LENGTH_SHORT).show();
		else if (resultCode == 2)
			Toast.makeText(this, "Perfil editado com sucesso", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void acaoDosEventosDoMenu(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_criar:
				// chamar activity criar perfil
				Intent intent = new Intent(this, FormularioPerfilActivity.class);
				intent.putExtra("tipo_form", Utils.FORM_INCLUIR); // 0, para form do tipo 'criar' e 1 para form do tipo 'editar'
				startActivityForResult(intent, 1);
				break;
		    // Respond to the action bar's Up/Home button
		    case android.R.id.home:
		        NavUtils.navigateUpFromSameTask(this);
		        break;
		}
	}

	@Override
	protected String getOptionMenuTitle()
	{
		return "Perfil";
	}

	@Override
	protected int getMenuID()
	{
		return R.menu.um_action_add;
	}

	// buscar por nome e autor
	@Override
	protected void acaoDoEventoBuscar(Editable s)
	{
		PerfilDAO dao_pfl = new PerfilDAO(this);
		String texto_para_pesquisa = s.toString();
		
		dao_pfl.open();
		carregarLista(dao_pfl.getPerfisByNomeOrAutor(texto_para_pesquisa, texto_para_pesquisa));
		dao_pfl.close();
	}

}
