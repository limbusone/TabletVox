package com.example.tabletvox03f.dal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.tabletvox03f.Utils;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;

public class FilesIO
{
	
	public static final String EXTENSAO_ARQUIVO_SOM = "wav";
	
	private Context activeContext;
	
	public FilesIO(Context c)
	{
		activeContext = c;
	}
	
	public boolean perfil_categorizado_xml_exists()
	{
		return activeContext.getFileStreamPath(Utils.PERFIL_ATIVO.getNome() + "_categorias.xml").exists();
	}
	
	public void copiarArquivosXmlDeAssetsParaInternalStorage()
	{
		
		String[] list;
		try
		{
			int l;
			list = activeContext.getAssets().list("dados_xml");
			if ((l = list.length) > 0)
			{
				String file_name;
				for (int i = 0; i < l; i++)
				{
					file_name = list[i];
					File file = activeContext.getFileStreamPath(file_name);
					if (!(file.exists())) // se o arquivo não existir em internal storage
						copiar_colar_xml_de_assets_para_internal_storage(file_name);
				}
			}
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	// tipo 0: imagem, 1: som
	public void copiarArquivoDeSomOuImagemParaInternalStorage(String de_caminho_arquivo, String para, int tipo) 
			throws IOException
	{

		String pasta = null;
		
		pasta = (tipo == 1) ? "sons" : "imagens";
		
		File fi = new File(de_caminho_arquivo);
		File fo = new File(activeContext.getDir(pasta, Context.MODE_PRIVATE).getPath() + "/" +  para);
		
		InputStream in 	= new FileInputStream(fi);
		//OutputStream out = c.openFileOutput(para, Context.MODE_PRIVATE);
		OutputStream out = new FileOutputStream(fo);
		
	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0)
	        out.write(buf, 0, len);
	    in.close();
	    out.close();		
	}
	
	// tipo 0: imagem, 1: som
	public boolean deletarArquivoImagemOuSom(String arquivo, int tipo)
	{
		String pasta = null;

		pasta = (tipo == 1) ? "sons" : "imagens";
		
		File f = new File (activeContext.getDir(pasta, Context.MODE_PRIVATE).getPath() + arquivo);
		return f.delete();
	}
	
	public boolean deletarArquivosDeImagemESom(String arquivo_imagem, String arquivo_som)
	{
		File fi = new File(activeContext.getDir("imagens", Context.MODE_PRIVATE).getPath() + "/" + arquivo_imagem);
		File fs = new File(activeContext.getDir("sons", Context.MODE_PRIVATE).getPath() + "/" + arquivo_som);
		
		return (fi.exists() && fs.exists()) ? fi.delete() && fs.delete() : false;
	}
	
	public boolean deletarArquivosDeImagemESom(AssocImagemSom ais)
	{
		File fi = new File(activeContext.getDir("imagens", Context.MODE_PRIVATE).getPath() + "/" + ais.getTituloImagem() + "." + ais.getExt());
		File fs = new File(activeContext.getDir("sons", Context.MODE_PRIVATE).getPath() + "/" + ais.getTituloSom() + "." + FilesIO.EXTENSAO_ARQUIVO_SOM);
		
		return (fi.exists() && fs.exists()) ? fi.delete() && fs.delete() : false;
	}	
	
	public boolean verificarExtensaoImagem(String path)
	{
		
		String ext = getExtensaoDoArquivo(path);
		return (ext == null) ? false :
				(
					ext.equals("jpg")  ||
					ext.equals("png")  ||
					ext.equals("gif")  ||
					ext.equals("JPG")  ||
					ext.equals("PNG")  ||
					ext.equals("GIF")
				);
			
	}
	
	public boolean verificarExtensaoSom(String path)
	{
		String ext = getExtensaoDoArquivo(path);
		return (ext == null) ? false :
				(
					ext.equals("wav") ||
					ext.equals("WAV")
				);		
	}
	
	public String getExtensaoDoArquivo(String path)
	{
		return (!(path == null) && path.length() > 0) ? path.substring(path.lastIndexOf('.') + 1) : path;
	}
	
	public String getNomeDoArquivoSemExtensao(String path)
	{
		return (!(path == null) && path.length() > 0) ? path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.')) : path;
	}
	
//	public static void teste_arquivos(Context c)
//	{
//		XmlUtilsTeste xml = new XmlUtilsTeste(c);
//		//xml.addSingleNode("root", "jeca");
//		Toast.makeText(c, xml.getMalandroName(), Toast.LENGTH_SHORT).show();
//	}
	
	// de /assets para internal storage 
	public void copiar_colar_xml_de_assets_para_internal_storage(String nome_arquivo_xml)
	{
		
		BufferedReader de_reader 	= null;
		BufferedWriter para_writer 	= null;
		try
		{
			de_reader 	= 	new BufferedReader(
							new InputStreamReader(
							activeContext.getAssets().open("dados_xml/" + nome_arquivo_xml), "UTF-8"));
			
			para_writer = 	new BufferedWriter(
							new OutputStreamWriter(
							activeContext.openFileOutput(nome_arquivo_xml, Context.MODE_PRIVATE), "UTF-8"));
			
			String line = null;
			
			while ((line = de_reader.readLine()) != null)
				para_writer.write(line); //write
			
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try
			{
				de_reader.close();
				para_writer.close();
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	public String getRealPathFromURI(Uri uri) 
	{
	    Cursor cursor = activeContext.getContentResolver().query(uri, null, null, null, null); 
	    cursor.moveToFirst(); 
	    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
	    return cursor.getString(idx);
	}
	
	public String getLastPathSegment(String p)
	{
		int last_idx = p.lastIndexOf('/');
		return p.substring(last_idx + 1);
	}
	
	// aqui recupera-se a imagem em si
	// a imagem está sendo recuperada da pasta assets/imagens
	// ou da pasta imagens em internal storage
	public Drawable getImgItemDrawableFromInternalStorageOrAssets(AssocImagemSom ais)
	{
		Context mContext = activeContext;
		Drawable drawable = null;
		try
		{
			// aqui recupera-se a imagem em si
			// a imagem está sendo recuperada da pasta assets/imagens
			// ou da pasta imagens em internal storage			
			File f_from_internal_storage = new File
			(
				mContext.getDir("imagens", Context.MODE_PRIVATE).getPath() + 
				"/" +  ais.getTituloImagem() + "." + ais.getExt()
			);

			InputStream ims = (f_from_internal_storage.exists()) 
							   ? new FileInputStream(f_from_internal_storage) 
							   : mContext.getAssets().open("imagens/" + ais.getTituloImagem() + "." + ais.getExt());
			drawable = Drawable.createFromStream(ims, null);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return drawable;		
	}
}
