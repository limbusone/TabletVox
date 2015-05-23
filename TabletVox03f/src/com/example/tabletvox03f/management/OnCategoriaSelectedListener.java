package com.example.tabletvox03f.management;

import com.example.tabletvox03f.dal.categoria.Categoria;

public interface OnCategoriaSelectedListener extends OnMyItemSelectedListener
{
	public void onDeleteItem(Categoria categoria);
}
