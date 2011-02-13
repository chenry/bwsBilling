/**
 * File Name: DatabaseUtil.java Date: Jul 31, 2004 Project Name:
 * bwsBillingSystem Description:
 */
package com.hs.bws.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.hs.bws.exception.ConnectionNotAvailableException;

public class DatabaseUtil {
    static Logger logger = Logger.getLogger(DatabaseUtil.class);
    private static DatabaseUtil singleton;

    private String databaseUrl;

    private String userId;

    private String password;

    private Connection conn;

    /**
     *  
     */
    private DatabaseUtil() {
        super();
    }

    public static DatabaseUtil getInstance() {
        if (singleton == null) {
            singleton = new DatabaseUtil();
        }
        return singleton;
    }

    public Connection getConnection() throws ConnectionNotAvailableException {
        userId = Messages.getString("bws.user_id"); //$NON-NLS-1$
        password = Messages.getString("bws.password"); //$NON-NLS-1$

        if (conn != null) {
            return conn;
        }
        databaseUrl = Messages.getString("bws.url"); //$NON-NLS-1$
        try {
            Class.forName(Messages.getString("bws.driver")).newInstance(); //$NON-NLS-1$
            conn = DriverManager.getConnection(databaseUrl, userId, password);
        } catch (Exception e) {
            throw new ConnectionNotAvailableException("The database connecction to " + databaseUrl +  " is not accessible with userId " + userId + " and password " + password, e); //$NON-NLS-1$ //$NON-NLS-2$
        }

        return conn;
    }
    
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Problems closing the result set", e);
            }
        }
    }
    
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Problems closing the connection.", e);
            }
        }
    }
    
    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.error("Problems closing the statment", e);
            }
        }
    }
    
    public static void close(Object o1) {
        if (o1 instanceof Connection) {close((Connection) o1);};
        if (o1 instanceof Statement) {close((Statement) o1);};
        if (o1 instanceof ResultSet) {close((ResultSet) o1);};
    }

    /**
     * Will close these database objects
     * @param o1
     * @param o2
     * @param o3
     * @deprecated Do not use this anymore
     */
    public static void close(Object o1, Object o2, Object o3) {
        close(o1);
        close(o2);
        close(o3);
    }

    public static void close(Object o1, Object o2) {
        close(o1);
        close(o2);
    }

}
