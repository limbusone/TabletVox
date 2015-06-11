package com.example.tabletvox03f.management.assocImagemSom;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabletvox03f.Erro;
import com.example.tabletvox03f.R;
import com.example.tabletvox03f.Utils;
import com.example.tabletvox03f.dal.FilesIO;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSomDAO;
import com.example.tabletvox03f.management.OnImagemSelectedListener;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ItemAssocImagemSomAdapter extends BaseAdapter
{

	protected Context mContext;
	protected ArrayList<AssocImagemSom> imagens;
	protected LayoutInflater inflator;
	protected OnImagemSelectedListener mListener;
	
	public ItemAssocImagemSomAdapter(Context context, ArrayList<AssocImagemSom> lista)
	{
		this.mContext 	= context;
		this.imagens 	= lista;
		this.inflator 	= LayoutInflater.from(this.mContext);
		this.mListener  = (OnImagemSelectedListener) this.mContext;
	}
	
	private class ViewHolder 
	{
		protected ImageView imgv;
	    protected TextView lblDesc;
	    protected ImageButton btnEdit, btnDelete;
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
			viewHolder.btnEdit			= (ImageButton) view.findViewById(R.id.btnEdit);
			viewHolder.btnDelete		= (ImageButton) view.findViewById(R.id.btnDelete);
			
			// para o click dos bot�es n�o "consumirem" o click do item do ListView
			// sem essas linhas de c�digo n�o funciona o click no item da lista
			viewHolder.btnDelete.setFocusable(false);
			viewHolder.btnEdit.setFocusable(false);
			
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
		//holder.imgv.setAssocImagemSom(ais);
		
		// eventos click dos botoes
		
		holder.btnEdit.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				mListener.onEditItem(ais);
			}
		});
		
		holder.btnDelete.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(ItemAssocImagemSomAdapter.this.mContext);
				
				// conteudo da dialog
				builder.setMessage("Tem Certeza?");
				builder.setTitle("Confirmar Exclus�o");
				
				// botoes
				
				// botao sim
				builder.setPositiveButton("Sim", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						boolean deletou = false;
						
						// deleta efetivamente a imagem e atualiza o label dos registros encontrados
						deletou = mListener.onDeleteItem(ais, ItemAssocImagemSomAdapter.this.getCount());
						
						if (deletou)
						{
							removeItem(ais);
							// refresh na lista
							ItemAssocImagemSomAdapter.this.refresh();							
						}
						
					}
				});
				
				// botao n�o
				builder.setNegativeButton("N�o", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						Toast.makeText(ItemAssocImagemSomAdapter.this.mContext, "Cancelado!", Toast.LENGTH_SHORT).show();
						return;
					}
				});				
				
				AlertDialog dialog = builder.create();
				
				dialog.show();
				
				
//				Toast.makeText(ItemPerfilAdapter.this.mContext, "Voc� clicou no bot�o Deletar!", Toast.LENGTH_SHORT).show();
			}
		});
		
		return view;
	}

	@Override
	public int getCount()
	{
		return imagens.size();
	}

	@Override
	public Object getItem(int position)
	{
		return imagens.get(position);
	}

	@Override
	public long getItemId(int arg0)
	{
		return 0;
	}
	
	public void removeItem(int position)
	{
		imagens.remove(position);
	}
	
	public void removeItem(AssocImagemSom ais)
	{
		imagens.remove(ais);
	}
	
	public void addItem(AssocImagemSom ais)
	{
		imagens.add(ais);
	}
	
	public ArrayList<AssocImagemSom> getItems()
	{
		return this.imagens;
	}
	
	public void refresh()
	{
		// informar o adapter que houve uma mudan�a na lista de views (tipo um refresh na lista)
		notifyDataSetChanged();
	}	
}
