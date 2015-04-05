/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.clerk.vehiclelist;

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
}
