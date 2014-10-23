package com.example.tabletvox03f.dal.assocImagemSom;

import android.os.Parcel;
import android.os.Parcelable;

public class AssocImagemSom implements Parcelable
{
	
	// atributos de negócio
	private int id;
	private String desc;
	private String titulo_imagem;
	private String titulo_som;
	private String ext; // extensão do arquivo de imagem
	private char tipo; // 'v' de verbo ou 'n' de substantivo ou 'c' de categoria
	private int cmd; // se é algum comando ou não (do tipo falar, apagar frase e etc...)
	private boolean atalho;
	
	// atributos auxiliares (de sistema)
	private int categoriaId;

	public AssocImagemSom()
	{}
	

	public AssocImagemSom(int id, String desc, String titulo_imagem, String titulo_som, String ext, char tipo, int cmd) 
	{
		this.id				= id;
		this.desc			= desc;
		this.titulo_imagem 	= titulo_imagem;
		this.titulo_som 	= titulo_som;
		this.ext 			= ext;
		this.tipo			= tipo;
		this.cmd 			= cmd;
	}	
	
	public AssocImagemSom(String desc, String titulo_imagem, String titulo_som, String ext, char tipo, int cmd) 
	{
		this.desc			= desc;
		this.titulo_imagem 	= titulo_imagem;
		this.titulo_som 	= titulo_som;
		this.ext 			= ext;
		this.tipo			= tipo;
		this.cmd 			= cmd;
	}

	public AssocImagemSom(int id, String desc, String titulo_imagem, String titulo_som, String ext, char tipo, int cmd, boolean atalho)
	{
		this.id				= id;
		this.desc			= desc;
		this.titulo_imagem 	= titulo_imagem;
		this.titulo_som 	= titulo_som;
		this.ext 			= ext;
		this.tipo			= tipo;
		this.cmd 			= cmd;
		this.atalho			= atalho;
	}

	public AssocImagemSom(String desc, String titulo_imagem, String titulo_som, String ext, char tipo, int cmd, boolean atalho)
	{
		this.desc			= desc;
		this.titulo_imagem 	= titulo_imagem;
		this.titulo_som 	= titulo_som;
		this.ext 			= ext;
		this.tipo			= tipo;
		this.cmd 			= cmd;
		this.atalho			= atalho;
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

	public int getCategoriaId()
	{
		return categoriaId;
	}


	public void setCategoriaId(int categoriaId)
	{
		this.categoriaId = categoriaId;
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
		dest.writeString(this.desc);
		dest.writeString(this.titulo_imagem);
		dest.writeString(this.titulo_som);
		dest.writeString(this.ext);
		dest.writeString("" + this.tipo);
		dest.writeInt(this.cmd);
		dest.writeInt((this.atalho) ? 1 : 0);
		
	}
	
	private void readFromParcel(Parcel in) 
	{
		 
		// We just need to read back each
		// field in the order that it was
		// written to the parcel
		this.id 			= in.readInt();
		this.desc			= in.readString();
		this.titulo_imagem 	= in.readString();
		this.titulo_som		= in.readString();
		this.ext			= in.readString();
		this.tipo			= in.readString().charAt(0);
		this.cmd			= in.readInt();
		this.atalho			= (in.readInt() == 1) ? true : false;
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
