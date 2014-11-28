package com.example.tabletvox03f.management;

import android.app.Activity;
import android.os.Bundle;

public class OpcoesActivity extends Activity
{
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
