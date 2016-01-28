/*
 * Created on Nov 10, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.hs.bws.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hs.bws.valueObject.InvoiceLineItem;
/**
 * @author flanderb
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InvoiceItemDialog extends Composite {

	private Label lblTitle = null;
	private Label lblDescription = null;
	private Text txtDescription = null;
	private Label lblAmount = null;
	private Text txtAmount = null;
	private Button btnSave = null;
	private Button btnCancel = null;
	private Shell mainShell;
	private InvoiceLineItem localItem = null;
	private String strType = null;
	
	private Label lblIDLable = null;
	private Text txtId = null;
	
	public InvoiceItemDialog(Composite parent, int style,InvoiceLineItem item ) {
		super(parent, style);
		localItem = item;
		strType = "Edit";
		initialize();
		fillText();
		mainShell = (Shell) parent;
		mainShell.setLayout(new org.eclipse.swt.layout.FillLayout());
		mainShell.setSize(new org.eclipse.swt.graphics.Point(250,150));
	}
	
	public static void main(String[] args) {
		
		org.eclipse.swt.widgets.Display display = org.eclipse.swt.widgets.Display.getDefault();		
		org.eclipse.swt.widgets.Shell thisShell = new org.eclipse.swt.widgets.Shell(display);
		
		//Shell thisShell = new Shell();
		InvoiceItemDialog iid = new InvoiceItemDialog(thisShell,SWT.NONE);
		System.out.println(iid.open().toString());
	}
	
	/**
	 * @param shell
	 * @param none
	 */
	public InvoiceItemDialog(Composite parent, int style) {
		super(parent, style);
		
		strType = "Add";
		initialize();
		fillText();

		mainShell = (Shell) parent;
		mainShell.setLayout(new org.eclipse.swt.layout.FillLayout());
		mainShell.setSize(new org.eclipse.swt.graphics.Point(250,150));

	}

	/**
	 * 
	 */
	private void fillText() {
		if (strType.equals("Edit")){
			txtId.setText(localItem.getId().toString());
			txtDescription.setText(localItem.getItemDesc());
			txtAmount.setText(localItem.getChargeAmount().toString());
		}else if (strType.equals("Add")){
			txtId.setText("To be determined");
			txtDescription.setText(new String(""));
			txtAmount.setText(new String(""));
		}
		
	}
	private void initialize() {
		GridData gridData11 = new GridData();
		GridData gridData21 = new GridData();
		GridData gridData1 = new GridData();
		GridData gridData4 = new GridData();
		GridData gridData3 = new GridData();
		GridData gridData2 = new GridData();
		GridLayout gridLayout1 = new GridLayout();
		lblTitle = new Label(this, SWT.NONE);
		lblIDLable = new Label(this, SWT.NONE);
		txtId = new Text(this, SWT.NONE);
		lblDescription = new Label(this, SWT.NONE);
		txtDescription = new Text(this, SWT.BORDER);
		lblAmount = new Label(this, SWT.NONE);
		txtAmount = new Text(this, SWT.BORDER);
		btnSave = new Button(this, SWT.NONE);
		btnCancel = new Button(this, SWT.NONE);
		
		if (strType.equals("Add")){
			lblTitle.setText("Add a line Item");
		}else if (strType.equalsIgnoreCase("Edit")){
			lblTitle.setText("Edit the line Item");
		}
		
		lblTitle.setLayoutData(gridData2);
		this.setLayout(gridLayout1);
		gridLayout1.numColumns = 2;
		gridData2.verticalSpan = 1;
		gridData2.horizontalSpan = 2;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		lblDescription.setText("Description");
		lblAmount.setText("Amount");
		btnSave.setText("Save");
		btnSave.setLayoutData(gridData21);
		btnCancel.setText("Cancel");
		btnCancel.setLayoutData(gridData1);
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		txtDescription.setLayoutData(gridData3);
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		txtAmount.setLayoutData(gridData4);
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData21.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		lblIDLable.setText("Line Item ID");
		txtId.setEditable(false);
		txtId.setEnabled(true);
		txtId.setLayoutData(gridData11);
		gridData11.grabExcessHorizontalSpace = true;
		gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		setSize(new org.eclipse.swt.graphics.Point(250,150));
		btnCancel.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				localItem = null;
				mainShell.close();
			}
		});
		btnSave.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				if (itemIsCorrect() ){
					localItem = createLineItemFromFields();
					mainShell.close();
				} else{
					
				}
			}
		});
	}
	
	/**
	 * @return
	 */
	protected boolean itemIsCorrect() {
		boolean bolReturnVal = true;
		MessageBox messageBox = new MessageBox(mainShell,
												SWT.ICON_ERROR
												| SWT.OK);
		String messageText = new String("There are errors:\n");
		if (txtDescription.getText().length() == 0){
			bolReturnVal = false;
			messageText = messageText + "Description can't be empty\n";
			txtDescription.setFocus();
		}
		
		if (txtAmount.getText().length() == 0){
			bolReturnVal = false;
			messageText = messageText + "Amount can't be empty\n";
			txtAmount.setFocus();
		}else{
			//See if amount is a double by creating a new one
			try{
				new Double(txtAmount.getText());
			}catch(Exception ex){
				bolReturnVal = false;
				messageText = messageText + "Amount has to be a number\n";
				txtAmount.setFocus();
			}

		}
		if (bolReturnVal == false) {
			messageBox.setMessage(messageText);
			messageBox.open();
		}else{
			//Todo should I dispose of messageBox somwhow?
		}
		
		return bolReturnVal;
	}
	/**
	 * Opens the window
	 * @return InvoiceLineItem, null if cancel was pressed
	 */
	public InvoiceLineItem open() {

		mainShell.open();
		//mainShell.pack();
		Display display = mainShell.getDisplay();
		while (!mainShell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep ();
		}
		//Local item will be populated if save is selected and null if cancel
		return localItem;
	}
	/**
	 * @return
	 */
	private InvoiceLineItem createLineItemFromFields() {

		if (strType.equals("Edit")){
			localItem.setId(new Integer(txtId.getText()));
		}
		else{
			//We create a new Invoice Line item because we haven't created one yet
			localItem = new InvoiceLineItem();
		}
		localItem.setItemDesc(txtDescription.getText());
		localItem.setChargeAmount(new Double(txtAmount.getText()));
		return localItem;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
