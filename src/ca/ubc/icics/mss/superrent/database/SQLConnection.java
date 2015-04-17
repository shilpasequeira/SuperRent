/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.database;
import ca.ubc.icics.mss.superrent.manager.reports.ReportsViewController;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*; 
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Avinash
 */
public class SQLConnection {
    
    public Connection getConnection() {
        Properties props = new Properties();
        FileInputStream fis = null;
        Connection con = null;
        try {
            // create the connection now
               con = DriverManager.getConnection("jdbc:mysql://dbserver.mss.icics.ubc.ca/team02", "team02", "s0ftw@re");
       
        } catch ( SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return con;
    }
}
