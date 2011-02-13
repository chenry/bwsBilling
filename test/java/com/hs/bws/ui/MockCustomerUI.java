/**
 * File Name: MockCustomerUI.java
 * Date: Sep 25, 2004
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


public class MockCustomerUI extends CustomerUI {

    /**
     * @param parent
     * @param style
     */
    public MockCustomerUI(Composite parent, int style) {
        super(parent, style);
        // @TODO Auto-generated constructor stub
    }
    
    
    
    /* (non-Javadoc)
     * @see com.hs.bws.ui.CustomerUI#initServices()
     */
    protected void initServices() {
        custService = MockCustomerService.getInstance();
    }
    public static void main(String[] args) {
        showGUI();
    }
    
    public static void showGUI(){
		try {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			CustomerUI inst = new MockCustomerUI(shell, SWT.NULL);
			shell.setLayout(new org.eclipse.swt.layout.FillLayout());
			Rectangle shellBounds = shell.computeTrim(0,0,518,648);
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
    
}
