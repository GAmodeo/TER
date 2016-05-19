package main;

public class Arc {
	int numero;
	private MSC domaine;
	private MSC codomaine;
	
	public Arc(MSC domaine,MSC codomaine){
		this.setDomaine(domaine);
		this.setCodomaine(codomaine);
	}
	
	public MSC getDomaine() {
		return domaine;
	}
	public boolean contient(int i,int j){
		return (this.getCodomaine().contient(i, j) || this.getDomaine().contient(i, j));			
	}
	
	public void setDomaine(MSC domaine) {
		this.domaine = domaine;
	}
	public MSC getCodomaine() {
		return codomaine;
	}
	public void setCodomaine(MSC codomaine) {
		this.codomaine = codomaine;
	}
	public void show(){
		System.out.println("arc qui relie "+domaine.getId()+"a "+codomaine.getId()+" numero SAT "+this.getNumero());
	}
	public void setNumero(int numero){
		this.numero=numero;
	}
	public int getNumero(){
		return this.numero;
	}
}
