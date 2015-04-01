package com.example.tabletvox03f.management;

import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;

public interface OnImagemSelectedListener
{
	public void onDeleteItem(int id);
	public void onDeleteItem(AssocImagemSom imagem);
}
