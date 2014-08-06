package com.example.tabletvox03f.dal;

import android.os.Parcel;
import android.os.Parcelable;

public class AssocImagemSom implements Parcelable
{
	private int id;
	private String desc;
	private String titulo_imagem;
	private String titulo_som;
	private String ext; // extensão do arquivo de imagem
	private char tipo; // 'v' de verbo ou 'n' de substantivo ou 'c' de categoria
	private int cmd; // se é algum comando ou não (do tipo falar, apagar frase e etc...)
	private boolean atalho;

	public AssocImagemSom()
	{}
	
	public AssocImagemSom(String dsc, String ti, String ts, String ex, char tp, int cm) 
	{
		this.desc			= dsc;
		this.titulo_imagem 	= ti;
		this.titulo_som 	= ts;
		this.ext 			= ex;
		this.tipo			= tp;
		this.cmd 			= cm;
	}

	public AssocImagemSom(String dsc, String ti, String ts, String ex, char tp, int cm, boolean ata)
	{
		this.desc			= dsc;
		this.titulo_imagem 	= ti;
		this.titulo_som 	= ts;
		this.ext 			= ex;
		this.tipo			= tp;
		this.cmd 			= cm;
		this.atalho			= ata;
	}
	
	// construtor de cópia
	public AssocImagemSom(AssocImagemSom ais)
	{
		this.desc 			= ais.getDesc();
		this.titulo_imagem 	= ais.getTituloImagem();
		this.titulo_som		= ais.getTituloSom();
		this.ext			= ais.getExt();
		this.tipo			= ais.getTipo();
		this.cmd			= ais.getCmd();
		this.atalho			= ais.isAtalho();
	}
	
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public String getTituloImagem() 
	{
		return titulo_imagem;
	}

	public String getTituloSom() 
	{
		return titulo_som;
	}

	public String getExt()
	{
		return ext;
	}

	public int getCmd()
	{
		return cmd;
	}	
	
	public void setTituloImagem(String value) 
	{
		this.titulo_imagem = value;
	}


	public void setTituloSom(String value) 
	{
		this.titulo_som = value;
	}

	public void setExt(String ex)
	{
		this.ext = ex;
	}

	public char getTipo()
	{
		return tipo;
	}

	public void setTipo(char tipo)
	{
		this.tipo = tipo;
	}

	public void setCmd(int cm)
	{
		this.cmd = cm;
	}

	public boolean isAtalho()
	{
		return atalho;
	}

	public void setAtalho(boolean atalho)
	{
		this.atalho = atalho;
	}

	/*** METODOS E ATRIBUTOS PARCELABLE ***/
	
	public AssocImagemSom(Parcel in) 
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
		
	}
	
	private void readFromParcel(Parcel in) 
	{
		 
		// We just need to read back each
		// field in the order that it was
		// written to the parcel
		this.id 		= in.readInt();
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
	public static final Parcelable.Creator<AssocImagemSom> CREATOR = new Parcelable.Creator<AssocImagemSom>()
	{
		public AssocImagemSom createFromParcel(Parcel in) 
		{
	       return new AssocImagemSom(in);
		}
	
		public AssocImagemSom[] newArray(int size) 
		{
	       return new AssocImagemSom[size];
		}
	};
}
