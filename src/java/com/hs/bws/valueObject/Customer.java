/**
 * File Name: Customer.java Date: Jul 25, 2004 Project Name: bwsBillingSystem
 * Description:
 */
package com.hs.bws.valueObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class Customer implements Cloneable, Comparable {
    public static final String FLAT_FILE_DELIM = "|";

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private Date creationDate;
    private Date lastUpdateDate;
    private Integer id;
    private double creditBalance = 0;
    private boolean useAltAddress = false;

    private String firstName = "";

    private String lastName = "";

    private String address1 = "";

    private String address2 = "";

    private String city = "";

    private String state = "";

    private String zipCode = "";

    private String altAddress1 = "";

    private String altAddress2 = "";

    private String altCity = "";

    private String altState = "";

    private String altZipCode = "";

    private String companyName = "";

    private String comments = "";

    private Date installationDate;

    private Double rentalCharge = new Double(0.00);
    
    private Date closeAccountDate;

    
    public String getNameForOrdering() {
        String name = "a";
        if (companyName != null && !companyName.equals("")) {
            name = companyName.toLowerCase();
        } else if (lastName != null && !lastName.equals("") && !firstName.equals("") && firstName != null) {
            name = lastName.toLowerCase() + firstName.toLowerCase();
        }
        return name;
    }
    /**
     * @return Returns the closeAccountDate.
     */
    public Date getCloseAccountDate() {
        return closeAccountDate;
    }
    /**
     * @param closeAccountDate The closeAccountDate to set.
     */
    public void setCloseAccountDate(Date closeAccountDate) {
        this.closeAccountDate = closeAccountDate;
    }
    /**
     * @return Returns the billStartMonth.
     */
    public String getBillStartMonth() {
        return billStartMonth;
    }
    /**
     * @param billStartMonth The billStartMonth to set.
     */
    public void setBillStartMonth(String billStartMonth) {
        this.billStartMonth = billStartMonth;
    }
    private Integer billCycleId = new Integer (0);
    private String billStartMonth = null;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id=" + id);
        sb.append(", firstName=" + firstName);
        sb.append(", lastName=" + lastName);
        sb.append(", companyName=" + companyName);
        sb.append(", address1=" + address1);
        sb.append(", address2=" + address2);
        sb.append(", city=" + city);
        sb.append(", state=" + state);
        sb.append(", zip=" + zipCode);
        sb.append(", altAddress1=" + altAddress1);
        sb.append(", altAddress2=" + altAddress2);
        sb.append(", altCity=" + altCity);
        sb.append(", altState=" + altState);
        sb.append(", altZipCode=" + altZipCode);
        String installDateString = (installationDate != null) ? sdf
                .format(installationDate) : null;
        sb.append(", installationDate=" + installDateString);
        sb.append(", creditBalance=" + creditBalance);
        sb.append(", rentalCharge=" + rentalCharge);
        sb.append(", billCycleId=" + billCycleId);
        sb.append(", comments=" + comments);
        sb.append(", billing_cycle_start_month_id " + this.getBillStartMonthId() + "\n");
        return sb.toString();
    }

    /**
     * @return Returns the billCycleId.
     */
    public Integer getBillCycleId() {
        return billCycleId;
    }

    /**
     * @param billCycleId
     *            The billCycleId to set.
     */
    public void setBillCycleId(Integer billCycleId) {
        this.billCycleId = billCycleId;
    }

    /**
     * @return Returns the installationDate.
     */
    public Date getInstallationDate() {
        return installationDate;
    }

    /**
     * @param installationDate
     *            The installationDate to set.
     */
    public void setInstallationDate(Date installationDate) {
        this.installationDate = installationDate;
    }

    /**
     * @return Returns the companyName.
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName
     *            The companyName to set.
     */
    public void setCompanyName(String companyName) {
        if (companyName != null) {
            this.companyName = companyName.trim();
        }
    }

    /**
     * Constructor
     */
    public Customer() {
    }
    
    public Customer(Integer custId) {
        id = custId;
    }

    /**
     * @return Returns the address1.
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * @param address1
     *            The address1 to set.
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * @return Returns the address2.
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * @param address2
     *            The address2 to set.
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * @return Returns the city.
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city
     *            The city to set.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return Returns the firstName.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     *            The firstName to set.
     */
    public void setFirstName(String firstName) {
        if (firstName != null) {
            this.firstName = firstName.trim();
        }
    }

    /**
     * @return Returns the id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return Returns the lastName.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     *            The lastName to set.
     */
    public void setLastName(String lastName) {
        if (lastName != null) {
            this.lastName = lastName.trim();
        }
    }

    /**
     * @return Returns the state.
     */
    public String getState() {
        return state;
    }

    /**
     * @param state
     *            The state to set.
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return Returns the zipCode.
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * @param zipCode
     *            The zipCode to set.
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * @return
     */
    public String getFlatFileString() {
        String s = id + Customer.FLAT_FILE_DELIM + lastName
                + Customer.FLAT_FILE_DELIM + firstName
                + Customer.FLAT_FILE_DELIM + address1
                + Customer.FLAT_FILE_DELIM + address2
                + Customer.FLAT_FILE_DELIM + city + Customer.FLAT_FILE_DELIM
                + state + Customer.FLAT_FILE_DELIM + zipCode
                + Customer.FLAT_FILE_DELIM;

        return s;
    }

    public static Integer getCustomerIdFromFileString(String customerString) {
        StringTokenizer strTokenizer = new StringTokenizer(customerString,
                Customer.FLAT_FILE_DELIM);
        Integer id = new Integer(strTokenizer.nextToken());
        return id;
    }

    public static Customer buildCustomerFromFileString(String customerString) {
        Customer customer = new Customer();
        StringTokenizer strTokenizer = new StringTokenizer(customerString,
                Customer.FLAT_FILE_DELIM);
        customer.setId(new Integer(strTokenizer.nextToken()));
        customer.setLastName(strTokenizer.nextToken());
        customer.setFirstName(strTokenizer.nextToken());
        customer.setAddress1(strTokenizer.nextToken());
        customer.setAddress2(strTokenizer.nextToken());
        customer.setCity(strTokenizer.nextToken());
        customer.setState(strTokenizer.nextToken());
        customer.setZipCode(strTokenizer.nextToken());
        return customer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        if (arg0 instanceof Customer) {
            Customer customer = (Customer) arg0;
            if (customer.id != null && customer.id.equals(this.id)) {
                return true;
            } else if ((customer.firstName != null && this.firstName != null && customer.firstName
                    .toLowerCase().trim().equals(
                            this.firstName.toLowerCase().trim()))
                    && (customer.lastName != null && this.lastName != null && customer.lastName
                            .toLowerCase().trim().equals(
                                    this.lastName.toLowerCase().trim()))
                    && (customer.address1 != null && this.address1 != null && customer.address1
                            .toLowerCase().trim().equals(
                                    this.address1.toLowerCase().trim()))) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * @return Returns the rentalCharge.
     */
    public Double getRentalCharge() {
        return rentalCharge;
    }

    /**
     * @param rentalCharge
     *            The rentalCharge to set.
     */
    public void setRentalCharge(Double rentalCharge) {
        this.rentalCharge = rentalCharge;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object arg0) {
        Customer c1 = (Customer) arg0;
        return this.id.compareTo(c1.id);
    }

    /**
     * @return Returns the altAddress1.
     */
    public String getAltAddress1() {
        return altAddress1;
    }

    /**
     * @param altAddress1
     *            The altAddress1 to set.
     */
    public void setAltAddress1(String altAddress1) {
        this.altAddress1 = altAddress1;
    }

    /**
     * @return Returns the altAddress2.
     */
    public String getAltAddress2() {
        return altAddress2;
    }

    /**
     * @param altAddress2
     *            The altAddress2 to set.
     */
    public void setAltAddress2(String altAddress2) {
        this.altAddress2 = altAddress2;
    }

    /**
     * @return Returns the altCity.
     */
    public String getAltCity() {
        return altCity;
    }

    /**
     * @param altCity
     *            The altCity to set.
     */
    public void setAltCity(String altCity) {
        this.altCity = altCity;
    }

    /**
     * @return Returns the altState.
     */
    public String getAltState() {
        return altState;
    }

    /**
     * @param altState
     *            The altState to set.
     */
    public void setAltState(String altState) {
        this.altState = altState;
    }

    /**
     * @return Returns the altZipCode.
     */
    public String getAltZipCode() {
        return altZipCode;
    }

    /**
     * @param altZipCode
     *            The altZipCode to set.
     */
    public void setAltZipCode(String altZipCode) {
        this.altZipCode = altZipCode;
    }

    /**
     * @return Returns the comments.
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments
     *            The comments to set.
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
    /**
     * @return Returns the creationDate.
     */
    public Date getCreationDate() {
        return creationDate;
    }
    /**
     * @param creationDate The creationDate to set.
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    /**
     * @return Returns the lastUpdateDate.
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }
    /**
     * @param lastUpdateDate The lastUpdateDate to set.
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
    /**
     * @return
     */
    public int getBillStartMonthId() {
    	if (this.billStartMonth != null){
            if (this.billStartMonth.equalsIgnoreCase("January")) return 1;
            if (this.billStartMonth.equalsIgnoreCase("February")) return 2;
            if (this.billStartMonth.equalsIgnoreCase("March")) return 3;
            if (this.billStartMonth.equalsIgnoreCase("April")) return 4;
            if (this.billStartMonth.equalsIgnoreCase("May")) return 5;
            if (this.billStartMonth.equalsIgnoreCase("June")) return 6;
            if (this.billStartMonth.equalsIgnoreCase("July")) return 7;
            if (this.billStartMonth.equalsIgnoreCase("August")) return 8;
            if (this.billStartMonth.equalsIgnoreCase("September")) return 9;
            if (this.billStartMonth.equalsIgnoreCase("October")) return 10;
            if (this.billStartMonth.equalsIgnoreCase("November")) return 11;
            if (this.billStartMonth.equalsIgnoreCase("December")) return 12;
            return 0;
    	} 
    	
    	return 0;
    }
    /**
     * @param int1
     */
    public void setBillStartMonthId(int int1) {
        if (int1 == 0) this.billStartMonth = "N/A";
        if (int1 == 1) this.billStartMonth = "January";
        if (int1 == 2) this.billStartMonth = "February";
        if (int1 == 3) this.billStartMonth = "March";
        if (int1 == 4) this.billStartMonth = "April";
        if (int1 == 5) this.billStartMonth = "May";
        if (int1 == 6) this.billStartMonth = "June";
        if (int1 == 7) this.billStartMonth = "July";
        if (int1 == 8) this.billStartMonth = "August";
        if (int1 == 9) this.billStartMonth = "September";
        if (int1 == 10) this.billStartMonth = "October";
        if (int1 == 11) this.billStartMonth = "November";
        if (int1 == 12) this.billStartMonth = "December";
    }
    /**
     * Will return the formal name for the customer
     * Formal Name is defined as Last Name, First Name for people
     * and Company Name for Companies
     * @return
     */
    public String getFormalName() {
        String name = "N/A";
        if (!(firstName.equalsIgnoreCase("") && lastName.equalsIgnoreCase(""))) {
            name = lastName + ", " + firstName;
        } else if (!companyName.equalsIgnoreCase("")) {
            name = companyName;
        }
        return name;
    }
    /**
     * @return Returns the creditBalance.
     */
    public double getCreditBalance() {
        return creditBalance;
    }
    /**
     * @param creditBalance The creditBalance to set.
     */
    public void setCreditBalance(double creditBalance) {
        this.creditBalance = creditBalance;
    }
    /**
     * @return Returns the useAltAddress.
     */
    public boolean isUseAltAddress() {
        return useAltAddress;
    }
    /**
     * @param useAltAddress The useAltAddress to set.
     */
    public void setUseAltAddress(boolean useAltAddress) {
        this.useAltAddress = useAltAddress;
    }
}