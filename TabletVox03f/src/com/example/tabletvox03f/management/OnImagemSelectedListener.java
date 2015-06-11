package com.example.tabletvox03f.management;

import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;

public interface OnImagemSelectedListener extends OnMyItemSelectedListener
{
	public boolean onDeleteItem(AssocImagemSom imagem);
	
	public boolean onDeleteItem(AssocImagemSom imagem, int num_encontrados);
	
	public void onEditItem(AssocImagemSom imagem);
}
