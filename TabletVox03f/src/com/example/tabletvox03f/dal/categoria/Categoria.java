package com.example.tabletvox03f.dal.categoria;

import java.util.ArrayList;

import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;

import android.os.Parcel;
import android.os.Parcelable;

public class Categoria implements Parcelable
{
	private int id;
	private String nome;
	private AssocImagemSom ais;
	private ArrayList<AssocImagemSom> imagens;
	private ArrayList<CategoriaAssocImagemSom> catImagens;
	private int pagina;
	private int ordem;
	
	public Categoria()
	{}
	
	public Categoria(int id, String nome)
	{
		this.id 	= id;
		this.nome 	= nome;
	}
	
	public Categoria(int id, String nome, AssocImagemSom ais)
	{
		this.id 	= id;
		this.nome 	= nome;
		this.ais 	= ais;
	}
	
	public Categoria(int id, String nome, AssocImagemSom ais, ArrayList<AssocImagemSom> imagens)
	{
		this.id 		= id;
		this.nome 		= nome;
		this.ais 		= ais;
		this.imagens 	= imagens;
	}
	public Categoria(int id, String nome, AssocImagemSom ais, ArrayList<AssocImagemSom> imagens, int pagina, int ordem)
	{
		this.id 		= id;
		this.nome 		= nome;
		this.ais 		= ais;
		this.imagens 	= imagens;
		this.pagina		= pagina;
		this.ordem 		= ordem;
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
		this.pagina		= categoria.getPagina();
		this.ordem		= categoria.getOrdem();
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
	
	public int getPagina()
	{
		return pagina;
	}


	public void setPagina(int pagina)
	{
		this.pagina = pagina;
	}


	public int getOrdem()
	{
		return ordem;
	}


	public void setOrdem(int ordem)
	{
		this.ordem = ordem;
	}
	
	public void setAll(Categoria categoria)
	{
		this.id 		= categoria.getId();
		this.ais 		= categoria.getAIS();
		this.nome 		= categoria.getNome();
		this.imagens 	= categoria.getImagens();
		this.catImagens = categoria.getCatImagens();
		this.pagina		= categoria.getPagina();
		this.ordem		= categoria.getOrdem();		
	}
	
	public AssocImagemSom getImagemById(int id)
	{
		AssocImagemSom imagem = null;
		AssocImagemSom imagem_temp;
		for (int i = 0, length = imagens.size(); i < length; i++)
		{
			imagem_temp = imagens.get(i);
			if (imagem_temp.getId() == id)
			{
				imagem = imagem_temp;
				break;
			}

		}
		
		return imagem;
	}		

	/* METODOS E ATRIBUTOS PARCELABLE */
	
	public Categoria(Parcel in) 
	{
		// inicializando arrayList para não dar nullPointerException ao chamar a linha
		this.imagens = new ArrayList<AssocImagemSom>();
		readFromParcel(in);
	}
	
	@Override
	public int describeContents()
	{
		// Auto-generated method stub
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
		dest.writeTypedList(this.imagens);
		dest.writeInt(this.pagina);
		dest.writeInt(this.ordem);
		
	}
	
	private void readFromParcel(Parcel in) 
	{
		 
		// We just need to read back each
		// field in the order that it was
		// written to the parcel
		this.id 		= in.readInt();
		this.ais		= in.readParcelable(AssocImagemSom.class.getClassLoader());
		this.nome 		= in.readString();
		in.readTypedList(this.imagens, AssocImagemSom.CREATOR);
		this.pagina		= in.readInt();
		this.ordem		= in.readInt();
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
