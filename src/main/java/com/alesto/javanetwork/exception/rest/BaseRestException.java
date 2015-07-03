package com.alesto.javanetwork.exception.rest;

public class BaseRestException extends Exception {

	/*
	 * ======================================================================
	 * ----- Constants
	 * ======================================================================
	 */

    private static final long serialVersionUID = 1L;
	
	/*
	 * ======================================================================
	 * ----- Fields
	 * ======================================================================
	 */

    private String logref = null;
	
	/*
	 * ======================================================================
	 * ----- Constructors
	 * ======================================================================
	 */

    public BaseRestException(String logref, String message) {
        super(message);

        this.logref = logref;
    }
	
	/*
	 * ======================================================================
	 * ----- Primitive accessors
	 * ======================================================================
	 */

    public String getLogref() {
        return logref;
    }

}
