package com.example.tabletvox03f;
import com.example.tabletvox03f.dal.perfil.PerfilDAO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class SplashScreenActivity extends Activity
{
	
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

	Handler mHandler = new Handler();	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
//		// carrega dados iniciais do aplicativo caso este não esteja já carregado
//		if (!(Utils.isAppCarregado(this)))
//		{
//			setContentView(R.layout.loading_interface);
//
//			// thread de carregamento inicial dos dados do aplicativo
//			new Thread(new Runnable()
//			{
//
//				@Override
//				public void run()
//				{
//					Utils.carregarDadosIniciais(SplashScreenActivity.this);
//
//					// acao pos carregamento
//					mHandler.postDelayed(new Runnable()
//					{
//						public void run()
//						{
//							// abrindo menu principal
//							SplashScreenActivity.this.abrirMainMenu();
//						}
//					}, SPLASH_TIME_OUT);
//				}
//			}).start();
//		}
//		else // caso já esteja carregado, simplesmente vai para o menu principal
//			abrirMainMenu();
		
		setContentView(R.layout.loading_interface);

		// thread de carregamento inicial dos dados do aplicativo
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				if (!(Utils.isAppCarregado(SplashScreenActivity.this)))
					Utils.carregarDadosIniciais(SplashScreenActivity.this);
				
				Utils.inicializarMock(SplashScreenActivity.this);

				// acao pos carregamento
				mHandler.postDelayed(new Runnable()
				{
					public void run()
					{
						// abrindo menu principal
						SplashScreenActivity.this.abrirMainMenu();
					}
				}, SPLASH_TIME_OUT);
			}
		}).start();
		

	}
	
	private void abrirMainMenu()
	{
		// inicializar perfil default		
		PerfilDAO pfl_dao = new PerfilDAO(this);
		pfl_dao.open();
		Utils.PERFIL_ATIVO = pfl_dao.getPerfilById(1);
		pfl_dao.close();
		
		Intent intent = new Intent(this, MainMenuActivity.class);
		startActivity(intent);
		
		finish();
	}
	
}
