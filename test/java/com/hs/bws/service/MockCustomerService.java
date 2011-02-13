/**
 * File Name: MockCustomerService.java
 * Date: Aug 19, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.hs.bws.exception.BWSGenericException;
import com.hs.bws.util.Messages;
import com.hs.bws.valueObject.Customer;


public class MockCustomerService extends CustomerService {
    Logger logger = Logger.getLogger(MockCustomerService.class);
    List customerList = new ArrayList();
    private static CustomerService mockCustomerService = null;
    private Customer custCredit = null;
    
    public static CustomerService getInstance() {
        if (mockCustomerService == null) {
            mockCustomerService = new MockCustomerService();
        }
        return mockCustomerService;
    }
    /**
     * 
     */
    private MockCustomerService() {
        super();
        initializeCustomerList();
    }
    
    /**
     * Used to generate the list of customers that are available
     */
    private void initializeCustomerList() {
        int totalInList = 100;
        for (int i=1; i<=totalInList; i++) {
            Customer cust = new Customer();
            cust.setId(new Integer(i));
            String name = null;
            if (totalInList % i == 1) {
                name = "Company " + i;
                cust.setCompanyName(name);
            } else {
                cust.setLastName("Henry");
                cust.setFirstName("Carlus");
                name = cust.getLastName() + ", " + cust.getFirstName(); 
            }
            cust.setAddress1("2440" + i + " Forest Grove");
            cust.setCity("Wyoming");
            cust.setState("MI");
            cust.setZipCode("49509");
            // will give some customers alternate addresses
            if (totalInList % i == 2) {
                cust.setLastName("Fochtman"); 
                cust.setAltAddress1("88 Kings Blvd.");
                cust.setAltAddress2("Apt 2");
                cust.setAltCity("Sparta");
                cust.setAltState("MI");
                cust.setAltZipCode("49345");
            }
            cust.setComments("I have heard through the grapevine that " + name +
            		" is pretty silly.");
            cust.setInstallationDate(new Date());
            cust.setRentalCharge(new Double(30.00));
            cust.setBillStartMonthId((i % 12) + 1);
            if (i % 4 == 0) {
                cust.setBillCycleId(new Integer(Messages.getString("billCycleId.annual")));
            } else if (i % 4 == 1) {
                cust.setBillCycleId(new Integer(Messages.getString("billCycleId.semiannual")));
            } else if (i % 4 == 2) {
                cust.setBillCycleId(new Integer(Messages.getString("billCycleId.quarterly")));
            } else if (i % 4 == 3) {
                cust.setBillCycleId(new Integer(Messages.getString("billCycleId.monthly")));
            }
            customerList.add(cust);
        }
    }
    
    /**
     * This method will be used if the user requests
     * to see all of the availble customers
     * @return List of Customer objects
     */
    public List getCompleteCustomerList() {
        return customerList;
    }
    
    
    /* (non-Javadoc)
     * @see com.hs.bws.service.CustomerService#getCustomerById(java.lang.Integer)
     */
    public Customer getCustomerById(Integer customerId) {
        Customer returnedCustomer = null;
        // we will search through the list of customers and return the 
        // customer whose id matches the one that was passed in.
        Customer c = new Customer();
        c.setId(customerId);
        logger.info("Customer Id: " + customerId);
        Collections.sort(customerList);
        
        returnedCustomer = (Collections.binarySearch(customerList, c) >= 0) ? (Customer) customerList.get(Collections.binarySearch(customerList, c)) : null;
        
        //this is for the test InvoiceServiceTest.testChangeOpenInvoiceLIToPreviousBalanceLI()
        if (customerId.intValue() == 1000) {
            returnedCustomer = new Customer();
            returnedCustomer.setBillCycleId(new Integer(Messages.getString("billCycleId.quarterly")));
            returnedCustomer.setBillStartMonthId(1);
            returnedCustomer.setRentalCharge(new Double(40));
            returnedCustomer.setId(customerId);
        } else if (customerId.intValue() == 2000) {
            // must set the balance equal to the rental charge or vice versa
            returnedCustomer = new Customer();
            returnedCustomer.setBillCycleId(new Integer(Messages.getString("billCycleId.quarterly")));
            returnedCustomer.setBillStartMonthId(12);
            returnedCustomer.setId(customerId);
            // this value must be the same as the invoice balance value in InvoiceServiceTest.testChangeOpenInvoiceLIToPreviousBalanceLI()
            // for ci2.
            returnedCustomer.setRentalCharge(new Double(400));
        } else if (customerId.intValue() == 3000) {
            returnedCustomer = new Customer();
            returnedCustomer.setBillCycleId(new Integer(Messages.getString("billCycleId.quarterly")));
            returnedCustomer.setBillStartMonthId(12);
            returnedCustomer.setId(customerId);
            returnedCustomer.setRentalCharge(new Double(500));
        } else if (customerId.intValue() == 4000) {
            returnedCustomer = new Customer();
            returnedCustomer.setBillCycleId(new Integer(Messages.getString("billCycleId.quarterly")));
            returnedCustomer.setBillStartMonthId(12);
            returnedCustomer.setId(customerId);
            returnedCustomer.setRentalCharge(new Double(5));
        }
        return returnedCustomer;
    }
    
    
    
    /* (non-Javadoc)
     * @see com.hs.bws.service.CustomerService#getCustomerByCustomer(com.hs.bws.valueObject.Customer)
     */
    public Customer getCustomerByCustomer(Customer customer) {
        Customer returnedCustomer = null;
        // we will search through the list of customers and return the 
        // customer whose id matches the one that was passed in.
        Collections.sort(customerList);
        int index = Collections.binarySearch(customerList, customer);
        returnedCustomer = (Customer) customerList.get(index);
        return returnedCustomer;
    }
    
    
    /* (non-Javadoc)
     * @see com.hs.bws.service.CustomerService#createCustomer(com.hs.bws.valueObject.Customer)
     */
    public void createCustomer(Customer customer) {
        customerList.add(customer);
    }
    
    public void updateCustomer(Customer c) {
        customerList.set(Collections.binarySearch(customerList, c), c);
    }
    
    /* (non-Javadoc)
     * @see com.hs.bws.service.CustomerService#closeCustomerAccount(com.hs.bws.valueObject.Customer)
     */
    public void closeCustomerAccount(Customer customer) {
        customer.setCloseAccountDate(new Date());
        updateCustomer(customer);
    }
    
    
    /* (non-Javadoc)
     * @see com.hs.bws.service.CustomerService#getAllCustomersWithOpenInvoices()
     */
    public List getAllCustomersWithOpenInvoices() {
        List customerWithOpenInvoicesList = new ArrayList();
        for (int i=0; i < 10; i++) {
            Customer cust = new Customer();
            cust.setId(new Integer(-i*10));
            cust.setLastName("Jaramillo");
            cust.setFirstName("Xaviar");
            cust.setBillStartMonthId(3);
            cust.setRentalCharge(new Double(35));
            cust.setBillCycleId(new Integer(100));
            customerWithOpenInvoicesList.add(cust);
        }
        return customerWithOpenInvoicesList;
    }
    
    /* (non-Javadoc)
     * @see com.hs.bws.service.CustomerService#updateCustomerCredit(java.lang.Integer, double)
     */
    public void updateCustomerCredit(Integer id, double creditBalance)
            throws BWSGenericException {
        logger.info("Customer: " + id + " creditBalance: " + creditBalance);
    }
    
}
