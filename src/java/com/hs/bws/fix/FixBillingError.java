/**
 * File Name: FixBillingError.java
 * Date: Mar 26, 2005
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.fix;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.hs.bws.service.CustomerService;
import com.hs.bws.service.InvoiceService;
import com.hs.bws.util.Messages;
import com.hs.bws.valueObject.Customer;
import com.hs.bws.valueObject.CustomerInvoice;


public class FixBillingError extends InvoiceService {

    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.MARCH);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.YEAR, 2005);
        
        FixBillingError fbe = new FixBillingError();
        List invoiceList = fbe.generateScheduledInvoices(cal.getTime());
        fbe.updateNecessaryValues(invoiceList, cal.getTime());
        fbe.createCustomerInvoices(invoiceList);
    }

    /**
     * @param invoiceList
     */
    private void updateNecessaryValues(List invoiceList, Date billDate) {
        for (Iterator invIter = invoiceList.iterator(); invIter.hasNext();) {
            CustomerInvoice inv = (CustomerInvoice) invIter.next();
            inv.setCreateDate(new Date());
            inv.setBillDate(billDate);
            inv.setDueDate(this.getInvoiceDueDateByBillCycleDate(billDate));
        }
        
    }

    /**
     * This method will retrieve the customers who should have been billed 
     * for the March billing
     * @param time - billing date
     */
    public List getCustomersBilledForThisMonthList(Date time) {
        CustomerService custService = CustomerService.getInstance();
        List fullCustList = custService.getAllCustomers();
        
        // we are only going to get the customers who have a billing month of 12
        // and a billing cycle id of 300 for quarterly
        List errorCustList = new ArrayList();
        for (Iterator custIter = fullCustList.iterator(); custIter.hasNext();) {
            Customer cust = (Customer) custIter.next();
            
            if (cust.getBillCycleId().intValue() == Integer.parseInt(Messages.getString("billCycleId.quarterly")) && cust.getBillStartMonthId() == 12) {
                errorCustList.add(cust);
            }
        }
        return errorCustList;
    }
    
    
    
}
