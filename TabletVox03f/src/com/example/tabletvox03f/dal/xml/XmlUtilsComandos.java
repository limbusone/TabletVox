package com.example.tabletvox03f.dal.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSomDAO;

import android.content.Context;

public class XmlUtilsComandos extends XmlUtils
{
	//private XmlUtilsAssocImagemSom xml_ais;
	private AssocImagemSomDAO dao_ais;
	
	public XmlUtilsComandos(Context c)
	{
		super("comandos.xml", c);
		//xml_ais = new XmlUtilsAssocImagemSom(c);
		dao_ais = new AssocImagemSomDAO(c);
	}
	
	// retorna todos os comandos
	public ArrayList<AssocImagemSom> getRegs()
	{
//		InputStream in = XmlUtils.class
//				.getResourceAsStream(caminho_arquivo_xml);
		InputStream in = getStream();

		ArrayList<AssocImagemSom> ais_list = new ArrayList<AssocImagemSom>();
		int id;
		
		dao_ais.open();
		
		try 
		{
			XmlPullParser p = getParse(in);
			p.require(XmlPullParser.START_TAG, null, "comandos");
			while (p.next() != XmlPullParser.END_TAG) 
			{
				// dentro da tag <comandos> (tag raiz)
				if (p.getEventType() != XmlPullParser.START_TAG)
					continue;

				String tag = p.getName();

				if (tag.equals("entrada"))
				{
					// dentro da tag <entrada>
					id = Integer.parseInt(read_atributo_tag_fechada(p, tag, "id"));
					//ais_list.add(xml_ais.getARegAssocImagemSomById(id));
					ais_list.add(dao_ais.getAISById(id));
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

		dao_ais.close();
		
		return ais_list;		
	}

	// retorna somente os comandos que são atalhos	
	public ArrayList<AssocImagemSom> getRegsAtalhos()
	{
//		InputStream in = XmlUtils.class
//				.getResourceAsStream(caminho_arquivo_xml);
		InputStream in = getStream();

		ArrayList<AssocImagemSom> ais_list = new ArrayList<AssocImagemSom>();
		int id;
		boolean isAtalho;
		
		dao_ais.open();
		
		try 
		{
			XmlPullParser p = getParse(in);
			p.require(XmlPullParser.START_TAG, null, "comandos");
			while (p.next() != XmlPullParser.END_TAG) 
			{
				// dentro da tag <comandos> (tag raiz)
				if (p.getEventType() != XmlPullParser.START_TAG)
					continue;

				String tag = p.getName();

				if (tag.equals("entrada"))
				{
					// dentro da tag <entrada>
					id = Integer.parseInt(read_atributo_tag_aberta(p, tag, "id"));
					isAtalho = Boolean.parseBoolean(read_atributo_tag_fechada(p, tag, "atalho"));
					if (isAtalho)
						//ais_list.add(xml_ais.getARegAssocImagemSomById(id));
						ais_list.add(dao_ais.getAISById(id));
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
		
		dao_ais.close();

		return ais_list;		
	}	
}
