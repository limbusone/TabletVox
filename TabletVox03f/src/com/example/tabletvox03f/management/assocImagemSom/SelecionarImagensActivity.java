package com.example.tabletvox03f.management.assocImagemSom;

import java.util.ArrayList;

import android.content.Intent;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.example.tabletvoxdemo.R;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSomDAO;

public class SelecionarImagensActivity extends ListaImagensActivity
{

	public static final int RC_ADD_IMG_SUCESSO 		= 1;
	public static final int RC_ADD_IMG_CANCELADO 	= 2;
	
	@Override
	protected void onCreateFilho()
	{

	}

	// recarregar/carregar a lista ao voltar para esse activity
	@Override
	protected void onResume()
	{
		onResumeSuper();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected BaseAdapter carregarLista()
	{
		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(this);  
		
		dao_ais.open();
		ArrayList<AssocImagemSom> lista = dao_ais.getImagens();
		dao_ais.close();
		
		return (new ItemSelectAssocImagemSomAdapter(this, (ArrayList<AssocImagemSom>) lista.clone()));
	}
	
	protected void carregarLista(ArrayList<AssocImagemSom> imagens)
	{
		lv.setAdapter(new ItemSelectAssocImagemSomAdapter(this, imagens));
	}

	@Override
	protected void acaoDoEventoItemClick(AdapterView<?> parent, View v,
			int position, long arg3)
	{
		// selecionar/desselecionar checkbox
		CheckBox checkbox = (CheckBox) v.findViewById(R.id.checkBox);
		checkbox.performClick();
	}

	@Override
	protected void acaoDosEventosDoMenu(MenuItem item)
	{
		switch (item.getItemId())
		{
		// resgata array de selecionados do adapter e manda uma cópia para a tela de lista de imagens da categoria
		case R.id.action_concluir:
			ArrayList<AssocImagemSom> selecionados = ((ItemSelectAssocImagemSomAdapter)lv.getAdapter()).getSelecionados();
			Intent intent = new Intent();
			intent.putParcelableArrayListExtra("selecionados", (ArrayList<AssocImagemSom>) selecionados.clone());
			this.setResult(RC_ADD_IMG_SUCESSO, intent);
			finish();			
			break;
		// cancela a ação e volta para tela de lista de imagens da categoria
		case R.id.action_cancelar:
			this.setResult(RC_ADD_IMG_CANCELADO);
			finish();			
			break;
		case R.id.action_gerenciar:
			Intent intent_lia = new Intent(this, ListaImagensActivity.class);
			startActivityForResult(intent_lia, 1);
			break;
		}
	}

	@Override
	protected void acaoDoEventoBuscar(Editable s)
	{
		String texto_para_pesquisa = s.toString();
		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(this);  
		
		dao_ais.open();
		carregarLista(dao_ais.getImagensByDesc(texto_para_pesquisa));
		dao_ais.close();
	}

	@Override
	protected String getOptionMenuTitle()
	{
		return "";
	}

	@Override
	protected int getMenuID()
	{
		return R.menu.action_concluir_cancelar_manage;
	}
	
	// callback ao voltar da tela listar imagens
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == ListaImagensActivity.RC_IMGS_GERENCIADAS);
	}		

}
