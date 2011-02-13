/*
 * Created on Nov 3, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.hs.bws.ui;

import java.util.Date;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.hs.bws.valueObject.Customer;
import com.hs.bws.valueObject.CustomerInvoice;
import com.hs.bws.valueObject.InvoiceLineItem;

/**
 * @author flanderb
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InvoiceDetailUITest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * 
	 */
	public void testOpenWithCustomerAndInvoice() {
		Display display = Display.getDefault();		
		Shell shell = new Shell(display);

		InvoiceDetailUI idUI = new InvoiceDetailUI(shell, SWT.NONE, true);
		Customer customer = new Customer();
		customer.setLastName("Flanders");
		customer.setFirstName("Ben");
		customer.setAddress1("1212 Something");
		customer.setAddress2("Apt 12");
		customer.setCity("Wyoming");
		customer.setState("MI");
		customer.setZipCode("49509");
		idUI.setCustomer(customer);
		
		CustomerInvoice custInvoice = new CustomerInvoice();
		custInvoice.setId(new Integer(3000));
		custInvoice.setDueDate(new Date());
		
		InvoiceLineItem ili1 = new InvoiceLineItem(new Integer(1),new Integer(3000), new String("Test Item 1"),new Double(11.11));
		InvoiceLineItem ili2 = new InvoiceLineItem(new Integer(2),new Integer(3000), new String("Test Item 2"),new Double(22.22));
		InvoiceLineItem ili3 = new InvoiceLineItem(new Integer(3),new Integer(3000), new String("Test Item 3"),new Double(33.33));
		InvoiceLineItem ili4 = new InvoiceLineItem(new Integer(4),new Integer(3000), new String("Test Item 4"),new Double(44.44));
		InvoiceLineItem ili5 = new InvoiceLineItem(new Integer(5),new Integer(3000), new String("Test Item 5"),new Double(55));
		
		custInvoice.addInvoiceLineItem(ili1);
		custInvoice.addInvoiceLineItem(ili2);
		custInvoice.addInvoiceLineItem(ili3);
		custInvoice.addInvoiceLineItem(ili4);
		custInvoice.addInvoiceLineItem(ili5);
		
		
		idUI.setInvoice(custInvoice);
		idUI.open();
	}

}
