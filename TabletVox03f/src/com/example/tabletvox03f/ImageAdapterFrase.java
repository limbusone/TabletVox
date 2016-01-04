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

import com.example.tabletvox03f.management.Opcoes;
import com.example.tabletvoxdemo.R;

public class ImageAdapterFrase extends BaseAdapter 
{
	

	// references to our images
	// private int[] mThumbIds;

	private ArrayList<ImgItem> imgi_list;


	public ImageAdapterFrase(ArrayList<ImgItem> a) 
	{
		imgi_list = a;
	}
	

	public int getCount() 
	{
		return imgi_list.size();
	}

	public Object getItem(int position) 
	{
		return null;
	}

	public long getItemId(int position) 
	{
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		
		ImgItem imagemItem;
		if (convertView == null)
		{
			SharedPreferences sp 	= PreferenceManager.getDefaultSharedPreferences(parent.getContext());
			int tamanhoImagem 		= Integer.parseInt(sp.getString(Opcoes.TAMANHO_IMAGEM_KEY, "" + Opcoes.TAMANHO_IMAGEM_DEFAULT));			
			
			Context mContext = imgi_list.get(position).getContext();
			
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
	        	imagemItem = new ImgItem(imgi_list.get(position));
	        else
	        {
	        	if (attributes.getAttributeCount() > 0)
	        		imagemItem = new ImgItem(imgi_list.get(position), attributes);
	        	else
	        		imagemItem = new ImgItem(imgi_list.get(position));
	        }			
			
			//imagemItem = new ImgItem(imgi_list.get(position));
			//imagemItem.setPadding(2, 2, 2, 2);
			imagemItem.setLayoutParams(new GridView.LayoutParams(tamanhoImagem, tamanhoImagem));
			//imagemItem.setScaleType(ImageView.ScaleType.FIT_CENTER);
			
			
			//imagemItem = imgi_list.get(position);
		}
		else 
			imagemItem = (ImgItem) convertView;
		

		return imagemItem;
	}

}