/**
 * File Name: InvoiceServiceFunctionTest.java
 * Date: Feb 12, 2005
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.service;

import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.hs.bws.valueObject.CustomerInvoice;


public class InvoiceServiceFunctionTest extends TestCase {
    InvoiceService is =  null;
    private Logger logger = Logger.getLogger(InvoiceServiceFunctionTest.class);
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        is = new InvoiceService();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Constructor for InvoiceServiceFunctionTest.
     * @param arg0
     */
    public InvoiceServiceFunctionTest(String arg0) {
        super(arg0);
    }

    public void testPrintInvoice() throws InterruptedException {
        CustomerInvoice custInvoice = new CustomerInvoice();
        custInvoice.setId(new Integer(2748));
        
        is.printInvoice(custInvoice);
        Thread.sleep(30000);
    }
    
    public void testGetCustomersBilledForThisMonthList() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.YEAR, 2005);
        logger.info("The calendar time is " + cal.getTime());
        
        List custBilledList = is.getCustomersBilledForThisMonthList(cal.getTime());
        assertEquals(4, custBilledList.size());
    }
    
    public void testPrintInvoiceDescAlpha() {
        InvoiceService invService = new InvoiceService() {
            public void printInvoice(CustomerInvoice customerInvoice) {
                logger.info("id: " + customerInvoice.getId());
            }
        };
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.MAY);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.YEAR, 2005);
        invService.printInvoicesByBillDate(cal.getTime());
    }
    

}
