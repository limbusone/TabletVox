package com.example.tabletvox03f;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
		
		// carrega dados iniciais do aplicativo caso este n�o esteja j� carregado
		if (!(Utils.isAppCarregado(this)))
		{
			setContentView(R.layout.loading_interface);

			// thread de carregamento inicial dos dados do aplicativo
			new Thread(new Runnable()
			{

				@Override
				public void run()
				{
					Utils.carregarDadosIniciais(SplashScreenActivity.this);

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
		else // caso j� esteja carregado, simplesmente vai para o menu principal
			abrirMainMenu();

	}
	
	private void abrirMainMenu()
	{
		Utils.inicializarPerfilDefault(this);
		
        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(SplashScreenActivity.this).build();
        ImageLoader.getInstance().init(config);		
		
		Intent intent = new Intent(this, MainMenuActivity.class);
		startActivity(intent);
		
		finish();
	}
	
}
