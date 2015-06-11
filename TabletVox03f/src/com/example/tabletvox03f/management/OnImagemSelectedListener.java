package com.example.tabletvox03f.management;

import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;

public interface OnImagemSelectedListener extends OnMyItemSelectedListener
{
	public void onDeleteItem(AssocImagemSom imagem);
	
	public void onDeleteItem(AssocImagemSom imagem, int num_encontrados);
}
