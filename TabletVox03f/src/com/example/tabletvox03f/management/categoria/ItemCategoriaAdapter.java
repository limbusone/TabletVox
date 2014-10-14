package com.example.tabletvox03f.management.categoria;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.tabletvox03f.dal.CategoriaDAOSingleton;
import com.example.tabletvox03f.dal.FilesIO;
import com.example.tabletvox03f.management.perfil.FormularioPerfilActivity;

public class ItemCategoriaAdapter extends BaseAdapter
{

	protected Context mContext;
	protected ArrayList<Categoria> categorias;
	protected LayoutInflater inflator;	

	
	public ArrayList<Categoria> getCategorias()
	{
		return this.categorias;
	}
	@Override
	public int getCount()
	{
		return categorias.size();
	}

	@Override
	public Object getItem(int position)
	{
		return categorias.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}
	
	public ItemCategoriaAdapter(Context context, ArrayList<Categoria> lista)
	{
		this.mContext = context;
		this.categorias = lista;
		this.inflator = LayoutInflater.from(this.mContext);
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
		
		View view = null;
		
		if (convertView == null)
		{
			view = inflator.inflate(R.layout.item_categoria, null);
			
			ViewHolder viewHolder 		= new ViewHolder();
			viewHolder.imgv				= (ImageView) 	view.findViewById(R.id.imgItem);
			viewHolder.lblNomeCategoria = (TextView) 	view.findViewById(R.id.lblNomeCategoria);
			viewHolder.btnEdit			= (ImageButton) view.findViewById(R.id.btnEdit);
			viewHolder.btnDelete		= (ImageButton) view.findViewById(R.id.btnDelete);
			viewHolder.btnManage		= (ImageButton) view.findViewById(R.id.btnManage);
			
			// para o click dos bot�es n�o "consumirem" o click do item do ListView
			// sem essas linhas de c�digo n�o funciona o click no item da lista
			viewHolder.btnDelete.setFocusable(false);
			viewHolder.btnEdit.setFocusable(false);
			viewHolder.btnManage.setFocusable(false);
			
			// esconder bot�o gerenciar
			viewHolder.btnManage.setVisibility(View.GONE);
			
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
		
//		holder.btnManage.setOnClickListener(new OnClickListener()
//		{
//			
//			@Override
//			public void onClick(View v)
//			{
//				Toast.makeText(ItemCategoriaAdapter.this.mContext, "Voc� clicou no bot�o Gerenciar!", Toast.LENGTH_SHORT).show();
//			}
//		});
		
		holder.btnEdit.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Toast.makeText(ItemCategoriaAdapter.this.mContext, "Voc� clicou no bot�o Editar!", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(ItemCategoriaAdapter.this.mContext, FormularioCategoriaActivity.class);
				intent.putExtra("categoria", categoria);
				
				// verifica se o "parent" desse adapter � a lista de categorias
				if (ItemCategoriaAdapter.this.mContext instanceof ListaCategoriasActivity)
				{
					intent.putExtra("tipo_form", 1);
					((Activity) ItemCategoriaAdapter.this.mContext).startActivityForResult(intent, 2);
				}
				// verifica se o "parent" desse adapter � o formulario do perfil
				else if (ItemCategoriaAdapter.this.mContext instanceof FormularioPerfilActivity)
				{
					intent.putExtra("tipo_form", 2);
					((Activity) ItemCategoriaAdapter.this.mContext).startActivityForResult(intent, 3);
				}
					
					
					
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
				builder.setTitle("Confirmar Exclus�o");
				
				// botoes
				
				// botao sim
				builder.setPositiveButton("Sim", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						
						Toast.makeText(ItemCategoriaAdapter.this.mContext, 
						"Excluido com sucesso! ID: " + Integer.toString(categoria.getId()), 
						Toast.LENGTH_SHORT).show();
						
						removeItem(categoria);
						
						
						// refresh na lista
						ItemCategoriaAdapter.this.refresh();
						
						// verifica se o "parent" desse adapter � a lista de categorias
						if (ItemCategoriaAdapter.this.mContext instanceof ListaCategoriasActivity)
						{
							// exclui efetivamente a categoria
							CategoriaDAOSingleton.getInstance().excluirCategoria(categoria.getId());
							
							// atualiza o label dos registros encontrados
							ListaCategoriasActivity lca = (ListaCategoriasActivity) ItemCategoriaAdapter.this.mContext;
							lca.atualizarLblNumEncontrados(ItemCategoriaAdapter.this.getCount());
						}
						// verifica se o "parent" desse adapter � o formulario do perfil
						else if (ItemCategoriaAdapter.this.mContext instanceof FormularioPerfilActivity)
						{
							((FormularioPerfilActivity) ItemCategoriaAdapter.this.mContext).excluirCategoriaDasNovasCategorias(categoria);
							((FormularioPerfilActivity) ItemCategoriaAdapter.this.mContext).excluirCategoriaDasAntigasCategorias(categoria);
						}
					}
				});
				
				// botao n�o
				builder.setNegativeButton("N�o", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						Toast.makeText(ItemCategoriaAdapter.this.mContext, "Cancelado!", Toast.LENGTH_SHORT).show();
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
	
	public void removeItem(int position)
	{
		categorias.remove(position);
	}
	
	public void removeItem(Categoria categoria)
	{
		categorias.remove(categoria);
	}
	
	public void addItem(Categoria categoria)
	{
		categorias.add(categoria);
	}
	
	public void editItem(Categoria categoriaNova, int id)
	{
		Categoria categoria = getCategoriaById(id);
		categoria.setNome(categoriaNova.getNome());
		categoria.setAIS(categoriaNova.getAIS());
		categoria.setImagens(categoriaNova.getImagens());	
	}
	
	private Categoria getCategoriaById(int id)
	{
		Categoria categoria = null;
		Categoria categoria_temp;
		for (int i = 0, length = categorias.size(); i < length; i++)
		{
			categoria_temp = categorias.get(i);
			if (categoria_temp.getId() == id)
			{
				categoria = categoria_temp;
				break;
			}

		}
		
		return categoria;
	}	
	
	public void refresh()
	{
		// informar o adapter que houve uma mudan�a na lista de views (tipo um refresh na lista)
		notifyDataSetChanged();
	}	

}