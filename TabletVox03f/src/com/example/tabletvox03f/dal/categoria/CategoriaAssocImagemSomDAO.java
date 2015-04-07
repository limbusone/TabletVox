package com.example.tabletvox03f.dal.categoria;

import java.util.ArrayList;

import com.example.tabletvox03f.dal.TabletVoxSQLiteOpenHelper;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSomDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CategoriaAssocImagemSomDAO
{
	private SQLiteDatabase database;
	public static String[] columns = 
	{ 
		TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_ID,
		TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID,
		TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID,
		TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_PAGE,
		TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_ORDEM
	};
	
	private TabletVoxSQLiteOpenHelper sqliteOpenHelper;
	
	public CategoriaAssocImagemSomDAO(Context c)
	{
		sqliteOpenHelper = new TabletVoxSQLiteOpenHelper(c);
	}
	
	public CategoriaAssocImagemSomDAO(TabletVoxSQLiteOpenHelper sqlOH)
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
	
	public void create(Categoria cat, AssocImagemSom ais)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID,  cat.getId());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID,  ais.getId());
		values.put(TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_PAGE, ais.getPagina());
		values.put(TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_ORDEM, ais.getOrdem());		
		database.insert(TabletVoxSQLiteOpenHelper.TABLE_CAT_AIS, null, values);		
	}
	
	public void create(Categoria cat, AssocImagemSom ais, int pagina, int ordem)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID,  cat.getId());
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID,  ais.getId());
		values.put(TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_PAGE, pagina);
		values.put(TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_ORDEM, ordem);
		database.insert(TabletVoxSQLiteOpenHelper.TABLE_CAT_AIS, null, values);		
	}
	
	public void create(long cat_id, long ais_id)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID,  cat_id);
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID,  ais_id);		
		database.insert(TabletVoxSQLiteOpenHelper.TABLE_CAT_AIS, null, values);		
	}
	
	public void create(long cat_id, long ais_id, int pagina, int ordem)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID,  cat_id);
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID,  ais_id);
		values.put(TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_PAGE, pagina);
		values.put(TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_ORDEM, ordem);
		database.insert(TabletVoxSQLiteOpenHelper.TABLE_CAT_AIS, null, values);
	}

	public void delete(long id)
	{
		database.delete(TabletVoxSQLiteOpenHelper.TABLE_CAT_AIS, TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_ID + " = " + id, null);
	}
	
	public void delete(long[] id_list)
	{
		int i = 0, length = id_list.length;
		
		for (; i < length; i++)
			database.delete(TabletVoxSQLiteOpenHelper.TABLE_CAT_AIS, TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_ID + " = " + id_list[i], null);
	}
	
	public void delete(int cat_id, int ais_id)
	{
		database.delete(TabletVoxSQLiteOpenHelper.TABLE_CAT_AIS, 
		TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID 	+ 	" = " 	+ cat_id + " AND " 
		+ TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID 	+ 	" = " 	+ ais_id, null);
		
	}
	
	public void delete_imagem(long ais_id)
	{
		database.delete(TabletVoxSQLiteOpenHelper.TABLE_CAT_AIS, TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID + " = " + ais_id, null);
	}
	
	public void delete(ArrayList<AssocImagemSom> imagens, long id)
	{
		for (int i = 0, length = imagens.size(); i < length; i++)
			delete((int) id, imagens.get(i).getId());
	}
	
	public void update(Categoria cat, AssocImagemSom ais, long id)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID,  cat.getId());		
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID,  ais.getId());
		values.put(TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_PAGE, ais.getPagina());
		values.put(TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_ORDEM, ais.getOrdem());
		database.update(TabletVoxSQLiteOpenHelper.TABLE_CAT_AIS, values, TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_ID + " = " + id, null);
	}
	
	public void update(Categoria cat, AssocImagemSom ais, long id, int pagina, int ordem)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID,  cat.getId());		
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID,  ais.getId());
		values.put(TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_PAGE, pagina);
		values.put(TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_ORDEM, ordem);
		database.update(TabletVoxSQLiteOpenHelper.TABLE_CAT_AIS, values, TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_ID + " = " + id, null);		
	}
	
	// retorna um arraylist com todos os registros
	public ArrayList<CategoriaAssocImagemSom> getAll()
	{
		ArrayList<CategoriaAssocImagemSom> cat_ais_list = new ArrayList<CategoriaAssocImagemSom>();
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_CAT_AIS, 
				columns, 
				null, null, null, null, null);
		
		AssocImagemSomDAO aisDao = new AssocImagemSomDAO(sqliteOpenHelper);
		CategoriaDAO catDao 	 = new CategoriaDAO(sqliteOpenHelper);		
		
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			CategoriaAssocImagemSom cat_ais = new CategoriaAssocImagemSom
			(
				cursor.getInt(0),
				catDao.getCategoriaById(cursor.getInt(1)),
				aisDao.getImagemById(cursor.getInt(2))
			);
			
			cat_ais_list.add(cat_ais);
			cursor.moveToNext();
		}
		cursor.close();
		return cat_ais_list;
	}
	
	// retorna um arraylist com registros pelo id da categoria
	public ArrayList<CategoriaAssocImagemSom> getCATAISByCatId(long id)
	{
		ArrayList<CategoriaAssocImagemSom> cat_ais_list = new ArrayList<CategoriaAssocImagemSom>();
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_CAT_AIS, columns, 
				TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID + " = " + id, 
				null, null, null, null);
		
		AssocImagemSomDAO aisDao = new AssocImagemSomDAO(sqliteOpenHelper);
		CategoriaDAO catDao 	 = new CategoriaDAO(sqliteOpenHelper);		
		
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			CategoriaAssocImagemSom cat_ais = new CategoriaAssocImagemSom
			(
				cursor.getInt(0),
				catDao.getCategoriaById(cursor.getInt(1)),
				aisDao.getImagemById(cursor.getInt(2))
			);
			
			cat_ais_list.add(cat_ais);
			cursor.moveToNext();
		}
		cursor.close();
		return cat_ais_list;
	}
	
	// retorna um arraylist de AssocImagemSom com registros pelo id da categoria e pela pagina
	public ArrayList<AssocImagemSom> getAISByCatIdAndPage(long id, int page)
	{
		ArrayList<AssocImagemSom> ais_list = new ArrayList<AssocImagemSom>();
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_CAT_AIS, columns, 
				TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID + " = " + id + " AND " + 
				TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_PAGE + " = " + page, 
				null, null, null, null);
		
		AssocImagemSomDAO aisDao = new AssocImagemSomDAO(sqliteOpenHelper);
		aisDao.open();
	
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			AssocImagemSom imagem = aisDao.getImagemById(cursor.getInt(2));
			imagem.setPagina(page);
			ais_list.add(imagem);
			cursor.moveToNext();
		}
		cursor.close();
		return ais_list;		
	}
	
	// retornar um arraylist de AssocImagemSom cuja categoria seja comandos
	public ArrayList<AssocImagemSom> getAISComandos(boolean soAtalho)
	{
		ArrayList<AssocImagemSom> ais_list = new ArrayList<AssocImagemSom>();
		
		int id_comando = 123; // id especifico que identifica a categoria que contem comandos
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_CAT_AIS, columns, 
				TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID + " = " + id_comando, 
				null, null, null, null);
		
		AssocImagemSomDAO aisDao = new AssocImagemSomDAO(sqliteOpenHelper);
	
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			AssocImagemSom ais = aisDao.getImagemById(cursor.getInt(2));
			if (soAtalho && ais.isAtalho())
				ais_list.add(ais);
			else if (!(soAtalho))
				ais_list.add(ais);
			cursor.moveToNext();
		}
		cursor.close();
		return ais_list;		
	}
	
	// busca um registro pelo ID
	public CategoriaAssocImagemSom getCATAISById(long id)
	{
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_CAT_AIS, columns, 
				TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_ID + " = " + id, 
				null, null, null, null);

		AssocImagemSomDAO aisDao = new AssocImagemSomDAO(sqliteOpenHelper);
		CategoriaDAO catDao 	 = new CategoriaDAO(sqliteOpenHelper);
		
		cursor.moveToFirst();
		
		return new CategoriaAssocImagemSom
		(
				cursor.getInt(0),
				catDao.getCategoriaById(cursor.getInt(1)),
				aisDao.getImagemById(cursor.getInt(2))
		);		
	}	
	
	// verifica se existem registros na tabela
	public boolean regs_exist()
	{
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_CAT_AIS, 
				columns, 
				null, null, null, null, null, "1");
		return (cursor.getCount() > 0);
	}
}
