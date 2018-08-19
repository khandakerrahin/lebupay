package com.lebupay.exception;

/**
 * This is MailSendException Class , is a custom exception class is used to Handle Mail Exceptions.
 * @author Java Team
 *
 */
public class MailSendException extends Exception {

	private static final long serialVersionUID = 1L;

	private String name;

	public MailSendException(String name) {
		super(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
