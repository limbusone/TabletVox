package com.example.tabletvox03f.dal;

public class CategoriaAssocImagemSom
{
	private int id;
	private Categoria categoria;
	private AssocImagemSom ais;
	private int page;
	
	public CategoriaAssocImagemSom(int id, Categoria cat, AssocImagemSom ais)
	{
		this.id = id;
		this.categoria = cat;
		this.ais = ais;
	}
	
	public CategoriaAssocImagemSom(Categoria categoria, AssocImagemSom ais, int page)
	{
		this.categoria  = categoria;
		this.ais		= ais;
		this.page 		= page;
	}
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	
	public Categoria getCategoria()
	{
		return categoria;
	}
	public void setCategoria(Categoria categoria)
	{
		this.categoria = categoria;
	}
	
	public AssocImagemSom getAis()
	{
		return ais;
	}
	public void setAis(AssocImagemSom ais)
	{
		this.ais = ais;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	
}
