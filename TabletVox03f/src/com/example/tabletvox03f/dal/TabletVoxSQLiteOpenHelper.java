package com.example.tabletvox03f.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TabletVoxSQLiteOpenHelper extends SQLiteOpenHelper
{
	// nome da tabela AssocImagemSom
	public static final String TABLE_AIS = "AssocImagemSom";
	// atributos de AssocImagemSom
	public static final String AIS_COLUMN_ID 			= "ais_id";
	public static final String AIS_COLUMN_DESC			= "ais_desc";
	public static final String AIS_COLUMN_TITULO_IMAGEM = "ais_titulo_imagem";
	public static final String AIS_COLUMN_TITULO_SOM 	= "ais_titulo_som";
	public static final String AIS_COLUMN_EXT 			= "ais_ext";
	public static final String AIS_COLUMN_TIPO 			= "ais_tipo";
	public static final String AIS_COLUMN_CMD 			= "ais_cmd";
	public static final String AIS_COLUMN_ATALHO		= "ais_atalho";
	
	// nome da tabela Perfil
	public static final String TABLE_PFL	= "Perfil";
	// atributos de Perfil
	public static final String PFL_COLUMN_ID	= "pfl_id";
	public static final String PFL_COLUMN_NOME	= "pfl_nome";
	public static final String PFL_COLUMN_AUTOR = "pfl_autor";
	
	// nome da tabela Categoria
	public static final String TABLE_CAT = "Categoria";
	// atributos de Categoria
	public static final String CAT_COLUMN_ID	= "cat_id";
	public static final String CAT_COLUMN_NOME	= "cat_nome";
	
	// nome da tabela PerfilCategoria
	public static final String TABLE_PFL_CAT = "PerfilCategoria";
	// atributos de PerfilCategoria
	public static final String PFL_CAT_COLUMN_ID = "pfl_cat_id";

	// nome da tabela CategoriaAssocImagemSom
	public static final String TABLE_CAT_AIS = "CategoriaAssocImagemSom";
	// atributos de CategoriaAssocImagemSom
	public static final String CAT_AIS_COLUMN_ID 	= "cat_ais_id";
	public static final String CAT_AIS_COLUMN_PAGE 	= "cat_ais_page";
	
	private static final String DATABASE_NAME = "tabletvox.db"; 	// nome do arquivo do banco
	private static final int	DATABASE_VERSION = 1;
	
	// comando para criar tabela
	private static final String DATABASE_CREATE = 
		"CREATE TABLE " + TABLE_AIS 
		+ "("
		+ AIS_COLUMN_ID 			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ AIS_COLUMN_DESC			+ " VARCHAR(40), "
		+ AIS_COLUMN_TITULO_IMAGEM 	+ " VARCHAR(20), "
		+ AIS_COLUMN_TITULO_SOM 	+ " VARCHAR(20), "
		+ AIS_COLUMN_EXT 			+ " VARCHAR(3), "
		+ AIS_COLUMN_TIPO 			+ " CHAR(1), "
		+ AIS_COLUMN_CMD 			+ " INTEGER, "
		+ AIS_COLUMN_ATALHO			+ " BOOLEAN"
		+ ");"
		+ "CREATE TABLE " + TABLE_PFL
		+ "("
		+ PFL_COLUMN_ID 	+ " INTEGER P RIMARY KEY AUTOINCREMENT, "
		+ PFL_COLUMN_NOME	+ " VARCHAR(40), "
		+ PFL_COLUMN_AUTOR	+ " VARCHAR(40)"
		+ ");"
		+ "CREATE TABLE " + TABLE_CAT
		+ "("
		+ CAT_COLUMN_ID 	+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ AIS_COLUMN_ID		+ " INTEGER, "
		+ CAT_COLUMN_NOME 	+ " VARCHAR(40)"
		+");"
		+ "CREATE TABLE " + TABLE_PFL_CAT
		+ "("
		+ PFL_CAT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ PFL_COLUMN_ID 	+ " INTEGER, "
		+ CAT_COLUMN_ID 	+ " INTEGER"
		+ ");"
		+ "CREATE TABLE " + TABLE_CAT_AIS
		+ "("
		+ CAT_AIS_COLUMN_ID 	+ " INTEGER PRIMARY KEY AUTOINCREMENT, "		
		+ CAT_COLUMN_ID 		+ " INTEGER, "
		+ AIS_COLUMN_ID 		+ " INTEGER, "
		+ CAT_AIS_COLUMN_PAGE 	+ " INTEGER"
		+ ");";

	public TabletVoxSQLiteOpenHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// Auto-generated constructor stub
	}

	// método acionado se o banco não existir
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// Auto-generated method stub
		db.execSQL(DATABASE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_AIS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PFL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PFL_CAT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAT_AIS);
		onCreate(db);
	}
	
}
