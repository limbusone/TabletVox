package com.example.tabletvox03f.dal.perfil;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.tabletvox03f.dal.categoria.Categoria;
import com.example.tabletvox03f.dal.categoria.ListaCategoria;

public class Perfil implements Parcelable
{
	private int id;
	private String nome;
	private String autor;
	private ListaCategoria categorias;
	
	public Perfil()
	{}
	
	public Perfil(int id, String nome, String autor)
	{
		this.id 	= id;
		this.nome 	= nome;
		this.autor 	= autor;
	}
	
	public Perfil(int id, String nome, String autor, ListaCategoria categorias)
	{
		this.id 	= id;
		this.nome 	= nome;
		this.autor 	= autor;
		this.categorias = categorias;
	}
	
	public Perfil(Perfil perfil)
	{
		this.id 		= perfil.getId();
		this.nome 		= perfil.getNome();
		this.autor 		= perfil.getAutor();
		this.categorias = perfil.getCategorias();
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
	
	public String getAutor()
	{
		return autor;
	}
	public void setAutor(String autor)
	{
		this.autor = autor;
	}

	public ListaCategoria getCategorias()
	{
		return categorias;
	}

	public void setCategorias(ListaCategoria categorias)
	{
		this.categorias = categorias;
	}

	public Categoria getCategoriaById(int id)
	{
		Categoria categoria = null;
		Categoria categoria_temp;
		for (int i = 0, length = categorias.size(); i < length; i++)
		{
			categoria_temp = categorias.get(i);
			if (categoria_temp.getId() == id)
			{
				categoria = categoria_temp;
				break;
			}

		}
		
		return categoria;
	}	
	
	/*** METODOS E ATRIBUTOS PARCELABLE ***/
	
	public Perfil(Parcel in) 
	{
		// inicializando arrayList para n�o dar nullPointerException ao chamar a linha 
		// in.readTypedList(this.categorias, Categoria.CREATOR);
		this.categorias = new ListaCategoria();
		readFromParcel(in);
	}
	
	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		// We just need to write each field into the
		// parcel. When we read from parcel, they
		// will come back in the same order
		dest.writeInt(this.id);
		dest.writeString(this.nome);
		dest.writeString(this.autor);
		dest.writeTypedList(this.categorias);
		
	}
	
	private void readFromParcel(Parcel in) 
	{
		 
		// We just need to read back each
		// field in the order that it was
		// written to the parcel
		this.id 		= in.readInt();
		this.nome 		= in.readString();
		this.autor 		= in.readString();
		in.readTypedList(this.categorias, Categoria.CREATOR);
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
   public static final Parcelable.Creator<Perfil> CREATOR =
   new Parcelable.Creator<Perfil>()
   {
	   public Perfil createFromParcel(Parcel in) 
	   {
	       return new Perfil(in);
	   }
	
	   public Perfil[] newArray(int size) 
	   {
	       return new Perfil[size];
	   }
   };

}
