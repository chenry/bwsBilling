/**
 * File Name: CustomerNameComparator.java
 * Date: Sep 24, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.util;

import java.util.Comparator;

import com.hs.bws.valueObject.Customer;


public class CustomerNameComparator implements Comparator {

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object arg0, Object arg1) {
        Customer c1 = (Customer) arg0;
        Customer c2 = (Customer) arg1;
        return c1.getFormalName().compareTo(c2.getFormalName());
    }
    
}
