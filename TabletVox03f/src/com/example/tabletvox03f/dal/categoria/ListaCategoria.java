package com.example.tabletvox03f.dal.categoria;

import java.util.ArrayList;

public class ListaCategoria extends ArrayList<Categoria>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Categoria getCategoriaById(int id)
	{
		Categoria categoria = null;
		Categoria categoria_temp;
		for (int i = 0, length = this.size(); i < length; i++)
		{
			categoria_temp = this.get(i);
			if (categoria_temp.getId() == id)
			{
				categoria = categoria_temp;
				break;
			}

		}
		
		return categoria;
	}
	
	public int getNumeroDePaginas()
	{
		if (this.isEmpty())
			return 1;
		else
		{
			int maxPage = this.get(0).getPagina();
			for (int i = 1, length = this.size(); i < length; i++)
			{
				Categoria categoria = this.get(i);
				if (categoria.getPagina() > maxPage)
					maxPage = categoria.getPagina();
			}
			
			return maxPage;
		}
	}

}
