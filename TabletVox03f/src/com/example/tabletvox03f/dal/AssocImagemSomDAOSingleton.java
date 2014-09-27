package com.example.tabletvox03f.dal;

import java.util.ArrayList;

public class AssocImagemSomDAOSingleton
{
	private static AssocImagemSomDAOSingleton AssocImagemSomDAOSingleton;
	
	private ArrayList<AssocImagemSom> listaAssocImagemSom = new ArrayList<AssocImagemSom>();
	
	public AssocImagemSomDAOSingleton()
	{
		inicializar();
	}	
	
	public static AssocImagemSomDAOSingleton getInstance() 
	{
		if(AssocImagemSomDAOSingleton == null) 
			AssocImagemSomDAOSingleton = new AssocImagemSomDAOSingleton();
		return AssocImagemSomDAOSingleton;
	}
	
	private void inicializar()
	{
//		listaAssocImagemSom.add(new AssocImagemSom(1, "AssocImagemSom 1", "Autor 1"));
//		listaAssocImagemSom.add(new AssocImagemSom(2, "AssocImagemSom 2", "Autor 2"));
//		listaAssocImagemSom.add(new AssocImagemSom(3, "AssocImagemSom 3", "Autor 3"));
	}
	
	public ArrayList<AssocImagemSom> getAssocImagemSoms() 
	{
		return listaAssocImagemSom;
	}

	public void incluirAssocImagemSom(AssocImagemSom ais)
	{
		listaAssocImagemSom.add(ais);
	}
	
	public AssocImagemSom incluirAssocImagemSomWithRandomGeneratedID(AssocImagemSom ais)
	{
		int generated_id;
		// entra em loop até gerar um id único
		while ( ! ( getAssocImagemSomById( generated_id = generateRandomInteger(1, 9999) ) == null ) );
		
		ais.setId(generated_id);
		
		listaAssocImagemSom.add(ais);
		
		return ais;
	}
	
	public void editarAssocImagemSom(AssocImagemSom aisAntigo, AssocImagemSom aisNovo)
	{
		if (listaAssocImagemSom.indexOf(aisAntigo) >= 0)
		{
			aisAntigo.setDesc(aisNovo.getDesc());
			aisAntigo.setTituloImagem(aisNovo.getTituloImagem());
			aisAntigo.setTituloSom(aisNovo.getTituloSom());
			aisAntigo.setExt(aisNovo.getExt());
			aisAntigo.setTipo(aisNovo.getTipo());
			aisAntigo.setCmd(aisNovo.getCmd());
			aisAntigo.setAtalho(aisNovo.isAtalho());
//			aisAntigo.setAutor(aisNovo.getAutor());
		}
	}
	
	public void editarAssocImagemSom(AssocImagemSom aisNovo)
	{
		int idx = listaAssocImagemSom.indexOf(aisNovo);
		if (idx >= 0)
		{
			AssocImagemSom ais = (AssocImagemSom) listaAssocImagemSom.get(idx);
			ais.setDesc(aisNovo.getDesc());
			ais.setTituloImagem(aisNovo.getTituloImagem());
			ais.setTituloSom(aisNovo.getTituloSom());
			ais.setExt(aisNovo.getExt());
			ais.setTipo(aisNovo.getTipo());
			ais.setCmd(aisNovo.getCmd());
			ais.setAtalho(aisNovo.isAtalho());
//			ais.setAutor(aisNovo.getAutor());
		}
	}
	
	public void editarAssocImagemSom(AssocImagemSom aisNovo, int id)
	{
		AssocImagemSom ais = getAssocImagemSomById(id);
		ais.setDesc(aisNovo.getDesc());
		ais.setTituloImagem(aisNovo.getTituloImagem());
		ais.setTituloSom(aisNovo.getTituloSom());
		ais.setExt(aisNovo.getExt());
		ais.setTipo(aisNovo.getTipo());
		ais.setCmd(aisNovo.getCmd());
		ais.setAtalho(aisNovo.isAtalho());
//		ais.setAutor(aisNovo.getAutor());
	}
	
	
	public void excluirAssocImagemSom(AssocImagemSom ais)
	{
		listaAssocImagemSom.remove(ais);
	}
	
	public void excluirAssocImagemSom(int id)
	{
		listaAssocImagemSom.remove(getAssocImagemSomById(id));
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<AssocImagemSom> getAll()
	{
		return (ArrayList<AssocImagemSom>) listaAssocImagemSom.clone();
	}
	
	public AssocImagemSom getAssocImagemSomById(int id)
	{
		AssocImagemSom ais = null;
		AssocImagemSom ais_temp;
		for (int i = 0, length = listaAssocImagemSom.size(); i < length; i++)
		{
			ais_temp = listaAssocImagemSom.get(i);
			if (ais_temp.getId() == id)
			{
				ais = ais_temp;
				break;
			}

		}
		
		return ais;
	}
	
	public ArrayList<AssocImagemSom> getImagensByDesc(String desc)
	{
		AssocImagemSom ais;
		ArrayList<AssocImagemSom> result = new ArrayList<AssocImagemSom>();
		for (int i = 0, length = listaAssocImagemSom.size(); i < length; i++)
		{
			ais = listaAssocImagemSom.get(i);
			
			if (ais.getDesc().toLowerCase().contains(desc.toLowerCase()))
				result.add(new AssocImagemSom(ais));
		}
		
		return result;		
	}	
	
	private int generateRandomInteger(int min, int max)
	{
		return min + (int)(Math.random() * ((max - min) + 1));
	}

}
