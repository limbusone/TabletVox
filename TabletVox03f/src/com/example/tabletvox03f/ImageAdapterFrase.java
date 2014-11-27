package com.example.tabletvox03f;

import java.util.ArrayList;

import com.example.tabletvox03f.management.Opcoes;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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
			
			imagemItem = imgi_list.get(position);
			imagemItem.setLayoutParams(new GridView.LayoutParams(Opcoes.getImageWidth(), Opcoes.getImageHeight()));
			imagemItem.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imagemItem.setPadding(2, 2, 2, 2);
			
			//imagemItem = imgi_list.get(position);
		}
		else 
			imagemItem = (ImgItem) convertView;
		

		return imagemItem;
	}

}