/**
 * File Name: CustomerInvoice.java
 * Date: August 16, 2004
 * Project Name: bwsBillingSystem
 * Description: Value object for the Customer Invoice
 */
package com.hs.bws.valueObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author Adam F. Sova
 */
public class CustomerInvoice implements Cloneable {
	private Integer id;
	private Integer customerId;
	private Date createDate;
	private Date dueDate;
	private Date closeDate;
	private Date billDate;
	private Double paymentAmt = new Double(0.00);
	private List invoiceLineItemList = new ArrayList();
	private List paymentList = new ArrayList();
	private Double paidAmt = new Double(0.00);
	private String paymentType;
	private Customer customer;
	
    /**
     * @return Returns the customer.
     */
    public Customer getCustomer() {
        return customer;
    }
    /**
     * @param customer The customer to set.
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    /**
     * @return Returns the invoiceLineItemList.
     */
    public List getInvoiceLineItemList() {
        return invoiceLineItemList;
    }
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id = " + id);
        sb.append(", customerId = " + customerId);
        sb.append(", createDate = " + createDate);
        sb.append(", dueDate = " + dueDate);
        sb.append(", closeDate = " + closeDate);
        sb.append(", paidAmt = " + paymentAmt);
        sb.append(", paymentType = " + paymentType);
        sb.append(", billDate = " + billDate);
        if (invoiceLineItemList != null && invoiceLineItemList.size() > 0) {
            sb.append("\n\t invoiceLineItems \n\t" + invoiceLineItemList.toString() + "\n");
        }
        return sb.toString();
    }
    /**
     * @param invoiceLineItemList The invoiceLineItemList to set.
     */
    public void setInvoiceLineItemList(List invoiceLineItemList) {
        this.invoiceLineItemList = invoiceLineItemList;
    }
    
    /**
     * This is the invoice total - what was paid 
     *
     */
    public Double getInvoiceBalance() {
        return new Double(getInvoiceTotal().doubleValue() - getPaidAmt().doubleValue());
    }
	/**
	 *  Constructor 
	 */
	public CustomerInvoice() {}	
	
	/**
	 *  Constructor 
	 */
	public CustomerInvoice(Integer id) { this.id = id;}	
	
	/**
	 * @return Returns the closeDate.
	 */
	public Date getCloseDate() {
		return closeDate;
	}
	/**
	 * @param closeDate The closeDate to set.
	 */
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	
	/**
	 * @return Returns the createDate.
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate The createDate to set.
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return Returns the customerId.
	 */
	public Integer getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId The customerId to set.
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return Returns the dueDate.
	 */
	public Date getDueDate() {
		return dueDate;
	}
	/**
	 * @param dueDate The dueDate to set.
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	/**
	 * @return Returns the dueDate.
	 */
	public Date getBillDate() {
		return billDate;
	}
	/**
	 * @param dueDate The dueDate to set.
	 */
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}	
	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return Returns the paidAmount.
	 */
	public Double getPaymentAmt() {
		return paymentAmt;
	}
	/**
	 * @param paidAmount The paidAmount to set.
	 */
	public void setPaymentAmt(Double paidAmount) {
		this.paymentAmt = paidAmount;
	}
    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
	/**
	 * @return Returns the invoiceTotal.
	 */
	public Double getInvoiceTotal() {
	    if (this.invoiceLineItemList != null && invoiceLineItemList.size() > 0) {
	        double total = 0.00;
	        for (Iterator iter = invoiceLineItemList.iterator(); iter.hasNext();) {
                InvoiceLineItem ili = (InvoiceLineItem) iter.next();
                total += ili.getChargeAmount().doubleValue();
                
            }
	        return new Double(total);
	    } else {
	        return  new Double(0.00);
	    }
	}
	public void addInvoiceLineItem(InvoiceLineItem ili) {
	    if (this.invoiceLineItemList == null) {
	        invoiceLineItemList = new ArrayList();
	    }
	    ili.setInvoiceId(this.getId());
	    invoiceLineItemList.add(ili);
	}
	
	public void addInvoiceLineItem(List iliList) {
	    if (this.invoiceLineItemList == null) {
	        invoiceLineItemList = new ArrayList();
	    }
	    invoiceLineItemList.addAll(iliList);
	}
	
	/**
	 * Will determine if the invoice is open or closed based on the
	 * balance and the closed date
	 * Currently, if the close date is not null or if the balance is = 0
	 * then it is considered to be closed and not open
	 * @return
	 */
	public boolean isOpen() {
	    boolean isOpen = true;
	    if (this.closeDate != null || this.getInvoiceBalance().intValue() == 0) {
	        isOpen = false;
	    }
	    return isOpen;
	}

	/**
	 * @return Returns the paymentType.
	 */
	public String getPaymentType() {
		return paymentType;
	}
	/**
	 * @param paymentType The paymentType to set.
	 */
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
	/**
	 * @return Returns the paidAmt.
	 */
	public Double getPaidAmt() {
		if(this.paymentList != null && this.paymentList.size() > 0) {
			double paidAmount = 0.0;
			for (Iterator paymentIter = paymentList.iterator(); paymentIter.hasNext();) {
				Payment payment = (Payment) paymentIter.next();
				paidAmount += payment.getPaymentAmt().doubleValue();
			}
			return new Double(paidAmount);
		} else {
			return new Double(0.00);
		}
	}

	public void addPayment(Payment pay) {
	    if (this.paymentList == null) {
	    	paymentList = new ArrayList();
	    }
	    pay.setInvoiceId(this.getId());
	    paymentList.add(pay);
	}	
	
	/**
	 * @return Returns the paymentList.
	 */
	public List getPaymentList() {
		return paymentList;
	}
	/**
	 * @param paymentList The paymentList to set.
	 */
	public void setPaymentList(List paymentList) {
		this.paymentList = paymentList;
	}	
}
