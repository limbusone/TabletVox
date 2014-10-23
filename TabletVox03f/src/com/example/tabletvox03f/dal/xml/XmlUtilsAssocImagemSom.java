package com.example.tabletvox03f.dal.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;

import android.content.Context;

public class XmlUtilsAssocImagemSom extends XmlUtils 
{
	public XmlUtilsAssocImagemSom(Context c) 
	{
		super("assoc_imagem_som.xml", c);
		// TODO Auto-generated constructor stub
	}

	public AssocImagemSom getARegAssocImagemSomById(int id)
	{
//		InputStream in = XmlUtils.class
//				.getResourceAsStream(caminho_arquivo_xml);
		
		InputStream in = getStream();

		AssocImagemSom ais = null;

		try 
		{
			XmlPullParser p = getParse(in);
			p.require(XmlPullParser.START_TAG, null, "assoc_imagem_som");
			
			while (p.next() != XmlPullParser.END_TAG) 
			{
				if (p.getEventType() != XmlPullParser.START_TAG)
					continue;

				String tag = p.getName();

				if (tag.equals("entrada") && read_atributo_tag_aberta(p, "entrada", "id").equals(Integer.toString(id)))
				{
					ais = readAssocImagemSom(p);
					break;
				}
				else
					// pula a tag
					skip(p);

			}
		} 
		catch (XmlPullParserException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				in.close();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ais;
		
	}
	public ArrayList<AssocImagemSom> getAllRegAssocImagemSom() 
	{
//		InputStream in = XmlUtils.class
//				.getResourceAsStream(caminho_arquivo_xml);
		InputStream in = getStream();

		ArrayList<AssocImagemSom> ais_list = new ArrayList<AssocImagemSom>();

		try {
			XmlPullParser p = getParse(in);
			p.require(XmlPullParser.START_TAG, null, "assoc_imagem_som");
			while (p.next() != XmlPullParser.END_TAG) {
				if (p.getEventType() != XmlPullParser.START_TAG)
					continue;

				String tag = p.getName();

				if (tag.equals("entrada"))
					ais_list.add(readAssocImagemSom(p));
				else
					// pula a tag
					skip(p);

			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ais_list;

	}

	// acesso ao conteudo de uma tag <entrada> em assoc_imagem_som.xml por id da
	// imagem
//	public AssocImagemSom getRegAssocImagemSomByIdImagem(int id)
//			throws XmlPullParserException, IOException {
//
////		InputStream in = Utils.class.getClass().getResourceAsStream(
////				caminho_arquivo_xml);
//		InputStream in = getStream();
//
//		AssocImagemSom ais = null;
//		try 
//		{
//			XmlPullParser p = getParse(in);
//			p.require(XmlPullParser.START_TAG, null, "assoc_imagem_som");
//			while (p.next() != XmlPullParser.END_TAG) 
//			{
//				if (p.getEventType() != XmlPullParser.START_TAG)
//					continue;
//
//				String tag = p.getName();
//
//				// ao encontrar uma tag <entrada> verifica se o conteudo de
//				// <id_imagem>
//				// equivale-se ao id procurado
//				if (tag.equals("entrada")
//						&& (ais = readAssocImagemSom(p)).getIdImagem() == id)
//					break;
//				else
//					// pula a tag
//					skip(p);
//
//			}
//		} 
//		finally 
//		{
//			in.close();
//		}
//
//		return ais;
//	}

	// acesso ao conteudo de uma tag <entrada> em assoc_imagem_som.xml por
	// titulo da imagem
	public AssocImagemSom getRegAssocImagemSomByTituloImagem(String titulo)
			throws XmlPullParserException, IOException {

//		InputStream in = Utils.class.getClass().getResourceAsStream(
//				caminho_arquivo_xml);
		InputStream in = getStream();

		AssocImagemSom ais = null;
		try 
		{
			XmlPullParser p = getParse(in);
			p.require(XmlPullParser.START_TAG, null, "assoc_imagem_som");
			while (p.next() != XmlPullParser.END_TAG) 
			{
				if (p.getEventType() != XmlPullParser.START_TAG)
					continue;

				String tag = p.getName();

				// ao encontrar uma tag <entrada> verifica se o conteudo de
				// <titulo_imagem>
				// equivale-se ao titulo procurado
				if (tag.equals("entrada")
						&& (ais = readAssocImagemSom(p)).getTituloImagem() == titulo)
					break;
				else
					// pula a tag
					skip(p);
			}
		} 
		finally 
		{
			in.close();
		}

		return ais;
	}

	// Parses the contents of an AssocImagemSom. If it encounters a
	// titulo_imagem, titulo_som, or id_imagem tag, hands them off
	// to their respective "read" methods for processing. Otherwise, skips the
	// tag.
	private AssocImagemSom readAssocImagemSom(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, null, "entrada");
		String desc				= "";
		String titulo_imagem 	= "";
		String titulo_som 		= "";
		String ext 				= "";
		char tipo				= '\u0000';
		String cmd				= "0";
		while (parser.next() != XmlPullParser.END_TAG) 
		{
			if (parser.getEventType() != XmlPullParser.START_TAG)
				continue;

			String name = parser.getName();
			if (name.equals("desc"))
				desc = read_conteudo_tag(parser, name);
			else if (name.equals("titulo_imagem")) 
				titulo_imagem = read_conteudo_tag(parser, name);
			else if (name.equals("titulo_som")) 
				titulo_som = read_conteudo_tag(parser, name);
			else if (name.equals("ext")) 
				ext = read_conteudo_tag(parser, name);
			else if (name.equals("tipo")) 
				tipo = read_conteudo_tag(parser, name).charAt(0); 
			else if (name.equals("cmd")) 
				cmd = read_conteudo_tag(parser, name);
			else 
				skip(parser);
			
		}
		return new AssocImagemSom(desc, titulo_imagem, titulo_som,
				ext, tipo, Integer.parseInt(cmd));
	}
	
}
