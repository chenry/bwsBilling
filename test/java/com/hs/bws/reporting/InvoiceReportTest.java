/**
 * File Name: InvoiceReportTest.java Date: Mar 28, 2005 Project Name:
 * bwsBillingSystem Description:
 */
package com.hs.bws.reporting;

import java.util.Calendar;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.hs.bws.util.Messages;
import com.hs.bws.valueObject.Customer;

public class InvoiceReportTest extends TestCase {
    Logger logger = Logger.getLogger(this.getClass());

    InvoiceReport ir = null;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ir = InvoiceReport.getInstance();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Constructor for InvoiceReportTest.
     * 
     * @param arg0
     */
    public InvoiceReportTest(String arg0) {
        super(arg0);
    }

    private Customer buildCust(String billCycleId, int startMonth) {
        Customer cust = new Customer();
        cust.setBillCycleId(new Integer(billCycleId));
        cust.setBillStartMonthId(startMonth);
        return cust;
    }

    public void testBuildMonthlyMonthOfServiceText() {
        Customer monthlyCust = buildCust(Messages.getString("billCycleId.monthly"), 1);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.YEAR, 2005);
        
        String expectedString = "Feb.";
        assertEquals(expectedString, ir.getMonthOfService(monthlyCust, cal.getTime()));
    }
    
    public void testBuildAnnualMonthOfServiceText() {
        String annualCycleId = Messages.getString("billCycleId.annual");
        Customer annualCust = buildCust(annualCycleId, 5);
        Customer annualCust2 = buildCust(annualCycleId, 1);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 4);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.YEAR, 2005);
        
        assertEquals("May - April", ir.getMonthOfService(annualCust, cal.getTime()));
        assertEquals("Jan. - Dec.", ir.getMonthOfService(annualCust2, cal.getTime()));
    }
    
    public void testBuildSemiAnnualMonthOfServiceText() {
        String semiAnnualCycleId = Messages.getString("billCycleId.semiannual");
        Customer semiAnnJanCust = buildCust(semiAnnualCycleId, 1);
        Customer semiAnnAprilCust = buildCust(semiAnnualCycleId, 4);
        Customer semiAnnAugCust = buildCust(semiAnnualCycleId, 8);
        Customer semiAnnDecCust = buildCust(semiAnnualCycleId, 12);
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 2);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.YEAR, 2005);
        assertEquals("Jan. - June", ir.getMonthOfService(semiAnnJanCust, cal.getTime()));
        assertEquals("Oct. - Mar.", ir.getMonthOfService(semiAnnAprilCust, cal.getTime()));
        assertEquals("Feb. - July", ir.getMonthOfService(semiAnnAugCust, cal.getTime()));
        assertEquals("Dec. - May", ir.getMonthOfService(semiAnnDecCust, cal.getTime()));
        
        cal.set(Calendar.MONTH, 8);
        assertEquals("July - Dec.", ir.getMonthOfService(semiAnnJanCust, cal.getTime()));
        assertEquals("April - Sept.", ir.getMonthOfService(semiAnnAprilCust, cal.getTime()));
        assertEquals("Aug. - Jan.", ir.getMonthOfService(semiAnnAugCust, cal.getTime()));
        assertEquals("June - Nov.", ir.getMonthOfService(semiAnnDecCust, cal.getTime()));
        
    }

    public void testBuildQtrMonthOfServiceText() {
        // billing cycle customers
        String qtrlyBillCycleId = Messages.getString("billCycleId.quarterly");
        Customer qtrJan = buildCust(qtrlyBillCycleId, 1);
        Customer qtrFeb = buildCust(qtrlyBillCycleId, 2);
        Customer qtrMar = buildCust(qtrlyBillCycleId, 3);
        Customer qtrNov = buildCust(qtrlyBillCycleId, 11);
        Customer qtrDec = buildCust(qtrlyBillCycleId, 12);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 10);
        cal.set(Calendar.YEAR, 2005);

        String qtrJanMOSExpected = "Jan., Feb., Mar.";
        assertEquals(qtrJanMOSExpected, ir.getMonthOfService(qtrJan, cal
                .getTime()));

        String qtrFebMOSExpected = "Nov., Dec., Jan.";
        assertEquals(qtrFebMOSExpected, ir.getMonthOfService(qtrFeb, cal
                .getTime()));
        assertEquals(qtrFebMOSExpected, ir.getMonthOfService(qtrNov, cal
                .getTime()));

        String qtrMarMOSExpected = "Dec., Jan., Feb.";
        assertEquals(qtrMarMOSExpected, ir.getMonthOfService(qtrMar, cal
                .getTime()));
        assertEquals(qtrMarMOSExpected, ir.getMonthOfService(qtrDec, cal
                .getTime()));

        // set the month to october
        cal.set(Calendar.MONTH, Calendar.OCTOBER);
        qtrJanMOSExpected = "Oct., Nov., Dec.";
        assertEquals(qtrJanMOSExpected, ir.getMonthOfService(qtrJan, cal
                .getTime()));

        qtrFebMOSExpected = "Aug., Sept., Oct.";
        assertEquals(qtrFebMOSExpected, ir.getMonthOfService(qtrFeb, cal
                .getTime()));
        assertEquals(qtrFebMOSExpected, ir.getMonthOfService(qtrNov, cal
                .getTime()));

        qtrMarMOSExpected = "Sept., Oct., Nov.";
        assertEquals(qtrMarMOSExpected, ir.getMonthOfService(qtrMar, cal
                .getTime()));
        assertEquals(qtrMarMOSExpected, ir.getMonthOfService(qtrDec, cal
                .getTime()));

    }

    /**
     * Will test the logic given a starting month id of a quarter return a set
     * that represents all of the months in that quarter
     */
    public void testBuildQtrSets() {
        int qtrMonthId = 1;
        Set qtrSet = ir.buildQtrSet(qtrMonthId);

        assertTrue(qtrSet.contains(new Integer(1)));
        assertTrue(qtrSet.contains(new Integer(2)));
        assertTrue(qtrSet.contains(new Integer(3)));

        qtrMonthId = 11;
        qtrSet = ir.buildQtrSet(qtrMonthId);
        assertTrue(qtrSet.contains(new Integer(11)));
        assertTrue(qtrSet.contains(new Integer(12)));
        assertTrue(qtrSet.contains(new Integer(1)));

        qtrMonthId = 12;
        qtrSet = ir.buildQtrSet(qtrMonthId);
        assertTrue(qtrSet.contains(new Integer(12)));
        assertTrue(qtrSet.contains(new Integer(1)));
        assertTrue(qtrSet.contains(new Integer(2)));

    }
    
    public void testBuildSemiAnnualMonthlySets() {
        int semiAnnMonthId = 1;
        
        Set semiAnnMonthSet = ir.buildSemiAnnMonthSet(semiAnnMonthId);
        assertTrue(semiAnnMonthSet.contains(new Integer(1)));
        assertTrue(semiAnnMonthSet.contains(new Integer(2)));
        assertTrue(semiAnnMonthSet.contains(new Integer(3)));
        assertTrue(semiAnnMonthSet.contains(new Integer(4)));
        assertTrue(semiAnnMonthSet.contains(new Integer(5)));
        assertTrue(semiAnnMonthSet.contains(new Integer(6)));
        
        semiAnnMonthId = 5;
        semiAnnMonthSet = ir.buildSemiAnnMonthSet(semiAnnMonthId);
        assertTrue(semiAnnMonthSet.contains(new Integer(5)));
        assertTrue(semiAnnMonthSet.contains(new Integer(6)));
        assertTrue(semiAnnMonthSet.contains(new Integer(7)));
        assertTrue(semiAnnMonthSet.contains(new Integer(8)));
        assertTrue(semiAnnMonthSet.contains(new Integer(9)));
        assertTrue(semiAnnMonthSet.contains(new Integer(10)));
        
        semiAnnMonthId = 8;
        semiAnnMonthSet = ir.buildSemiAnnMonthSet(semiAnnMonthId);
        assertTrue(semiAnnMonthSet.contains(new Integer(8)));
        assertTrue(semiAnnMonthSet.contains(new Integer(9)));
        assertTrue(semiAnnMonthSet.contains(new Integer(10)));
        assertTrue(semiAnnMonthSet.contains(new Integer(11)));
        assertTrue(semiAnnMonthSet.contains(new Integer(12)));
        assertTrue(semiAnnMonthSet.contains(new Integer(1)));
        
        
        
    }

    public void testBuildNameAndAddress() {
        Customer cust = new Customer();
        cust.setLastName("Henry");
        cust.setFirstName("Carlus");
        cust.setAddress1("325 Alger SE");
        cust.setCity("Grand Rapids");
        cust.setState("MI");
        cust.setZipCode("49509");
        
        cust.setAltAddress1("2440 Forest Grove");
        cust.setAltAddress2("P.O. Box 325");
        cust.setAltCity("Wyoming");
        cust.setAltState("Michigan");
        cust.setAltZipCode("48076");

        String expectedString = "Carlus Henry\n" + "325 Alger SE\n"
                + "Grand Rapids, MI 49509";

        assertEquals(expectedString, ir.buildNameAndAddress(cust));
        
        expectedString = "Carlus Henry\n" + "2440 Forest Grove\nP.O. Box 325\nWyoming, Michigan 48076";
        cust.setUseAltAddress(true);
        assertEquals(expectedString, ir.buildNameAndAddress(cust));
    }

}