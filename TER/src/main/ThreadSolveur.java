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
	
	public ThreadSolveur(String mode,MSG msg,Solveur solveur,SolveurLineaire SLN){
		this.mode=mode;this.msg=msg;this.solveur=solveur;this.SLN=SLN;
	}
	//selon le mode on apelle la methode verifierDivergence du solveur sat ou du solveur en PLNE
	public void run(){
		if(mode.equals("SAT")){
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
