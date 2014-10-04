package com.example.tabletvox03f.management.assocImagemSom;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.AssocImagemSom;
import com.example.tabletvox03f.dal.FilesIO;

public class ItemSimplesAssocImagemSomAdapter extends ItemAssocImagemSomAdapter
{

	public ItemSimplesAssocImagemSomAdapter(Context context,
			ArrayList<AssocImagemSom> lista)
	{
		super(context, lista);
	}
	
	private class ViewHolder 
	{
		protected ImageView imgv;
	    protected TextView lblDesc;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = null;
		
		if (convertView == null)
		{
			view = inflator.inflate(R.layout.item_ais_simples, null);
			
			ViewHolder viewHolder 		= new ViewHolder();
			viewHolder.imgv				= (ImageView) 	view.findViewById(R.id.imgItem);
			viewHolder.lblDesc 			= (TextView) 	view.findViewById(R.id.lblDesc);
			
			view.setTag(viewHolder);
		}
		else
			view = convertView;
		
		ViewHolder holder = (ViewHolder) view.getTag();
		
		final AssocImagemSom ais = imagens.get(position);
		
		FilesIO fIO = new FilesIO(mContext);
		holder.lblDesc.setText(ais.getDesc());
		// recuperar imagem
		holder.imgv.setImageDrawable(fIO.getImgItemDrawableFromInternalStorageOrAssets(ais));
		//holder.imgv.setAssocImagemSom(ais);
		
		return view;
	}

}
