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
		
		ArrayList<AssocImagemSom> imagens = cat.getImagens();
		// se houver itens incluir as imagens
		if ( !( (imagens == null) || (imagens.size() == 0) ) )
		{
			CategoriaAssocImagemSomDAO dao_cat_ais = new CategoriaAssocImagemSomDAO(sqliteOpenHelper);
			
			for (int i = 0, length = imagens.size(); i < length; i++)
				dao_cat_ais.create(cat, imagens.get(i));
		}
			
		
		database.insert(TabletVoxSQLiteOpenHelper.TABLE_CAT, null, values);		
	}
	
	public void create(long ais_id, String nome)
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
		
		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(sqliteOpenHelper);
		
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
				dao_ais.getAISById(cursor.getInt(1))
			 );
			cat_list.add(cat);
			cursor.moveToNext();
		}
		cursor.close();
		return cat_list;
	}

	// busca um registro pelo ID
	public Categoria getCategoriaById(long id)
	{
		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(sqliteOpenHelper);
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_CAT, columns, 
				TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID + " = " + id, 
				null, null, null, null);
		
		cursor.moveToFirst();
		
		return new Categoria
		(
			cursor.getInt(0),
			cursor.getString(2),
			dao_ais.getAISById(cursor.getInt(1))
		 );		
	}
	
	public ArrayList<Categoria> getCategoriasByNome(String nome)
	{
		ArrayList<Categoria> cat_list = new ArrayList<Categoria>();
		
		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(sqliteOpenHelper);
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_CAT, columns, 
				TabletVoxSQLiteOpenHelper.CAT_COLUMN_NOME + " LIKE " + "'%" + nome + "%'", 
				null, null, null, null);
		
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			Categoria cat = new Categoria
			(
				cursor.getInt(0),
				cursor.getString(2),
				dao_ais.getAISById(cursor.getInt(1))
			 );
			cat_list.add(cat);
			cursor.moveToNext();
		}
		cursor.close();
		
		return cat_list;		
	}
	
	public ArrayList<AssocImagemSom> getImagens(long categoriaId)
	{
		ArrayList<AssocImagemSom> ais_list = new ArrayList<AssocImagemSom>();
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_CAT_AIS, columns, 
				TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID + " = " + categoriaId, 
				null, null, null, null);
		
		AssocImagemSomDAO aisDao = new AssocImagemSomDAO(sqliteOpenHelper);
	
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			ais_list.add(aisDao.getAISById(cursor.getInt(2)));
			cursor.moveToNext();
		}
		
		cursor.close();
		
		return ais_list;		
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
