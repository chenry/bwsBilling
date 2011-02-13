/**
 * File Name: MockInvoiceService.java
 * Date: Sep 11, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.service;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hs.bws.exception.BWSGenericException;
import com.hs.bws.valueObject.Customer;
import com.hs.bws.valueObject.CustomerInvoice;
import com.hs.bws.valueObject.InvoiceLineItem;
import com.hs.bws.valueObject.PendingService;


public class MockInvoiceService extends InvoiceService {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    Map customerInvoicesMap = new HashMap();
    /**
     * 
     */
    public MockInvoiceService() {
        super();
    }
    
    public List addInvoiceList = new ArrayList();
    public void addCustomerInvoice(CustomerInvoice custInvoice) {
        addInvoiceList.add(custInvoice);
    }
    
    public void initializeServices() {
        this.custInvDAO = new MockInvoiceDAO();
        this.custService = MockCustomerService.getInstance();
    }
    
    public List getCustomersBilledForThisMonthList(java.util.Date billCycleRunDate) {
        List customersToBeBilledList = new ArrayList();
        for (int i = 0; i < 3; i++) {
            int invoiceId = i * 100;
            Customer cust = new Customer();
            cust.setId(new Integer(invoiceId));
            cust.setBillCycleId(new Integer(400));
            cust.setBillStartMonthId(i);
            cust.setRentalCharge(new Double(30));
            customersToBeBilledList.add(cust);
        }
        return customersToBeBilledList;
    }
    
    
    
    /* (non-Javadoc)
     * @see com.hs.bws.service.InvoiceService#getPendingServicesByCustomerId(int)
     */
    public List getPendingServicesByCustomerId(int customerId)
            throws BWSGenericException {
        List pendingServiceList = new ArrayList();
        if (customerId == 200) {
            PendingService ps = new PendingService();
            ps.setId(1010);
            ps.setDesc("Outstanding Charge");
            ps.setChargeAmt(55.55);
            ps.setCustomerId(customerId);
            pendingServiceList.add(ps);
        }
        return pendingServiceList;
    }
    public List getCustomersWithOpenInvoicesList() {
        List customersWithOpenInvoicesList = new ArrayList();
        for (int i = 0; i < 3; i++) {
            Customer cust = new Customer();
            cust.setId(new Integer(100 + i));
            cust.setBillCycleId(new Integer(400));
            cust.setBillStartMonthId(i);
            cust.setRentalCharge(new Double(40));
            customersWithOpenInvoicesList.add(cust);
        }
        return customersWithOpenInvoicesList;
    }
    
    
    public CustomerInvoice getOpenCustomerInvoices(Integer custId) {
        CustomerInvoice custInvoice = null;
        if (custId.intValue() != 11) {
            custInvoice = new CustomerInvoice();
            custInvoice.setCloseDate(null);
            custInvoice.setCreateDate(new Date());
            custInvoice.setCustomerId(custId);
            custInvoice.setId(new Integer(100));
            custInvoice.setPaymentAmt(new Double(0));
            custInvoice.setDueDate(sdf
                    .parse("10/10/2004", new ParsePosition(0)));
        }
        return custInvoice;
    }
    
    public List getClosedCustomerInvoices(Integer custId) {
        List closedInvoices = new ArrayList();
        if (custId.intValue() != 11) {
            for (int i = 0; i < 10; i++) {
                CustomerInvoice custInvoice = new CustomerInvoice();
                custInvoice.setCreateDate(new Date());
                custInvoice.setCustomerId(custId);
                custInvoice.setId(new Integer(i + 200));
                custInvoice.setDueDate(sdf
                        .parse("10/10/2003", new ParsePosition(0)));
                custInvoice.setCloseDate(sdf.parse("10/10/2003", new ParsePosition(
                        0)));
                closedInvoices.add(custInvoice);
            
            }
      }
        
      return closedInvoices;
    }
    
    public void applyPaymentByCustIdAndPaymentAmt(Integer custId, Double paymentAmt) {
        logger.info("Will apply " + paymentAmt + " to customer " + custId);
    }
    
    public List getCustomersWithOpenInvoices() {
        return getCustomersWithOpenInvoicesList();
    }
    
    
    /* (non-Javadoc)
     * @see com.hs.bws.service.InvoiceService#aggregateListOfInvoicesByCustomer(java.util.List)
     */
    public List aggregateListOfInvoicesByCustomer(List completeListOfInvoices) {
        // @TODO not sure what the new date parameter is being used for.
        List l = super.aggregateListOfInvoicesByCustomer(completeListOfInvoices, new java.util.Date(), new java.util.Date());
        return l;
    }
    /* (non-Javadoc)
     * @see com.hs.bws.service.InvoiceService#getOpenInvoices()
     */
    public List getOpenInvoices() {
        List openInvoiceList = new ArrayList();
        // will create 5 customer invoices with open balances
        for (int i = 0; i < 5; i++) {
            int invoiceId = 1 + i;
            int invoiceLIId = 5 + i;
            CustomerInvoice custInvoice = new CustomerInvoice();
            custInvoice.setCreateDate(new Date());
            custInvoice.setCustomerId(new Integer(i));
            custInvoice.setId(new Integer(invoiceId));
            
            InvoiceLineItem ili = new InvoiceLineItem();
            ili.setChargeAmount(new Double(20));
            ili.setId(new Integer(invoiceLIId));
            ili.setInvoiceId(new Integer(invoiceId));
            ili.setItemDesc("Past Charges");
        }
        return openInvoiceList;
    }
    
    /* (non-Javadoc)
     * @see com.hs.bws.service.InvoiceService#deltePendingServices(java.util.List)
     */
    protected void deletePendingServiceList(List pendingServicesList) {
        // do nothing
    }
}
