package com.example.tabletvox03f.dal;

import java.util.ArrayList;

public class CategoriaDAOSingleton
{
	private static CategoriaDAOSingleton CategoriaDAOSingleton;
	
	private ArrayList<Categoria> listaCategoria = new ArrayList<Categoria>();
	
	public CategoriaDAOSingleton()
	{
		inicializar();
	}	
	
	public static CategoriaDAOSingleton getInstance() 
	{
		if(CategoriaDAOSingleton == null) 
			CategoriaDAOSingleton = new CategoriaDAOSingleton();
		return CategoriaDAOSingleton;
	}
	
	private void inicializar()
	{
//		listaCategoria.add(new Categoria(1, "Categoria 1", "Autor 1"));
//		listaCategoria.add(new Categoria(2, "Categoria 2", "Autor 2"));
//		listaCategoria.add(new Categoria(3, "Categoria 3", "Autor 3"));
	}
	
	public ArrayList<Categoria> getCategorias() 
	{
		return listaCategoria;
	}

	public void incluirCategoria(Categoria categoria)
	{
		listaCategoria.add(categoria);
	}
	
	public void incluirCategoriaWithRandomGeneratedID(Categoria categoria)
	{
		int generated_id;
		// entra em loop at� gerar um id �nico
		while ( ! ( getCategoriaById( generated_id = generateRandomInteger(1, 9999) ) == null ) );
		
		categoria.setId(generated_id);
		
		listaCategoria.add(categoria);
	}
	
	public void editarCategoria(Categoria categoriaAntigo, Categoria categoriaNovo)
	{
		if (listaCategoria.indexOf(categoriaAntigo) >= 0)
		{
			categoriaAntigo.setNome(categoriaNovo.getNome());
//			categoriaAntigo.setAutor(categoriaNovo.getAutor());
		}
	}
	
	public void editarCategoria(Categoria categoriaNovo)
	{
		int idx = listaCategoria.indexOf(categoriaNovo);
		if (idx >= 0)
		{
			Categoria categoria = (Categoria) listaCategoria.get(idx);
			categoria.setNome(categoriaNovo.getNome());
//			categoria.setAutor(categoriaNovo.getAutor());
		}
	}
	
	public void editarCategoria(Categoria categoriaNovo, int id)
	{
		Categoria categoria = getCategoriaById(id);
		categoria.setNome(categoriaNovo.getNome());
//		categoria.setAutor(categoriaNovo.getAutor());
	}
	
	public void excluirCategoria(Categoria categoria)
	{
		listaCategoria.remove(categoria);
	}
	
	public void excluirCategoria(int id)
	{
		listaCategoria.remove(getCategoriaById(id));
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Categoria> getAll()
	{
		return (ArrayList<Categoria>) listaCategoria.clone();
	}
	
	public Categoria getCategoriaById(int id)
	{
		Categoria categoria = null;
		Categoria categoria_temp;
		for (int i = 0, length = listaCategoria.size(); i < length; i++)
		{
			categoria_temp = listaCategoria.get(i);
			if (categoria_temp.getId() == id)
			{
				categoria = categoria_temp;
				break;
			}

		}
		
		return categoria;
	}
	
	private int generateRandomInteger(int min, int max)
	{
		return min + (int)(Math.random() * ((max - min) + 1));
	}
	
	
}