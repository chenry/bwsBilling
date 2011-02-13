/**
 * File Name: PendingServiceTest.java
 * Date: Nov 23, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.valueObject;

import junit.framework.TestCase;


public class PendingServiceTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Constructor for PendingServiceTest.
     * @param arg0
     */
    public PendingServiceTest(String arg0) {
        super(arg0);
    }
    
    public void testBuildInvoiceLineItem() {
        PendingService pendServ = new PendingService();
        pendServ.setId(2020);
        pendServ.setCustomerId(1000);
        pendServ.setDesc("Some kind of Service");
        pendServ.setChargeAmt(19.95);
        
        InvoiceLineItem ili = pendServ.buildInvoiceLineItem();
        assertEquals(pendServ.getDesc(), ili.getItemDesc());
        assertEquals(pendServ.getChargeAmt(), ili.getChargeAmount().doubleValue(), 0.01);
    }

}
