package com.example.tabletvox03f.management.assocImagemSom;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.tabletvoxdemo.R;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSomDAO;
import com.example.tabletvox03f.management.FormularioBaseActivity;

public class SelecionarImagemActivity extends ListaImagensActivity
{
	
	public static final int RC_SELECIONAR_IMG_SUCESSO 	= 1;
	
	public static final int RC_SELECIONAR_IMG_CANCELADO = 2;	
	
	@Override
	protected void onCreateFilho()
	{
		// habilita up back
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	// resgatar imagem e mandá-la para o formulario de categorias
	@Override
	protected void acaoDoEventoItemClick(AdapterView<?> parent, View v,
			int position, long arg3)
	{
		AssocImagemSom ais = (AssocImagemSom) parent.getItemAtPosition(position);
		Intent data = new Intent();
		data.putExtra("ais", ais);
		this.setResult(RC_SELECIONAR_IMG_SUCESSO, data);
		
		finish();
	}
	
	@Override
	protected BaseAdapter carregarLista()
	{
		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(this);  
		
		dao_ais.open();
		ArrayList<AssocImagemSom> lista = dao_ais.getImagens();
		dao_ais.close();
		
		return (new ItemSimplesAssocImagemSomAdapter(this, (ArrayList<AssocImagemSom>) lista.clone()));
	}
	
	@Override
	protected void carregarLista(ArrayList<AssocImagemSom> imagens)
	{
		lv.setAdapter(new ItemSimplesAssocImagemSomAdapter(this, (ArrayList<AssocImagemSom>) imagens.clone()));
	}	
	
	@Override
	protected void acaoDosEventosDoMenu(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_add:
				// chamar activity criar imagem
				Intent intent = new Intent(this, FormularioAssocImagemSomActivity.class);
				intent.putExtra("tipo_form", FormularioBaseActivity.FORM_INCLUIR); // 0, para form do tipo 'criar' e 1 para form do tipo 'editar'
				startActivityForResult(intent, 1);			
				break;		
			// cancela a ação e volta pro formulario
			case R.id.action_cancelar:
				this.setResult(RC_SELECIONAR_IMG_CANCELADO);
				finish();
				break;
		    // Respond to the action bar's Up/Home button
		    case android.R.id.home:
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
		return R.menu.action_add_cancelar;
	}
	
	// callback ao voltar da tela adicionar
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == FormularioAssocImagemSomActivity.RC_IMG_INCLUIDA_SUCESSO)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			// conteudo da dialog
			builder.setMessage(getString(R.string.add_img_recem_criada));
			builder.setTitle(getString(R.string.add_imagem));
			
			final Intent ais = data;
			
			// botoes
			
			// botao sim
			// mandar imagem recem criada para o formulario de categorias
			builder.setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() 
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					SelecionarImagemActivity.this.setResult(RC_SELECIONAR_IMG_SUCESSO, ais);
					finish();
				}
			});
			
			// botao nao
			builder.setNegativeButton(getString(R.string.nao), new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					return;
				}
			});
			
			AlertDialog dialog = builder.create();
			
			dialog.show();			
			
			Toast.makeText(this, getString(R.string.img_incluida_com_sucesso), Toast.LENGTH_SHORT).show();
		}
	}		
	
}
