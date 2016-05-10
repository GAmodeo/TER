package main;

public class Instance {
	private int id;
	private int numeroSAT;
	
	public Instance(int id){
		this.setId(id);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNumeroSAT() {
		return numeroSAT;
	}
	public void setNumeroSAT(int numeroSAT) {
		this.numeroSAT = numeroSAT;
	}
	
}
