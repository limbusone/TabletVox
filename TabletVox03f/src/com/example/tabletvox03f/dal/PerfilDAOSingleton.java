package com.example.tabletvox03f.dal;

import java.util.ArrayList;

public class PerfilDAOSingleton
{
	private static PerfilDAOSingleton PerfilDAOSingleton;
	
	private ArrayList<Perfil> listaPerfil = new ArrayList<Perfil>(); 


	public PerfilDAOSingleton()
	{
		inicializar();
	}
	
	public static PerfilDAOSingleton getInstance() 
	{
		if(PerfilDAOSingleton == null) 
			PerfilDAOSingleton = new PerfilDAOSingleton();
		return PerfilDAOSingleton;
	}
	
	private void inicializar()
	{
		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		categorias.add(new Categoria(1, "animais", new AssocImagemSom("animais", 		"ANIMAIS", 		"ANIMAIS", 		"jpg", 'c', 0)));
		categorias.add(new Categoria(2, "pessoas", new AssocImagemSom("pessoas", 		"PESSOAS", 		"PESSOAS", 		"jpg", 'c', 0)));
		
		listaPerfil.add(new Perfil(1, "Perfil 1", "Autor 1", categorias));
		listaPerfil.add(new Perfil(2, "Perfil 2", "Autor 2"));
		listaPerfil.add(new Perfil(3, "Perfil 3", "Autor 3"));
	}
	
	public ArrayList<Perfil> getPerfis() 
	{
		return listaPerfil;
	}

	public void incluirPerfil(Perfil perfil)
	{
		listaPerfil.add(perfil);
	}
	
	public void incluirPerfilWithRandomGeneratedID(Perfil perfil)
	{
		int generated_id;
		// entra em loop até gerar um id único
		while ( ! ( getPerfilById( generated_id = generateRandomInteger(1, 9999) ) == null ) );
		
		perfil.setId(generated_id);
		
		listaPerfil.add(perfil);
	}
	
	public void editarPerfil(Perfil perfilAntigo, Perfil perfilNovo)
	{
		if (listaPerfil.indexOf(perfilAntigo) >= 0)
		{
			perfilAntigo.setNome(perfilNovo.getNome());
			perfilAntigo.setAutor(perfilNovo.getAutor());
		}
	}
	
	public void editarPerfil(Perfil perfilNovo)
	{
		int idx = listaPerfil.indexOf(perfilNovo);
		if (idx >= 0)
		{
			Perfil perfil = (Perfil) listaPerfil.get(idx);
			perfil.setNome(perfilNovo.getNome());
			perfil.setAutor(perfilNovo.getAutor());
		}
	}
	
	public void editarPerfil(Perfil perfilNovo, int id)
	{
		Perfil perfil = getPerfilById(id);
		perfil.setNome(perfilNovo.getNome());
		perfil.setAutor(perfilNovo.getAutor());
	}
	
	public void excluirPerfil(Perfil perfil)
	{
		listaPerfil.remove(perfil);
	}
	
	public void excluirPerfil(int id)
	{
		listaPerfil.remove(getPerfilById(id));
	}
	
	public Perfil getPerfilById(int id)
	{
		Perfil perfil = null;
		Perfil perfil_temp;
		for (int i = 0, length = listaPerfil.size(); i < length; i++)
		{
			perfil_temp = listaPerfil.get(i);
			if (perfil_temp.getId() == id)
			{
				perfil = perfil_temp;
				break;
			}

		}
		
		return perfil;
	}
	
	public ArrayList<Perfil> getPerfisByNomeOrAutor(String nome, String autor)
	{
		Perfil perfil;
		ArrayList<Perfil> result = new ArrayList<Perfil>();
		for (int i = 0, length = listaPerfil.size(); i < length; i++)
		{
			perfil = listaPerfil.get(i);
			
			if (perfil.getNome().toLowerCase().contains(nome.toLowerCase()) || 
				perfil.getAutor().toLowerCase().contains(autor.toLowerCase()))
				result.add(new Perfil(perfil));
		}
		
		return result;
	}
	
	private int generateRandomInteger(int min, int max)
	{
		return min + (int)(Math.random() * ((max - min) + 1));
	}
	
}
