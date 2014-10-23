package com.example.tabletvox03f.dal.perfil;

import com.example.tabletvox03f.dal.categoria.Categoria;

public class PerfilCategoria
{
	private int id;
	private Perfil perfil;
	private Categoria categoria;
	
	public PerfilCategoria(int id, Perfil pfl, Categoria cat)
	{
		this.id 		= id;
		this.perfil 	= pfl;
		this.categoria 	= cat;
	}
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	
	public Perfil getPerfil()
	{
		return perfil;
	}
	public void setPerfil(Perfil perfil)
	{
		this.perfil = perfil;
	}
	
	public Categoria getCategoria()
	{
		return categoria;
	}
	public void setCategoria(Categoria categoria)
	{
		this.categoria = categoria;
	}

}
