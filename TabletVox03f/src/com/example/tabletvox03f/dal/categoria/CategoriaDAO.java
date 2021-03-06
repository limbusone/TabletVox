package com.example.tabletvox03f.dal.categoria;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.tabletvox03f.dal.TabletVoxSQLiteOpenHelper;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSomDAO;
import com.example.tabletvox03f.dal.perfil.PerfilCategoriaDAO;

public class CategoriaDAO
{
	private SQLiteDatabase database;
	private String[] columns = 
	{ 
		TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID,
		TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID,
		TabletVoxSQLiteOpenHelper.CAT_COLUMN_NOME,
		TabletVoxSQLiteOpenHelper.CAT_COLUMN_SIS
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
	
	public void create(Categoria cat)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID,  cat.getAIS().getId());
		values.put(TabletVoxSQLiteOpenHelper.CAT_COLUMN_NOME,  cat.getNome());
		
		
		cat.setId((int) database.insert(TabletVoxSQLiteOpenHelper.TABLE_CAT, null, values));
		ArrayList<AssocImagemSom> imagens = cat.getImagens();
		// se houver itens incluir as imagens
		if ( !( (imagens == null) || (imagens.size() == 0) ) )
		{
			CategoriaAssocImagemSomDAO dao_cat_ais = new CategoriaAssocImagemSomDAO(sqliteOpenHelper);
			dao_cat_ais.open();
			
			for (int i = 0, length = imagens.size(); i < length; i++)
				dao_cat_ais.create(cat, imagens.get(i));
		}		
	}
	
	public void create(long ais_id, String nome)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID,  (int) ais_id);
		values.put(TabletVoxSQLiteOpenHelper.CAT_COLUMN_NOME,  nome);
		database.insert(TabletVoxSQLiteOpenHelper.TABLE_CAT, null, values);		
	}
	
	public void create(long ais_id, String nome, ArrayList<AssocImagemSom> imagens)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID,  (int) ais_id);
		values.put(TabletVoxSQLiteOpenHelper.CAT_COLUMN_NOME,  nome);
		
		int cat_id = (int) database.insert(TabletVoxSQLiteOpenHelper.TABLE_CAT, null, values);
		// se houver itens incluir as imagens
		if ( !( (imagens == null) || (imagens.size() == 0) ) )
		{
			CategoriaAssocImagemSomDAO dao_cat_ais = new CategoriaAssocImagemSomDAO(sqliteOpenHelper);
			dao_cat_ais.open();
			
			for (int i = 0, length = imagens.size(); i < length; i++)
				dao_cat_ais.create(cat_id, imagens.get(i).getId());
		}		
	}
	
	public void create(long ais_id, String nome, ArrayList<AssocImagemSom> imagens, int pagina)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID,  (int) ais_id);
		values.put(TabletVoxSQLiteOpenHelper.CAT_COLUMN_NOME,  nome);
		
		int cat_id = (int) database.insert(TabletVoxSQLiteOpenHelper.TABLE_CAT, null, values);
		// se houver itens incluir as imagens
		if ( !( (imagens == null) || (imagens.size() == 0) ) )
		{
			CategoriaAssocImagemSomDAO dao_cat_ais = new CategoriaAssocImagemSomDAO(sqliteOpenHelper);
			dao_cat_ais.open();
			
			for (int i = 0, length = imagens.size(); i < length; i++)
				dao_cat_ais.create(cat_id, imagens.get(i).getId(), pagina, 0);
		}		
	}
	
	public ListaCategoria create(ListaCategoria categorias)
	{
		ListaCategoria retorno = new ListaCategoria();
		
		for (int i = 0, length = categorias.size(); i < length; i++)
		{
			Categoria cat = categorias.get(i);
			create(cat);
			retorno.add(cat);
		}
		return retorno;
	}

	public void delete(long id)
	{
		database.delete(TabletVoxSQLiteOpenHelper.TABLE_CAT, TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID + " = " + id, null);
		// se houver imagens vinculadas a essa categoria, deletar as associa��es
		CategoriaAssocImagemSomDAO cat_ais_dao = new CategoriaAssocImagemSomDAO(sqliteOpenHelper);
		cat_ais_dao.open();
		cat_ais_dao.delete(getImagens(id), id);
		// e se esta categoria for vinculada a um ou mais perfis, deleta as associa��es
		PerfilCategoriaDAO pfl_cat_dao = new PerfilCategoriaDAO(sqliteOpenHelper);
		pfl_cat_dao.open();
		pfl_cat_dao.delete_categoria(id);
		
	}
	
	public void delete(long[] id_list)
	{
		int i = 0, length = id_list.length;
		
		for (; i < length; i++)
			delete(id_list[i]);
	}	
	
	public void update(Categoria cat, long id)
	{
		ContentValues values = new ContentValues();
		values.put(TabletVoxSQLiteOpenHelper.AIS_COLUMN_ID,  cat.getAIS().getId());		
		values.put(TabletVoxSQLiteOpenHelper.CAT_COLUMN_NOME,  cat.getNome());
		
		CategoriaAssocImagemSomDAO dao_cat_ais = new CategoriaAssocImagemSomDAO(sqliteOpenHelper);
		dao_cat_ais.open();
		// verifica se h� algum item modificado
		ArrayList<AssocImagemSom> imagens_antigas 	= getImagens(id);
		ArrayList<AssocImagemSom> imagens_novas 	= cat.getImagens();
		boolean listaAlterada = false;
		if (imagens_antigas.size() == imagens_novas.size())
		{
			for (int i = 0, length = imagens_antigas.size(); i < length; i++)
			{
				if (!(imagens_antigas.get(i).getId() == imagens_novas.get(i).getId()))
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
			// deletar todas as imagens da categoria
			for (int i = 0, length = imagens_antigas.size(); i < length; i++)
				dao_cat_ais.delete(cat.getId(), imagens_antigas.get(i).getId());
			// incluir novas imagens da categoria
			for (int i = 0, length = imagens_novas.size(); i < length; i++)
				dao_cat_ais.create(cat, imagens_novas.get(i));
		}
		
		database.update(TabletVoxSQLiteOpenHelper.TABLE_CAT, values, TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID + " = " + id, null);
	}
	
	public void update(ListaCategoria categorias)
	{
		for (int i = 0, length = categorias.size(); i < length; i++)
			update(categorias.get(i), categorias.get(i).getId());
	}
	
	
	// retorna um arraylist com todos os registros
	public ListaCategoria getAll()
	{
		ListaCategoria cat_list = new ListaCategoria();
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_CAT, 
				columns, 
				null, null, null, null, null);
		
		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(sqliteOpenHelper);
		dao_ais.open();
		
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			Categoria cat = new Categoria
			(
				cursor.getInt(0),
				cursor.getString(2),
				dao_ais.getImagemById(cursor.getInt(1))
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
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_CAT, columns, 
				TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID + " = " + id, 
				null, null, null, null);

		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(sqliteOpenHelper);
		dao_ais.open();
		
		cursor.moveToFirst();
		
		return new Categoria
		(
			cursor.getInt(0),
			cursor.getString(2),
			dao_ais.getImagemById(cursor.getInt(1))
		 );		
	}
	
	public ListaCategoria getCategoriasByNome(String nome)
	{
		ListaCategoria cat_list = new ListaCategoria();
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_CAT, columns, 
				TabletVoxSQLiteOpenHelper.CAT_COLUMN_NOME + " LIKE " + "'%" + nome + "%'", 
				null, null, null, null);

		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(sqliteOpenHelper);
		dao_ais.open();
		
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			Categoria cat = new Categoria
			(
				cursor.getInt(0),
				cursor.getString(2),
				dao_ais.getImagemById(cursor.getInt(1))
			 );
			cat_list.add(cat);
			cursor.moveToNext();
		}
		cursor.close();
		
		return cat_list;
	}
	
	public ListaCategoria getCategoriasByIdInterval(int inicio, int fim)
	{
		ListaCategoria cat_list = new ListaCategoria();
		
		Cursor cursor = database.query(
				TabletVoxSQLiteOpenHelper.TABLE_CAT, columns, 
				TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID + " BETWEEN " + 
				Integer.toString(inicio) + " AND " + Integer.toString(fim), 
				null, null, null, null);

		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(sqliteOpenHelper);
		dao_ais.open();
		
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			Categoria cat = new Categoria
			(
				cursor.getInt(0),
				cursor.getString(2),
				dao_ais.getImagemById(cursor.getInt(1))
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
				TabletVoxSQLiteOpenHelper.TABLE_CAT_AIS, CategoriaAssocImagemSomDAO.columns, 
				TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID + " = " + categoriaId, 
				null, null, null, null);
		
		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(sqliteOpenHelper);
		dao_ais.open();
	
		cursor.moveToFirst();
		while (!(cursor.isAfterLast()))
		{
			AssocImagemSom imagem = dao_ais.getImagemById(cursor.getInt(2));
			imagem.setPagina(cursor.getInt(3));
			ais_list.add(imagem);
			cursor.moveToNext();
		}
		
		cursor.close();
		
		return ais_list;		
	}
	
	public ArrayList<AssocImagemSom> getImagens(long categoriaId, int pagina)
	{
		CategoriaAssocImagemSomDAO dao_cat_ais = new CategoriaAssocImagemSomDAO(sqliteOpenHelper);
		dao_cat_ais.open();
		
		return dao_cat_ais.getAISByCatIdAndPage(categoriaId, pagina);
	}
	
	public int getNumeroDePaginas(int categoriaId)
	{
		String sql = "SELECT " +
		"max(" + TabletVoxSQLiteOpenHelper.CAT_AIS_COLUMN_PAGE + ") " 	+
		"FROM " 														+ 
		TabletVoxSQLiteOpenHelper.TABLE_CAT_AIS 						+ 
		" WHERE " 														+ 
		TabletVoxSQLiteOpenHelper.CAT_COLUMN_ID + "=" + Integer.toString(categoriaId); 
		
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.getCount() > 0)
		{
			cursor.moveToFirst();
			return cursor.getInt(0);
		}
		return 0;
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
