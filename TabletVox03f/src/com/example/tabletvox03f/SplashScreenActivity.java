package com.example.tabletvox03f;
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
						// abrindo main menu
						Intent intent = new Intent(SplashScreenActivity.this, MainMenuActivity.class);
						startActivity(intent);
						
						finish();
					}
				}, SPLASH_TIME_OUT);
			}
		}).start();		
	}
	
}
