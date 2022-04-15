package com.algorithmicintuition.tech.vendingmachine.types;

public enum Coin {

	PENNY(.01), NICKLE(.05), DIME(.10), QUARTER(.25);

	private double value;

	private Coin(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

}