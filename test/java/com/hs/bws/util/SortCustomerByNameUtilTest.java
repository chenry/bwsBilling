/**
 * File Name: SortCustomerByNameUtilTest.java
 * Date: Apr 27, 2005
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import com.hs.bws.valueObject.Customer;
import com.hs.bws.valueObject.CustomerInvoice;


public class SortCustomerByNameUtilTest extends TestCase {
    Customer cust1 = null;
    Customer cust2 = null;
    Customer cust3 = null;
    Customer cust4 = null;
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        cust1 = new Customer();
        cust2 = new Customer();
        cust3 = new Customer();
        cust4 = new Customer();

        cust1.setCompanyName("ACME Furniture");
        
        cust2.setLastName("Henry");
        cust2.setFirstName("Carlus");
        
        cust3.setLastName("Krupp");
        cust3.setFirstName("Mark");

        cust4.setLastName("Krupp");
        cust4.setFirstName("Victoria");
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Constructor for SortCustomerByNameUtilTest.
     * @param arg0
     */
    public SortCustomerByNameUtilTest(String arg0) {
        super(arg0);
    }
    
    public void testSortCustomerByCustomerName() {
        List custList = new ArrayList();
        custList.add(cust2);
        custList.add(cust1);
        custList.add(cust3);
        custList.add(cust4);

        Collections.sort(custList, new SortCustomerByNameUtil());
        Customer cust = (Customer) custList.get(0);
        assertEquals(cust1.getCompanyName(), cust.getCompanyName());

        cust = (Customer) custList.get(1);
        assertEquals(cust2.getLastName(), cust.getLastName());
        
        cust = (Customer) custList.get(2);
        assertEquals(cust3.getLastName(), cust.getLastName());
        assertEquals(cust3.getFirstName(), cust.getFirstName());
        
        
        cust = (Customer) custList.get(3);
        assertEquals(cust4.getLastName(), cust.getLastName());
        assertEquals(cust4.getFirstName(), cust.getFirstName());
        
        /*
         * Make sure that the reverse order works
         */
        Collections.sort(custList, new SortCustomerByNameUtil());
        Collections.reverse(custList);
        
        cust = (Customer) custList.get(0);
        assertEquals(cust4.getLastName(), cust.getLastName());
        
        cust = (Customer) custList.get(1);
        assertEquals(cust3.getLastName(), cust.getLastName());
        assertEquals(cust3.getFirstName(), cust.getFirstName());
        
        
        cust = (Customer) custList.get(2);
        assertEquals(cust2.getLastName(), cust.getLastName());
        assertEquals(cust2.getFirstName(), cust.getFirstName());
        
        cust = (Customer) custList.get(3);
        assertEquals(cust1.getLastName(), cust.getLastName());
        assertEquals(cust1.getFirstName(), cust.getFirstName());
        
        
    }
    
    public void testSortCustInvByCustomerName() {
        List custInvList = new ArrayList();
        
        CustomerInvoice custInv1 = new CustomerInvoice();
        custInv1.setId(new Integer(1));
        custInv1.setCustomer(cust1);

        CustomerInvoice custInv2 = new CustomerInvoice();
        custInv2.setId(new Integer(2));
        custInv2.setCustomer(cust2);
        
        CustomerInvoice custInv3 = new CustomerInvoice();
        custInv3.setId(new Integer(3));
        custInv3.setCustomer(cust3);
        
        CustomerInvoice custInv4 = new CustomerInvoice();
        custInv4.setCustomer(cust4);
        custInv4.setId(new Integer(4));
        
        custInvList.add(custInv2);
        custInvList.add(custInv4);
        custInvList.add(custInv1);
        custInvList.add(custInv3);
        
        Collections.sort(custInvList, new SortCustomerByNameUtil());
        
        CustomerInvoice custInv = null;
        custInv = (CustomerInvoice) custInvList.get(0);
        assertEquals(custInv1.getId(), custInv.getId());
        
        custInv = (CustomerInvoice) custInvList.get(1);
        assertEquals(custInv2.getId(), custInv.getId());
        
        custInv = (CustomerInvoice) custInvList.get(2);
        assertEquals(custInv3.getId(), custInv.getId());
        
        custInv = (CustomerInvoice) custInvList.get(3);
        assertEquals(custInv4.getId(), custInv.getId());
        /*
         * Make sure that the reverse works
         */
        Collections.sort(custInvList, new SortCustomerByNameUtil());
        Collections.reverse(custInvList);
        
        custInv = (CustomerInvoice) custInvList.get(0);
        assertEquals(custInv4.getId(), custInv.getId());
        
        custInv = (CustomerInvoice) custInvList.get(1);
        assertEquals(custInv3.getId(), custInv.getId());
        
        custInv = (CustomerInvoice) custInvList.get(2);
        assertEquals(custInv2.getId(), custInv.getId());
        
        custInv = (CustomerInvoice) custInvList.get(3);
        assertEquals(custInv1.getId(), custInv.getId());
        
    }

}
