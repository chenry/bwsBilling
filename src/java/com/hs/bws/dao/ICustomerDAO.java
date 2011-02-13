/**
 * File Name: ICustomerDAO.java
 * Date: May 28, 2005
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.hs.bws.valueObject.Customer;

public interface ICustomerDAO {
    public abstract void createCustomer(Connection conn, Customer customer)
            throws SQLException;

    public abstract void updateCustomer(Connection conn, Customer customer)
            throws SQLException;

    public abstract void deleteCustomer(Connection conn, Customer customer)
            throws SQLException;

    /**	 
     * 
     * @param conn
     *            Connection used for the query
     * @param template
     *            Customer object with the search critera inside it
     * @return a list of customers that match the template
     * @throws SQLException
     */
    public abstract List getMatchingCustomers(Connection conn, Customer template)
            throws SQLException;

    /**
     * @param saveFile
     */
    public abstract void saveCustomerListToFile(File saveFile);

    /**
     * @return
     * @throws SQLException
     */
    public abstract List getBillingCycleGroupList(Connection conn)
            throws SQLException;

    /**
     * @param conn
     * @param id
     * @param creditBalance
     * @throws SQLException
     */
    public abstract void updateCustomerCredit(Connection conn, Integer id,
            double creditBalance) throws SQLException;
}