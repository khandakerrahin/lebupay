package com.lebupay.exception;

/**
 * This is DBConnectionException class extends Exception class is custom exception class used to handle database Exception. 
 * @author Java Team
 *
 */
public class DBConnectionException extends Exception {

	private static final long serialVersionUID = 1L;

	private String name;

	/**
	 * This method is used to Catch DB Exception.
	 * @param name
	 */
	public DBConnectionException(String name) {
		super(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
