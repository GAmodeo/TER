package main;

public class Action {
	private String type;
	private int source;
	private int destination;
	
	public Action(String type,int source,int destination){
		this.setType(type);
		this.setSource(source);
		this.setDestination(destination);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}
	public void show(){
		System.out.println("action de type : "+type+" depuis "+source+" vers "+destination);
	}
	
}
