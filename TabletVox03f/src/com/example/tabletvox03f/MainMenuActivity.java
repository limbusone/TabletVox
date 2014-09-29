package com.example.tabletvox03f;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.tabletvox03f.dal.FilesIO;
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
		
		// inicializa frase global
		Utils.lista_imagens_frase_global = new ArrayList<ImgItem>();
		
		Button cmd_modo_touch 		= (Button) findViewById(R.id.cmdLaunchModoTouch);
		Button cmd_modo_varredura 	= (Button) findViewById(R.id.cmdLaunchModoVarredura);
		
		// evento que starta modo touch
		cmd_modo_touch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Intent intent;
				FilesIO fio = new FilesIO(MainMenuActivity.this);
				
				// verifica-se a existencia do arquivo nomedoperfil_categorias.xml
				// se existir significa que o perfil é categorizado
				//if (Utils.perfil_categorizado_xml(MainMenuActivity.this))
				if (fio.perfil_categorizado_xml_exists())
				{
					//Utils.TELAS_NOME_ARQUIVO_XML_ATIVO = Utils.PERFIL_ATIVO + "_categorias";
					intent = new Intent(MainMenuActivity.this, ModoTouchCategoriasActivity.class);
				} 
				else // perfil nao categorizado
				{
					Utils.TELAS_NOME_ARQUIVO_XML_ATIVO = Utils.PERFIL_ATIVO.getNome();
					intent = new Intent(MainMenuActivity.this, ModoTouchActivity.class);
				}
				
				
				startActivity(intent);
			}
			
		});

		// evento que starta modo varredura
		cmd_modo_varredura.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				
				Intent intent;
				FilesIO fio = new FilesIO(MainMenuActivity.this);
				
				// verifica-se a existencia do arquivo nomedoperfil_categorias.xml
				// se existir significa que o perfil é categorizado
				//if (Utils.perfil_categorizado_xml(MainMenuActivity.this))
				if (fio.perfil_categorizado_xml_exists())
				{
					//Utils.TELAS_NOME_ARQUIVO_XML_ATIVO = Utils.PERFIL_ATIVO + "_categorias";
					intent = new Intent(MainMenuActivity.this, ModoVarreduraCategoriasActivity.class);
				} 
				else // perfil nao categorizado
				{
					Utils.TELAS_NOME_ARQUIVO_XML_ATIVO = Utils.PERFIL_ATIVO.getNome();
					intent = new Intent(MainMenuActivity.this, ModoVarreduraActivity.class);
				}				
				
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
				startActivity(intent);				
				break;
			case R.id.action_gerenciar_imagens:
				intent = new Intent(this, ListaImagensActivity.class);
				startActivity(intent);				
				break;
			case R.id.action_gerenciar_categorias:
				intent = new Intent(this, ListaCategoriasActivity.class);
				startActivity(intent);
				break;
		}
		
		return false;
		
	}	

}
