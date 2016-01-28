package com.hs.bws.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hs.bws.valueObject.InputDialogData;

/**
* This code was generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a
* for-profit company or business) then you should purchase
* a license - please visit www.cloudgarden.com for details.
*/
public class InputDialog extends org.eclipse.swt.widgets.Dialog {
	private Button btnCancel;
	private Button btnOk;
	private Text txtUserInput;
	private Label lblQuestion;
	private Shell dialogShell;
	private InputDialogData idd;
	private String questionText = "Please enter the appropriate information below:";

	public InputDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	public void setQuestionText(String message) {
	    questionText = message;
	}

	/**
	* Opens the Dialog Shell.
	* Auto-generated code - any changes you make will disappear.
	*/
	public InputDialogData open(){
		try {
			preInitGUI();
			idd = new InputDialogData();
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			dialogShell.setText(getText());
			lblQuestion = new Label(dialogShell,SWT.NULL);
			txtUserInput = new Text(dialogShell, SWT.BORDER);
			btnOk = new Button(dialogShell,SWT.PUSH| SWT.CENTER);
			btnCancel = new Button(dialogShell,SWT.PUSH| SWT.CENTER);
	
			dialogShell.setSize(new org.eclipse.swt.graphics.Point(304,114));
	
			GridData lblQuestionLData = new GridData();
			lblQuestionLData.verticalAlignment = GridData.CENTER;
			lblQuestionLData.horizontalAlignment = GridData.BEGINNING;
			lblQuestionLData.widthHint = -1;
			lblQuestionLData.heightHint = -1;
			lblQuestionLData.horizontalIndent = 0;
			lblQuestionLData.horizontalSpan = 2;
			lblQuestionLData.verticalSpan = 1;
			lblQuestionLData.grabExcessHorizontalSpace = false;
			lblQuestionLData.grabExcessVerticalSpace = false;
			lblQuestion.setLayoutData(lblQuestionLData);
			lblQuestion.setText(questionText);
	
			GridData txtUserInputLData = new GridData();
			txtUserInputLData.verticalAlignment = GridData.CENTER;
			txtUserInputLData.horizontalAlignment = GridData.FILL;
			txtUserInputLData.widthHint = -1;
			txtUserInputLData.heightHint = -1;
			txtUserInputLData.horizontalIndent = 0;
			txtUserInputLData.horizontalSpan = 2;
			txtUserInputLData.verticalSpan = 1;
			txtUserInputLData.grabExcessHorizontalSpace = false;
			txtUserInputLData.grabExcessVerticalSpace = false;
			txtUserInput.setLayoutData(txtUserInputLData);
	
			GridData btnOkLData = new GridData();
			btnOkLData.verticalAlignment = GridData.CENTER;
			btnOkLData.horizontalAlignment = GridData.END;
			btnOkLData.widthHint = 26;
			btnOkLData.heightHint = 23;
			btnOkLData.horizontalIndent = 0;
			btnOkLData.horizontalSpan = 1;
			btnOkLData.verticalSpan = 1;
			btnOkLData.grabExcessHorizontalSpace = false;
			btnOkLData.grabExcessVerticalSpace = false;
			btnOk.setLayoutData(btnOkLData);
			btnOk.setText("OK");
			btnOk.setSize(new org.eclipse.swt.graphics.Point(26,23));
	
			GridData btnCancelLData = new GridData();
			btnCancelLData.widthHint = 44;
			btnCancelLData.heightHint = 23;
			btnCancel.setLayoutData(btnCancelLData);
			btnCancel.setText("Cancel");
			btnCancel.setSize(new org.eclipse.swt.graphics.Point(44,23));
			GridLayout dialogShellLayout = new GridLayout(2, true);
			dialogShell.setLayout(dialogShellLayout);
			dialogShellLayout.marginWidth = 30;
			dialogShellLayout.marginHeight = 5;
			dialogShellLayout.numColumns = 2;
			dialogShellLayout.makeColumnsEqualWidth = true;
			dialogShellLayout.horizontalSpacing = 5;
			dialogShellLayout.verticalSpacing = 5;
			dialogShell.layout();
			Rectangle bounds = dialogShell.computeTrim(0, 0, 304,114);
			dialogShell.setSize(bounds.width, bounds.height);
			postInitGUI();
			dialogShell.open();
			Listener listener = new Listener () {
			    public void handleEvent (Event event) {
			    idd.setButtonResponse(event.widget == btnOk);
			    idd.setUserInput(txtUserInput.getText());
			    dialogShell.close(); 
			    }
			};
			btnOk.addListener(SWT.Selection, listener);
			btnCancel.addListener(SWT.Selection, listener);
			Display display = dialogShell.getDisplay();
			while (!dialogShell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return idd;
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
			InputDialog inst = new InputDialog(shell, SWT.NULL);
			inst.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
