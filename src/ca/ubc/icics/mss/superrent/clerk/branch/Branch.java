/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.clerk.branch;

import ca.ubc.icics.mss.superrent.database.SQLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shilpa
 */
public class Branch {
    private SQLConnection scon = new SQLConnection();
    private int id;
    private String city;
    private String location;
    
    /**
     * 
     * @param branchID 
     */
    public Branch(int branchID) {
        //using try-with-resources to avoid closing resources (boiler plate code)
        try (Connection con = scon.getConnection();
                ResultSet rs = con.createStatement().executeQuery("SELECT * FROM vehicle WHERE "
                        + "vehicle_id=" + branchID)) {
            
            while(rs.next()){
                this.id = rs.getInt("branch_id");
                this.city = rs.getString("city");
                this.location = rs.getString("location");
            }
        } catch (SQLException e) {
            Logger.getLogger(Branch.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    /**
     * 
     * @return 
     */
    public int getID() {
        return id;
    }
    
    /**
     * 
     * @return 
     */
    public String getCity() {
        return city;
    }
    
    /**
     * 
     * @return 
     */
    public String getLocation() {
        return location;
    }
}
