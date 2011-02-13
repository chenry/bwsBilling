/**
 * File Name: AllApplicationTests.java
 * Date: Jul 25, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.hs.bws.dao.CustomerDAOTest;
import com.hs.bws.dao.DatabaseUtilTest;
import com.hs.bws.dao.InvoiceDAOTest;
import com.hs.bws.reporting.InvoiceReportTest;
import com.hs.bws.service.InvoiceServiceTest;
import com.hs.bws.util.SortCustomerByNameUtilTest;
import com.hs.bws.util.SortTableUtilComparatorTest;
import com.hs.bws.valueObject.CustomerInvoiceTest;
import com.hs.bws.valueObject.CustomerTest;
import com.hs.bws.valueObject.PendingServiceTest;


public class AllApplicationTests extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AllApplicationTests.class);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for Brown Water Softener Co.");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(CustomerTest.class));
		suite.addTest(new TestSuite(DatabaseUtilTest.class));
		suite.addTest(new TestSuite(CustomerDAOTest.class));
		suite.addTest(new TestSuite(InvoiceServiceTest.class));
		suite.addTest(new TestSuite(CustomerInvoiceTest.class));
		suite.addTest(new TestSuite(InvoiceDAOTest.class));
		suite.addTest(new TestSuite(SortCustomerByNameUtilTest.class));
		suite.addTest(new TestSuite(InvoiceReportTest.class));
		suite.addTest(new TestSuite(DatabaseUtilTest.class));
		suite.addTest(new TestSuite(SortTableUtilComparatorTest.class));
		suite.addTest(new TestSuite(PendingServiceTest.class));
		
		//$JUnit-END$
		return suite;
	}
    
}
