package com.example.tabletvox03f.management.categoria;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tabletvoxdemo.R;
import com.example.tabletvox03f.dal.FilesIO;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.categoria.Categoria;
import com.example.tabletvox03f.dal.categoria.ListaCategoria;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ItemSelectCategoriaAdapter extends ItemCategoriaAdapter
{

	private ListaCategoria selecionados;
	
	public ItemSelectCategoriaAdapter(Context context, ListaCategoria lista)
	{
		super(context, lista);
		selecionados = new ListaCategoria();
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
		ImageLoader.getInstance().displayImage(fIO.getImgItemUriFromInternalStorageOrAssets(ais), holder.imgv);
		//holder.imgv.setAssocImagemSom(ais);
		
		// adicionando ou excluindo da lista de selecionados conforme estado do checkbox
		holder.checkbox.setOnClickListener(new OnClickListener()
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
		
		holder.checkbox.setTag(categoria);
		
		if (selecionados.contains(categoria))
			holder.checkbox.setChecked(true);
		else
			holder.checkbox.setChecked(false);
		
		return view;
	}

	public ListaCategoria getSelecionados()
	{
		return selecionados;
	}

	public void setSelecionados(ListaCategoria selecionados)
	{
		this.selecionados = selecionados;
	}
	
}
