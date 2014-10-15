package com.example.tabletvox03f.management.assocImagemSom;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.AssocImagemSom;
import com.example.tabletvox03f.dal.FilesIO;

public class ItemSelectAssocImagemSomAdapter extends ItemAssocImagemSomAdapter
{

	private ArrayList<AssocImagemSom> selecionados;
	
	public ItemSelectAssocImagemSomAdapter(Context context, ArrayList<AssocImagemSom> lista)
	{
		super(context, lista);
		selecionados = new ArrayList<AssocImagemSom>();
	}
	
	private class ViewHolder 
	{
		protected CheckBox checkbox;
		protected ImageView imgv;
	    protected TextView lblDesc;
	}
	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
		View view = null;
		
		if (convertView == null)
		{
			view = inflator.inflate(R.layout.item_ais_checkbox, null);
			
			ViewHolder viewHolder 		= new ViewHolder();
			viewHolder.checkbox			= (CheckBox) 	view.findViewById(R.id.checkBox);
			viewHolder.imgv				= (ImageView) 	view.findViewById(R.id.imgItem);
			viewHolder.lblDesc 			= (TextView) 	view.findViewById(R.id.lblDesc);
			
			viewHolder.checkbox.setFocusable(false);
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
		
		// adicionando ou excluindo da lista de selecionados conforme estado do checkbox
		holder.checkbox.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				CheckBox chk = (CheckBox) v;
				AssocImagemSom ais = (AssocImagemSom) chk.getTag();
				
				if (chk.isChecked())
					selecionados.add(ais);
				else
					selecionados.remove(ais);
			}
		});
		
		holder.checkbox.setTag(ais);
		
		if (selecionados.contains(ais))
			holder.checkbox.setChecked(true);
		else
			holder.checkbox.setChecked(false);		
		
		return view;
	}

	public ArrayList<AssocImagemSom> getSelecionados()
	{
		return selecionados;
	}

	public void setSelecionados(ArrayList<AssocImagemSom> selecionados)
	{
		this.selecionados = selecionados;
	}

}
