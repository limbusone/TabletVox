package com.example.tabletvox03f;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tabletvox03f.dal.categoria.ListaCategoria;
import com.example.tabletvox03f.dal.perfil.PerfilDAO;
import com.example.tabletvox03f.management.OpcoesActivity;
import com.example.tabletvox03f.management.assocImagemSom.ListaImagensActivity;
import com.example.tabletvox03f.management.categoria.ListaCategoriasActivity;
import com.example.tabletvox03f.management.perfil.SelecionarPerfilActivity;

public class MainMenuActivity extends Activity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

		mudaTituloConformePerfilSelecionado();
		
		// inicializa frase global
		Utils.lista_imagens_frase_global = new ArrayList<ImgItem>();
		
		ImageButton cmd_modo_touch 		= (ImageButton) findViewById(R.id.cmdLaunchModoTouch);
		ImageButton cmd_modo_varredura 	= (ImageButton) findViewById(R.id.cmdLaunchModoVarredura);
		
		// evento que starta modo touch
		cmd_modo_touch.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v)
			{
				Intent intent;
				long categoriaId = existeSomenteUmaCategoriaNoPerfil();
				if (categoriaId > 0)
				// carrega ModoTouchActivity diretamente
				{
					intent = new Intent(MainMenuActivity.this, ModoTouchActivity.class);
					intent.putExtra("categoriaId", categoriaId);
				}
				else
					intent = new Intent(MainMenuActivity.this, ModoTouchCategoriasActivity.class);
								
				startActivity(intent);
			}
			
		});

		// evento que starta modo varredura
		cmd_modo_varredura.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v)
			{
				Intent intent;
				
				long categoriaId = existeSomenteUmaCategoriaNoPerfil();
				if (categoriaId > 0)
				// carrega ModoVarreduraActivity diretamente
				{
					intent = new Intent(MainMenuActivity.this, ModoVarreduraActivity.class);
					intent.putExtra("categoriaId", categoriaId);
				}
				else				
					intent = new Intent(MainMenuActivity.this, ModoVarreduraCategoriasActivity.class);
				
				startActivity(intent);
			}
			
		});		
		
	
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		Utils.lista_imagens_frase_global.clear(); // limpar lista global quando o aplicativo volta para o menu
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	// trata os eventos ligados ao menu do action bar
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent;
		switch(item.getItemId())
		{
			case R.id.action_selecionar_perfil:
				intent = new Intent(MainMenuActivity.this, SelecionarPerfilActivity.class);
				startActivityForResult(intent, 1);				
				break;
			case R.id.action_gerenciar_imagens:
				intent = new Intent(this, ListaImagensActivity.class);
				startActivity(intent);				
				break;
			case R.id.action_gerenciar_categorias:
				intent = new Intent(this, ListaCategoriasActivity.class);
				startActivity(intent);
				break;
			case R.id.action_opcoes:
				intent = new Intent(this, OpcoesActivity.class);
				startActivity(intent);
				break;
		}
		
		return false;
		
	}
	
	// callback ao voltar da tela selecionar perfil
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode)
		{
			case SelecionarPerfilActivity.RC_PFL_SELECIONADO_SUCESSO: // selecionado com sucesso
				Toast.makeText(MainMenuActivity.this, "Perfil selecionado!", Toast.LENGTH_SHORT).show();
				mudaTituloConformePerfilSelecionado();
				break;
			case 2: // seleção cancelada
			default:
				break;
		}
		
	}
	
	public void mudaTituloConformePerfilSelecionado()
	{
		setTitle(Utils.PERFIL_ATIVO.getNome());
	}
	
	private long existeSomenteUmaCategoriaNoPerfil()
	{
		PerfilDAO dao_pfl = new PerfilDAO(this);
		
		dao_pfl.open();
		ListaCategoria categorias = dao_pfl.getCategorias(Utils.PERFIL_ATIVO.getId());
		dao_pfl.close();
		
		if (categorias.size() > 1)
			return 0;
		else
			// popular perfil ativo com a categoria
			Utils.PERFIL_ATIVO.setCategorias(categorias);
		
		return categorias.get(0).getId();
	}

}
