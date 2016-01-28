package com.hs.bws.ui;

import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hs.bws.valueObject.PendingService;
public class PendingServiceDialog extends org.eclipse.swt.widgets.Dialog {
    Logger logger = Logger.getLogger(PendingServiceDialog.class);
    private DecimalFormat df = new DecimalFormat("##,##0.00");
	private Shell dialogShell;
	private Label lblChargeAmt;
	private Button btnOk;
	private Button btnCancel;
	private Text txtChargeAmtValue;
	private Text txtDescriptionValue;
	private Label lblDescription;
	private Label lblHeader;
	private PendingService pendServ;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Dialog inside a new Shell.
	*/
	public static void main(String[] args) {
		try {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			PendingServiceDialog inst = new PendingServiceDialog(shell, SWT.NULL);
			inst.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PendingServiceDialog(Shell parent, int style) {
		super(parent, style);
	}

	public void open() {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			GridLayout dialogShellLayout = new GridLayout();
			dialogShell.setLayout(dialogShellLayout);
			dialogShellLayout.numColumns = 2;
			dialogShell.layout();
			dialogShell.pack();
			dialogShell.setSize(219, 148);
            {
                lblHeader = new Label(dialogShell, SWT.NONE);
                lblHeader.setText("Pending Service");
                GridData lblHeaderLData = new GridData();
                lblHeaderLData.horizontalAlignment = GridData.CENTER;
                lblHeaderLData.horizontalSpan = 2;
                lblHeader.setLayoutData(lblHeaderLData);
            }
            {
                lblDescription = new Label(dialogShell, SWT.NONE);
                lblDescription.setText("Description:");
            }
            {
                txtDescriptionValue = new Text(dialogShell, SWT.BORDER);
            }
            {
                lblChargeAmt = new Label(dialogShell, SWT.NONE);
                lblChargeAmt.setText("Charge Amount:");
            }
            {
                txtChargeAmtValue = new Text(dialogShell, SWT.BORDER);
            }
            {
                btnOk = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
                btnOk.setText("OK");
                GridData btnOkLData = new GridData();
                btnOk.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent evt) {
                        if ((txtDescriptionValue.getText() != null && txtDescriptionValue.getText().length() > 0)
                                && (txtChargeAmtValue.getText() != null && txtChargeAmtValue.getText().length() > 0)) {
                            pendServ = new PendingService();
                            try {
                                pendServ.setDesc(txtDescriptionValue.getText());
                                pendServ.setChargeAmt(Double
                                        .parseDouble(txtChargeAmtValue
                                                .getText()));
                            } catch (Exception e) {
                                logger.error("There was a problem when trying to transform the values on the pendingServiceDialog into a PendingService value object", e);
                                pendServ = null;
                            }
                        } else {
                            pendServ = null;
                        }
                        
                        dialogShell.close();
                    }
                });
                btnOkLData.horizontalAlignment = GridData.END;
                btnOkLData.widthHint = 26;
                btnOkLData.heightHint = 23;
                btnOk.setLayoutData(btnOkLData);
            }
            {
                btnCancel = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
                btnCancel.setText("Cancel");
    			{
                    btnCancel.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent evt) {
                            pendServ = null;
                            dialogShell.close();
                        }
                    });
    			}
                
            }
			initFields();
			dialogShell.open();
			Display display = dialogShell.getDisplay();
			while (!dialogShell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
     * 
     */
    private void initFields() {
        if (this.pendServ != null) {
            txtDescriptionValue.setText(pendServ.getDesc());
            txtChargeAmtValue.setText(df.format(pendServ.getChargeAmt()));
        }
    }

    /**
     * @return Returns the pendServ.
     */
    public PendingService getPendServ() {
        return pendServ;
    }
    /**
     * @param pendServ The pendServ to set.
     */
    public void setPendServ(PendingService pendServ) {
        this.pendServ = pendServ;
    }
}
