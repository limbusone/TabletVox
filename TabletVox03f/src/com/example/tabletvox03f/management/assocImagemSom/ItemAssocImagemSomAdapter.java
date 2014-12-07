package com.example.tabletvox03f.management.assocImagemSom;

import java.util.ArrayList;

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

import com.example.tabletvox03f.Erro;
import com.example.tabletvox03f.R;
import com.example.tabletvox03f.Utils;
import com.example.tabletvox03f.dal.FilesIO;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSomDAO;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ItemAssocImagemSomAdapter extends BaseAdapter
{

	protected Context mContext;
	protected ArrayList<AssocImagemSom> imagens;
	protected LayoutInflater inflator;
	
	public ItemAssocImagemSomAdapter(Context context, ArrayList<AssocImagemSom> lista)
	{
		this.mContext 	= context;
		this.imagens 	= lista;
		this.inflator 	= LayoutInflater.from(this.mContext);
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
			
			// para o click dos botões não "consumirem" o click do item do ListView
			// sem essas linhas de código não funciona o click no item da lista
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
				Toast.makeText(ItemAssocImagemSomAdapter.this.mContext, "Você clicou no botão Editar!", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(ItemAssocImagemSomAdapter.this.mContext, FormularioAssocImagemSomActivity.class);
				intent.putExtra("tipo_form", 1);
				intent.putExtra("ais", ais);
				ItemAssocImagemSomAdapter.this.mContext.startActivity(intent);
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
				builder.setTitle("Confirmar Exclusão");
				
				// botoes
				
				// botao sim
				builder.setPositiveButton("Sim", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						removeItem(ais);
						
						Toast.makeText(ItemAssocImagemSomAdapter.this.mContext, 
						"Excluido com sucesso! ID: " + Integer.toString(ais.getId()), 
						Toast.LENGTH_SHORT).show();						

						// refresh na lista
						ItemAssocImagemSomAdapter.this.refresh();							
						
						// verifica se o "parent" desse adapter é a lista de imagens
						if (ItemAssocImagemSomAdapter.this.mContext instanceof ListaImagensActivity)
						{
							FilesIO fio = new FilesIO(ItemAssocImagemSomAdapter.this.mContext);
							if (!(fio.deletarArquivosDeImagemESom(ais)))
							{
								Utils.erros.add(new Erro("Erro ao excluir o arquivo de imagem ou de som. Exclusão cancelada!"));
								Utils.exibirErros(ItemAssocImagemSomAdapter.this.mContext);
								return;
							}							
							
							//AssocImagemSomDAOSingleton.getInstance().excluirAssocImagemSom(ais.getId());
							
							AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(ItemAssocImagemSomAdapter.this.mContext);
							dao_ais.open();
							dao_ais.delete(ais.getId());
							dao_ais.close();
							
							// atualiza o label dos registros encontrados
							ListaImagensActivity lia = (ListaImagensActivity) ItemAssocImagemSomAdapter.this.mContext;
							lia.atualizarLblNumEncontrados(ItemAssocImagemSomAdapter.this.getCount());
						}
						
					}
				});
				
				// botao não
				builder.setNegativeButton("Não", new DialogInterface.OnClickListener()
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
				
				
//				Toast.makeText(ItemPerfilAdapter.this.mContext, "Você clicou no botão Deletar!", Toast.LENGTH_SHORT).show();
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
		// informar o adapter que houve uma mudança na lista de views (tipo um refresh na lista)
		notifyDataSetChanged();
	}	
}
