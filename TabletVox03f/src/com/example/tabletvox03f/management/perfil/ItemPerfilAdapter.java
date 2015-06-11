package com.example.tabletvox03f.management.perfil;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabletvox03f.R;
import com.example.tabletvox03f.dal.perfil.Perfil;
import com.example.tabletvox03f.management.OnPerfilSelectedListener;

public class ItemPerfilAdapter extends BaseAdapter
{
	
	private Context mContext;
	private ArrayList<Perfil> perfis;
	private LayoutInflater inflator;
	private OnPerfilSelectedListener mListener;

	public ItemPerfilAdapter(Context context, ArrayList<Perfil> lista)
	{
		this.mContext 	= context;
		this.perfis 	= lista;
		this.inflator 	= LayoutInflater.from(this.mContext);
		this.mListener  = (OnPerfilSelectedListener) this.mContext;
	}
	
	private class ViewHolder 
	{
	    protected TextView lblAutor, lblNomePerfil;
	    protected ImageButton btnEdit, btnDelete, btnManage;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = null;
		
		if (convertView == null)
		{
			view = inflator.inflate(R.layout.item_perfil, null);
			
			ViewHolder viewHolder 		= new ViewHolder();
			viewHolder.lblAutor			= (TextView) view.findViewById(R.id.lblAutor);
			viewHolder.lblNomePerfil	= (TextView) view.findViewById(R.id.lblNomePerfil);
			viewHolder.btnEdit			= (ImageButton) view.findViewById(R.id.btnEdit);
			viewHolder.btnDelete		= (ImageButton) view.findViewById(R.id.btnDelete);
			viewHolder.btnManage		= (ImageButton) view.findViewById(R.id.btnManage);
			
			
			
//			viewHolder.btnDelete.setOnClickListener(new OnClickListener()
//			{
//				
//				@Override
//				public void onClick(View v)
//				{
//					Toast.makeText(ItemPerfilAdapter.this.mContext, "Você clicou no botão Deletar!", Toast.LENGTH_SHORT).show();
//				}
//			});
			
			
			// para o click dos botões não "consumirem" o click do item do ListView
			// sem essas linhas de código não funciona o click no item da lista
			viewHolder.btnDelete.setFocusable(false);
			viewHolder.btnEdit.setFocusable(false);
			viewHolder.btnManage.setFocusable(false);
			
			//esconder botão gerenciar
			viewHolder.btnManage.setVisibility(View.GONE);
			
			view.setTag(viewHolder);
		}
		else
			view = convertView;
		
		ViewHolder holder = (ViewHolder) view.getTag();
		
		final Perfil perfil = perfis.get(position);
		
		holder.lblAutor.setText(perfil.getAutor());
		holder.lblNomePerfil.setText(perfil.getNome());

//		holder.btnManage.setOnClickListener(new OnClickListener()
//		{
//			
//			@Override
//			public void onClick(View v)
//			{
//				// setar PERFIL_ATIVO e abrir o activity ListaCategoriasDoPerfilActivity 
//				Toast.makeText(ItemPerfilAdapter.this.mContext, "Você clicou no botão Gerenciar!", Toast.LENGTH_SHORT).show();
////				Utils.PERFIL_ATIVO = perfil;
////				Intent intent = new Intent(ItemPerfilAdapter.this.mContext, ListaCategoriasDoPerfilActivity.class);
////				ItemPerfilAdapter.this.mContext.startActivity(intent);
//			}
//		});
		
		holder.btnEdit.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				//Toast.makeText(ItemPerfilAdapter.this.mContext, "Você clicou no botão Editar!", Toast.LENGTH_SHORT).show();
				mListener.onEditItem(perfil);

			}
		});
		
		holder.btnDelete.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(ItemPerfilAdapter.this.mContext);
				
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
						boolean deletou = false;
						
						// verifica se o "parent" desse adapter é a tela de selecionarPerfilActivity
						if (ItemPerfilAdapter.this.mContext instanceof SelecionarPerfilActivity)
							deletou = mListener.onDeleteItem(perfil, ItemPerfilAdapter.this.getCount() - 1);
						
						if (deletou)
						{
							removeItem(perfil);
							// refresh na lista
							ItemPerfilAdapter.this.refresh();
						}
					}
				});
				
				// botao não
				builder.setNegativeButton("Não", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						Toast.makeText(ItemPerfilAdapter.this.mContext, "Cancelado!", Toast.LENGTH_SHORT).show();
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

	@Override
	public int getCount()
	{
		return perfis.size();
	}

	@Override
	public Perfil getItem(int position) 
	{
		return perfis.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return 0;
	}
	
	public void removeItem(int position)
	{
		perfis.remove(position);
	}
	
	public void removeItem(Perfil perfil)
	{
		perfis.remove(perfil);
	}

	public void refresh()
	{
		// informar o adapter que houve uma mudança na lista de views (tipo um refresh na lista)
		notifyDataSetChanged();
	}
}
