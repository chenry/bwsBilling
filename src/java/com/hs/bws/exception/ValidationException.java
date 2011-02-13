/*
 * Created on Dec 4, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.hs.bws.exception;

/**
 * @author Compaq_Owner
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ValidationException extends Exception {
	String message = "Unknown Exception occurred.";
	/**
	 * @param string
	 */
	public ValidationException(String string) {
		message = string;
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return message;
	}
}
