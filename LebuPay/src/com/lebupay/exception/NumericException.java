package com.lebupay.exception;

/**
 * This is NumericException Class , is a custom exception class is used to validate Numeric values.
 * @author Java Team
 *
 */
public class NumericException extends Exception {

	private static final long serialVersionUID = 1L;

	private String name;

	public NumericException(String name) {
		super(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
