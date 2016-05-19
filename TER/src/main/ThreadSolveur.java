package main;

import java.util.ArrayList;

import pl.SolveurLineaire;
import sat.Solveur;


public class ThreadSolveur extends Thread{
	
	String mode;
	MSG msg;
	Solveur solveur;
	SolveurLineaire SLN;
	String vitesse;
	
	public ThreadSolveur(String mode,MSG msg,Solveur solveur,SolveurLineaire SLN,String vitesse){
		this.mode=mode;this.msg=msg;this.solveur=solveur;this.SLN=SLN;this.vitesse=vitesse;
	}
	
	public void run(){
		if(mode.equals("SAT")){
			if(vitesse.equals("rapide")){
				String solution=solveur.verifierDivergenceRapide();
				Main.ecrireSolutionDiv(solution,msg,solveur.getTempsRapide());
				return;
			}
			long time1=System.nanoTime();
			ArrayList<String> solutionsSAT=solveur.verifierDivergence();
			long time2=System.nanoTime();
			Main.ecrireSolutionDiv(solutionsSAT,msg,"SAT",(time2-time1)/1000000);
			
		}
		else{
			long time1=System.nanoTime();
			ArrayList<String> solutionsLN=SLN.verifierDivergence();
			long time2=System.nanoTime();
			
			Main.ecrireSolutionDiv(solutionsLN, msg,"LN",(time2-time1)/1000000);			
		}
	}
}
