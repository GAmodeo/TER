package fleur;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class Generateur {
	public static void genererFleur(int taille){
		try{
			File f=new File("fleur"+taille+".xml"); 
			f.createNewFile();
			
			String nl=System.getProperty("line.separator"); 
			
			FileWriter fw=new FileWriter(f);
			fw.write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>"+nl);  
			
			fw.write("<MSG>\n"+nl);
			fw.write("\t<listeMSC>"+nl);
			
			for(int i=1 ; i<=taille ; i++){
				int dest;
				if(i == taille){
					dest=1;
				}
				else
					dest=i+1;
				
				fw.write("\t\t<MSC>"+nl);
				
				fw.write("\t\t\t<id>"+i+"</id>"+nl);
				
				fw.write("\t\t\t<listeActions>"+nl);
				
				fw.write("\t\t\t\t<action type=\"emission\">"+nl);
				
				fw.write("\t\t\t\t\t<source>"+i+"</source>"+nl);
				fw.write("\t\t\t\t\t<destination>"+dest+"</destination>"+nl);
				
				fw.write("\t\t\t\t</action>"+nl);
				
				fw.write("\t\t\t\t<action type=\"reception\">"+nl);
				
				fw.write("\t\t\t\t\t<source>"+dest+"</source>"+nl);
				fw.write("\t\t\t\t\t<destination>"+i+"</destination>"+nl);
				
				fw.write("\t\t\t\t</action>"+nl);
				
				fw.write("\t\t\t</listeActions>"+nl);
				
				fw.write("\t\t</MSC>"+nl);
			}

			
			
			fw.write("\t</listeMSC>"+nl);
			
			fw.write("\t<listeArcs>"+nl);
			for(int i=1 ; i<=taille ; i++){
				if(i !=1){
				fw.write("\t\t<arc>"+nl);
				
				fw.write("\t\t\t<domaine>"+i+"</domaine>"+nl);
				fw.write("\t\t\t<codomaine>"+1+"</codomaine>"+nl);
			
				fw.write("\t\t</arc>"+nl);
				}
				for(int j=i+1;j<=taille;j++){
					fw.write("\t\t<arc>"+nl);
				
					fw.write("\t\t\t<domaine>"+i+"</domaine>"+nl);
					fw.write("\t\t\t<codomaine>"+j+"</codomaine>"+nl);
				
					fw.write("\t\t</arc>"+nl);
				}				
			}
			fw.write("\t</listeArcs>"+nl);
			
			fw.write("</MSG>"+nl);
			
			fw.close(); // fermer le fichier � la fin des traitements
		} catch (Exception e) {}
	}
}

