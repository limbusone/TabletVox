package com.example.tabletvox03f.dal;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PerfilCategoriaDAO
{
	private SQLiteDatabase database;
	private String[] columns = 
	{
		TabletVoxSQLiteOpenHelper.PFL_CAT_COLUMN_ID,
		TabletVoxSQLiteOpenHelper.PFL_COLUMN_ID,
		TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID
	};
	
	private TabletVoxSQLiteOpenHelper sqliteOpenHelper;
	
	public PerfilCategoriaDAO(Context c)
	{
		sqliteOpenHelper = new TabletVoxSQLiteOpenHelper(c);
	}
	
	public PerfilCategoriaDAO(TabletVoxSQLiteOpenHelper sqlOH)
	{
		sqliteOpenHelper = sqlOH;
	}
	
	// abre a conex�o
	public void open() throws SQLException
	{
		database = sqliteOpenHelper.getWritableDatabase();
	}

	// fecha a conex�o
	public void close()
	{
		sqliteOpenHelper.close();
	}
	
	public void create(Perfil pfl, Categoria cat)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.PFL_COLUMN_ID,  pfl.getId());
		values.put(TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID,  cat.getId());
		database.insert(TabletVoxSQLiteOpenHelper.TABLE_PFL_CAT, null, values);		
	}
	
	public void create(long pfl_id, long cat_id)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.PFL_COLUMN_ID,  pfl_id);
		values.put(TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID,  cat_id);
		database.insert(TabletVoxSQLiteOpenHelper.TABLE_PFL_CAT, null, values);		
	}

	public void delete(long id)
	{
		database.delete(TabletVoxSQLiteOpenHelper.TABLE_PFL_CAT, TabletVoxSQLiteOpenHelper.PFL_CAT_COLUMN_ID + " = " + id, null);
	}
	
	public void delete(long[] id_list)
	{
		int i = 0, length = id_list.length;
		
		for (; i < length; i++)
			database.delete(TabletVoxSQLiteOpenHelper.TABLE_PFL, TabletVoxSQLiteOpenHelper.PFL_CAT_COLUMN_ID + " = " + id_list[i], null);
	}	
	
	public void update(Perfil pfl, Categoria cat, long id)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.PFL_COLUMN_ID,  pfl.getId());
		values.put(TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID,  cat.getId());
		database.update(TabletVoxSQLiteOpenHelper.TABLE_PFL, values, TabletVoxSQLiteOpenHelper.PFL_CAT_COLUMN_ID + " = " + id, null);
	}
	
	// retorna um arraylist com todos os registros
	public ArrayList<PerfilCategoria> getAll()
	{
		ArrayList<PerfilCategoria> pfl_cat_list = new ArrayList<PerfilCategoria>();
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_PFL_CAT, 
				columns, 
				null, null, null, null, null);
		
		PerfilDAO pflDao 	= new PerfilDAO(sqliteOpenHelper);
		CategoriaDAO catDao = new CategoriaDAO(sqliteOpenHelper);		
		
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			PerfilCategoria pfl_cat = new PerfilCategoria
			(
				cursor.getInt(0),
				pflDao.getPFLById(cursor.getInt(1)),
				catDao.getCategoriaById(cursor.getInt(2))
			);
			
			pfl_cat_list.add(pfl_cat);
			cursor.moveToNext();
		}
		cursor.close();
		return pfl_cat_list;
	}
	
	// retorna um arraylist com registros pelo id do perfil
	public ArrayList<PerfilCategoria> getPFLCATListByPerfilId(long id)
	{
		ArrayList<PerfilCategoria> pfl_cat_list = new ArrayList<PerfilCategoria>();
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_PFL_CAT, columns, 
				TabletVoxSQLiteOpenHelper.PFL_COLUMN_ID + " = " + id, 
				null, null, null, null);
		
		PerfilDAO pflDao 	= new PerfilDAO(sqliteOpenHelper);
		CategoriaDAO catDao = new CategoriaDAO(sqliteOpenHelper);		
		
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			PerfilCategoria pfl_cat = new PerfilCategoria
			(
				cursor.getInt(0),
				pflDao.getPFLById(cursor.getInt(1)),
				catDao.getCategoriaById(cursor.getInt(2))
			);
			
			pfl_cat_list.add(pfl_cat);
			cursor.moveToNext();
		}
		cursor.close();
		return pfl_cat_list;
	}
	
	// retorna um arraylist de Categorias pelo id do perfil
	public ArrayList<Categoria> getCategoriasByPerfilId(long id)
	{
		ArrayList<Categoria> cat_list = new ArrayList<Categoria>();
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_PFL_CAT, columns, 
				TabletVoxSQLiteOpenHelper.PFL_COLUMN_ID + " = " + id, 
				null, null, null, null);
		
		CategoriaDAO catDao = new CategoriaDAO(sqliteOpenHelper);		
		
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			cat_list.add(catDao.getCategoriaById(cursor.getInt(2)));
			cursor.moveToNext();
		}
		cursor.close();
		return cat_list;		
	}

	// busca um registro pelo ID
	public PerfilCategoria getPFLCATById(long id)
	{
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_PFL_CAT, columns, 
				TabletVoxSQLiteOpenHelper.PFL_CAT_COLUMN_ID + " = " + id, 
				null, null, null, null);

		PerfilDAO pflDao 	= new PerfilDAO(sqliteOpenHelper);
		CategoriaDAO catDao = new CategoriaDAO(sqliteOpenHelper);
		
		cursor.moveToFirst();
		
		return new PerfilCategoria
		(
			cursor.getInt(0),
			pflDao.getPFLById(cursor.getInt(1)),
			catDao.getCategoriaById(cursor.getInt(2))
		);		
	}
	
	
	// verifica se existem registros na tabela
	public boolean regs_exist()
	{
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_PFL, 
				columns, 
				null, null, null, null, null, "1");
		return (cursor.getCount() > 0);
	}	

}
