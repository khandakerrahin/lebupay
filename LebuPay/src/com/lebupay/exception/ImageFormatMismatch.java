package com.lebupay.exception;

public class ImageFormatMismatch extends Exception {

	private static final long serialVersionUID = -5818677341225133418L;

	private String name;

	public ImageFormatMismatch(String name) {
		super(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
