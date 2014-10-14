package com.example.tabletvox03f.dal;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.tabletvox03f.ImageAdapter;
import com.example.tabletvox03f.Utils;

// classe de carregamento das imagens do xml telas.xml em assincronia com a UI
public class CarregarImagensTelas extends AsyncTask<Integer, Void, ArrayList<AssocImagemSom>>
{
	//private XmlUtilsTelas xml;
	protected Context activeContext;
	protected GridView gridview;
	//protected WebView wview;
	protected ProgressBar pgrbar;

	// atividade em background numa Thread separada
	// params[0] : pagina, params[1] : opcao carregar categorias ou imagens comuns,
	// params[2] : id da categoria
	@Override
	protected ArrayList<AssocImagemSom> doInBackground(Integer... params)
	{
		ArrayList<AssocImagemSom> list = new ArrayList<AssocImagemSom>();

		Perfil perfil = Utils.PERFIL_ATIVO;
		
		//CategoriaAssocImagemSomDAO cat_ais_dao = new CategoriaAssocImagemSomDAO(activeContext);
		
		//list = cat_ais_dao.getAISByCatIdAndPage(params[1], params[0]);
		
		switch (params[1])
		{
			case 1: // carregar categorias
				ArrayList<Categoria> categorias = perfil.getCategorias();
				
				for (int i = 0, length = categorias.size(); i < length; i++)
				{
					Categoria categoria = categorias.get(i);
					AssocImagemSom ais = categoria.getAIS();
					
					ais.setCategoriaId(categoria.getId());
					list.add(ais);
				}
				break;
			case 2:
				break;
				// carregar imagens comuns
			case 0:
			default:
				list = 	(ArrayList<AssocImagemSom>)
						CategoriaDAOSingleton.getInstance().
						getCategoriaById(params[2]).getImagens().clone();
				
				break;
		}
		

		
		
		
//		xml = new XmlUtilsTelas(activeContext, Utils.TELAS_NOME_ARQUIVO_XML_ATIVO, "root");
//		
//		list = xml.getRegsByNumPage(params[0]);
		
		return list;

	}
	
	// metodo que roda na UI Thread depois da atividade em background
	// carrega o gridview com as imagens retornadas
	@Override
	protected void onPostExecute(ArrayList<AssocImagemSom> ais_list)
	{
		if (ais_list != null)
			gridview.setAdapter(new ImageAdapter(activeContext, ais_list));
		//wview.setVisibility(View.INVISIBLE);
		pgrbar.setVisibility(View.GONE);
	}	
	
}
