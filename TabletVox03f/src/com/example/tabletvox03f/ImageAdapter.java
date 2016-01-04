package com.example.tabletvox03f;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.tabletvox03f.dal.FilesIO;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.management.Opcoes;
import com.example.tabletvoxdemo.R;

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
			int tamanhoImagem 		= Integer.parseInt(sp.getString(Opcoes.TAMANHO_IMAGEM_KEY, "" + Opcoes.TAMANHO_IMAGEM_DEFAULT));
			
			XmlResourceParser parser = mContext.getResources().getXml(R.xml.imgitem_attrs);
	        AttributeSet attributes = null;
	        int state = XmlPullParser.END_DOCUMENT;
	        do 
	        {
	            try 
	            {
	                state = parser.next();
	                if (state == XmlPullParser.START_TAG) 
	                {
	                    if (parser.getName().equals("style")) 
	                    {
	                        attributes = Xml.asAttributeSet(parser);
	                        break;
	                    }
	                }
	            } 
	            catch (Exception ignore) 
	            {
	            	//ignore it - can't do much anyway
	            } 
	        } 
	        while(state != XmlPullParser.END_DOCUMENT);
	            

	        if (attributes == null)
	        	imagemItem = new ImgItem(mContext);
	        else
	        {
	        	if (attributes.getAttributeCount() > 0)
	        		imagemItem = new ImgItem(mContext, attributes);
	        	else
	        		imagemItem = new ImgItem(mContext);
	        }
	        
	        //imagemItem = new ImgItem(mContext);
			//imagemItem.setPadding(2, 2, 2, 2);
			imagemItem.setLayoutParams(new GridView.LayoutParams(tamanhoImagem, tamanhoImagem));
			//imagemItem.setScaleType(ImageView.ScaleType.CENTER_CROP);

			
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