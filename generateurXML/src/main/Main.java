package main;

import fenetres.Generateur;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int tailleFichierXML=Integer.parseInt(args[1]);
		String typeFichierXML=args[0];
	
		if (typeFichierXML.equals("fenetres")){
			fenetres.Generateur.genererFenetres(tailleFichierXML);
			System.out.println("protocole fenetres coulissantes de taille "+tailleFichierXML+" genere");
		}
		if (typeFichierXML.equals("circuit")){
			circuit.Generateur.genererCircuit(tailleFichierXML);
			System.out.println("protocole circuit de taille "+tailleFichierXML+" genere");
		}
		if (typeFichierXML.equals("fleur")){
			fleur.Generateur.genererFleur(tailleFichierXML);
			System.out.println("protocole fleur de taille "+tailleFichierXML+" genere");
		}
		if (typeFichierXML.equals("densite")){
			densite.Generateur.genererFleur(tailleFichierXML,15);
			System.out.println("protocole densite de taille "+tailleFichierXML+" genere");
		}
	}

}
