/**
 * File Name: ConnectionNotAvailableException.java
 * Date: Jul 31, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.exception;

import org.apache.log4j.Logger;


public class ConnectionNotAvailableException extends Exception {
    Logger logger = Logger.getLogger(ConnectionNotAvailableException.class);
    /**
     * 
     */
    public ConnectionNotAvailableException() {
        super();
    }

    /**
     * @param arg0
     */
    public ConnectionNotAvailableException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public ConnectionNotAvailableException(Throwable arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public ConnectionNotAvailableException(String arg0, Throwable arg1) {
        logger.error("Error Occurred: ", arg1);
    }

}
