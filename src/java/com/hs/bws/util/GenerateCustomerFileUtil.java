/**
 * File Name: GenerateCustomerFileUtil.java Date: Jul 25, 2004 Project Name:
 * bwsBillingSystem Description:
 */
package com.hs.bws.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import com.hs.bws.valueObject.Customer;

public class GenerateCustomerFileUtil {

    public static void main(String[] args) {
        GenerateCustomerFileUtil g = new GenerateCustomerFileUtil();
        g.generateCustomerFile(args);
    }

    /**
     *  
     */
    private void generateCustomerFile(String[] nbrToCreate) {
        File customerFile = new File(System.getProperty("user.dir")
                + File.separatorChar + "data" + File.separatorChar
                + "customerDataFile.txt");
        if (customerFile.exists()) {
            customerFile.delete();
        }
        int nbrOfRecords = 0;
        // we will create the number of records specified in the command line
        // argument
        if (nbrToCreate == null || nbrToCreate.length == 0) {
            nbrOfRecords = 100;
        } else {
            nbrOfRecords = Integer.parseInt(nbrToCreate[0]);
        }

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream(customerFile));
            for (int i = 0; i < nbrOfRecords; i++) {
                Customer c = new Customer();
                c.setId(new Integer(i));
                c.setLastName("Henry" + i);
                c.setFirstName("Carlus" + i);
                c.setAddress1("244" + i + " Forest Grove");
                c.setAddress2("Apt 10" + i);
                c.setCity("Wyoming");
                c.setState("MI");
                c.setZipCode("4950" + i);

                // print record to file
                pw.println(c.getFlatFileString());
            }
            
            Customer fochtmanCustomer = new Customer();
            fochtmanCustomer.setId(new Integer(1000));
            fochtmanCustomer.setLastName("Fochtman");
            fochtmanCustomer.setFirstName("Chris");
            fochtmanCustomer.setAddress1("244 Forest Grove");
            fochtmanCustomer.setAddress2("Apt 10");
            fochtmanCustomer.setCity("Wyoming");
            fochtmanCustomer.setState("MI");
            fochtmanCustomer.setZipCode("4950");
            pw.println(fochtmanCustomer.getFlatFileString());
            
        } catch (FileNotFoundException e) {
            System.err.println("Problem writing records to file.  Message = "
                    + e.getMessage());
        } finally {
            pw.flush();
            pw.close();
        }
    }
}

