package com.example.tabletvox03f.management.perfil;

import java.util.ArrayList;

import android.app.Activity;
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
import com.example.tabletvox03f.management.FormularioBaseActivity;
import com.example.tabletvox03f.management.ListaComBuscaManageActivity;
import com.example.tabletvox03f.management.OnPerfilSelectedListener;
import com.example.tabletvox03f.management.Opcoes;

public class SelecionarPerfilActivity extends ListaComBuscaManageActivity implements OnPerfilSelectedListener
{

	public static final int RC_PFL_SELECIONADO_SUCESSO = 1;
	
	@Override
	protected void onCreateFilho()
	{
		// habilita up back
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
		setResult(RC_PFL_SELECIONADO_SUCESSO);
		finish();
	}
	
	// callback ao voltar da tela adicionar /editar perfil
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == FormularioPerfilActivity.RC_PFL_INCLUIDA_SUCESSO)
			Toast.makeText(this, R.string.perfil_incluido_sucesso, Toast.LENGTH_SHORT).show();
		else if (resultCode == FormularioPerfilActivity.RC_PFL_EDITADA_SUCESSO)
			Toast.makeText(this, R.string.perfil_editado_sucesso, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void acaoDosEventosDoMenu(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_criar:
				// chamar activity criar perfil
				Intent intent = new Intent(this, FormularioPerfilActivity.class);
				intent.putExtra("tipo_form", FormularioBaseActivity.FORM_INCLUIR); // 0, para form do tipo 'criar' e 1 para form do tipo 'editar'
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
		return getString(R.string.perfil);
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

	@Override
	public void onDeleteItem(int id)
	{
		
	}

	@Override
	public void onDeleteItem(Perfil perfil)
	{
		
	}

	// deleta efetivamente o perfil e atualiza o label dos registros encontrados
	@Override
	public boolean onDeleteItem(Perfil perfil, int num_encontrados)
	{
		// não é possível deletar o perfil default
		if (perfil.getId() == Opcoes.PERFIL_DEFAULT_DEFAULT)
			return false;
		
		Toast.makeText(this, 
		"Excluido com sucesso! ID: " + Integer.toString(perfil.getId()), 
		Toast.LENGTH_SHORT).show();		
		
		PerfilDAO dao_pfl = new PerfilDAO(this);
		
		dao_pfl.open();
		// exclui efetivamente o perfil
		dao_pfl.delete(perfil.getId());
		dao_pfl.close();
		
		atualizarLblNumEncontrados(--num_encontrados);
		
		return true;
	}

	@Override
	public void onEditItem(Perfil perfil)
	{
		// popular categorias no perfil
		PerfilDAO dao_pfl = new PerfilDAO(this);
		
		dao_pfl.open();
		perfil.setCategorias(dao_pfl.getCategorias(perfil.getId()));
		dao_pfl.close();
		
		// chamar form editar
		Intent intent = new Intent(this, FormularioPerfilActivity.class);
		intent.putExtra("tipo_form", FormularioBaseActivity.FORM_ALTERAR); // 1 para 'editar perfil'
		
		intent.putExtra("perfil", perfil);
		startActivityForResult(intent, 2);
		
	}

}
