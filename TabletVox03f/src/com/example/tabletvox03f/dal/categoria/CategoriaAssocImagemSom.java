package com.example.tabletvox03f.dal.categoria;

import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;

public class CategoriaAssocImagemSom
{
	private int id;
	private Categoria categoria;
	private AssocImagemSom ais;
	private int page;
	private int ordem;
	
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

	public int getOrdem()
	{
		return ordem;
	}

	public void setOrdem(int ordem)
	{
		this.ordem = ordem;
	}

	
}
