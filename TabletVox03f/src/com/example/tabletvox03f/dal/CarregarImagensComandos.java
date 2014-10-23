package com.example.tabletvox03f.dal;

import java.util.ArrayList;

import com.example.tabletvox03f.ImageAdapter;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.xml.XmlUtilsComandos;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

public class CarregarImagensComandos extends AsyncTask<Boolean, Void, ArrayList<AssocImagemSom>>
{
	private XmlUtilsComandos xml;
	protected GridView gridview;
	//protected WebView wview;
	protected Context activeContext;
	protected ProgressBar pgrbar; 
	
	// atividade em background numa Thread separada
	@Override
	protected ArrayList<AssocImagemSom> doInBackground(Boolean... params)
	{
		ArrayList<AssocImagemSom> list;
		
//		CategoriaAssocImagemSomDAO cat_ais_dao = new CategoriaAssocImagemSomDAO(activeContext);
//		
//		// params[0] contem booleano que indica se eh para retornar os atalhos (true) ou todos os comandos (false)		
//		list = cat_ais_dao.getAISComandos(params[0]); 
		
		xml = new XmlUtilsComandos(activeContext);
		
		// params[0] contem booleano que indica se eh para retornar os atalhos (true) ou todos os comandos (false)		
		list = (params[0]) ? xml.getRegsAtalhos() : xml.getRegs();

		return list;
	}

	// metodo que roda na UI Thread depois da atividade em background
	// carrega o gridview com as imagens retornadas
	@Override
	protected void onPostExecute(ArrayList<AssocImagemSom> ais_list)
	{
		if (ais_list != null)
			gridview.setAdapter(new ImageAdapter(activeContext, ais_list));
//		if (wview != null)
//			wview.setVisibility(View.INVISIBLE);
		if (pgrbar != null)
			pgrbar.setVisibility(View.GONE);
		
	}

}

