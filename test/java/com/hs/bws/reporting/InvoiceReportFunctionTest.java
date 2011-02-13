/**
 * File Name: InvoiceReportFunctionTest.java
 * Date: Mar 31, 2005
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.reporting;

import java.util.Date;

import junit.framework.TestCase;

import com.hs.bws.service.CustomerService;
import com.hs.bws.valueObject.Customer;
import com.hs.bws.valueObject.CustomerInvoice;


public class InvoiceReportFunctionTest extends TestCase {
    InvoiceReport ir = null;
    int secondsToWait = 5;
    CustomerInvoice custInv = null;
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ir = InvoiceReport.getInstance();
        
        // this should be a customer invoice that has invoice line items associated with it.
        custInv = new CustomerInvoice() {
            public Double getInvoiceTotal() {
                return new Double(1234567890.00);
            }
        };
        custInv.setId(new Integer(47064));
        custInv.setBillDate(new Date());

    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        Thread.sleep(secondsToWait * 1000);
    }

    /**
     * Constructor for InvoiceReportFunctionTest.
     * @param arg0
     */
    public InvoiceReportFunctionTest(String arg0) {
        super(arg0);
    }
    
    public void testLongCompanyNamePrintInvoice() throws InterruptedException {
        // this customer currently has the longest name / company name in the system with a total of 44 characters
        Customer customer = CustomerService.getInstance().getCustomerById(new Integer(5150));
        ir.printInvoice(custInv, customer);
    }
    
    public void testTwoAddressLineItems() throws InterruptedException {
        // this customer has two addresses
        Customer customer = CustomerService.getInstance().getCustomerById(new Integer(5305));
        ir.printInvoice(custInv, customer);
    }
    
    public void testLongFullNamePrintInvoice() throws InterruptedException {
        // this customer currently has the longest name / company name in the system with a total of 20 characters
        Customer customer = CustomerService.getInstance().getCustomerById(new Integer(5242));
        ir.printInvoice(custInv, customer);
    }
    
    public void testLongStreetAddress() throws InterruptedException {
        // this customer currently has the longest street address in the system 27 characters
        Customer customer = CustomerService.getInstance().getCustomerById(new Integer(5276));
        ir.printInvoice(custInv, customer);
    }

    public void testLongCityStateZip() throws InterruptedException {
        // this customer currently has the longest city, state, zip in the system 23 characters
        Customer customer = CustomerService.getInstance().getCustomerById(new Integer(5877));
        ir.printInvoice(custInv, customer);
    }

}
