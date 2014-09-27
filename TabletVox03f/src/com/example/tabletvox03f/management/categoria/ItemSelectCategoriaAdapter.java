package com.example.tabletvox03f.management.categoria;

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
import com.example.tabletvox03f.dal.Categoria;
import com.example.tabletvox03f.dal.FilesIO;

public class ItemSelectCategoriaAdapter extends ItemCategoriaAdapter
{

	private ArrayList<Categoria> selecionados;
	
	public ItemSelectCategoriaAdapter(Context context, ArrayList<Categoria> lista)
	{
		super(context, lista);
		selecionados = new ArrayList<Categoria>();
	}
	
	private class ViewHolder 
	{
		protected CheckBox checkbox;
		protected ImageView imgv;
	    protected TextView lblNomeCategoria;
	}
	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
		View view = null;
		
		if (convertView == null)
		{
			view = inflator.inflate(R.layout.item_categoria_checkbox, null);
			
			ViewHolder viewHolder 		= new ViewHolder();
			viewHolder.checkbox			= (CheckBox) 	view.findViewById(R.id.checkBox);
			viewHolder.imgv				= (ImageView) 	view.findViewById(R.id.imgItem);
			viewHolder.lblNomeCategoria = (TextView) 	view.findViewById(R.id.lblNomeCategoria);
			
			// adicionando ou excluindo da lista de selecionados conforme estado do checkbox
			viewHolder.checkbox.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					CheckBox chk = (CheckBox) v;
					Categoria categoria = (Categoria) chk.getTag();
					
					if (chk.isChecked())
						selecionados.add(categoria);
					else
						selecionados.remove(categoria);
				}
			});
			
			viewHolder.checkbox.setFocusable(false);
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
		holder.checkbox.setTag(categoria);
		
		return view;
	}

	public ArrayList<Categoria> getSelecionados()
	{
		return selecionados;
	}

	public void setSelecionados(ArrayList<Categoria> selecionados)
	{
		this.selecionados = selecionados;
	}
	
}
