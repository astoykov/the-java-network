package com.alesto.javanetwork.exception.rest;

public class ResourceNotFoundException extends BaseRestException {

	/*
	 * ======================================================================
	 * ----- Constants
	 * ======================================================================
	 */

    private static final long serialVersionUID = 1L;
	
	/*
	 * ======================================================================
	 * ----- Constructors
	 * ======================================================================
	 */

    public ResourceNotFoundException(String logref, String message) {
        super(logref, message);
    }

}
