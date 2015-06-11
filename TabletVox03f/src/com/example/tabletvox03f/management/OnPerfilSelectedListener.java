package com.example.tabletvox03f.management;

import com.example.tabletvox03f.dal.perfil.Perfil;

public interface OnPerfilSelectedListener extends OnMyItemSelectedListener
{
	public void onDeleteItem(Perfil perfil);
	
	public void onDeleteItem(Perfil perfil, int num_encontrados);
}
