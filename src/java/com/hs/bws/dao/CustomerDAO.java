/**
 * File Name: CustomerDAO.java Date: Jul 26, 2004 Project Name: bwsBillingSystem
 * Description:
 */
package com.hs.bws.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hs.bws.util.DatabaseUtil;
import com.hs.bws.util.Messages;
import com.hs.bws.valueObject.Customer;

public class CustomerDAO implements ICustomerDAO {
	Logger logger = Logger.getLogger(CustomerDAO.class);

	protected Map customerMap = null;

	public CustomerDAO() {
	}

	public void createCustomer(Connection conn, Customer customer)
			throws SQLException {
		PreparedStatement stmt = null;
		String query = "INSERT INTO CUSTOMER "
				+ "(first_name, last_name, company_name, address1, address2, city, state, zipcode, alt_address1, "
				+ "alt_address2, alt_city, alt_state, alt_zipcode, comment, rental_charge, billing_cycle_id, install_date, "
				+ "creation_date, last_update_stamp, close_date, billing_start_month_id, credit_bal, use_alt_address) "
				+ "VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		if (logger.isDebugEnabled()) {
			logger.debug("query: " + query);
		}
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, customer.getFirstName());
			stmt.setString(2, customer.getLastName());
			stmt.setString(3, customer.getCompanyName());
			stmt.setString(4, customer.getAddress1());
			stmt.setString(5, customer.getAddress2());
			stmt.setString(6, customer.getCity());
			stmt.setString(7, customer.getState());
			stmt.setString(8, customer.getZipCode());
			stmt.setString(9, customer.getAltAddress1());
			stmt.setString(10, customer.getAltAddress2());
			stmt.setString(11, customer.getAltCity());
			stmt.setString(12, customer.getAltState());
			stmt.setString(13, customer.getAltZipCode());
			stmt.setString(14, customer.getComments());
			stmt.setDouble(15, customer.getRentalCharge().doubleValue());
			stmt.setInt(16, customer.getBillCycleId().intValue());
			stmt.setDate(17, new java.sql.Date(customer.getInstallationDate()
					.getTime()));
			stmt.setDate(18, new java.sql.Date(new java.util.Date().getTime()));
			stmt.setDate(19, new java.sql.Date(new java.util.Date().getTime()));
			stmt.setDate(20, null);
			stmt.setInt(21, customer.getBillStartMonthId());
			stmt.setDouble(22, customer.getCreditBalance());
			if (customer.isUseAltAddress()) {
				stmt.setString(23, "Y");
			} else {
				stmt.setString(23, "N");
			}
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			DatabaseUtil.close(stmt);
		}
	}

	public void updateCustomer(Connection conn, Customer customer) throws SQLException {
		List pList = new ArrayList();
		PreparedStatement stmt = null;
		String query = "UPDATE CUSTOMER SET ";

		if (customer.getFirstName() != "") {
			query = query + " first_name = ?,";
			pList.add(customer.getFirstName());
		}
		if (customer.getLastName() != "") {
			query = query + " last_name = ?,";
			pList.add(customer.getLastName());
		}
		if (customer.getCompanyName() != "") {
			query = query + " company_name = ?,";
			pList.add(customer.getCompanyName());
		}
		if (customer.getAddress1() != "") {
			query = query + " address1 = ?,";
			pList.add(customer.getAddress1());
		}
		if (customer.getAddress2() != null) {
			query = query + " address2 = ?,";
			pList.add(customer.getAddress2());
		}
		if (customer.getCity() != "") {
			query = query + " city = ?,";
			pList.add(customer.getCity());
		}
		if (customer.getState() != "") {
			query = query + " state = ?,";
			pList.add(customer.getState());
		}
		if (customer.getZipCode() != "") {
			query = query + " zipcode = ?,";
			pList.add(customer.getZipCode());
		}
		if (customer.getAltAddress1() != "") {
			query = query + " alt_address1 = ?,";
			pList.add(customer.getAltAddress1());
		}
		if (customer.getAltAddress2() != "") {
			query = query + " alt_address2 = ?,";
			pList.add(customer.getAltAddress2());
		}
		if (customer.getAltCity() != "") {
			query = query + " alt_city = ?,";
			pList.add(customer.getAltCity());
		}
		if (customer.getAltState() != "") {
			query = query + " alt_state = ?,";
			pList.add(customer.getAltState());
		}
		if (customer.getAltZipCode() != "") {
			query = query + " alt_zipcode = ?,";
			pList.add(customer.getAltZipCode());
		}
		if (customer.getComments() != "") {
			query = query + " comment = ?,";
			pList.add(customer.getComments());
		}
		if (customer.getRentalCharge().doubleValue() != 0) {
			query = query + " rental_charge = ?,";
			pList.add(customer.getRentalCharge());
		}

		if (customer.getCreditBalance() != 0) {
			query = query + " credit_bal = ?,";
			pList.add(new Double(customer.getCreditBalance()));
		}

		if (customer.getBillCycleId().intValue() != 0) {
			query = query + " billing_cycle_id = ?,";
			pList.add(customer.getBillCycleId());
		}

		if (customer.getInstallationDate() != null) {
			query = query
					+ " install_date = ?,";
			pList.add(new java.sql.Date(customer.getInstallationDate().getTime()));
		}

		if (customer.getCloseAccountDate() != null) {
			query = query
					+ " close_date = ?,";
			pList.add(new java.sql.Date(customer.getCloseAccountDate().getTime()));
		} else {
			query = query + " close_date = null, ";
		}

		if (customer.getBillStartMonthId() != 0) {
			query = query + "billing_start_month_id = ?,";
			pList.add(new Integer(customer.getBillStartMonthId()));
		}
		if (customer.isUseAltAddress()) {
			query = query + "use_alt_address = 'Y'" + ",";
		} else {
			query = query + "use_alt_address = 'N'" + ",";
		}

		query = query + "last_update_stamp = '"
				+ new java.sql.Date(new java.util.Date().getTime()) + "'";

		query = query + " WHERE customer_id = " + customer.getId();

		if (logger.isDebugEnabled()) {
			logger.debug("query: " + query);
		}
		try {
			stmt = conn.prepareStatement(query);
			int j = 0;
			for (int i = 0; i < pList.size(); i++) {
				j = i + 1;
				Object currParm = pList.get(i);
				stmt.setObject(j, currParm);
			}
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			DatabaseUtil.close(stmt);
		}

		
	}
	public void deleteCustomer(Connection conn, Customer customer)
			throws SQLException {
		PreparedStatement stmt = null;
		String query = "DELETE FROM CUSTOMER WHERE CUSTOMER_ID = ?";

		if (logger.isDebugEnabled()) {
			logger.debug("query: " + query);
		}
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, customer.getId().intValue());

			stmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			DatabaseUtil.close(stmt);
		}

	}

	/**
	 * 
	 * @param conn
	 *            Connection used for the query
	 * @param template
	 *            Customer object with the search critera inside it
	 * @return a list of customers that match the template
	 * @throws SQLException
	 */
	public List getMatchingCustomers(Connection conn, Customer template)
			throws SQLException {
		List customerList = null;
		ResultSet rs = null;
		Statement stmt = null;
		Customer customer = null;
		String query = null;

		// Dynamically creates the query to return the matching customers
		query = "SELECT * " + "FROM customer "
				+ "WHERE customer_id IS NOT NULL ";

		if (template.getId() != null) {
			query = query + " AND customer_id = " + template.getId().intValue();
		}

		if (template.getFirstName() != "") {
			query = query + " AND first_name LIKE '%" + template.getFirstName()
					+ "%'";
		}

		if (template.getLastName() != "") {
			query = query + " AND last_name LIKE '%" + template.getLastName()
					+ "%'";
		}

		if (template.getCompanyName() != "") {
			query = query + " AND company_name LIKE '%"
					+ template.getCompanyName() + "%'";
		}

		if (template.getAddress1() != "") {
			query = query + " AND address1 LIKE '%" + template.getAddress1()
					+ "%'";
		}

		if (template.getAddress2() != "") {
			query = query + " AND address2 LIKE '%" + template.getAddress2()
					+ "%'";
		}

		if (template.getCity() != "") {
			query = query + " AND city LIKE '%" + template.getCity() + "%'";
		}

		if (template.getState() != "") {
			query = query + " AND state LIKE '%" + template.getState() + "%'";
		}

		if (template.getZipCode() != "") {
			query = query + " AND zipcode LIKE '%" + template.getZipCode()
					+ "%'";
		}

		if (template.getAltAddress1() != "") {
			query = query + " AND alt_address1 LIKE '%"
					+ template.getAltAddress1() + "%'";
		}

		if (template.getAltAddress2() != "") {
			query = query + " AND alt_address2 LIKE '%"
					+ template.getAltAddress2() + "%'";
		}

		if (template.getAltCity() != "") {
			query = query + " AND alt_city LIKE '%" + template.getAltCity()
					+ "%'";
		}

		if (template.getAltState() != "") {
			query = query + " AND alt_state LIKE '%" + template.getAltState()
					+ "%'";
		}

		if (template.getAltZipCode() != "") {
			query = query + " AND alt_zipcode LIKE '%"
					+ template.getAltZipCode() + "%'";
		}

		if (template.getComments() != "") {
			query = query + " AND comment LIKE '%" + template.getComments()
					+ "%'";
		}

		if (logger.isDebugEnabled()) {
			logger.debug("query: " + query);
		}
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			if (rs != null && rs.next()) {
				customerList = new ArrayList();
				do {
					customer = new Customer();
					customer.setId(new Integer(rs.getInt("customer_id")));
					customer.setFirstName(rs.getString("first_name"));
					customer.setLastName(rs.getString("last_name"));
					customer.setAddress1(rs.getString("address1"));
					customer.setAddress2(rs.getString("address2"));
					customer.setCity(rs.getString("city"));
					customer.setState(rs.getString("state"));
					customer.setZipCode(rs.getString("zipcode"));
					customer.setAltAddress1(rs.getString("alt_address1"));
					customer.setAltAddress2(rs.getString("alt_address2"));
					customer.setAltCity(rs.getString("alt_city"));
					customer.setAltState(rs.getString("alt_state"));
					customer.setAltZipCode(rs.getString("alt_zipCode"));
					customer.setComments(rs.getString("comment"));
					customer.setCreditBalance(rs.getDouble("credit_bal"));
					customer.setRentalCharge(new Double(rs
							.getDouble("rental_charge")));
					customer.setBillCycleId(new Integer(rs
							.getInt("billing_cycle_id")));
					customer.setInstallationDate(rs.getDate("install_date"));
					customer.setCreationDate(rs.getDate("creation_date"));
					customer.setLastUpdateDate(rs.getDate("last_update_stamp"));
					customer.setCloseAccountDate(rs.getDate("close_date"));
					customer.setBillStartMonthId(rs
							.getInt("billing_start_month_id"));
					customer.setCompanyName(rs.getString("company_name"));
					if (rs.getString("use_alt_address") != null) {
						if (rs.getString("use_alt_address")
								.compareToIgnoreCase("Y") == 0) {
							customer.setUseAltAddress(true);
						} else {
							customer.setUseAltAddress(false);
						}
					} else {
						customer.setUseAltAddress(false);
					}
					customerList.add(customer);
				} while (rs.next());
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			DatabaseUtil.close(rs, stmt);
		}
		return customerList;
	}

	/**
	 * @param saveFile
	 */
	public void saveCustomerListToFile(File saveFile) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileOutputStream(saveFile));
			for (Iterator iter = customerMap.values().iterator(); iter
					.hasNext();) {
				Customer currCustomer = (Customer) iter.next();
				pw.println(currCustomer.getFlatFileString());
			}

		} catch (FileNotFoundException e) {
			System.err.println("Problem writing records to file.  Message = "
					+ e.getMessage());
		} finally {
			pw.flush();
			pw.close();
		}

	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public List getBillingCycleGroupList(Connection conn) throws SQLException {
		List billingCycleGroupList = null;
		ResultSet rs = null;
		Statement stmt = null;
		Map billCycleGroupMap = null;
		String query = "SELECT billing_cycle_group_id, billing_cycle_group_desc from billing_cycle_group";

		if (logger.isDebugEnabled()) {
			logger.debug("query: " + query);
		}
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			if (rs != null && rs.next()) {
				billingCycleGroupList = new ArrayList();
				do {
					billCycleGroupMap = new HashMap();
					billCycleGroupMap.put(Messages.getString("bcg.id"),
							new Integer(rs.getInt(1)));
					billCycleGroupMap.put(Messages.getString("bcg.desc"), rs
							.getString(2));
					billingCycleGroupList.add(billCycleGroupMap);
				} while (rs.next());
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			DatabaseUtil.close(rs, stmt);
		}
		return billingCycleGroupList;
	}

	/**
	 * @param conn
	 * @param id
	 * @param creditBalance
	 * @throws SQLException
	 */
	public void updateCustomerCredit(Connection conn, Integer id,
			double creditBalance) throws SQLException {
		PreparedStatement stmt = null;
		String query = "UPDATE CUSTOMER SET " + "credit_bal = ? "
				+ "WHERE customer_id = ? ";

		if (logger.isDebugEnabled()) {
			logger.debug("query: " + query);
		}
		try {
			stmt = conn.prepareStatement(query);
			stmt.setDouble(1, creditBalance);
			stmt.setInt(2, id.intValue());
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			DatabaseUtil.close(stmt);
		}

	}
}