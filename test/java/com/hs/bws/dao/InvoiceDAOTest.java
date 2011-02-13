/**
 * File Name: InvoiceDAOTest.java
 * Date: Sep 25, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.hs.bws.util.DatabaseUtil;
import com.hs.bws.valueObject.CustomerInvoice;
import com.hs.bws.valueObject.PendingService;


public class InvoiceDAOTest extends TestCase {
    Logger logger = Logger.getLogger(InvoiceDAOTest.class);
    Connection conn = null;
    IInvoiceDAO dao = null;    
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        conn = DatabaseUtil.getInstance().getConnection();
        dao = new InvoiceDAO();

    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetOpenInvoices() {
        try {
            List l = dao.getOpenInvoices(conn);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testGetAllCustomerInvoices() {
        try {
            List l = dao.getAllCustomerInvoices(conn, new Integer(20));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testGetOpenInvoicesByCustomerId() {
        try {
            CustomerInvoice ci = dao.getOpenInvoicesByCustomerId(conn, new Integer(20));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testGetClosedInvoicesByCustomerId() {
        try {
            List l = dao.getClosedInvoicesByCustomerId(conn, new Integer(20));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testGetCustomerInvoice() {
        try {
            CustomerInvoice custInv = dao.getCustomerInvoice(conn, new Integer(20));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testGetInvoiceLineItemListByInvoiceId() {
        try {
            List l = dao.getInvoiceLineItemListByInvoiceId(conn, new Integer(20));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    public void testGetInvoiceByBillDate() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.MAY);
        cal.set(Calendar.DATE, 10);
        cal.set(Calendar.YEAR, 2005);
        dao.getInvoicesByBillDate(conn, cal.getTime());
    }
    
    public void testGentNextInvoiceId() {
        int invoiceId = -22;
        try {
            invoiceId = dao.getNextInvoiceId(conn);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
        
        assertTrue(invoiceId > 0);
    }
    
    public void testGetAllPendingServices() {
        try {
            Object o = dao.getAllPendingServices(conn);
            if (o != null) { logger.info(o.toString());}
            
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }
    
    public void tesstCreatePendingService() {
        PendingService ps = new PendingService();
        ps.setCustomerId(1004);
        ps.setDesc("Some Labor");
        ps.setChargeAmt(40.95);
        
        try {
            dao.createPendingService(conn, ps);
        } catch (SQLException e) {
            fail(e.getMessage());
        } 
    }
    
    public void tesstGetPendingServiceByCustomerId() {
        try {
            List l = dao.getPendingServiceByCustomerId(conn, 3000);
            assertEquals(2, l.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }
    
    public void tesstDeletePendingService() {
        
        try {
            dao.deletePendingService(conn, 3);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }
    
    public void tesstUpdatePendingService() {
        PendingService ps = new PendingService();
        ps.setId(4);
        ps.setCustomerId(1004);
        ps.setDesc("Salt");
        ps.setChargeAmt(30.00);
        try {
            dao.updatePendingService(conn, ps);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }
    
}
