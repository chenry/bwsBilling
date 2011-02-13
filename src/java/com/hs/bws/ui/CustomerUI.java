package com.hs.bws.ui;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.hs.bws.exception.BWSGenericException;
import com.hs.bws.exception.ValidFieldsUnavailableException;
import com.hs.bws.service.CustomerService;
import com.hs.bws.util.Messages;
import com.hs.bws.util.SortTableUtilComparator;
import com.hs.bws.valueObject.Customer;
import com.hs.bws.valueObject.InputDialogData;

/**
 * This code was generated using CloudGarden's Jigloo SWT/Swing GUI Builder,
 * which is free for non-commercial use. If Jigloo is being used commercially
 * (ie, by a for-profit company or business) then you should purchase a license -
 * please visit www.cloudgarden.com for details.
 */

public class CustomerUI extends org.eclipse.swt.widgets.Composite {
    
    protected CustomerService custService = null;

	/*
	 * First we create all the widgets that we need
	 */
	private Label lblAccountClosedValue;
	
    private Label lblAccountClosedStatus;

    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    private DecimalFormat df = new DecimalFormat("#,##0.00");

    private Button btnFindAll;

    Logger logger = Logger.getLogger(CustomerUI.class);

    List billCycleGroupList = null;

    private Button btnCloseAcct;

    private Button btnUpdate;

	private Button btnReopenAccount;

    private TableColumn tblClmRentalCharge;

    private TableColumn tblClmInstallationDate;

    private TableColumn tblClmBillCycleType;

    private TableColumn tblClmCustName;

    private TableColumn tblClmCustomerId;

    private Table tblCustomerList;

    private Button btnCreate;

    private Button btnFind;

    private Button btnClear;

    private Composite cmpButton;

    private Composite cmpCommentAndButtons;

    private Text txtComment;

    private Group grpComment;

    private Text txtRentalCharge;

    private Label lblRentalCharge;

    private Label lblInstallationDateFormat;

    private Text txtInstallationDate;

    private Label lblInstallationDate;

    private Combo cmbStartMonth;

    private Label lblStartMonth;

    private Combo cmbBillingCycleType;

    private Label lblBillingCycleType;

    private Group grpBillingInfo;

    private Text txtAltZipCode;

    private Label lblAltZipCode;

    private Text txtAltState;

    private Label lblAltState;

    private Text txtAltCity;

    private Label lblAltCity;

    private Text txtAltAddress2;

    private Label lblAltAddress2;

    private Text txtAltAddress1;

    private Label lblAltAddress1;

    private Group grpAlternateAddress;

    private Text txtZipCode;

    private Label lblZipCode;

    private Text txtState;

    private Label lblState;

    private Text txtCity;

    private Label lblCity;

    private Text txtAddress2;

    private Label lblAddress2;

    private Text txtAddress1;

    private Label lblAdd1;

    private Group grpMainAddress;

    private Text txtCompanyName;

    private Label lblCompanyName;

    private Text txtFirstName;

    private Label lblFirstName;

    private Text txtLastName;

    private Label lblLastName;

    private Text txtCustomerId;

    private Label lblId;
    private Text txtCreditAmt;
    private Label lblCredit;
    private Button btnUseAltAddress;
    private Button chkShowClosedAcct;

    private Group grpCustomerName;

	private int lastSortColumn = -1;
	
    /**
	 * Constructor for the class
	 * 
	 * @param parent
	 *            Don't quite understand what this is for
	 * @param style
	 *            another confusing parameter
	 *  
	 */
    public CustomerUI(Composite parent, int style) {
        super(parent, style);
        initServices();
        initGUI();
        initFields();
		createTableListners();
		createBtnReopenAccount();
        this.getShell().setText("Customer Maintenace");
    }

    /**
	 *  
	 */
	private void createBtnReopenAccount() {
		btnReopenAccount = new Button(cmpButton, SWT.PUSH | SWT.CENTER);
		//TODO implement if the the account is closed, else inactive
		btnReopenAccount.setText("Reopen Account");
		cmpButton.layout();
		btnReopenAccount.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				btnReopenAccountWidgetSelected(evt);
			}
		});
	}

	/**
	 *  
	 */
	private void createTableListners() {

		tblClmCustomerId.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				sort(0, SortTableUtilComparator.COMPARE_TYPE_DOUBLE);
			}
		});

		tblClmCustName.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				sort(1, SortTableUtilComparator.COMPARE_TYPE_STRING);
			}
		});

		tblClmBillCycleType.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				sort(2, SortTableUtilComparator.COMPARE_TYPE_STRING);
			}
		});

		tblClmInstallationDate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				sort(3, SortTableUtilComparator.COMPARE_TYPE_DATE);
			}
		});

		tblClmRentalCharge.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				sort(4, SortTableUtilComparator.COMPARE_TYPE_DOUBLE);
			}
		});

	}

	/**
	 *  This method is responsible for sorting the objects that are in
	 * the customer table
	 */
	private void sort(int column, int type) {

		if (tblCustomerList.getItemCount() <= 1)
			return;

		TableItem[] items = tblCustomerList.getItems();
		String[][] data = new String[items.length][tblCustomerList
				.getColumnCount()];
		for (int i = 0; i < items.length; i++) {
			for (int j = 0; j < tblCustomerList.getColumnCount(); j++) {
				data[i][j] = items[i].getText(j);
			}
		}

		Arrays.sort(data, new SortTableUtilComparator(column, type));

		if (lastSortColumn != column) {
			for (int i = 0; i < data.length; i++) {
				items[i].setText(data[i]);
			}
			lastSortColumn = column;
		} else {
			// reverse order if the current column is selected again
			int j = data.length - 1;
			for (int i = 0; i < data.length; i++) {
				items[i].setText(data[j--]);
			}
			lastSortColumn = -1;
		}
		
		// we need to run through the table list and make sure that the customers
		// who have closed accounts are highlighted in red.
		highlightCustomerTableClosedAccounts();

	}

	/**
     * This method will run through the customer list and make sure that the
     * closed accounts are highlighted properly
     */
    private void highlightCustomerTableClosedAccounts() {
        TableItem[] custTblItems = tblCustomerList.getItems();
        // make sure that there are records that should be processed
        if (custTblItems.length == 0) {
            // since there were not any records to process, we will just simply return.
            return;
        }
        // there are records to process.
        for (int i = 0; i < custTblItems.length; i++) {
            custTblItems[i].setBackground(null);
            Customer cust = getCustomerByTableItem(custTblItems[i]);
            if (cust.getCloseAccountDate() != null) {
                custTblItems[i].setBackground(new Color(this.getDisplay(), 255, 0, 0));
                custTblItems[i].setForeground(new Color(this.getDisplay(), 0,0,0));
            }
        }
        
    }

    /**
     * Will initialize the fields that should have data in them already
     */
    private void initFields() {
        // we will populate the combo boxes
        try {
            billCycleGroupList = custService.getBillingCycleTypesList();
        } catch (BWSGenericException e) {
            MessageBox messageBox = new MessageBox(this.getShell(),
                    SWT.ICON_ERROR | SWT.OK);
            messageBox.setText("Initialization Error!!!");
            messageBox.setMessage("System not initialized properly. "
                    + e.getMessage());
            messageBox.open();
            System.exit(21);
        }
        // adds the entries for the bill cylce type to the combo box
        for (Iterator iter = billCycleGroupList.iterator(); iter.hasNext();) {
            Map billCycleTypeMap = (Map) iter.next();
            cmbBillingCycleType.add((String) billCycleTypeMap.get(Messages
                    .getString("bcg.desc")));
        }

        // add the entries for the months in the month dropdown
        cmbStartMonth.setItems(new String[] { "January", "February", "March",
                "April", "May", "June", "July", "August", "September",
                "October", "November", "December" });
    }

    protected void initServices() {
        custService = CustomerService.getInstance();
    }

	/**
	* Initializes the GUI.
	* Auto-generated code - any changes you make will disappear.
	*/
    public void initGUI(){
		try {
			preInitGUI();
	
			grpCustomerName = new Group(this,SWT.NULL);
			lblId = new Label(grpCustomerName,SWT.NULL);
			txtCustomerId = new Text(grpCustomerName,SWT.BORDER);
			lblLastName = new Label(grpCustomerName,SWT.NULL);
			txtLastName = new Text(grpCustomerName,SWT.BORDER);
			lblFirstName = new Label(grpCustomerName,SWT.NULL);
			txtFirstName = new Text(grpCustomerName,SWT.BORDER);
			lblCompanyName = new Label(grpCustomerName,SWT.NULL);
			txtCompanyName = new Text(grpCustomerName,SWT.BORDER);
			grpMainAddress = new Group(this,SWT.NULL);
			lblAdd1 = new Label(grpMainAddress,SWT.NULL);
			txtAddress1 = new Text(grpMainAddress,SWT.BORDER);
			lblAddress2 = new Label(grpMainAddress,SWT.NULL);
			txtAddress2 = new Text(grpMainAddress,SWT.BORDER);
			lblCity = new Label(grpMainAddress,SWT.NULL);
			txtCity = new Text(grpMainAddress,SWT.BORDER);
			lblState = new Label(grpMainAddress,SWT.NULL);
			txtState = new Text(grpMainAddress,SWT.BORDER);
			lblZipCode = new Label(grpMainAddress,SWT.NULL);
			txtZipCode = new Text(grpMainAddress,SWT.BORDER);
			grpAlternateAddress = new Group(this,SWT.NULL);
			lblAltAddress1 = new Label(grpAlternateAddress,SWT.NULL);
			txtAltAddress1 = new Text(grpAlternateAddress,SWT.BORDER);
			lblAltAddress2 = new Label(grpAlternateAddress,SWT.NULL);
			txtAltAddress2 = new Text(grpAlternateAddress,SWT.BORDER);
			lblAltCity = new Label(grpAlternateAddress,SWT.NULL);
			txtAltCity = new Text(grpAlternateAddress,SWT.BORDER);
			lblAltState = new Label(grpAlternateAddress,SWT.NULL);
			txtAltState = new Text(grpAlternateAddress,SWT.BORDER);
			lblAltZipCode = new Label(grpAlternateAddress,SWT.NULL);
			txtAltZipCode = new Text(grpAlternateAddress,SWT.BORDER);
			grpBillingInfo = new Group(this,SWT.NULL);
			lblBillingCycleType = new Label(grpBillingInfo,SWT.NULL);
			cmbBillingCycleType = new Combo(grpBillingInfo,SWT.READ_ONLY);
			lblStartMonth = new Label(grpBillingInfo,SWT.NULL);
			cmbStartMonth = new Combo(grpBillingInfo,SWT.READ_ONLY);
			lblInstallationDate = new Label(grpBillingInfo,SWT.NULL);
			txtInstallationDate = new Text(grpBillingInfo,SWT.BORDER);
			lblInstallationDateFormat = new Label(grpBillingInfo,SWT.NULL);
			lblRentalCharge = new Label(grpBillingInfo,SWT.NULL);
			txtRentalCharge = new Text(grpBillingInfo,SWT.BORDER);
			lblAccountClosedStatus = new Label(grpBillingInfo,SWT.NULL);
			lblAccountClosedValue = new Label(grpBillingInfo,SWT.NULL);
			grpComment = new Group(this,SWT.NULL);
			txtComment = new Text(grpComment,SWT.MULTI| SWT.BORDER);
			cmpCommentAndButtons = new Composite(this,SWT.NULL);
			tblCustomerList = new Table(cmpCommentAndButtons,SWT.FULL_SELECTION);
			tblClmCustomerId = new TableColumn(tblCustomerList,SWT.NULL);
			tblClmCustName = new TableColumn(tblCustomerList,SWT.NULL);
			tblClmBillCycleType = new TableColumn(tblCustomerList,SWT.NULL);
			tblClmInstallationDate = new TableColumn(tblCustomerList,SWT.NULL);
			tblClmRentalCharge = new TableColumn(tblCustomerList,SWT.NULL);
			cmpButton = new Composite(cmpCommentAndButtons,SWT.NULL);
			btnClear = new Button(cmpButton,SWT.PUSH| SWT.CENTER);
			btnFind = new Button(cmpButton,SWT.PUSH| SWT.CENTER);
			btnCreate = new Button(cmpButton,SWT.PUSH| SWT.CENTER);
			btnUpdate = new Button(cmpButton,SWT.PUSH| SWT.CENTER);
			btnCloseAcct = new Button(cmpButton,SWT.PUSH| SWT.CENTER);
			btnFindAll = new Button(cmpButton,SWT.PUSH| SWT.CENTER);
	
			this.setSize(518, 678);
	
			GridData grpCustomerNameLData = new GridData();
			grpCustomerNameLData.verticalAlignment = GridData.CENTER;
			grpCustomerNameLData.horizontalAlignment = GridData.FILL;
			grpCustomerNameLData.widthHint = -1;
			grpCustomerNameLData.heightHint = -1;
			grpCustomerNameLData.horizontalIndent = 0;
			grpCustomerNameLData.horizontalSpan = 2;
			grpCustomerNameLData.verticalSpan = 1;
			grpCustomerNameLData.grabExcessHorizontalSpace = false;
			grpCustomerNameLData.grabExcessVerticalSpace = false;
			grpCustomerName.setLayoutData(grpCustomerNameLData);
			grpCustomerName.setText("Customer Name");
	
			GridData lblIdLData = new GridData();
			lblIdLData.verticalAlignment = GridData.CENTER;
			lblIdLData.horizontalAlignment = GridData.BEGINNING;
			lblIdLData.widthHint = 12;
			lblIdLData.heightHint = 13;
			lblIdLData.horizontalIndent = 0;
			lblIdLData.horizontalSpan = 1;
			lblIdLData.verticalSpan = 1;
			lblIdLData.grabExcessHorizontalSpace = false;
			lblIdLData.grabExcessVerticalSpace = false;
			lblId.setLayoutData(lblIdLData);
			lblId.setText("Id:");
			lblId.setSize(new org.eclipse.swt.graphics.Point(12,13));
	
			GridData txtCustomerIdLData = new GridData();
			txtCustomerIdLData.horizontalSpan = 3;
			txtCustomerId.setLayoutData(txtCustomerIdLData);
	
			GridData lblLastNameLData = new GridData();
			lblLastNameLData.verticalAlignment = GridData.CENTER;
			lblLastNameLData.horizontalAlignment = GridData.BEGINNING;
			lblLastNameLData.widthHint = -1;
			lblLastNameLData.heightHint = -1;
			lblLastNameLData.horizontalIndent = 0;
			lblLastNameLData.horizontalSpan = 1;
			lblLastNameLData.verticalSpan = 1;
			lblLastNameLData.grabExcessHorizontalSpace = false;
			lblLastNameLData.grabExcessVerticalSpace = false;
			lblLastName.setLayoutData(lblLastNameLData);
			lblLastName.setText("Last Name:");
	
			GridData txtLastNameLData = new GridData();
			txtLastNameLData.heightHint = 13;
			txtLastNameLData.horizontalSpan = 3;
			txtLastNameLData.horizontalAlignment = GridData.FILL;
			txtLastName.setLayoutData(txtLastNameLData);
	
			GridData lblFirstNameLData = new GridData();
			lblFirstNameLData.verticalAlignment = GridData.CENTER;
			lblFirstNameLData.horizontalAlignment = GridData.BEGINNING;
			lblFirstNameLData.widthHint = -1;
			lblFirstNameLData.heightHint = -1;
			lblFirstNameLData.horizontalIndent = 0;
			lblFirstNameLData.horizontalSpan = 1;
			lblFirstNameLData.verticalSpan = 1;
			lblFirstNameLData.grabExcessHorizontalSpace = false;
			lblFirstNameLData.grabExcessVerticalSpace = false;
			lblFirstName.setLayoutData(lblFirstNameLData);
			lblFirstName.setText("First Name:");
	
			GridData txtFirstNameLData = new GridData();
			txtFirstNameLData.heightHint = 13;
			txtFirstNameLData.horizontalSpan = 3;
			txtFirstNameLData.horizontalAlignment = GridData.FILL;
			txtFirstName.setLayoutData(txtFirstNameLData);
	
			GridData lblCompanyNameLData = new GridData();
			lblCompanyNameLData.verticalAlignment = GridData.CENTER;
			lblCompanyNameLData.horizontalAlignment = GridData.BEGINNING;
			lblCompanyNameLData.widthHint = -1;
			lblCompanyNameLData.heightHint = -1;
			lblCompanyNameLData.horizontalIndent = 0;
			lblCompanyNameLData.horizontalSpan = 1;
			lblCompanyNameLData.verticalSpan = 1;
			lblCompanyNameLData.grabExcessHorizontalSpace = false;
			lblCompanyNameLData.grabExcessVerticalSpace = false;
			lblCompanyName.setLayoutData(lblCompanyNameLData);
			lblCompanyName.setText("Company Name:");
	
			GridData txtCompanyNameLData = new GridData();
			txtCompanyNameLData.horizontalAlignment = GridData.FILL;
			txtCompanyNameLData.heightHint = 13;
			txtCompanyNameLData.horizontalSpan = 3;
			txtCompanyName.setLayoutData(txtCompanyNameLData);
			GridLayout grpCustomerNameLayout = new GridLayout();
			grpCustomerName.setLayout(grpCustomerNameLayout);
			grpCustomerNameLayout.numColumns = 4;
			grpCustomerNameLayout.makeColumnsEqualWidth = true;
			grpCustomerName.layout();
	
			GridData grpMainAddressLData = new GridData();
			grpMainAddressLData.verticalAlignment = GridData.CENTER;
			grpMainAddressLData.horizontalAlignment = GridData.FILL;
			grpMainAddressLData.widthHint = -1;
			grpMainAddressLData.heightHint = -1;
			grpMainAddressLData.horizontalIndent = 0;
			grpMainAddressLData.horizontalSpan = 2;
			grpMainAddressLData.verticalSpan = 1;
			grpMainAddressLData.grabExcessHorizontalSpace = false;
			grpMainAddressLData.grabExcessVerticalSpace = false;
			grpMainAddress.setLayoutData(grpMainAddressLData);
			grpMainAddress.setText("Main Address");
	
			GridData lblAdd1LData = new GridData();
			lblAdd1LData.verticalAlignment = GridData.CENTER;
			lblAdd1LData.horizontalAlignment = GridData.BEGINNING;
			lblAdd1LData.widthHint = -1;
			lblAdd1LData.heightHint = -1;
			lblAdd1LData.horizontalIndent = 0;
			lblAdd1LData.horizontalSpan = 1;
			lblAdd1LData.verticalSpan = 1;
			lblAdd1LData.grabExcessHorizontalSpace = false;
			lblAdd1LData.grabExcessVerticalSpace = false;
			lblAdd1.setLayoutData(lblAdd1LData);
			lblAdd1.setText("Address 1:");
	
			GridData txtAddress1LData = new GridData();
			txtAddress1LData.verticalAlignment = GridData.CENTER;
			txtAddress1LData.horizontalAlignment = GridData.BEGINNING;
			txtAddress1LData.widthHint = 107;
			txtAddress1LData.heightHint = 13;
			txtAddress1LData.horizontalIndent = 0;
			txtAddress1LData.horizontalSpan = 1;
			txtAddress1LData.verticalSpan = 1;
			txtAddress1LData.grabExcessHorizontalSpace = false;
			txtAddress1LData.grabExcessVerticalSpace = false;
			txtAddress1.setLayoutData(txtAddress1LData);
			txtAddress1.setSize(new org.eclipse.swt.graphics.Point(107,13));
	
			GridData lblAddress2LData = new GridData();
			lblAddress2LData.verticalAlignment = GridData.CENTER;
			lblAddress2LData.horizontalAlignment = GridData.BEGINNING;
			lblAddress2LData.widthHint = -1;
			lblAddress2LData.heightHint = -1;
			lblAddress2LData.horizontalIndent = 0;
			lblAddress2LData.horizontalSpan = 1;
			lblAddress2LData.verticalSpan = 1;
			lblAddress2LData.grabExcessHorizontalSpace = false;
			lblAddress2LData.grabExcessVerticalSpace = false;
			lblAddress2.setLayoutData(lblAddress2LData);
			lblAddress2.setText("Address 2:");
	
			GridData txtAddress2LData = new GridData();
			txtAddress2LData.verticalAlignment = GridData.CENTER;
			txtAddress2LData.horizontalAlignment = GridData.BEGINNING;
			txtAddress2LData.widthHint = 107;
			txtAddress2LData.heightHint = 13;
			txtAddress2LData.horizontalIndent = 0;
			txtAddress2LData.horizontalSpan = 3;
			txtAddress2LData.verticalSpan = 1;
			txtAddress2LData.grabExcessHorizontalSpace = false;
			txtAddress2LData.grabExcessVerticalSpace = false;
			txtAddress2.setLayoutData(txtAddress2LData);
			txtAddress2.setSize(new org.eclipse.swt.graphics.Point(107,13));
	
			GridData lblCityLData = new GridData();
			lblCityLData.verticalAlignment = GridData.CENTER;
			lblCityLData.horizontalAlignment = GridData.BEGINNING;
			lblCityLData.widthHint = -1;
			lblCityLData.heightHint = -1;
			lblCityLData.horizontalIndent = 0;
			lblCityLData.horizontalSpan = 1;
			lblCityLData.verticalSpan = 1;
			lblCityLData.grabExcessHorizontalSpace = false;
			lblCityLData.grabExcessVerticalSpace = false;
			lblCity.setLayoutData(lblCityLData);
			lblCity.setText("City:");
	
			GridData txtCityLData = new GridData();
			txtCityLData.verticalAlignment = GridData.CENTER;
			txtCityLData.horizontalAlignment = GridData.BEGINNING;
			txtCityLData.widthHint = -1;
			txtCityLData.heightHint = -1;
			txtCityLData.horizontalIndent = 0;
			txtCityLData.horizontalSpan = 1;
			txtCityLData.verticalSpan = 1;
			txtCityLData.grabExcessHorizontalSpace = false;
			txtCityLData.grabExcessVerticalSpace = false;
			txtCity.setLayoutData(txtCityLData);
	
			GridData lblStateLData = new GridData();
			lblStateLData.verticalAlignment = GridData.CENTER;
			lblStateLData.horizontalAlignment = GridData.BEGINNING;
			lblStateLData.widthHint = -1;
			lblStateLData.heightHint = -1;
			lblStateLData.horizontalIndent = 0;
			lblStateLData.horizontalSpan = 1;
			lblStateLData.verticalSpan = 1;
			lblStateLData.grabExcessHorizontalSpace = false;
			lblStateLData.grabExcessVerticalSpace = false;
			lblState.setLayoutData(lblStateLData);
			lblState.setText("State:");
	
			GridData txtStateLData = new GridData();
			txtStateLData.verticalAlignment = GridData.CENTER;
			txtStateLData.horizontalAlignment = GridData.BEGINNING;
			txtStateLData.widthHint = -1;
			txtStateLData.heightHint = -1;
			txtStateLData.horizontalIndent = 0;
			txtStateLData.horizontalSpan = 1;
			txtStateLData.verticalSpan = 1;
			txtStateLData.grabExcessHorizontalSpace = false;
			txtStateLData.grabExcessVerticalSpace = false;
			txtState.setLayoutData(txtStateLData);
	
			GridData lblZipCodeLData = new GridData();
			lblZipCodeLData.verticalAlignment = GridData.CENTER;
			lblZipCodeLData.horizontalAlignment = GridData.BEGINNING;
			lblZipCodeLData.widthHint = -1;
			lblZipCodeLData.heightHint = -1;
			lblZipCodeLData.horizontalIndent = 0;
			lblZipCodeLData.horizontalSpan = 1;
			lblZipCodeLData.verticalSpan = 1;
			lblZipCodeLData.grabExcessHorizontalSpace = false;
			lblZipCodeLData.grabExcessVerticalSpace = false;
			lblZipCode.setLayoutData(lblZipCodeLData);
			lblZipCode.setText("Zip Code:");
	
			GridData txtZipCodeLData = new GridData();
			txtZipCodeLData.verticalAlignment = GridData.CENTER;
			txtZipCodeLData.horizontalAlignment = GridData.BEGINNING;
			txtZipCodeLData.widthHint = -1;
			txtZipCodeLData.heightHint = -1;
			txtZipCodeLData.horizontalIndent = 0;
			txtZipCodeLData.horizontalSpan = 1;
			txtZipCodeLData.verticalSpan = 1;
			txtZipCodeLData.grabExcessHorizontalSpace = false;
			txtZipCodeLData.grabExcessVerticalSpace = false;
			txtZipCode.setLayoutData(txtZipCodeLData);
			GridLayout grpMainAddressLayout = new GridLayout(6, true);
			grpMainAddress.setLayout(grpMainAddressLayout);
			grpMainAddressLayout.marginWidth = 5;
			grpMainAddressLayout.marginHeight = 5;
			grpMainAddressLayout.numColumns = 6;
			grpMainAddressLayout.makeColumnsEqualWidth = true;
			grpMainAddressLayout.horizontalSpacing = 5;
			grpMainAddressLayout.verticalSpacing = 5;
			grpMainAddress.layout();
	
			GridData grpAlternateAddressLData = new GridData();
			grpAlternateAddressLData.horizontalAlignment = GridData.FILL;
			grpAlternateAddressLData.heightHint = 83;
			grpAlternateAddressLData.horizontalSpan = 2;
			grpAlternateAddress.setLayoutData(grpAlternateAddressLData);
			grpAlternateAddress.setText("Alternate Address");

			GridData lblAltAddress1LData = new GridData();
			lblAltAddress1LData.verticalAlignment = GridData.CENTER;
			lblAltAddress1LData.horizontalAlignment = GridData.BEGINNING;
			lblAltAddress1LData.widthHint = -1;
			lblAltAddress1LData.heightHint = -1;
			lblAltAddress1LData.horizontalIndent = 0;
			lblAltAddress1LData.horizontalSpan = 1;
			lblAltAddress1LData.verticalSpan = 1;
			lblAltAddress1LData.grabExcessHorizontalSpace = false;
			lblAltAddress1LData.grabExcessVerticalSpace = false;
			lblAltAddress1.setLayoutData(lblAltAddress1LData);
			lblAltAddress1.setText("Alt. Address 1:");
	
			GridData txtAltAddress1LData = new GridData();
			txtAltAddress1LData.verticalAlignment = GridData.CENTER;
			txtAltAddress1LData.horizontalAlignment = GridData.BEGINNING;
			txtAltAddress1LData.widthHint = 107;
			txtAltAddress1LData.heightHint = 13;
			txtAltAddress1LData.horizontalIndent = 0;
			txtAltAddress1LData.horizontalSpan = 1;
			txtAltAddress1LData.verticalSpan = 1;
			txtAltAddress1LData.grabExcessHorizontalSpace = false;
			txtAltAddress1LData.grabExcessVerticalSpace = false;
			txtAltAddress1.setLayoutData(txtAltAddress1LData);
			txtAltAddress1.setSize(new org.eclipse.swt.graphics.Point(107,13));
	
			GridData lblAltAddress2LData = new GridData();
			lblAltAddress2LData.verticalAlignment = GridData.CENTER;
			lblAltAddress2LData.horizontalAlignment = GridData.BEGINNING;
			lblAltAddress2LData.widthHint = -1;
			lblAltAddress2LData.heightHint = -1;
			lblAltAddress2LData.horizontalIndent = 0;
			lblAltAddress2LData.horizontalSpan = 1;
			lblAltAddress2LData.verticalSpan = 1;
			lblAltAddress2LData.grabExcessHorizontalSpace = false;
			lblAltAddress2LData.grabExcessVerticalSpace = false;
			lblAltAddress2.setLayoutData(lblAltAddress2LData);
			lblAltAddress2.setText("Alt. Address 2:");
	
			GridData txtAltAddress2LData = new GridData();
			txtAltAddress2LData.verticalAlignment = GridData.CENTER;
			txtAltAddress2LData.horizontalAlignment = GridData.BEGINNING;
			txtAltAddress2LData.widthHint = 107;
			txtAltAddress2LData.heightHint = 13;
			txtAltAddress2LData.horizontalIndent = 0;
			txtAltAddress2LData.horizontalSpan = 3;
			txtAltAddress2LData.verticalSpan = 1;
			txtAltAddress2LData.grabExcessHorizontalSpace = false;
			txtAltAddress2LData.grabExcessVerticalSpace = false;
			txtAltAddress2.setLayoutData(txtAltAddress2LData);
			txtAltAddress2.setSize(new org.eclipse.swt.graphics.Point(107,13));
	
			GridData lblAltCityLData = new GridData();
			lblAltCityLData.verticalAlignment = GridData.CENTER;
			lblAltCityLData.horizontalAlignment = GridData.BEGINNING;
			lblAltCityLData.widthHint = -1;
			lblAltCityLData.heightHint = -1;
			lblAltCityLData.horizontalIndent = 0;
			lblAltCityLData.horizontalSpan = 1;
			lblAltCityLData.verticalSpan = 1;
			lblAltCityLData.grabExcessHorizontalSpace = false;
			lblAltCityLData.grabExcessVerticalSpace = false;
			lblAltCity.setLayoutData(lblAltCityLData);
			lblAltCity.setText("Alt City:");
	
			GridData txtAltCityLData = new GridData();
			txtAltCityLData.verticalAlignment = GridData.CENTER;
			txtAltCityLData.horizontalAlignment = GridData.BEGINNING;
			txtAltCityLData.widthHint = -1;
			txtAltCityLData.heightHint = -1;
			txtAltCityLData.horizontalIndent = 0;
			txtAltCityLData.horizontalSpan = 1;
			txtAltCityLData.verticalSpan = 1;
			txtAltCityLData.grabExcessHorizontalSpace = false;
			txtAltCityLData.grabExcessVerticalSpace = false;
			txtAltCity.setLayoutData(txtAltCityLData);
	
			GridData lblAltStateLData = new GridData();
			lblAltStateLData.verticalAlignment = GridData.CENTER;
			lblAltStateLData.horizontalAlignment = GridData.BEGINNING;
			lblAltStateLData.widthHint = -1;
			lblAltStateLData.heightHint = -1;
			lblAltStateLData.horizontalIndent = 0;
			lblAltStateLData.horizontalSpan = 1;
			lblAltStateLData.verticalSpan = 1;
			lblAltStateLData.grabExcessHorizontalSpace = false;
			lblAltStateLData.grabExcessVerticalSpace = false;
			lblAltState.setLayoutData(lblAltStateLData);
			lblAltState.setText("Alt State:");
	
			GridData txtAltStateLData = new GridData();
			txtAltStateLData.verticalAlignment = GridData.CENTER;
			txtAltStateLData.horizontalAlignment = GridData.BEGINNING;
			txtAltStateLData.widthHint = -1;
			txtAltStateLData.heightHint = -1;
			txtAltStateLData.horizontalIndent = 0;
			txtAltStateLData.horizontalSpan = 1;
			txtAltStateLData.verticalSpan = 1;
			txtAltStateLData.grabExcessHorizontalSpace = false;
			txtAltStateLData.grabExcessVerticalSpace = false;
			txtAltState.setLayoutData(txtAltStateLData);
	
			GridData lblAltZipCodeLData = new GridData();
			lblAltZipCodeLData.verticalAlignment = GridData.CENTER;
			lblAltZipCodeLData.horizontalAlignment = GridData.BEGINNING;
			lblAltZipCodeLData.widthHint = -1;
			lblAltZipCodeLData.heightHint = -1;
			lblAltZipCodeLData.horizontalIndent = 0;
			lblAltZipCodeLData.horizontalSpan = 1;
			lblAltZipCodeLData.verticalSpan = 1;
			lblAltZipCodeLData.grabExcessHorizontalSpace = false;
			lblAltZipCodeLData.grabExcessVerticalSpace = false;
			lblAltZipCode.setLayoutData(lblAltZipCodeLData);
			lblAltZipCode.setText("Alt. Zip Code:");
	
			GridData txtAltZipCodeLData = new GridData();
			txtAltZipCodeLData.widthHint = 54;
			txtAltZipCodeLData.heightHint = 12;
			txtAltZipCode.setLayoutData(txtAltZipCodeLData);
            {
                btnUseAltAddress = new Button(grpAlternateAddress, SWT.CHECK
                    | SWT.LEFT);
                btnUseAltAddress.setText("Use Alt Address?");
                GridData btnUseAltAddressLData = new GridData();
                btnUseAltAddressLData.widthHint = 99;
                btnUseAltAddressLData.heightHint = 20;
                btnUseAltAddress.setLayoutData(btnUseAltAddressLData);
            }
			GridLayout grpAlternateAddressLayout = new GridLayout();
			grpAlternateAddress.setLayout(grpAlternateAddressLayout);
			grpAlternateAddressLayout.numColumns = 6;
			grpAlternateAddress.layout();
	
			GridData grpBillingInfoLData = new GridData();
			grpBillingInfoLData.verticalAlignment = GridData.CENTER;
			grpBillingInfoLData.horizontalAlignment = GridData.FILL;
			grpBillingInfoLData.widthHint = -1;
			grpBillingInfoLData.heightHint = -1;
			grpBillingInfoLData.horizontalIndent = 0;
			grpBillingInfoLData.horizontalSpan = 2;
			grpBillingInfoLData.verticalSpan = 1;
			grpBillingInfoLData.grabExcessHorizontalSpace = false;
			grpBillingInfoLData.grabExcessVerticalSpace = false;
			grpBillingInfo.setLayoutData(grpBillingInfoLData);
			grpBillingInfo.setText("Billing Info");
	
			GridData lblBillingCycleTypeLData = new GridData();
			lblBillingCycleTypeLData.verticalAlignment = GridData.CENTER;
			lblBillingCycleTypeLData.horizontalAlignment = GridData.BEGINNING;
			lblBillingCycleTypeLData.widthHint = -1;
			lblBillingCycleTypeLData.heightHint = -1;
			lblBillingCycleTypeLData.horizontalIndent = 0;
			lblBillingCycleTypeLData.horizontalSpan = 1;
			lblBillingCycleTypeLData.verticalSpan = 1;
			lblBillingCycleTypeLData.grabExcessHorizontalSpace = false;
			lblBillingCycleTypeLData.grabExcessVerticalSpace = false;
			lblBillingCycleType.setLayoutData(lblBillingCycleTypeLData);
			lblBillingCycleType.setText("Billing Cycle Type:");
	
			GridData cmbBillingCycleTypeLData = new GridData();
			cmbBillingCycleTypeLData.verticalAlignment = GridData.CENTER;
			cmbBillingCycleTypeLData.horizontalAlignment = GridData.BEGINNING;
			cmbBillingCycleTypeLData.widthHint = -1;
			cmbBillingCycleTypeLData.heightHint = -1;
			cmbBillingCycleTypeLData.horizontalIndent = 0;
			cmbBillingCycleTypeLData.horizontalSpan = 1;
			cmbBillingCycleTypeLData.verticalSpan = 1;
			cmbBillingCycleTypeLData.grabExcessHorizontalSpace = false;
			cmbBillingCycleTypeLData.grabExcessVerticalSpace = false;
			cmbBillingCycleType.setLayoutData(cmbBillingCycleTypeLData);
	
			GridData lblStartMonthLData = new GridData();
			lblStartMonthLData.verticalAlignment = GridData.CENTER;
			lblStartMonthLData.horizontalAlignment = GridData.BEGINNING;
			lblStartMonthLData.widthHint = -1;
			lblStartMonthLData.heightHint = -1;
			lblStartMonthLData.horizontalIndent = 0;
			lblStartMonthLData.horizontalSpan = 1;
			lblStartMonthLData.verticalSpan = 1;
			lblStartMonthLData.grabExcessHorizontalSpace = false;
			lblStartMonthLData.grabExcessVerticalSpace = false;
			lblStartMonth.setLayoutData(lblStartMonthLData);
			lblStartMonth.setText("Start Month:");
	
			GridData cmbStartMonthLData = new GridData();
			cmbStartMonthLData.verticalAlignment = GridData.CENTER;
			cmbStartMonthLData.horizontalAlignment = GridData.BEGINNING;
			cmbStartMonthLData.widthHint = -1;
			cmbStartMonthLData.heightHint = -1;
			cmbStartMonthLData.horizontalIndent = 0;
			cmbStartMonthLData.horizontalSpan = 1;
			cmbStartMonthLData.verticalSpan = 1;
			cmbStartMonthLData.grabExcessHorizontalSpace = false;
			cmbStartMonthLData.grabExcessVerticalSpace = false;
			cmbStartMonth.setLayoutData(cmbStartMonthLData);
	
			GridData lblInstallationDateLData = new GridData();
			lblInstallationDateLData.verticalAlignment = GridData.CENTER;
			lblInstallationDateLData.horizontalAlignment = GridData.BEGINNING;
			lblInstallationDateLData.widthHint = -1;
			lblInstallationDateLData.heightHint = -1;
			lblInstallationDateLData.horizontalIndent = 0;
			lblInstallationDateLData.horizontalSpan = 1;
			lblInstallationDateLData.verticalSpan = 1;
			lblInstallationDateLData.grabExcessHorizontalSpace = false;
			lblInstallationDateLData.grabExcessVerticalSpace = false;
			lblInstallationDate.setLayoutData(lblInstallationDateLData);
			lblInstallationDate.setText("Installation Date:");
	
			GridData txtInstallationDateLData = new GridData();
			txtInstallationDateLData.verticalAlignment = GridData.CENTER;
			txtInstallationDateLData.horizontalAlignment = GridData.BEGINNING;
			txtInstallationDateLData.widthHint = -1;
			txtInstallationDateLData.heightHint = -1;
			txtInstallationDateLData.horizontalIndent = 0;
			txtInstallationDateLData.horizontalSpan = 1;
			txtInstallationDateLData.verticalSpan = 1;
			txtInstallationDateLData.grabExcessHorizontalSpace = false;
			txtInstallationDateLData.grabExcessVerticalSpace = false;
			txtInstallationDate.setLayoutData(txtInstallationDateLData);
	
			GridData lblInstallationDateFormatLData = new GridData();
			lblInstallationDateFormatLData.verticalAlignment = GridData.CENTER;
			lblInstallationDateFormatLData.horizontalAlignment = GridData.BEGINNING;
			lblInstallationDateFormatLData.widthHint = -1;
			lblInstallationDateFormatLData.heightHint = -1;
			lblInstallationDateFormatLData.horizontalIndent = 0;
			lblInstallationDateFormatLData.horizontalSpan = 2;
			lblInstallationDateFormatLData.verticalSpan = 1;
			lblInstallationDateFormatLData.grabExcessHorizontalSpace = false;
			lblInstallationDateFormatLData.grabExcessVerticalSpace = false;
			lblInstallationDateFormat.setLayoutData(lblInstallationDateFormatLData);
			lblInstallationDateFormat.setText("MM/DD/YYYY");
	
			GridData lblRentalChargeLData = new GridData();
			lblRentalChargeLData.verticalAlignment = GridData.CENTER;
			lblRentalChargeLData.horizontalAlignment = GridData.BEGINNING;
			lblRentalChargeLData.widthHint = -1;
			lblRentalChargeLData.heightHint = -1;
			lblRentalChargeLData.horizontalIndent = 0;
			lblRentalChargeLData.horizontalSpan = 1;
			lblRentalChargeLData.verticalSpan = 1;
			lblRentalChargeLData.grabExcessHorizontalSpace = false;
			lblRentalChargeLData.grabExcessVerticalSpace = false;
			lblRentalCharge.setLayoutData(lblRentalChargeLData);
			lblRentalCharge.setText("Rental Charge:");
	
			GridData txtRentalChargeLData = new GridData();
			txtRentalChargeLData.verticalAlignment = GridData.CENTER;
			txtRentalChargeLData.horizontalAlignment = GridData.BEGINNING;
			txtRentalChargeLData.widthHint = -1;
			txtRentalChargeLData.heightHint = -1;
			txtRentalChargeLData.horizontalIndent = 0;
			txtRentalChargeLData.horizontalSpan = 1;
			txtRentalChargeLData.verticalSpan = 1;
			txtRentalChargeLData.grabExcessHorizontalSpace = false;
			txtRentalChargeLData.grabExcessVerticalSpace = false;
			txtRentalCharge.setLayoutData(txtRentalChargeLData);
	
			GridData lblAccountClosedStatusLData = new GridData();
			lblAccountClosedStatusLData.verticalAlignment = GridData.CENTER;
			lblAccountClosedStatusLData.horizontalAlignment = GridData.BEGINNING;
			lblAccountClosedStatusLData.widthHint = -1;
			lblAccountClosedStatusLData.heightHint = -1;
			lblAccountClosedStatusLData.horizontalIndent = 0;
			lblAccountClosedStatusLData.horizontalSpan = 1;
			lblAccountClosedStatusLData.verticalSpan = 1;
			lblAccountClosedStatusLData.grabExcessHorizontalSpace = false;
			lblAccountClosedStatusLData.grabExcessVerticalSpace = false;
			lblAccountClosedStatus.setLayoutData(lblAccountClosedStatusLData);
			lblAccountClosedStatus.setText("Account Closed:");
	
			GridData lblAccountClosedValueLData = new GridData();
			lblAccountClosedValueLData.verticalAlignment = GridData.CENTER;
			lblAccountClosedValueLData.horizontalAlignment = GridData.BEGINNING;
			lblAccountClosedValueLData.widthHint = 62;
			lblAccountClosedValueLData.heightHint = 13;
			lblAccountClosedValueLData.horizontalIndent = 0;
			lblAccountClosedValueLData.horizontalSpan = 1;
			lblAccountClosedValueLData.verticalSpan = 1;
			lblAccountClosedValueLData.grabExcessHorizontalSpace = false;
			lblAccountClosedValueLData.grabExcessVerticalSpace = false;
			lblAccountClosedValue.setLayoutData(lblAccountClosedValueLData);
			lblAccountClosedValue.setText("N/A");
			lblAccountClosedValue.setSize(new org.eclipse.swt.graphics.Point(62,13));
            {
                lblCredit = new Label(grpBillingInfo, SWT.NONE);
                lblCredit.setText("Credit:");
            }
            {
                txtCreditAmt = new Text(grpBillingInfo, SWT.BORDER);
            }
            {
                chkShowClosedAcct = new Button(grpBillingInfo, SWT.CHECK
                    | SWT.LEFT);
                chkShowClosedAcct.setText("Show Closed Accts?");
            }
			GridLayout grpBillingInfoLayout = new GridLayout(4, true);
			grpBillingInfo.setLayout(grpBillingInfoLayout);
			grpBillingInfoLayout.marginWidth = 5;
			grpBillingInfoLayout.marginHeight = 5;
			grpBillingInfoLayout.numColumns = 4;
			grpBillingInfoLayout.makeColumnsEqualWidth = false;
			grpBillingInfoLayout.horizontalSpacing = 5;
			grpBillingInfoLayout.verticalSpacing = 5;
			grpBillingInfo.layout();
	
			GridData grpCommentLData = new GridData();
			grpCommentLData.verticalAlignment = GridData.CENTER;
			grpCommentLData.horizontalAlignment = GridData.FILL;
			grpCommentLData.widthHint = -1;
			grpCommentLData.heightHint = 81;
			grpCommentLData.horizontalIndent = 0;
			grpCommentLData.horizontalSpan = 2;
			grpCommentLData.verticalSpan = 1;
			grpCommentLData.grabExcessHorizontalSpace = false;
			grpCommentLData.grabExcessVerticalSpace = false;
			grpComment.setLayoutData(grpCommentLData);
			grpComment.setText("Comments");
			grpComment.setSize(new org.eclipse.swt.graphics.Point(501,81));
	
			GridData txtCommentLData = new GridData();
			txtCommentLData.verticalAlignment = GridData.CENTER;
			txtCommentLData.horizontalAlignment = GridData.FILL;
			txtCommentLData.widthHint = -1;
			txtCommentLData.heightHint = 64;
			txtCommentLData.horizontalIndent = 0;
			txtCommentLData.horizontalSpan = 1;
			txtCommentLData.verticalSpan = 1;
			txtCommentLData.grabExcessHorizontalSpace = false;
			txtCommentLData.grabExcessVerticalSpace = false;
			txtComment.setLayoutData(txtCommentLData);
			txtComment.setSize(new org.eclipse.swt.graphics.Point(478,64));
			GridLayout grpCommentLayout = new GridLayout(1, true);
			grpComment.setLayout(grpCommentLayout);
			grpCommentLayout.marginWidth = 5;
			grpCommentLayout.marginHeight = 5;
			grpCommentLayout.numColumns = 1;
			grpCommentLayout.makeColumnsEqualWidth = true;
			grpCommentLayout.horizontalSpacing = 5;
			grpCommentLayout.verticalSpacing = 5;
			grpComment.layout();
	
			GridData cmpCommentAndButtonsLData = new GridData();
			cmpCommentAndButtonsLData.verticalAlignment = GridData.BEGINNING;
			cmpCommentAndButtonsLData.horizontalAlignment = GridData.FILL;
			cmpCommentAndButtonsLData.widthHint = -1;
			cmpCommentAndButtonsLData.heightHint = -1;
			cmpCommentAndButtonsLData.horizontalIndent = 0;
			cmpCommentAndButtonsLData.horizontalSpan = 2;
			cmpCommentAndButtonsLData.verticalSpan = 1;
			cmpCommentAndButtonsLData.grabExcessHorizontalSpace = false;
			cmpCommentAndButtonsLData.grabExcessVerticalSpace = false;
			cmpCommentAndButtons.setLayoutData(cmpCommentAndButtonsLData);
	
			GridData tblCustomerListLData = new GridData();
			tblCustomerListLData.verticalAlignment = GridData.FILL;
			tblCustomerListLData.horizontalAlignment = GridData.FILL;
			tblCustomerListLData.widthHint = -1;
			tblCustomerListLData.heightHint = -1;
			tblCustomerListLData.horizontalIndent = 0;
			tblCustomerListLData.horizontalSpan = 2;
			tblCustomerListLData.verticalSpan = 1;
			tblCustomerListLData.grabExcessHorizontalSpace = false;
			tblCustomerListLData.grabExcessVerticalSpace = false;
			tblCustomerList.setLayoutData(tblCustomerListLData);
			tblCustomerList.setHeaderVisible(true);
			tblCustomerList.setItemCount(5);
			tblCustomerList.setLinesVisible(true);
			tblCustomerList.setSize(new org.eclipse.swt.graphics.Point(480,89));
			tblCustomerList.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					tblCustomerListWidgetSelected(evt);
				}
			});
	
			tblClmCustomerId.setText("ID");
			tblClmCustomerId.setWidth(50);
	
			tblClmCustName.setText("Customer Name");
			tblClmCustName.setWidth(100);
	
			tblClmBillCycleType.setText("Bill Cycle Type");
			tblClmBillCycleType.setWidth(84);
	
			tblClmInstallationDate.setText("Installation Date");
			tblClmInstallationDate.setWidth(102);
	
			tblClmRentalCharge.setText("Rental Charge");
			tblClmRentalCharge.setWidth(102);
	
			GridData cmpButtonLData = new GridData();
			cmpButtonLData.verticalAlignment = GridData.BEGINNING;
			cmpButtonLData.horizontalAlignment = GridData.FILL;
			cmpButtonLData.widthHint = -1;
			cmpButtonLData.heightHint = 56;
			cmpButtonLData.horizontalIndent = 0;
			cmpButtonLData.horizontalSpan = 2;
			cmpButtonLData.verticalSpan = 1;
			cmpButtonLData.grabExcessHorizontalSpace = false;
			cmpButtonLData.grabExcessVerticalSpace = false;
			cmpButton.setLayoutData(cmpButtonLData);
			cmpButton.setSize(new org.eclipse.swt.graphics.Point(497,56));
	
			btnClear.setText("Clear");
			btnClear.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					btnClearWidgetSelected(evt);
				}
			});
	
			RowData btnFindLData = new RowData(32, 23);
			btnFind.setLayoutData(btnFindLData);
			btnFind.setText("Find");
			btnFind.setSize(new org.eclipse.swt.graphics.Point(32,23));
			btnFind.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					btnFindWidgetSelected(evt);
				}
			});
	
			btnCreate.setText("Create");
			btnCreate.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					btnCreateWidgetSelected(evt);
				}
			});
	
			btnUpdate.setText("Update");
			btnUpdate.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					btnUpdateWidgetSelected(evt);
				}
			});
	
			btnCloseAcct.setText("Close Acct.");
			btnCloseAcct.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					btnCloseAcctWidgetSelected(evt);
				}
			});
	
			btnFindAll.setText("Find All");
			btnFindAll.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					btnFindAllWidgetSelected(evt);
				}
			});
			RowLayout cmpButtonLayout = new RowLayout(256);
			cmpButton.setLayout(cmpButtonLayout);
			cmpButtonLayout.type = SWT.HORIZONTAL;
			cmpButtonLayout.marginWidth = 0;
			cmpButtonLayout.marginHeight = 0;
			cmpButtonLayout.spacing = 3;
			cmpButtonLayout.wrap = true;
			cmpButtonLayout.pack = true;
			cmpButtonLayout.fill = false;
			cmpButtonLayout.justify = false;
			cmpButtonLayout.marginLeft = 3;
			cmpButtonLayout.marginTop = 3;
			cmpButtonLayout.marginRight = 3;
			cmpButtonLayout.marginBottom = 3;
			cmpButton.layout();
			GridLayout cmpCommentAndButtonsLayout = new GridLayout(2, true);
			cmpCommentAndButtons.setLayout(cmpCommentAndButtonsLayout);
			cmpCommentAndButtonsLayout.marginWidth = 5;
			cmpCommentAndButtonsLayout.marginHeight = 5;
			cmpCommentAndButtonsLayout.numColumns = 2;
			cmpCommentAndButtonsLayout.makeColumnsEqualWidth = true;
			cmpCommentAndButtonsLayout.horizontalSpacing = 5;
			cmpCommentAndButtonsLayout.verticalSpacing = 5;
			cmpCommentAndButtons.layout();
			GridLayout thisLayout = new GridLayout(2, true);
			this.setLayout(thisLayout);
			thisLayout.marginWidth = 5;
			thisLayout.marginHeight = 5;
			thisLayout.numColumns = 2;
			thisLayout.makeColumnsEqualWidth = true;
			thisLayout.horizontalSpacing = 5;
			thisLayout.verticalSpacing = 5;
			this.layout();
	
			postInitGUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    /** Add your pre-init code in here */
    public void preInitGUI() {
    }

    /** Add your post-init code in here */
    public void postInitGUI() {
    }

    /** Auto-generated main method */
    public static void main(String[] args) {
        showGUI();
    }

	/**
	* This static method creates a new instance of this class and shows
	* it inside a new Shell.
	*
	* It is a convenience method for showing the GUI, but it can be
	* copied and used as a basis for your own code.	*
	* It is auto-generated code - the body of this method will be
	* re-generated after any changes are made to the GUI.
	* However, if you delete this method it will not be re-created.	*/
    public static void showGUI(){
		try {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			CustomerUI inst = new CustomerUI(shell, SWT.NULL);
			shell.setLayout(new org.eclipse.swt.layout.FillLayout());
			shell.setBounds(22, 29, 768, 600);
			Rectangle shellBounds = shell.computeTrim(0,0,518,690);
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
    /**
     * Auto-generated method
     */
    public Table getTblCustomerList() {
        return tblCustomerList;
    }

    /** Auto-generated event handler method */
    protected void btnClearWidgetSelected(SelectionEvent evt) {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering btnClearWidgetSelected.");
        }
        txtCustomerId.setText("");
        txtLastName.setText("");
        txtFirstName.setText("");
        txtCompanyName.setText("");
        txtAddress1.setText("");
        txtAddress2.setText("");
        txtCity.setText("");
        txtState.setText("");
        txtZipCode.setText("");
        txtAltAddress1.setText("");
        txtAltAddress2.setText("");
        txtAltCity.setText("");
        txtAltState.setText("");
        txtAltZipCode.setText("");
        txtInstallationDate.setText("");
        txtRentalCharge.setText("");
        txtComment.setText("");
        cmbBillingCycleType.setText("");
        cmbStartMonth.setText("");
        lblAccountClosedValue.setText("N/A");
        tblCustomerList.removeAll();
    }

    /** Auto-generated event handler method */
    protected void btnFindWidgetSelected(SelectionEvent evt) {
		//Holds the list of customers that matches the template
		List foundCustomerList = null;

        if (logger.isDebugEnabled()) {
            logger.debug("Entering btnFindWidgetSelected");
        }
		//get the template customer data to search from
        Customer cust = buildCustomerFromFields();
        if (logger.isDebugEnabled()) {
            logger.debug("custFromFields = " + cust.toString());
        }

		//Get the list of customers that match the template

		try {
			foundCustomerList = custService.getMatchingCustomers(cust);
		} catch (BWSGenericException e1) {
			e1.printStackTrace();
		}

		if (foundCustomerList != null && foundCustomerList.size() == 1) {
			//There is only one customer and we can populate the fields
			Customer foundCustomer = null;

			//get customer
			foundCustomer = (Customer) foundCustomerList.get(0);

			//load the fields with the customer
			loadFieldsWithCustomer(foundCustomer);

			//Clear the customer table
			tblCustomerList.removeAll();

		} else if (foundCustomerList != null && foundCustomerList.size() > 1) {
			//There is more than one customer that matches so we will populate
			// the list
			//and populate the comments with the commen that there were too
			// many
			initializeCustomerTable(foundCustomerList);
		} else {
			logger.error("No customers match the critera - " + cust.toString());
            MessageBox messageBox = new MessageBox(this.getShell(),
                    SWT.ICON_ERROR | SWT.OK);
            messageBox.setText("Find error!!!");
			messageBox.setMessage("No customers match the critera specified");
            messageBox.open();
            return;
        }
        }

    /**
     *  
     */
    private void alertNoCustomersMatchingSearch() {
        MessageBox messageBox = new MessageBox(this.getShell(),
                SWT.ICON_INFORMATION | SWT.OK);
        messageBox.setText("Information Not Available");
        messageBox
                .setMessage("There were no customers matching your search criteria.");
        messageBox.open();
    }

    /**
     * Will return the customer that was built based on the information in the
     * fields
     * 
     * @return Customer
     */
    private Customer buildCustomerFromFields() {
        Customer cust = new Customer();
        cust.setId((txtCustomerId.getText() != null && !txtCustomerId.getText()
                .equalsIgnoreCase("")) ? new Integer(txtCustomerId.getText())
                : null);
        cust.setLastName((txtLastName.getText() != null && !txtLastName
                .getText().equalsIgnoreCase("")) ? txtLastName.getText() : "");

        cust.setFirstName((txtFirstName.getText() != null && !txtFirstName
                .getText().equalsIgnoreCase("")) ? txtFirstName.getText() : "");

        cust
                .setCompanyName((txtCompanyName.getText() != null && !txtCompanyName
                        .getText().equalsIgnoreCase("")) ? txtCompanyName
                        .getText() : "");

        cust.setAddress1((txtAddress1.getText() != null && !txtAddress1
                .getText().equalsIgnoreCase("")) ? txtAddress1.getText() : "");

        cust.setAddress2((txtAddress2.getText() != null && !txtAddress2
                .getText().equalsIgnoreCase("")) ? txtAddress2.getText() : "");

        cust.setCity((txtCity.getText() != null && !txtCity.getText()
                .equalsIgnoreCase("")) ? txtCity.getText() : "");

        cust.setState((txtState.getText() != null && !txtState.getText()
                .equalsIgnoreCase("")) ? txtState.getText() : "");

        cust.setZipCode((txtZipCode.getText() != null && !txtZipCode.getText()
                .equalsIgnoreCase("")) ? txtZipCode.getText() : "");

        cust
                .setAltAddress1((txtAltAddress1.getText() != null && !txtAltAddress1
                        .getText().equalsIgnoreCase("")) ? txtAltAddress1
                        .getText() : "");

        cust
                .setAltAddress2((txtAltAddress2.getText() != null && !txtAltAddress2
                        .getText().equalsIgnoreCase("")) ? txtAltAddress2
                        .getText() : "");

        cust.setAltCity((txtAltCity.getText() != null && !txtAltCity.getText()
                .equalsIgnoreCase("")) ? txtAltCity.getText() : "");

        cust.setAltState((txtAltState.getText() != null && !txtAltState
                .getText().equalsIgnoreCase("")) ? txtAltState.getText() : "");

        cust
                .setAltZipCode((txtAltZipCode.getText() != null && !txtAltZipCode
                        .getText().equalsIgnoreCase("")) ? txtAltZipCode
                        .getText() : "");
        cust.setUseAltAddress(btnUseAltAddress.getSelection());

        /*
         * 
         * Must experiment with retrieving values from the dropdowns
         */

        Date installDate = null;
        if (txtInstallationDate.getText() != null
                && !txtInstallationDate.getText().equalsIgnoreCase("")) {
            installDate = sdf.parse(txtInstallationDate.getText(),
                    new ParsePosition(0));
        }

        String billTypeDesc = cmbBillingCycleType.getText();
        cust.setBillCycleId(getBillCycleIdByBillCycleDesc(billTypeDesc));

        if (logger.isDebugEnabled()) {
            logger.debug("BillTypeDesc " + billTypeDesc
                    + " and billCycleTypeId " + cust.getBillCycleId());
        }

        if (cmbStartMonth.getText() != null) {
            cust.setBillStartMonth(cmbStartMonth.getText());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Start Month " + cust.getBillStartMonth());
        }

        cust.setInstallationDate(installDate);

        try {
            cust
                    .setRentalCharge((txtRentalCharge.getText() != null && !txtRentalCharge
                            .getText().equalsIgnoreCase("")) ? new Double(
                            txtRentalCharge.getText()) : new Double(0));
            // must be able to handle when the numbers have a comma
            cust
            		.setCreditBalance((txtCreditAmt.getText() != null && !txtCreditAmt
            		        .getText().equalsIgnoreCase("")) ? 
            		         df.parse(txtCreditAmt.getText()).doubleValue() : new Double(0).doubleValue());
        } catch (Exception e) {
            logger.error("Number exception occurred " + e.getMessage(), e);
        }

        cust.setComments((txtComment.getText() != null && !txtComment.getText()
                .equalsIgnoreCase("")) ? txtComment.getText() : "");
        // build the closeAccountDate from the fields
        String accountClosedValue = lblAccountClosedValue.getText();
        if (Character.isDigit(accountClosedValue.charAt(0))) {
            cust.setCloseAccountDate(sdf.parse(accountClosedValue,
                    new ParsePosition(0)));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Set the customer close account date to "
                    + cust.getCloseAccountDate());
        }

        return cust;
    }

    /** Auto-generated event handler method */
    protected void btnCreateWidgetSelected(SelectionEvent evt) {
        // make sure that all of the fields that are required are filled in.
        // create the customer.
        if (logger.isDebugEnabled()) {
            logger.debug("Entering btnCreateWidgetSelected");
        }

        Customer customer = this.buildCustomerFromFields();
        
        try {
            if (customer.getId() != null) {
                throw new ValidFieldsUnavailableException("When creating a customer, you must not have a value for the id");
            }
            custService.createCustomer(customer);
        } catch (ValidFieldsUnavailableException e) {
            MessageBox messageBox = new MessageBox(this.getShell(),
                    SWT.ICON_ERROR | SWT.OK);
            messageBox.setText("Create error!!!");
            messageBox.setMessage(e.getMessage());
            messageBox.open();
            return;
        } catch (BWSGenericException e) {
            MessageBox messageBox = new MessageBox(this.getShell(),
                    SWT.ICON_ERROR | SWT.OK);
            messageBox.setText("Create error!!!");
            messageBox.setMessage(e.getMessage());
            messageBox.open();
            return;
        }
		initializeCustomerTable(custService.getAllCustomers());

    }

    /** Auto-generated event handler method */
    protected void btnUpdateWidgetSelected(SelectionEvent evt) {
        // make sure that all of the fields that are required are filled in.
        if (logger.isDebugEnabled()) {
            logger.debug("Entering btnUpdateWigetSelected");
        }
        Customer customer = this.buildCustomerFromFields();
        custService.updateCustomer(customer);
        initializeCustomerTable(custService.getAllCustomers());

    }

    /** Auto-generated event handler method */
    protected void btnCloseAcctWidgetSelected(SelectionEvent evt) {
        // make sure the required fields are filled in
        // notify the user to see if they are sure
        // close the customer account
        if (logger.isDebugEnabled()) {
            logger.debug("Entering btnCloseAcctWidgetSelected");
        }
        Customer customer = this.buildCustomerFromFields();
        InputDialog inputDialog = new InputDialog(this.getShell(),
                SWT.APPLICATION_MODAL);
        inputDialog.setText("Close Account");
        inputDialog
                .setQuestionText("Please enter the close account date, MM/DD/YYYY");
        
        InputDialogData idd = inputDialog.open();
        
        if (logger.isDebugEnabled()) {
            logger.debug("idd User Input " + idd.getUserInput());
            logger.debug("idd button " + idd.isButtonResponse());
        }
        if (idd.isButtonResponse()) {
            try {
               customer.setCloseAccountDate(sdf.parse(idd.getUserInput().trim(), new ParsePosition(0)));
            	custService.closeCustomerAccount(customer);
				initializeCustomerTable(custService.getAllCustomers());
				if (customer.getCloseAccountDate() != null) {
					lblAccountClosedValue.setText(sdf.format(customer.getCloseAccountDate()));
				}
				
            } catch (BWSGenericException e) {
                MessageBox messageBox = new MessageBox(this.getShell(),
                        SWT.ICON_ERROR | SWT.OK);
                messageBox.setText("Close account error!!!");
                messageBox.setMessage(e.getMessage());
                messageBox.open();
                return;
            }
        }

    }

    /** Auto-generated event handler method */
    protected void btnFindAllWidgetSelected(SelectionEvent evt) {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering btnFindAllWidgetSelected");
        }

		List customerList = custService.getAllCustomers();
        if (customerList == null || customerList.size() == 0) {
            alertNoCustomersMatchingSearch();
            return;
        }
        initializeCustomerTable(customerList);
    }

	protected void btnReopenAccountWidgetSelected(SelectionEvent evt) {
		logger.debug("Entering btnReopenAccountWidgetSelected");
		Customer cust = new Customer();
		
		if (txtCustomerId.getText() == null || txtCustomerId.getText().trim().equals("")) {
            MessageBox messageBox = new MessageBox(this.getShell(),
                    SWT.ICON_ERROR | SWT.OK);
            messageBox.setText("Invalid Customer Id");
            messageBox.setMessage("The customer Id must be set when reopening an account");
            messageBox.open();
		} else {
			cust.setId(new Integer(txtCustomerId.getText()));
			cust.setCloseAccountDate(null);
			custService.updateCustomer(cust);
			lblAccountClosedValue.setText("No");
			
			TableItem ti = tblCustomerList.getSelection()[tblCustomerList.getSelectionIndex()];
            ti.setBackground(null);
		}
		
	}

    /**
     * Will add the entries to the customer table
     * 
     * @param customerList
     */
    private void initializeCustomerTable(List customerList) {
        // first clear out the values that may already exist
        tblCustomerList.removeAll();
        for (Iterator iter = customerList.iterator(); iter.hasNext();) {
            Customer customer = (Customer) iter.next();
            // if the show closed account is not selected and the close account date is not null
            // then we will not show it.
            if (!chkShowClosedAcct.getSelection() && customer.getCloseAccountDate() != null) {
                continue;
            }
            
            TableItem tableItem = new TableItem(tblCustomerList, SWT.NONE);
            if (logger.isDebugEnabled()) {
                logger.debug("addCustomerToTable: " + customer.toString());
            }
            
            
            String name = customer.getFormalName();

            if (name == null || name.equalsIgnoreCase("")) {
                name = "N/A";
            }

            String installDate = sdf.format(customer.getInstallationDate());
            tableItem.setText(new String[] {
                    customer.getId().toString(),
                    name,
                    getBillCycleGroupDescByBillCycleId(customer
                            .getBillCycleId()), installDate,
                    df.format(customer.getRentalCharge()) });
            
            // if the customer is closed, then we will make the text white and the background red
            if (customer.getCloseAccountDate() != null) {
                tableItem.setBackground(new Color(this.getDisplay(), 255, 0, 0));
                tableItem.setForeground(new Color(this.getDisplay(), 0,0,0));
                
            }
        }
    }

    /** Auto-generated event handler method */
    protected void tblCustomerListWidgetSelected(SelectionEvent evt) {
        TableItem ti = (TableItem) evt.item;
        if (logger.isDebugEnabled()) {
            logger.debug("Customer Id: " + ti.getText(0));
        }
		Customer cust = getCustomerByTableItem(ti);

        loadFieldsWithCustomer(cust);

    }

    /**
     * Will retrieve a customer based on the TableItem that is passed in
     * @param ti tableItem that we are retrieving a customer for.
     * @return Customer object representing the tableItem
     */
    private Customer getCustomerByTableItem(TableItem ti) {
		//Get the CustomerID for the selected line
        Customer searchCust = new Customer();
        Integer customerId = (ti.getText(0) != null && !ti.getText(0)
                .equalsIgnoreCase("")) ? new Integer(ti.getText(0)) : null;
                

         //Set the customer search object's id field
		searchCust.setId(customerId);

		Customer customer = null;
		try {
			//This will only return one field so we return field 0
			customer = (Customer) custService.getMatchingCustomers(
					searchCust).get(0);
		} catch (BWSGenericException e) {
			e.printStackTrace();
		}
		
		return customer;
    }

    /**
     * Responsible for updating all of the text objects on the screen with the
     * value of the customer
     * 
     * @param cust -
     *            customer
     */
    private void loadFieldsWithCustomer(Customer cust) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        txtCustomerId.setText(cust.getId().toString());
        txtLastName.setText((cust.getLastName() != null) ? cust.getLastName()
                : "");
        txtFirstName.setText((cust.getFirstName() != null) ? cust
                .getFirstName() : "");
        txtCompanyName.setText(cust.getCompanyName());
        txtAddress1.setText(cust.getAddress1());
        txtAddress2.setText(cust.getAddress2());
        txtCity.setText(cust.getCity());
        txtState.setText(cust.getState());
        txtZipCode.setText(cust.getZipCode());
        txtAltAddress1.setText(cust.getAltAddress1());
        txtAltAddress2.setText(cust.getAltAddress2());
        txtAltCity.setText(cust.getAltCity());
        txtAltState.setText(cust.getAltState());
        txtAltZipCode.setText(cust.getAltZipCode());
        txtInstallationDate.setText(sdf.format(cust.getInstallationDate()));
        txtRentalCharge.setText(df.format(cust.getRentalCharge()));
        txtComment.setText(cust.getComments());
        txtCreditAmt.setText(df.format(cust.getCreditBalance()));
        btnUseAltAddress.setSelection(cust.isUseAltAddress());
        
        if (logger.isDebugEnabled()) {
            logger.debug("Customer Close Account Date = "
                    + cust.getCloseAccountDate());
        }
        lblAccountClosedValue
                .setText((cust.getCloseAccountDate() != null) ? sdf.format(cust
                        .getCloseAccountDate()) : "No");
        // we will now update the cmbbilling cycle group list
        cmbBillingCycleType.setText(getBillCycleGroupDescByBillCycleId(cust
                .getBillCycleId()));

        // we will now update teh cmbBillStartMonth
        cmbStartMonth.setText(cust.getBillStartMonth());
    }

    /**
     * @param billCycleId
     * @return
     */
    private String getBillCycleGroupDescByBillCycleId(Integer billCycleId) {
        for (Iterator iter = billCycleGroupList.iterator(); iter.hasNext();) {
            Map billCycleGroupMap = (Map) iter.next();
            if (billCycleId.equals((Integer) billCycleGroupMap.get(Messages
                    .getString("bcg.id")))) {
                return (String) billCycleGroupMap.get(Messages
                        .getString("bcg.desc"));
            }
        }
        return "N/A";
    }

    /**
     * @param billCycleId
     * @return
     */
    private Integer getBillCycleIdByBillCycleDesc(String billCycleDesc) {
        for (Iterator iter = billCycleGroupList.iterator(); iter.hasNext();) {
            Map billCycleGroupMap = (Map) iter.next();
            if (billCycleDesc.equals((String) billCycleGroupMap.get(Messages
                    .getString("bcg.desc")))) {
                return (Integer) billCycleGroupMap.get(Messages
                        .getString("bcg.id"));
            }
        }
        return new Integer(0);
    }
}