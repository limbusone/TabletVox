package com.example.tabletvox03f.management;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.AssocImagemSom;
import com.example.tabletvox03f.dal.Categoria;
import com.example.tabletvox03f.dal.FilesIO;

public class ItemSelectCategoriaAdapter extends ItemCategoriaAdapter
{


	public ItemSelectCategoriaAdapter(Context context, ArrayList<Categoria> lista)
	{
		super(context, lista);
	}
	
	private class ViewHolder 
	{
		protected ImageView imgv;
	    protected TextView lblNomeCategoria;
	}
	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		
		View view = null;
		
		if (convertView == null)
		{
			view = inflator.inflate(R.layout.item_categoria, null);
			
			ViewHolder viewHolder 		= new ViewHolder();
			viewHolder.imgv				= (ImageView) 	view.findViewById(R.id.imgItem);
			viewHolder.lblNomeCategoria = (TextView) 	view.findViewById(R.id.lblNomeCategoria);
			
			view.setTag(viewHolder);
		}
		else
			view = convertView;
		
		ViewHolder holder = (ViewHolder) view.getTag();
		
		final Categoria categoria = categorias.get(position);
		
		AssocImagemSom ais = categoria.getAIS();
		FilesIO fIO = new FilesIO(mContext);
		holder.lblNomeCategoria.setText(categoria.getNome());
		// recuperar imagem
		holder.imgv.setImageDrawable(fIO.getImgItemDrawableFromInternalStorageOrAssets(ais));
		//holder.imgv.setAssocImagemSom(ais);
		
		
		return view;
	}
	
}
