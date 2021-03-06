package com.example.tabletvox03f.management.assocImagemSom;

import java.util.ArrayList;

import android.content.Intent;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.tabletvox03f.Erro;
import com.example.tabletvox03f.R;
import com.example.tabletvox03f.Utils;
import com.example.tabletvox03f.dal.FilesIO;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSomDAO;
import com.example.tabletvox03f.management.FormularioBaseActivity;
import com.example.tabletvox03f.management.ListaComBuscaManageActivity;
import com.example.tabletvox03f.management.OnImagemSelectedListener;

public class ListaImagensActivity extends ListaComBuscaManageActivity implements OnImagemSelectedListener
{

	public static final int RC_IMGS_GERENCIADAS = 1;
	
	
	@Override
	protected void onCreateFilho()
	{
		// habilita up back
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected BaseAdapter carregarLista()
	{
		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(this);  
		
		dao_ais.open();
		ArrayList<AssocImagemSom> lista = dao_ais.getImagens();
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
		switch (item.getItemId())
		{
			case R.id.action_criar:
				// chamar activity criar imagem
				Intent intent = new Intent(this, FormularioAssocImagemSomActivity.class);
				intent.putExtra("tipo_form", FormularioBaseActivity.FORM_INCLUIR); // 0, para form do tipo 'criar' e 1 para form do tipo 'editar'
				startActivityForResult(intent, 1);
				break;
		    // Respond to the action bar's Up/Home button
		    case android.R.id.home:
		    	this.setResult(RC_IMGS_GERENCIADAS);
		    	finish();
		        break;
		}

	}

	@Override
	protected void acaoDoEventoBuscar(Editable s)
	{
		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(this); 
		String texto_para_pesquisa = s.toString();
		
		dao_ais.open();
		carregarLista(dao_ais.getImagensByDesc(texto_para_pesquisa));
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
		
		if (resultCode == FormularioAssocImagemSomActivity.RC_IMG_INCLUIDA_SUCESSO)
			Toast.makeText(this, "Imagem incluida com sucesso!", Toast.LENGTH_SHORT).show();
		else if (resultCode == FormularioAssocImagemSomActivity.RC_IMG_EDITADA_SUCESSO)
			Toast.makeText(this, "Imagem editada com sucesso", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDeleteItem(int id)
	{
		
	}

	@Override
	public boolean onDeleteItem(AssocImagemSom imagem)
	{
		return false;
	}

	@Override
	public boolean onDeleteItem(AssocImagemSom imagem, int num_encontrados)
	{
		FilesIO fio = new FilesIO(this);
		if (!(fio.deletarArquivosDeImagemESom(imagem)))
		{
			Utils.limpaErros();
			Utils.erros.add(new Erro("Erro ao excluir o arquivo de imagem ou de som. Exclus�o cancelada!"));
			Utils.exibirErros(this);
			return false;
		}
		
		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(this);
		dao_ais.open();
		dao_ais.delete(imagem.getId());
		dao_ais.close();		
		
		atualizarLblNumEncontrados(--num_encontrados);
		
		Toast.makeText(this, 
		"Excluido com sucesso! ID: " + Integer.toString(imagem.getId()), 
		Toast.LENGTH_SHORT).show();
		
		return true;
	}

	@Override
	public void onEditItem(AssocImagemSom imagem)
	{
		Toast.makeText(this, "Voc� clicou no bot�o Editar!", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, FormularioAssocImagemSomActivity.class);
		intent.putExtra("tipo_form", FormularioBaseActivity.FORM_ALTERAR);
		intent.putExtra("ais", imagem);
		startActivity(intent);
		
	}		
}
