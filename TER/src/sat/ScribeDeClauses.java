package sat;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class ScribeDeClauses {
	
	
	public static void ecrireClauses(String nomFichier,ArrayList<ArrayList<Integer>> clauses,int nbVariables,int nbClauses){
		try{
			File f=new File(nomFichier); 
			f.createNewFile();
			
			FileWriter fw=new FileWriter(f);
			fw.write("c "+nomFichier+"\n"+"c"+"\n");  // écrire une ligne dans le fichier resultat.txt
			
			fw.write("p cnf "+nbVariables+" "+nbClauses+"\n");
			
			for(ArrayList<Integer> liste : clauses){
				for(int variable : liste){
					fw.write(variable+" ");
				}
				fw.write("0\n");
			}
			
			fw.close(); // fermer le fichier à la fin des traitements
		} catch (Exception e) {}
	}
}

