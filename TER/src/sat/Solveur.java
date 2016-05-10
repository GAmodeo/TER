package sat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import main.Instance;
import main.MSG;

public class Solveur {
	private MSG msg;
	private FabriqueDeClauses fabriqueDeClauses;
	
	public Solveur(MSG msg){
		this.setMsg(msg);
		this.fabriqueDeClauses=new FabriqueDeClauses(this.getMsg());
	}

	public void verifierDivergence(){
		//on a nbarcs + nbInstances comme nombres de vars
		final int nbvar = msg.getNbArcs()+msg.getInstances().size();



		//maintenant on selectionne les cycles elementaires.
		
		ArrayList<ArrayList<Integer>> cycle=fabriqueDeClauses.fabriquerCycles(this.msg);
		
		

		//on va interroger SAT pour chaque instance j
		
		int nbCanauxDivergents=0;
		for(int j=0;j<msg.getInstances().size();j++){
			Instance instanceJ=msg.getInstances().get(j);
			
			ArrayList<ArrayList<Integer>> ReachableJ=fabriqueDeClauses.fabriquerReachable(this.msg,instanceJ);
			
			ArrayList<ArrayList<Integer>> concat1=(ArrayList<ArrayList<Integer>>) cycle.clone();
			concat1.addAll(ReachableJ);
		
			//maintenant pour chaque j on verifie la divergence sur i,j
			for(int i=0;i<msg.getInstances().size();i++){
				if(i == j)	continue;
				Instance instanceI=msg.getInstances().get(i);
				
				
				ArrayList<ArrayList<Integer>> DivergenceIJ=fabriqueDeClauses.fabriquerDivergence(this.msg,instanceJ,instanceI);
				ArrayList<ArrayList<Integer>> concat2=(ArrayList<ArrayList<Integer>>) concat1.clone();
				concat2.addAll(DivergenceIJ);
				
				ScribeDeClauses.ecrireClauses("output.cnf",concat2,nbvar,concat2.size());
				
				//this.show(concat2);
				if(this.verifierSAT()>0){
					nbCanauxDivergents++;
				}
			}
		}
		if(nbCanauxDivergents == 0){
			System.out.println("Solveur SAT : Pas de divergence detectee");
		}
		else{
			System.out.println("Solveur SAT : "+nbCanauxDivergents+" canaux divergents detectes");
		}

	}
	
	private void show(ArrayList<ArrayList<Integer>> cycle) {
		for(ArrayList<Integer> clause : cycle){
			System.out.print("clause ");
			for(int var : clause){
				
				System.out.print(var+" ");
			
			}
			System.out.println(" ");
		}
		
		
	}

	private int verifierSAT() {
		ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        DimacsReader reader = new DimacsReader(solver);
        // CNF filename is given on the command line 
        
        //une variable qui nous permet de compter les modeles
        int nbModeles = 0;
        try {
            IProblem problem = reader.parseInstance("output.cnf");
            if (problem.isSatisfiable()) {
                System.out.println("Satisfiable !");
                System.out.println(reader.decode(problem.model()));
                nbModeles++;
            } else {
                
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        } catch (ParseFormatException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        } catch (ContradictionException e) {
            System.out.println("Unsatisfiable (trivial)!");
        } catch (TimeoutException e) {
            System.out.println("Timeout, sorry!");      
        }
        return nbModeles;
	}




	public MSG getMsg() {
		return msg;
	}

	public void setMsg(MSG msg) {
		this.msg = msg;
	}
}
