/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.clerk.customer;

import ca.ubc.icics.mss.superrent.clerk.rentreserve.Reserve;
import ca.ubc.icics.mss.superrent.database.SQLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shilpa
 */
public class CustomerRepository {
    
    /**
     * Search for a customer by his ID and return customer object
     * @param id
     * @return Customer object is customer exists else null
     */
    public static Customer searchForCustomerByID (int id) {
        //using try-with-resources to avoid closing resources (boiler plate code)
        try (Connection con = SQLConnection.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT customer_id FROM customer WHERE "
                        + "customer_id=" + id)) {
            
            while(rs.next()) {
                Customer customer = new Customer(rs.getInt("customer_id"));
                
                return customer;
            }
        } catch (SQLException e) {
            Logger.getLogger(Reserve.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return null;
    }
    
    /**
     * Search for a customer by his phone number and return customer object
     * @param phone
     * @return Customer object is customer exists else null
     */
    public static Customer searchForCustomerByPhone (int phone) {
        //using try-with-resources to avoid closing resources (boiler plate code)
        try (Connection con = SQLConnection.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT customer_id FROM customer WHERE "
                        + "phone=" + phone)) {
            
            while(rs.next()) {
                Customer customer = new Customer(rs.getInt("customer_id"));
                
                return customer;
            }
        } catch (SQLException e) {
            Logger.getLogger(Reserve.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return null;
    }
}
