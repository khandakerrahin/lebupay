package com.lebupay.exception;

/**
 * This is EmptyValueException class extends Exception class is custom exception class used to validate Empty String. 
 * @author Java Team
 *
 */
public class EmptyValueException extends Exception {

	private static final long serialVersionUID = 1L;

	private String name;

	/**
	 * This method is used to check Empty Value Exception.
	 * @param name
	 */
	public EmptyValueException(String name) {
		super(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
