package com.example.tabletvox03f.management.assocImagemSom;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.FilesIO;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.management.OnImagemSelectedListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ItemDeleteAssocImagemSomAdapter extends ItemAssocImagemSomAdapter
{
	
	public ItemDeleteAssocImagemSomAdapter(Context context,
			ArrayList<AssocImagemSom> lista)
	{
		super(context, lista);
		mListener = (OnImagemSelectedListener) context;
	}
	
	private class ViewHolder 
	{
		protected ImageView imgv;
	    protected TextView lblDesc;
	    protected ImageButton btnDelete;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = null;
		
		if (convertView == null)
		{
			view = inflator.inflate(R.layout.item_ais, null);
			
			ViewHolder viewHolder 		= new ViewHolder();
			viewHolder.imgv				= (ImageView) 	view.findViewById(R.id.imgItem);
			viewHolder.lblDesc 			= (TextView) 	view.findViewById(R.id.lblDesc);
			viewHolder.btnDelete		= (ImageButton) view.findViewById(R.id.btnDelete);
			
			// esconder botão edit
			((ImageButton) view.findViewById(R.id.btnEdit)).setVisibility(View.GONE);
			
			// para o click dos botões não "consumirem" o click do item do ListView
			// sem essas linhas de código não funciona o click no item da lista
			viewHolder.btnDelete.setFocusable(false);
			
			view.setTag(viewHolder);			
		}
		else
			view = convertView;
		
		ViewHolder holder = (ViewHolder) view.getTag();
		
		final AssocImagemSom ais = imagens.get(position);
		
		FilesIO fIO = new FilesIO(mContext);
		holder.lblDesc.setText(ais.getDesc());
		// recuperar imagem
		ImageLoader.getInstance().displayImage(fIO.getImgItemUriFromInternalStorageOrAssets(ais), holder.imgv);
		
		// eventos click dos botoes
		
		holder.btnDelete.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(ItemDeleteAssocImagemSomAdapter.this.mContext);
				
				// conteudo da dialog
				builder.setMessage("Tem Certeza?");
				builder.setTitle("Confirmar Exclusão");
				
				// botoes
				
				// botao sim
				builder.setPositiveButton("Sim", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						
						removeItem(ais);
						
						mListener.onDeleteItem(ais);
						
						// refresh na lista
						ItemDeleteAssocImagemSomAdapter.this.refresh();
					}
				});
				
				// botao não
				builder.setNegativeButton("Não", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						Toast.makeText(ItemDeleteAssocImagemSomAdapter.this.mContext, "Cancelado!", Toast.LENGTH_SHORT).show();
						return;
					}
				});				
				
				AlertDialog dialog = builder.create();
				
				dialog.show();
						
			}
		});
		
		return view;
	}

}
