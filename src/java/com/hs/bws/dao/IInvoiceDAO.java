/**
 * File Name: IInvoiceDAO.java
 * Date: May 28, 2005
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.hs.bws.valueObject.Customer;
import com.hs.bws.valueObject.CustomerInvoice;
import com.hs.bws.valueObject.InvoiceLineItem;
import com.hs.bws.valueObject.Payment;
import com.hs.bws.valueObject.PendingService;

public interface IInvoiceDAO {
    /**
     * Creates a new customer invoice
     * 
     * @param conn - Connection to the database
     * @param customerInvoice - contains the customerInvoice information
     * @throws SQLException
     */
    public abstract void createCustomerInvoice(Connection conn,
            CustomerInvoice customerInvoice) throws SQLException;

    /**
     * This method is responsible for retrieving all of the open invoices
     * 
     * @param conn Database connection
     * @return List of all the open invoices
     * @throws SQLException
     */
    public abstract List getOpenInvoices(Connection conn) throws SQLException;

    /**
     * Get a list of all the customers invoices
     * 
     * @param conn - Connection to the database
     * @param customerInvoice - contains the customerId information
     * @return List of customer invoices
     * @throws SQLException
     */
    public abstract List getAllCustomerInvoices(Connection conn,
            Integer customerId) throws SQLException;

    /**
     * Get all of the open invoices on a customers account
     * 
     * @param conn - Connection to the database
     * @param customerInvoice - contains the customer id needed to 
     * 							retrieve the open invoices
     * @return List of open invoices
     * @throws SQLException
     */
    public abstract CustomerInvoice getOpenInvoicesByCustomerId(
            Connection conn, Integer customerId) throws SQLException;

    /**
     * Get all of the closed invoices on a customers account
     * 
     * @param conn - Connection to the database
     * @param customerInvoice - contains the customer id needed to 
     * 							retrieve the closed invoices
     * @return List of closed invoices
     * @throws SQLException
     */
    public abstract List getClosedInvoicesByCustomerId(Connection conn,
            Integer customerId) throws SQLException;

    /**
     * Update the customer invoice and set the new Paid amount
     * 
     * @param conn - Connection to the database
     * @param customerInvoice - contains the new paid amount
     * @throws SQLException
     */
    public abstract void updateInvoicePaidAmt(Connection conn,
            CustomerInvoice customerInvoice) throws SQLException;

    /**
     * Adds a payment to an issue
     * @param conn as Connection
     * @param payment as CustomerInvoice
     * @throws SQLException
     */
    public abstract void addPayment(Connection conn, Payment payment)
            throws SQLException;

    /**
     * Close the customer invoice because it is paid in full or they are
     * choosing to have the customer not pay this invoice
     * 
     * @param conn - Connection to the database
     * @param customerInvoice - contains the updated invoice information
     * @throws SQLException
     */
    public abstract void closeInvoice(Connection conn,
            CustomerInvoice customerInvoice) throws SQLException;

    /**
     * Add an invoice line item to an existing invoice
     * 
     * @param conn - Connection to the database
     * @param invLineItem - invoice line item detail
     * @throws SQLException
     */
    public abstract void addInvoiceLineItem(Connection conn,
            InvoiceLineItem invLineItem) throws SQLException;

    /**
     * @param connection
     * @param custInvoice
     */
    public abstract void deleteInvoice(Connection conn, Integer invoiceId)
            throws SQLException;

    /**
     * Deletes all invoices for the given bill date
     * @param conn as Connection
     * @param billDate as Date
     * @throws SQLException
     */
    public abstract void deleteInvoicesByBillDate(Connection conn,
            java.util.Date billDate) throws SQLException;

    /**
     * @param connection
     * @param ili
     */
    public abstract void deleteInvoiceLineItem(Connection conn,
            Integer invoiceLineItemId) throws SQLException;

    /**
     * @param connection
     * @param ili
     */
    public abstract void updateInvoiceLineItem(Connection conn,
            InvoiceLineItem ili) throws SQLException;

    /**
     * @param connection
     * @param invoiceId
     * @return
     */
    public abstract CustomerInvoice getCustomerInvoice(Connection conn,
            Integer invoiceId) throws SQLException;

    /**
     * @param conn database connection
     * @param invoiceId
     * @return
     */
    public abstract List getInvoiceLineItemListByInvoiceId(Connection conn,
            Integer invoiceId) throws SQLException;

    /**
     * @throws SQLException
     * 
     */
    public abstract int getNextInvoiceId(Connection conn) throws SQLException;

    /**
     * @throws SQLException
     * 
     */
    public abstract int getNextPaymentId(Connection conn) throws SQLException;

    /**
     * Get a list of all the invoices by the bill date
     * 
     * @param billDate as Date
     * @return List of CustomerInvoice objects
     */
    public abstract List getInvoicesByBillDate(Connection conn,
            java.util.Date billDate);

    /**
     * Get the customer information for the given invoice id
     * 
     * @param conn as Connection
     * @param custInvoice as CustomerInvoice
     * @return Customer object
     */
    public abstract Customer getCustomerInformationByCustId(Connection conn,
            Integer custId);

    /**
     * This method will retrieve all of the pending services
     * that are in the pending services table
     * @throws SQLException
     */
    public abstract List getAllPendingServices(Connection conn)
            throws SQLException;

    /**
     * Creates the pending service that was passed into this method. 
     * @param conn - database connection
     * @param ps - object that will be created
     * @throws SQLException
     */
    public abstract void createPendingService(Connection conn, PendingService ps)
            throws SQLException;

    /**
     * Updates the pending service that was passed into this method. 
     * @param conn - database connection
     * @param ps - object that will be created
     * @throws SQLException
     */
    public abstract void updatePendingService(Connection conn, PendingService ps)
            throws SQLException;

    /**
     * @throws SQLException
     * 
     */
    public abstract List getPendingServiceByCustomerId(Connection conn,
            int customerId) throws SQLException;

    /**
     * Will delete the pending service
     * @param conn database connection
     * @param pendingServiceId
     * @throws SQLException
     */
    public abstract void deletePendingService(Connection conn,
            int pendingServiceId) throws SQLException;

    /**
     * @param connection
     * @param custInvoice
     * @return
     */
    public abstract List getPaymentListByInvoiceId(Connection conn,
            CustomerInvoice custInvoice) throws SQLException;

    /**
     * Will delete all invoice line items in the list
     * @param connection as Connection
     * @param inClause as String
     */
    public abstract void deleteInvoiceLineItemsByInvoiceIds(Connection conn,
            String inClause) throws SQLException;

    /**
     * Will delete all payments in the list
     * @param connection as Connection
     * @param inClause as String
     */
    public abstract void deletePaymentsByInvoiceIds(Connection conn,
            String inClause) throws SQLException;
}