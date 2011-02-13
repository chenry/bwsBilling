package com.hs.bws.ui;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
import com.hs.bws.exception.OpenInvoicesNotAvailableException;
import com.hs.bws.service.CustomerService;
import com.hs.bws.service.InvoiceService;
import com.hs.bws.util.CustInvoiceDueDateComparator;
import com.hs.bws.util.CustomerNameComparator;
import com.hs.bws.util.Messages;
import com.hs.bws.valueObject.Customer;
import com.hs.bws.valueObject.CustomerInvoice;
import com.hs.bws.valueObject.InputDialogData;
import com.hs.bws.valueObject.Payment;
import com.hs.bws.valueObject.PendingService;

/**
* This code was generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a
* for-profit company or business) then you should purchase
* a license - please visit www.cloudgarden.com for details.
*/
public class CustomerInvoiceUI extends org.eclipse.swt.widgets.Composite {
	private Customer currentCustomer;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    DecimalFormat df = new DecimalFormat("##,##0.00");
    Logger logger = Logger.getLogger(CustomerInvoiceUI.class);
	private Label lblRentalChargeValue;
	private Label lblRentalCharge;
	private Label lblInstallDateValue;
	private Label lblInstallDate;
	private Label lblStartMonthValue;
	private Label lblStartMonth;
	private Label lblZipCodeValue;
	private Label lblZipCode;
	private Label lblStateValue;
	private Label lblState;
	private Label lblCityValue;
	private Label lblCity;
	private Label lblAddress1Value;
	private Label lblAddress1;
	private Label lblNameValue;
	protected CustomerService custService;
	protected InvoiceService invService;
	private Combo cmbCustomerName;
	private Label lblCustName;
	private Button btnSearch;
	private Button btnApply;
	private Text txtPaymentAmt;
	private Label lblApplyPayment;
	private Composite cmpApplyPayment;
	private Label lblCreditValue;
	private Label lblCredit;
	private Button btnPrintInvoice;
	private TableColumn tblClmPendServId;
	private TableColumn tblClmChargeAmt;
	private TableColumn tblClmDescription;
	private Button btnDeletePendingServ;
	private Label lblPaymentType;
	private Combo cmbPaymentType;
	private Button btnEditPendingServ;
	private Button btnCreatePendingServ;
	private Table tblPendingService;
	private Composite cmpInvoicePendingService;
	private Group grpPendingServices;
	private Button btnEditInvoice;
	private TableColumn tblclmTotalInvoiceAmt;
	private TableColumn tblclmDueDate;
	private TableColumn tblclmItemCounts;
	private TableColumn tblclmCreationDate;
	private TableColumn tbclmInvoiceNbr;
	private Table tblInvoiceList;
	private Group grpInvoiceList;
	private Label lblName;
	private Group grpCustInfo;
	private Text txtCustId;
	private Label lblCustId;
	private Composite cmpSearch;
	public CustomerInvoiceUI(Composite parent, int style) {
		super(parent, style);
		initServices();
		initGUI();
		initFields();
		this.getShell().setText("Customer Invoice");
	}
	
	protected void initServices() {
	    custService = CustomerService.getInstance();
	    invService = new InvoiceService();
	}

	/**
     * This method will be used to initialize the fields before the 
     * screen is displayed. 
     */
    private void initFields() {
        // will populate combo boxes
        List customerList = custService.getAllCustomers();
        Collections.sort(customerList, new CustomerNameComparator());
        for (Iterator iter = customerList.iterator(); iter.hasNext();) {
            Customer cust = (Customer) iter.next();
            // we will not show any of the customers who have cancelled
            if (cust.getCloseAccountDate() != null) {
                continue;
            }
            cmbCustomerName.add(getCustomerDropDownValue(cust));
        }
        
        // populate the combo payment type combo box.
        cmbPaymentType.add(Messages.getString("paymentTypeDesc.regular"));
        cmbPaymentType.add(Messages.getString("paymentTypeDesc.forgiving"));
        
        cmbPaymentType.select(0);
        
    }
    
    /**
     * Responsible for the format of the combo drop down box
     * for the customer value
     * @param cust - Customer object that will be evaluated.
     * @return String representing the value for the drop down box.
     */
    private String getCustomerDropDownValue(Customer cust) {
        return cust.getId() + " - " + cust.getFormalName();
    }

	/**
	* Initializes the GUI.
	* Auto-generated code - any changes you make will disappear.
	*/
	public void initGUI(){
		try {
			preInitGUI();
	
			cmpSearch = new Composite(this,SWT.NULL);
			lblCustId = new Label(cmpSearch,SWT.NULL);
			txtCustId = new Text(cmpSearch,SWT.BORDER);
			btnSearch = new Button(cmpSearch,SWT.PUSH| SWT.CENTER);
			lblCustName = new Label(cmpSearch,SWT.NULL);
			cmbCustomerName = new Combo(cmpSearch,SWT.NULL);
			grpCustInfo = new Group(this,SWT.NULL);
			lblName = new Label(grpCustInfo,SWT.NULL);
			lblNameValue = new Label(grpCustInfo,SWT.NULL);
			lblAddress1 = new Label(grpCustInfo,SWT.NULL);
			lblAddress1Value = new Label(grpCustInfo,SWT.NULL);
			lblCity = new Label(grpCustInfo,SWT.NULL);
			lblCityValue = new Label(grpCustInfo,SWT.NULL);
			lblState = new Label(grpCustInfo,SWT.NULL);
			lblStateValue = new Label(grpCustInfo,SWT.NULL);
			lblZipCode = new Label(grpCustInfo,SWT.NULL);
			lblZipCodeValue = new Label(grpCustInfo,SWT.NULL);
			lblStartMonth = new Label(grpCustInfo,SWT.NULL);
			lblStartMonthValue = new Label(grpCustInfo,SWT.NULL);
			lblInstallDate = new Label(grpCustInfo,SWT.NULL);
			lblInstallDateValue = new Label(grpCustInfo,SWT.NULL);
			lblRentalCharge = new Label(grpCustInfo,SWT.NULL);
			lblRentalChargeValue = new Label(grpCustInfo,SWT.NULL);

			this.setSize(457, 658);
	
			GridData cmpSearchLData = new GridData();
			cmpSearchLData.horizontalAlignment = GridData.FILL;
			cmpSearchLData.heightHint = 63;
			cmpSearchLData.horizontalSpan = 2;
			cmpSearch.setLayoutData(cmpSearchLData);
			cmpSearch.setSize(new org.eclipse.swt.graphics.Point(431,63));
	
			GridData lblCustIdLData = new GridData();
			lblCustIdLData.verticalAlignment = GridData.CENTER;
			lblCustIdLData.horizontalAlignment = GridData.BEGINNING;
			lblCustIdLData.widthHint = -1;
			lblCustIdLData.heightHint = -1;
			lblCustIdLData.horizontalIndent = 0;
			lblCustIdLData.horizontalSpan = 1;
			lblCustIdLData.verticalSpan = 1;
			lblCustIdLData.grabExcessHorizontalSpace = false;
			lblCustIdLData.grabExcessVerticalSpace = false;
			lblCustId.setLayoutData(lblCustIdLData);
			lblCustId.setText("Customer Id:");
	
			GridData txtCustIdLData = new GridData();
			txtCustIdLData.verticalAlignment = GridData.CENTER;
			txtCustIdLData.horizontalAlignment = GridData.BEGINNING;
			txtCustIdLData.widthHint = -1;
			txtCustIdLData.heightHint = -1;
			txtCustIdLData.horizontalIndent = 0;
			txtCustIdLData.horizontalSpan = 1;
			txtCustIdLData.verticalSpan = 1;
			txtCustIdLData.grabExcessHorizontalSpace = false;
			txtCustIdLData.grabExcessVerticalSpace = false;
			txtCustId.setLayoutData(txtCustIdLData);
	
			GridData btnSearchLData = new GridData();
			btnSearchLData.verticalAlignment = GridData.CENTER;
			btnSearchLData.horizontalAlignment = GridData.BEGINNING;
			btnSearchLData.widthHint = 45;
			btnSearchLData.heightHint = 23;
			btnSearchLData.horizontalIndent = 0;
			btnSearchLData.horizontalSpan = 1;
			btnSearchLData.verticalSpan = 1;
			btnSearchLData.grabExcessHorizontalSpace = false;
			btnSearchLData.grabExcessVerticalSpace = false;
			btnSearch.setLayoutData(btnSearchLData);
			btnSearch.setText("Search");
			btnSearch.setSize(new org.eclipse.swt.graphics.Point(45,23));
			btnSearch.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					btnSearchWidgetSelected(evt);
				}
			});

			GridData lblCustNameLData = new GridData();
			lblCustNameLData.verticalAlignment = GridData.CENTER;
			lblCustNameLData.horizontalAlignment = GridData.BEGINNING;
			lblCustNameLData.widthHint = -1;
			lblCustNameLData.heightHint = -1;
			lblCustNameLData.horizontalIndent = 0;
			lblCustNameLData.horizontalSpan = 1;
			lblCustNameLData.verticalSpan = 1;
			lblCustNameLData.grabExcessHorizontalSpace = false;
			lblCustNameLData.grabExcessVerticalSpace = false;
			lblCustName.setLayoutData(lblCustNameLData);
			lblCustName.setText("Name:");
	
			GridData cmbCustomerNameLData = new GridData();
			cmbCustomerNameLData.verticalAlignment = GridData.CENTER;
			cmbCustomerNameLData.horizontalAlignment = GridData.BEGINNING;
			cmbCustomerNameLData.widthHint = 150;
			cmbCustomerNameLData.heightHint = 21;
			cmbCustomerNameLData.horizontalIndent = 0;
			cmbCustomerNameLData.horizontalSpan = 1;
			cmbCustomerNameLData.verticalSpan = 1;
			cmbCustomerNameLData.grabExcessHorizontalSpace = false;
			cmbCustomerNameLData.grabExcessVerticalSpace = false;
			cmbCustomerName.setLayoutData(cmbCustomerNameLData);
			cmbCustomerName.setSize(new org.eclipse.swt.graphics.Point(150,21));
			GridLayout cmpSearchLayout = new GridLayout();
			cmpSearch.setLayout(cmpSearchLayout);
			cmpSearchLayout.numColumns = 3;
			cmpSearch.layout();
	
			GridData grpCustInfoLData = new GridData();
			grpCustInfoLData.horizontalAlignment = GridData.FILL;
			grpCustInfoLData.heightHint = 95;
			grpCustInfoLData.horizontalSpan = 2;
			grpCustInfo.setLayoutData(grpCustInfoLData);
			grpCustInfo.setText("Customer Information");
	
			GridData lblNameLData = new GridData();
			lblNameLData.verticalAlignment = GridData.CENTER;
			lblNameLData.horizontalAlignment = GridData.END;
			lblNameLData.widthHint = -1;
			lblNameLData.heightHint = -1;
			lblNameLData.horizontalIndent = 0;
			lblNameLData.horizontalSpan = 1;
			lblNameLData.verticalSpan = 1;
			lblNameLData.grabExcessHorizontalSpace = false;
			lblNameLData.grabExcessVerticalSpace = false;
			lblName.setLayoutData(lblNameLData);
			lblName.setText("Name:");
	
			GridData lblNameValueLData = new GridData();
			lblNameValueLData.verticalAlignment = GridData.CENTER;
			lblNameValueLData.horizontalAlignment = GridData.FILL;
			lblNameValueLData.widthHint = -1;
			lblNameValueLData.heightHint = -1;
			lblNameValueLData.horizontalIndent = 0;
			lblNameValueLData.horizontalSpan = 5;
			lblNameValueLData.verticalSpan = 1;
			lblNameValueLData.grabExcessHorizontalSpace = false;
			lblNameValueLData.grabExcessVerticalSpace = false;
			lblNameValue.setLayoutData(lblNameValueLData);
			lblNameValue.setText("<Name>");
	
			GridData lblAddress1LData = new GridData();
			lblAddress1LData.verticalAlignment = GridData.CENTER;
			lblAddress1LData.horizontalAlignment = GridData.END;
			lblAddress1LData.widthHint = -1;
			lblAddress1LData.heightHint = -1;
			lblAddress1LData.horizontalIndent = 0;
			lblAddress1LData.horizontalSpan = 1;
			lblAddress1LData.verticalSpan = 1;
			lblAddress1LData.grabExcessHorizontalSpace = false;
			lblAddress1LData.grabExcessVerticalSpace = false;
			lblAddress1.setLayoutData(lblAddress1LData);
			lblAddress1.setText("Address 1:");
	
			GridData lblAddress1ValueLData = new GridData();
			lblAddress1ValueLData.verticalAlignment = GridData.CENTER;
			lblAddress1ValueLData.horizontalAlignment = GridData.FILL;
			lblAddress1ValueLData.widthHint = -1;
			lblAddress1ValueLData.heightHint = -1;
			lblAddress1ValueLData.horizontalIndent = 0;
			lblAddress1ValueLData.horizontalSpan = 5;
			lblAddress1ValueLData.verticalSpan = 1;
			lblAddress1ValueLData.grabExcessHorizontalSpace = false;
			lblAddress1ValueLData.grabExcessVerticalSpace = false;
			lblAddress1Value.setLayoutData(lblAddress1ValueLData);
			lblAddress1Value.setText("<Address 1>");
	
			GridData lblCityLData = new GridData();
			lblCityLData.verticalAlignment = GridData.CENTER;
			lblCityLData.horizontalAlignment = GridData.END;
			lblCityLData.widthHint = -1;
			lblCityLData.heightHint = -1;
			lblCityLData.horizontalIndent = 0;
			lblCityLData.horizontalSpan = 1;
			lblCityLData.verticalSpan = 1;
			lblCityLData.grabExcessHorizontalSpace = false;
			lblCityLData.grabExcessVerticalSpace = false;
			lblCity.setLayoutData(lblCityLData);
			lblCity.setText("City:");
	
			GridData lblCityValueLData = new GridData();
			lblCityValueLData.verticalAlignment = GridData.CENTER;
			lblCityValueLData.horizontalAlignment = GridData.FILL;
			lblCityValueLData.widthHint = -1;
			lblCityValueLData.heightHint = -1;
			lblCityValueLData.horizontalIndent = 0;
			lblCityValueLData.horizontalSpan = 1;
			lblCityValueLData.verticalSpan = 1;
			lblCityValueLData.grabExcessHorizontalSpace = false;
			lblCityValueLData.grabExcessVerticalSpace = false;
			lblCityValue.setLayoutData(lblCityValueLData);
			lblCityValue.setText("<City>");
	
			GridData lblStateLData = new GridData();
			lblStateLData.verticalAlignment = GridData.CENTER;
			lblStateLData.horizontalAlignment = GridData.END;
			lblStateLData.widthHint = -1;
			lblStateLData.heightHint = -1;
			lblStateLData.horizontalIndent = 0;
			lblStateLData.horizontalSpan = 1;
			lblStateLData.verticalSpan = 1;
			lblStateLData.grabExcessHorizontalSpace = false;
			lblStateLData.grabExcessVerticalSpace = false;
			lblState.setLayoutData(lblStateLData);
			lblState.setText("State:");
	
			GridData lblStateValueLData = new GridData();
			lblStateValueLData.verticalAlignment = GridData.CENTER;
			lblStateValueLData.horizontalAlignment = GridData.FILL;
			lblStateValueLData.widthHint = -1;
			lblStateValueLData.heightHint = -1;
			lblStateValueLData.horizontalIndent = 0;
			lblStateValueLData.horizontalSpan = 1;
			lblStateValueLData.verticalSpan = 1;
			lblStateValueLData.grabExcessHorizontalSpace = false;
			lblStateValueLData.grabExcessVerticalSpace = false;
			lblStateValue.setLayoutData(lblStateValueLData);
			lblStateValue.setText("<State>");
	
			GridData lblZipCodeLData = new GridData();
			lblZipCodeLData.verticalAlignment = GridData.CENTER;
			lblZipCodeLData.horizontalAlignment = GridData.END;
			lblZipCodeLData.widthHint = -1;
			lblZipCodeLData.heightHint = -1;
			lblZipCodeLData.horizontalIndent = 0;
			lblZipCodeLData.horizontalSpan = 1;
			lblZipCodeLData.verticalSpan = 1;
			lblZipCodeLData.grabExcessHorizontalSpace = false;
			lblZipCodeLData.grabExcessVerticalSpace = false;
			lblZipCode.setLayoutData(lblZipCodeLData);
			lblZipCode.setText("Zip Code:");
	
			GridData lblZipCodeValueLData = new GridData();
			lblZipCodeValueLData.verticalAlignment = GridData.CENTER;
			lblZipCodeValueLData.horizontalAlignment = GridData.FILL;
			lblZipCodeValueLData.widthHint = -1;
			lblZipCodeValueLData.heightHint = -1;
			lblZipCodeValueLData.horizontalIndent = 0;
			lblZipCodeValueLData.horizontalSpan = 1;
			lblZipCodeValueLData.verticalSpan = 1;
			lblZipCodeValueLData.grabExcessHorizontalSpace = false;
			lblZipCodeValueLData.grabExcessVerticalSpace = false;
			lblZipCodeValue.setLayoutData(lblZipCodeValueLData);
			lblZipCodeValue.setText("<Zip Code>");
	
			GridData lblStartMonthLData = new GridData();
			lblStartMonthLData.verticalAlignment = GridData.CENTER;
			lblStartMonthLData.horizontalAlignment = GridData.END;
			lblStartMonthLData.widthHint = -1;
			lblStartMonthLData.heightHint = -1;
			lblStartMonthLData.horizontalIndent = 0;
			lblStartMonthLData.horizontalSpan = 1;
			lblStartMonthLData.verticalSpan = 1;
			lblStartMonthLData.grabExcessHorizontalSpace = false;
			lblStartMonthLData.grabExcessVerticalSpace = false;
			lblStartMonth.setLayoutData(lblStartMonthLData);
			lblStartMonth.setText("Start Month:");
	
			GridData lblStartMonthValueLData = new GridData();
			lblStartMonthValueLData.verticalAlignment = GridData.CENTER;
			lblStartMonthValueLData.horizontalAlignment = GridData.FILL;
			lblStartMonthValueLData.widthHint = -1;
			lblStartMonthValueLData.heightHint = -1;
			lblStartMonthValueLData.horizontalIndent = 0;
			lblStartMonthValueLData.horizontalSpan = 1;
			lblStartMonthValueLData.verticalSpan = 1;
			lblStartMonthValueLData.grabExcessHorizontalSpace = false;
			lblStartMonthValueLData.grabExcessVerticalSpace = false;
			lblStartMonthValue.setLayoutData(lblStartMonthValueLData);
			lblStartMonthValue.setText("<Start Month>");
	
			GridData lblInstallDateLData = new GridData();
			lblInstallDateLData.verticalAlignment = GridData.CENTER;
			lblInstallDateLData.horizontalAlignment = GridData.END;
			lblInstallDateLData.widthHint = -1;
			lblInstallDateLData.heightHint = -1;
			lblInstallDateLData.horizontalIndent = 0;
			lblInstallDateLData.horizontalSpan = 1;
			lblInstallDateLData.verticalSpan = 1;
			lblInstallDateLData.grabExcessHorizontalSpace = false;
			lblInstallDateLData.grabExcessVerticalSpace = false;
			lblInstallDate.setLayoutData(lblInstallDateLData);
			lblInstallDate.setText("Install Date:");
	
			GridData lblInstallDateValueLData = new GridData();
			lblInstallDateValueLData.verticalAlignment = GridData.CENTER;
			lblInstallDateValueLData.horizontalAlignment = GridData.FILL;
			lblInstallDateValueLData.widthHint = -1;
			lblInstallDateValueLData.heightHint = -1;
			lblInstallDateValueLData.horizontalIndent = 0;
			lblInstallDateValueLData.horizontalSpan = 3;
			lblInstallDateValueLData.verticalSpan = 1;
			lblInstallDateValueLData.grabExcessHorizontalSpace = false;
			lblInstallDateValueLData.grabExcessVerticalSpace = false;
			lblInstallDateValue.setLayoutData(lblInstallDateValueLData);
			lblInstallDateValue.setText("<Install Date>");
	
			GridData lblRentalChargeLData = new GridData();
			lblRentalChargeLData.verticalAlignment = GridData.CENTER;
			lblRentalChargeLData.horizontalAlignment = GridData.END;
			lblRentalChargeLData.widthHint = -1;
			lblRentalChargeLData.heightHint = -1;
			lblRentalChargeLData.horizontalIndent = 0;
			lblRentalChargeLData.horizontalSpan = 1;
			lblRentalChargeLData.verticalSpan = 1;
			lblRentalChargeLData.grabExcessHorizontalSpace = false;
			lblRentalChargeLData.grabExcessVerticalSpace = false;
			lblRentalCharge.setLayoutData(lblRentalChargeLData);
			lblRentalCharge.setText("Rental Charge:");
	
			GridData lblRentalChargeValueLData = new GridData();
			lblRentalChargeValueLData.verticalAlignment = GridData.CENTER;
			lblRentalChargeValueLData.horizontalAlignment = GridData.FILL;
			lblRentalChargeValueLData.widthHint = -1;
			lblRentalChargeValueLData.heightHint = -1;
			lblRentalChargeValueLData.horizontalIndent = 0;
			lblRentalChargeValueLData.horizontalSpan = 1;
			lblRentalChargeValueLData.verticalSpan = 1;
			lblRentalChargeValueLData.grabExcessHorizontalSpace = false;
			lblRentalChargeValueLData.grabExcessVerticalSpace = false;
			lblRentalChargeValue.setLayoutData(lblRentalChargeValueLData);
			lblRentalChargeValue.setText("<Rental Charge>");
            {
                lblCredit = new Label(grpCustInfo, SWT.NONE);
                lblCredit.setText("Credit:");
                GridData lblCreditLData = new GridData();
                lblCreditLData.horizontalAlignment = GridData.END;
                lblCredit.setLayoutData(lblCreditLData);
            }
            {
                lblCreditValue = new Label(grpCustInfo, SWT.NONE);
                lblCreditValue.setText("<Credit Bal>");
            }
			GridLayout grpCustInfoLayout = new GridLayout(6, true);
			grpCustInfo.setLayout(grpCustInfoLayout);
            {
                cmpInvoicePendingService = new Composite(this, SWT.NONE);
                GridLayout composite1Layout = new GridLayout();
                composite1Layout.makeColumnsEqualWidth = true;
                GridData composite1LData = new GridData();
                cmpInvoicePendingService.setLayout(composite1Layout);
                composite1LData.verticalAlignment = GridData.BEGINNING;
                composite1LData.horizontalSpan = 2;
                cmpInvoicePendingService.setLayoutData(composite1LData);
                {
                    grpPendingServices = new Group(cmpInvoicePendingService, SWT.NONE);
                    GridLayout grpPendingServicesLayout = new GridLayout();
                    GridData grpPendingServicesLData = new GridData();
                    grpPendingServicesLData.widthHint = 310;
                    grpPendingServicesLData.heightHint = 158;
                    grpPendingServices.setLayoutData(grpPendingServicesLData);
                    grpPendingServicesLayout.numColumns = 3;
                    grpPendingServices.setLayout(grpPendingServicesLayout);
                    grpPendingServices.setText("Pending Services");
                    {
                        tblPendingService = new Table(grpPendingServices, SWT.FULL_SELECTION);
                        tblPendingService.setLinesVisible(true);
                        tblPendingService.setItemCount(5);
                        tblPendingService.setHeaderVisible(true);
                        GridData tblPendingServiceLData = new GridData();
                        tblPendingServiceLData.heightHint = 89;
                        tblPendingServiceLData.horizontalSpan = 3;
                        tblPendingServiceLData.horizontalAlignment = GridData.FILL;
                        tblPendingServiceLData.grabExcessHorizontalSpace = true;
                        tblPendingService.setLayoutData(tblPendingServiceLData);
                        {
                            tblClmPendServId = new TableColumn(
                                tblPendingService,
                                SWT.NONE);
                            tblClmPendServId.setText("Id");
                            tblClmPendServId.setWidth(33);
                        }
                        {
                            tblClmDescription = new TableColumn(
                                tblPendingService,
                                SWT.NONE);
                            tblClmDescription.setText("Description");
                            tblClmDescription.setWidth(107);
                        }
                        {
                            tblClmChargeAmt = new TableColumn(
                                tblPendingService,
                                SWT.NONE);
                            tblClmChargeAmt.setText("Charge Amt");
                            tblClmChargeAmt.setWidth(145);
                        }
                    }
                    {
                        btnCreatePendingServ = new Button(grpPendingServices, SWT.PUSH
                            | SWT.CENTER);
                        btnCreatePendingServ.setText("Create");
                        btnCreatePendingServ
                            .addSelectionListener(new SelectionAdapter() {
                            public void widgetSelected(SelectionEvent evt) {
                        		try {
                        		    btnCreatePendingServWidgetSelected(evt);
                        		} catch (Exception e) {
                        			e.printStackTrace();
                        		}
                            }
                            });
                    }
                    {
                        btnEditPendingServ = new Button(grpPendingServices, SWT.PUSH
                            | SWT.CENTER);
                        btnEditPendingServ.setText("Edit");
                        btnEditPendingServ.addSelectionListener(new SelectionAdapter() {
                            public void widgetSelected(SelectionEvent evt) {
                                // make sure that there is one item that is highlighted
                                if (tblPendingService.getSelectionCount() != 1) {
                                    //@TODO throw up a message box indicating that they must select a pending service to edit
                                } else {
                                    PendingService pendingServ = getSelectedPendingService();
                                    logger.info("Pending Service: " + pendingServ.toString());
                                    
                                    // Open the pending Service dialog to be edited
                            		try {
                            			Display display = Display.getDefault();
                            			Shell shell = new Shell(display);
                            			PendingServiceDialog psDialog = new PendingServiceDialog(shell, SWT.NULL);
                            			psDialog.setPendServ(pendingServ);
                            			psDialog.open();
                            			pendingServ = psDialog.getPendServ();
                            			if (pendingServ == null) {
                            			    return;
                            			} else {
                                			pendingServ.setCustomerId(currentCustomer.getId().intValue());
                                			logger.info("Pending Service returned: " + pendingServ.toString());
                                			invService.updatePendingService(pendingServ);
                                			getCustomerInvoiceSummary();
                            			}
                            		} catch (Exception e) {
                            			e.printStackTrace();
                            		}
                                    
                                }
                            }
                        });
                    }
                    {
                        btnDeletePendingServ = new Button(grpPendingServices, SWT.PUSH
                            | SWT.CENTER);
                        btnDeletePendingServ.setText("Delete");
                        btnDeletePendingServ
                            .addSelectionListener(new SelectionAdapter() {
                            public void widgetSelected(SelectionEvent evt) {
                                if (tblPendingService.getSelectionCount() != 1) {
                                    //@TODO will send a message saying that one item musts be selected
                                } else {
                                    PendingService pendServ = getSelectedPendingService();
                                    invService.deletePendingService(pendServ);
                                    getCustomerInvoiceSummary();
                                }
                            }
                            });
                    }
                }
                {
                    grpInvoiceList = new Group(
                        cmpInvoicePendingService,
                        SWT.NONE);
                    GridLayout grpInvoiceListLayout = new GridLayout();
                    grpInvoiceListLayout.numColumns = 2;
                    GridData grpInvoiceListLData = new GridData();
                    grpInvoiceListLData.verticalAlignment = GridData.BEGINNING;
                    grpInvoiceListLData.widthHint = 397;
                    grpInvoiceListLData.heightHint = 144;
                    grpInvoiceList.setLayoutData(grpInvoiceListLData);
                    grpInvoiceList.setText("Invoice List");
                    grpInvoiceList.setLayout(grpInvoiceListLayout);
                    {
                        tblInvoiceList = new Table(
                            grpInvoiceList,
                            SWT.FULL_SELECTION);
                        GridData tblInvoiceListLData = new GridData();
                        tblInvoiceListLData.horizontalAlignment = GridData.FILL;
                        tblInvoiceListLData.heightHint = 89;
                        tblInvoiceListLData.horizontalSpan = 2;
                        tblInvoiceList.setLayoutData(tblInvoiceListLData);
                        tblInvoiceList.setHeaderVisible(true);
                        tblInvoiceList.setItemCount(5);
                        tblInvoiceList.setLinesVisible(true);
                        {
                            tbclmInvoiceNbr = new TableColumn(
                                tblInvoiceList,
                                SWT.NONE);
                            tbclmInvoiceNbr.setText("Inv. #");
                            tbclmInvoiceNbr.setWidth(50);
                        }
                        {
                            tblclmCreationDate = new TableColumn(
                                tblInvoiceList,
                                SWT.NONE);
                            tblclmCreationDate.setText("Date Created");
                            tblclmCreationDate.setWidth(90);
                        }
                        {
                            tblclmItemCounts = new TableColumn(
                                tblInvoiceList,
                                SWT.NONE);
                            tblclmItemCounts.setText("# of LineItems");
                            tblclmItemCounts.setWidth(90);
                        }
                        {
                            tblclmDueDate = new TableColumn(
                                tblInvoiceList,
                                SWT.NONE);
                            tblclmDueDate.setText("Due Date");
                            tblclmDueDate.setWidth(60);
                        }
                        {
                            tblclmTotalInvoiceAmt = new TableColumn(
                                tblInvoiceList,
                                SWT.NONE);
                            tblclmTotalInvoiceAmt.setText("Invoice Amt.");
                            tblclmTotalInvoiceAmt.setWidth(75);
                        }
                    }
                    {
                        btnEditInvoice = new Button(grpInvoiceList, SWT.PUSH
                            | SWT.CENTER);
                        btnEditInvoice.setText("Edit Invoice");
                        btnEditInvoice.addSelectionListener(new SelectionAdapter() {
                            public void widgetSelected(SelectionEvent evt) {
                                logger
                                    .info("We will edit the selected invoice");
                                if (tblInvoiceList.getSelectionCount() == 1) {
                                    CustomerInvoice custInv = getSelectedInvoice();
                                    // we will allow the detail to be seen, but we will not allow the
                                    // invoice to be editable if the invoice is already closed
                                    boolean isEditable = true;
                                    if (!custInv.isOpen()) {
                                        isEditable = false;
                                    }
                                    Customer cust = custService
                                        .getCustomerById(custInv
                                            .getCustomerId());
                                    Display display = Display.getDefault();
                                    Shell shell = new Shell(
                                        display,
                                        SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
                                    InvoiceDetailUI invoiceDetailUI = new InvoiceDetailUI(
                                        shell,
                                        SWT.NONE, isEditable);
                                    invoiceDetailUI.setInvoice(custInv);
                                    invoiceDetailUI.setCustomer(cust);
                                    invoiceDetailUI.open();
                                    // perform the search so that all of the information is updated properly
                                    getCustomerInvoiceSummary();
                                } else {
                                    // this should be a message box.
                                    logger
                                        .info("There was not an invoice selected");
                                    return;
                                }
                            }

                        });
                    }
                    {
                        btnPrintInvoice = new Button(grpInvoiceList, SWT.PUSH
                            | SWT.CENTER);
                        btnPrintInvoice.setText("Print Invoice");
                        btnPrintInvoice
                            .addSelectionListener(new SelectionAdapter() {
                            public void widgetSelected(SelectionEvent evt) {
                                System.out
                                    .println("btnPrintInvoice.widgetSelected, event="
                                        + evt);
                                if (tblInvoiceList.getSelectionCount() == 1) {
                                    CustomerInvoice custInv = getSelectedInvoice();
                                    invService.printInvoice(custInv);
                                }

                            
                            }
                            });
                    }
                    grpInvoiceList.layout();
                }
            }
			grpCustInfoLayout.marginWidth = 5;
			grpCustInfoLayout.marginHeight = 5;
			grpCustInfoLayout.numColumns = 6;
			grpCustInfoLayout.makeColumnsEqualWidth = false;
			grpCustInfoLayout.horizontalSpacing = 5;
			grpCustInfoLayout.verticalSpacing = 5;
			grpCustInfo.layout();

            {
                cmpApplyPayment = new Composite(this, SWT.NONE);
                GridLayout cmpApplyPaymentLayout = new GridLayout();
                cmpApplyPaymentLayout.numColumns = 5;
                GridData cmpApplyPaymentLData = new GridData();
                cmpApplyPaymentLData.horizontalAlignment = GridData.FILL;
                cmpApplyPaymentLData.heightHint = 51;
                cmpApplyPaymentLData.horizontalSpan = 2;
                cmpApplyPayment.setLayoutData(cmpApplyPaymentLData);
                cmpApplyPayment.setLayout(cmpApplyPaymentLayout);
                {
                    lblApplyPayment = new Label(cmpApplyPayment, SWT.NONE);
                    GridData lblApplyPaymentLData = new GridData();
                    lblApplyPayment.setLayoutData(lblApplyPaymentLData);
                    lblApplyPayment.setText("*Apply Payment");
                }
                {
                    txtPaymentAmt = new Text(cmpApplyPayment, SWT.BORDER);
                    GridData txtPaymentAmtLData = new GridData();
                    txtPaymentAmt.setLayoutData(txtPaymentAmtLData);
                }
                {
                    lblPaymentType = new Label(cmpApplyPayment, SWT.NONE);
                    lblPaymentType.setText("Payment Type:");
                }
                {
                    cmbPaymentType = new Combo(cmpApplyPayment, SWT.NONE);
                }
                {
                    btnApply = new Button(cmpApplyPayment, SWT.PUSH
                        | SWT.CENTER);
                    GridData btnApplyLData = new GridData();
                    btnApply.setLayoutData(btnApplyLData);
                    btnApply.setText("Apply");
                    btnApply.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent evt) {
                            btnApplyWidgetSelected(evt);
                        }
                    });
                }
                cmpApplyPayment.layout();
            }

			GridLayout thisLayout = new GridLayout();
			this.setLayout(thisLayout);
			thisLayout.numColumns = 2;
			this.layout();
	
			postInitGUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
     * @param evt
	 * @throws BWSGenericException
     */
    protected void btnCreatePendingServWidgetSelected(SelectionEvent evt) throws BWSGenericException {
	    if (currentCustomer == null) {
            MessageBox messageBox = new MessageBox(this.getShell(),
                    SWT.ICON_ERROR | SWT.OK);
            messageBox.setText("Invalid Selection");
            messageBox.setMessage("Please specify a customer before attempting to create a pending service");
            messageBox.open();
            return;
	    } 
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		PendingServiceDialog psDialog = new PendingServiceDialog(shell, SWT.NULL);
		psDialog.setPendServ(null);
		psDialog.open();
		PendingService pendingServ = psDialog.getPendServ();
		if (pendingServ == null) {
		    return;
		} else {
			pendingServ.setCustomerId(currentCustomer.getId().intValue());
			logger.info("Pending Service returned: " + pendingServ.toString());
			invService.createPendingService(pendingServ);
			getCustomerInvoiceSummary();
		}
    }

    /**
	 * Use this method in order to retrieve the selected TableItem
	 * and transform it into an InvoiceLineItem
	 * 
     * @return InvoiceLineItem - this is the invoice Line item that was selected
     */
    protected CustomerInvoice getSelectedInvoice() {
        // @TODO complete this method
        CustomerInvoice custInv = new CustomerInvoice();
        TableItem selectedTI = tblInvoiceList.getSelection()[0];
        custInv = invService.getCustomerInvoice(new Integer(selectedTI.getText(0)));
        logger.info("Customer Invoice: " + custInv.toString());
       return custInv;
    }

    /** Add your pre-init code in here 	*/
	public void preInitGUI(){
	}

	/** Add your post-init code in here 	*/
	public void postInitGUI(){
	}

	/** Auto-generated main method */
	public static void main(String[] args){
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
			CustomerInvoiceUI inst = new CustomerInvoiceUI(shell, SWT.NULL);
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
	/**
	 * This is the logic that will fire when the button is clicked
	 */
	protected void btnSearchWidgetSelected(SelectionEvent evt){
	    getCustomerInvoiceSummary();
	        
	}

    /**
     * 
     */
    private void getCustomerInvoiceSummary() {
        Customer cust = null;
	    if (txtCustId.getText() != null && !txtCustId.getText().equalsIgnoreCase("")) {
	        logger.info("We will look for the customer invoice information by customer id " + txtCustId.getText());
	        cust = new Customer(new Integer(txtCustId.getText()));
	    } else {
	        logger.info("We will look for the customer based on the combo box");
	        cust = new Customer(parseCustIdFromCmbValue(cmbCustomerName.getText()));
	    }
	    // set the private Customer variable to the customer that is currently selected.
	    currentCustomer = cust;
       try {
           getCustomerAndLoadScreen(cust);
                
       	} catch (BWSGenericException e) {
                MessageBox messageBox = new MessageBox(this.getShell(),
                        SWT.ICON_ERROR | SWT.OK);
                messageBox.setText("Search Error!!");
                messageBox.setMessage("Could not find the customer information. Error Message: " + e.getMessage());
                messageBox.open();
        }
    }

    /**
     * @param cust
     * @throws BWSGenericException
     */
    private void getCustomerAndLoadScreen(Customer cust) throws BWSGenericException {
        Customer customerResult = null;
        if (custService.getMatchingCustomers(cust) != null) {
            customerResult = (Customer)custService.getMatchingCustomers(cust).get(0);
        } else {
            MessageBox messageBox = new MessageBox(this.getShell(),
                    SWT.ICON_ERROR | SWT.OK);
            messageBox.setText("Customer not found.");
            messageBox.setMessage("Customer with id " + cust.getId() + " could not be found.  Please check the customer id.");
            messageBox.open();
            return;
        }
        // fill in the customer information for the screen.
        populateCustomerInfo(customerResult);
        // get the invoice information for this customer
        List invoiceList = invService.getInvoicesByCustomer(customerResult.getId());
        if (invoiceList != null && invoiceList.size() > 0) {
            initializeInvoiceTable(invoiceList);
        } else {
            tblInvoiceList.removeAll();
        }
        
        List pendingServiceList = invService.getPendingServicesByCustomerId(customerResult.getId().intValue());
        if (pendingServiceList != null && pendingServiceList.size() > 0) {
            initializePendingServiceTable(pendingServiceList);
        } else {
            tblPendingService.removeAll();
        }
        // set the payment amount to nothing when refreshing the screen.
        txtPaymentAmt.setText("");
    }

    /**
     * @param pendingServiceList
     */
    private void initializePendingServiceTable(List pendingServiceList) {
        // first clear out the rows that are currently in the table
        tblPendingService.removeAll();
        for (Iterator iter = pendingServiceList.iterator(); iter.hasNext();) {
            TableItem tblItem = new TableItem(tblPendingService, SWT.NONE);
            PendingService ps = (PendingService) iter.next();
            // description, charge amount
            tblItem.setText(new String[] {
                    ps.getId() + "",
                    ps.getDesc(),
                    df.format(ps.getChargeAmt())});
        }
    }

    /**
     * This method will be used to initialize the customer table 
     */
    private void initializeInvoiceTable(List invList) {
        // first clear out the rows that are currently in the table
        tblInvoiceList.removeAll();
        Collections.sort(invList, new CustInvoiceDueDateComparator());
        for (Iterator iter = invList.iterator(); iter.hasNext();) {
            TableItem tblItem = new TableItem(tblInvoiceList, SWT.NONE);
            CustomerInvoice inv = (CustomerInvoice) iter.next();
            
            // inv_id, date created, lineItemCount, DueDate, InvAmt
            tblItem.setText(new String[] {
                    inv.getId().toString(),
                    sdf.format(inv.getCreateDate()),
                    String.valueOf(inv.getInvoiceLineItemList().size()),
                    sdf.format(inv.getDueDate()),
                    df.format(inv.getInvoiceBalance())});
            // if the invoice is in an open status, we will make the
            // text white and the background color red.
            if (inv.isOpen()) {
                tblItem.setBackground(new Color(this.getDisplay(), 255, 0, 0));
                tblItem.setForeground(new Color(this.getDisplay(), 0,0,0));
            }
        }
        
        
    }

    /**
     * This method will take a customer object and populate all of the appropriate 
     * customer fields
     * @param customerResult
     */
    private void populateCustomerInfo(Customer customerResult) {
        cmbCustomerName.setText(getCustomerDropDownValue(customerResult));
        lblNameValue.setText(customerResult.getId() + " - " + customerResult.getFormalName());
        lblAddress1Value.setText(customerResult.getAddress1());
        lblCityValue.setText(customerResult.getCity());
        lblStateValue.setText(customerResult.getState());
        lblZipCodeValue.setText(customerResult.getZipCode());
        lblStartMonthValue.setText(customerResult.getBillStartMonth());
        lblInstallDateValue.setText(sdf.format(customerResult.getInstallationDate()));
        lblRentalChargeValue.setText(df.format(customerResult.getRentalCharge()));
        lblCreditValue.setText(df.format(customerResult.getCreditBalance()));
    }

	/** Auto-generated event handler method */
	protected void btnApplyWidgetSelected(SelectionEvent evt){
	    Double paymentAmt = null;
	    if (txtPaymentAmt.getText() != null && !txtPaymentAmt.getText().equalsIgnoreCase("")) {
	        try {
                paymentAmt = new Double(txtPaymentAmt.getText());
            } catch (Exception e) {
                logger.error("Could not transform the payment amount " + txtPaymentAmt.getText() + " to a double value", e);
                return;
            }
	    } else {
	        return;
	    }
	    MessageBox msgBox = new MessageBox(this.getShell(), SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
	    msgBox.setText("Confirmation");
	    msgBox.setMessage("Are you sure you want to apply " + df.format(paymentAmt) + " to " + lblNameValue.getText() + " account?");
	    int response = msgBox.open();
	    if (response == SWT.OK) {
            logger.info("we will apply the payment of " + paymentAmt + " to the customer account");
            Integer custId = parseCustIdFromCmbValue(lblNameValue.getText());
            try {
                Payment payment = new Payment();
                payment.setPaymentAmt(paymentAmt);
                // determine the payment type from the cmbPayment was either regular or forgiving.
                // default will be regular
                payment.setPaymentType(Messages.getString("paymentTypeCode.regular"));
                if (cmbPaymentType.getText().equalsIgnoreCase(Messages.getString("paymentTypeDesc.regular"))) {
                    payment.setPaymentType(Messages.getString("paymentTypeCode.regular"));
                } else if (cmbPaymentType.getText().equalsIgnoreCase(Messages.getString("paymentTypeDesc.forgiving")))  {
                    payment.setPaymentType(Messages.getString("paymentTypeCode.forgiving"));
                }
                invService.applyPaymentByCustIdAndPaymentAmt(custId, payment);
            } catch (OpenInvoicesNotAvailableException e1) {
                MessageBox messageBox = new MessageBox(this.getShell(),
                        SWT.ICON_ERROR | SWT.OK);
                messageBox.setText("Applying Payments Error");
                messageBox.setMessage("There were not any open invoices to apply to the customer");
                messageBox.open();
            }
            try {
                getCustomerAndLoadScreen(new Customer(custId));
            } catch (BWSGenericException e) {
                MessageBox messageBox = new MessageBox(this.getShell(),
                        SWT.ICON_ERROR | SWT.OK);
                messageBox.setText("Search Error!!");
                messageBox.setMessage("Could not find the customer information. Error Message: " + e.getMessage());
                messageBox.open();
            }
        }
	}

    /**
     * This is a convenience method that will be used to parse the 
     * customer id when the String is in the form of
     * <cust_id> - <custFormalName>
     * @return
     */
    private Integer parseCustIdFromCmbValue(String value) {
        StringTokenizer strToken = new StringTokenizer(value, "-");
        Integer custId = null;
        if (strToken.countTokens() == 2) {
            custId = new Integer(strToken.nextToken().trim());
        }
        logger.info("Customer id: " + custId);
        return custId;
    }

	/** Auto-generated event handler method */
	protected void btnGenerateInvoicesWidgetSelected(SelectionEvent evt){
        InputDialog inputDialog = new InputDialog(this.getShell(),
                SWT.APPLICATION_MODAL);
        inputDialog.setText("Generate Invoices");
        inputDialog
                .setQuestionText("Please enter the date, MM/DD/YYYY, to use to generate the invoices");
        InputDialogData idd = inputDialog.open();
        if (logger.isDebugEnabled()) {
            logger.debug("idd User Input " + idd.getUserInput());
            logger.debug("idd button " + idd.isButtonResponse());
        }
        if (idd.isButtonResponse()) {
            Date generateInvoiceDate = sdf.parse(idd.getUserInput().trim(),
                        new ParsePosition(0));
            //@TODO using the same logic, we need to trap for null in the customer UI for close date
            if (generateInvoiceDate == null) {
                MessageBox messageBox = new MessageBox(this.getShell(),
                        SWT.ICON_ERROR | SWT.OK);
                messageBox.setText("Generate Invoice Error!!");
                messageBox.setMessage("Please be sure to enter the date in the following format: MM/DD/YYYYY");
                messageBox.open();
            }
            // using the date entered, generate all of the invoices for all of the customers
            // who should be invoiced
            invService.generateInvoicesByRunDate(generateInvoiceDate);
            
            // clear the screen as if they were just coming into the screen
            clearScreenValues();
        }
	    
	}

    /**
     * Will clear out any of the screen values
     */
    private void clearScreenValues() {
        txtCustId.setText("");
        cmbCustomerName.setText("");
        lblNameValue.setText("<Name>");
        lblAddress1Value.setText("<Address1>");
        lblCityValue.setText("<City>");
        lblStateValue.setText("<State>");
        lblZipCodeValue.setText("<Zip Code>");
        lblStartMonthValue.setText("<Start Month>");
        lblInstallDateValue.setText("<Install Date>");
        lblRentalChargeValue.setText("<Rental Charge>");
        tblInvoiceList.removeAll();
        txtPaymentAmt.setText("");
    }

    /**
     * Will return the selected pending service
     * @return
     */
    protected PendingService getSelectedPendingService() {
        PendingService pendingServ = new PendingService();
        TableItem selectedTI = tblPendingService.getSelection()[0];
        pendingServ.setId(Integer.parseInt(selectedTI.getText(0)));
        pendingServ.setDesc(selectedTI.getText(1));
        pendingServ.setChargeAmt(Double.parseDouble(selectedTI.getText(2)));
        return pendingServ;
    }
}
