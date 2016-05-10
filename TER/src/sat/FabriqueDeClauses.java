package sat;

import java.util.ArrayList;


import main.*;

public class FabriqueDeClauses {

	private MSG msg;
	public FabriqueDeClauses(MSG msg){
		this.msg=msg;
	}
	public ArrayList<ArrayList<Integer>> fabriquerCycles(MSG msg) {
		
		ArrayList<ArrayList<Integer>> cycle=new ArrayList<ArrayList<Integer>>();
		// pour tout arc un arc quitte un etat si et seulement si un arc y entre: 
		//une methode qui va mettre dans notre liste toutes les clauses pour selectionner nos cycles
		creerClausesCycles(cycle);
		
		
		
		return cycle;
	}

	private void creerClausesCycles(ArrayList<ArrayList<Integer>> cycle) {
		//un arc quitte si un arc entre
		clausesCycle1(cycle);
		//un arc sort ou entre au plus
		clausesCycle2(cycle);
	}

	private void clausesCycle1(ArrayList<ArrayList<Integer>> cycle) {

		ArrayList<Integer> listeCourante;
		
		for(int a=0;a<msg.getNbArcs();a++){
			Arc arcA=msg.getArcs().get(a);
			//on ajoute une clasue commencant par non a
			cycle.add(new ArrayList<Integer>());
			listeCourante=cycle.get(cycle.size()-1);
			listeCourante.add(-msg.numArc(a));
			
			// a -> existe a'/ dom(a') = cod (a)
			for(int aPrime=0;aPrime<msg.getNbArcs();aPrime++){
			Arc arcAPrime=msg.getArcs().get(aPrime);
				if(arcA.getCodomaine().equals(arcAPrime.getDomaine())){
					listeCourante.add(msg.numArc(aPrime));
				}
			}
		}
		for(int a=0;a<msg.getNbArcs();a++){
			Arc arcA=msg.getArcs().get(a);
			// a -> existe a'/ dom(a) = cod (a')
			cycle.add(new ArrayList<Integer>());
			listeCourante=cycle.get(cycle.size()-1);
			listeCourante.add(-msg.numArc(a));
			
			for(int aPrime=0;aPrime<msg.getNbArcs();aPrime++){
				Arc arcAPrime=msg.getArcs().get(aPrime);
				if(arcA.getDomaine().equals(arcAPrime.getCodomaine())){
					listeCourante.add(msg.numArc(aPrime));
				}
			}
		}
		// fin un arc quitte un etat si et seulement si un arc y entre
	}
	private void clausesCycle2(ArrayList<ArrayList<Integer>> cycle) {
		ArrayList<Integer> listeCourante;
		
		//on selectionne tous les couples d'arretes
		for(int a=0;a<msg.getNbArcs();a++){
			Arc arcA=msg.getArcs().get(a);
			
			for(int aPrime=a+1;aPrime<msg.getNbArcs();aPrime++){
				//on a tous les couples avec a != a'
				Arc arcAPrime=msg.getArcs().get(aPrime);
				
				//ce couple ce peut partager un domaine
				if(arcA.getDomaine().equals(arcAPrime.getDomaine())){
					cycle.add(new ArrayList<Integer>());
					listeCourante=cycle.get(cycle.size()-1);
					
					listeCourante.add(-msg.numArc(a));
					listeCourante.add(-msg.numArc(aPrime));
				}
				//ce couple ne peut partager un codomaine
				if(arcA.getCodomaine().equals(arcAPrime.getCodomaine())){
					cycle.add(new ArrayList<Integer>());
					listeCourante=cycle.get(cycle.size()-1);
					
					listeCourante.add(-msg.numArc(a));
					listeCourante.add(-msg.numArc(aPrime));
				}
			}
		}
	}
	public ArrayList<ArrayList<Integer>> fabriquerReachable(MSG msg, Instance j) {
		
		ArrayList<ArrayList<Integer>> Reachable=new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> listeCourante;
		
		//on met instance a vrai
		Reachable.add(new ArrayList<Integer>());
		listeCourante=Reachable.get(Reachable.size()-1);
		//
		listeCourante.add(j.getNumeroSAT());
		
		//maintenant on met a vrai tous les reachable 
		// pour tout a/ a contiens s->r : (a et s) -> r
		//donc on parcourt les arretes
		ArrayList<Arc> arcs=msg.getArcs();
		for(int a=0;a<arcs.size();a++){
			Arc arcCourant=arcs.get(a);
			MSC domaine=arcCourant.getDomaine();
			MSC codomaine=arcCourant.getCodomaine();
			
			//maintenant on parcourt les emissions/reception contenues dans les domaines et codomaine
			clausesReachable1(Reachable,arcCourant,domaine,msg);
			clausesReachable1(Reachable,arcCourant,codomaine,msg);
		}
		
		return Reachable;
	}
	private void clausesReachable1(ArrayList<ArrayList<Integer>> reachable,Arc arc,MSC msc,MSG msg) {
		//on parcourt les action qui sont dans ce msc
		ArrayList<Action> actions = msc.getActions();
		
		ArrayList<Integer> listeCourante;
		//pour chacune on met non a ou non s ou r
		for(int action=0;action<actions.size();action++){
			int s=msc.getActions().get(action).getSource();
			int r=msc.getActions().get(action).getDestination();
			
			int numS=msg.getInstanceDepuisId(s).getNumeroSAT();
			int numR=msg.getInstanceDepuisId(r).getNumeroSAT();
			
			reachable.add(new ArrayList<Integer>());
			listeCourante=reachable.get(reachable.size()-1);
			
			listeCourante.add(-arc.getNumero());
			listeCourante.add(-numS);
			listeCourante.add(numR);
			
		}
	}
	public ArrayList<ArrayList<Integer>> fabriquerDivergence(MSG msg2, Instance j, Instance i) {
		
		
		ArrayList<ArrayList<Integer>> Divergence=new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> listeCourante;
		
		//on met i a faux
		Divergence.add(new ArrayList<Integer>());
		listeCourante=Divergence.get(Divergence.size()-1);
		//
		listeCourante.add(-i.getNumeroSAT());
		
		//on cree la derniere clause qui contient toutes les arretes a  / i->aj
		Divergence.add(new ArrayList<Integer>());
		listeCourante=Divergence.get(Divergence.size()-1);
		
		ArrayList<Arc> arcs=msg.getArcs();
		for(int a=0;a<arcs.size();a++){
			Arc arcCourant=arcs.get(a);
			MSC domaine=arcCourant.getDomaine();
			MSC codomaine=arcCourant.getCodomaine();
			
			if(domaine.contient(i.getId(),j.getId()) || codomaine.contient(i.getId(),j.getId())){
				listeCourante.add(arcCourant.getNumero());
			}
		}
		
		
		return Divergence;
	}

}
