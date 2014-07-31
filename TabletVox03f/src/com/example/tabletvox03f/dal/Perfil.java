package com.example.tabletvox03f.dal;

import java.util.ArrayList;

public class Perfil
{
	private int id;
	private String nome;
	private String autor;
	private ArrayList<Categoria> categorias;
	
	public Perfil()
	{}
	
	public Perfil(int id, String nome, String autor)
	{
		this.id 	= id;
		this.nome 	= nome;
		this.autor 	= autor;
	}
	
	public Perfil(int id, String nome, String autor, ArrayList<Categoria> categorias)
	{
		this.id 	= id;
		this.nome 	= nome;
		this.autor 	= autor;
		this.categorias = categorias;
	}
	
	public Perfil(Perfil perfil)
	{
		this.id 		= perfil.getId();
		this.nome 		= perfil.getNome();
		this.autor 		= perfil.getAutor();
		this.categorias = perfil.getCategorias();
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
	
	public String getAutor()
	{
		return autor;
	}
	public void setAutor(String autor)
	{
		this.autor = autor;
	}

	public ArrayList<Categoria> getCategorias()
	{
		return categorias;
	}

	public void setCategorias(ArrayList<Categoria> categorias)
	{
		this.categorias = categorias;
	}

}
