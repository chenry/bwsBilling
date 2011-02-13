package com.hs.bws.ui;

//Imports
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.hs.bws.service.InvoiceService;
import com.hs.bws.valueObject.InputDialogData;

/**
 * @author Adam Sova
 * @date 10/21/2004
 */
public class BWSMainUI {

	// Create an instance of the shell
	private org.eclipse.swt.widgets.Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	
	// Instance of the logger for this class
	private static Logger logger = Logger.getLogger(BWSMainUI.class);
	
	// Create the date format for the invoices
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	// Buttons
	private Button custMaintButton = null;
	private Button createInvoicesButton = null;
	private Button printInvoicesButton = null;
	private Button invMaintButton = null;
	private Button exitButton = null;
	
	// Labels
	private Label maintLabel = null;
	private Label actionLabel = null;
	
	private Button deleteInvoicesButton = null;
	/**
	 * This is the main method of the class.  It creates the shell
	 * and opens it.
	 *  
	 * @param args as String[]
	 */
	public static void main(String[] args) {
				try {
					// Get the display
					org.eclipse.swt.widgets.Display display = org.eclipse.swt.widgets.Display
							.getDefault();
					// Create a new instance of this class
					BWSMainUI thisClass = new BWSMainUI();
					// Create and open the shell
					thisClass.createSShell();
					thisClass.sShell.open();
					// Keep the shell open until we dispose it
					while (!thisClass.sShell.isDisposed()) {
						if (!display.readAndDispatch())
							display.sleep();
					}
					// Dispose the display
					display.dispose();
				} catch (Exception e) {
					logger.fatal("Major exception occurred", e);
				}				
	}

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		sShell = new Shell(SWT.BORDER | SWT.SHELL_TRIM);		   
		custMaintButton = new Button(sShell, SWT.NONE);
		createInvoicesButton = new Button(sShell, SWT.NONE);
		printInvoicesButton = new Button(sShell, SWT.NONE);
		invMaintButton = new Button(sShell, SWT.NONE);
		exitButton = new Button(sShell, SWT.NONE);
		maintLabel = new Label(sShell, SWT.SHADOW_OUT | SWT.CENTER);
		actionLabel = new Label(sShell, SWT.CENTER);
		deleteInvoicesButton = new Button(sShell, SWT.NONE);
		
		// Set the shell information
		sShell.setText("BWS Application");
		sShell.setFont(new org.eclipse.swt.graphics.Font(org.eclipse.swt.widgets.Display.getDefault(), "Times New Roman", 8, org.eclipse.swt.SWT.BOLD));
		sShell.setMinimized(false);
		sShell.setLocation(new org.eclipse.swt.graphics.Point(100,100));
		sShell.setMaximized(false);
		
		// Setup the customer maintenance button
		custMaintButton.setBounds(new org.eclipse.swt.graphics.Rectangle(15,55,150,30));
		custMaintButton.setText("Customer Maintenance");
		
		// Setup the create invoices button
		createInvoicesButton.setBounds(new org.eclipse.swt.graphics.Rectangle(200,55,150,30));
		createInvoicesButton.setText("Generate Invoices");
		
		// Setup the print invoices button
		printInvoicesButton.setBounds(new org.eclipse.swt.graphics.Rectangle(200,95,150,30));
		printInvoicesButton.setText("Print Invoices");
		
		// Setup the invoice maintenance button
		invMaintButton.setBounds(new org.eclipse.swt.graphics.Rectangle(15,95,150,30));
		invMaintButton.setText("Invoice Maintenance");

		// Setup the delete invoices button
		deleteInvoicesButton.setBounds(new org.eclipse.swt.graphics.Rectangle(200,135,150,30));
		deleteInvoicesButton.setEnabled(false);
		deleteInvoicesButton.setText("Delete Invoices");		
		
		// Setup the exit button
		exitButton.setBounds(new org.eclipse.swt.graphics.Rectangle(200,175,150,30));
		exitButton.setText("Exit Application");
		
		// Set the maintenance form label
		maintLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(15,5,150,40));
		maintLabel.setText("Maintenance Forms");
		maintLabel.setFont(new org.eclipse.swt.graphics.Font(org.eclipse.swt.widgets.Display.getDefault(), "Tahoma", 12, org.eclipse.swt.SWT.BOLD));
		
		// Set the actions label
		actionLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(200,5,150,30));
		actionLabel.setText("Actions");
		actionLabel.setFont(new org.eclipse.swt.graphics.Font(org.eclipse.swt.widgets.Display.getDefault(), "Tahoma", 12, org.eclipse.swt.SWT.BOLD));
		
		// Set the size of the shell
		sShell.setSize(new org.eclipse.swt.graphics.Point(372,254));
		
		// The exit button just exits the application
		exitButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				System.exit(0);
			}
		});
		
		// The invoice maintenance button just opens the CustomerInvoiceUI
		invMaintButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				CustomerInvoiceUI.main(new String[0]);
			}
		});
		
		// The create invoices button uses the same logic and the create invoices button on
		// the CustomerInvoiceUI.
		createInvoicesButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
		        // Input dialog to capture the date to create the invoices for
				InputDialog inputDialog = new InputDialog(sShell, SWT.APPLICATION_MODAL);
		        inputDialog.setText("Generate Invoices");
		        inputDialog.setQuestionText("Please enter the date, MM/DD/YYYY, to use to generate the invoices");
		        InputDialogData idd = inputDialog.open();
		        
		        // See if we should log this
		        if (logger.isDebugEnabled()) {
		            logger.debug("idd User Input " + idd.getUserInput());
		            logger.debug("idd button " + idd.isButtonResponse());
		        }
		        
		        // If they click ok, capture the input
		        if (idd.isButtonResponse()) {
		            Date generateInvoiceDate = sdf.parse(idd.getUserInput().trim(), new ParsePosition(0));
		            //@TODO using the same logic, we need to trap for null in the customer UI for close date
		            if (generateInvoiceDate == null) {
		                MessageBox messageBox = new MessageBox(sShell, SWT.ICON_ERROR | SWT.OK);
		                messageBox.setText("Generate Invoice Error!!");
		                messageBox.setMessage("Please be sure to enter the date in the following format: MM/DD/YYYYY");
		                messageBox.open();
		            }
		            // using the date entered, generate all of the invoices for all of the customers
		            // who should be invoiced		            
		            InvoiceService invService = new InvoiceService();
		            invService.generateInvoicesByRunDate(generateInvoiceDate);
		        }
			}
		});
		
		// The print invoices button prints all the invoices for the bill date they enter
		printInvoicesButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
		        // Input dialog to capture the date to create the invoices for
				InputDialog inputDialog = new InputDialog(sShell, SWT.APPLICATION_MODAL);
		        inputDialog.setText("Print Invoices");
		        inputDialog.setQuestionText("Please enter the bill date, MM/DD/YYYY, to use to print the invoices");
		        InputDialogData idd = inputDialog.open();
		        
		        // See if we should log this
		        if (logger.isDebugEnabled()) {
		            logger.debug("idd User Input " + idd.getUserInput());
		            logger.debug("idd button " + idd.isButtonResponse());
		        }
		        
		        // If they click ok, capture the input
		        if (idd.isButtonResponse()) {
		            Date billDate = sdf.parse(idd.getUserInput().trim(), new ParsePosition(0));
		            if (billDate == null) {
		                MessageBox messageBox = new MessageBox(sShell, SWT.ICON_ERROR | SWT.OK);
		                messageBox.setText("Print Invoice Error!!");
		                messageBox.setMessage("Please be sure to enter the date in the following format: MM/DD/YYYYY");
		                messageBox.open();
		            }
		            // using the date entered, print all of the invoices for all of the customers
		            // who have invoices with that bill date	            
					InvoiceService invService = new InvoiceService();
					invService.printInvoicesByBillDate(billDate);
		        }								
			}
		});

		// The print invoices button prints all the invoices for the bill date they enter
		deleteInvoicesButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
		        // Input dialog to capture the date to create the invoices for
				InputDialog inputDialog = new InputDialog(sShell, SWT.APPLICATION_MODAL);
		        inputDialog.setText("Delete Invoices");
		        inputDialog.setQuestionText("Please enter the bill date, MM/DD/YYYY, to use to delete the invoices");
		        InputDialogData idd = inputDialog.open();
		        
		        // See if we should log this
		        if (logger.isDebugEnabled()) {
		            logger.debug("idd User Input " + idd.getUserInput());
		            logger.debug("idd button " + idd.isButtonResponse());
		        }
		        
		        // If they click ok, capture the input
		        if (idd.isButtonResponse()) {
		            Date billDate = sdf.parse(idd.getUserInput().trim(), new ParsePosition(0));
		            if (billDate == null) {
		                MessageBox messageBox = new MessageBox(sShell, SWT.ICON_ERROR | SWT.OK);
		                messageBox.setText("Delete Invoice Error!!");
		                messageBox.setMessage("Please be sure to enter the date in the following format: MM/DD/YYYYY");
		                messageBox.open();
		            }
		            // using the date entered, print all of the invoices for all of the customers
		            // who have invoices with that bill date	            
					InvoiceService invService = new InvoiceService();
					invService.deleteInvoicesByBillDate(billDate);
		        }								
			}
		});		
		
		// The customer maintenance button just opens the CustomerUI
		custMaintButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				CustomerUI.main(new String[0]);
			}
		});
	}
}
