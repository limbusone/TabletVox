package com.example.tabletvox03f.management;

import java.util.ArrayList;

import android.annotation.SuppressLint;
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

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.AssocImagemSom;
import com.example.tabletvox03f.dal.Categoria;
import com.example.tabletvox03f.dal.FilesIO;

public class ItemCategoriaAdapter extends BaseAdapter
{

	private Context mContext;
	private ArrayList<Categoria> categorias;
	private LayoutInflater inflator;	

	
	public ArrayList<Categoria> getCategorias()
	{
		return this.categorias;
	}
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return categorias.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return categorias.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	public ItemCategoriaAdapter(Context context, ArrayList<Categoria> lista)
	{
		this.mContext = context;
		this.categorias = lista;
		this.inflator = LayoutInflater.from(this.mContext);
		// TODO Auto-generated constructor stub
	}
	
	private class ViewHolder 
	{
		protected ImageView imgv;
	    protected TextView lblNomeCategoria;
	    protected ImageButton btnEdit, btnDelete, btnManage;
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
			
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.imgv				= (ImageView) 	view.findViewById(R.id.imgItem);
			viewHolder.lblNomeCategoria = (TextView) 	view.findViewById(R.id.lblNomeCategoria);
			viewHolder.btnEdit			= (ImageButton) view.findViewById(R.id.btnEdit);
			viewHolder.btnDelete		= (ImageButton) view.findViewById(R.id.btnDelete);
			viewHolder.btnManage		= (ImageButton) view.findViewById(R.id.btnManage);
			
			// para o click dos botões não "consumirem" o click do item do ListView
			// sem essas linhas de código não funciona o click no item da lista
			viewHolder.btnDelete.setFocusable(false);
			viewHolder.btnEdit.setFocusable(false);
			viewHolder.btnManage.setFocusable(false);
			
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
		
		// eventos click dos botoes
		
		holder.btnManage.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Toast.makeText(ItemCategoriaAdapter.this.mContext, "Você clicou no botão Gerenciar!", Toast.LENGTH_SHORT).show();
			}
		});
		
		holder.btnEdit.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Toast.makeText(ItemCategoriaAdapter.this.mContext, "Você clicou no botão Editar!", Toast.LENGTH_SHORT).show();
			}
		});
		
		holder.btnDelete.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(ItemCategoriaAdapter.this.mContext);
				
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
						// TODO Auto-generated method stub
						//PerfilDAOSingleton.getInstance().excluirPerfil(perfil.getId());
						
						Toast.makeText(ItemCategoriaAdapter.this.mContext, 
						"Excluido com sucesso! ID: " + Integer.toString(categoria.getId()), 
						Toast.LENGTH_SHORT).show();
						
						removeItem(categoria);
						// refresh na lista
						ItemCategoriaAdapter.this.refresh();
					}
				});
				
				// botao não
				builder.setNegativeButton("Não", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						Toast.makeText(ItemCategoriaAdapter.this.mContext, "Cancelado!", Toast.LENGTH_SHORT).show();
						return;
					}
				});				
				
				AlertDialog dialog = builder.create();
				
				dialog.show();
				
				
//				Toast.makeText(ItemPerfilAdapter.this.mContext, "Você clicou no botão Deletar!", Toast.LENGTH_SHORT).show();
			}
		});
		
		return view;
	}
	
	public void removeItem(int position)
	{
		categorias.remove(position);
	}
	
	public void removeItem(Categoria categoria)
	{
		categorias.remove(categoria);
	}

	public void refresh()
	{
		// informar o adapter que houve uma mudança na lista de views (tipo um refresh na lista)
		notifyDataSetChanged();
	}	

}
