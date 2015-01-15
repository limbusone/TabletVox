package com.example.tabletvox03f.management;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class OpcoesActivity extends ActionBarActivity
{
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new OpcoesFragment())
                .commit();
    }
	
	
}
