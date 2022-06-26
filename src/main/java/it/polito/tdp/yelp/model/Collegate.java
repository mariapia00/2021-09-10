package it.polito.tdp.yelp.model;

public class Collegate {

	private Business b1;
	private Business b2;
	private double weight;
	public Collegate(Business b1, Business b2, double weight) {
		super();
		this.b1 = b1;
		this.b2 = b2;
		this.weight = weight;
	}
	public Business getB1() {
		return b1;
	}
	public void setB1(Business b1) {
		this.b1 = b1;
	}
	public Business getB2() {
		return b2;
	}
	public void setB2(Business b2) {
		this.b2 = b2;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
}
