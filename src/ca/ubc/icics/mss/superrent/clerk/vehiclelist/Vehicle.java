/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.clerk.vehiclelist;

import ca.ubc.icics.mss.superrent.clerk.branch.Branch;
import ca.ubc.icics.mss.superrent.database.SQLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author warrior
 */
public class Vehicle {
    private int id;
    private String name;
    private String plateNumber;
    private String category;
    private String type;
    private boolean isAvailable;
    private String thumbnail;
    private int manufacturedYear;
    private int odometerReading;
    private int branchID;
    private int hourlyRate;
    private int dailyRate;
    private int weeklyRate;
    private int hourlyPremium;
    private int dailyPremium;
    private int weeklyPremium;
    
    /**
     * 
     * @param vehicleID 
     */
    public Vehicle (int vehicleID) {
        //using try-with-resources to avoid closing resources (boiler plate code)
        try (Connection con = SQLConnection.getConnection();
                ResultSet rs = con.createStatement().executeQuery("SELECT * FROM vehicle WHERE vehicle_id=" + vehicleID)) {
             
            while(rs.next()) {
                System.out.println("Looking for vehicle with ID " + vehicleID);
                this.id = vehicleID;
                this.name = rs.getString("vehicle_name");
                this.category = rs.getString("vehicle_category");
                this.type = rs.getString("vehicle_type");
                this.plateNumber = rs.getString("plate_number");
                this.odometerReading = rs.getInt("odometer_reading");
                this.manufacturedYear = rs.getInt("vehicle_manufactured_year");
                this.thumbnail = rs.getString("vehicle_thumbnail");
                this.branchID = rs.getInt("branch_id");
                
                // Get the rates based on the vehicle type
                try (ResultSet typeResult = con.createStatement().executeQuery("SELECT * FROM vehicle_category WHERE vehicle_category = '" + category + "'")) {
                    while (typeResult.next()) {
                        System.out.println("Looking for vehicle with category " + category);
                        this.hourlyRate = typeResult.getInt("hourly_rate");
                        this.dailyRate = typeResult.getInt("daily_rate");
                        this.weeklyRate = typeResult.getInt("weekly_rate");
                        this.hourlyPremium = typeResult.getInt("hourly_premium");
                        this.dailyPremium = typeResult.getInt("daily_premium");
                        this.weeklyPremium = typeResult.getInt("weekly_premium");
                    }
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, e);
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
    public String getName() {
        return name;
    }
    
    /**
     * 
     * @return 
     */
    public String getCategory() {
        return category;
    }
    
    /**
     * 
     * @return 
     */
    public String getType() {
        return type;
    }
    
    /**
     * 
     * @return 
     */
    public boolean getIsAvailable() {
        
        try (Connection con = SQLConnection.getConnection()) {
            try (ResultSet rs1 = con.createStatement().executeQuery(
                "SELECT vehicle_id FROM sold_vehicle WHERE vehicle_id = " + this.id)) {
                while(rs1.next()) {
                    this.isAvailable = false;
                    return isAvailable;
                }
            }
            
            // Check the for sale table
            try (ResultSet rs2 = con.createStatement().executeQuery(
                        "SELECT vehicle_id FROM for_sale_vehicle WHERE vehicle_id = " + this.id)) {
                while(rs2.next()) {
                    this.isAvailable = false;
                    return isAvailable;
                }
            }
            
            // Check the rent table
            try (ResultSet rs3 = con.createStatement().executeQuery(
                        "SELECT vehicle_id FROM rent WHERE vehicle_id = " + this.id)) {
                while(rs3.next()) {
                    this.isAvailable = false;
                    return isAvailable;
                }
            }
            
            // Check the reserve table
            try (ResultSet rs4 = con.createStatement().executeQuery(
                        "SELECT vehicle_id FROM reserve WHERE vehicle_id = " + this.id)) {
                while(rs4.next()) {
                    this.isAvailable = false;
                    return isAvailable;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, e);
        }
        
        isAvailable = true;
        return isAvailable;
    }
    
    /**
     * 
     * @return 
     */
    public String getThumbnail() {
        return thumbnail;
    }
    
    /**
     * 
     * @return 
     */
    public Branch getBranch() {
        Branch branch = new Branch (branchID);
        return branch;
    }
    
    /**
     * 
     * @return 
     */
    public int getBranchID() {
        return branchID;
    }
    
    /**
     * 
     * @return 
     */
    public int getHourlyRate() {
        return hourlyRate;
    }
    
    /**
     * 
     * @return 
     */
    public int getDailyRate() {
        return dailyRate;
    }
    
    /**
     * 
     * @return 
     */
    public int getWeeklyRate() {
        return weeklyRate;
    }
    
    /**
     * 
     * @return 
     */
    public int getHourlyPremium() {
        return hourlyPremium;
    }
    
    /**
     * 
     * @return 
     */
    public int getDailyPremium() {
        return dailyPremium;
    }
    
    /**
     * 
     * @return 
     */
    public int getWeeklyPremium() {
        return weeklyPremium;
    }
    
    /**
     * 
     * @return 
     */
    public int getManufacturedYear() {
        return this.manufacturedYear;
    }
    
    /**
     * 
     * @return 
     */
    public int getOdometerReading() {
        return this.odometerReading;
    }
    
    /**
     * 
     * @return 
     */
    public String getPlateNumber() {
        return this.plateNumber;
    }
}

