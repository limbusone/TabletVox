package com.example.tabletvox03f.management.perfil;

import android.content.Intent;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.tabletvox03f.Erro;
import com.example.tabletvoxdemo.R;
import com.example.tabletvox03f.Utils;
import com.example.tabletvox03f.dal.categoria.Categoria;
import com.example.tabletvox03f.dal.categoria.CategoriaDAO;
import com.example.tabletvox03f.dal.categoria.ListaCategoria;
import com.example.tabletvox03f.dal.perfil.Perfil;
import com.example.tabletvox03f.dal.perfil.PerfilDAO;
import com.example.tabletvox03f.management.FormularioBaseActivity;

public class FormularioPerfilActivity extends FormularioBaseActivity
{
	public static final int RC_PFL_INCLUIDA_SUCESSO = 1;
	public static final int RC_PFL_EDITADA_SUCESSO 	= 2; 	
	
	private Perfil pfl;
	
	private ListaCategoria novasCategorias;
	
	private ListaCategoria antigasCategorias;

	private OnClickListener definirCategoriasEvento = new OnClickListener()
	{
		
		@Override
		public void onClick(View arg0)
		{
			FormularioPerfilActivity thisContext = FormularioPerfilActivity.this;
			Intent intent = new Intent(thisContext, ListaCategoriasPerfilActivity.class);
			intent.putExtra("novasCategorias", (Parcelable) novasCategorias);
			intent.putExtra("antigasCategorias", (Parcelable) antigasCategorias);
			thisContext.startActivityForResult(intent, 1);
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
		// carregar evento do botão definir categorias
		Button btnDefinirCategorias = (Button) findViewById(R.id.btnDefinirCategorias);
		btnDefinirCategorias.setOnClickListener(definirCategoriasEvento);
		
	}
	
	protected void initCriarForm()
	{
		pfl = new Perfil();
		
		pfl.setCategorias(new ListaCategoria());
	}
	
	protected void initEditarForm(Intent intent)
	{
		EditText txtNome = (EditText) findViewById(R.id.txtNomePerfil);
		EditText txtAutor = (EditText) findViewById(R.id.txtAutorPerfil);
		
		pfl = intent.getParcelableExtra("perfil");
		
		txtNome.setText(pfl.getNome());
		txtAutor.setText(pfl.getAutor());
		
		novasCategorias   = new ListaCategoria();
		antigasCategorias = (ListaCategoria) pfl.getCategorias().clone();
	}

	@Override
	protected void incluir()
	{
		pfl.setNome(((EditText) findViewById(R.id.txtNomePerfil)).getText().toString());
		pfl.setAutor(((EditText) findViewById(R.id.txtAutorPerfil)).getText().toString());
		
		// seta somente se há items 
//		pfl.setCategorias((lv.getCount() > 0) ? 
//						  (ListaCategoria)((ItemCategoriaAdapter)lv.getAdapter()).getCategorias().clone() : 
//						   null);
		
		
		retirarErros();
		
		if (!(validarIncluir()))
		{
			Utils.exibirErros(this);
			return;
		}

		PerfilDAO dao_pfl 		= new PerfilDAO(this);
		CategoriaDAO dao_cat 	= new CategoriaDAO(this);
		
		// assumindo que todas as categorias são novas (e são! pois estamos incluindo um novo perfil)
		
		//ListaCategoria categoriasDoAdapter = (ListaCategoria)((ItemCategoriaAdapter)lv.getAdapter()).getCategorias().clone();
		
		concatenarAutorComNomeDaCategoria(pfl.getCategorias());
		
		dao_cat.open();
		
		// seta items
		pfl.setCategorias(dao_cat.create(pfl.getCategorias()));
		
		dao_cat.close();
		
		dao_pfl.open();
		
		dao_pfl.create(pfl);
		
		dao_pfl.close();
		
		this.setResult(RC_PFL_INCLUIDA_SUCESSO);
		finish();
		
	}

	@Override
	protected void editar()
	{
		pfl.setNome(((EditText) findViewById(R.id.txtNomePerfil)).getText().toString());
		pfl.setAutor(((EditText) findViewById(R.id.txtAutorPerfil)).getText().toString());
		
		// seta somente se há items 
//		pfl.setCategorias((lv.getCount() > 0) ? 
//						  (ListaCategoria)((ItemCategoriaAdapter)lv.getAdapter()).getCategorias().clone() : 
//						   null);
		
		retirarErros();
		
		if (!(validarEditar()))
		{
			Utils.exibirErros(this);
			return;
		}
		
		PerfilDAO dao_pfl 		= new PerfilDAO(this);
		CategoriaDAO dao_cat 	= new CategoriaDAO(this);
		
		concatenarAutorComNomeDaCategoria(novasCategorias);

		dao_cat.open();
		
		// edita as categorias numa eventual mudança destas
		dao_cat.update(antigasCategorias);
		
		// adicionar eventuais novos itens
		novasCategorias = dao_cat.create(novasCategorias);
		
		dao_cat.close();
		
		// adicionar itens das novas categorias na lista das antigas
		antigasCategorias.addAll(novasCategorias);
		
		// seta items
		pfl.setCategorias((ListaCategoria) antigasCategorias.clone());
		
		dao_pfl.open();
		dao_pfl.update(pfl, pfl.getId());
		dao_pfl.close();
		
		this.setResult(RC_PFL_EDITADA_SUCESSO);
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

	private void retirarErros()
	{
		Utils.limpaErros();
		((EditText) findViewById(R.id.txtNomePerfil)).setError(null);
		((EditText) findViewById(R.id.txtAutorPerfil)).setError(null);
	}

	// concatenar o nome da categoria com o autor do perfil 
	// para diferenciá-la das outras categorias
	private void concatenarAutorComNomeDaCategoria(Categoria categoria)
	{
		String autor = pfl.getAutor();
		
		categoria.setNome(categoria.getNome().concat(" - " + autor));		
	}
	
	private void concatenarAutorComNomeDaCategoria(ListaCategoria categorias)
	{
		for (int i = 0, length = categorias.size(); i < length; i++)
			concatenarAutorComNomeDaCategoria(categorias.get(i));
	}
	
	// callback ao voltar da tela de definicao de categorias
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(resultCode)
		{
			case ListaCategoriasPerfilActivity.RC_DEFINIR_CATS_SUCESSO:
				ListaCategoria categorias 			= data.getParcelableExtra("categorias");
				ListaCategoria novasCategorias 		= data.getParcelableExtra("novasCategorias");
				ListaCategoria antigasCategorias 	= data.getParcelableExtra("antigasCategorias");
				
				pfl.setCategorias(categorias);
				this.novasCategorias 	= novasCategorias;
				this.antigasCategorias 	= antigasCategorias;
				break;
			case ListaCategoriasPerfilActivity.RC_DEFINIR_CATS_CANCELADO:
			default:
			break;
		}
	}
	
	@Override
	protected boolean acaoDosEventosDoMenu(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			finish();
			break;
		}
		return true;
	}
	
}
