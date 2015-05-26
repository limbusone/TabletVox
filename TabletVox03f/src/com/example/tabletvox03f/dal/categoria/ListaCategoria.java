package com.example.tabletvox03f.dal.categoria;

import java.util.ArrayList;

import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;

import android.os.Parcel;
import android.os.Parcelable;

public class ListaCategoria extends ArrayList<Categoria> implements Parcelable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ListaCategoria()
	{
		
	}
	
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
			int maxPage = 1;
			for (int i = 1, length = this.size(); i < length; i++)
			{
				Categoria categoria = this.get(i);
				if (categoria.getPagina() > maxPage)
					maxPage = categoria.getPagina();
			}
			
			return maxPage;
		}
	}
	
	/* METODOS E ATRIBUTOS PARCELABLE */
	
    public ListaCategoria(Parcel in) 
    {
        this();
        readFromParcel(in);
    }
    
    public void writeToParcel(Parcel dest, int flags) 
    {
        int size = this.size();

        // We have to write the list size, we need him recreating the list
        dest.writeInt(size);
        
        for (int i = 0; i < size; i++) 
        {
            Categoria c = this.get(i);

    		dest.writeInt(c.getId());
    		dest.writeString(c.getNome());
    		dest.writeParcelable(c.getAIS(), flags);
    		dest.writeTypedList(c.getImagens());
    		dest.writeInt(c.getPagina());
    		dest.writeInt(c.getOrdem());            
        }        
    }    

    @SuppressWarnings("unchecked")
	private void readFromParcel(Parcel in) 
    {
        this.clear();

        // First we have to read the list size
        int size = in.readInt();
        
        for (int i = 0; i < size; i++) 
        {
            Categoria c = new Categoria
    		(
        		in.readInt(), 
        		in.readString(), 
        		(AssocImagemSom) in.readParcelable(AssocImagemSom.class.getClassLoader()), 
        		in.readArrayList(AssocImagemSom.class.getClassLoader()),
        		in.readInt(),
        		in.readInt()
    		);

            this.add(c);
        }        

    }

    public int describeContents() 
    {
        return 0;
    }

    public static final Parcelable.Creator<ListaCategoria> CREATOR = new Parcelable.Creator<ListaCategoria>() 
    {
        public ListaCategoria createFromParcel(Parcel in) 
        {
            return new ListaCategoria(in);
        }

        public ListaCategoria[] newArray(int size) 
        {
            return new ListaCategoria[size];
        }
    };
	
}
