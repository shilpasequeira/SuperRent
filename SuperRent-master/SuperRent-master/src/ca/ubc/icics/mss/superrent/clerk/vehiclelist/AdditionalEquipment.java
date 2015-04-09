/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.clerk.vehiclelist;

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
public class AdditionalEquipment {
    private int id;
    private String name;
    private String type;
    private int dailyRate;
    private int hourlyRate;
    private int stock;
    private int branchID;
    
    public AdditionalEquipment() {
    }
    
    public AdditionalEquipment(int id) {
        try (Connection con = SQLConnection.getConnection();
                ResultSet rs = con.createStatement().executeQuery(
                        "SELECT * FROM additional_equipment WHERE equipment_id=" + id)) {
            
            while(rs.next()){
                this.id = rs.getInt("equipment_id");
                this.name = rs.getString("equipment_name");
                this.type = rs.getString("equipment_type");
                this.dailyRate = rs.getInt("equipment_daily_rate");
                this.hourlyRate = rs.getInt("equipment_hourly_rate");
                this.stock = rs.getInt("equipment_stock");
                this.branchID = rs.getInt("branch_id");
            }
        } catch (SQLException e) {
            Logger.getLogger(AdditionalEquipment.class.getName()).log(Level.SEVERE, null, e);
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
    public String getType() {
        return type;
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
    public int getHourlyRate() {
        return hourlyRate;
    }
    
    /**
     * 
     * @return 
     */
    public int getStock() {
        return stock;
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
     */
    public void setID(int id) {
        this.id = id;
    }
    
    /**
     *  
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     *  
     */
    public void setDailyRate(int rate) {
        this.dailyRate = rate;
    }
    
    /**
     * 
     */
    public void setHourlyRate(int rate) {
        this.hourlyRate = rate;
    }
    
    /**
     * 
     */
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    /**
     * 
     */
    public void setBranchID(int id) {
        this.branchID = id;
    }
    
    
}
