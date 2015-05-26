package com.example.tabletvox03f.dal;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.tabletvox03f.ImageAdapter;
import com.example.tabletvox03f.Utils;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.categoria.Categoria;
import com.example.tabletvox03f.dal.categoria.CategoriaDAO;
import com.example.tabletvox03f.dal.categoria.ListaCategoria;
import com.example.tabletvox03f.dal.perfil.Perfil;
import com.example.tabletvox03f.dal.perfil.PerfilDAO;

// classe de carregamento das imagens em assincronia com a UI
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
	
	public static final int OPCAO_CARREGAR_IMAGENS 		= 0;
	public static final int OPCAO_CARREGAR_CATEGORIAS 	= 1;
	
	@Override
	protected ArrayList<AssocImagemSom> doInBackground(Integer... params)
	{
		ArrayList<AssocImagemSom> list = new ArrayList<AssocImagemSom>();

		Perfil perfil = Utils.PERFIL_ATIVO;
		
		//CategoriaAssocImagemSomDAO cat_ais_dao = new CategoriaAssocImagemSomDAO(activeContext);
		
		//list = cat_ais_dao.getAISByCatIdAndPage(params[1], params[0]);
		
		switch (params[1])
		{
			case OPCAO_CARREGAR_CATEGORIAS: // carregar categorias
				PerfilDAO pfl_dao = new PerfilDAO(activeContext);
				
				pfl_dao.open();
				perfil.setCategorias(pfl_dao.getCategorias(perfil.getId()));
				pfl_dao.close();
				
				ListaCategoria categorias = perfil.getCategorias();
				
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
			case OPCAO_CARREGAR_IMAGENS:
			default:
				CategoriaDAO cat_dao = new CategoriaDAO(activeContext);
				cat_dao.open();
				list = cat_dao.getImagens(params[2], params[0]);
				cat_dao.close();
				
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
