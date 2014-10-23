package com.example.tabletvox03f.dal.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSomDAO;

import android.content.Context;

public class XmlUtilsTelas extends XmlUtils
{
	//private XmlUtilsAssocImagemSom xml_ais;
	private AssocImagemSomDAO dao_ais;
	private String tag_root_name;
	
	public XmlUtilsTelas(Context c, String nome_arq_xml, String troot)
	{
		super(nome_arq_xml + ".xml", c);
		tag_root_name = troot;
		//xml_ais = new XmlUtilsAssocImagemSom(c);
		dao_ais = new AssocImagemSomDAO(c);
	}
	
	public ArrayList<AssocImagemSom> getRegsByNumPage(int num_page)
	{
//		InputStream in = XmlUtils.class
//				.getResourceAsStream(caminho_arquivo_xml);
		InputStream in = getStream();

		ArrayList<AssocImagemSom> ais_list = null;

		try 
		{
			XmlPullParser p = getParse(in);
			p.require(XmlPullParser.START_TAG, null, tag_root_name);

			while (p.next() != XmlPullParser.END_TAG) 
			{
				// dentro da tag raiz
				if (p.getEventType() != XmlPullParser.START_TAG)
					continue;

				String tag = p.getName();
				if (tag.equals("page") && read_atributo_tag_aberta(p, "page", "numero").equals(Integer.toString(num_page)))
				{
					// dentro da tag <page> sob numero escolhido
					ais_list = read_page(p);
					break;
				}	
				else
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

		return ais_list;		
	}
	
	private ArrayList<AssocImagemSom> read_page(XmlPullParser parser) throws NumberFormatException, IOException, XmlPullParserException
	{
		int id;
		ArrayList<AssocImagemSom> ais_list = new ArrayList<AssocImagemSom>();
		parser.require(XmlPullParser.START_TAG, null, "page");
		
		dao_ais.open();
		
		while (parser.next() != XmlPullParser.END_TAG) 
		{
			if (parser.getEventType() != XmlPullParser.START_TAG)
				continue;

			String name = parser.getName();

			if (name.equals("entrada"))
			{
				id = Integer.parseInt(read_atributo_tag_fechada(parser, "entrada", "id"));
				//ais_list.add(xml_ais.getARegAssocImagemSomById(id));
				ais_list.add(dao_ais.getAISById(id));
			}
			else
				skip(parser);
			
		}
		
		dao_ais.close();
		
		return ais_list;
	}
	
	// retorna o numero da ultima pagina
	public int getLastPage()
	{
		Document xmlDoc = getDocument();
		NodeList pages = xmlDoc.getElementsByTagName("page");
		Node last = pages.item(pages.getLength() - 1);
		Node attr = last.getAttributes().getNamedItem("numero");
		int lastPage = Integer.parseInt(attr.getNodeValue());
		return lastPage;
	}
	
	public boolean inserirImagemByPage(String pagina, String idImagem)
	{
		Node parentNode = getNodeByAttrValue("page", "numero", pagina);
		if (parentNode != null)
		{
			addSingleNodeWithAttr(parentNode, "entrada", "id", idImagem);
			return true;
		}
		return false;
	}
	
	public boolean excluirImagemById(String pagina, String idImagem)
	{
		Node parentNode = getNodeByAttrValue("page", "numero", pagina);
		if (parentNode != null)
			return deleteSingleNode(parentNode, "entrada", idImagem);
		
		return false;
	}
	
	public boolean alterarImagemById(String pagina, String idImagem, String newAttrValue)
	{
		Node parentNode = getNodeByAttrValue("page", "numero", pagina);
		if (parentNode != null)
			return updateAttrValueById(parentNode, idImagem, "id", newAttrValue);
		return false;
	}
}
