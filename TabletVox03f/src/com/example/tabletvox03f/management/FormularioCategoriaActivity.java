package com.example.tabletvox03f.management;

import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.Categoria;

public class FormularioCategoriaActivity extends FormularioBaseActivity
{
	
	private Categoria cat;
	
	@Override
	protected int[] getDadosForm()
	{
		int[] dados = {R.layout.formulario_categoria_interface, R.menu.um_action_salvar, 
				R.string.title_activity_criar_categoria, R.string.title_activity_editar_categoria};
		return dados;
	}
	
	@Override
	protected void onCreateFilho()
	{
		
	}
	
	@Override
	protected void initCriarForm()
	{
		// TODO Auto-generated method stub
		cat = new Categoria();
	}

	@Override
	protected void initEditarForm(Intent intent)
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void incluir()
	{
		// TODO Auto-generated method stub
		TextView txtNomeCategoria = (TextView) findViewById(R.id.txtNomeCategoria);
		
		cat.setNome(txtNomeCategoria.getText().toString());
		
		//CategoriaDAOSingleton.getInstance().incluirCategoria(cat);
		
		Toast.makeText(this, "Você clicou em incluir!", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void editar()
	{
		// TODO Auto-generated method stub
		Toast.makeText(this, "Você clicou em editar!", Toast.LENGTH_SHORT).show();
	}

}
