package com.example.tabletvox03f.dal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class XmlUtils 
{
	//protected final String XML_FOLDER = "/com/example/tabletvox03a/utils/";
	//protected final String XML_FOLDER = "dados_xml/";
	protected final String XML_ASSETS_FOLDER = "dados_xml/";
	protected String caminho_arquivo_xml;
	protected Context mContext;

	public XmlUtils(String arquivo_xml, Context c) 
	{
		//caminho_arquivo_xml = XML_FOLDER + arquivo_xml;
		caminho_arquivo_xml = arquivo_xml;
		mContext = c;
	}
	
	public InputStream getStream()
	{
		try
		{
			//return mContext.getAssets().open(caminho_arquivo_xml);
			// retorna o stream do internal storage
			return mContext.openFileInput(caminho_arquivo_xml);
		}
		catch (FileNotFoundException fnfe) // se o arquivo não for encontrado em internal storage, busca-se em assets
		{
			try
			{
				return mContext.getAssets().open(XML_ASSETS_FOLDER + caminho_arquivo_xml);
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}

	}
	
	// retorna um objeto DOM
	protected Document getDocument()
	{
        Document document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try 
        {
            DocumentBuilder db = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(getStream());
            document = db.parse(inputSource);
        } catch (ParserConfigurationException e) 
        {
            Log.e("Error: ", e.getMessage());
            return null;
        } 
        catch (SAXException e) 
        {
            Log.e("Error: ", e.getMessage());
            return null;
        } 
        catch (IOException e) 
        {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        return document;		
	}
	
	// operações com o XML
	
    protected String getValue(Element item, String name) 
    {
        NodeList nodes = item.getElementsByTagName(name);
        return this.getTextNodeValue(nodes.item(0));
    }
    
    protected final String getTextNodeValue(Node node) 
    {
        Node child;
        if (node != null) 
        {
            if (node.hasChildNodes()) 
            {
                child = node.getFirstChild();
                while(child != null) 
                {
                    if (child.getNodeType() == Node.TEXT_NODE) 
                        return child.getNodeValue();
                    child = child.getNextSibling();
                }
            }
        }
        return "";
    }
    
    public void addSingleNode(String parentName, String tagName)
    {
    	Document xmlDoc = getDocument();
    	Node parentNode = xmlDoc.getElementsByTagName(parentName).item(0);
    	Element newNode = xmlDoc.createElement(tagName);
    	parentNode.appendChild(newNode);
    	
    	salvar(xmlDoc);
    }
    
    public void addSingleNodeWithAttr(String parentName, String tagName, String attrName, String attrValue)
    {
    	Document xmlDoc = getDocument();
    	Node parentNode = xmlDoc.getElementsByTagName(parentName).item(0);
    	Element newNode = xmlDoc.createElement(tagName);
    	Attr newAttr	= xmlDoc.createAttribute(attrName);
    	newAttr.setValue(attrValue);
    	newNode.setAttributeNode(newAttr);
    	parentNode.appendChild(newNode);
    	
    	salvar(xmlDoc);
    }
    
    public void addSingleNodeWithAttr(Node parentNode, String tagName, String attrName, String attrValue)
    {
    	Document xmlDoc = parentNode.getOwnerDocument();
    	Element newNode = xmlDoc.createElement(tagName);
    	Attr newAttr	= xmlDoc.createAttribute(attrName);
    	newAttr.setValue(attrValue);
    	newNode.setAttributeNode(newAttr);
    	parentNode.appendChild(newNode);
    	
    	salvar(xmlDoc);
    }
    
    public boolean updateSingleNodeWithAttrById(String id, String attrName, String attrValue)
    {
    	Document xmlDoc = getDocument();
    	Node node = xmlDoc.getElementById(id);
    	node.getAttributes().getNamedItem(attrName).setNodeValue(attrValue);
    	
    	salvar(xmlDoc);
    	return true;
    }
    
    public boolean updateAttrValueById(Node parentNode, String id, String attrName, String attrValue)
    {
    	Document xmlDoc = parentNode.getOwnerDocument();
    	Node n = getNodeByIdAndParentNode(parentNode, id);
    	if (n != null)
    	{
    		n.getAttributes().getNamedItem(attrName).setNodeValue(attrValue);
    		salvar(xmlDoc);
    		return true;
    	}
    	return false;
    }
    
    public void getNodeById(String tagName, String id)
    {
    }
    
    public Node getNodeById(String tagName, String id, Document xmlDoc)
    {
    	NodeList nodes = xmlDoc.getElementsByTagName(tagName);
    	int i = 0, length = nodes.getLength();
    	String idAtual = "";
    	for (; i < length; i++)
    	{
    		idAtual = nodes.item(i).getAttributes().getNamedItem("id").getNodeValue();
    		if (id.equals(idAtual))
    		{
    			Node nodeFound = nodes.item(i);
    			return nodeFound;
    		}
    	}
    	return null;
    }
    
    public Node getNodeByIdAndParentNode(Node parentNode, String id)
    {
    	NodeList nodes = parentNode.getChildNodes();
    	int i = 0, length = nodes.getLength();
    	String idAtual = "";
    	for (; i < length; i++)
    	{
    		if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)
    		{
	    		idAtual = nodes.item(i).getAttributes().getNamedItem("id").getNodeValue();
	    		if (id.equals(idAtual))
	    		{
	    			Node nodeFound = nodes.item(i);
	    			return nodeFound;
	    		}
    		}
    	}
    	return null;
    }
    
    public Node getNodeByAttrValue(String tagName, String attrName, String attrValue)
    {
    	Document xmlDoc = getDocument();
    	NodeList nodes = xmlDoc.getElementsByTagName(tagName);
    	int i = 0, length = nodes.getLength();
    	String attrValueAtual = "";
    	for (; i < length; i++)
    	{
    		attrValueAtual = nodes.item(i).getAttributes().getNamedItem(attrName).getNodeValue();
    		if (attrValue.equals(attrValueAtual))
    		{
    			Node nodeFound = nodes.item(i);
    			return nodeFound;
    		}
    	}    	
    	return null;
    }
    
    public boolean deleteSingleNode(String parentName, String tagName, String id)
    {
    	Document xmlDoc = getDocument();
    	Node parentNode = xmlDoc.getElementsByTagName(parentName).item(0);
    	
    	Node oldChild = getNodeById(tagName, id, xmlDoc);

    	if (oldChild != null)
    	{
    		parentNode.removeChild(oldChild);
    		salvar(xmlDoc);
    		return true;
    	}
    	return false;
    }
    
    public boolean deleteSingleNode(Node parentNode, String tagName, String id)
    {
    	Document xmlDoc = parentNode.getOwnerDocument();
    	Node oldChild = getNodeByIdAndParentNode(parentNode, id);
    	
    	if (oldChild != null)
    	{
    		parentNode.removeChild(oldChild);
    		salvar(xmlDoc);
    		return true;
    	}
    	return false;
    }
    
    public boolean deleteSingleNode(String parentName, String id)
    {
    	Document xmlDoc = getDocument();
    	Node parentNode = xmlDoc.getElementsByTagName(parentName).item(0);
    	Node oldChild = xmlDoc.getElementById(id);
    	
    	if (oldChild != null)
    	{
    		parentNode.removeChild(oldChild);
    		salvar(xmlDoc);
    		return true;
    	}
    	return false;
    }
    
    
    public void addTextToNode(Node n, String text)
    {
    	Document xmlDoc = getDocument();
    	n.appendChild(xmlDoc.createTextNode(text));
    	
    	salvar(xmlDoc);
    }
    
    // salvando o arquivo xml
    protected void salvar(Document xmlDoc)
    {
    	try
		{
        	File arquivo 				= mContext.getFileStreamPath(caminho_arquivo_xml);
        	
        	TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer 	= tFactory.newTransformer();

			DOMSource source 			= new DOMSource(xmlDoc);
	    	StreamResult result 		= new StreamResult(arquivo);
	    	
	    	transformer.transform(source, result);
	    	
		} 
    	catch (TransformerConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	catch (TransformerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    }

	// For the tags titulo_imagem and titulo_som, extracts their text values.
	protected String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) 
		{
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	// serve para pular para outra tag de mesmo nível hierarquico
	protected void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG)
			throw new IllegalStateException();

		int depth = 1;

		while (depth != 0) 
		{
			switch (parser.next()) 
			{
				case XmlPullParser.END_TAG:
					depth--;
					break;
				case XmlPullParser.START_TAG:
					depth++;
					break;
			}
		}

	}

	// carrega arquivo xml na memória
	protected XmlPullParser getParse(InputStream in)
			throws XmlPullParserException, IOException 
    {
		XmlPullParser parser = Xml.newPullParser();
		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		parser.setInput(in, null);
		parser.nextTag();
		return parser;
	}
	
	protected String read_conteudo_tag(XmlPullParser parser, String nome_tag) throws IOException, XmlPullParserException 
	{
		parser.require(XmlPullParser.START_TAG, null, nome_tag);
		String conteudo_da_tag = readText(parser);
		parser.require(XmlPullParser.END_TAG, null, nome_tag);
		return conteudo_da_tag;
	}
	
	protected String read_atributo_tag_fechada(XmlPullParser parser, String nome_tag, String nome_atributo) throws IOException, XmlPullParserException
	{
		parser.require(XmlPullParser.START_TAG, null, nome_tag);
		String conteudo_atributo = parser.getAttributeValue(null, nome_atributo);
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, null, nome_tag);
		return conteudo_atributo;
	}
	
	protected String read_atributo_tag_aberta(XmlPullParser parser, String nome_tag, String nome_atributo) throws IOException, XmlPullParserException
	{
		String conteudo_atributo = parser.getAttributeValue(null, nome_atributo);
		return conteudo_atributo;		
	}
}
