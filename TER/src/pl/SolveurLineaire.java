package pl;

import java.util.ArrayList;

import org.gnu.glpk.GLPK;
import org.gnu.glpk.GLPKConstants;
import org.gnu.glpk.SWIGTYPE_p_double;
import org.gnu.glpk.SWIGTYPE_p_int;
import org.gnu.glpk.glp_iocp;
import org.gnu.glpk.glp_prob;

import main.Arc;
import main.Instance;
import main.MSC;
import main.MSG;

public class SolveurLineaire {
	private MSG msg;
	private int compteurCDivergents;
	private ArrayList<String> modelesDivergence;
	private long tempsDiv;
	
	public SolveurLineaire(MSG msg){
		this.setMsg(msg);
		this.setCompteurCDivergents(0);
		this.modelesDivergence=new ArrayList<String>();
		this.tempsDiv=0;
		GLPK.glp_term_out(GLPK.GLP_OFF);
	}
	
	public ArrayList<String> verifierDivergence(){
		
		glp_prob lp;
        SWIGTYPE_p_int ind;
        SWIGTYPE_p_double val;


        // Create problem
        lp = GLPK.glp_create_prob();
       // System.out.println("Problem created");
        GLPK.glp_set_prob_name(lp, "myProblem");

        // Define columns
        definirColonnes(lp);

        // Create constraints

        // Allocate memory 
        int nbVars=msg.getNbArcs()+msg.getNbInstances()+1;
        //+1 car on commence a l'indice 1
        ind = GLPK.new_intArray(nbVars+1);
        val = GLPK.new_doubleArray(nbVars+1);
            

        definirLignes(ind,lp,val);
            
         
        //maintenant on a la bas du pb on le defini pour chaque canal
        for(Instance i : msg.getInstances()){
    		for(Instance j : msg.getInstances()){
    			if(i.getId()==j.getId())
    				continue;
    				glp_prob nouveauLp = GLPK.glp_create_prob();
    				//ainsi on garde la premiere partie du pb et on en cree un nouveau
    				//pour chaque couple 
    				GLPK.glp_copy_prob(nouveauLp, lp, 0);
    				
    				  // Define objective
    	            definirNouveauLp(nouveauLp,i,j);
    				}
    		} 

    		this.tempsDiv=this.tempsDiv/1000000;
    		return modelesDivergence;
	}
	


	private void definirNouveauLp(glp_prob nouveauLp,Instance i,Instance j) {
		  //GLPK.glp_write_lp(nouveauLp, null,"solutionLP2");
		int nbVars=msg.getNbArcs()+msg.getNbInstances()+1;
        
		SWIGTYPE_p_int ind;
        SWIGTYPE_p_double val;
        ind = GLPK.new_intArray(nbVars+1);
        val = GLPK.new_doubleArray(nbVars+1);
		
		definirContientActionij(ind,nouveauLp,val,i,j);
		definirObjectif(nouveauLp);
		
		
	//  solve model
		glp_iocp iocp;
		int ret;
		
	    iocp = new glp_iocp();
	    GLPK.glp_init_iocp(iocp);
	    iocp.setPresolve(GLPKConstants.GLP_ON);
	    GLPK.glp_write_lp(nouveauLp, null, "solutionLP");
	    
	    long t1=System.nanoTime();
	    ret = GLPK.glp_intopt(nouveauLp, iocp);
	    long t2=System.nanoTime();
	    this.tempsDiv=this.tempsDiv+(t2-t1);
	    
	//  Retrieve solution
	    if (ret == 0) {
	      write_mip_solution(nouveauLp,i,j);
	    }
	    else {
	      //System.out.println("The problem could not be solved"+ret);
	    };
	    
	    // free memory
	    GLPK.glp_delete_prob(nouveauLp);
	  

         // Free memory
         GLPK.delete_intArray(ind);
         GLPK.delete_doubleArray(val);
         
         
         /*if(ret !=0)
        	 System.exit(ret);*/

	}

	private void definirObjectif(glp_prob lp) {
        GLPK.glp_set_obj_name(lp, "zOBJ");
        GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MIN);     
        for(Arc a : msg.getArcs())
            GLPK.glp_set_obj_coef(lp, a.getNumero(), 1);        		
	}

	private void definirLignes(SWIGTYPE_p_int ind, glp_prob lp, SWIGTYPE_p_double val) {
		
		//on ajoute au probleme les lignes qui definissent un cycle
		definirCycle(ind,lp,val);
		
		propagation(ind,lp,val);
		
		//dit que le cycle doit contenir une action de i vers j
		//s'occupe aussi de specifier que vj=1 et vi=0
				
		
		
		
	}

	private void propagation(SWIGTYPE_p_int ind, glp_prob lp, SWIGTYPE_p_double val) {
		for(Instance s : msg.getInstances()){
			for(Instance r : msg.getInstances()){
				if(s.getId()==r.getId())
					continue;
				
				//pour chaque couple d'instances :
				//on verifie sur une arrete contient une action s->r
				for(Arc a : msg.getArcs()){
					if(  ( a.getCodomaine().contient( s.getId(), r.getId() ) ) || ( a.getCodomaine().contient( s.getId(), r.getId()) ) ){
						// Create rows
						int indice=GLPK.glp_add_rows(lp, 1);
				        // Set row details
				        GLPK.glp_set_row_name(lp, indice, "c"+indice);
				        //va + vs - vr < 1
				        GLPK.glp_set_row_bnds(lp, indice, GLPKConstants.GLP_UP, 1, 1);
				        //va
				        GLPK.intArray_setitem(ind, 1, a.getNumero());
				        GLPK.doubleArray_setitem(val, 1, 1);
				        //vs
				        GLPK.intArray_setitem(ind, 2, s.getNumeroSAT());
				        GLPK.doubleArray_setitem(val, 2, 1);
				        //vr
				        GLPK.intArray_setitem(ind, 3, r.getNumeroSAT());
				        GLPK.doubleArray_setitem(val, 3, -1);
				        
				        GLPK.glp_set_mat_row(lp, indice, 3, ind, val);
					}
				}
			}
		}
		
	}

	private void definirContientActionij(SWIGTYPE_p_int ind, glp_prob lp, SWIGTYPE_p_double val, Instance i,
			Instance j) {
		
		// Create row
        int indice=GLPK.glp_add_rows(lp, 1);
        GLPK.glp_set_row_name(lp, indice, "c"+indice);
        GLPK.glp_set_row_bnds(lp, indice, GLPKConstants.GLP_LO, 0.5,0.5);
        
        int indiceArray=1;
		for(Arc arcCourant : this.msg.getArcs()){
			if(!arcCourant.getCodomaine().contient(i.getId(), j.getId()))
				continue;
			
		     GLPK.intArray_setitem(ind, indiceArray, numColonne(arcCourant));
		     GLPK.doubleArray_setitem(val, indiceArray, 1);
		     indiceArray++;
	        
		}
		//num de la ligne =indice qui contient indice Array elements
		if(indiceArray>1){
			GLPK.glp_set_mat_row(lp, indice, indiceArray-1, ind, val);
		}
        
        
        //de plus on a vj=1 et vi=0
        
        //vj=1
        indice=GLPK.glp_add_rows(lp, 1);
        GLPK.glp_set_row_name(lp, indice, "c"+indice);
        GLPK.glp_set_row_bnds(lp, indice, GLPKConstants.GLP_FX, 1,1);
	    GLPK.intArray_setitem(ind, 1, numColonne(j));
	    GLPK.doubleArray_setitem(val, indiceArray, 1);
	    GLPK.glp_set_mat_row(lp, indice, 1, ind, val);
	   
	    //vi=0
        indice=GLPK.glp_add_rows(lp, 1);
        GLPK.glp_set_row_name(lp, indice, "c"+indice);
        GLPK.glp_set_row_bnds(lp, indice, GLPKConstants.GLP_FX, 0,0);
	    GLPK.intArray_setitem(ind, 1, numColonne(i));
	    GLPK.doubleArray_setitem(val, indiceArray, 1);
	    GLPK.glp_set_mat_row(lp, indice, 1, ind, val);
		
	}

	private void definirCycle(SWIGTYPE_p_int ind, glp_prob lp, SWIGTYPE_p_double val) {
		for(MSC etat : this.msg.getEtats()){
			// Create rows
	        int indice=GLPK.glp_add_rows(lp, 1);
	
	        // le nom de la ligne plus le fait qu'on ait sum va - sumva =0
	        GLPK.glp_set_row_name(lp, indice, "c"+indice);
	        GLPK.glp_set_row_bnds(lp, indice, GLPKConstants.GLP_FX, 0,0);
	        
	        //le numero de l'indice qu'on place actuellement
	        int indiceArray=1;
	        for(Arc arcCourant : etat.getDomaines()){
		        GLPK.intArray_setitem(ind, indiceArray, numColonne(arcCourant));
		        GLPK.doubleArray_setitem(val, indiceArray, 1);
		        indiceArray++;
	        }
	        for(Arc arcCourant : etat.getCodomaines()){
	        	//System.out.println("INDICE "+numColonne(arcCourant)+indiceArray);
		        GLPK.intArray_setitem(ind, indiceArray, numColonne(arcCourant));
		        GLPK.doubleArray_setitem(val, indiceArray, -1);
		        indiceArray++;
	        }
	        //num de la ligne =indice qui contient indice Array elements

	        
	        //on met indiceArray-1 pour respecter la taille effective du tab
	        GLPK.glp_set_mat_row(lp, indice, indiceArray-1, ind, val);
		}
	}

	private void definirColonnes(glp_prob lp) {
		
		//+1 car on commence a l'indice 1
		int nbColonnes=msg.getNbArcs()+msg.getNbInstances()+1;
		//System.out.println("COLONNES "+nbColonnes);
		GLPK.glp_add_cols(lp, nbColonnes);
		 
		for(Arc arc : msg.getArcs()){ 

			//le nom des va
	        GLPK.glp_set_col_name(lp, numColonne(arc), "a"+arc);
	        //continuous variables
	        GLPK.glp_set_col_kind(lp,  numColonne(arc), GLPKConstants.GLP_BV);
	        //double bounded entre 0 et 0.5
	        //GLPK.glp_set_col_bnds(lp,  numColonne(arc), GLPKConstants.GLP_DB, 0, .5);
		}
		for(Instance instance : msg.getInstances()){ 

			//le nom des va
	        GLPK.glp_set_col_name(lp, numColonne(instance) , "i"+instance);
	        //variable binaire
	        GLPK.glp_set_col_kind(lp,  numColonne(instance), GLPKConstants.GLP_BV);
	        //peut valoir 0 ou 1
	       // GLPK.glp_set_col_bnds(lp,  numColonne(instance), GLPKConstants.GLP_DB, 0, 1);
		}

		
	}
	  void write_mip_solution(glp_prob lp, Instance i, Instance j) {
		  	int in;
		    int n;
		    String sol=new String();
		    double val;
		    
		    /*name = GLPK.glp_get_obj_name(lp);
		    val  = GLPK.glp_mip_obj_val(lp);
		    System.out.print(name);
		    System.out.print(" = ");
		    System.out.println(val);*/
		    n = GLPK.glp_get_num_cols(lp);
		    
		    sol=sol.concat(i.getId()+" "+j.getId());
		    for(in=1; in < n; in++)
		    {
		    	/*
		      name = GLPK.glp_get_col_name(lp, i);
		      val  = GLPK.glp_mip_col_val(lp, i);
		      System.out.print(i);
		      System.out.print("=");
		      System.out.print(val+"  ");*/
		      
		      sol=sol.concat(" "+ GLPK.glp_mip_col_val(lp, in));
		    }
		  
		this.modelesDivergence.add(sol);
		
		/*if(GLPK.glp_get_prim_stat(lp) == GLPK.GLP_FEAS){
			System.out.println("NEVERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
		}
		System.out.println("TEST " +GLPK.glp_get_prim_stat(lp));*/
		
		this.compteurCDivergents++;
		
		 
		  //System.out.println("TEST "+line);
	  }
	

	private int numColonne(Arc arc) {
		return arc.getNumero();
	}
	private int numColonne(Instance instance) {
		return instance.getNumeroSAT();
	}

	public MSG getMsg() {
		return msg;
	}

	public void setMsg(MSG msg) {
		this.msg = msg;
	}

	public int getCompteurCDivergents() {
		return compteurCDivergents;
	}

	public void setCompteurCDivergents(int compteurCDivergents) {
		this.compteurCDivergents = compteurCDivergents;
	}

	public long getTempsDiv() {
		// TODO Auto-generated method stub
		return this.tempsDiv;
	}
}
