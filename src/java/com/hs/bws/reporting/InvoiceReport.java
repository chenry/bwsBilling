package com.hs.bws.reporting;

// Imports
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.view.JasperViewer;

import org.apache.log4j.Logger;

import com.hs.bws.exception.ConnectionNotAvailableException;
import com.hs.bws.util.DatabaseUtil;
import com.hs.bws.util.Messages;
import com.hs.bws.valueObject.Customer;
import com.hs.bws.valueObject.CustomerInvoice;

/**
 * @author Adam Sova
 */
public class InvoiceReport {
	private static InvoiceReport singleton;

	// Get the logger so we can show our mistakes :o)
	static Logger logger = Logger.getLogger(InvoiceReport.class);	
	
	/**
	 * Private constructor
	 */
	private InvoiceReport() {
		super();
	}

	/**
	 * Get an instance of the class
	 * 
	 * @return InvoiceReport
	 */
	public static InvoiceReport getInstance() {
		if(singleton == null) {
			singleton = new InvoiceReport();
		}
		return singleton;
	}
	
	/**
	 * This method will print the request invoice
	 * @param invoiceId - id of the invoice to be printed
	 */
	public void printInvoice(CustomerInvoice custInv, Customer customer) {
		// Get the location of the jasper xml file (stored in the bws.properties file)
	    //@TODO have to be able to run this inside the project as well as change things when it is deployed
		String jasperXmlFile = Messages.getString("jasper.invoice.file");	
		
		try {	
			// Find out if we are using company name or an individual's name
			// Load the xml file
			JasperDesign jd = JasperManager.loadXmlDesign(jasperXmlFile);
			
			// Compiile the report so it can be used
			JasperReport jr = JasperManager.compileReport(jd);
			// Create a map to put the parameters in and set the parameters.
			Map parameters = new HashMap();
			parameters.put("INVOICE_NBR", custInv.getId());
			parameters.put("MONTHOFSERVICE", getMonthOfService(customer, custInv.getBillDate()));
			parameters.put("CUSTOMER_NBR", customer.getId());
			parameters.put("INVOICE_TOTAL", new Double(custInv.getInvoiceTotal().doubleValue() - custInv.getPaidAmt().doubleValue()));
            parameters.put("NAMEANDADDRESS", buildNameAndAddress(customer));
            parameters.put("FULL_NAME", customer.getLastName() +", " + customer.getFirstName());
			
			// See if they have made any payments against the invoice
			if(custInv.getPaidAmt().doubleValue() > 0) {
				NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
				parameters.put("PAYMENT_TOTAL", "(" + nf.format(custInv.getPaidAmt()) + ")");
				parameters.put("PAYMENT_LBL", Messages.getString("paymentRecieved.desc"));
			}
			// Populate the report using the compiled report, parameters, and database connection
			JasperPrint jp = JasperManager.fillReport(jr, parameters, DatabaseUtil.getInstance().getConnection());

			String sendToPrinter = Messages.getString("invoiceReport.sendToPrinter");
			if (sendToPrinter != null && sendToPrinter.equalsIgnoreCase("Y")) {
				// This call will print the report to the printer.
				JasperManager.printReport(jp, false);
			} else {
				// Uncomment this if you want to view the report instead of printing it
				JasperViewer.viewReport(jp);
			}
			
		} catch(JRException jrex) {
			logger.info("JRException occurred: " + jrex.getMessage());
		} catch(ConnectionNotAvailableException cnaex) {
			logger.info("ConnectionNotAvailableException: " + cnaex.getMessage());
        }
	}

    /**
     * @param customer
     * @return
     */
    public String buildNameAndAddress(Customer cust) {
        String nameAndAddress = "";
        String name = "";
        if (cust.getCompanyName() != null && !cust.getCompanyName().equals("")) {
            name = cust.getCompanyName() + "\n";
        } else {
            name = cust.getFirstName() + " " + cust.getLastName() + "\n";
        }
        nameAndAddress = name;
        if (cust.isUseAltAddress()) {
            nameAndAddress += cust.getAltAddress1() + "\n";
            if (cust.getAltAddress2() != null && !cust.getAltAddress2().equals("")) {
                nameAndAddress += cust.getAltAddress2() + "\n";
            }
            nameAndAddress += cust.getAltCity() + ", " + cust.getAltState() + " " + cust.getAltZipCode();
            
        } else {
            nameAndAddress += cust.getAddress1() + "\n";
            if (cust.getAddress2() != null && !cust.getAddress2().equals("")) {
                nameAndAddress += cust.getAddress2() + "\n";
            }
            nameAndAddress += cust.getCity() + ", " + cust.getState() + " " + cust.getZipCode();
            
        }
        return nameAndAddress;
    }

    /**
     * This method is responsible for determining the appropriate value for the 
     * Month of Service
     * @param cust
     * @param billDate
     * @return
     */
    public String getMonthOfService(Customer cust, Date billDate) {
        String monthOfServ = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        logger.info("billDate = " + billDate);
        Integer billDateMonth = new Integer(Integer.parseInt(sdf.format(billDate)));
        if (cust.getBillCycleId().intValue() == Integer.parseInt(Messages.getString("billCycleId.quarterly"))) {
            // this customer is quarterly
            // first we will retrieve the start months for this customer
            int firstQtrMonth = cust.getBillStartMonthId();
            int secondQtrMonth = (cust.getBillStartMonthId() + 3) % 12 == 0 ? 12 : (cust.getBillStartMonthId() + 3) % 12;
            int thirdQtrMonth = (cust.getBillStartMonthId() + 6) % 12 == 0 ? 12 : (cust.getBillStartMonthId() + 6) % 12;
            int fourthQtrMonth = (cust.getBillStartMonthId() + 9) % 12 == 0 ? 12 : (cust.getBillStartMonthId() + 9) % 12;

            logger.info("firstQtrMonth, secondQtrMonth, thirdQtrMonth, fourthQtrMonth = " + firstQtrMonth + ", " + secondQtrMonth + ", " + thirdQtrMonth + ", " + fourthQtrMonth );
            
            // determine where the month of the bill date falls
            logger.info("billDateMonth is " + billDateMonth);
            
            // retrieve the sets for the quarters
            Set firstQtrSet = buildQtrSet(firstQtrMonth);
            Set secondQtrSet = buildQtrSet(secondQtrMonth);
            Set thirdQtrSet = buildQtrSet(thirdQtrMonth);
            
            // find where the month falls
            int selectedQtrMonth = 0;
            if (firstQtrSet.contains(billDateMonth)) {
                // the billing is in the first quarter months.  Show this information
                logger.info("The billing " + billDateMonth + " is near the firstQtrMonth " + firstQtrMonth);
                selectedQtrMonth = firstQtrMonth;
            } else if (secondQtrSet.contains(billDateMonth)) {
                // the billing is in the second qtr months, Show this information
                logger.info("The billing " + billDateMonth + " is near the secondQtrMonth " + secondQtrMonth);
                selectedQtrMonth = secondQtrMonth;
            } else if (thirdQtrSet.contains(billDateMonth)) {
                // the billing is in the third qtr months, show this information
                logger.info("The billing " + billDateMonth + " is near the thirdQtrMonth " + thirdQtrMonth);
                selectedQtrMonth = thirdQtrMonth;
            } else {
                // the billing is in the fourth qtr months, Show this information.
                logger.info("The billing " + billDateMonth + " is near the fourthQtrMonth " + fourthQtrMonth);
                selectedQtrMonth = fourthQtrMonth;
            }
            
            monthOfServ = getMonthAbbrev(selectedQtrMonth) + ", " + getMonthAbbrev(selectedQtrMonth + 1) + ", " + getMonthAbbrev(selectedQtrMonth + 2);
            
        } else if (cust.getBillCycleId().intValue() == Integer.parseInt(Messages.getString("billCycleId.monthly"))) {
            // this customer is a monthly customer
            monthOfServ = getMonthAbbrev(billDateMonth.intValue());
            
        } else if (cust.getBillCycleId().intValue() == Integer.parseInt(Messages.getString("billCycleId.annual"))) {
            // this customer is a yearly customer.
            
            monthOfServ = getMonthAbbrev(cust.getBillStartMonthId()) + " - " + getMonthAbbrev(cust.getBillStartMonthId() - 1);
        } else if (cust.getBillCycleId().intValue() == Integer.parseInt(Messages.getString("billCycleId.semiannual"))) {
            // this is the customer who is semi annual
            int firstSemiAnnStartMonth = cust.getBillStartMonthId();
            int secondSemiAnnStartMonth = cust.getBillStartMonthId() + 6;
            
            Set firstSemiAnnSet = buildSemiAnnMonthSet(firstSemiAnnStartMonth);
            int selSemiAnnStartMonth = -1;
            if (firstSemiAnnSet.contains(billDateMonth)) {
                selSemiAnnStartMonth = firstSemiAnnStartMonth;
            } else {
                selSemiAnnStartMonth = secondSemiAnnStartMonth;
            }
            monthOfServ = getMonthAbbrev(selSemiAnnStartMonth) + " - " + getMonthAbbrev(selSemiAnnStartMonth + 5);
            
        }
        return monthOfServ;
    }
    
    private String getMonthAbbrev(int monthId) {
        String monthAbbrev = "";
        monthId = (monthId > 12) ? monthId - 12 : monthId;
        switch (monthId) {
		case 1: monthAbbrev = "Jan."; break;
		case 2: monthAbbrev = "Feb."; break;
		case 3: monthAbbrev = "Mar."; break;
		case 4: monthAbbrev = "April"; break;
		case 5: monthAbbrev = "May"; break;
		case 6: monthAbbrev = "June"; break;
		case 7: monthAbbrev = "July"; break;
		case 8: monthAbbrev = "Aug."; break;
		case 9: monthAbbrev = "Sept."; break;
		case 10: monthAbbrev = "Oct."; break;
		case 11: monthAbbrev = "Nov."; break;
		case 12: monthAbbrev = "Dec."; break;
		case 0: monthAbbrev = "Dec."; break;
        }
        return monthAbbrev;
    }

    /**
     * With the given monthId this method is responsible
     * for building a set that represents all of the months in the
     * given quarter
     * @param qtrMonthId - starting fiscal month - 1 based not 0
     * @return Set containing Integers of months in the quarter
     */
    public Set buildQtrSet(int qtrMonthId) {
        Set qtrSet = new HashSet();

        int secondMonthId = (qtrMonthId + 1 <= 12) ? qtrMonthId + 1 : (qtrMonthId + 1) % 12;
        int thirdMonthId = (qtrMonthId + 2 <= 12) ? qtrMonthId + 2 : (qtrMonthId + 2) % 12;

        qtrSet.add(new Integer(qtrMonthId));
        qtrSet.add(new Integer(secondMonthId));
        qtrSet.add(new Integer(thirdMonthId));
        
        return qtrSet;
    }

    /**
     * This method is responsible for building a set of Integers that 
     * represent the months that fall within the Semi Annual range for the
     * given start month
     * @param semiAnnMonthId - start month of the semi annual time frame.
     * @return Set containing the months within the semi annual time frame
     */
    public Set buildSemiAnnMonthSet(int semiAnnMonthId) {
        Set semiAnnMonthlySet = new HashSet();

        int currMonth = semiAnnMonthId;
        for (int i = 0; i < 6; i++) {
            semiAnnMonthlySet.add(new Integer(currMonth));
            currMonth = (currMonth + 1) % 12 == 0 ? 12 : (currMonth + 1) % 12;
        }
        return semiAnnMonthlySet;
    }
}
