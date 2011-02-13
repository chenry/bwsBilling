/**
 * File Name: OpenInvoicesNotAvailableException.java
 * Date: Sep 25, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.exception;


public class OpenInvoicesNotAvailableException extends Exception {

    /**
     * 
     */
    public OpenInvoicesNotAvailableException() {
        super();
    }

    /**
     * @param arg0
     */
    public OpenInvoicesNotAvailableException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public OpenInvoicesNotAvailableException(Throwable arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public OpenInvoicesNotAvailableException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

}
