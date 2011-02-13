/**
 * File Name: BWSGenericException.java
 * Date: Aug 26, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.exception;

import org.apache.log4j.Logger;


public class BWSGenericException extends Exception{
    Logger logger = Logger.getLogger(BWSGenericException.class);
    /**
     * 
     */
    public BWSGenericException() {
        super();
    }
    
    /**
     * @param arg0
     */
    public BWSGenericException(String arg0) {
        super(arg0);
        logger.error("Exception Occurred.  Message = " + arg0);
    }

    /**
     * @param arg0
     */
    public BWSGenericException(Throwable arg0) {
        super(arg0);
        logger.error("Exception Occurred: ", arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public BWSGenericException(String arg0, Throwable arg1) {
        super(arg0, arg1);
        logger.error("Exception Occurred: ", arg1);
    }

    
    
}
