package com.example.tabletvox03f.dal;

import java.util.ArrayList;

import com.example.tabletvox03f.ImageAdapter;
import com.example.tabletvox03f.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

// classe de carregamento das imagens do xml telas.xml em assincronia com a UI
public class CarregarImagensTelas extends AsyncTask<Integer, Void, ArrayList<AssocImagemSom>>
{
	private XmlUtilsTelas xml;
	protected Context activeContext;
	protected GridView gridview;
	//protected WebView wview;
	protected ProgressBar pgrbar;

	// atividade em background numa Thread separada
	// params[0] : pagina, params[1] : id da categoria 
	@Override
	protected ArrayList<AssocImagemSom> doInBackground(Integer... params)
	{
		ArrayList<AssocImagemSom> list;
		
		//CategoriaAssocImagemSomDAO cat_ais_dao = new CategoriaAssocImagemSomDAO(activeContext);
		
		//list = cat_ais_dao.getAISByCatIdAndPage(params[1], params[0]);
		
		xml = new XmlUtilsTelas(activeContext, Utils.TELAS_NOME_ARQUIVO_XML_ATIVO, "root");
		
		list = xml.getRegsByNumPage(params[0]);
		
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
