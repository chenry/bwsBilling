/**
 * File Name: CustomerService.java
 * Date: Aug 2, 2004
 * Project Name: bwsBillingSystem
 * Description: 
 */
package com.hs.bws.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.hs.bws.dao.CustomerDAO;
import com.hs.bws.dao.ICustomerDAO;
import com.hs.bws.exception.BWSGenericException;
import com.hs.bws.exception.ConnectionNotAvailableException;
import com.hs.bws.exception.ValidFieldsUnavailableException;
import com.hs.bws.util.DatabaseUtil;
import com.hs.bws.valueObject.Customer;


public class CustomerService {
    private static CustomerService custService = null;
    static Logger logger = Logger.getLogger(CustomerService.class.getName());
    private ICustomerDAO custDAO = new CustomerDAO();
    
    public static CustomerService getInstance() {
        if (custService == null) {
            custService = new CustomerService();
        }
        return custService;
    }
    /**
     * This is the object that will be used to retrieve 
     * information
     */
    protected CustomerService() {
        super();
    }

    /**
     * @param c
     */
    public void updateCustomer(Customer c) {
        try {
            custDAO.updateCustomer(DatabaseUtil.getInstance().getConnection(), c);
        } catch (SQLException e) {
            logger.info("SQLException occurred: " + e.getMessage());
        } catch (ConnectionNotAvailableException e) {
            logger.info("Connection was not available " + e.getMessage());
        }
    }

    /**
     * @param c
     */
    public void addCustomer(Customer c) {
        try {
            custDAO.createCustomer(DatabaseUtil.getInstance().getConnection(), c);
        } catch (SQLException e) {
            logger.info("SQLException occurred: " + e.getMessage());
        } catch (ConnectionNotAvailableException e) {
            logger.info("Connection was not available " + e.getMessage());
        }
    }

    /**
     * @param customer
     */
    public void deleteCustomer(Customer customer) {
        try {
            custDAO.deleteCustomer(DatabaseUtil.getInstance().getConnection(), customer);
        } catch (SQLException e) {
            logger.info("SQLException occurred: " + e.getMessage());
        } catch (ConnectionNotAvailableException e) {
            logger.info("Connection was not available " + e.getMessage());
        }
    }

    /**
     * @return
     * @throws BWSGenericException
     */
    public List getBillingCycleTypesList() throws BWSGenericException {
        List billCycleGroupList;
        try {
            billCycleGroupList = custDAO.getBillingCycleGroupList(DatabaseUtil.getInstance().getConnection());
        } catch (Exception e) {
            throw new BWSGenericException("Could not retrieve the billing cycle types.  If this problem persists, please notify support", e);
        }
        return billCycleGroupList;
    }
    
    /**
     * @param customer
     * @throws ValidFieldsUnavailableException
     * @throws BWSGenericException
     * @throws ConnectionNotAvailableException
     * @throws SQLException
     */
    public void createCustomer(Customer customer) throws ValidFieldsUnavailableException, BWSGenericException {
        validateRequiredFieldsAreAvailable(customer);
        try {
            custDAO.createCustomer(DatabaseUtil.getInstance().getConnection(), customer);
        } catch (Exception e) {
            throw new BWSGenericException("Could not create the customer.  If this problem persists, please notify support", e);
        }
    }

    
    /**
     * @throws ValidFieldsUnavailableException
     * 
     */
    private void validateRequiredFieldsAreAvailable(Customer c) throws ValidFieldsUnavailableException {
        if((c.getLastName() == null || c.getLastName().equals("")) && (c.getFirstName() == null || c.getFirstName().equals("")) && (c.getCompanyName() == null || c.getCompanyName().equals(""))) throw new ValidFieldsUnavailableException("Please specify a last name, first name or company name for the customer");
        if(c.getAddress1() == null || c.getAddress1().equals("")) throw new ValidFieldsUnavailableException("The address1 field was not available");
        if(c.getCity() == null || c.getCity().equals("")) throw new ValidFieldsUnavailableException("The city field was not available");
        if(c.getState() == null || c.getState().equals("")) throw new ValidFieldsUnavailableException("The state field was not available");
        if(c.getZipCode() == null || c.getZipCode().equals("")) throw new ValidFieldsUnavailableException("The zip code field was not available");
        if(c.getBillCycleId() == null || c.getBillCycleId().intValue() == 0) throw new ValidFieldsUnavailableException("The billing cycle field was not available");
        if(c.getBillStartMonth() == null || c.getBillStartMonth().equals("")) throw new ValidFieldsUnavailableException("The billing cycle start month field was not available");
        if(c.getInstallationDate() == null) throw new ValidFieldsUnavailableException("The installation date field was not available");
        
    }

    /**
     * @param customer
     * @throws BWSGenericException
     */
    public void closeCustomerAccount(Customer customer) throws BWSGenericException {
        try {
            custDAO.updateCustomer(DatabaseUtil.getInstance().getConnection(), customer);
        } catch (Exception e) {
            throw new BWSGenericException("Could not update the customer", e);
        }
    }
    /**
     * @param id
     * @param creditBalance
     * @throws BWSGenericException
     */
    public void updateCustomerCredit(Integer id, double creditBalance) throws BWSGenericException {
        try {
            custDAO.updateCustomerCredit(DatabaseUtil.getInstance().getConnection(), id, creditBalance);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BWSGenericException("Could not update the customer credit information");
        }
    }
    
    public List getMatchingCustomers(Customer template)throws BWSGenericException{
    	List customers = null;
    	
    	try {
    		customers = custDAO.getMatchingCustomers(DatabaseUtil.getInstance().getConnection(),template);
    	} catch (Exception e) {
    		throw new BWSGenericException("Could not update the customer", e);
}
    	return customers;
    }
	/**
	 * @return List of all the customers
	 */
	public List getAllCustomers() {
		List customerList = null;
		
		//We use an empty customer to return all the customers
		Customer cust = new Customer();
		
		try {
			customerList = this.getMatchingCustomers(cust);
		} catch (BWSGenericException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return customerList;
	}
	/**
	 * @param customerId
	 * @return
	 */
	public Customer getCustomerById(Integer customerId) {
		Customer cust = new Customer(customerId);
		
		List customerList = null;
		
		try {
			customerList = this.getMatchingCustomers(cust);
		} catch (BWSGenericException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (customerList != null && customerList.size() == 1) {
			//There is only one customer and we can populate the fields
			//get customer
			cust = (Customer) customerList.get(0);

		}
		
		
		return cust;
	}
    
}
