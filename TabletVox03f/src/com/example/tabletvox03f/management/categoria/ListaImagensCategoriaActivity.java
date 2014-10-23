package com.example.tabletvox03f.management.categoria;

import java.util.ArrayList;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.categoria.Categoria;
import com.example.tabletvox03f.management.ListaSimplesManageActivity;
import com.example.tabletvox03f.management.assocImagemSom.ItemDeleteAssocImagemSomAdapter;
import com.example.tabletvox03f.management.assocImagemSom.SelecionarImagensActivity;

public class ListaImagensCategoriaActivity extends ListaSimplesManageActivity
{

	private Categoria categoria; 
	
	@Override
	protected void onCreateFilho()
	{
		Intent intent = getIntent();
		
		categoria = (Categoria) intent.getParcelableExtra("categoria");
		if (categoria.getImagens() == null)
			categoria.setImagens(new ArrayList<AssocImagemSom>());
		
		carregarListaPai();
	}
	
	@Override
	protected void acaoDoEventoItemClick(AdapterView<?> parent, View v,
			int position, long arg3)
	{
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	@Override
	protected BaseAdapter carregarLista()
	{
		ArrayList<AssocImagemSom> lista = categoria.getImagens();
		
		return (new ItemDeleteAssocImagemSomAdapter(this, (ArrayList<AssocImagemSom>) lista.clone()));
	}
	
	@Override
	protected void acaoDosEventosDoMenu(MenuItem item)
	{
		Intent intent = new Intent();
		
		switch(item.getItemId())
		{
			case R.id.action_concluir:
				ArrayList<AssocImagemSom> imagens = ((ItemDeleteAssocImagemSomAdapter)lv.getAdapter()).getItems();
				intent.putParcelableArrayListExtra("imagens", (ArrayList<AssocImagemSom>) imagens.clone());			
				this.setResult(3, intent);
				finish();
				break;
			case R.id.action_add:
				intent = new Intent(this, SelecionarImagensActivity.class);
				startActivityForResult(intent, 1);
				break;
			case R.id.action_cancelar:
				this.setResult(4);
				finish();
				break;
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
		return R.menu.action_concluir_add_cancelar;
	}

	// callback ao voltar da tela adicionar imagens
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		// adicionar imagem com sucesso
		if (resultCode == 1)
		{
			ArrayList<AssocImagemSom> imagens = data.getParcelableArrayListExtra("selecionados"); 
			addNovasImagens(imagens);
		}
			
		// adicionar imagem cancelado
		if (resultCode == 2)
			Toast.makeText(this, "Cancelado!", Toast.LENGTH_SHORT).show();
	}

	private void addNovasImagens(ArrayList<AssocImagemSom> imagens)
	{
		ItemDeleteAssocImagemSomAdapter isaisa = (ItemDeleteAssocImagemSomAdapter) lv.getAdapter();
		for (int i = 0, length = imagens.size(); i < length; i++)
			isaisa.addItem(imagens.get(i));

		isaisa.refresh();
		
	}

}
