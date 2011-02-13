/**
 * File Name: FixBillingErrorTest.java
 * Date: Mar 26, 2005
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.fix;

import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;


public class FixBillingErrorTest extends TestCase {
    Calendar cal = null;
    private FixBillingError fbe;
    Logger logger = Logger.getLogger(this.getClass());
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.MARCH);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.YEAR, 2005);
        fbe = new FixBillingError();
        
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Constructor for FixBillingErrorTest.
     * @param arg0
     */
    public FixBillingErrorTest(String arg0) {
        super(arg0);
    }
    
    public void testGetScheduledBilledCustomers() {
        List custList = fbe.getCustomersBilledForThisMonthList(cal.getTime());
        assertEquals(117, custList.size());
    }
    
    public void testGenerateScheduledInvoices() {
        List invoiceList = fbe.generateScheduledInvoices(cal.getTime());
        assertEquals(117, invoiceList.size());
        logger.info("Invoices in the list \n" + invoiceList);
    }
    
}
