/**
 * File Name: DatabaseUtilTest.java
 * Date: Jul 30, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import junit.framework.TestCase;

import com.hs.bws.util.DatabaseUtil;


public class DatabaseUtilTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testGetConnection() {
        DatabaseUtil dUtil = DatabaseUtil.getInstance();
        try {
            Connection conn = dUtil.getConnection();
            DatabaseMetaData dmd = conn.getMetaData();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
}
