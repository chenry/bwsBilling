/**
 * File Name: InvoiceDAO.java 
 * Date: August 26, 2004 
 * Project Name: bwsBillingSystem
 * Description: Customer Invoice DAO
 */
package com.hs.bws.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.hs.bws.util.DatabaseUtil;
import com.hs.bws.valueObject.Customer;
import com.hs.bws.valueObject.CustomerInvoice;
import com.hs.bws.valueObject.InvoiceLineItem;
import com.hs.bws.valueObject.Payment;
import com.hs.bws.valueObject.PendingService;

/**
 * @author Adam F. Sova
 */
public class InvoiceDAO implements IInvoiceDAO {
	Logger logger = Logger.getLogger(InvoiceDAO.class);
	/**
	 * Constructor
	 */
	public InvoiceDAO() {}
	
	/**
	 * Creates a new customer invoice
	 * 
	 * @param conn - Connection to the database
	 * @param customerInvoice - contains the customerInvoice information
	 * @throws SQLException
	 */
	public void createCustomerInvoice(Connection conn, CustomerInvoice customerInvoice) 
			throws SQLException {
		PreparedStatement stmt = null;
		String query = "Insert into invoice " +
						"(customer_id, create_date, due_date, close_date, Paid_amount, invoice_id, bill_date) " +
				       " values " +
					   	"(?,?,?,null,?,?,?)";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, customerInvoice.getCustomerId().intValue());
			stmt.setDate(2, new java.sql.Date(customerInvoice.getCreateDate().getTime()));
			stmt.setDate(3, new java.sql.Date(customerInvoice.getDueDate().getTime()));
			stmt.setDouble(4, customerInvoice.getPaymentAmt().doubleValue());
			stmt.setInt(5, customerInvoice.getId().intValue());
			stmt.setDate(6, new java.sql.Date(customerInvoice.getBillDate().getTime()));
			logger.debug("query: " + query);
			logger.debug("parameters: " + customerInvoice.getCustomerId() + ", " + customerInvoice.getCreateDate() + ", " + 
			        customerInvoice.getDueDate() + ", " + customerInvoice.getPaymentAmt() + ", " + customerInvoice.getId() +
					", " + customerInvoice.getBillDate());
			stmt.executeUpdate();
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
			DatabaseUtil.close(stmt);
		}
	}
	
	/**
	 * This method is responsible for retrieving all of the open invoices
	 * 
	 * @param conn Database connection
	 * @return List of all the open invoices
	 * @throws SQLException
	 */
	public List getOpenInvoices(Connection conn) throws SQLException {
	    List openInvoiceList = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    
	    String query = "Select invoice_id, customer_id, create_date, due_date, close_date, paid_amount " +
	    		"from invoice where close_date is null";
	    
		try {
			stmt = conn.prepareStatement(query);
			logger.debug("query: " + query);
			rs = stmt.executeQuery();
			
			if(rs != null) {
				if(rs.next()) {
				    openInvoiceList = new ArrayList();
					do {
						CustomerInvoice ci = new CustomerInvoice();
						ci.setId(new Integer(rs.getInt(1)));
						ci.setCustomerId(new Integer(rs.getInt(2)));
						ci.setCreateDate(rs.getDate(3));
						ci.setDueDate(rs.getDate(4));
						ci.setCloseDate(rs.getDate(5));
						ci.setPaymentAmt(new Double(rs.getDouble(6)));
						openInvoiceList.add(ci);
					} while(rs.next());
				}
			}
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(rs, stmt);
		}		
	    
	    return openInvoiceList;
	}
	
	/**
	 * Get a list of all the customers invoices
	 * 
	 * @param conn - Connection to the database
	 * @param customerInvoice - contains the customerId information
	 * @return List of customer invoices
	 * @throws SQLException
	 */
	public List getAllCustomerInvoices(Connection conn, Integer customerId) 
			throws SQLException {
		List customerInvoiceList = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String query = "Select " +
						"invoice_id, customer_id, create_date, due_date, close_date, Paid_amount " +
				       "From " +
					    "invoice " +
					   "Where " +
					    "customer_id = ?";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, customerId.intValue());
			logger.debug("query: " + query);
			logger.debug("parameters: " + customerId.intValue());
			rs = stmt.executeQuery();
			
			if(rs != null) {
				if(rs.next()) {
					customerInvoiceList = new Vector();
					do {
						CustomerInvoice ci = new CustomerInvoice();
						ci.setId(new Integer(rs.getInt(1)));
						ci.setCustomerId(new Integer(rs.getInt(2)));
						ci.setCreateDate(rs.getDate(3));
						ci.setDueDate(rs.getDate(4));
						ci.setCloseDate(rs.getDate(5));
						ci.setPaymentAmt(new Double(rs.getDouble(6)));
						customerInvoiceList.add(ci);
					} while(rs.next());
				}
			}
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(rs, stmt);
		}		
		return customerInvoiceList;
	}
	
	/**
	 * Get all of the open invoices on a customers account
	 * 
	 * @param conn - Connection to the database
	 * @param customerInvoice - contains the customer id needed to 
	 * 							retrieve the open invoices
	 * @return List of open invoices
	 * @throws SQLException
	 */
	public CustomerInvoice getOpenInvoicesByCustomerId(Connection conn, Integer customerId) 
			throws SQLException {
		List customerInvoiceList = null;
		PreparedStatement stmt = null;
		CustomerInvoice ci = null;
		ResultSet rs = null;
		
		String query = "Select " +
						"invoice_id, customer_id, create_date, due_date, close_date, Paid_amount " +
				       "From " +
					    "invoice " +
					   "Where " +
					    "customer_id = ? " +
						"and close_date is null";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, customerId.intValue());
			logger.debug("query: " + query);
			logger.debug("parameters: " + customerId.intValue());
			rs = stmt.executeQuery();
			
			if(rs != null) {
				if(rs.next()) {
				    ci = new CustomerInvoice();
					ci.setId(new Integer(rs.getInt(1)));
					ci.setCustomerId(new Integer(rs.getInt(2)));
					ci.setCreateDate(rs.getDate(3));
					ci.setDueDate(rs.getDate(4));
					ci.setCloseDate(rs.getDate(5));
					ci.setPaymentAmt(new Double(rs.getDouble(6)));
				}
			}
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(rs, stmt);
		}		
		return ci;		
	}
	
	/**
	 * Get all of the closed invoices on a customers account
	 * 
	 * @param conn - Connection to the database
	 * @param customerInvoice - contains the customer id needed to 
	 * 							retrieve the closed invoices
	 * @return List of closed invoices
	 * @throws SQLException
	 */
	public List getClosedInvoicesByCustomerId(Connection conn, Integer customerId) 
			throws SQLException {
		List customerInvoiceList = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String query = "Select " +
						"invoice_id, customer_id, create_date, due_date, close_date, Paid_amount " +
				       "From " +
					    "invoice " +
					   "Where " +
					    "customer_id = ? " +
						"and close_date is not null";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, customerId.intValue());
			logger.debug("query: " + query);
			logger.debug("parameters: " + customerId.intValue());
			rs = stmt.executeQuery();
			
			if(rs != null) {
				if(rs.next()) {
					customerInvoiceList = new Vector();
					do {
						CustomerInvoice ci = new CustomerInvoice();
						ci.setId(new Integer(rs.getInt(1)));
						ci.setCustomerId(new Integer(rs.getInt(2)));
						ci.setCreateDate(rs.getDate(3));
						ci.setDueDate(rs.getDate(4));
						ci.setCloseDate(rs.getDate(5));
						ci.setPaymentAmt(new Double(rs.getDouble(6)));
						customerInvoiceList.add(ci);
					} while(rs.next());
				}
			}
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(rs, stmt);
		}		
		return customerInvoiceList;		
	}
	
	/**
	 * Update the customer invoice and set the new Paid amount
	 * 
	 * @param conn - Connection to the database
	 * @param customerInvoice - contains the new paid amount
	 * @throws SQLException
	 */
	public void updateInvoicePaidAmt(Connection conn, CustomerInvoice customerInvoice) 
			throws SQLException {
		PreparedStatement stmt = null;
		
		String query = "Update invoice " +
					   "Set " +
					    "Paid_amount = ? " +
					   "Where " +
					    "invoice_id = ?";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setDouble(1, customerInvoice.getPaymentAmt().doubleValue());
			stmt.setInt(2, customerInvoice.getId().intValue());
			
			stmt.executeUpdate();
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(stmt);
		}
	}
	
	/**
	 * Adds a payment to an issue
	 * @param conn as Connection
	 * @param payment as CustomerInvoice
	 * @throws SQLException
	 */
	public void addPayment(Connection conn, Payment payment) 
		throws SQLException {
		PreparedStatement stmt = null;
		
		String query = "Insert into payment " +
			            "(invoice_id, payment_amt, payment_type, applied_date) " +
					   "values " +
					    "(?,?,?,curdate())";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, payment.getInvoiceId().intValue());
			stmt.setDouble(2, payment.getPaymentAmt().doubleValue());
			stmt.setString(3, payment.getPaymentType());
			logger.debug("query: " + query);
			logger.debug("parameters: " + payment.getInvoiceId() + ", " + payment.getPaymentAmt() + ", " +
										payment.getPaymentType());
			
			stmt.executeUpdate();			
		} catch(SQLException sqlex) {
			logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(stmt);
		}
	}
	
	/**
	 * Close the customer invoice because it is paid in full or they are
	 * choosing to have the customer not pay this invoice
	 * 
	 * @param conn - Connection to the database
	 * @param customerInvoice - contains the updated invoice information
	 * @throws SQLException
	 */
	public void closeInvoice(Connection conn, CustomerInvoice customerInvoice) 
			throws SQLException {
		PreparedStatement stmt = null;
		
		String query = "Update invoice " +
					   "Set " +
						"close_date = curdate() " +
					   "Where " +
					    "invoice_id = ?";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, customerInvoice.getId().intValue());
			logger.debug("query: " + query);
			logger.debug("parameters: " + customerInvoice.getPaymentAmt() + ", " + customerInvoice.getId());
			
			stmt.executeUpdate();
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(stmt);
		}		
	}
	
	/**
	 * Add an invoice line item to an existing invoice
	 * 
	 * @param conn - Connection to the database
	 * @param invLineItem - invoice line item detail
	 * @throws SQLException
	 */
	public void addInvoiceLineItem(Connection conn, InvoiceLineItem invLineItem)
		throws SQLException {
		PreparedStatement stmt = null;
		
		String query = "Insert into invoice_line_item " +
		                "(invoice_id, item_desc, charge_amt) " +
					   "values " +
					    "(?,?,?)";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, invLineItem.getInvoiceId().intValue());
			stmt.setString(2, invLineItem.getItemDesc());
			stmt.setDouble(3, invLineItem.getChargeAmount().doubleValue());
			
			stmt.executeUpdate();
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(stmt);
		}
	}

	/**
	 * @param connection
	 * @param custInvoice
	 */
	public void deleteInvoice(Connection conn, Integer invoiceId) 
		throws SQLException {
		PreparedStatement stmt = null;
		
		String query = "Delete from invoice where invoice_id = ?";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, invoiceId.intValue());
			
			stmt.executeUpdate();
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(stmt);
		}	
	}
	
	/**
	 * Deletes all invoices for the given bill date
	 * @param conn as Connection
	 * @param billDate as Date
	 * @throws SQLException
	 */
	public void deleteInvoicesByBillDate(Connection conn, java.util.Date billDate) 
		throws SQLException {
		PreparedStatement stmt = null;
		
		String query = "Delete from invoice where bill_date = ?";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setDate(1, (java.sql.Date)billDate);
			
			stmt.executeUpdate();
		} catch(SQLException sqlex) {
			logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(stmt);
		}
	}

	/**
	 * @param connection
	 * @param ili
	 */
	public void deleteInvoiceLineItem(Connection conn, Integer invoiceLineItemId) 
		throws SQLException{
		PreparedStatement stmt = null;
		
		String query = "Delete from invoice_line_item where invoice_line_item_id = ?";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, invoiceLineItemId.intValue());
			logger.info("query: " + query);
			logger.info("parameters: " + invoiceLineItemId.intValue());
			stmt.executeUpdate();
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(stmt);
		}
	}

	/**
	 * @param connection
	 * @param ili
	 */
	public void updateInvoiceLineItem(Connection conn, InvoiceLineItem ili) 
		throws SQLException {
		PreparedStatement stmt = null;
		String query = "update invoice_line_item " +
						" set invoice_id = ?, " +
						" item_desc = ?, " +
						" charge_amt = ? " +
						" where invoice_line_item_id = ?";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, ili.getInvoiceId().intValue());
			stmt.setString(2, ili.getItemDesc());
			stmt.setDouble(3, ili.getChargeAmount().doubleValue());
			stmt.setInt(4,ili.getId().intValue());
			logger.debug("query: " + query);
			logger.debug("parameters: " + ili.getInvoiceId() + ", " + ili.getItemDesc() + ", " + 
			        ili.getChargeAmount());
			stmt.executeUpdate();
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(stmt);
		}
		
	}

	/**
	 * @param connection
	 * @param invoiceId
	 * @return
	 */
	public CustomerInvoice getCustomerInvoice(Connection conn, Integer invoiceId) 
		throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		CustomerInvoice ci = null;
		
		String query = "Select " +
						"invoice_id, customer_id, create_date, due_date, close_date, Paid_amount, bill_date " +
				       "From " +
					    "invoice " +
					   "Where " +
					    "invoice_id = ?";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, invoiceId.intValue());
			logger.debug("query: " + query);
			logger.debug("parameters: " + invoiceId.intValue());
			rs = stmt.executeQuery();
			
			if(rs != null) {
				if(rs.next()) {
					do {
						ci = new CustomerInvoice();
						ci.setId(new Integer(rs.getInt(1)));
						ci.setCustomerId(new Integer(rs.getInt(2)));
						ci.setCreateDate(rs.getDate(3));
						ci.setDueDate(rs.getDate(4));
						ci.setCloseDate(rs.getDate(5));
						ci.setPaymentAmt(new Double(rs.getDouble(6)));
						ci.setBillDate(rs.getDate(7));
					} while(rs.next());
				}
			}
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(rs, stmt);
		}		
		return ci;
	}

	/**
	 * @param conn database connection
	 * @param invoiceId
	 * @return
	 */
	public List getInvoiceLineItemListByInvoiceId(Connection conn, Integer invoiceId) 
		throws SQLException {
	    List invLineItemList = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    
	    String query = "select invoice_line_item_id,invoice_id, item_desc, charge_amt from " +
	    		"invoice_line_item where invoice_id = ?";
	    
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, invoiceId.intValue());
			rs = stmt.executeQuery();
			
			if(rs != null) {
				if(rs.next()) {
				    invLineItemList = new ArrayList();
					do {
					    InvoiceLineItem ili = new InvoiceLineItem();
					    ili.setId(new Integer(rs.getInt(1)));
					    ili.setInvoiceId(new Integer(rs.getInt(2)));
					    ili.setItemDesc(rs.getString(3));
					    ili.setChargeAmount(new Double(rs.getDouble(4)));
						invLineItemList.add(ili);
					} while(rs.next());
				}
			}
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(rs, stmt);
		}		
	    
	    return invLineItemList;
	    
	}

    /**
     * @throws SQLException
     * 
     */
    public int getNextInvoiceId(Connection conn) throws SQLException {
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    int invoiceId = 2000;
	    
	    String query = "select max(invoice_id + 1) from invoice";
	    
		try {
			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();
			
			if(rs != null) {
				if(rs.next()) {
				    invoiceId = rs.getInt(1);
				}
			}
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(rs, stmt);
		}
		
		if (invoiceId == 0) {
		    invoiceId = 2000;
		}
	    
	    return invoiceId;
	    
    }

    /**
     * @throws SQLException
     * 
     */
    public int getNextPaymentId(Connection conn) throws SQLException {
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    int paymentId = 2000;
	    
	    String query = "select max(payment_id + 1) from payment";
	    
		try {
			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();
			
			if(rs != null) {
				if(rs.next()) {
				    paymentId = rs.getInt(1);
				}
			}
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(rs, stmt);
		}
		
		if (paymentId == 0) {
		    paymentId = 1;
		}
	    
	    return paymentId;
	    
    }    
    
	/**
	 * Get a list of all the invoices by the bill date
	 * 
	 * @param billDate as Date
	 * @return List of CustomerInvoice objects
	 */
	public List getInvoicesByBillDate(Connection conn, java.util.Date billDate) {
		List invoiceList = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String query = "select " +
		                "invoice_id, customer_id, close_date " +
					   "from " +
					    "invoice " +
					   "where " +
					    "bill_date = ?";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setDate(1, new java.sql.Date(billDate.getTime()));
			logger.info("query: " + query);
			rs = stmt.executeQuery();
			
			if(rs != null) {
				if(rs.next()) {
					invoiceList = new Vector();
					do {
						CustomerInvoice custInvoice = new CustomerInvoice();
						custInvoice.setId(new Integer(rs.getInt(1)));
						custInvoice.setCustomerId(new Integer(rs.getInt(2)));
						custInvoice.setCloseDate(rs.getDate(3));
						invoiceList.add(custInvoice);
					} while(rs.next());
				}
			}
		} catch(SQLException sqlex) {
			logger.error(sqlex.getMessage(), sqlex);
		} finally {
		    DatabaseUtil.close(rs, stmt);
		}
				
		return invoiceList;
	}

	/**
	 * Get the customer information for the given invoice id
	 * 
	 * @param conn as Connection
	 * @param custInvoice as CustomerInvoice
	 * @return Customer object
	 */
	public Customer getCustomerInformationByCustId(Connection conn, Integer custId) {
		Customer customer = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String query = "select " +
						"company_name, first_name, last_name, " +
						"address1, city, state, zipcode " +
					   "from " +
					    "customer " +
					   "where " +
					    "customer_id = ?";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, custId.intValue());
			
			rs = stmt.executeQuery();
			
			if(rs != null) {
				if(rs.next()) {
					customer = new Customer();
					customer.setCompanyName(rs.getString(1));
					customer.setFirstName(rs.getString(2));
					customer.setLastName(rs.getString(3));
					customer.setAddress1(rs.getString(4));
					customer.setCity(rs.getString(5));
					customer.setState(rs.getString(6));
					customer.setZipCode(rs.getString(7));
				}
			}
		} catch(SQLException sqlex) {
			logger.error(sqlex.getMessage(), sqlex);
		} finally {
		    DatabaseUtil.close(rs, stmt);
		}
				
		return customer;
	}

    /**
     * This method will retrieve all of the pending services
     * that are in the pending services table
     * @throws SQLException
     */
    public List getAllPendingServices(Connection conn) throws SQLException {
		List pendingServicesList = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String query = "Select " +
						"pending_service_id, customer_id, description, charge_amt " +
				       "From " +
					    "pending_services ";
		try {
			stmt = conn.prepareStatement(query);
			logger.debug("query: " + query);
			rs = stmt.executeQuery();
			
			if(rs != null) {
				if(rs.next()) {
					pendingServicesList = new ArrayList();
					do {
						PendingService pendServ = new PendingService();
						pendServ.setId(rs.getInt(1));
						pendServ.setCustomerId(rs.getInt(2));
						pendServ.setDesc(rs.getString(3));
						pendServ.setChargeAmt(rs.getDouble(4));
						pendingServicesList.add(pendServ);
					} while(rs.next());
				}
			}
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(rs, stmt);
		}		
		return pendingServicesList;
    }

    /**
     * Creates the pending service that was passed into this method. 
     * @param conn - database connection
     * @param ps - object that will be created
     * @throws SQLException
     */
    public void createPendingService(Connection conn, PendingService ps) throws SQLException {
		PreparedStatement stmt = null;
		String query = "Insert into pending_services " +
						"(customer_id, description, charge_amt) " +
				       " values " +
					   	"(?,?,?)";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, ps.getCustomerId());
			stmt.setString(2, ps.getDesc());
			stmt.setDouble(3, ps.getChargeAmt());
			logger.debug("query: " + query);
			logger.debug("parameters: " + ps.getCustomerId() + ", " + ps.getDesc() + ", " + ps.getChargeAmt());
			stmt.executeUpdate();
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(stmt);
		}
    }

    /**
     * Updates the pending service that was passed into this method. 
     * @param conn - database connection
     * @param ps - object that will be created
     * @throws SQLException
     */
    public void updatePendingService(Connection conn, PendingService ps) throws SQLException {
		PreparedStatement stmt = null;
		String query = "Update pending_services " +
						" set customer_id = ?, description = ?, charge_amt = ? " +
						"where pending_service_id = ?";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, ps.getCustomerId());
			stmt.setString(2, ps.getDesc());
			stmt.setDouble(3, ps.getChargeAmt());
			stmt.setInt(4, ps.getId());
			logger.debug("query: " + query);
			logger.debug("parameters: " + ps.getCustomerId() + ", " + ps.getDesc() + ", " + ps.getChargeAmt() + ", " + ps.getId());
			stmt.executeUpdate();
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(stmt);
		}
    }


    /**
     * @throws SQLException
     * 
     */
    public List getPendingServiceByCustomerId(Connection conn, int customerId) throws SQLException {
		List pendingServicesList = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String query = "Select " +
						"pending_service_id, customer_id, description, charge_amt " +
				       "From " +
					    "pending_services " +
					    "where customer_id = ?";
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, customerId);
			logger.debug("query: " + query);
			logger.debug("parameters: " + customerId);
			rs = stmt.executeQuery();
			
			if(rs != null) {
				if(rs.next()) {
					pendingServicesList = new ArrayList();
					do {
						PendingService pendServ = new PendingService();
						pendServ.setId(rs.getInt(1));
						pendServ.setCustomerId(rs.getInt(2));
						pendServ.setDesc(rs.getString(3));
						pendServ.setChargeAmt(rs.getDouble(4));
						pendingServicesList.add(pendServ);
					} while(rs.next());
				}
			}
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(rs, stmt);
		}		
		return pendingServicesList;
    }

    /**
     * Will delete the pending service
     * @param conn database connection
     * @param pendingServiceId
     * @throws SQLException
     */
    public void deletePendingService(Connection conn, int pendingServiceId) throws SQLException {
		PreparedStatement stmt = null;
		
		String query = "Delete from pending_services where pending_service_id = ?";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, pendingServiceId);
			
			stmt.executeUpdate();
		} catch(SQLException sqlex) {
		    logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(stmt);
		}	
    }

	/**
	 * @param connection
	 * @param custInvoice
	 * @return
	 */
	public List getPaymentListByInvoiceId(Connection conn, CustomerInvoice custInvoice) 
		throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List paymentList = null;
		
		String query = "select invoice_id, payment_id, payment_amt, payment_type, applied_date " +
		               "from payment " +
					   "where invoice_id = ?";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, custInvoice.getId().intValue());
			
			rs = stmt.executeQuery();
			
			if(rs != null) {
				if(rs.next()) {
				    paymentList = new ArrayList();
					do {
					    Payment payment = new Payment();
					    payment.setPaymentId(new Integer(rs.getInt(1)));
					    payment.setInvoiceId(new Integer(rs.getInt(2)));
					    payment.setPaymentAmt(new Double(rs.getDouble(3)));
					    payment.setPaymentType(rs.getString(4));
					    payment.setAppliedDate(rs.getDate(5));
						paymentList.add(payment);
					} while(rs.next());
				}
			}			
		} catch(SQLException sqlex) {
			logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(rs, stmt);
		}
		
		return paymentList;
	}

	/**
	 * Will delete all invoice line items in the list
	 * @param connection as Connection
	 * @param inClause as String
	 */
	public void deleteInvoiceLineItemsByInvoiceIds(Connection conn, String inClause) 
		throws SQLException {
		PreparedStatement stmt = null;
		
		String query = "delete from invoice_line_item where invoice_id in (" + inClause + ")";
		
		try {
			stmt = conn.prepareStatement(query);
			logger.debug("query: " + query);
			
			stmt.executeUpdate();
		} catch(SQLException sqlex) {
			logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(stmt);
		}
	}

	/**
	 * Will delete all payments in the list
	 * @param connection as Connection
	 * @param inClause as String
	 */
	public void deletePaymentsByInvoiceIds(Connection conn, String inClause) 
		throws SQLException {
		PreparedStatement stmt = null;
		
		String query = "delete from payment where invoice_id in (" + inClause + ")";
		
		try {
			stmt = conn.prepareStatement(query);
			logger.debug("query: " + query);
			
			stmt.executeUpdate();
		} catch(SQLException sqlex) {
			logger.error(sqlex.getMessage(), sqlex);
			throw sqlex;
		} finally {
		    DatabaseUtil.close(stmt);
		}
		
	}
}
