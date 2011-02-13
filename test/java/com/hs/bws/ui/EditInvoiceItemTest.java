/*
 * Created on Nov 10, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.hs.bws.ui;


import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.hs.bws.valueObject.InvoiceLineItem;

/**
 * @author flanderb
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EditInvoiceItemTest extends TestCase {

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
	
	public void testEditInvoiceLineItem() {
		Display display = Display.getDefault();		
		Shell shell = new Shell(display);
		InvoiceLineItem returnedItem = null;

		InvoiceLineItem item = new InvoiceLineItem(new Integer(1),new Integer(3000), new String("Test Item 1"),new Double(11.11));
		InvoiceItemDialog idUI = new InvoiceItemDialog(shell, SWT.NONE, item);

		returnedItem = idUI.open();
		System.out.println(returnedItem.toString());
	}
	public void testAddInvoiceLineItem(){
		Display display = Display.getDefault();		
		Shell shell = new Shell(display);
		InvoiceLineItem returnedItem = null;
		
		InvoiceItemDialog idAddUI = new InvoiceItemDialog(shell, SWT.NONE);
		
		returnedItem = idAddUI.open();
		System.out.println(returnedItem.toString());
	}
	


}
