package com.hs.bws.valueObject;

import java.util.Date;

public class Payment {
	
	private Integer invoiceId;
	private Integer paymentId;
	private String paymentType;
	private Double paymentAmt;
	private Date appliedDate;
	
	public Payment() {}
	
	
	/**
	 * @return Returns the appliedDate.
	 */
	public Date getAppliedDate() {
		return appliedDate;
	}
	/**
	 * @param appliedDate The appliedDate to set.
	 */
	public void setAppliedDate(Date appliedDate) {
		this.appliedDate = appliedDate;
	}
	/**
	 * @return Returns the invoiceId.
	 */
	public Integer getInvoiceId() {
		return invoiceId;
	}
	/**
	 * @param invoiceId The invoiceId to set.
	 */
	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}
	/**
	 * @return Returns the paymentAmt.
	 */
	public Double getPaymentAmt() {
		return paymentAmt;
	}
	/**
	 * @param paymentAmt The paymentAmt to set.
	 */
	public void setPaymentAmt(Double paymentAmt) {
		this.paymentAmt = paymentAmt;
	}
	/**
	 * @return Returns the paymentId.
	 */
	public Integer getPaymentId() {
		return paymentId;
	}
	/**
	 * @param paymentId The paymentId to set.
	 */
	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
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
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("paymentId=" + paymentId);
        sb.append(", invoiceId=" + invoiceId);
        sb.append(", paymentType=" + paymentType);
        sb.append(", paymentAmt=" + paymentAmt);
        sb.append(", appliedDate=" + appliedDate);
        return sb.toString();
    }	
}
