package main;

import java.util.ArrayList;

import pl.SolveurLineaire;
import sat.Solveur;

public class Main {

	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		Parseur parseur=new Parseur(args[0]);
		MSG msg=parseur.construireMSG();
		
		ArrayList<String> solutionsSAT=new ArrayList<String>();
		ArrayList<String> solutionsLN=new ArrayList<String>();
		

		Solveur solveur=new Solveur(msg);
		SolveurLineaire SLN=new SolveurLineaire(msg);
		//msg.show();
		
		//solutionsSAT=solveur.verifierDivergence();	
		//ecrireSolutionSATDiv(solutionsSAT,msg);
				
		//SLN.verifierDivergence();
		
		ThreadSolveur threadSAT=new ThreadSolveur("SAT",msg,solveur,SLN);
		ThreadSolveur threadLN=new ThreadSolveur("LN",msg,solveur,SLN);
		
		threadSAT.start();
		threadLN.start();
		
	}


	public synchronized static void ecrireSolutionDiv(ArrayList<String> solutions, MSG msg,String arg, long temps) {
		// TODO Auto-generated method stub
		
		System.out.println("Solveur "+arg+" ,temps de resolution : "+temps);
		
		
		if(solutions.size()==0){
			System.out.println("Solveur "+arg+" : Pas de divergence detectee");
		}
		else {
			
			System.out.println("Solveur "+arg+" : divergence detectee !");
			
			for(String solution : solutions){
				String vars[]=solution.split(" ");
				String i=vars[0];
				String j=vars[1];
				System.out.print("Le canal "+i+" "+j+" dans le cycle composé des MSCs : ");
				for(int indice=0 ; indice<msg.getNbArcs() ; indice++){
					double vArc;
					if(arg.equals("SAT")){
						vArc=Integer.parseInt(vars[indice+2]);
					}
					else{
						vArc=Double.parseDouble(vars[indice+2]);
					}
					if(vArc>0) {
						System.out.print(msg.getArcs().get(indice).getDomaine().getId()+" ");
					}
				}
				System.out.println();
			}
			System.out.println("**************************************");
			System.out.println("**************************************");
		}
	}
	
}
