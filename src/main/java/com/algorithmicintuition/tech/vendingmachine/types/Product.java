package com.algorithmicintuition.tech.vendingmachine.types;



public class Product {
	
	private int id;
	private double costInPennys;
	private String name;
	private int inventoryCount;
	
	public Product(int id, String name, double costInPennys, int inventoryCount) {
		super();
		this.id = id;
		this.costInPennys = costInPennys;
		this.name = name;
		this.inventoryCount = inventoryCount;
	}
	
	
	public void setInventoryCount(int inventoryCount) {
		this.inventoryCount = inventoryCount;
	}


	public int getId() {
		return this.id;
	}
	
	public int getInventoryCount() {
		return inventoryCount;
	}
	
	public double getCostInPennys() {
		return costInPennys;
	}
	
	public String getName() {
		return name;
	}


	public String toJson() {
		return "{ id: " + this.id + ", " +
				"name: \"" + this.name + "\", " +
				"costInPennys:" + this.costInPennys + ", " +
				"inventoryCount: " + this.inventoryCount + " }";
	}
	
	

}
