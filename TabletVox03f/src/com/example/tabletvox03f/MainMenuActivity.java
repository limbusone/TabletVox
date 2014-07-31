package com.example.tabletvox03f;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.tabletvox03f.dal.AssocImagemSomDAO;
import com.example.tabletvox03f.dal.FilesIO;
import com.example.tabletvox03f.management.SelecionarPerfilActivity;

public class MainMenuActivity extends Activity
{
	
	ProgressBar mProgress;
	Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		// inicializa frase global
		Utils.lista_imagens_frase_global = new ArrayList<ImgItem>();
		
		Button cmd_modo_touch 		= (Button) findViewById(R.id.cmdLaunchModoTouch);
		Button cmd_modo_varredura 	= (Button) findViewById(R.id.cmdLaunchModoVarredura);
		Button cmd_mudarPerfil		= (Button) findViewById(R.id.cmdLaunchMudarPerfil);
		
		
		cmd_modo_touch.setVisibility(View.GONE);
		cmd_modo_varredura.setVisibility(View.GONE);
		cmd_mudarPerfil.setVisibility(View.GONE);
		
		
		
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
				// TODO Auto-generated method stub
				
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
		
	
		cmd_mudarPerfil.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainMenuActivity.this, SelecionarPerfilActivity.class);
				startActivity(intent);
			}
		});
		

		
		mProgress = (ProgressBar) findViewById(R.id.progressBarInicializar);
		mProgress.setVisibility(View.VISIBLE);
		findViewById(R.id.lblCarregando).setVisibility(View.VISIBLE);
		
		// thread de carregamento inicial dos dados do aplicativo
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				carregarDadosIniciais();
				
				// acao pos carregamento
				mHandler.post(new Runnable()
				{
					public void run()
					{
						mProgress.setVisibility(View.GONE);
						findViewById(R.id.lblCarregando).setVisibility(View.GONE);
						findViewById(R.id.cmdLaunchModoTouch).setVisibility(View.VISIBLE);
						findViewById(R.id.cmdLaunchModoVarredura).setVisibility(View.VISIBLE);
						findViewById(R.id.cmdLaunchMudarPerfil).setVisibility(View.VISIBLE);
						
						// abrindo formulario para teste
//						Intent intent = new Intent(MainMenuActivity.this, FormularioAssocImagemSomActivity.class);
//						startActivity(intent);
					}
				});
			}
		}).start();
		
	}
	
	private void carregarDadosIniciais()
	{
		// se não houver registros, inicializar o banco
		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(this);
		
		dao_ais.open();
		if (!(dao_ais.regs_exist()))
			Utils.inicializarBD(dao_ais);
		dao_ais.close();
		
		(new FilesIO(this)).copiarArquivosXmlDeAssetsParaInternalStorage();
		
		//Utils.copiarArquivosXmlDeAssetsParaInternalStorage(this);
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

}
