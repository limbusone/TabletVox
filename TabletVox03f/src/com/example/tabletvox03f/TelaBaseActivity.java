package com.example.tabletvox03f;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tabletvox03f.management.Opcoes;

public class TelaBaseActivity extends ActionBarActivity
{
	public static ArrayList<ImgItem> lista_imagens_frase_global;
	protected ArrayList<ImgItem> lista_imagens_frase;
	protected Intent sservice_intent; // servico de som
	protected int current_page;
	protected int init_page;
	protected int final_page;
	private Executor ex;
	private String currentTitle;
	
	protected int current_categoriaId;
	
	protected GridView gridview;
	protected GridView gridview_frase;
	protected GridView gridview_atalhos;
	
	private static final int TOCAR_SOM_FRASE_COD_CMD 	= 1;
	private static final int VOLTAR_TELA_COD_CMD 		= 2;

	public TelaBaseActivity()
	{
		// utilizado para executar a thread tocarSomFrase uma de cada vez
		ex = Executors.newSingleThreadExecutor();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.telas_interface);
		
		// altera layout params dos gridviews de acordo com as opções do usuário
		gridview 			= (GridView) findViewById(R.id.gridview);
		gridview_frase 		= (GridView) findViewById(R.id.gridview_frase);
		gridview_atalhos	= (GridView) findViewById(R.id.gridview_atalhos);
		
		SharedPreferences sp 	= PreferenceManager.getDefaultSharedPreferences(this);
		int tamanhoImagem 		= Integer.parseInt(sp.getString(Opcoes.TAMANHO_IMAGEM_KEY, "" + Opcoes.TAMANHO_IMAGEM_DEFAULT));
		
		gridview.setColumnWidth(tamanhoImagem);
		gridview_atalhos.setColumnWidth(tamanhoImagem);
		gridview_frase.setColumnWidth(tamanhoImagem);
		
		gridview_atalhos.setLayoutParams(new LinearLayout.LayoutParams(tamanhoImagem, LinearLayout.LayoutParams.WRAP_CONTENT));		
	}
	
	public int getCurrent_page()
	{
		return current_page;
	}

	public int setCurrent_page(int current_page)
	{
		this.current_page = current_page;
		return current_page;
	}

	public int getInit_page()
	{
		return init_page;
	}

	public int setInit_page(int init_page)
	{
		this.init_page = init_page;
		return init_page;
	}

	public int getFinal_page()
	{
		return final_page;
	}

	public void setFinal_page(int final_page)
	{
		this.final_page = final_page;
	}
	
	public String getCurrentTitle()
	{
		return currentTitle;
	}

	public void setCurrentTitle(String currentTitle)
	{
		this.currentTitle = currentTitle;
		setTitle(currentTitle);
	}

	public ArrayList<ImgItem> getListaImagensFrase()
	{
		return lista_imagens_frase;
	}

	public Intent getSservice_intent()
	{
		return sservice_intent;
	}

	public void setSservice_intent(Intent sservice_intent)
	{
		this.sservice_intent = sservice_intent;
	}
	
	public int getCurrent_categoriaId()
	{
		return current_categoriaId;
	}

	public void setCurrent_categoriaId(int current_categoriaId)
	{
		this.current_categoriaId = current_categoriaId;
	}

	// copia frase global para frase local
	public boolean copiarFraseGlobalParaFraseLocal()
	{
		// verifica antes se a frase global está inicializada e se possue elementos
		if (!(TelaBaseActivity.lista_imagens_frase_global == null) && !(TelaBaseActivity.lista_imagens_frase_global.isEmpty()))
		{
			//lista_imagens_frase = (ArrayList<ImgItem>) TelaBaseActivity.lista_imagens_frase_global.clone();
			lista_imagens_frase = new ArrayList<ImgItem>();
			
			for (int i = 0, length = TelaBaseActivity.lista_imagens_frase_global.size(); i < length; i++)
				lista_imagens_frase.add(new ImgItem(TelaBaseActivity.lista_imagens_frase_global.get(i)));
			return true;
		}
		
		return false;
	}
	
	// chama o activity modo touch com base na categoria selecionada
	public void carregarCategoriaModoTouch(String nome_categoria)
	{
		Utils.TELAS_NOME_ARQUIVO_XML_ATIVO = Utils.PERFIL_ATIVO.getNome() + "_categoria_" + nome_categoria;
		
		Intent intent = new Intent(this, ModoTouchActivity.class);
		startActivity(intent); 

	}
	
	public void carregarCategoriaModoTouch(ImgItem imgi)
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		if (sp.getBoolean(Opcoes.TOCAR_SOM_AO_SELECIONAR_IMAGEM_KEY, Opcoes.TOCAR_SOM_AO_SELECIONAR_IMAGEM_DEFAULT))
			imgi.tocarSom(this);

		//carregarCategoriaModoTouch(imgi.getAssocImagemSom().getDesc());
		carregarCategoriaModoTouch(imgi.getAssocImagemSom().getCategoriaId());
	}
	
	public void carregarCategoriaModoTouch(long id)
	{
		Intent intent = new Intent(this, ModoTouchActivity.class);
		intent.putExtra("categoriaId", id);
		startActivity(intent); 
	}
	

	// chama o activity modo varredura com base na categoria selecionada
	public void carregarCategoriaModoVarredura(String nome_categoria)
	{
		Utils.TELAS_NOME_ARQUIVO_XML_ATIVO = Utils.PERFIL_ATIVO.getNome() + "_categoria_" + nome_categoria;
		
		Intent intent = new Intent(this, ModoVarreduraActivity.class);
		startActivity(intent); 

	}
	
	public void carregarCategoriaModoVarredura(ImgItem imgi)
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		if (sp.getBoolean(Opcoes.TOCAR_SOM_AO_SELECIONAR_IMAGEM_KEY, Opcoes.TOCAR_SOM_AO_SELECIONAR_IMAGEM_DEFAULT))
			imgi.tocarSom(this);
		
		//carregarCategoriaModoVarredura(imgi.getAssocImagemSom().getDesc());
		carregarCategoriaModoVarredura(imgi.getAssocImagemSom().getCategoriaId());
	}
	
	public void carregarCategoriaModoVarredura(long id)
	{
		Intent intent = new Intent(this, ModoVarreduraActivity.class);
		intent.putExtra("categoriaId", id);
		startActivity(intent); 
	}	
	
	public boolean hasItens(GridView gv)
	{
		return (gv.getChildCount() > 0);
	}
	
	/******* COMANDOS *******/
	
	protected void acionarComando(int cod_cmd)
	{
		switch (cod_cmd)
		{
			// tocar som frase
			case TOCAR_SOM_FRASE_COD_CMD:
				tocarSomFrase();
				break;
			// voltar para tela anterior
			case VOLTAR_TELA_COD_CMD:
				finish();
				break;
		}
	}
	
	public void acionarComando(ImgItem imgi)
	{
		int cod_cmd = imgi.getAssocImagemSom().getCmd();
		switch (cod_cmd)
		{
			// tocar som frase
			case TOCAR_SOM_FRASE_COD_CMD:
				tocarSomFrase();
				break;
			// voltar para tela anterior
			case VOLTAR_TELA_COD_CMD:
				finish();
				break;
		}	
	}
	
	// adicionar imagem para a frase
	public void addImagemFrase(ImgItem imgi)
	{
		lista_imagens_frase.add(new ImgItem(imgi));
		
		TelaBaseActivity.lista_imagens_frase_global.add(new ImgItem(imgi));
		
		gridview_frase.setAdapter(new ImageAdapterFrase(lista_imagens_frase));

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		if (sp.getBoolean(Opcoes.TOCAR_SOM_AO_SELECIONAR_IMAGEM_KEY, Opcoes.TOCAR_SOM_AO_SELECIONAR_IMAGEM_DEFAULT))
		{	
			sservice_intent.putExtra("titulo_som", imgi.getAssocImagemSom().getTituloSom());
			imgi.tocarSom(sservice_intent);
			sservice_intent.removeExtra("titulo_som");
		}
		
		if (sp.getBoolean(Opcoes.VOLTAR_AUTOMATICAMENTE_PARA_TELA_CATEGORIAS_KEY, Opcoes.VOLTAR_AUTOMATICAMENTE_PARA_TELA_CATEGORIAS_DEFAULT))
			finish();
	}
	
	// remover imagem da frase
	public void removerImagemDaFrase(int position)
	{
		lista_imagens_frase.remove(position);
		TelaBaseActivity.lista_imagens_frase_global.remove(position);
		gridview_frase.setAdapter(new ImageAdapterFrase(lista_imagens_frase));
	}
	
	// paginacao circular
	public void vaiParaProximaPagina(AsyncTask<Integer, ?, ?> ast)
	{
		int cp = current_page; 
		
		if (++cp <= final_page)
			current_page = cp;
		else
			current_page = cp = 1; // aqui volta para a primeira pagina
		ast.execute(cp);
		Toast.makeText(this, "Página: " + Integer.toString(cp), Toast.LENGTH_SHORT).show();
	}
	
	// paginacao circular
	public void vaiParaProximaPagina(AsyncTask<Integer, ?, ?> ast, int opcao)
	{
		int cp = current_page; 
		
		if (++cp <= final_page)
			current_page = cp;
		else
			current_page = cp = 1; // aqui volta para a primeira pagina
		ast.execute(cp, opcao);
		Toast.makeText(this, "Página: " + Integer.toString(cp), Toast.LENGTH_SHORT).show();
	}
	
	// paginacao circular
	public void vaiParaProximaPagina(AsyncTask<Integer, ?, ?> ast, int opcao, int categoriaId)
	{
		int cp = current_page; 
		
		if (++cp <= final_page)
			current_page = cp;
		else
			current_page = cp = 1; // aqui volta para a primeira pagina
		ast.execute(cp, opcao, categoriaId);
		Toast.makeText(this, "Página: " + Integer.toString(cp), Toast.LENGTH_SHORT).show();
	}	
	
	public void tocarSomFrase()
	{
		// passar vetor de imagens da frase por cópia
		@SuppressWarnings("unchecked")
		final ArrayList<ImgItem> lif = (ArrayList<ImgItem>) lista_imagens_frase.clone();

		
		//funcionalidade em outra thread
		Runnable r = new Runnable()
		{
			public void run()
			{
				int length = lif.size();
				// varrendo a frase e tocando o som de cada imagem
				for (int i = 0; i < length; i++)
				{
					ImgItem imgi = lif.get(i);
					imgi.tocarSom(TelaBaseActivity.this);
					
					while (imgi.tocandoSom());
					try
					{
						SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(TelaBaseActivity.this);
						int intervalo = Integer.parseInt(sp.getString(Opcoes.INTERVALO_TEMPO_TOCAR_FRASE_KEY, "" + Opcoes.INTERVALO_TEMPO_TOCAR_FRASE_DEFAULT));
						
						Thread.sleep(intervalo);
					} 
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					imgi.encerrarMediaPlayer();
			
				}				
			}
		};
		
		ex.execute(r);
	}	
}
