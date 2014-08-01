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

import com.example.tabletvox03f.Utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class FilesIO
{
	private Context activeContext;
	
	public FilesIO()
	{}
	
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
					if (!(file.exists())) // se o arquivo n�o existir em internal storage
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
	
	public boolean verificarExtensaoImagem(String path)
	{
		String ext = getExtensaoDoArquivo(path);
		return (
					ext.equals("jpg")  ||
					ext.equals("png") ||
					ext.equals("gif") ||
					ext.equals("JPG") ||
					ext.equals("PNG") ||
					ext.equals("GIF")
				);
			
	}
	
	public boolean verificarExtensaoSom(String path)
	{
		String ext = getExtensaoDoArquivo(path);
		return  (
					ext.equals("wav") ||
					ext.equals("WAV")
				);		
	}
	
	public String getExtensaoDoArquivo(String path)
	{
		return path.substring(path.lastIndexOf('.') + 1);
	}
	
	public String getNomeDoArquivoSemExtensao(String path)
	{
		return path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
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
}