package com.lebupay.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is FormExceptions Class extends Exception Class, is a custom exception class is used to Handle Forms Exceptions and validate forms input.
 * @author Java Team
 *
 */
public class FormExceptions extends Exception {

	private static final long serialVersionUID = -2792746956950525730L;

	private Map<String, Exception> exceptions = new HashMap<String, Exception>();
	
	private List<String> exe = new ArrayList<String>();

	/**
	 * This is constructor for FormException class.
	 * @param exceptions
	 */
	public FormExceptions(Map<String, Exception> exceptions) {
		super();
		this.exceptions = exceptions;
	}
	
	/**
	 *  This is constructor for FormException class.
	 * @param exe
	 */
	public FormExceptions(List<String> exe) {
		super();
		this.exe = exe;
	}

	/**
	 * This method is used to get Exception.
	 * @return Map
	 */
	public Map<String, Exception> getExceptions() {
		return exceptions;
	}

	/**
	 * This method is used to set Exception.
	 * @param exceptions
	 */
	public void setExceptions(Map<String, Exception> exceptions) {
		this.exceptions = exceptions;
	}

	public List<String> getExe() {
		return exe;
	}

	public void setExe(List<String> exe) {
		this.exe = exe;
	}

}
