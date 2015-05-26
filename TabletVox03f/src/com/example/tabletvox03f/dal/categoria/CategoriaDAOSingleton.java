package com.example.tabletvox03f.dal.categoria;


public class CategoriaDAOSingleton
{
	private static CategoriaDAOSingleton CategoriaDAOSingleton;
	
	private ListaCategoria listaCategoria = new ListaCategoria();
	
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
//		listaCategoria.add
//		(new Categoria
//			(
//				1, 
//				"animais", 
//				AssocImagemSomDAOSingleton.getInstance().getAssocImagemSomById(1)
//			)
//		);
//		
//		listaCategoria.add
//		(new Categoria
//			(
//				2, 
//				"pessoas", 
//				AssocImagemSomDAOSingleton.getInstance().getAssocImagemSomById(2)
//			)
//		);
//		
//		listaCategoria.add
//		(new Categoria
//			(
//				3, 
//				"banheiro", 
//				AssocImagemSomDAOSingleton.getInstance().getAssocImagemSomById(3)
//			)
//		);
//
//		listaCategoria.add
//		(new Categoria
//			(
//				4, 
//				"bebidas", 
//				new AssocImagemSom("bebidas", "BEBIDAS", "BEBIDAS", "jpg", 'c', 0)
//			)
//		);
		
	}
	
	public ListaCategoria getCategorias() 
	{
		return listaCategoria;
	}

	public void incluirCategoria(Categoria categoria)
	{
		listaCategoria.add(categoria);
	}
	
	public void incluirCategoriaWithRandomGeneratedID(Categoria categoria)
	{
		categoria.setId(gerarIdUnico());
		
		listaCategoria.add(categoria);
	}
	
	public ListaCategoria incluirCategoriasWithRandomGeneratedID(ListaCategoria categorias)
	{
		ListaCategoria retorno = new ListaCategoria();
		
		for (int i = 0, length = categorias.size(); i < length; i++)
		{
			Categoria categoria = categorias.get(i);
			categoria.setId(gerarIdUnico());
			listaCategoria.add(categoria);
			retorno.add(categoria);
		}
		
		return retorno;
	}
	
	public void editarCategoria(Categoria categoriaAntigo, Categoria categoriaNovo)
	{
		if (listaCategoria.indexOf(categoriaAntigo) >= 0)
		{
			categoriaAntigo.setNome(categoriaNovo.getNome());
			categoriaAntigo.setAIS(categoriaNovo.getAIS());
			categoriaAntigo.setImagens(categoriaNovo.getImagens());
		}
	}
	
	public void editarCategoria(Categoria categoriaNovo)
	{
		int idx = listaCategoria.indexOf(categoriaNovo);
		if (idx >= 0)
		{
			Categoria categoria = (Categoria) listaCategoria.get(idx);
			categoria.setNome(categoriaNovo.getNome());
			categoria.setAIS(categoriaNovo.getAIS());
			categoria.setImagens(categoriaNovo.getImagens());
		}
	}
	
	public void editarCategoria(Categoria categoriaNovo, int id)
	{
		Categoria categoria = getCategoriaById(id);
		categoria.setNome(categoriaNovo.getNome());
		categoria.setAIS(categoriaNovo.getAIS());
		categoria.setImagens(categoriaNovo.getImagens());
	}
	
	public void editarCategorias(ListaCategoria categoriasNovas)
	{
		for (int i = 0, length = categoriasNovas.size(); i < length; i++)
		{
			Categoria categoriaNova = categoriasNovas.get(i);
			Categoria categoria = getCategoriaById(categoriaNova.getId());
			categoria.setNome(categoriaNova.getNome());
			categoria.setAIS(categoriaNova.getAIS());
			categoria.setImagens(categoriaNova.getImagens());
		}
	}
	
	public void excluirCategoria(Categoria categoria)
	{
		listaCategoria.remove(categoria);
	}
	
	public void excluirCategoria(int id)
	{
		listaCategoria.remove(getCategoriaById(id));
	}
	
	public ListaCategoria getAll()
	{
		return (ListaCategoria) listaCategoria.clone();
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
	
	public ListaCategoria getCategoriasByNome(String nome)
	{
		Categoria categoria;
		ListaCategoria result = new ListaCategoria();
		for (int i = 0, length = listaCategoria.size(); i < length; i++)
		{
			categoria = listaCategoria.get(i);
			
			if (categoria.getNome().toLowerCase().contains(nome.toLowerCase()))
				result.add(new Categoria(categoria));
		}
		
		return result;		
	}
	
	public int gerarIdUnico()
	{
		int generated_id;
		// entra em loop até gerar um id único
		while ( ! ( getCategoriaById( generated_id = generateRandomInteger(1, 9999) ) == null ) );
		
		return generated_id;
	}
	
	private int generateRandomInteger(int min, int max)
	{
		return min + (int)(Math.random() * ((max - min) + 1));
	}
	
	
}
