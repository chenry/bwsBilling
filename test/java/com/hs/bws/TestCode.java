/**
 * File Name: TestCode.java
 * Date: Aug 20, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws;

import java.text.DecimalFormat;

import junit.framework.TestCase;

import org.apache.log4j.Logger;


public class TestCode extends TestCase {
    Logger logger = Logger.getLogger(this.getClass());
    public void testMod() {
        // for calculating the quarterly pay periods
        int monthId = 8;
        int secondQtrMonth = (monthId + 3) % 12;
        int thirdQtrMonth = (secondQtrMonth + 3) % 12;
        int fourthQtrMonth = (thirdQtrMonth + 3) % 12;
        assertEquals(11, secondQtrMonth);
        assertEquals(2, thirdQtrMonth);
        assertEquals(5, fourthQtrMonth);
        
        // for calcuating the semi-annual pay periods
        int secondSAMonth = (monthId + 6) % 12;
        
        assertEquals(2, secondSAMonth);
        
    }
    
    public void testDecimalFormat() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        Double d = new Double(0.00);
        System.out.println(df.format(d));
        d = new Double(30);
        System.out.println(df.format(d));
    }
    
    public void testLogging() {
        for (int i = 0; i < 20; i++) {
            logger.info("Hello world " + i);
        }
    }
    
}
