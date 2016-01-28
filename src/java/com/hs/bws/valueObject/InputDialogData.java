/**
 * File Name: InputDialogData.java
 * Date: Aug 28, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.valueObject;


public class InputDialogData {
    private String userInput;
    private boolean buttonResponse;
    /**
     * 
     */
    public InputDialogData() {
        super();
    }

    /**
     * @return Returns the buttonResponse.
     */
    public boolean isButtonResponse() {
        return buttonResponse;
    }
    /**
     * @param buttonResponse The buttonResponse to set.
     */
    public void setButtonResponse(boolean buttonResponse) {
        this.buttonResponse = buttonResponse;
    }
    /**
     * @return Returns the userInput.
     */
    public String getUserInput() {
        return userInput;
    }
    /**
     * @param userInput The userInput to set.
     */
    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }
}
