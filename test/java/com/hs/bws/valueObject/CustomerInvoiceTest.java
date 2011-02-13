/**
 * File Name: CustomerInvoiceTest.java
 * Date: Aug 18, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.valueObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import com.hs.bws.util.CustInvoiceDueDateComparator;


public class CustomerInvoiceTest extends TestCase {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
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
    
    public void testGetInvoiceTotal() {
        CustomerInvoice custInvoice = new CustomerInvoice();
        InvoiceLineItem ili1 = new InvoiceLineItem();
        ili1.setChargeAmount(new Double(10.00));
        InvoiceLineItem ili2 = new InvoiceLineItem();
        ili2.setChargeAmount(new Double(20.00));
        InvoiceLineItem ili3 = new InvoiceLineItem();
        ili3.setChargeAmount(new Double(30.00));
        InvoiceLineItem ili4 = new InvoiceLineItem();
        ili4.setChargeAmount(new Double(40.00));
        
        List invoiceLineItemsList = new ArrayList();
        invoiceLineItemsList.add(ili1);
        invoiceLineItemsList.add(ili2);
        invoiceLineItemsList.add(ili3);
        invoiceLineItemsList.add(ili4);
        
        custInvoice.setInvoiceLineItemList(invoiceLineItemsList);
        assertEquals(new Double(100.00), custInvoice.getInvoiceTotal());
        
        
    }
    
    public void testGetInvoiceBalance() {
        CustomerInvoice custInvoice = new CustomerInvoice() {
            public Double getInvoiceTotal() {
                return new Double(100.00);
            }
        };
        assertEquals(new Double(100), custInvoice.getInvoiceTotal());
        Payment pay = new Payment();
        pay.setPaymentAmt(new Double(20));
        custInvoice.addPayment(pay);
        assertEquals(new Double(80), custInvoice.getInvoiceBalance());
        
        // when there is an overpayment, we will return a negative number
        // this number shows that their was an overpayment 
        Payment pay1 = new Payment();
        pay1.setPaymentAmt(new Double(100));
        custInvoice.addPayment(pay1);
        assertEquals(new Double(-20), custInvoice.getInvoiceBalance());
        
        
    }
    
    public void testOrderInvoiceByDueDate() {
        CustomerInvoice custInv1 = new CustomerInvoice();
        custInv1.setDueDate(sdf.parse("10/10/2004", new ParsePosition(0)));
        
        CustomerInvoice custInv2 = new CustomerInvoice();
        custInv2.setDueDate(sdf.parse("10/20/2004", new ParsePosition(0)));
        
        CustomerInvoice custInv3 = new CustomerInvoice();
        custInv3.setDueDate(sdf.parse("10/25/2004", new ParsePosition(0)));
        
        List custInvList = new ArrayList();
        custInvList.add(custInv2);
        custInvList.add(custInv3);
        custInvList.add(custInv1);
        Collections.sort(custInvList, new CustInvoiceDueDateComparator());
        
        assertEquals("10/25/2004", sdf.format(((CustomerInvoice) custInvList.get(0)).getDueDate()));
        assertEquals("10/20/2004", sdf.format(((CustomerInvoice) custInvList.get(1)).getDueDate()));
        assertEquals("10/10/2004", sdf.format(((CustomerInvoice) custInvList.get(2)).getDueDate()));
    }
    
}
