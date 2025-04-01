package com.daw.services.exceptions;

public class TareaNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TareaNotFoundException(String message) {
		super(message);
	}

}
