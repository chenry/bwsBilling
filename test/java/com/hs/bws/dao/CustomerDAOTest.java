/**
 * File Name: CustomerDAOTest.java Date: Jul 26, 2004 Project Name:
 * bwsBillingSystem Description:
 */
package com.hs.bws.dao;

import java.sql.Connection;
import java.util.List;

import junit.framework.TestCase;

import com.hs.bws.util.DatabaseUtil;
import com.hs.bws.valueObject.Customer;

public class CustomerDAOTest extends TestCase {
    CustomerDAO customerDao = null;

    Customer stephCustomer = null;
    
    Customer existingCustomer = null;

    List customerList = null;

    Connection conn = null;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        customerDao = new CustomerDAO();

        conn = DatabaseUtil.getInstance().getConnection();
        Customer cust = new Customer(); 
        
        customerList = customerDao.getMatchingCustomers(conn,cust);
        existingCustomer = (Customer) customerList.get(0);

        stephCustomer = new Customer();
        stephCustomer.setLastName("Henry");
        stephCustomer.setFirstName("Stephanie");
        stephCustomer.setAddress1("2440 Forest Grove");
        stephCustomer.setCity("Wyoming");
        stephCustomer.setState("MI");
        stephCustomer.setZipCode("49509");
	
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();

        stephCustomer = null;
        existingCustomer = null;
        customerDao = null;
    }

    public void testGetCustomerListWithConnection() {
        List customerList = null;
        Customer cust = new Customer();
        try {
            customerList = customerDao.getMatchingCustomers(conn, cust);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertNotNull(customerList);
        assertTrue(customerList.size() > 0);
    }
        
    public void testGetCustomersByTemplateCustomer(){
    	List customerList = null;
    	Customer templateCustomer = null;
    	templateCustomer = new Customer();
    	templateCustomer.setLastName(existingCustomer.getLastName());
    	
    	try{
    		customerList = customerDao.getMatchingCustomers(conn,templateCustomer);
    	}catch(Exception e){
    		fail(e.getMessage());
    	}
    	assertNotNull(customerList);
    	assertTrue(customerList.size() > 0);
    		
    	}
    
}