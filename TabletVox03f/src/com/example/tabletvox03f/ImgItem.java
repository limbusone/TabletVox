package com.example.tabletvox03f;


import java.io.File;
import java.io.IOException;

import com.example.tabletvox03f.dal.AssocImagemSom;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.widget.ImageView;

// essa classe representa as imagens que aparecem no software
public class ImgItem extends ImageView 
{

	private AssocImagemSom ais;
	private MediaPlayer mp;

	public ImgItem(Context ctx) 
	{
		super(ctx);
	}
	
	//construtor de copia
	public ImgItem(ImgItem i)
	{
		super(i.getContext());
		setImageDrawable(i.getDrawable());		
		ais = i.getAssocImagemSom();
	}

	public void setAssocImagemSom(AssocImagemSom value) 
	{
		this.ais = value;
	}

	public AssocImagemSom getAssocImagemSom() 
	{
		return this.ais;
	}
	

	public void tocarSom(Context c) 
	{
		
		//MediaPlayer mp = MediaPlayer.create(this.getContext(), ais.getIdSom());
		mp = new MediaPlayer();
		try
		{
			// aqui recupera-se o som em si
			// o som está sendo recuperado de internal storage
			// ou da pasta assets/sons
			
			File f_from_internal_storage = new File(c.getDir("sons", Context.MODE_PRIVATE).getPath() + "/" +  ais.getTituloSom() + "." + Utils.EXTENSAO_ARQUIVO_SOM );
			
			if (f_from_internal_storage.exists())
				mp.setDataSource(f_from_internal_storage.getAbsolutePath());
			else
			{

				AssetFileDescriptor afd = c.getAssets().openFd("sons/" + ais.getTituloSom() + "." + Utils.EXTENSAO_ARQUIVO_SOM);
				mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			}
			mp.prepare();
		} 
		catch (IOException e)
		{
			// Auto-generated catch block
			e.printStackTrace();
		}
		mp.start();
	}
	
	public void encerrarMediaPlayer()
	{
		mp.release();
		mp = null;
	}
	
	public void tocarSom(Intent ss)
	{
		this.getContext().startService(ss);		
	}
	
	public void tocarSom(String caminho_absoluto)
	{
		mp = new MediaPlayer();
		try
		{
			mp.setDataSource(caminho_absoluto);
			mp.prepare();
		} catch (IllegalArgumentException e)
		{
			// Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e)
		{
			// Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e)
		{
			// Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// Auto-generated catch block
			e.printStackTrace();
		}
		
		mp.start();
	}
	
}
