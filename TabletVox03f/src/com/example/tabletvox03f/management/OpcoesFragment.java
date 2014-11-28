package com.example.tabletvox03f.management;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.tabletvox03f.R;

@SuppressLint("NewApi")
public class OpcoesFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.opcoes);
    }

}
