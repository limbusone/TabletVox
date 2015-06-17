package com.example.tabletvox03f.management;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.tabletvox03f.R;

@SuppressLint("NewApi")
public class OpcoesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.opcoes);
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		// validando valores das opcoes abaixo
		if (key.equals(Opcoes.INTERVALO_TEMPO_TOCAR_FRASE_KEY))
		{
			String valor = sharedPreferences.getString(key, "" + Opcoes.INTERVALO_TEMPO_TOCAR_FRASE_DEFAULT);
			String valor_validado = Integer.toString(Opcoes.validarIntervaloTempoTocarFrase(valor));
			Editor editor = sharedPreferences.edit();
			editor.putString(key, valor_validado);
			editor.commit();
		}
		else if (key.equals(Opcoes.INTERVALO_TEMPO_VARREDURA_KEY))
		{
			String valor = sharedPreferences.getString(key, "" + Opcoes.INTERVALO_TEMPO_VARREDURA_DEFAULT);
			String valor_validado = Integer.toString(Opcoes.validarIntervaloTempoVarredura(valor));
			Editor editor = sharedPreferences.edit();
			editor.putString(key, valor_validado);
			editor.commit();			
		}
		
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}
	
	

}
