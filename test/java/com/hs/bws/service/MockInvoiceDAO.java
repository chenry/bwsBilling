/**
 * File Name: MockInvoiceDAO.java
 * Date: Aug 18, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hs.bws.dao.InvoiceDAO;
import com.hs.bws.valueObject.CustomerInvoice;


public class MockInvoiceDAO extends InvoiceDAO {
    Logger logger = Logger.getLogger(MockInvoiceDAO.class);
    List addInvoiceList = new ArrayList();
    /**
     * 
     */
    public MockInvoiceDAO() {
        super();
    }
    
    public void createCustomerInvoice(Connection conn, CustomerInvoice customerInvoice) {
        addInvoiceList.add(customerInvoice);
    }
    
    

    /* (non-Javadoc)
     * @see com.hs.bws.dao.InvoiceDAO#updateCustomerInvoice(java.sql.Connection, com.hs.bws.valueObject.CustomerInvoice)
     */
    public void updateCustomerInvoice(Connection conn,
            CustomerInvoice customerInvoice) throws SQLException {
        logger.info("We really didn't call the DAO");
    }
    
    
    /* (non-Javadoc)
     * @see com.hs.bws.dao.InvoiceDAO#closeInvoice(java.sql.Connection, com.hs.bws.valueObject.CustomerInvoice)
     */
    public void closeInvoice(Connection conn, CustomerInvoice customerInvoice)
            throws SQLException {
        logger.info("Do nothing.");
    }
    
    
    
    /* (non-Javadoc)
     * @see com.hs.bws.dao.InvoiceDAO#updateInvoicePaidAmt(java.sql.Connection, com.hs.bws.valueObject.CustomerInvoice)
     */
    public void updateInvoicePaidAmt(Connection conn,
            CustomerInvoice customerInvoice) throws SQLException {
        	logger.info("Do nothing");
    }
    
    public void addPayment(Connection conn, CustomerInvoice customerInvoice) 
    	throws SQLException {
    	logger.info("Do nothing");
    }
}
