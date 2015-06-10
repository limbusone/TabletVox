package com.example.tabletvox03f.management;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.tabletvox03f.R;

public class OpcoesActivityOldApi extends PreferenceActivity
{
    @SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.opcoes);

    }
}
