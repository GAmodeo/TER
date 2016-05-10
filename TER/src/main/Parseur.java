package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Parseur {

	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private Document document;

	public Parseur(String nomFichierXML){
		factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		try {
			document = builder.parse(new File(nomFichierXML));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public MSG construireMSG(){
		
		
		//contiendra l'element MSG qui a pour noeuds fils la liste des MSC et celle des arcs
		final Element msg = document.getDocumentElement();
		
		//on recupere ces dits noeuds fils
		final NodeList listeMSC_listeArcs = msg.getChildNodes();
		
		//ici on recupere les noeuds MSC
		final NodeList elementsMSC =  listeMSC_listeArcs.item(1).getChildNodes();
		
		//puis on recupere les arcs
		final NodeList arcs =  listeMSC_listeArcs.item(3).getChildNodes();

		ArrayList<MSC> listeMSC=construireListeMSC(elementsMSC);
		MSG msg1=new MSG(listeMSC);
		ArrayList<Arc> listeArcs=construireListeArcs(arcs,msg1);	
		msg1.setArcs(listeArcs);
		msg1.numeroter();
		msg1.listerDomaines(listeArcs);
		
		
		return msg1;
	}

	private ArrayList<Arc> construireListeArcs(NodeList arcs,MSG msg) {
		
		ArrayList<Arc> listeArcs=new ArrayList<Arc>();
		
		for (int i = 0; i<arcs.getLength(); i++) {
		    if(arcs.item(i).getNodeType() == Node.ELEMENT_NODE) {
		        Element arc = (Element) arcs.item(i);
		 
		        Arc arcCourant=creerArc(arc,msg);
		        listeArcs.add(arcCourant);
		    }				
		}

		return listeArcs;
	}

	private Arc creerArc(Element arc,MSG msg) {
		Element domaine=  (Element) arc.getElementsByTagName("domaine").item(0);
		Element codomaine= (Element) arc.getElementsByTagName("codomaine").item(0);
			    
	    MSC MSCDomaine=msg.trouverEtat(domaine.getTextContent());
	    MSC MSCCodomaine=msg.trouverEtat(codomaine.getTextContent());

		return new Arc(MSCDomaine,MSCCodomaine);
	}

	private ArrayList<MSC> construireListeMSC(NodeList elementsMSC) {
		ArrayList<MSC> listeMSC=new ArrayList<MSC>();
		
		for (int i = 0; i<elementsMSC.getLength(); i++) {
			
		    if(elementsMSC.item(i).getNodeType() == Node.ELEMENT_NODE) {
		        Element MSC = (Element) elementsMSC.item(i);
		        
		        MSC mscCourant=creerMSC(MSC);
		        listeMSC.add(mscCourant);
		    }				
		}

		return listeMSC;
	}

	private MSC creerMSC(Element msc) {
		Element id=  (Element) msc.getElementsByTagName("id").item(0);
		
		
		Element listeActions= (Element)  msc.getElementsByTagName("listeActions").item(0);
		
		
		ArrayList<Action> actions=construireListeActions(listeActions);
			
		
		return new MSC(id.getTextContent(),actions);
	}

	private ArrayList<Action> construireListeActions(Element listeActions) {
		ArrayList<Action> actions=new ArrayList<Action>();
		
		NodeList liste=listeActions.getChildNodes();
		for (int i = 0; i<liste.getLength(); i++) {
		    if(liste.item(i).getNodeType() == Node.ELEMENT_NODE) {
		        Element action = (Element) liste.item(i);
		  
		        Action actionCourante=creerAction(action);
		        actions.add(actionCourante);
		    }				
		}

		
		return actions;
	}

	private Action creerAction(Element action) {
		
		String typeAction;
		int source,destination;
		
		Element elementSource=  (Element) action.getElementsByTagName("source").item(0);
		Element elementDestination= (Element) action.getElementsByTagName("destination").item(0);
		


		typeAction=action.getAttribute("type").substring(0);
		source=Integer.parseInt(elementSource.getTextContent());
		destination=Integer.parseInt(elementDestination.getTextContent());

	
		return new Action(typeAction,source,destination);
	}
}
