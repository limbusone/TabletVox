package com.example.tabletvox03f.management;

import com.example.tabletvox03f.dal.categoria.Categoria;

public interface OnCategoriaSelectedListener extends OnMyItemSelectedListener
{
	public boolean onDeleteItem(Categoria categoria);
	
	public boolean onDeleteItem(Categoria categoria, int num_encontrados);
	
	public void onEditItem(Categoria categoria);
}
