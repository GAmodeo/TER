package main;

import java.util.ArrayList;

public class MSC {
	private ArrayList <Arc> domaines;
	private ArrayList <Arc> codomaines;
	private ArrayList<Action> actions;
	private String id;
	private int numero;
	
	public MSC(String id,ArrayList<Action> actions){
		this.id=id;
		this.setActions(actions);
		this.setDomaines(new ArrayList<Arc>());
		this.setCodomaines(new ArrayList<Arc>());
	}

	public ArrayList<Action> getActions() {
		return actions;
	}

	public void setActions(ArrayList<Action> actions) {
		this.actions = actions;
	}
	public String getId(){
		return this.id;
	}
	public void show(){
		System.out.println("MSC id: "+id);
		
		for(int i=0;i< actions.size(); i++){
			actions.get(i).show();
		}
	
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	//retourne vrai si dans les actions il y en a une telle que la soutrce est i et la dest j
	public boolean contient(int i, int j) {
		
		for(int indice=0;indice<actions.size();indice++){
			Action actionCourante=actions.get(indice);
			
			if( (actionCourante.getSource() == i) && (actionCourante.getDestination() == j) )
				return true;
		}
		
		return false;
	}

	public ArrayList <Arc> getCodomaines() {
		return codomaines;
	}

	public void setCodomaines(ArrayList <Arc> codomaines) {
		this.codomaines = codomaines;
	}

	public ArrayList <Arc> getDomaines() {
		return domaines;
	}

	public void setDomaines(ArrayList <Arc> domaines) {
		this.domaines = domaines;
	}
	public void addDomaine(Arc arc){
		this.getDomaines().add(arc);
	}
	public void addCodomaine(Arc arc){
		this.getCodomaines().add(arc);
	}
}
