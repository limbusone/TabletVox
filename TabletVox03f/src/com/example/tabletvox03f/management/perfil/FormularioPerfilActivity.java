package com.example.tabletvox03f.management.perfil;

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
import com.example.tabletvox03f.dal.categoria.Categoria;
import com.example.tabletvox03f.dal.categoria.CategoriaDAO;
import com.example.tabletvox03f.dal.perfil.Perfil;
import com.example.tabletvox03f.dal.perfil.PerfilDAO;
import com.example.tabletvox03f.management.FormularioBaseActivity;
import com.example.tabletvox03f.management.categoria.FormularioCategoriaActivity;
import com.example.tabletvox03f.management.categoria.ItemCategoriaAdapter;
import com.example.tabletvox03f.management.categoria.SelecionarCategoriasActivity;

public class FormularioPerfilActivity extends FormularioBaseActivity
{
	public static final int RC_PFL_INCLUIDA_SUCESSO = 1;
	public static final int RC_PFL_EDITADA_SUCESSO 	= 2; 	
	
	private Perfil pfl;

	private ListView lv;
	
	private Button btnAddCategoria;
	
	private ArrayList<Categoria> novasCategorias;
	
	private ArrayList<Categoria> antigasCategorias;
	
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
		
		novasCategorias = new ArrayList<Categoria>();
	}
	
	protected void initCriarForm()
	{
		pfl = new Perfil();
		
		inicializarLista();
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
		
		inicializarLista();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void incluir()
	{
		pfl.setNome(((EditText) findViewById(R.id.txtNomePerfil)).getText().toString());
		pfl.setAutor(((EditText) findViewById(R.id.txtAutorPerfil)).getText().toString());
		
		// seta somente se há items 
//		pfl.setCategorias((lv.getCount() > 0) ? 
//						  (ArrayList<Categoria>)((ItemCategoriaAdapter)lv.getAdapter()).getCategorias().clone() : 
//						   null);
		
		
		retirarErros();
		
		if (!(validarIncluir()))
		{
			Utils.exibirErros(this);
			return;
		}

		PerfilDAO dao_pfl = new PerfilDAO(this);
		CategoriaDAO dao_cat = new CategoriaDAO(this);
		
		//assumindo que todas as categorias são novas (e são! pois estamos incluindo um novo perfil)
		
		ArrayList<Categoria> categoriasDoAdapter = (ArrayList<Categoria>)((ItemCategoriaAdapter)lv.getAdapter()).getCategorias().clone();
		
		concatenarAutorComNomeDaCategoria(categoriasDoAdapter);
		
		dao_cat.open();
		
		// seta items
		pfl.setCategorias(dao_cat.create(categoriasDoAdapter));
		
		dao_cat.close();
		
		dao_pfl.open();
		
		dao_pfl.create(pfl);
		
		dao_pfl.close();
		
		this.setResult(RC_PFL_INCLUIDA_SUCESSO);
		finish();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void editar()
	{
		pfl.setNome(((EditText) findViewById(R.id.txtNomePerfil)).getText().toString());
		pfl.setAutor(((EditText) findViewById(R.id.txtAutorPerfil)).getText().toString());
		
		// seta somente se há items 
//		pfl.setCategorias((lv.getCount() > 0) ? 
//						  (ArrayList<Categoria>)((ItemCategoriaAdapter)lv.getAdapter()).getCategorias().clone() : 
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
		pfl.setCategorias((ArrayList<Categoria>) antigasCategorias.clone());
		
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

	// inicializa categorias caso não vier setada
	private void inicializarLista()
	{
		if (pfl.getCategorias() == null)
			pfl.setCategorias(new ArrayList<Categoria>());
		
		carregarLista();
	}
	
	protected void carregarLista()
	{
		ArrayList<Categoria> lista = pfl.getCategorias();
		
		antigasCategorias = (ArrayList<Categoria>) lista.clone();
		
		carregarCategoriasComImagens(antigasCategorias);
		
		lv.setAdapter(new ItemCategoriaAdapter(this, (ArrayList<Categoria>) antigasCategorias.clone()));
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
		{
			Categoria categoria = categorias.get(i);
			
			ica.addItem(categoria);
			carregarCategoriaComImagens(categoria);
			novasCategorias.add(categoria);
		}

		ica.refresh();
		
	}
	
	// callback ao voltar da tela adicionar categorias ou da tela do formulario de categoria
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(resultCode)
		{
			case SelecionarCategoriasActivity.RC_ADD_CAT_SUCESSO: // adicionar categoria com sucesso
				ArrayList<Categoria> categorias = data.getParcelableArrayListExtra("selecionados"); 
				addNovasCategorias(categorias);
				break;
			case SelecionarCategoriasActivity.RC_ADD_CAT_CANCELADO:	// adicionar categoria cancelado 
				Toast.makeText(this, "Cancelado!", Toast.LENGTH_SHORT).show();
				break;
			case FormularioCategoriaActivity.RC_CAT_EDITADA_NP_SUCESSO: // edição não persistente sucedida
				atualizarCategoriaEditadaNP((Categoria) data.getParcelableExtra("categoria"));
				break;
		}
	}
	
	// concatenar o nome da categoria com o autor do perfil 
	// para diferenciá-la das outras categorias
	private void concatenarAutorComNomeDaCategoria(Categoria categoria)
	{
		String autor = pfl.getAutor();
		
		categoria.setNome(categoria.getNome().concat(" - " + autor));		
	}
	
	private void concatenarAutorComNomeDaCategoria(ArrayList<Categoria> categorias)
	{
		for (int i = 0, length = categorias.size(); i < length; i++)
			concatenarAutorComNomeDaCategoria(categorias.get(i));
	}
	
	private void atualizarCategoriaEditadaNP(Categoria categoria)
	{
		ItemCategoriaAdapter ica = (ItemCategoriaAdapter) lv.getAdapter();
		ica.editItem(categoria, categoria.getId());
		ica.refresh();		
	}
	
	public void excluirCategoriaDasNovasCategorias(Categoria categoria)
	{
		novasCategorias.remove(categoria);
	}
	
	public void excluirCategoriaDasAntigasCategorias(Categoria categoria)
	{
		antigasCategorias.remove(categoria);
	}
	
	private void carregarCategoriasComImagens(ArrayList<Categoria> categorias)
	{
		Categoria categoria;
		CategoriaDAO cat_dao = new CategoriaDAO(this);
		cat_dao.open();
		for (int i = 0, length = categorias.size(); i < length; i++)
		{
			categoria = categorias.get(i);
			categoria.setImagens(cat_dao.getImagens(categoria.getId()));
		}
		cat_dao.close();
	}
	
	private void carregarCategoriaComImagens(Categoria categoria)
	{
		CategoriaDAO cat_dao = new CategoriaDAO(this);
		cat_dao.open();
		categoria.setImagens(cat_dao.getImagens(categoria.getId()));
		cat_dao.close();
	}
	
}
