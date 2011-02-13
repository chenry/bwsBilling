/*
 * Created on Oct 17, 2004
 *
 */
package com.hs.bws.ui;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.hs.bws.exception.ValidationException;
import com.hs.bws.service.CustomerService;
import com.hs.bws.service.InvoiceService;
import com.hs.bws.util.Messages;
import com.hs.bws.valueObject.Customer;
import com.hs.bws.valueObject.CustomerInvoice;
import com.hs.bws.valueObject.InvoiceLineItem;
import com.hs.bws.valueObject.Payment;
/**
 * @author flanderb
 *
 */
public class InvoiceDetailUI extends Composite {
	Logger logger = Logger.getLogger(InvoiceDetailUI.class);
	protected InvoiceService invService;
	protected CustomerService custService;
	protected Customer customer;
	protected CustomerInvoice invoice;
	private Group grpInvoiceDetail = null;
	private Group grpCustomerInfo = null;
	private Group grpInvoiceInfo = null;
	private Label lblCustomerName = null;
	private Label lblAddress = null;
	private Label lblCityStateZip = null;
	private Label lblInvoiceIdLabel = null;
	private Label lblDueDateLable = null;
	private Label lblTotalAmountLabel = null;
	private Label lblTotalAmount = null;
	private Label lblDueDate = null;
	private Label lblInvoiceId = null;
	private Shell mainShell;
	private Table tblInvoiceItem = null;
	private Button btnEditItem = null;
	private Button btnDeleteItem = null;
	private Button btnAddItem = null;
	private GridLayout gridLayout11 = null;
	private DecimalFormat df = new DecimalFormat("#,##0.00");
	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	/**
	 * This method initializes group	
	 *
	 */    
	private void createGroup2() {
		GridData gridData41 = new GridData();
		GridData gridData5 = new GridData();
		GridData gridData4 = new GridData();
		GridData gridData3 = new GridData();
		GridData gridData21 = new GridData();
		GridLayout gridLayout1 = new GridLayout();
		grpInvoiceDetail = new Group(this, SWT.NONE);		   
		createTable();
		btnEditItem = new Button(grpInvoiceDetail, SWT.NONE);
		btnDeleteItem = new Button(grpInvoiceDetail, SWT.NONE);
		btnAddItem = new Button(grpInvoiceDetail, SWT.NONE);
		grpInvoiceDetail.setText("InvoiceDetailUI");
		grpInvoiceDetail.setLayout(gridLayout1);
		grpInvoiceDetail.setLayoutData(gridData41);
		gridLayout1.numColumns = 2;
		btnEditItem.setText("Edit Item");
		btnEditItem.setLayoutData(gridData21);
		btnDeleteItem.setText("Delete Item");
		btnDeleteItem.setLayoutData(gridData3);
		gridData21.grabExcessVerticalSpace = true;
		gridData21.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData21.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData21.grabExcessHorizontalSpace = false;
		gridData3.grabExcessVerticalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		btnAddItem.setText("Add Item");
		btnAddItem.setLayoutData(gridData5);
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.horizontalSpan = 1;
		gridData5.grabExcessVerticalSpace = true;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData41.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData41.grabExcessHorizontalSpace = true;
		gridData41.horizontalSpan = 2;
		gridData41.verticalSpan = 1;
		gridData41.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData41.grabExcessVerticalSpace = false;
		gridData41.heightHint = 100;
		btnAddItem.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				addLineItem();
			}
		});
		btnDeleteItem.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				btnDeleteItemWidgetSelected(e);
			}
		});
		btnEditItem.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				btnEditItemWidgetSelected(e);
			}
		});
	}
	/**
	 * @param e
	 */
	protected void btnEditItemWidgetSelected(SelectionEvent e) {
		if (tblInvoiceItem.getSelectionCount() == 1){
			try {
				editItem(tblInvoiceItem.getItem(tblInvoiceItem.getSelectionIndex()));
			} catch (ValidationException e1) {
				createMessage("Invalid Selection", e1.getMessage());
			}
		}
	}
	
	/**
	 * Will create the message box
	 * @param title
	 * @param message
	 */
	private void createMessage(String title, String message) {
        MessageBox messageBox = new MessageBox(this.getShell(),
                SWT.ICON_ERROR | SWT.OK);
        messageBox.setText(title);
        messageBox.setMessage(message);
        messageBox.open();
	}
	/**
	 * 
	 */
	protected void btnDeleteItemWidgetSelected(org.eclipse.swt.events.SelectionEvent e) {
		if (tblInvoiceItem.getSelectionCount() == 1){
			try {
				deleteItem(tblInvoiceItem.getItem(tblInvoiceItem.getSelectionIndex()));
			} catch (ValidationException e1) {
				createMessage("Invalid Selection", e1.getMessage());
			}
		}
	}
	/**
	 * @param item
	 */
	protected void addLineItem() {
		//Bring up the add Item Dialog
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		
		InvoiceItemDialog idAddUI = new InvoiceItemDialog(shell, SWT.NONE);
		
		
		//Save the returned object
		InvoiceLineItem lineItem = idAddUI.open();
		

		//If the user didn't hit cancel then update
		if (lineItem != null){
			//Add the invoice ID to the line item
			lineItem.setInvoiceId(invoice.getId());
			
			//Add the line item to the database
			invService.addInvoiceLineItem(lineItem);
			
			//Refresh the current invoice object
			invoice = invService.getCustomerInvoice(invoice.getId()); 
			
			//Refresh the screen
			initInvoiceFields();
			fillItemList();
		}
		
	}
	/**
	 * @param item
	 * @throws ValidationException
	 */
	protected void deleteItem(TableItem item) throws ValidationException {
		// make sure that a payment was not selected
		verifyPaymentNotSelected(item);
		//Delete the line item
		invService.deleteInvoiceLineItem(new Integer(item.getText(0)));
		
		//Refresh the current invoice object
		invoice = invService.getCustomerInvoice(invoice.getId());
		
		//Refresh the screen
		initInvoiceFields();
		fillItemList();
	}
	/**
	 * @throws ValidationException
	 * 
	 */
	private void verifyPaymentNotSelected(TableItem tblItm) throws ValidationException {
		if (tblItm.getText(0).equalsIgnoreCase(Messages.getString("paymentRecieved.code"))) {
			throw new ValidationException("You cannot edit or delete a payment");
		}
	}
	/**
	 * @param item
	 * @throws ValidationException
	 */
	protected void editItem(TableItem item) throws ValidationException {
		// validate the line item
		verifyPaymentNotSelected(item);
		//create a line item from the Table Item
		InvoiceLineItem lineItem = new InvoiceLineItem(new Integer(item.getText(0)),
														invoice.getId(), 
														item.getText(1),
														new Double(item.getText(2)));
		//Send that Invoice Item to the editor
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		InvoiceItemDialog iiDialog = new InvoiceItemDialog(shell, SWT.APPLICATION_MODAL, lineItem);
		
		//return the newly edited item back into the Invoice Item
		lineItem = iiDialog.open();
		//If the user didn't hit canel then update
		if (lineItem != null){
			//Save the line item in the database
			invService.updateInvoiceLineItem(lineItem);
			
			//Refresh the current invoice object
			invoice = invService.getCustomerInvoice(invoice.getId());
			
			//Refresh the screen
			initInvoiceFields();
			fillItemList();
		}
	}
	
	/**
	 * This method initializes group	
			gridData1.grabExcessHorizontalSpace = true;

			CustomerInfo.setLayoutData(gridData22);
			gridData22.grabExcessHorizontalSpace = true;
	 *
	 */    
	private void createGroup3() {
		GridData gridData1 = new GridData();
		RowLayout rowLayout2 = new RowLayout();
		grpCustomerInfo = new Group(this, SWT.NONE);		   
		lblCustomerName = new Label(grpCustomerInfo, SWT.NONE);
		lblAddress = new Label(grpCustomerInfo, SWT.NONE);
		lblCityStateZip = new Label(grpCustomerInfo, SWT.NONE);
		grpCustomerInfo.setText("CustomerInfo");
		grpCustomerInfo.setLayout(rowLayout2);
		grpCustomerInfo.setLayoutData(gridData1);
		lblCustomerName.setText("Customer Name");
		lblAddress.setText("Address");
		rowLayout2.type = org.eclipse.swt.SWT.VERTICAL;
		lblCityStateZip.setText("City, State ZIP");
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
	}
	/**
	 * This method initializes group	
			InvoiceInfo.setLayoutData(gridData31);
			gridData31.grabExcessHorizontalSpace = true;
	 *
			gridLayout4.numColumns = 2;
	 */    
	private void createGroup4() {
		GridData gridData22 = new GridData();
		GridLayout gridLayout4 = new GridLayout();
		grpInvoiceInfo = new Group(this, SWT.NONE);		   
		lblDueDateLable = new Label(grpInvoiceInfo, SWT.NONE);
		lblDueDate = new Label(grpInvoiceInfo, SWT.NONE);
		lblInvoiceIdLabel = new Label(grpInvoiceInfo, SWT.NONE);
		lblInvoiceId = new Label(grpInvoiceInfo, SWT.NONE);
		lblTotalAmountLabel = new Label(grpInvoiceInfo, SWT.NONE);
		lblTotalAmount = new Label(grpInvoiceInfo, SWT.NONE);
		grpInvoiceInfo.setText("InvoiceInfo");
		grpInvoiceInfo.setLayout(gridLayout4);
		grpInvoiceInfo.setLayoutData(gridData22);
		lblInvoiceIdLabel.setText("InvoiceId:");
		lblDueDateLable.setText("Due Date:");
		lblTotalAmountLabel.setText("Total Amount:");
		gridLayout4.numColumns = 2;
		lblTotalAmount.setText("$0.00");
		lblDueDate.setText("00/00/0000");
		lblInvoiceId.setText("00000");
		gridData22.grabExcessHorizontalSpace = true;
		gridData22.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
	}
	/**
	 * This method initializes table	
	 *
	 */    
	private void createTable() {
		GridData gridData2 = new GridData();
		tblInvoiceItem = new Table(grpInvoiceDetail, SWT.FULL_SELECTION);		   
		TableColumn clmLineItemID = new TableColumn(tblInvoiceItem, SWT.NONE);
		TableColumn clmLineItemDescription = new TableColumn(tblInvoiceItem, SWT.NONE);
		TableColumn clmILineItemAmount = new TableColumn(tblInvoiceItem, SWT.NONE);
		clmLineItemID.setWidth(60);
		clmLineItemID.setText("Item ID");
		clmLineItemDescription.setWidth(200);
		clmLineItemDescription.setText("Description");
		clmILineItemAmount.setWidth(50);
		clmILineItemAmount.setText("Amount");
		tblInvoiceItem.setLinesVisible(true);
		tblInvoiceItem.setHeaderVisible(true);
		tblInvoiceItem.setLayoutData(gridData2);
		gridData2.verticalSpan = 3;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
	}
		public void open() {
			//mainshell.set
			mainShell.open();
			mainShell.pack();
			initCustomerFields();
			initInvoiceFields();
			fillItemList();
			Display display = mainShell.getDisplay();
			while (!mainShell.isDisposed()) {
				if (!display.readAndDispatch()) display.sleep ();
			}
		}
		/**
		 * 
		 */
		private void initInvoiceFields() {
			if (invoice != null) {
				lblInvoiceId.setText(invoice.getId().toString());
				lblInvoiceId.pack();
				

				lblDueDate.setText(sdf.format(invoice.getDueDate()));
				lblDueDate.pack();

				lblTotalAmount.setText("$" + df.format(invoice.getInvoiceBalance()));
				lblTotalAmount.pack();
			}
			
		}
		/**
		 * 
		 */
		private void initCustomerFields() {
			if (customer != null) {
				lblCustomerName.setText(customer.getLastName() + ", " + customer.getFirstName());
				lblCustomerName.pack();
				
				lblAddress.setText(customer.getAddress1() + ", " + customer.getAddress2());
				lblAddress.pack();
				
				lblCityStateZip.setText(customer.getCity() + ", " + customer.getState() + " " + customer.getZipCode());
				lblCityStateZip.pack();
			}
		}
	public InvoiceDetailUI(Composite parent, int style, boolean isEditable) {
		super(parent, style);
		
		mainShell = (Shell) parent;
		mainShell.setLayout(new org.eclipse.swt.layout.FillLayout());
		this.getShell().setText("Invoice Detail");
		initialize();
		initServices();
		if (!isEditable) {
		    btnAddItem.setEnabled(false);
		    btnDeleteItem.setEnabled(false);
		    btnEditItem.setEnabled(false);
		}
	}
	
	
	/**
	 * 
	 */
	protected void initServices() {
		invService = new InvoiceService();
		custService = CustomerService.getInstance();
		
	}
	protected void initialize() {
		GridLayout gridLayout11 = new GridLayout();
		createGroup3();
		createGroup4();
		createGroup2();
		this.setLayout(gridLayout11);
		this.setBounds(new org.eclipse.swt.graphics.Rectangle(0,0,500,200));
		gridLayout11.numColumns = 2;
		gridLayout11.makeColumnsEqualWidth = true;
	}
	/**
	 * @return Returns the customer.
	 */
	public Customer getCustomer() {
		return customer;
	}
	/**
	 * @param customer The customer to set.
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	/**
	 * This fills the Line Item table 
	 * @return
	 */
	private void fillItemList(){
		//Remove all things from the table. Might not be needed but it is safe
		tblInvoiceItem.removeAll();
		//Grab the Line Items from the invoice
		List lineItems = invoice.getInvoiceLineItemList();

		if (lineItems != null && lineItems.size() > 0) {
			for (Iterator iter = lineItems.iterator(); iter.hasNext();) {
				TableItem tableItem = new TableItem(tblInvoiceItem, SWT.NONE);
				InvoiceLineItem ili = (InvoiceLineItem) iter.next();
				//Display those items
				tableItem.setText(new String[] {
						ili.getId().toString(),
						ili.getItemDesc(),
						df.format(ili.getChargeAmount())});
			}
		}
		
		// Grab the payment items from the invoice
		List paymentLineItems = invoice.getPaymentList();
		if (paymentLineItems != null && paymentLineItems.size() > 0) {
			for (Iterator paymentIter = paymentLineItems.iterator(); paymentIter.hasNext();) {
				TableItem tableItem = new TableItem(tblInvoiceItem, SWT.NONE);
				Payment payment = (Payment) paymentIter.next();
				// display the payment items
				tableItem.setText(new String[] {
						"X",
						Messages.getString("paymentRecieved.desc"),
						"-" + df.format(payment.getPaymentAmt())});
			}
		}
	}
	
	
	
	/**
	 * @return Returns the invoice.
	 */
	public CustomerInvoice getInvoice() {
		return invoice;
	}
	/**
	 * @param invoice The invoice to set.
	 */
	public void setInvoice(CustomerInvoice invoice) {
		this.invoice = invoice;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
