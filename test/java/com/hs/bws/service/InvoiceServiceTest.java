/**
 * File Name: InvoiceServiceTest.java
 * Date: Aug 18, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.service;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.hs.bws.util.Messages;
import com.hs.bws.valueObject.Customer;
import com.hs.bws.valueObject.CustomerInvoice;
import com.hs.bws.valueObject.InvoiceLineItem;
import com.hs.bws.valueObject.Payment;


public class InvoiceServiceTest extends TestCase {
    Logger logger = Logger.getLogger(InvoiceServiceTest.class);
    InvoiceService invService = null;
    CustomerService mockCustServ = null;
    Customer cust1, cust2, cust3, cust4;
    CustomerInvoice ci1, ci2, ci3, ci4;
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        invService = new MockInvoiceService();
        mockCustServ = MockCustomerService.getInstance();
        buildCustomersAndInvoicesForChangeOpenInvoiceToPrevBalance();
    }

    /**
         * Need 4 test customer Invoices
         * custInv1 - first bill in the customer's billing cycle.
         * custInv2 - it is still the customer billing cycle and the open invoice has a total equal to the rental charge
         * custInv3 - it is still the customer billing cycle and the open invoice has a total that is less than the rental charge
         * custInv4 - it is still the customer billing cycle and the open invoice has a total that is more than the rental charge.
     */
    private void buildCustomersAndInvoicesForChangeOpenInvoiceToPrevBalance() {
        ci1 = new CustomerInvoice(new Integer(1000));
        ci2 = new CustomerInvoice(new Integer(2000));
        ci3 = new CustomerInvoice(new Integer(3000));
        ci4 = new CustomerInvoice(new Integer(4000));
        
        // will set the customer id equal to the invoice id.
        ci1.setCustomerId(ci1.getId());
        ci2.setCustomerId(ci2.getId());
        ci3.setCustomerId(ci3.getId());
        ci4.setCustomerId(ci4.getId());

        // retrieve the customers that will be used for the test.
        cust1 = mockCustServ.getCustomerById(ci1.getCustomerId());
        cust2 = mockCustServ.getCustomerById(ci2.getCustomerId());
        cust3 = mockCustServ.getCustomerById(ci3.getCustomerId());
        cust4 = mockCustServ.getCustomerById(ci4.getCustomerId());
        
        // build the customer invoice item list
        ci1.setInvoiceLineItemList(buildInvoiceLineItemList(20, ci1.getId(), "Item", 5));
        ci2.setInvoiceLineItemList(buildInvoiceLineItemList(40, ci2.getId(), "Item", 10));
        ci3.setInvoiceLineItemList(buildInvoiceLineItemList(30, ci3.getId(), "Item", 10));
        ci4.setInvoiceLineItemList(buildInvoiceLineItemList(30, ci4.getId(), "Item", 10));
        
        assertEquals(5, ci1.getInvoiceLineItemList().size());
        assertEquals(10, ci2.getInvoiceLineItemList().size());
        assertEquals(10, ci3.getInvoiceLineItemList().size());
        assertEquals(10, ci4.getInvoiceLineItemList().size());
        
        assertEquals(new Double(100), ci1.getInvoiceBalance());
        // this value must be the same as the MockCustomerService Rental Charge getCustomerById for 2000 customer
        assertEquals(cust2.getRentalCharge(), ci2.getInvoiceBalance());
        // this value must be less than the MockCustomerService Rental Charge getCustomerById for 3000 customer
        assertTrue(cust3.getRentalCharge().doubleValue() > ci3.getInvoiceBalance().doubleValue());
        // this value must be more than the MockCustomerService Rental Charge getCustomerById for 4000 customer
        assertTrue(cust4.getRentalCharge().doubleValue() < ci4.getInvoiceBalance().doubleValue());
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testAggregateListOfInvoicesByCustomer() {
        CustomerInvoice ci1 = new CustomerInvoice();
        ci1.setId(new Integer(100));
        ci1.setCustomerId(new Integer(1));
        Calendar c = Calendar.getInstance();
        // will set the due date to januaray 10
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DATE, 10);
        ci1.setDueDate(c.getTime());
        ci1.addInvoiceLineItem(new InvoiceLineItem(new Integer(2000), new Integer(100), "Item1", new Double(40)));

        CustomerInvoice ci2 = new CustomerInvoice();
        ci2.setId(new Integer(101));
        ci2.setCustomerId(new Integer(1));
        // will set the due date to february 10
        c.set(Calendar.MONTH, 1);
        c.set(Calendar.DATE, 10);
        ci2.setDueDate(c.getTime());
        ci2.addInvoiceLineItem(new InvoiceLineItem(new Integer(2100), new Integer(101), "Item1", new Double(20)));

        CustomerInvoice ci3 = new CustomerInvoice();
        ci3.setId(new Integer(102));
        ci3.setCustomerId(new Integer(1));
        // will set the due date to march 10
        c.set(Calendar.MONTH, 2);
        c.set(Calendar.DATE, 10);
        ci3.setDueDate(c.getTime());
        ci3.addInvoiceLineItem(new InvoiceLineItem(new Integer(2100), new Integer(102), "Item1", new Double(20)));

        CustomerInvoice ci4 = new CustomerInvoice();
        ci4.setId(new Integer(103));
        ci4.setCustomerId(new Integer(2));
        // will set the due date to april 10
        c.set(Calendar.MONTH, 3);
        c.set(Calendar.DATE, 10);
        ci4.setDueDate(c.getTime());
        ci4.addInvoiceLineItem(new InvoiceLineItem(new Integer(2100), new Integer(103), "Item1", new Double(25)));

        List custInvList = new ArrayList();
        custInvList.add(ci1);
        custInvList.add(ci2);
        custInvList.add(ci3);
        custInvList.add(ci4);
        
        //@TODO not sure what the new date parameter is being used for.
        List l = invService.aggregateListOfInvoicesByCustomer(custInvList, new java.util.Date(), new java.util.Date());
        assertTrue(l.size() == 2);
        for (Iterator iter = l.iterator(); iter.hasNext();) {
            CustomerInvoice inv = (CustomerInvoice) iter.next();
            if (inv.getCustomerId().intValue() == 1) {
                logger.info("Customer 1 invoice " + inv);
                assertEquals(new Double(80), inv.getInvoiceTotal());
                assertTrue(3 == inv.getInvoiceLineItemList().size());
            } else if (inv.getCustomerId().intValue() == 2) {
                logger.info("Customer 2 invoice " + inv);
                assertEquals(new Double(25), inv.getInvoiceTotal());
                assertTrue(1 == inv.getInvoiceLineItemList().size());
            }
        }
    }
    
    public void testPopulateInvoiceLineItems() {
        InvoiceService invService1 = new MockInvoiceService() {
            public List getInvoiceLineItemList(CustomerInvoice ci) {
                List l = new ArrayList();
                for (int i = 0; i < 20; i++) {
                    InvoiceLineItem ili = new InvoiceLineItem(new Integer(ci.getId().intValue() + 100), ci.getId(), "SomeDesc", new Double(20));
                    l.add(ili);
                }
                return l;
            }
        };
        
        CustomerInvoice custInv = new CustomerInvoice();
        custInv.setId(new Integer(200));
        custInv = invService1.populateInvoiceLineItems(custInv);
        assertTrue(20 == custInv.getInvoiceLineItemList().size());
        assertEquals(new Double(400), custInv.getInvoiceTotal());
    }
    
    public void testApplyPayments() {
        // retrieve open invoices
        // this invoice will be the newest.
        CustomerInvoice custInvoice1 = new CustomerInvoice();
        custInvoice1.setDueDate(new Date(System.currentTimeMillis()));
        InvoiceLineItem ili1 = new InvoiceLineItem();
        ili1.setChargeAmount(new Double(50.00));
        custInvoice1.addInvoiceLineItem(ili1);
        custInvoice1.setCustomerId(new Integer(10));
        Payment pay = new Payment();
        pay.setPaymentAmt(new Double(10.00));
        custInvoice1.addPayment(pay);
        
        CustomerInvoice custInvoice2 = new CustomerInvoice();
        custInvoice2.setDueDate(new Date(System.currentTimeMillis()));
        InvoiceLineItem ili2 = new InvoiceLineItem();
        ili2.setChargeAmount(new Double(90.00));
        custInvoice2.addInvoiceLineItem(ili2);
        custInvoice2.setCustomerId(new Integer(20));
        Payment pay1 = new Payment();
        pay1.setPaymentAmt(new Double(50.00));
        pay1.setPaymentId(new Integer(1));
        pay1.setPaymentType("C");    
        custInvoice2.addPayment(pay1);
               
        Payment p = new Payment();
        p.setPaymentAmt(new Double(20));
        custInvoice1.addPayment(p);
        
        Payment p1 = new Payment();
        p1.setPaymentAmt(new Double(40));
        custInvoice2.addPayment(p1);
        
        // determine if the balance is correct on the open invoices
        double openBalance = 0.00;
        assertEquals(new Double(20.00), custInvoice1.getInvoiceBalance());
        //@TODO having a hard time testing this because this customer should have a credit balance
        // set to 15.00.  Currently just printing out to stdout the result of the test.
        assertEquals(new Double(0), custInvoice2.getInvoiceBalance());
    }
    
    public void testAddInvoiceToCustomer() {
        InvoiceService invService = new InvoiceService() {
            public void initializeServices() {
                this.custInvDAO = new MockInvoiceDAO();
            }
        };
        CustomerInvoice custInvoice = new CustomerInvoice();
        custInvoice.setCustomerId(new Integer(99));
        custInvoice.setId(new Integer(1));
        invService.addCustomerInvoice(custInvoice);
    }

    /**
     * This method will contain the logic necessary
     * to test when a billing cycle is issued, 
     * to correctly retrieve the customers who should be billed.
     */
    public void testGetCustomersBilledForThisMonthList() {
        InvoiceService invService = new InvoiceService() {
            public void initializeServices() {
                this.custInvDAO = new MockInvoiceDAO();
                this.custService = MockCustomerService.getInstance();
            }
        };
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
        java.util.Date billCycleRunDate = sdf.parse("08/01/2004", new ParsePosition(0));
        int monthId = new Integer(sdfMonth.format(billCycleRunDate)).intValue();
        List custBilledList = invService.getCustomersBilledForThisMonthList(billCycleRunDate);
        logger.info("Size of list of customers being billed " + custBilledList.size());
        for (Iterator iter = custBilledList.iterator(); iter.hasNext();) {
            Customer cust = (Customer) iter.next();
            logger.debug("Customers: " + cust.getBillStartMonth() + "|" + cust.getBillCycleId());
            if (cust.getBillCycleId().intValue() == Integer.parseInt(Messages.getString("billCycleId.annual"))) {
                assertEquals(monthId, cust.getBillStartMonthId());
            } else if (cust.getBillCycleId().intValue() == Integer.parseInt(Messages.getString("billCycleId.semiannual"))) {
                int secondSAMonthId = ((monthId + 6) % 12);
                if (cust.getBillStartMonthId() != monthId && cust.getBillStartMonthId() != secondSAMonthId) {
                    fail("the customer was Semi-annual and his start month was neither " + monthId + " or " + secondSAMonthId + " it was " + cust.getBillStartMonthId());
                }
            } else if (cust.getBillCycleId().intValue() == Integer.parseInt(Messages.getString("billCycleId.quarterly"))) {
                int secondQtrMonthId = ((monthId + 3) % 12);
                int thirdQtrMonthId = ((secondQtrMonthId + 3) % 12);
                int fourthQtrMonthId = ((thirdQtrMonthId + 3) % 12);
                if (cust.getBillStartMonthId() != monthId && cust.getBillStartMonthId() != secondQtrMonthId 
                        && cust.getBillStartMonthId() != thirdQtrMonthId
                        && cust.getBillStartMonthId() != fourthQtrMonthId) {
                    fail("the customer was quarterly and his start month was neither " + monthId + ", " + secondQtrMonthId + ", " + thirdQtrMonthId + ", " + fourthQtrMonthId + ", it was " + cust.getBillStartMonthId());
                }
            }
        }
    }
    
    /**
     * Will make sure that we retrieve the correct bill due date
     */
    public void testGetInvoiceDueDateByBillCycleDate() {
        String dateString = "10/1/2004";
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        java.util.Date d = sdf.parse(dateString, new ParsePosition(0));
        java.util.Date dueDate = invService.getInvoiceDueDateByBillCycleDate(d);
        assertEquals(sdf.parse("10/10/2004", new ParsePosition(0)), dueDate);
    }
    
    public void testGenerateScheduledInvoices() {
        // we will rely on the method getCustomersBilledForThisMonthList in the mock object.
        List schedInvList = invService.generateScheduledInvoices(new java.util.Date());
        assertEquals(3, schedInvList.size());
        for (Iterator iter = schedInvList.iterator(); iter.hasNext();) {
            CustomerInvoice custInv = (CustomerInvoice) iter.next();
            
            if (custInv.getCustomerId().intValue() != 200) {
                assertEquals(new Double(30), custInv.getInvoiceBalance());
                assertEquals(1, custInv.getInvoiceLineItemList().size());
                for (Iterator iterator = custInv.getInvoiceLineItemList().iterator(); iterator
                        .hasNext();) {
                    InvoiceLineItem ili = (InvoiceLineItem) iterator.next();
                    assertEquals(new Double(30), ili.getChargeAmount());
                    assertEquals(custInv.getId(), ili.getInvoiceId());
                    assertEquals("Rental Charge",ili.getItemDesc());
                }
            } else {
                assertEquals(new Double(85.55), custInv.getInvoiceBalance());
                assertEquals(2, custInv.getInvoiceLineItemList().size());
            }
       }
    }
    
    private List buildInvoiceLineItemList(double chargeAmt, Integer invoiceId, String itemDesc, int iliCnt) {
        List iliList = new ArrayList();
        for (int i = 0; i < iliCnt; i++) {
            InvoiceLineItem ili = new InvoiceLineItem();
            ili.setChargeAmount(new Double(chargeAmt));
            ili.setInvoiceId(invoiceId);
            ili.setItemDesc(itemDesc + " " + i);
            iliList.add(ili);
        }
        return iliList;
    }
    
    /**
     * Tests the logic that will take an open invoice
     * and create a previous balance for it
     * 
     */
    public void testChangeOpenInvoiceLIToPreviousBalanceLI() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 10);
        cal.set(Calendar.YEAR, 2005);

        List invoiceList = new ArrayList();
        invoiceList.add(ci1);
        invoiceList.add(ci2);
        invoiceList.add(ci3);
        invoiceList.add(ci4);
        
        invService.changeOpenInvoiceLIToPreviousBalanceLI(invoiceList, cal.getTime());
        // iterate over the list of invoices
        for (Iterator iter = invoiceList.iterator(); iter.hasNext();) {
            CustomerInvoice custInv = (CustomerInvoice) iter.next();
            // for ci1 this line item should have a balance of 100
            if (custInv.getId().equals(ci1.getId())) {
                // this customer should only have one line item.  It shoul be the previous balance.
                assertEquals(1, custInv.getInvoiceLineItemList().size());
                assertEquals(ci1.getInvoiceBalance(), custInv.getInvoiceBalance());
                assertEquals("Prev. Balance", ((InvoiceLineItem) custInv.getInvoiceLineItemList().get(0)).getItemDesc());
            } else if (custInv.getId().equals(ci2.getId())) {
                assertEquals(1, custInv.getInvoiceLineItemList().size());
                assertEquals(ci2.getInvoiceBalance(), custInv.getInvoiceBalance());
                assertEquals("Rental Charge", ((InvoiceLineItem) custInv.getInvoiceLineItemList().get(0)).getItemDesc());
            } else if (custInv.getId().equals(ci3.getId())) {
                assertEquals(1, custInv.getInvoiceLineItemList().size());
                assertEquals(ci3.getInvoiceBalance(), custInv.getInvoiceBalance());
                assertEquals("Prev. Balance", ((InvoiceLineItem) custInv.getInvoiceLineItemList().get(0)).getItemDesc());
            } else if (custInv.getId().equals(ci4.getId())) {
                assertEquals(2, custInv.getInvoiceLineItemList().size());
                logger.info("value = " + ci4.getInvoiceBalance());
                assertEquals(ci4.getInvoiceBalance(), custInv.getInvoiceBalance());
                boolean foundPrevBal = false;
                boolean foundRentalCharge = false;
                for (Iterator iterator = custInv.getInvoiceLineItemList().iterator(); iterator
                        .hasNext();) {
                    InvoiceLineItem ili = (InvoiceLineItem) iterator.next();
                    if (ili.getItemDesc().equals("Prev. Balance")) {
                        foundPrevBal = true;
                        // make sure that the balance is appropriate
                        assertEquals(new Double(ci4.getInvoiceBalance().doubleValue() - cust4.getRentalCharge().doubleValue()), ili.getChargeAmount());
                    } else if (ili.getItemDesc().equals("Rental Charge")) {
                        foundRentalCharge = true;
                        assertEquals(cust4.getRentalCharge(), ili.getChargeAmount());
                    }
                }
                assertTrue(foundPrevBal);
                assertTrue(foundRentalCharge);
            }
        }
    }

    
    public void testChangeOpenInvoiceLIToPreviousBalanceLIWithNewDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 10);
        cal.set(Calendar.YEAR, 2005);

        List invoiceList = new ArrayList();
        invoiceList.add(ci1);
        invoiceList.add(ci2);
        invoiceList.add(ci3);
        invoiceList.add(ci4);
        
        invService.changeOpenInvoiceLIToPreviousBalanceLI(invoiceList, cal.getTime());
        // iterate over the list of invoices
        for (Iterator iter = invoiceList.iterator(); iter.hasNext();) {
            CustomerInvoice custInv = (CustomerInvoice) iter.next();
            // for ci1 this line item should have a balance of 100
            if (custInv.getId().equals(ci1.getId())) {
                // this customer should only have one line item.  It shoul be the previous balance.
                assertEquals(2, custInv.getInvoiceLineItemList().size());
                assertEquals(ci1.getInvoiceBalance(), custInv.getInvoiceBalance());
                boolean foundPrevBal = false;
                boolean foundRentalCharge = false;
                for (Iterator iterator = custInv.getInvoiceLineItemList().iterator(); iterator
                        .hasNext();) {
                    InvoiceLineItem ili = (InvoiceLineItem) iterator.next();
                    if (ili.getItemDesc().equals("Prev. Balance")) {
                        foundPrevBal = true;
                        assertEquals(new Double(ci1.getInvoiceBalance().doubleValue() - cust1.getRentalCharge().doubleValue()), ili.getChargeAmount());
                    } else if (ili.getItemDesc().equals("Rental Charge")) {
                        foundRentalCharge = true;
                        assertEquals(cust1.getRentalCharge(), ili.getChargeAmount());
                    }
                }
                assertTrue(foundPrevBal);
                assertTrue(foundRentalCharge);
            } else if (custInv.getId().equals(ci2.getId())) {
                assertEquals(1, custInv.getInvoiceLineItemList().size());
                assertEquals(ci2.getInvoiceBalance(), custInv.getInvoiceBalance());
                assertEquals("Prev. Balance", ((InvoiceLineItem) custInv.getInvoiceLineItemList().get(0)).getItemDesc());
            } else if (custInv.getId().equals(ci3.getId())) {
                assertEquals(1, custInv.getInvoiceLineItemList().size());
                assertEquals(ci3.getInvoiceBalance(), custInv.getInvoiceBalance());
                assertEquals("Prev. Balance", ((InvoiceLineItem) custInv.getInvoiceLineItemList().get(0)).getItemDesc());
            } else if (custInv.getId().equals(ci4.getId())) {
                assertEquals(1, custInv.getInvoiceLineItemList().size());
                assertEquals(ci4.getInvoiceBalance(), custInv.getInvoiceBalance());
            }
        }
    }
    
    
    /**
     * Will test the method that determines if a customer should be billed for the
     * given bill date
     */
    public void testIsCustBilledForBillDate() {
        Customer cust1 = new Customer();
        cust1.setBillCycleId(new Integer(Messages.getString("billCycleId.annual")));
        cust1.setBillStartMonthId(10);
        
        Customer cust2 = new Customer();
        cust2.setBillCycleId(new Integer(Messages.getString("billCycleId.semiannual")));
        cust2.setBillStartMonthId(4);
        
        Customer cust3 = new Customer();
        cust3.setBillCycleId(new Integer(Messages.getString("billCycleId.quarterly")));
        cust3.setBillStartMonthId(3);
        
        Customer cust4 = new Customer();
        cust4.setBillCycleId(new Integer(Messages.getString("billCycleId.quarterly")));
        cust4.setBillStartMonthId(4);
        
        Customer cust5 = new Customer();
        cust5.setBillCycleId(new Integer(Messages.getString("billCycleId.quarterly")));
        cust5.setBillStartMonthId(5);
        
        Customer cust6 = new Customer();
        cust6.setBillCycleId(new Integer(Messages.getString("billCycleId.monthly")));
        cust6.setBillStartMonthId(8);
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.OCTOBER);
        cal.set(Calendar.DATE, 10);
        cal.set(Calendar.YEAR, 2005);
        
        invService =new InvoiceService();
        assertTrue(invService.isCustSchduledToBeBilled(cust1, cal.getTime()));
        assertTrue(invService.isCustSchduledToBeBilled(cust2, cal.getTime()));
        assertFalse(invService.isCustSchduledToBeBilled(cust3, cal.getTime()));
        assertTrue(invService.isCustSchduledToBeBilled(cust4, cal.getTime()));
        assertFalse(invService.isCustSchduledToBeBilled(cust5, cal.getTime()));
        assertTrue(invService.isCustSchduledToBeBilled(cust6, cal.getTime()));
        
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        assertFalse(invService.isCustSchduledToBeBilled(cust1, cal.getTime()));
        assertFalse(invService.isCustSchduledToBeBilled(cust2, cal.getTime()));
        assertTrue(invService.isCustSchduledToBeBilled(cust3, cal.getTime()));
        assertFalse(invService.isCustSchduledToBeBilled(cust4, cal.getTime()));
        assertFalse(invService.isCustSchduledToBeBilled(cust5, cal.getTime()));
        assertTrue(invService.isCustSchduledToBeBilled(cust6, cal.getTime()));
        
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        assertFalse(invService.isCustSchduledToBeBilled(cust1, cal.getTime()));
        assertFalse(invService.isCustSchduledToBeBilled(cust2, cal.getTime()));
        assertFalse(invService.isCustSchduledToBeBilled(cust3, cal.getTime()));
        assertTrue(invService.isCustSchduledToBeBilled(cust4, cal.getTime()));
        assertFalse(invService.isCustSchduledToBeBilled(cust5, cal.getTime()));
        assertTrue(invService.isCustSchduledToBeBilled(cust6, cal.getTime()));
        
        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
        assertFalse(invService.isCustSchduledToBeBilled(cust1, cal.getTime()));
        assertFalse(invService.isCustSchduledToBeBilled(cust2, cal.getTime()));
        assertFalse(invService.isCustSchduledToBeBilled(cust3, cal.getTime()));
        assertFalse(invService.isCustSchduledToBeBilled(cust4, cal.getTime()));
        assertTrue(invService.isCustSchduledToBeBilled(cust5, cal.getTime()));
        assertTrue(invService.isCustSchduledToBeBilled(cust6, cal.getTime()));
        
        cal.set(Calendar.MONTH, Calendar.APRIL);
        assertFalse(invService.isCustSchduledToBeBilled(cust1, cal.getTime()));
        assertTrue(invService.isCustSchduledToBeBilled(cust2, cal.getTime()));
        assertFalse(invService.isCustSchduledToBeBilled(cust3, cal.getTime()));
        assertTrue(invService.isCustSchduledToBeBilled(cust4, cal.getTime()));
        assertFalse(invService.isCustSchduledToBeBilled(cust5, cal.getTime()));
        assertTrue(invService.isCustSchduledToBeBilled(cust6, cal.getTime()));
        
        
    }
    
}

