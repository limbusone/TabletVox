package com.example.tabletvox03f.dal.perfil;

import java.util.ArrayList;

import com.example.tabletvox03f.dal.TabletVoxSQLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PerfilDAO
{
	private SQLiteDatabase database;
	private String[] columns = 
	{ 
		TabletVoxSQLiteOpenHelper.PFL_COLUMN_ID,
		TabletVoxSQLiteOpenHelper.PFL_COLUMN_NOME,
		TabletVoxSQLiteOpenHelper.PFL_COLUMN_AUTOR
	};
	
	private TabletVoxSQLiteOpenHelper sqliteOpenHelper;
	
	public PerfilDAO(Context c)
	{
		sqliteOpenHelper = new TabletVoxSQLiteOpenHelper(c);
	}
	
	public PerfilDAO(TabletVoxSQLiteOpenHelper sqlOH)
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
	
	public void create(Perfil pfl)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.PFL_COLUMN_NOME,  pfl.getNome());
		values.put(TabletVoxSQLiteOpenHelper.PFL_COLUMN_AUTOR, pfl.getAutor());
		database.insert(TabletVoxSQLiteOpenHelper.TABLE_PFL, null, values);		
	}
	
	public void create(String nome, String autor)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.PFL_COLUMN_NOME,  nome);
		values.put(TabletVoxSQLiteOpenHelper.PFL_COLUMN_AUTOR, autor);
		database.insert(TabletVoxSQLiteOpenHelper.TABLE_PFL, null, values);		
	}

	public void delete(long id)
	{
		database.delete(TabletVoxSQLiteOpenHelper.TABLE_PFL, TabletVoxSQLiteOpenHelper.PFL_COLUMN_ID + " = " + id, null);
	}
	
	public void delete(long[] id_list)
	{
		int i = 0, length = id_list.length;
		
		for (; i < length; i++)
			database.delete(TabletVoxSQLiteOpenHelper.TABLE_PFL, TabletVoxSQLiteOpenHelper.PFL_COLUMN_ID + " = " + id_list[i], null);
	}	
	
	public void update(Perfil pfl, long id)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.PFL_COLUMN_NOME,  pfl.getNome());
		values.put(TabletVoxSQLiteOpenHelper.PFL_COLUMN_AUTOR, pfl.getAutor());
		database.update(TabletVoxSQLiteOpenHelper.TABLE_PFL, values, TabletVoxSQLiteOpenHelper.PFL_COLUMN_ID + " = " + id, null);
	}
	
	// retorna um arraylist com todos os registros
	public ArrayList<Perfil> getAll()
	{
		ArrayList<Perfil> pfl_list = new ArrayList<Perfil>();
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_PFL, 
				columns, 
				null, null, null, null, null);
		
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			Perfil pfl = new Perfil
			(
				cursor.getInt(0),
				cursor.getString(1), 
				cursor.getString(2)
			 );
			pfl_list.add(pfl);
			cursor.moveToNext();
		}
		cursor.close();
		return pfl_list;
	}

	// busca um registro pelo ID
	public Perfil getPFLById(long id)
	{
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_PFL, columns, 
				TabletVoxSQLiteOpenHelper.PFL_COLUMN_ID + " = " + id, 
				null, null, null, null);
		
		cursor.moveToFirst();
		
		return new Perfil
		(
			cursor.getInt(0),
			cursor.getString(1), 
			cursor.getString(2)
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
