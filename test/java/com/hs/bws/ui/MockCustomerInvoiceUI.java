/**
 * File Name: MockCustomerInvoiceUI.java
 * Date: Sep 24, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.hs.bws.service.MockCustomerService;
import com.hs.bws.service.MockInvoiceService;


public class MockCustomerInvoiceUI extends CustomerInvoiceUI {
    
    

    /**
     * @param parent
     * @param style
     */
    public MockCustomerInvoiceUI(Composite parent, int style) {
        super(parent, style);
    }
    
    public static void main(String[] args){
        showGUI();
    }
    
	public static void showGUI(){
		try {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			CustomerInvoiceUI inst = new MockCustomerInvoiceUI(shell, SWT.NULL);
			shell.setLayout(new org.eclipse.swt.layout.FillLayout());
			Rectangle shellBounds = shell.computeTrim(0,0,418,443);
			shell.setSize(shellBounds.width, shellBounds.height);
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
     * @see com.hs.bws.ui.CustomerInvoiceUI#initServices()
     */
    protected void initServices() {
        this.custService = MockCustomerService.getInstance();
        this.invService = new MockInvoiceService();
    }
}
