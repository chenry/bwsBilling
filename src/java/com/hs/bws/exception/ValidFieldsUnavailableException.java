/**
 * File Name: ValidFieldsUnavailableException.java
 * Date: Aug 26, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.exception;


public class ValidFieldsUnavailableException extends Exception {

    /**
     * 
     */
    public ValidFieldsUnavailableException() {
        super();
    }

    /**
     * @param arg0
     */
    public ValidFieldsUnavailableException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public ValidFieldsUnavailableException(Throwable arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public ValidFieldsUnavailableException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

}
