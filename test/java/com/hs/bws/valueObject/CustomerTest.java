/**
 * File Name: CustomerTest.java
 * Date: Jul 25, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.valueObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.hs.bws.util.CustomerNameComparator;


public class CustomerTest extends TestCase {
    Logger logger = Logger.getLogger(CustomerTest.class);
    Integer id = new Integer(1);
    String address1 = "2440 Forest Grove";
    String address2 = "Apt 12C";
    String city = "Wyoming";
    String emailAddress = "chenry@gfs.com";
    String faxNbr = "N/A";
    String firstName = "Carlus";
    String homePhoneNbr = "(616) 534-1091";
    String lastName = "Henry";
    String state = "MI";
    String workPhoneNbr = "(616) 717-7819";
    String zipCode = "49509";
    Customer c1 = new Customer();

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        c1.setId(id);
        c1.setAddress1(address1);
        c1.setAddress2(address2);
        c1.setCity(city);
        c1.setFirstName(firstName);
        c1.setLastName(lastName);
        c1.setState(state);
        c1.setZipCode(zipCode);
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testGetFlatFileString() {
        
        String result = c1.getFlatFileString();
        String expectedResult = id + Customer.FLAT_FILE_DELIM + 
        lastName + Customer.FLAT_FILE_DELIM + 
        firstName + Customer.FLAT_FILE_DELIM + 
        address1 + Customer.FLAT_FILE_DELIM + 
        address2 + Customer.FLAT_FILE_DELIM + 
        city + Customer.FLAT_FILE_DELIM + 
        state + Customer.FLAT_FILE_DELIM + 
        zipCode + Customer.FLAT_FILE_DELIM;
        
        assertEquals(expectedResult, result);
    }
    
    public void testEquals() {
        Customer customer1 = new Customer();
        customer1.setId(new Integer(33));
        customer1.setFirstName("Carlus");
        customer1.setLastName("Henry");
        customer1.setAddress1("2440 Forest Grove");
        
        Customer customer2 = new Customer();
        customer2.setId(new Integer(33));
        
        assertEquals(customer1, customer2);
        
        customer2.setId(new Integer(34));
        assertFalse(customer2.equals(customer1));
        customer2.setFirstName(customer1.getFirstName());
        customer2.setLastName(customer1.getLastName());
        customer2.setAddress1(customer1.getAddress1());
        
        assertEquals(customer1, customer2);
    }
    public void testBuildCustomerFromFileString() {
        String fileString = c1.getFlatFileString();
        Customer c2 = Customer.buildCustomerFromFileString(fileString);
        assertEquals(c1, c2);
    }
    
    public void testOrderCustomerByName() {
        List customerList = new ArrayList();
        Customer cust1 = new Customer();
        cust1.setLastName("Henry");
        cust1.setFirstName("Carlus");
        
        Customer cust2 = new Customer();
        cust2.setLastName("Henry");
        cust2.setFirstName("Stephanie");
        
        Customer cust3 = new Customer();
        cust3.setLastName("Fochtman");
        cust3.setFirstName("Kristy");
        
        Customer cust4 = new Customer();
        cust4.setCompanyName("Dave Ramsey Show");
        
        customerList.add(cust3);
        customerList.add(cust1);
        customerList.add(cust2);
        customerList.add(cust4);
        
        Collections.sort(customerList, new CustomerNameComparator());
        Customer c1 = (Customer) customerList.get(0);
        Customer c2 = (Customer) customerList.get(1);
        Customer c3 = (Customer) customerList.get(2);
        Customer c4 = (Customer) customerList.get(3);

        assertEquals("Dave Ramsey Show", c1.getFormalName());
        assertEquals("Fochtman, Kristy", c2.getFormalName());
        assertEquals("Henry, Carlus", c3.getFormalName());
        assertEquals("Henry, Stephanie", c4.getFormalName());
        
    }
    
    /**
     * Tests the ordering of the customer by formal name
     */
    public void testGetFormalName() {
        Customer cust1 = new Customer();
        cust1.setLastName("Henry");
        cust1.setFirstName("Carlus");
        
        Customer cust2 = new Customer();
        cust2.setLastName("Henry");
        cust2.setFirstName("Stephanie");
        
        Customer cust3 = new Customer();
        cust3.setLastName("Fochtman");
        cust3.setFirstName("Kristy");
        
        Customer cust4 = new Customer();
        cust4.setCompanyName("Dave Ramsey Show");
        
        assertEquals("Henry, Carlus", cust1.getFormalName());
        assertEquals("Henry, Stephanie", cust2.getFormalName());
        assertEquals("Fochtman, Kristy", cust3.getFormalName());
        assertEquals("Dave Ramsey Show", cust4.getFormalName());
    }
    
}

