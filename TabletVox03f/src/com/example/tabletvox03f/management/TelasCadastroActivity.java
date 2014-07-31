package com.example.tabletvox03f.management;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.XmlUtilsTelas;

public class TelasCadastroActivity extends Activity
{
	private String modo;
	private String modo_title;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_telas_cadastro);
		
		modo = "modo_varredura";
		modo_title = "Modo Varredura";
		
		TextView lblModo = (TextView) findViewById(R.id.lblModo);
		
		lblModo.setText(modo_title);
		
		Button cmdSalvar  = (Button) findViewById(R.id.cmdSalvarTelasCadastro);
		Button cmdExcluir = (Button) findViewById(R.id.cmdDeletarTelasCadastro);
		
		cmdSalvar.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				EditText txtPagina 	 = (EditText) TelasCadastroActivity.this.findViewById(R.id.txtPagina);
				EditText txtIdImagem = (EditText) TelasCadastroActivity.this.findViewById(R.id.txtIdImagem);
				
				String pagina = txtPagina.getText().toString(), id = txtIdImagem.getText().toString();
				
				XmlUtilsTelas xml = new XmlUtilsTelas(TelasCadastroActivity.this, TelasCadastroActivity.this.modo, TelasCadastroActivity.this.modo);
				if (xml.inserirImagemByPage(pagina, id))
					Toast.makeText(TelasCadastroActivity.this, "Incluido com sucesso!", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(TelasCadastroActivity.this, "Página não encontrada", Toast.LENGTH_SHORT).show();
			}
		});
		
		cmdExcluir.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				EditText txtPagina 	 = (EditText) TelasCadastroActivity.this.findViewById(R.id.txtPagina);
				EditText txtIdImagem = (EditText) TelasCadastroActivity.this.findViewById(R.id.txtIdImagem);
				
				String pagina = txtPagina.getText().toString(), id = txtIdImagem.getText().toString();
				
				XmlUtilsTelas xml = new XmlUtilsTelas(TelasCadastroActivity.this, TelasCadastroActivity.this.modo, TelasCadastroActivity.this.modo);
				if (xml.excluirImagemById(pagina, id))
					Toast.makeText(TelasCadastroActivity.this, "Excluido com sucesso!", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(TelasCadastroActivity.this, "Não foi possível excluir", Toast.LENGTH_SHORT).show();					
				
			}
		});
		/*
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
								   R.layout.list_textview, montarArrayPaginas()); 
		ListView lvPaginas = (ListView) findViewById(R.id.lvPaginas);
		lvPaginas.setAdapter(adapter);
		
		lvPaginas.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3)
			{
				
			}
		});
		*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.telas_cadastro, menu);
		return true;
	}
	
//	private String[] montarArrayPaginas()
//	{
//		XmlUtilsTelas xml = new XmlUtilsTelas(this, "modo_touch");
//		int num_paginas = xml.getLastPage(), i = 0;
//		String[] arr = new String[num_paginas];
//		for (; i < num_paginas; i++)
//			arr[i] = "Página " + (i + 1);
//		return arr;
//	}

}
