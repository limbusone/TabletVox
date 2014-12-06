package com.example.tabletvox03f;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tabletvox03f.management.Opcoes;

public class TelaBaseActivity extends Activity
{
	protected ArrayList<ImgItem> lista_imagens_frase;
	protected Intent sservice_intent; // servico de som
	protected int current_page;
	protected int init_page;
	protected int final_page;
	private Executor ex;
	private String currentTitle;
	
	protected int current_categoriaId;

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
		GridView gridview 			= (GridView) findViewById(R.id.gridview);
		GridView gridview_atalhos 	= (GridView) findViewById(R.id.gridview_atalhos);
		GridView gridview_frase 	= (GridView) findViewById(R.id.gridview_frase);
		
		SharedPreferences sp 	= PreferenceManager.getDefaultSharedPreferences(this);
		int tamanhoImagem 		= Integer.parseInt(sp.getString("tamanho_imagem", "" + Opcoes.TAMANHO_IMAGEM_DEFAULT));
		
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
	@SuppressWarnings("unchecked")
	public boolean copiarFraseGlobalParaFraseLocal()
	{
		// verifica antes se a frase global está inicializada e se possue elementos
		if (!(Utils.lista_imagens_frase_global == null) && !(Utils.lista_imagens_frase_global.isEmpty()))
		{
			lista_imagens_frase = (ArrayList<ImgItem>) Utils.lista_imagens_frase_global.clone();
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
		
		if (sp.getBoolean("tocar_som_ao_selecionar_imagem", true))
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
		
		if (sp.getBoolean("tocar_som_ao_selecionar_imagem", true))
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
		if (cod_cmd == 1) // tocar som frase
		{
			//Comandos.tocarSomFrase(ModoTouchActivity.this);
			//Comandos.tocarSomFrase();
			tocarSomFrase();
		}
		else if (cod_cmd == 2) // voltar para tela anterior
		{
			finish();
		}
	}
	
	public void acionarComando(ImgItem imgi)
	{
		int cod_cmd = imgi.getAssocImagemSom().getCmd();
		if (cod_cmd == 1) // tocar som frase
		{
			//Comandos.tocarSomFrase(ModoTouchActivity.this);
			//Comandos.tocarSomFrase();
			tocarSomFrase();
		}
		else if (cod_cmd == 2) // voltar para tela anterior
		{
			finish();
		}		
	}
	
	// adicionar imagem para a frase
	public void addImagemFrase(ImgItem imgi)
	{
		lista_imagens_frase.add(new ImgItem(imgi));
		
		Utils.lista_imagens_frase_global.add(new ImgItem(imgi));
		
		((GridView) findViewById(R.id.gridview_frase))
		.setAdapter(new ImageAdapterFrase(lista_imagens_frase));
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		if (sp.getBoolean("tocar_som_ao_selecionar_imagem", true))
		{	
			sservice_intent.putExtra("titulo_som", imgi.getAssocImagemSom().getTituloSom());
			imgi.tocarSom(sservice_intent);
			sservice_intent.removeExtra("titulo_som");
		}
	}
	
	// remover imagem da frase
	public void removerImagemDaFrase(int position)
	{
		lista_imagens_frase.remove(position);
		Utils.lista_imagens_frase_global.remove(position);
		((GridView) findViewById(R.id.gridview_frase))
				.setAdapter(new ImageAdapterFrase(lista_imagens_frase));
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
					
					try
					{
						SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(TelaBaseActivity.this);
						int intervalo = Integer.parseInt(sp.getString("intervalo_tempo_tocar_frase", "" + Opcoes.INTERVALO_TEMPO_TOCAR_FRASE_DEFAULT));
						
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
