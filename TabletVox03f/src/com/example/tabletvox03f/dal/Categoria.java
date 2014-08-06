package com.example.tabletvox03f.dal;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Categoria implements Parcelable
{
	private int id;
	private String nome;
	private AssocImagemSom ais;
	private ArrayList<AssocImagemSom> imagens;
	private ArrayList<CategoriaAssocImagemSom> catImagens;
	
	public Categoria()
	{}
	
	public Categoria(int id, String nome)
	{
		this.id 	= id;
		this.nome 	= nome;
	}
	
	public Categoria(int id, AssocImagemSom ais, String nome)
	{
		this.id 	= id;
		this.ais 	= ais;
		this.nome 	= nome;
	}
	
	public Categoria(String nome)
	{
		this.nome = nome;
	}
	
	public Categoria(AssocImagemSom ais, String nome)
	{
		this.ais 	= ais;
		this.nome 	= nome;
	}
	

	public Categoria(AssocImagemSom ais, String nome, ArrayList<CategoriaAssocImagemSom> cat_ais_list)
	{
		this.ais 		= ais;
		this.nome 		= nome;
		this.catImagens = cat_ais_list;
	}	
	
	public Categoria(Categoria categoria)
	{
		this.id 		= categoria.getId();
		this.ais 		= categoria.getAIS();
		this.nome 		= categoria.getNome();
		this.imagens 	= categoria.getImagens();
		this.catImagens = categoria.getCatImagens();
	}
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getNome()
	{
		return nome;
	}
	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public ArrayList<AssocImagemSom> getImagens()
	{
		return imagens;
	}

	public void setImagens(ArrayList<AssocImagemSom> imagens)
	{
		this.imagens = imagens;
	}

	public AssocImagemSom getAIS()
	{
		return ais;
	}

	public void setAIS(AssocImagemSom ais)
	{
		this.ais = ais;
	}

	public ArrayList<CategoriaAssocImagemSom> getCatImagens()
	{
		return catImagens;
	}

	public void setCatImagens(ArrayList<CategoriaAssocImagemSom> catImagens)
	{
		this.catImagens = catImagens;
	}

	/*** METODOS E ATRIBUTOS PARCELABLE ***/
	
	public Categoria(Parcel in) 
	{
		readFromParcel(in);
	}
	
	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		// We just need to write each field into the
		// parcel. When we read from parcel, they
		// will come back in the same order
		dest.writeInt(this.id);
		dest.writeParcelable(this.ais, flags);
		dest.writeString(this.nome);
		
	}
	
	private void readFromParcel(Parcel in) 
	{
		 
		// We just need to read back each
		// field in the order that it was
		// written to the parcel
		this.id 		= in.readInt();
		this.ais		= in.readParcelable(AssocImagemSom.class.getClassLoader());
		this.nome 		= in.readString();
	}
	
    /**
    *
    * This field is needed for Android to be able to
    * create new objects, individually or as arrays.
    *
    * This also means that you can use use the default
    * constructor to create the object and use another
    * method to hyrdate it as necessary.
    *
    * I just find it easier to use the constructor.
    * It makes sense for the way my brain thinks ;-)
    *
    */
	public static final Parcelable.Creator<Categoria> CREATOR = new Parcelable.Creator<Categoria>()
	{
		public Categoria createFromParcel(Parcel in) 
		{
	       return new Categoria(in);
		}
	
		public Categoria[] newArray(int size) 
		{
	       return new Categoria[size];
		}
	};
   
	
}
