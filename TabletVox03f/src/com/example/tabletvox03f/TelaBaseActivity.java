package com.example.tabletvox03f;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.example.tabletvox03f.management.Opcoes;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.GridView;
import android.widget.Toast;

public class TelaBaseActivity extends Activity
{
	protected ArrayList<ImgItem> lista_imagens_frase;
	protected Intent sservice_intent; // servico de som
	protected int current_page;
	protected int init_page;
	protected int final_page;
	private Executor ex;

	public TelaBaseActivity()
	{
		// utilizado para executar a thread tocarSomFrase uma de cada vez
		ex = Executors.newSingleThreadExecutor();
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
	
	// copia frase global para frase local
	@SuppressWarnings("unchecked")
	public boolean copiarFraseGlobalParaFraseLocal()
	{
		// verifica antes se a frase global est� inicializada e se possue elementos
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
		if (Opcoes.isTocar_som_ao_selecionar_imagem())
			imgi.tocarSom(this);

		carregarCategoriaModoTouch(imgi.getAssocImagemSom().getDesc());
	}
	
	public void carregarCategoriaModoTouch(long id)
	{
		Intent intent = new Intent(this, ModoTouchActivity.class);
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
		if (Opcoes.isTocar_som_ao_selecionar_imagem())
			imgi.tocarSom(this);
		
		carregarCategoriaModoVarredura(imgi.getAssocImagemSom().getDesc());
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
		sservice_intent.putExtra("titulo_som", imgi.getAssocImagemSom().getTituloSom());
		imgi.tocarSom(sservice_intent);
		sservice_intent.removeExtra("titulo_som");		
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
		Toast.makeText(this, "P�gina: " + Integer.toString(cp), Toast.LENGTH_SHORT).show();
	}
	
	public void tocarSomFrase()
	{
		// passar vetor de imagens da frase por c�pia
		@SuppressWarnings("unchecked")
		final ArrayList<ImgItem> lif = (ArrayList<ImgItem>) lista_imagens_frase.clone();

		
		//funcionalidade em outra thread
		Runnable r = new Runnable()
		{
			public void run()
			{
				int length = lif.size();
				for (int i = 0; i < length; i++)
				{
					ImgItem imgi = lif.get(i);
					imgi.tocarSom(TelaBaseActivity.this);
					
					try
					{
						Thread.sleep(Opcoes.getIntervalo_tempo_tocar_frase());
					} 
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					imgi.encerrarMediaPlayer();
			
				}				
			}
		};
		
		ex.execute(r);
	}	
}
