/**
 * File Name: CustInvoiceDueDateComparator.java
 * Date: Sep 24, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.util;

import java.util.Comparator;

import com.hs.bws.valueObject.CustomerInvoice;


public class CustInvoiceDueDateComparator implements Comparator {

    /**
     * This comparator will be used to sort the invoices 
     * by due date in descending order
     */
    public CustInvoiceDueDateComparator() {
        super();
    }

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object arg0, Object arg1) {
        if (arg0 instanceof CustomerInvoice && arg1 instanceof CustomerInvoice) {
            CustomerInvoice inv1 = (CustomerInvoice) arg0;
            CustomerInvoice inv2 = (CustomerInvoice) arg1;
            return inv2.getDueDate().compareTo(inv1.getDueDate());
        }
        return 0;
    }

}
