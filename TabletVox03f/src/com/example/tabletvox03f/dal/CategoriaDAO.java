package com.example.tabletvox03f.dal;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CategoriaDAO
{
	private SQLiteDatabase database;
	private String[] columns = 
	{ 
		TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID,
		TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID,
		TabletVoxSQLiteOpenHelper.CAT_COLUMN_NOME
	};
	
	private TabletVoxSQLiteOpenHelper sqliteOpenHelper;
	
	public CategoriaDAO(Context c)
	{
		sqliteOpenHelper = new TabletVoxSQLiteOpenHelper(c);
	}
	
	public CategoriaDAO(TabletVoxSQLiteOpenHelper sqlOH)
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
	
	public void create(Categoria cat)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID,  cat.getAIS().getId());
		values.put(TabletVoxSQLiteOpenHelper.CAT_COLUMN_NOME,  cat.getNome());
		database.insert(TabletVoxSQLiteOpenHelper.TABLE_CAT, null, values);		
	}
	
	public void create(long ais_id, String nome, String autor)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID,  ais_id);
		values.put(TabletVoxSQLiteOpenHelper.CAT_COLUMN_NOME,  nome);
		database.insert(TabletVoxSQLiteOpenHelper.TABLE_CAT, null, values);		
	}

	public void delete(long id)
	{
		database.delete(TabletVoxSQLiteOpenHelper.TABLE_CAT, TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID + " = " + id, null);
	}
	
	public void delete(long[] id_list)
	{
		int i = 0, length = id_list.length;
		
		for (; i < length; i++)
			database.delete(TabletVoxSQLiteOpenHelper.TABLE_CAT, TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID + " = " + id_list[i], null);
	}	
	
	public void update(Categoria cat, long id)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID,  cat.getAIS().getId());		
		values.put(TabletVoxSQLiteOpenHelper.CAT_COLUMN_NOME,  cat.getNome());
		database.update(TabletVoxSQLiteOpenHelper.TABLE_CAT, values, TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID + " = " + id, null);
	}
	
	// retorna um arraylist com todos os registros
	public ArrayList<Categoria> getAll()
	{
		ArrayList<Categoria> cat_list = new ArrayList<Categoria>();
		
		AssocImagemSomDAO ais_dao = new AssocImagemSomDAO(sqliteOpenHelper);
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_CAT, 
				columns, 
				null, null, null, null, null);
		
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			Categoria cat = new Categoria
			(
				cursor.getInt(0),
				cursor.getString(2),
				ais_dao.getAISById(cursor.getInt(1))
			 );
			cat_list.add(cat);
			cursor.moveToNext();
		}
		cursor.close();
		return cat_list;
	}

	// busca um registro pelo ID
	public Categoria getCatById(long id)
	{
		AssocImagemSomDAO ais_dao = new AssocImagemSomDAO(sqliteOpenHelper);
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_CAT, columns, 
				TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID + " = " + id, 
				null, null, null, null);
		
		cursor.moveToFirst();
		
		return new Categoria
		(
			cursor.getInt(0),
			cursor.getString(2),
			ais_dao.getAISById(cursor.getInt(1))
		 );		
	}	
	
	// verifica se existem registros na tabela
	public boolean regs_exist()
	{
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_CAT, 
				columns, 
				null, null, null, null, null, "1");
		return (cursor.getCount() > 0);
	}	

}
