package com.example.tabletvox03f.management.categoria;

import java.util.ArrayList;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabletvox03f.Erro;
import com.example.tabletvox03f.ImgItem;
import com.example.tabletvox03f.R;
import com.example.tabletvox03f.Utils;
import com.example.tabletvox03f.dal.FilesIO;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.categoria.Categoria;
import com.example.tabletvox03f.dal.categoria.CategoriaDAO;
import com.example.tabletvox03f.management.FormularioNaoPersistenteBaseActivity;
import com.example.tabletvox03f.management.assocImagemSom.SelecionarImagemActivity;

public class FormularioCategoriaActivity extends FormularioNaoPersistenteBaseActivity
{
	
	public static final int RC_CAT_INCLUIDA_SUCESSO 	= 1;
	public static final int RC_CAT_EDITADA_SUCESSO 		= 2;
	public static final int RC_CAT_EDITADA_NP_SUCESSO 	= 5;
	
	// boolean que indica se o activity que chamou o activity atual é o FormularioPerfilActivity	
	private boolean isFormularioPerfilActivity = false;	
	
	private Categoria cat;
	
	private RelativeLayout laySom;
	
	private ImageView imgImagem;
	
	private TextView lblSomSel;
	
	private EditText txtNome;
	
	private AssocImagemSom ais_selecionado;
	
	private OnClickListener escolherImagemEvento = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			FormularioCategoriaActivity thisContext = FormularioCategoriaActivity.this;
			Intent intent = new Intent(thisContext, SelecionarImagemActivity.class);
			thisContext.startActivityForResult(intent, 1);
		
		}
	};
	
	private OnClickListener tocarSomEvento = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			FormularioCategoriaActivity thisContext = FormularioCategoriaActivity.this;
			ImgItem imgItem = new ImgItem(thisContext);
			imgItem.setAssocImagemSom(ais_selecionado);
			imgItem.tocarSom(thisContext);
		}
	};
	
	private OnClickListener definirImagensEvento = new OnClickListener()
	{
		
		@Override
		public void onClick(View arg0)
		{
			FormularioCategoriaActivity thisContext = FormularioCategoriaActivity.this;
			Intent intent = new Intent(thisContext, ListaImagensCategoriaActivity.class);
			intent.putExtra("categoria", cat);
			thisContext.startActivityForResult(intent, 1);
		}
	};
	
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
		imgImagem = (ImageView) findViewById(R.id.imgImagem);
		lblSomSel = (TextView) findViewById(R.id.lblSomSel);
		laySom	  = (RelativeLayout) findViewById(R.id.laySom);
		txtNome	  = (EditText) findViewById(R.id.txtNomeCategoria);
		
		Button btnEscolherImagem = (Button) findViewById(R.id.btnEscolherImagem);
		btnEscolherImagem.setOnClickListener(escolherImagemEvento);
		
		ImageButton btnTocar = (ImageButton) findViewById(R.id.btnTocar);
		btnTocar.setOnClickListener(tocarSomEvento);
		
		Button btnDefinirImagens = (Button) findViewById(R.id.btnDefinirImagens);
		btnDefinirImagens.setOnClickListener(definirImagensEvento);
		
		// habilita up back
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
	}
	
	@Override
	protected void initCriarForm()
	{
		cat = new Categoria();
	}

	@Override
	protected void initEditarForm(Intent intent)
	{
		cat = intent.getParcelableExtra("categoria");
		
		ais_selecionado = cat.getAIS();
		
		atribuirDadosAISAosControles(ais_selecionado);
		txtNome.setText(cat.getNome());
		
		
	}

	@Override
	protected void incluir()
	{
		cat.setNome(txtNome.getText().toString());
		cat.setAIS(ais_selecionado);
		
		retirarErros();
		
		if (!(validarIncluir()))
		{
			Utils.exibirErros(this);
			return;
		}
		
		CategoriaDAO dao_cat = new CategoriaDAO(this);
		
		dao_cat.open();
		dao_cat.create(cat);
		dao_cat.close();
		
		this.setResult(RC_CAT_INCLUIDA_SUCESSO);
		finish();
	}

	@Override
	protected void editar()
	{
		cat.setNome(txtNome.getText().toString());
		cat.setAIS(ais_selecionado);

		retirarErros();
		
		if (!(validarEditar()))
		{
			Utils.exibirErros(this);
			return;
		}
		
		
		CategoriaDAO dao_cat = new CategoriaDAO(this);
		
		dao_cat.open();
		dao_cat.update(cat, cat.getId());
		dao_cat.close();
		
		this.setResult(RC_CAT_EDITADA_SUCESSO);
		finish();
	}
	
	private boolean validarIncluir()
	{
		boolean retorno = true;
		
		if (cat.getNome() == null || cat.getNome().length() == 0)
		{
			Utils.erros.add(new Erro("Campo nome obrigatório!", txtNome, "EditText"));
			retorno = false;
		}
		
		if (cat.getAIS() == null)
		{
			Utils.erros.add(new Erro("Escolha uma imagem!"));
			retorno = false;			
		}
		
		return retorno;
	}	
	
	private boolean validarEditar()
	{
		boolean retorno = true;

		if (cat.getNome() == null || cat.getNome().length() == 0)
		{
			Utils.erros.add(new Erro("Campo nome obrigatório!", txtNome, "EditText"));
			retorno = false;
		}
		
		if (cat.getAIS() == null)
		{
			Utils.erros.add(new Erro("Escolha uma imagem!"));
			retorno = false;			
		}
		
		return retorno;
	}

	// callback ao voltar da tela selecionar uma imagem
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		// selecionar uma imagem com sucesso
		if (resultCode == SelecionarImagemActivity.RC_SELECIONAR_IMG_SUCESSO)
		{
			ais_selecionado = data.getParcelableExtra("ais");
			cat.setAIS(ais_selecionado);
			
			atribuirDadosAISAosControles(ais_selecionado);
		}

		// selecionar uma imagem cancelado
		if (resultCode == SelecionarImagemActivity.RC_SELECIONAR_IMG_CANCELADO)
			Toast.makeText(this, "Cancelado!", Toast.LENGTH_SHORT).show();
		
		// definir imagens com sucesso
		if (resultCode == ListaImagensCategoriaActivity.RC_DEFINIR_IMGS_SUCESSO)
		{
			ArrayList<AssocImagemSom> imagens = data.getParcelableArrayListExtra("imagens");
			cat.setImagens(imagens);
		}
		
		// definir imagens cancelado
		if (resultCode == ListaImagensCategoriaActivity.RC_DEFINIR_IMGS_CANCELADO);
	}
	
	private void atribuirDadosAISAosControles(AssocImagemSom ais)
	{
		if (!(ais == null))
		{
			FilesIO fio = new FilesIO(this);

			imgImagem.setImageDrawable(fio.getImgItemDrawableFromInternalStorageOrAssets(ais));

			lblSomSel.setText(ais.getTituloSom() + ".wav");
			
			imgImagem.setVisibility(View.VISIBLE);

			laySom.setVisibility(View.VISIBLE);
		}
	}
	
	private void retirarErros()
	{
		Utils.limpaErros();
		txtNome.setError(null);
	}

	@Override
	protected void initEditarNPForm(Intent intent)
	{
		cat = intent.getParcelableExtra("categoria");
		
		ais_selecionado = cat.getAIS();
		
		atribuirDadosAISAosControles(ais_selecionado);
		txtNome.setText(cat.getNome());
		
	}

	@Override
	protected void editarNP()
	{
	
		cat.setNome(txtNome.getText().toString());
		cat.setAIS(ais_selecionado);

		retirarErros();
		
		if (!(validarEditar()))
		{
			Utils.exibirErros(this);
			return;
		}
		
		
		Intent data = new Intent();
		data.putExtra("categoria", cat);
		this.setResult(RC_CAT_EDITADA_NP_SUCESSO, data);
		finish();
		
	}

	@Override
	protected void acaoDosEventosDoMenu(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				finish();
				break;
		}
		
	}	
}
