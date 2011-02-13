/**
 * File Name: PendingService.java
 * Date: Nov 23, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.valueObject;


public class PendingService {
    private int id;
    private int customerId;
    private String desc;
    private double chargeAmt;
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id = ").append(id)
          .append(" customerId = ").append(customerId)
          .append(" desc = ").append(desc)
          .append(" chargeAmt = ").append(chargeAmt);
        return sb.toString();
    }
    /**
     * @return Returns the chargeAmt.
     */
    public double getChargeAmt() {
        return chargeAmt;
    }
    /**
     * @param chargeAmt The chargeAmt to set.
     */
    public void setChargeAmt(double chargeAmt) {
        this.chargeAmt = chargeAmt;
    }
    /**
     * @return Returns the customerId.
     */
    public int getCustomerId() {
        return customerId;
    }
    /**
     * @param customerId The customerId to set.
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    /**
     * @return Returns the desc.
     */
    public String getDesc() {
        return desc;
    }
    /**
     * @param desc The desc to set.
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }
    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * 
     */
    public PendingService() {
        super();
        // @TODO Auto-generated constructor stub
    }
    /**
     * @return
     */
    public InvoiceLineItem buildInvoiceLineItem() {
        InvoiceLineItem ili = new InvoiceLineItem();
        ili.setItemDesc(desc);
        ili.setChargeAmount(new Double(chargeAmt));
        return ili;
    }

}
