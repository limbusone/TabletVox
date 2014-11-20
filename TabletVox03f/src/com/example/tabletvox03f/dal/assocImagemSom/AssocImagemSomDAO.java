package com.example.tabletvox03f.dal.assocImagemSom;

import java.io.IOException;
import java.util.ArrayList;

import com.example.tabletvox03f.dal.FilesIO;
import com.example.tabletvox03f.dal.TabletVoxSQLiteOpenHelper;
import com.example.tabletvox03f.dal.categoria.CategoriaAssocImagemSomDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AssocImagemSomDAO
{
	private SQLiteDatabase database;
	private String[] columns = 
	{ 
		TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID,
		TabletVoxSQLiteOpenHelper.AIS_COLUMN_DESC,
		TabletVoxSQLiteOpenHelper.AIS_COLUMN_TITULO_IMAGEM,
		TabletVoxSQLiteOpenHelper.AIS_COLUMN_TITULO_SOM,
		TabletVoxSQLiteOpenHelper.AIS_COLUMN_EXT,
		TabletVoxSQLiteOpenHelper.AIS_COLUMN_TIPO,
		TabletVoxSQLiteOpenHelper.AIS_COLUMN_CMD,
		TabletVoxSQLiteOpenHelper.AIS_COLUMN_ATALHO
	};
	
	private TabletVoxSQLiteOpenHelper sqliteOpenHelper;
	
	public TabletVoxSQLiteOpenHelper getSqliteOpenHelper()
	{
		return sqliteOpenHelper;
	}

	public AssocImagemSomDAO(Context c)
	{
		sqliteOpenHelper = new TabletVoxSQLiteOpenHelper(c);
	}
	
	public AssocImagemSomDAO(TabletVoxSQLiteOpenHelper sqlOH)
	{
		sqliteOpenHelper = sqlOH;
	}
	
	// abre a conexão
	public void open() throws SQLException
	{
		database = sqliteOpenHelper.getWritableDatabase();
	}

	// fecha a conexão
	public void close()
	{
		sqliteOpenHelper.close();
	}
	
	public void create(AssocImagemSom ais)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_DESC, ais.getDesc());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_TITULO_IMAGEM, ais.getTituloImagem());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_TITULO_SOM, ais.getTituloSom());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_EXT, ais.getExt());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_TIPO, "" + ais.getTipo());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_CMD, ais.getCmd());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_ATALHO, ais.isAtalho());
		database.insert(TabletVoxSQLiteOpenHelper.TABLE_AIS, null, values);
	}
	
	public boolean create(AssocImagemSom ais, String caminhoOrigemImagem, String caminhoOrigemSom, String caminhoDestinoImagem, String caminhoDestinoSom, Context c) throws IOException
	{
		boolean retorno = true;
		
		FilesIO fio = new FilesIO(c);
		
		ContentValues values = new ContentValues();
		
		// upload imagem
		fio.copiarArquivoDeSomOuImagemParaInternalStorage(caminhoOrigemImagem, 	caminhoDestinoImagem, 	 0);
		// upload som
		fio.copiarArquivoDeSomOuImagemParaInternalStorage(caminhoOrigemSom, 	caminhoDestinoSom, 		 1);
		
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_DESC, ais.getDesc());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_TITULO_IMAGEM, ais.getTituloImagem());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_TITULO_SOM, ais.getTituloSom());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_EXT, ais.getExt());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_TIPO, "" + ais.getTipo());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_CMD, ais.getCmd());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_ATALHO, ais.isAtalho());
		
		database.insert(TabletVoxSQLiteOpenHelper.TABLE_AIS, null, values);

		return retorno;
	}
	
	public void create(String dsc, String ti, String ts, String ex, char tp, int cm, boolean ata)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_DESC, dsc);
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_TITULO_IMAGEM, ti);
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_TITULO_SOM, ts);
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_EXT, ex);
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_TIPO, "" + tp);
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_CMD, cm);
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_ATALHO, ata);
		database.insert(TabletVoxSQLiteOpenHelper.TABLE_AIS, null, values);		
	}
	
	public void create(String dsc, String ti, String ts, String ex, char tp, int cm)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_DESC, dsc);
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_TITULO_IMAGEM, ti);
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_TITULO_SOM, ts);
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_EXT, ex);
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_TIPO, "" + tp);
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_CMD, cm);
		database.insert(TabletVoxSQLiteOpenHelper.TABLE_AIS, null, values);		
	}	
	
	public void delete(long id)
	{
		database.delete(TabletVoxSQLiteOpenHelper.TABLE_AIS, TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID + " = " + id, null);
		// se esta imagem for vinculada a uma ou mais categorias, deleta as associações
		CategoriaAssocImagemSomDAO cat_ais_dao = new CategoriaAssocImagemSomDAO(sqliteOpenHelper);
		cat_ais_dao.open();
		cat_ais_dao.delete_imagem(id);
	}
	
	public void delete(long[] id_list)
	{
		int i = 0, length = id_list.length;
		
		for (; i < length; i++)
			database.delete(TabletVoxSQLiteOpenHelper.TABLE_AIS, TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID + " = " + id_list[i], null);
	}
	
	public void update(AssocImagemSom ais, long id)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_DESC, ais.getDesc());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_TITULO_IMAGEM, ais.getTituloImagem());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_TITULO_SOM, ais.getTituloSom());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_EXT, ais.getExt());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_TIPO, "" + ais.getTipo());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_CMD, ais.getCmd());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_ATALHO, ais.isAtalho());
		database.update(TabletVoxSQLiteOpenHelper.TABLE_AIS, values, TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID + " = " + id, null);
	}
	
	// retorna um arraylist com todos os registros
	public ArrayList<AssocImagemSom> getAll()
	{
		ArrayList<AssocImagemSom> ais_list = new ArrayList<AssocImagemSom>();
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_AIS, 
				columns, 
				null, null, null, null, null);
		
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			AssocImagemSom ais = new AssocImagemSom
			(
				cursor.getInt(0),
				cursor.getString(1), 
				cursor.getString(2),
				cursor.getString(3),
				cursor.getString(4), 
				cursor.getString(5).charAt(0), 
				cursor.getInt(6),
				(cursor.getInt(7) == 1) ? true : false
			 );
			ais_list.add(ais);
			cursor.moveToNext();
		}
		cursor.close();
		return ais_list;
	}
	
	// retorna todas as imagens exceto as imagens-comandos
	public ArrayList<AssocImagemSom> getImagens()
	{
		ArrayList<AssocImagemSom> ais_list = new ArrayList<AssocImagemSom>();
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_AIS, columns, 
				TabletVoxSQLiteOpenHelper.AIS_COLUMN_CMD + " = 0", 
				null, null, null, null);
		
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			AssocImagemSom ais = new AssocImagemSom
			(
				cursor.getInt(0),
				cursor.getString(1), 
				cursor.getString(2),
				cursor.getString(3),
				cursor.getString(4), 
				cursor.getString(5).charAt(0), 
				cursor.getInt(6),
				(cursor.getInt(7) == 1) ? true : false
			 );
			ais_list.add(ais);
			cursor.moveToNext();
		}
		cursor.close();
		
		return ais_list;		
	}
	
	// busca um registro pelo ID
	public AssocImagemSom getImagemById(long id)
	{
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_AIS, columns, 
				TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID + " = " + id, 
				null, null, null, null);
		
		cursor.moveToFirst();
		
		return new AssocImagemSom
		(
			cursor.getInt(0),
			cursor.getString(1), 
			cursor.getString(2),
			cursor.getString(3),
			cursor.getString(4), 
			cursor.getString(5).charAt(0), 
			cursor.getInt(6),
			(cursor.getInt(7) == 1) ? true : false
		 );		
	}
	
	public ArrayList<AssocImagemSom> getImagensByDesc(String desc)
	{
		ArrayList<AssocImagemSom> ais_list = new ArrayList<AssocImagemSom>();
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_AIS, columns, 
				TabletVoxSQLiteOpenHelper.AIS_COLUMN_DESC + " LIKE " + "'%" + desc + "%' AND " + TabletVoxSQLiteOpenHelper.AIS_COLUMN_CMD + " = 0", 
				null, null, null, null);
		
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			AssocImagemSom ais = new AssocImagemSom
			(
				cursor.getInt(0),
				cursor.getString(1), 
				cursor.getString(2),
				cursor.getString(3),
				cursor.getString(4), 
				cursor.getString(5).charAt(0), 
				cursor.getInt(6),
				(cursor.getInt(7) == 1) ? true : false
			 );
			ais_list.add(ais);
			cursor.moveToNext();
		}
		cursor.close();
		
		return ais_list;
	}
	
	public ArrayList<AssocImagemSom> getImagensByTituloImagem(String ti)
	{
		ArrayList<AssocImagemSom> ais_list = new ArrayList<AssocImagemSom>();
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_AIS, columns, 
				TabletVoxSQLiteOpenHelper.AIS_COLUMN_TITULO_IMAGEM + " LIKE " + "'%" + ti + "%'", 
				null, null, null, null);
		
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			AssocImagemSom ais = new AssocImagemSom
			(
				cursor.getInt(0),
				cursor.getString(1), 
				cursor.getString(2),
				cursor.getString(3),
				cursor.getString(4), 
				cursor.getString(5).charAt(0), 
				cursor.getInt(6),
				(cursor.getInt(7) == 1) ? true : false
			 );
			ais_list.add(ais);
			cursor.moveToNext();
		}
		cursor.close();
		
		return ais_list;
	}
	
	public ArrayList<AssocImagemSom> getImagensByTituloSom(String ts)
	{
		ArrayList<AssocImagemSom> ais_list = new ArrayList<AssocImagemSom>();
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_AIS, columns, 
				TabletVoxSQLiteOpenHelper.AIS_COLUMN_TITULO_SOM + " LIKE " + "'%" + ts + "%'", 
				null, null, null, null);
		
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			AssocImagemSom ais = new AssocImagemSom
			(
				cursor.getInt(0),
				cursor.getString(1), 
				cursor.getString(2),
				cursor.getString(3),
				cursor.getString(4), 
				cursor.getString(5).charAt(0), 
				cursor.getInt(6),
				(cursor.getInt(7) == 1) ? true : false
			 );
			ais_list.add(ais);
			cursor.moveToNext();
		}
		cursor.close();
		
		return ais_list;
	}
	
	public ArrayList<AssocImagemSom> getImagensByIdInterval(int inicio, int fim)
	{
		ArrayList<AssocImagemSom> ais_list = new ArrayList<AssocImagemSom>();
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_AIS, columns, 
				TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID + " BETWEEN " + 
				Integer.toString(inicio) + " AND " + Integer.toString(fim), 
				null, null, null, null);
		
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			AssocImagemSom ais = new AssocImagemSom
			(
				cursor.getInt(0),
				cursor.getString(1), 
				cursor.getString(2),
				cursor.getString(3),
				cursor.getString(4), 
				cursor.getString(5).charAt(0), 
				cursor.getInt(6),
				(cursor.getInt(7) == 1) ? true : false
			 );
			ais_list.add(ais);
			cursor.moveToNext();
		}
		cursor.close();		
		
		return ais_list;
	}
	
	// verifica se existem registros na tabela
	public boolean regs_exist()
	{
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_AIS, 
				columns, 
				null, null, null, null, null, "1");
		return (cursor.getCount() > 0);
	}
}
