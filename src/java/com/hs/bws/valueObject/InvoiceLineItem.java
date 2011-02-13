/*
 * Created on Aug 16, 2004
 *
 */
package com.hs.bws.valueObject;

/**
 * @author Adam Sova
 */
public class InvoiceLineItem {

	private Integer id;
	private Integer invoiceId;
	private String itemDesc;
	private Double chargeAmount;
		
	/**
	 * Constructor
	 */
	public InvoiceLineItem() {}
	
	public InvoiceLineItem(Integer id, Integer invoiceId, String itemDesc, Double chargeAmount) {
	    this.id = id;
	    this.invoiceId = invoiceId;
	    this.itemDesc = itemDesc;
	    this.chargeAmount = chargeAmount;
	}
	
	
	
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id = " + id);
        sb.append(", invoiceId = " + invoiceId);
        sb.append(", itemDesc = " + itemDesc);
        sb.append(", chargeAmt = " + chargeAmount);
        
        return sb.toString();
    }
	/**
	 * @return Returns the chargeAmount.
	 */
	public Double getChargeAmount() {
		return chargeAmount;
	}
	/**
	 * @param chargeAmount The chargeAmount to set.
	 */
	public void setChargeAmount(Double chargeAmount) {
		this.chargeAmount = chargeAmount;
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
	 * @return Returns the itemDesc.
	 */
	public String getItemDesc() {
		return itemDesc;
	}
	/**
	 * @param itemDesc The itemDesc to set.
	 */
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
}
