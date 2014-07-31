package com.example.tabletvox03f.dal;

import java.util.ArrayList;

public class Categoria
{
	private int id;
	private String nome;
	private AssocImagemSom ais;
	private ArrayList<AssocImagemSom> imagens;
	private ArrayList<CategoriaAssocImagemSom> catImagens;
	
	public Categoria()
	{}
	
	public Categoria(int id, String nome)
	{
		this.id 	= id;
		this.nome 	= nome;
	}
	
	public Categoria(int id, AssocImagemSom ais, String nome)
	{
		this.id 	= id;
		this.ais 	= ais;
		this.nome 	= nome;
	}
	
	public Categoria(String nome)
	{
		this.nome = nome;
	}
	
	public Categoria(AssocImagemSom ais, String nome)
	{
		this.ais 	= ais;
		this.nome 	= nome;
	}
	

	public Categoria(AssocImagemSom ais, String nome, ArrayList<CategoriaAssocImagemSom> cat_ais_list)
	{
		this.ais 		= ais;
		this.nome 		= nome;
		this.catImagens = cat_ais_list;
	}	
	
	public Categoria(Categoria categoria)
	{
		this.id 		= categoria.getId();
		this.ais 		= categoria.getAIS();
		this.nome 		= categoria.getNome();
		this.imagens 	= categoria.getImagens();
		this.catImagens = categoria.getCatImagens();
	}
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getNome()
	{
		return nome;
	}
	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public ArrayList<AssocImagemSom> getImagens()
	{
		return imagens;
	}

	public void setImagens(ArrayList<AssocImagemSom> imagens)
	{
		this.imagens = imagens;
	}

	public AssocImagemSom getAIS()
	{
		return ais;
	}

	public void setAIS(AssocImagemSom ais)
	{
		this.ais = ais;
	}

	public ArrayList<CategoriaAssocImagemSom> getCatImagens()
	{
		return catImagens;
	}

	public void setCatImagens(ArrayList<CategoriaAssocImagemSom> catImagens)
	{
		this.catImagens = catImagens;
	}

	
	
}
