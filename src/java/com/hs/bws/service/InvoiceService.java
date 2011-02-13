/**
 * File Name: InvoiceService.java
 * Date: Aug 18, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hs.bws.dao.CustomerDAO;
import com.hs.bws.dao.IInvoiceDAO;
import com.hs.bws.dao.InvoiceDAO;
import com.hs.bws.dao.ICustomerDAO;
import com.hs.bws.exception.BWSGenericException;
import com.hs.bws.exception.ConnectionNotAvailableException;
import com.hs.bws.exception.OpenInvoicesNotAvailableException;
import com.hs.bws.reporting.InvoiceReport;
import com.hs.bws.util.DatabaseUtil;
import com.hs.bws.util.Messages;
import com.hs.bws.util.SortCustomerByNameUtil;
import com.hs.bws.valueObject.Customer;
import com.hs.bws.valueObject.CustomerInvoice;
import com.hs.bws.valueObject.InvoiceLineItem;
import com.hs.bws.valueObject.Payment;
import com.hs.bws.valueObject.PendingService;

public class InvoiceService {
    static Logger logger = Logger.getLogger(InvoiceService.class);

    protected IInvoiceDAO custInvDAO = null;
    //@FIXME remove the CustomerDAO from this object.  We should only access it throught he CustService object.
    protected ICustomerDAO custDAO = null;

    protected CustomerService custService = null;

    /**
     * Constructor
     */
    public InvoiceService() {
        super();
        initializeServices();
    }

    /**
     * Apply a payment to a customer's account
     * @param invoiceList - contains a list of open invoices on the customer's account
     * @param paidAmt - the amount to be applied to the customer's account
     * @throws OpenInvoicesNotAvailableException
     */
    public void applyPayment(CustomerInvoice custInvoice, Payment incomingPayment) throws OpenInvoicesNotAvailableException {
        Integer customerId = custInvoice.getCustomerId();
        try {
            // Make sure that there are invoices
            if (custInvoice != null) {
                double invBalance = custInvoice.getInvoiceBalance().doubleValue();
               
                // Get the total invoice amount
                // Check to see if we are paying more than the balance of the invoice
                // If so, then we are actually going to close the invoice.
                if (incomingPayment.getPaymentAmt().doubleValue() >= invBalance) {
                	// Create the payment
                    Payment payment = new Payment();
                    payment.setPaymentId(new Integer(getNextPaymentId()));
                    payment.setPaymentType(incomingPayment.getPaymentType());
                    payment.setPaymentAmt(new Double(invBalance));
                    payment.setInvoiceId(custInvoice.getId());
                    custInvoice.addPayment(payment);
                    
                	// we will close the current invoice
                    custInvoice.setCloseDate(new Date(System.currentTimeMillis()));
                   
                    // we will apply the payment now
                    custInvDAO.addPayment(DatabaseUtil.getInstance().getConnection(), payment);                      
                    custInvDAO.closeInvoice(DatabaseUtil.getInstance().getConnection(), custInvoice);                    

                    // we will update the paid amount
                    incomingPayment.setPaymentAmt(new Double(incomingPayment.getPaymentAmt().doubleValue() - invBalance));
                } else {
                    // we are not paying more money than what is open on the
                    // invoice.  

                    // Set the amount that we are going to pay
                    Payment payment = new Payment();
                    payment.setPaymentId(new Integer(getNextPaymentId()));
                    payment.setPaymentType(incomingPayment.getPaymentType());
                    payment.setPaymentAmt(incomingPayment.getPaymentAmt());
                    payment.setInvoiceId(custInvoice.getId());
                    
                    // make the change to the customer's invoice
                    custInvDAO.addPayment(DatabaseUtil.getInstance().getConnection(), payment);
                    incomingPayment.setPaymentAmt(new Double(0.00));
                }
            } else {
                throw new OpenInvoicesNotAvailableException();
            }
            
            // determine if the paid amount still has money left over
            if (incomingPayment.getPaymentAmt().doubleValue() > 0) {
                // since there is stil money left over after paying the open invoice, we will add
                // it to the customer account as a credit.
                Customer cust = custService.getCustomerById(customerId);
                cust.setCreditBalance(cust.getCreditBalance() + incomingPayment.getPaymentAmt().doubleValue());
                try {
                    // update the customer credit balance
                    custService.updateCustomerCredit(cust.getId(), cust.getCreditBalance());
                } catch (BWSGenericException e1) {
                    logger.error("Error: " + e1.getMessage());
                }
            }
        } catch (SQLException e) {
            logger.info("SQLException occurred: " + e.getMessage());
        } catch (ConnectionNotAvailableException e) {
            logger.info("Connection was not available " + e.getMessage());
        }
    }

    /**
     * Initialize the services
     */
    public void initializeServices() {
        custInvDAO = new InvoiceDAO();
        custDAO = new CustomerDAO();
        custService = new CustomerService();
    }

    /**
     * Add an invoice to the customer's account
     * 
     * @param custInvoice -
     *            invoice information to be added
     */
    public void addCustomerInvoice(CustomerInvoice custInvoice) {
        logger.info("Creating the following invoices: " + custInvoice.toString());
        try {
            custInvDAO.createCustomerInvoice(DatabaseUtil.getInstance()
                    .getConnection(), custInvoice);
        } catch (SQLException e) {
            logger.info("SQLException occurred: " + e.getMessage());
        } catch (ConnectionNotAvailableException e) {
            logger.info("Connection was not available " + e.getMessage());
        }
    }

    /**
     * Add an invoice to the customer's account
     * 
     * @param custInvoice -
     *            invoice information to be added
     */
    public void deleteCustomerInvoice(CustomerInvoice custInvoice) {
        try {
            custInvDAO.deleteInvoice(DatabaseUtil.getInstance()
                    .getConnection(), custInvoice.getId());
        } catch (SQLException e) {
            logger.info("SQLException occurred: " + e.getMessage());
        } catch (ConnectionNotAvailableException e) {
            logger.info("Connection was not available " + e.getMessage());
        }
    }

    /**
     * Get the open invoices on a customer's account by the customer id
     * @param ci customer id
     * @return
     */
    public CustomerInvoice getOpenCustomerInvoices(Integer customerId) {
        CustomerInvoice custInvoice = null;
        try {
            // there can only be one open invoice at a time.
            custInvoice = custInvDAO.getOpenInvoicesByCustomerId(DatabaseUtil.getInstance().getConnection(), customerId);
            if (custInvoice != null) {
                	// retrieve the list of invoice line items
                    List iliList = this.getInvoiceLineItemList(custInvoice);
                    if (iliList != null && iliList.size() > 0) {
                        custInvoice.setInvoiceLineItemList(iliList);
                    }
                    
                    // retrieve the list of payments on this invoice.
                    List paymentList = this.getPaymentList(custInvoice);
                    if(paymentList != null && paymentList.size() > 0) {
                    	custInvoice.setPaymentList(paymentList);
                    }
                }
        } catch (SQLException e) {
            logger.info("SQLException occurred: " + e.getMessage());
        } catch (ConnectionNotAvailableException e) {
            logger.info("Connection was not available " + e.getMessage());
        }
        return custInvoice;
    }

    /**
     * Get the closed invoices on a customer's account by customer id
     * 
     * @param ci customer id
     * @return
     */
    public List getClosedCustomerInvoices(Integer customerId) {
        List invoiceList = null;
        try {
            invoiceList = custInvDAO.getClosedInvoicesByCustomerId(DatabaseUtil
                    .getInstance().getConnection(), customerId);
        } catch (SQLException e) {
            logger.info("SQLException occurred: " + e.getMessage());
        } catch (ConnectionNotAvailableException e) {
            logger.info("Connection was not available " + e.getMessage());
        }
        return invoiceList;
    }

    /**
     * Add an invoice line item to an invoice
     * 
     * @param ili -
     *            the detail to be added
     */
    public void addInvoiceLineItem(InvoiceLineItem ili) {
        try {
            custInvDAO.addInvoiceLineItem(DatabaseUtil.getInstance()
                    .getConnection(), ili);
        } catch (SQLException e) {
            logger.info("SQLException occurred: " + e.getMessage());
        } catch (ConnectionNotAvailableException e) {
            logger.info("Connection was not available " + e.getMessage());
        }
    }

    /**
     * Delete an invoice line item from an invoice
     * 
     * @param ili -
     *            invoice line item to be deleted
     */
    public void deleteInvoiceLineItem(Integer invoiceLineItemId) {
        try {
            custInvDAO.deleteInvoiceLineItem(DatabaseUtil.getInstance()
                    .getConnection(), invoiceLineItemId);
        } catch (SQLException e) {
            logger.info("SQLException occurred: " + e.getMessage());
        } catch (ConnectionNotAvailableException e) {
            logger.info("Connection was not available " + e.getMessage());
        }
    }

    /**
     * Update an invoice line item
     * 
     * @param ili -
     *            updated invoice line item detail
     */
    public void updateInvoiceLineItem(InvoiceLineItem ili) {
        try {
            custInvDAO.updateInvoiceLineItem(DatabaseUtil.getInstance()
                    .getConnection(), ili);
        } catch (SQLException e) {
            logger.info("SQLException occurred: " + e.getMessage());
        } catch (ConnectionNotAvailableException e) {
            logger.info("Connection was not available " + e.getMessage());
        }
    }

    /**
     * Get an invoice with the invoice detail and payment information
     * @param invoiceId -
     *            invoice id to of the invoice to be retrieved
     * @return CustomerInvoice object
     */
    public CustomerInvoice getCustomerInvoice(Integer invoiceId) {
        CustomerInvoice custInvoice = null;

        try {
            custInvoice = custInvDAO.getCustomerInvoice(DatabaseUtil
                    .getInstance().getConnection(), invoiceId);

            // Set the line item list
            if (custInvoice != null) {
                custInvoice.setInvoiceLineItemList(this.getInvoiceLineItemList(custInvoice));
                custInvoice.setPaymentList(this.getPaymentList(custInvoice));
            }
        } catch (SQLException e) {
            logger.info("SQLException occurred: " + e.getMessage());
        } catch (ConnectionNotAvailableException e) {
            logger.info("Connection was not available " + e.getMessage());
        }

        return custInvoice;
    }

    /**
	 * @param custInvoice
	 * @return
	 */
	private List getPaymentList(CustomerInvoice custInvoice) {
		List paymentList = null;
		
		try {
			paymentList = custInvDAO.getPaymentListByInvoiceId(
					DatabaseUtil.getInstance().getConnection(), custInvoice);
		} catch(SQLException e) {
			logger.info("SQLException occurred: " + e.getMessage());
		} catch(ConnectionNotAvailableException e) {
			logger.info("Connection was not available: " + e.getMessage());
		}
		return paymentList;
	}

	/**
     * Get the invoice line item list for an invoice
     * 
     * @param ci -
     *            contains the invoice id needed to get the line items
     * @return List of invoice line items
     */
    protected List getInvoiceLineItemList(CustomerInvoice ci) {
        List invoiceLineItemList = null;

        try {
            invoiceLineItemList = custInvDAO.getInvoiceLineItemListByInvoiceId(
                    DatabaseUtil.getInstance().getConnection(), ci.getId());
        } catch (SQLException e) {
            logger.info("SQLException occurred: " + e.getMessage());
        } catch (ConnectionNotAvailableException e) {
            logger.info("Connection was not available " + e.getMessage());
        }

        return invoiceLineItemList;
    }

    /**
     * Will retrieve the customers who should be billed this  month
     * @param monthId
     */
    public List getCustomersBilledForThisMonthList(java.util.Date billCycleRunDate) {
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
        logger.info("Date that the bill cycle is being run for is " + billCycleRunDate);
        int monthId = new Integer(sdfMonth.format(billCycleRunDate)).intValue();
        logger.info("Month being billed is month " + monthId);
        // retrieve all of the customers
        List customerList = custService.getAllCustomers();
        List customersToBeBilledList = new ArrayList();

        // using the complete list of customers, determine which ones need to be
        // billed
        for (Iterator iter = customerList.iterator(); iter.hasNext();) {
            Customer cust = (Customer) iter.next();
            // get rid of those customers who has closed their account prior to this billing cycle run
            if (cust.getCloseAccountDate() != null && cust.getCloseAccountDate().before(billCycleRunDate)) {
                continue;
            }
            // determine if this customer is due to be billed according to the date
            if (isCustSchduledToBeBilled(cust, billCycleRunDate)) {
                customersToBeBilledList.add(cust);
            }
        }

        return customersToBeBilledList;
    }

    /**
     * Will run the normal billing cycle for a particular date.
     * @param date - date used to run the billing cycle
     */
    public void generateInvoicesByRunDate(java.util.Date billCycleRunDate) {
        // we will first generate invoices for those customers who are scheduled to
        // be billed normally.
        List scheduledInvoiceList = generateScheduledInvoices(billCycleRunDate);
        
        // we will now retrieve all of the invoices that have an open balance.
        List openInvoiceList = getOpenInvoices();
        
        // we will now retrieve invoices for customers who have a pending service
        List pendServInvoiceList = getPendServiceInvoices();

        // since it is possible that a customer can have a combination of the above:
        //  - scheduled to be billed invoices
        //  - open Inovice
        //  - pending services
        // ...we must aggregate those invoices into one per customer
        // this will be the list of all the invoices
        List allInvoices = new ArrayList();
        
        // determine if there were any scheduled inovices
        if (scheduledInvoiceList != null) {
            allInvoices.addAll(scheduledInvoiceList);
        }
        
        // determine if there were any pending service invoices
        if (pendServInvoiceList != null) {
            logger.info("Will add " + pendServInvoiceList.size() + " invoices generated from pending services");
            allInvoices.addAll(pendServInvoiceList);
        }
        
        java.util.Date dueDate = this.getInvoiceDueDateByBillCycleDate(billCycleRunDate);
        // determine if there were any open invoices
        if (openInvoiceList != null) {
            // since there were some open invoices, we must update the line items of these
            // invoices to only one line item with a previous balance.
            changeOpenInvoiceLIToPreviousBalanceLI(openInvoiceList, dueDate);
            allInvoices.addAll(openInvoiceList);
        }
        
        List generatedInvoices = aggregateListOfInvoicesByCustomer(allInvoices, dueDate, billCycleRunDate);
        // create all of the invoices in the database
        if (generatedInvoices != null) {
            this.createCustomerInvoices(generatedInvoices);
        }

        // close all of the open invoices, since their totals will be assigned to the new invoices
        if (openInvoiceList != null) {
            closeInvoices(openInvoiceList);
        }
    }
    
    /**
     * Will create invoices based on the pending services.
     * @return
     */
    private List getPendServiceInvoices() {
        List pendingServInvoiceList = new ArrayList();
        List pendServList = null;
        try {
            // retrieve all of the pending services.
            pendServList = this.custInvDAO.getAllPendingServices(DatabaseUtil.getInstance().getConnection());
            
            // determine if there were any pending services retrieved.
            if (pendServList != null && pendServList.size() > 0) {
                // since there were pending services retrieved, we will change them into invoices and add them to the 
                // pendingServInvoiceList.
                for (Iterator pendServIter = pendServList.iterator(); pendServIter.hasNext();) {
                    PendingService ps = (PendingService) pendServIter.next();
                    CustomerInvoice custInvoice = new CustomerInvoice();
                    custInvoice.setCreateDate(new java.util.Date());
                    custInvoice.setCustomerId(new Integer(ps.getCustomerId()));
                    custInvoice.addInvoiceLineItem(ps.buildInvoiceLineItem());
                    pendingServInvoiceList.add(custInvoice);
                    deletePendingService(ps);
                }
            }
        } catch (Exception e) {
            logger.error("There was a problem with retrieveing all of the pending services. Message: " + e.getMessage(), e);
        }
        return pendingServInvoiceList;
    }

    /**
     * @param openInvoiceList
     */
    private void closeInvoices(List openInvoiceList) {
        for (Iterator iter = openInvoiceList.iterator(); iter.hasNext();) {
            CustomerInvoice ci = (CustomerInvoice) iter.next();
            ci.setCloseDate(new java.util.Date());
            closeInvoice(ci);
        }
    }

    /**
     * @param ci
     */
    private void closeInvoice(CustomerInvoice ci) {
        try {
            custInvDAO.closeInvoice(DatabaseUtil.getInstance()
                    .getConnection(), ci);
        } catch (SQLException e) {
            logger.info("SQLException occurred: " + e.getMessage());
        } catch (ConnectionNotAvailableException e) {
            logger.info("Connection was not available " + e.getMessage());
        }
    }

    /**
     * @param generatedInvoices
     */
    public void createCustomerInvoices(List generatedInvoices) {
        for (Iterator iter = generatedInvoices.iterator(); iter.hasNext();) {
            CustomerInvoice ci = (CustomerInvoice) iter.next();
            ci.setId(new Integer(getNextInvoiceId()));
            addCustomerInvoice(ci);
            
            for (Iterator iliIter = ci.getInvoiceLineItemList().iterator(); iliIter.hasNext();) {
                InvoiceLineItem ili = (InvoiceLineItem) iliIter.next();
                ili.setInvoiceId(ci.getId());
                addInvoiceLineItem(ili);
            }
            
            Customer customer = new Customer();
            
            try {
            	customer.setId(ci.getCustomerId());
            	List singleCustomerList = custDAO.getMatchingCustomers(DatabaseUtil.getInstance().getConnection(), customer);
            	
            	// Get the customer so we can get their current credit balance
            	for (Iterator custListIter = singleCustomerList.iterator(); custListIter.hasNext();) {
					customer = (Customer) custListIter.next();
					break;
				}
            	
            	// IF the customer has a credit balance, apply it towards the invoice
            	if(customer.getCreditBalance() > 0) {
            		// Set the customer credit to 0, since we are applying the whole credit
            		// as a payment.  If there is leftover money after the payment is made,
            		// a new credit will be issued.
            		try {
            			custService.updateCustomerCredit(customer.getId(), 0.00);
            		} catch(BWSGenericException e) {
            			logger.error("Error updating customer credit: " + e.getMessage());
            		}
            		
            		Payment payment = new Payment();
            		payment.setPaymentAmt(new Double(customer.getCreditBalance()));
            		payment.setPaymentType("C");
            		// Apply the payment
            		this.applyPayment(ci, payment);
            	}
            } catch (SQLException e) {
                logger.info("SQLException occurred: " + e.getMessage());
            } catch (ConnectionNotAvailableException e) {
                logger.info("Connection was not available " + e.getMessage());
            } catch (OpenInvoicesNotAvailableException ipnaex) {
            	logger.info("OpenInvoicesNotAvailableException occurred: " + ipnaex.getMessage());
            }
            
        }
    }
    
    /**
     * Will retrieve the next available invoice id.
     * @return
     */
    public int getNextInvoiceId() {
        int invoiceId = 0;
        try {
            invoiceId = custInvDAO.getNextInvoiceId(DatabaseUtil.getInstance().getConnection());
        } catch (Exception e) {
            logger.error("There was a problem retreiving the next invoice id");
        }
        return invoiceId;
    }

    /**
     * Will retrieve the next available invoice id.
     * @return
     */
    public int getNextPaymentId() {
        int paymentId = 0;
        try {
            paymentId = custInvDAO.getNextPaymentId(DatabaseUtil.getInstance().getConnection());
        } catch (Exception e) {
            logger.error("There was a problem retreiving the next payment id");
        }
        return paymentId;
    }    
    
    /**
     * @param billCycleRunDate
     */
    public List generateScheduledInvoices(java.util.Date billCycleRunDate) {
        // @TODO when generating invoices, must look at the credit for the customer and apply if any is available.
        List custBilledNormallyList = this.getCustomersBilledForThisMonthList(billCycleRunDate);
        List normalBillingInvoiceList = new ArrayList();
        for (Iterator iter = custBilledNormallyList.iterator(); iter.hasNext();) {
            Customer cust = (Customer) iter.next();
            // we will create the invoices for the customers who would normally be billed during this period
            CustomerInvoice custInvoice = new CustomerInvoice();
            custInvoice.setCreateDate(new java.util.Date());
            custInvoice.setCustomerId(cust.getId());
            custInvoice.setPaymentAmt(new Double(0));
            
            // we will create the default line item which is the rental charge
            InvoiceLineItem invLI = new InvoiceLineItem();
            invLI.setChargeAmount(cust.getRentalCharge());
            invLI.setItemDesc("Rental Charge");
            custInvoice.addInvoiceLineItem(invLI);
            
            try {
                // we will also determine if there are any pending services that need to be applied to the invoice
                List pendingServicesList = getPendingServicesByCustomerId(cust.getId().intValue());
                if (pendingServicesList != null && pendingServicesList.size() > 0) {
                    for (Iterator pendingServiceListIter = pendingServicesList.iterator(); pendingServiceListIter
                            .hasNext();) {
                        PendingService pendingServ = (PendingService) pendingServiceListIter.next();
                        custInvoice.addInvoiceLineItem(pendingServ.buildInvoiceLineItem());
                        deletePendingService(pendingServ);
                    }
                }
            } catch (BWSGenericException e) {
                e.printStackTrace();
            }
            normalBillingInvoiceList.add(custInvoice);
        }
        return normalBillingInvoiceList;
    }

    /**
     * Will delete the given pending service
     * @param pendingServicesList
     */
    public void deletePendingService(PendingService pendingService) {
        try {
            custInvDAO.deletePendingService(DatabaseUtil.getInstance().getConnection(), pendingService.getId());
        } catch (Exception e) {
            logger.error("There was a problem deleting all of the pending services that were applied to the invoices");
        }
    }
    
    /**
     * Will delete generated invoices for the specified bill date
     * @param billDate as Date
     */
    public void deleteInvoicesByBillDate(java.util.Date billDate) {
    	try {
    	    // this is expecting a fully hydrated invoice with line items and payments
    		List invoiceList = this.getInvoicesByBillDate(billDate);
    		
    		if(invoiceList != null) {
    			// I'm going to see how this turns out.  I plan on creating the in clause for
    			// the invoices generated
    			String inClause = "";
    			for (Iterator invoiceIter = invoiceList.iterator(); invoiceIter.hasNext();) {
					CustomerInvoice ci = (CustomerInvoice) invoiceIter.next();
					inClause += ci.getId().toString() + ", ";
					
					// Get the payment list for this invoice
					List paymentList = this.getPaymentList(ci);
					
					// See if there were any payments against this invoice
					if(paymentList != null) {
						double creditAmt = 0.0;
						for (Iterator paymentIter = paymentList.iterator(); paymentIter.hasNext();) {
							Payment payment = (Payment) paymentIter.next();
							creditAmt += payment.getPaymentAmt().doubleValue();
						}
						
						// Create a customer object and set the id so we can lookup the customer information
						Customer customer = new Customer();
						customer.setId(ci.getCustomerId());
						
						try {
							// Get the customer information for this customer, so we can get their current credit balance
							List custList = custDAO.getMatchingCustomers(DatabaseUtil.getInstance().getConnection(), customer);
							
							// Make sure we found at least one customer (should only be 1 customer)
							if(custList != null) {
								for (Iterator custIter = custList.iterator(); custIter.hasNext();) {
									 customer = (Customer) custIter.next();
									 break;
								}
								//  Set the new credit balance and then update it
								customer.setCreditBalance(customer.getCreditBalance() + creditAmt);								
								custDAO.updateCustomerCredit(DatabaseUtil.getInstance().getConnection(), 
																customer.getId(), customer.getCreditBalance());
							}
						} catch(ConnectionNotAvailableException e) {
							logger.error("Connection not available while getting matching customers: " + e.getMessage());
						} catch(SQLException e) {
							logger.error("SQLException while getting matching customers: " + e.getMessage());
						}
					}
				}
    			// Remove the , and space from the end of the inClause
    			if(!inClause.equals("")) {
    				inClause = inClause.substring(0, (inClause.length() - 2));
    			}
    			
    			// Delete all payments, invoice line items and invoices for this billDate
    			custInvDAO.deletePaymentsByInvoiceIds(DatabaseUtil.getInstance().getConnection(), inClause);
    			custInvDAO.deleteInvoiceLineItemsByInvoiceIds(DatabaseUtil.getInstance().getConnection(), inClause);
    			custInvDAO.deleteInvoicesByBillDate(DatabaseUtil.getInstance().getConnection(), billDate);
    		}
    		
    	} catch(SQLException sqlex) {
    		logger.error("SQLException while deleting invoices by bill date: " + sqlex.getMessage());
    	} catch(ConnectionNotAvailableException cnaex) {
    		logger.error("Connection not available while trying to delete invoices by bill date: " + cnaex.getMessage());
    	}
    }

    /**
     * This method is responsible for retrieving all of the pending services by customer id
     * @param customerId represents the customer that we will look for pending services
     * @return BWSGenericException
     * @throws BWSGenericException
     */
    public List getPendingServicesByCustomerId(int customerId) throws BWSGenericException {
        List pendingServiceList = null ;
        try {
            pendingServiceList = custInvDAO.getPendingServiceByCustomerId(DatabaseUtil.getInstance().getConnection(), customerId);
        } catch (Exception e) {
            logger.error("Error occurred when trying to get pending services by customer id " + customerId);
            throw new BWSGenericException("Error occured when trying to get pending services by customer id " + customerId);
        }
        return pendingServiceList;
    }

    /**
     * The purpose of this method is to take a list of invoices, and aggregate them
     * so that there is only one invoice by customer
     * - will update the customer id, create date, due date, billDate
     * @param completeListOfInvoices
     * @return List of aggregated invoices
     */
    public List aggregateListOfInvoicesByCustomer(List completeListOfInvoices, java.util.Date dueDate,
    		java.util.Date billDate) {
        Map customerMap = new HashMap();
        CustomerInvoice permanentInv = null;
        for (Iterator iter = completeListOfInvoices.iterator(); iter.hasNext();) {
            CustomerInvoice currInv = (CustomerInvoice) iter.next();
            // determine if we have already processed an invoice for this customer.
            if (customerMap.get(currInv.getCustomerId()) != null) {
                // we have already processed an invoice for this customer, we will use that one.
                permanentInv = (CustomerInvoice) customerMap.get(currInv.getCustomerId());
            } else {
                // we have not yet processed an invoice for this customer, we will build it.
                permanentInv = new CustomerInvoice();
                permanentInv.setCustomerId(currInv.getCustomerId());
                permanentInv.setCreateDate(new java.util.Date());
                permanentInv.setDueDate(dueDate);
                permanentInv.setBillDate(billDate);
            }
            // add all of the line items associated with this to the permanent one
            // stored in the map
            permanentInv.addInvoiceLineItem(currInv.getInvoiceLineItemList());
            customerMap.put(permanentInv.getCustomerId(), permanentInv);
        }
        return new ArrayList(customerMap.values());
    }

    /**
     * This method is responsible for retreiving a list of
     * Invoices for those customers who have open invoices.
     * @return List - full of customerInvoices.
     */
    public List getOpenInvoices() {
        List openInvoiceList = new ArrayList();
        try {
            openInvoiceList = custInvDAO.getOpenInvoices(DatabaseUtil.getInstance().getConnection());
            
            if(openInvoiceList != null) {
	            // for each open invoice, we will hydrate it with the appropriate 
	            // line items
	            for (Iterator iter = openInvoiceList.iterator(); iter.hasNext();) {
	                CustomerInvoice ci = (CustomerInvoice) iter.next();
	                ci.setInvoiceLineItemList(this.getInvoiceLineItemList(ci));
	                ci.setPaymentList(this.getPaymentList(ci));
	            }
            }
        } catch (Exception e) {
            logger.error("There was a problem retrieving the open invoices. " + e.getMessage(), e);
        }
        return openInvoiceList;
    }
    
    
    /**
     * Will take the given invoice, and fill it with the line items that are associated with it
     * This method will return the same invoice that was passed into it
     * @param inv - this is the invoice that will be populated with line items
     * @return CustomerInvoice the same invoice passed in, but it will have the line items filled
     */
    public CustomerInvoice populateInvoiceLineItems(CustomerInvoice inv) {
        // we will retrieve all of the line items for this invoice
        List invLineItemList = getInvoiceLineItemList(inv);
        if (invLineItemList != null && invLineItemList.size() > 0) {
            inv.setInvoiceLineItemList(invLineItemList);
        }
        return inv;
    }

    /**
     * Will retrieve a list of customers who have open invoices
     * @return List of Customers
     */
    public List getCustomersWithOpenInvoices() {
        List customerList = null;
        try {
            // this method will retrieve all of the open invoices
            List openInvoiceList = custInvDAO.getOpenInvoices(DatabaseUtil.getInstance().getConnection());
            if (openInvoiceList != null && openInvoiceList.size() > 0) {
                customerList = new ArrayList();
                for (Iterator iter = openInvoiceList.iterator(); iter.hasNext();) {
                    // using the information from the open invoices, we will retrieve the customer information
                    CustomerInvoice custInv = (CustomerInvoice) iter.next();
                    Customer cust = custService.getCustomerById(custInv.getCustomerId());
                    customerList.add(cust);
                }
            }
        } catch (SQLException e) {
            logger.info("Exception occurred: " + e.getMessage());
        } catch (ConnectionNotAvailableException e) {
            logger.info("Exception occurred: " + e.getMessage());
        }
        return customerList;
    }
    
    /**
     * Will retrieve all of the invoices for a given customer
     * @param customerId the id of the customer
     * @return List full of CustomerInvoice objects
     */
    public List getInvoicesByCustomer(Integer customerId) {
        List invoiceList = new ArrayList();
        CustomerInvoice openInvoice = null;
        List closedInvoiceList = null;
        openInvoice = getOpenCustomerInvoices(customerId);
        closedInvoiceList = getClosedCustomerInvoices(customerId);
        
        if (openInvoice != null) {
            invoiceList.add(openInvoice);
        }
        
        if (closedInvoiceList != null && closedInvoiceList.size() > 0) {
            logger.info("Closed Invoice list has a size of " + closedInvoiceList.size());
            populateInvoiceLineItemsWithInvoiceList(closedInvoiceList);
            invoiceList.addAll(closedInvoiceList);
        }
        
        return invoiceList;
    }
    
    /**
     * @param openInvoiceList
     */
    private void populateInvoiceLineItemsWithInvoiceList(List openInvoiceList) {
        if (openInvoiceList != null && openInvoiceList.size() > 0) {
            for (Iterator iter = openInvoiceList.iterator(); iter.hasNext();) {
                CustomerInvoice custInv = (CustomerInvoice) iter.next();
                custInv = populateInvoiceLineItems(custInv);
                logger.info("Now the value of the invoiceList is " + custInv.getInvoiceTotal());
            }
        }
    }

    /**
     * This method will apply payments to the customer invoices
     * @param custId customer id of the customer who we would like to apply the payment to
     * @param paymentAmt this is the payment amount that we are going to apply
     * @throws OpenInvoicesNotAvailableException - exception is thrown when there are not any open invoices to apply payments to
     */
    public void applyPaymentByCustIdAndPaymentAmt(Integer custId, Payment incomingPayment) throws OpenInvoicesNotAvailableException {
        CustomerInvoice openInvoice = null;
        // we must retrieve all of the open invoices for the given customer
        openInvoice = getOpenCustomerInvoices(custId);
        
        Payment payment = new Payment();
        payment.setPaymentAmt(incomingPayment.getPaymentAmt());
        payment.setPaymentType(incomingPayment.getPaymentType());
        applyPayment(openInvoice, payment);
    }
    
    public java.util.Date getInvoiceDueDateByBillCycleDate(java.util.Date billCycleDate) {
        Date d = null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(billCycleDate);
        cal.set(Calendar.DAY_OF_MONTH, 10);
        return cal.getTime();
    }

    /**
     * Will take a list of Customer Invoices that were open from the previous billing cycle run
     * and change their invoice line items to either Prev. Balance or Rental Charge or a combination of both.
     * @param invoiceList
     * @param dueDate - represents the date the invoice is due to be paid.
     */
    public void changeOpenInvoiceLIToPreviousBalanceLI(List invoiceList, Date dueDate) {
        //@TODO we must determine if this is still the customers billing cycle. 
        // (Have they already recieved a billing for this billing cycle, or is this their first bill of their billing cycle)
        // 	yes - compare amt. owed to Rental Charge, and take the appropriate action.
        //				amt. owed == Rental Charge 	- change verbage to Rental Charge
        //				amt. owed < Rental Charge		- change verbage to Prev. Balance
        //				amt. owed > Rental Charge		- create two line items, one for Rental Charge other for Prev. Balance
        //		no - Since this is the first bill of the billing cycle, then we will change the verbage to Prev. Balance.
        for (Iterator iter = invoiceList.iterator(); iter.hasNext();) {
            boolean createRentalCharge = false;
            String lineItemDesc = "Prev. Balance";
            CustomerInvoice custInv = (CustomerInvoice) iter.next();
            Customer cust = custService.getCustomerById(custInv.getCustomerId());
            logger.info("Customer looks like: " + cust);
            if (!isCustSchduledToBeBilled(cust, dueDate)) {
                // this customer is not scheduled to be billed but they have an open balance
                // we will now determine how that oopen balance compares to the rental charge
                if (custInv.getInvoiceBalance().equals(cust.getRentalCharge())) {
                    // since the open balance was equal to the rental charge, then we will
                    // keep the verbage for Rental Charge
                    lineItemDesc = "Rental Charge";
                } else if (custInv.getInvoiceBalance().doubleValue() < cust.getRentalCharge().doubleValue()) {
                    // the balance was less than the rental charge.  We will change the verbage to Prev Balance
                    lineItemDesc = "Prev. Balance";
                } else if (custInv.getInvoiceBalance().doubleValue() > cust.getRentalCharge().doubleValue()) {
                    // the balance was greater than the rental charge.
                    createRentalCharge = true;
                    //We will create two new invoice line items.
                    // one for the rental charge and one for the previous balance.  Since we will by default create
                    // a line item for the Prev. Balance, we will just add one for the rental charge here.
                }
            }
            InvoiceLineItem ili = new InvoiceLineItem();
            // determine if we are going to create a rental charge line item
            if (createRentalCharge) {
                // we will calculate the difference of the total amount owed and the rental charge.  This will be the Prev. Balance amount
                ili.setChargeAmount(new Double(custInv.getInvoiceBalance().doubleValue() - cust.getRentalCharge().doubleValue()));
            } else {
                // we will just apply the full amount owed.
                ili.setChargeAmount(custInv.getInvoiceBalance());
            }
            ili.setInvoiceId(custInv.getId());
            ili.setItemDesc(lineItemDesc);
            custInv.getInvoiceLineItemList().clear();
            custInv.addInvoiceLineItem(ili);
            // determine if we should create a Rental Charge line item.
            if (createRentalCharge) {
                InvoiceLineItem rentalILI = new InvoiceLineItem();
                rentalILI.setChargeAmount(cust.getRentalCharge());
                rentalILI.setInvoiceId(custInv.getId());
                rentalILI.setItemDesc("Rental Charge");
                custInv.addInvoiceLineItem(rentalILI);
            }
        }
    }
    
    /**
     * Will take a bill date and retrieve all the invoices created on that date.
     * Then will get the customer information for each invoice and send it to 
     * the invoice report to be printed.
     * @param billDate
     */
    public void printInvoicesByBillDate(java.util.Date billDate) {
    	try {
    		// Get the list of invoices for this billing date
    		List invoiceList = this.getInvoicesByBillDate(billDate);
    		
    		if(invoiceList != null) {
    			// Loop over each invoice and update its customer information
    		    List invoiceWCustList = new ArrayList();
    			for(Iterator iter = invoiceList.iterator(); iter.hasNext();) {
    			    CustomerInvoice custInv = (CustomerInvoice) iter.next();
    			    // determine if the invoice is closed or not
    			    custInv.setInvoiceLineItemList(this.getInvoiceLineItemList(custInv));
    			    custInv.setPaymentList(this.getPaymentList(custInv));
    			    if (!custInv.isOpen()) {
    			        continue;
    			    }
    			    // the invoice is open so we will continue
    			    Customer cust = custService.getCustomerById(custInv.getCustomerId());
    			    if (cust != null) {
    			        // check to see if the customer's account has been closed
    			        if (cust.getCloseAccountDate() != null) {
    			            // since this customers account has been closed, we will skip it.
    			            continue;
    			        }
    			        custInv.setCustomer(cust);
    			    }
    			    
    			    invoiceWCustList.add(custInv);
    			}
    			
    			// sort the customer invoice list
    			Collections.sort(invoiceWCustList, new SortCustomerByNameUtil());
    			Collections.reverse(invoiceWCustList);
    			
    			// print each customer invoice
    			for (Iterator iter = invoiceWCustList.iterator(); iter
                        .hasNext();) {
    				printInvoice((CustomerInvoice) iter.next());
                }
    		}
    	} catch(Exception ex) {
    		logger.info("Error in printInvoicesByBillDate: " + ex.getMessage());
    	}
    }
    
    /**
     * Prints an individual invoice
     * 
     * @param customerInvoice as CustomerInvoice
     */
    public void printInvoice(CustomerInvoice customerInvoice) {
    	try {
    		customerInvoice = this.getCustomerInvoice(customerInvoice.getId());
    		Customer customer = (Customer) custDAO.getMatchingCustomers(DatabaseUtil.getInstance().getConnection(), new Customer(customerInvoice.getCustomerId())).get(0);

    		// Make sure we successfully retrieved the customer information and then print the invoice
    		if(customer != null) {
    			InvoiceReport.getInstance().printInvoice(customerInvoice, customer);
    		}
    	} catch(ConnectionNotAvailableException cnaax) {
    		logger.info("ConnectionNotAvailableException in printInvoice: " + cnaax.getMessage());
    	} catch (SQLException e) {
            // @TODO Auto-generated catch block
    	    logger.error("SQLError Exception " + e.getMessage(), e);
        }
    }
    
    /**
     * This method is used to update the pending service
     * @param ps - Pending Service that will be updated
     * @throws BWSGenericException
     */
    public void updatePendingService(PendingService ps) throws BWSGenericException {
        try {
            custInvDAO.updatePendingService(DatabaseUtil.getInstance().getConnection(), ps);
        } catch (Exception e) {
            logger.error("Problems updating the pending service " + ps.toString());
            throw new BWSGenericException("Problems occurred when trying to update the pending service");
        }
    }
    
    /**
     * This method is used to create the pending service
     * @param ps - Pending Service that will be created
     * @throws BWSGenericException
     */
    public void createPendingService(PendingService ps) throws BWSGenericException {
        try {
            custInvDAO.createPendingService(DatabaseUtil.getInstance().getConnection(), ps);
        } catch (Exception e) {
            logger.error("Problems creating the pending service " + ps.toString());
            throw new BWSGenericException("Problems occurred when trying to create the pending service");
        }
    }
    
    /**
     * Will retrieve all invoices for the bill date
     * @param billDate as Date
     * @return List of invoices
     */
    public List getInvoicesByBillDate(java.util.Date billDate) {
    	List invoiceList = null;
    	try {
    		invoiceList = custInvDAO.getInvoicesByBillDate(DatabaseUtil.getInstance().getConnection(), billDate);
    	} catch(ConnectionNotAvailableException e) {
    		logger.error("Connection not available while getting invoices by bill date: " + e.getMessage());
    	}
    	return invoiceList;
    }

    /**
     * Will determine if a customer is due to be billed based on the customers billing cycle id, bill start month,
     * and the date that is passed in.
     * @param cust1
     * @param time
     * @return
     */
    public boolean isCustSchduledToBeBilled(Customer cust, Date date) {
        boolean dueToBeBilled = false;
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
        int monthId = new Integer(sdfMonth.format(date)).intValue();
        if (cust.getBillCycleId().intValue() == Integer.parseInt(Messages.getString("billCycleId.annual"))) {
            // customer is billed annually
            if (cust.getBillStartMonthId() == monthId) {
                dueToBeBilled = true;
            }
        } else if (cust.getBillCycleId().intValue() == Integer.parseInt(Messages.getString("billCycleId.semiannual"))) {
            // customer is billed semi annually
            int secondSAMonth = (monthId + 6) % 12;
            secondSAMonth = (secondSAMonth == 0) ? 12 : secondSAMonth;
            
            if (cust.getBillStartMonthId() == monthId
                    || cust.getBillStartMonthId() == secondSAMonth) {
                dueToBeBilled = true;
            }
            
        } else if (cust.getBillCycleId().intValue() == Integer.parseInt(Messages.getString("billCycleId.quarterly"))) {
            // customer is billed quarterly

            int secondQtrMonth = (monthId + 3) % 12;
            int thirdQtrMonth = (secondQtrMonth + 3) % 12;
            int fourthQtrMonth = (thirdQtrMonth + 3) % 12;

            // we will have to handle the case when the quarter months are a value of 0
            // if the months were 0 based, this would not be necessary.  Since they are not, 
            // we have to check the quarters for 0 and add set it to 12 which represents December
            secondQtrMonth = (secondQtrMonth == 0) ? 12 : secondQtrMonth;
            thirdQtrMonth = (thirdQtrMonth ==0) ? 12 : thirdQtrMonth;
            fourthQtrMonth = (fourthQtrMonth == 0) ? 12 : fourthQtrMonth;
            
            if (cust.getBillStartMonthId() == monthId
                    || cust.getBillStartMonthId() == secondQtrMonth
                    || cust.getBillStartMonthId() == thirdQtrMonth
                    || cust.getBillStartMonthId() == fourthQtrMonth) {
                dueToBeBilled = true;
            }
        } else if (cust.getBillCycleId().intValue() == Integer.parseInt(Messages.getString("billCycleId.monthly"))) {
            dueToBeBilled = true;
        }
        return dueToBeBilled;
    }
    
}