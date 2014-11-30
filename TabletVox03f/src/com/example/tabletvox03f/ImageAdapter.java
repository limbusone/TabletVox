package com.example.tabletvox03f;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.tabletvox03f.dal.FilesIO;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;

public class ImageAdapter extends BaseAdapter 
{
	private Context mContext;

	// references to our images
	// private int[] mThumbIds;

	private ArrayList<AssocImagemSom> ais_list;

	public ImageAdapter(Context c) 
	{
		mContext = c;
		// carregarImagensNoArray();
	}

	public ImageAdapter(Context c, ArrayList<AssocImagemSom> a) 
	{
		mContext = c;
		ais_list = a;
	}
	

	public int getCount() 
	{
		return ais_list.size();
	}

	public Object getItem(int position) 
	{
		return ais_list.get(position);
	}

	public long getItemId(int position) 
	{
		return 0;
	}

	// cria um novo ImageItem para cada item referenciado pelo Adapter
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ImgItem imagemItem;
		AssocImagemSom ais;
		if (convertView == null) 
		{ // if it's not recycled, initialize some
									// attributes
			SharedPreferences sp 	= PreferenceManager.getDefaultSharedPreferences(mContext);
			int tamanhoImagem 		= Integer.parseInt(sp.getString("tamanho_imagem", "" + Utils.TAMANHO_IMAGEM_DEFAULT));
			
			imagemItem = new ImgItem(mContext);
			imagemItem.setLayoutParams(new GridView.LayoutParams(tamanhoImagem, tamanhoImagem));
			imagemItem.setScaleType(ImageView.ScaleType.CENTER_CROP);

			imagemItem.setPadding(2, 2, 2, 2);
			//imagemItem.setBackgroundResource(R.drawable.borda);
		} 
		else 
		{
			imagemItem = (ImgItem) convertView;
		}

		ais = ais_list.get(position);
		imagemItem.setImageDrawable((new FilesIO(mContext)).getImgItemDrawableFromInternalStorageOrAssets(ais));
		imagemItem.setAssocImagemSom(ais);

		return imagemItem;
	}

	/*
	 * private void carregarImagensNoArray() { // aqui pega-se todos os ids das
	 * imagens em res/drawable int[] ids =
	 * Utils.getAllResourceIDs(R.drawable.class);
	 * 
	 * // elimina as 2 primeiras imagens de drawable // e armazena o resto em
	 * mThumbIds que será usada para exibir as imagens mThumbIds = new
	 * int[ids.length - 2]; System.arraycopy(ids, 2, mThumbIds, 0, ids.length -
	 * 2);
	 * 
	 * }
	 */
}