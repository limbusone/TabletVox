package com.example.tabletvox03f.dal.perfil;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.tabletvox03f.dal.TabletVoxSQLiteOpenHelper;
import com.example.tabletvox03f.dal.categoria.Categoria;
import com.example.tabletvox03f.dal.categoria.CategoriaDAO;

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
		
		pfl.setId((int) database.insert(TabletVoxSQLiteOpenHelper.TABLE_PFL, null, values));
		ArrayList<Categoria> categorias = pfl.getCategorias();
		// se houver itens incluir as categorias
		if ( !( (categorias == null) || (categorias.size() == 0) ) )
		{
			PerfilCategoriaDAO dao_pfl_cat = new PerfilCategoriaDAO(sqliteOpenHelper);
			dao_pfl_cat.open();
			
			for (int i = 0, length = categorias.size(); i < length; i++)
				dao_pfl_cat.create(pfl, categorias.get(i));
		}
	}
	
	public void create(String nome, String autor)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.PFL_COLUMN_NOME,  nome);
		values.put(TabletVoxSQLiteOpenHelper.PFL_COLUMN_AUTOR, autor);
		database.insert(TabletVoxSQLiteOpenHelper.TABLE_PFL, null, values);		
	}
	
	public void create(String nome, String autor, ArrayList<Categoria> categorias)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.PFL_COLUMN_NOME,  nome);
		values.put(TabletVoxSQLiteOpenHelper.PFL_COLUMN_AUTOR, autor);
		database.insert(TabletVoxSQLiteOpenHelper.TABLE_PFL, null, values);	
		
		int pfl_id = (int) database.insert(TabletVoxSQLiteOpenHelper.TABLE_PFL, null, values);
		// se houver itens incluir as categorias
		if ( !( (categorias == null) || (categorias.size() == 0) ) )
		{
			PerfilCategoriaDAO dao_pfl_cat = new PerfilCategoriaDAO(sqliteOpenHelper);
			dao_pfl_cat.open();
			
			for (int i = 0, length = categorias.size(); i < length; i++)
				dao_pfl_cat.create(pfl_id, categorias.get(i).getId());
		}		
	}
	
	public void delete(long id)
	{
		database.delete(TabletVoxSQLiteOpenHelper.TABLE_PFL, TabletVoxSQLiteOpenHelper.PFL_COLUMN_ID + " = " + id, null);
		// se houver categorias vinculadas a esse perfil, deletar as associações
		PerfilCategoriaDAO pfl_cat_dao = new PerfilCategoriaDAO(sqliteOpenHelper);
		pfl_cat_dao.open();
		pfl_cat_dao.delete(getCategorias(id), id);		
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
		
		PerfilCategoriaDAO dao_pfl_cat = new PerfilCategoriaDAO(sqliteOpenHelper);
		dao_pfl_cat.open();
		// verifica se há algum item modificado
		ArrayList<Categoria> categorias_antigas 	= getCategorias(id);
		ArrayList<Categoria> categorias_novas 		= pfl.getCategorias();
		boolean listaAlterada = false;
		if (categorias_antigas.size() == categorias_novas.size())
		{
			for (int i = 0, length = categorias_antigas.size(); i < length; i++)
			{
				if (!(categorias_antigas.get(i).getId() == categorias_novas.get(i).getId()))
				{
					listaAlterada = true;
					break;
				}
			}
		}
		else
			listaAlterada = true;
		
		if (listaAlterada)
		{
			// deletar todas as categorias do perfil
			for (int i = 0, length = categorias_antigas.size(); i < length; i++)
				dao_pfl_cat.delete(pfl.getId(), categorias_antigas.get(i).getId());
			// incluir novas categorias do perfil
			for (int i = 0, length = categorias_novas.size(); i < length; i++)
				dao_pfl_cat.create(pfl, categorias_novas.get(i));
		}
		
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
	public Perfil getPerfilById(long id)
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
	
	public ArrayList<Perfil> getPerfisByNomeOrAutor(String nome, String autor)
	{
		Perfil perfil;
		ArrayList<Perfil> result = new ArrayList<Perfil>();

		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_PFL, columns, 
				TabletVoxSQLiteOpenHelper.PFL_COLUMN_NOME + " LIKE " + "'%" + nome + "%' OR "
				+ TabletVoxSQLiteOpenHelper.PFL_COLUMN_AUTOR + " LIKE " + "'%" + autor + "%'", 
				null, null, null, null);
		
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			perfil = new Perfil
			(
				cursor.getInt(0),
				cursor.getString(1), 
				cursor.getString(2)
			 );
			result.add(perfil);
			cursor.moveToNext();			
		}
		
		return result;
	}	
	
	public ArrayList<Categoria> getCategorias(long perfilId)
	{
		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_PFL_CAT, PerfilCategoriaDAO.columns, 
				TabletVoxSQLiteOpenHelper.PFL_COLUMN_ID + " = " + perfilId, 
				null, null, null, null);
		
		CategoriaDAO dao_cat = new CategoriaDAO(sqliteOpenHelper);
		dao_cat.open();
	
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			categorias.add(dao_cat.getCategoriaById(cursor.getInt(2)));
			cursor.moveToNext();
		}
		
		cursor.close();
		
		return categorias;		
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
