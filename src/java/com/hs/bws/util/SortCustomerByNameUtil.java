/**
 * File Name: SortCustomerByNameUtil.java
 * Date: Apr 27, 2005
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.util;

import java.util.Comparator;

import com.hs.bws.valueObject.Customer;
import com.hs.bws.valueObject.CustomerInvoice;

/**
 * Object will be used to do the sorting on the customer by the customer name.
 */
public class SortCustomerByNameUtil implements Comparator {

    /**
     * 
     */
    public SortCustomerByNameUtil() {
        super();
        // @TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object arg0, Object arg1) {
        int result = -1;
        if ((arg0 instanceof Customer) && (arg1 instanceof Customer)) {
            Customer cust1 = (Customer) arg0;
            Customer cust2 = (Customer) arg1;
            result = returnCustomerSort(cust1, cust2);
        } else if ((arg0 instanceof CustomerInvoice) && (arg1 instanceof CustomerInvoice)) {
            CustomerInvoice custInv1 = (CustomerInvoice) arg0;
            CustomerInvoice custInv2 = (CustomerInvoice) arg1;
            result = returnCustomerInvoiceSort(custInv1, custInv2);
        }
        
        return result;
    }
    
    /**
     * Will compare the customer  name for sort ordering
     * @param custInv1
     * @param custInv2
     * @return
     */
    private int returnCustomerInvoiceSort(CustomerInvoice custInv1, CustomerInvoice custInv2) {
        
        // @TODO Auto-generated method stub
        return returnCustomerSort(custInv1.getCustomer(), custInv2.getCustomer());
    }

    /**
     * Will compare the customer  name for sort ordering
     * @param cust1
     * @param cust2
     * @return
     */
    public int returnCustomerSort(Customer cust1, Customer cust2) {
        return cust1.getNameForOrdering().compareTo(cust2.getNameForOrdering());
    }

}
