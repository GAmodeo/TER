package main;

import java.io.FileNotFoundException;

import pl.SolveurLineaire;
import sat.Solveur;

public class Main {

	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		Parseur parseur=new Parseur(args[0]);
		MSG msg=parseur.construireMSG();
		
		//msg.show();
		Solveur solveur=new Solveur(msg);
		
		solveur.verifierDivergence();
		
		SolveurLineaire SLN=new SolveurLineaire(msg);
		SLN.verifierDivergence();
		
	}
	
}
