package com.example.tabletvox03f.management;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tabletvox03f.Erro;
import com.example.tabletvox03f.R;
import com.example.tabletvox03f.Utils;
import com.example.tabletvox03f.dal.Categoria;
import com.example.tabletvox03f.dal.Perfil;
import com.example.tabletvox03f.dal.PerfilDAOSingleton;

public class FormularioPerfilActivity extends FormularioBaseActivity
{
	private Perfil pfl;

	private ListView lv;
	
	private Button btnAddCategoria;
	
	private OnClickListener addCategoriaEvento = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// chamar tela para selecionar categorias
			Intent intent = new Intent(FormularioPerfilActivity.this, SelecionarCategoriasActivity.class);
			FormularioPerfilActivity.this.startActivityForResult(intent, 1);
		}
	};
	
	private OnItemClickListener itemClick = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			
		}
	};	
	
	@Override
	protected int[] getDadosForm()
	{
		int[] dados = {R.layout.formulario_perfil_interface, R.menu.um_action_salvar, 
				R.string.title_activity_criar_perfil, R.string.title_activity_editar_perfil};
		
		return dados;
	}
	
	@Override
	protected void onCreateFilho()
	{
		// carregar evento do click em um item da lista
		lv = (ListView) findViewById(R.id.listViewCat);
		lv.setOnItemClickListener(itemClick);
		
		// carregar evento do botão adicionar categoria
		btnAddCategoria = (Button) findViewById(R.id.btnAddCategoria);
		btnAddCategoria.setOnClickListener(addCategoriaEvento);
	}
	
	protected void initCriarForm()
	{
		pfl = new Perfil();
		
		// inicializa categorias caso não vier setada
		// serve para não bugar o metodo carregarLista()		
		if (pfl.getCategorias() == null)
			pfl.setCategorias(new ArrayList<Categoria>());
		
		carregarLista();
	}
	
	protected void initEditarForm(Intent intent)
	{
		EditText txtNome = (EditText) findViewById(R.id.txtNomePerfil);
		EditText txtAutor = (EditText) findViewById(R.id.txtAutorPerfil);
		
		pfl = intent.getParcelableExtra("perfil");
		
//		pfl = new Perfil
//				(
//					intent.getIntExtra("pfl_id", 0), 
//					intent.getStringExtra("pfl_nome"), 
//					intent.getStringExtra("pfl_autor")
//				);
		
		txtNome.setText(pfl.getNome());
		txtAutor.setText(pfl.getAutor());
		
		// inicializa categorias caso não vier setada
		// serve para não bugar o metodo carregarLista()
		if (pfl.getCategorias() == null)
			pfl.setCategorias(new ArrayList<Categoria>());
		
		carregarLista();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void incluir()
	{
		pfl.setNome(((EditText) findViewById(R.id.txtNomePerfil)).getText().toString());
		pfl.setAutor(((EditText) findViewById(R.id.txtAutorPerfil)).getText().toString());
		
		// seta somente se há items 
		pfl.setCategorias((lv.getCount() > 0) ? 
						  (ArrayList<Categoria>)((ItemCategoriaAdapter)lv.getAdapter()).getCategorias().clone() : 
						   null);
		
		retirarErros();
		
		if (!(validarIncluir()))
		{
			Utils.exibirErros(this);
			return;
		}
		
//		Toast.makeText(this, "Você clicou em incluir Perfil!", Toast.LENGTH_SHORT).show();
		
		PerfilDAOSingleton.getInstance().incluirPerfilWithRandomGeneratedID(pfl);
		
//		Intent intent = new Intent(this, SelecionarPerfilActivity.class);
		
		// setando flag que chama um activity existente (se houver)
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		
//		startActivity(intent);
//		finishActivity(3);
		this.setResult(1);
		finish();
		//NavUtils.navigateUpFromSameTask(this);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void editar()
	{
		pfl.setNome(((EditText) findViewById(R.id.txtNomePerfil)).getText().toString());
		pfl.setAutor(((EditText) findViewById(R.id.txtAutorPerfil)).getText().toString());
		
		// seta somente se há items 
		pfl.setCategorias((lv.getCount() > 0) ? 
						  (ArrayList<Categoria>)((ItemCategoriaAdapter)lv.getAdapter()).getCategorias().clone() : 
						   null);
		
		retirarErros();
		
		if (!(validarEditar()))
		{
			Utils.exibirErros(this);
			return;
		}
		
//		Toast.makeText(this, "Você clicou em editar Perfil!", Toast.LENGTH_SHORT).show();
		
		//PerfilDAOSingleton.getInstance().editarPerfil(pfl, pfl.getId());
		PerfilDAOSingleton.getInstance().editarPerfilComCategorias(pfl, pfl.getId());
		
//		Intent intent = new Intent(this, SelecionarPerfilActivity.class);
		
		// setando flag que chama um activity existente (se houver)
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		
//		startActivity(intent);		
//		finishActivity(4);
		this.setResult(2);
		finish();
	}
	
	private boolean validarIncluir()
	{
		boolean retorno = true;
		
		if (pfl.getNome() == null || pfl.getNome().length() == 0)
		{
			Utils.erros.add(new Erro("Campo nome obrigatório!", findViewById(R.id.txtNomePerfil), "EditText"));
			retorno = false;
		}
		
		if (pfl.getAutor() == null || pfl.getAutor().length() == 0)
		{
			Utils.erros.add(new Erro("Campo autor obrigatório!", findViewById(R.id.txtAutorPerfil), "EditText"));
			retorno = false;
		}
		
		return retorno;
	}
	
	private boolean validarEditar()
	{
		boolean retorno = true;

		if (pfl.getNome() == null || pfl.getNome().length() == 0)
		{
			Utils.erros.add(new Erro("Campo nome obrigatório!", findViewById(R.id.txtNomePerfil), "EditText"));
			retorno = false;
		}
		
		if (pfl.getAutor() == null || pfl.getAutor().length() == 0)
		{
			Utils.erros.add(new Erro("Campo autor obrigatório!", findViewById(R.id.txtAutorPerfil), "EditText"));
			retorno = false;
		}		
		
		return retorno;
	}	
	
	@SuppressWarnings("unchecked")
	protected void carregarLista()
	{
		ArrayList<Categoria> lista = pfl.getCategorias();
		
		lv.setAdapter(new ItemCategoriaAdapter(this, (ArrayList<Categoria>) lista.clone()));
	}
	
	private void retirarErros()
	{
		Utils.limpaErros();
		((EditText) findViewById(R.id.txtNomePerfil)).setError(null);
		((EditText) findViewById(R.id.txtAutorPerfil)).setError(null);
	}


	private void addNovasCategorias(ArrayList<Categoria> categorias)
	{
		ItemCategoriaAdapter ica = (ItemCategoriaAdapter) lv.getAdapter();
		for (int i = 0, length = categorias.size(); i < length; i++)
			ica.addItem(categorias.get(i));

		ica.refresh();
		
	}
	
	// callback ao voltar da tela adicionar categorias
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		// adicionar categoria com sucesso
		if (resultCode == 1)
		{
			ArrayList<Categoria> categorias = data.getParcelableArrayListExtra("selecionados"); 
			addNovasCategorias(categorias);
		}
			
			
		
		// adicionar categoria cancelado
		if (resultCode == 2)
			Toast.makeText(this, "Cancelado!", Toast.LENGTH_SHORT).show();
	}	

}
