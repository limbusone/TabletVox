package com.example.tabletvox03f.management.categoria;

import java.util.ArrayList;

import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.Categoria;
import com.example.tabletvox03f.dal.FilesIO;
import com.example.tabletvox03f.management.FormularioBaseActivity;

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
		ImageButton imgBItem 	= (ImageButton) findViewById(R.id.imgImagem);
		EditText txtNome 		= (EditText) findViewById(R.id.txtNomeCategoria);
		
		cat = intent.getParcelableExtra("categoria");
		
		imgBItem.setImageDrawable((new FilesIO(this)).getImgItemDrawableFromInternalStorageOrAssets(cat.getAIS()));
		txtNome.setText(cat.getNome());
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
	
	// callback ao voltar da tela selecionar uma imagem
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		// selecionar uma imagem com sucesso
		if (resultCode == 1)
		{
		
		}

		// selecionar uma imagem cancelado
		if (resultCode == 2)
			Toast.makeText(this, "Cancelado!", Toast.LENGTH_SHORT).show();
	}

}
