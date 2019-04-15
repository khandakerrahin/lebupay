package com.lebupay.model;

public class IssuerNumberModel extends CommonModel{

	private int length;
	private boolean luhn;
	
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public boolean isLuhn() {
		return luhn;
	}
	public void setLuhn(boolean luhn) {
		this.luhn = luhn;
	}
	
}
