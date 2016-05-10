package main;

import java.util.ArrayList;
import java.util.HashMap;

public class MSG {
	
	private int nbArcs;
	private int nbEtats;
	private int nbInstances;
	private ArrayList<MSC> etats;
	private ArrayList<Arc> arcs; 
	private ArrayList<Instance> instances;
	private HashMap<Integer,Instance> idInstances;
	
	
	public MSG(ArrayList<MSC> etats){
		this.setEtats(etats);
		this.arcs=new ArrayList<Arc>();
		this.instances=new  ArrayList<Instance>();
		this.setNbInstances(0);
		this.idInstances=new HashMap<Integer,Instance>();
	}

	
	public ArrayList<MSC> getEtats() {
		return etats;
	}

	public void setEtats(ArrayList<MSC> etats) {
		this.etats = etats;
	}

	public ArrayList<Arc> getArcs() {
		return arcs;
	}

	public void setArcs(ArrayList<Arc> arcs) {
		this.arcs = arcs;
	}


	public MSC trouverEtat(String id) {
		for(int i=0;i< etats.size(); i++){
			MSC MscCourant=etats.get(i);
			if(MscCourant.getId().equals(id)){
				return MscCourant;
			}
		}
		return null;
	}
	public void show(){
		for(int i=0;i< etats.size(); i++){
			etats.get(i).show();
		}
		for(int i=0;i< arcs.size(); i++){
			arcs.get(i).show();
		}
		System.out.println("instances presentes :");
		for(Instance i :this.getInstances()){
			System.out.println("instance id :"+ i.getId()+" numero sat "+i.getNumeroSAT());
		}
	}
	public void numeroter(){
		this.setNbEtats(this.getEtats().size());
		this.setNbArcs(this.getArcs().size());
		
		for(int i=0;i<this.getNbArcs();i++){
			this.getArcs().get(i).setNumero(i+1);
		}
		for(int i=0;i<this.getNbEtats();i++){
			this.getEtats().get(i).setNumero(i+1);
		}
		
		//on parcourt les etats
		for(int i=0;i<this.getNbEtats();i++){
			
			MSC MSCCourant=this.getEtats().get(i);
			ArrayList<Action> actions=MSCCourant.getActions();

			//on repertorie les instances presentes sur l'etat i
			for(int j=0;j<MSCCourant.getActions().size();j++){
						
				Action action=actions.get(j);
				int destination=action.getDestination();
				int source=action.getSource();
				
				if(idInstances.get(destination) == null){
					
					Instance instanceCourante=new Instance(destination);
					instanceCourante.setNumeroSAT(nbArcs+this.getNbInstances()+1);
					idInstances.put(destination,instanceCourante);
					this.instances.add(instanceCourante);
					this.setNbInstances(this.getNbInstances()+1);
				}
				if(idInstances.get(source) == null){
					
					Instance instanceCourante=new Instance(source);
					instanceCourante.setNumeroSAT(nbArcs+this.getNbInstances()+1);
					idInstances.put(source,instanceCourante);
					this.instances.add(instanceCourante);
					this.setNbInstances(this.getNbInstances()+1);
				}
			}
					
			
		}
		
	}


	public int getNbArcs() {
		return nbArcs;
	}


	public void setNbArcs(int nbArcs) {
		this.nbArcs = nbArcs;
	}


	public int getNbEtats() {
		return nbEtats;
	}


	public void setNbEtats(int nbEtats) {
		this.nbEtats = nbEtats;
	}
	public int numArc(int a){
		return this.getArcs().get(a).getNumero();
	}
	public ArrayList<Instance> getInstances()
	{
		return this.instances;
	}


	public int getNbInstances() {
		return nbInstances;
	}


	public void setNbInstances(int nbInstances) {
		this.nbInstances = nbInstances;
	}
	
	//cette methode retourne le num de clause sat de l'instance d'id "id"

	//on trouve une instance a partir de son id et on retourne l'objet asocie grace a la hashmap
	public Instance getInstanceDepuisId(int id){
		return this.idInstances.get(id);
	}

	//ainsi chaque etat saura de quel arc il est le domaine et codomaine
	//utile pour la PL
	public void listerDomaines(ArrayList<Arc> listeArcs) {
		for(Arc arc : listeArcs){
			MSC domaine=arc.getDomaine();
			domaine.addDomaine(arc);
			
			MSC codomaine=arc.getCodomaine();
			codomaine.addCodomaine(arc);
		}
		
	}

}
