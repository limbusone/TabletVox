package com.example.tabletvox03f.management.assocImagemSom;

import java.util.ArrayList;

import android.content.Intent;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.AssocImagemSom;
import com.example.tabletvox03f.dal.AssocImagemSomDAOSingleton;
import com.example.tabletvox03f.management.ListaComBuscaManageActivity;

public class SelecionarImagensActivity extends ListaComBuscaManageActivity
{

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
		ArrayList<AssocImagemSom> lista = AssocImagemSomDAOSingleton.getInstance().getImagens();
		
		return (new ItemSelectAssocImagemSomAdapter(this, (ArrayList<AssocImagemSom>) lista.clone()));
	}
	
	private void carregarLista(ArrayList<AssocImagemSom> categorias)
	{
		lv.setAdapter(new ItemSelectAssocImagemSomAdapter(this, categorias));
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
		// resgata array de selecionados do adapter e manda uma cópia para a tela de lista de imagens da categoria 
		if (item.getItemId() == R.id.action_concluir)
		{
			ArrayList<AssocImagemSom> selecionados = ((ItemSelectAssocImagemSomAdapter)lv.getAdapter()).getSelecionados();
			Intent intent = new Intent();
			intent.putParcelableArrayListExtra("selecionados", (ArrayList<AssocImagemSom>) selecionados.clone());
			this.setResult(1, intent);
			finish();
			
		}
		// cancela a ação e volta para tela de lista de imagens da categoria
		else if (item.getItemId() == R.id.action_cancelar)
		{
			this.setResult(2);
			finish();
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
		return "";
	}

	@Override
	protected int getMenuID()
	{
		return R.menu.action_concluir_cancelar;
	}

}
