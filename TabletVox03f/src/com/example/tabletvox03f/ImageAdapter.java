package com.example.tabletvox03f;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.tabletvox03f.dal.AssocImagemSom;

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
			imagemItem = new ImgItem(mContext);
			imagemItem.setLayoutParams(new GridView.LayoutParams(85, 85));
			imagemItem.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imagemItem.setPadding(2, 2, 2, 2);
			//imagemItem.setBackgroundResource(R.drawable.borda);
		} 
		else 
		{
			imagemItem = (ImgItem) convertView;
		}

		String titulo = (ais = ais_list.get(position)).getTituloImagem();
		String ext 	  = "." + ais.getExt();
		try
		{
			// aqui recupera-se a imagem em si
			// a imagem está sendo recuperada da pasta assets/imagens
			// ou da pasta imagens em internal storage			
			File f_from_internal_storage = new File(mContext.getDir("imagens", Context.MODE_PRIVATE).getPath() + "/" +  titulo + ext);

			InputStream ims = (f_from_internal_storage.exists()) 
							   ? new FileInputStream(f_from_internal_storage) 
							   : mContext.getAssets().open("imagens/" + titulo + ext);
			Drawable d = Drawable.createFromStream(ims, null);
			imagemItem.setImageDrawable(d);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Uri path = Uri.parse("file:///android_asset/imagens/" + titulo + ".png");
		
		//imagemItem.setImageResource(id);
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