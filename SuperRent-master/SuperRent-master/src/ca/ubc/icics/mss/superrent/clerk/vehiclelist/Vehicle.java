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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author warrior
 */
public class Vehicle {
    
    
    private int id;
    private String name;
    private String plateNumber;
    private String category;
    private String vehicleType;
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
    	    private ObjectProperty albumArt= new SimpleObjectProperty();
            private StringProperty pnum= new SimpleStringProperty();
	    private StringProperty vid= new SimpleStringProperty();
	    private StringProperty vname= new SimpleStringProperty();
            private StringProperty type= new SimpleStringProperty();
            private StringProperty bname= new SimpleStringProperty();
            private StringProperty sprice= new SimpleStringProperty();
            private StringProperty sdate= new SimpleStringProperty();
            private StringProperty edate= new SimpleStringProperty();
            private StringProperty odays= new SimpleStringProperty();
            private StringProperty otime= new SimpleStringProperty();
            
            
            
            public Vehicle(Thumbnail album,String pnum,String vid,String vname,String type,String bname) {
                setAlbum(album);
                setPnum(pnum);
                setVname(vname);
	        setVid(vid);
                setType(type);
                setBname(bname);
                              

	    }
            
             
                 
	 
	    public Vehicle(Thumbnail album,String p_num,String vid,String vname,String type,String bname,String sprice) {
                setAlbum(album);
                setPnum(p_num);
                setVname(vname);
	        setVid(vid);
                setType(type);
                setBname(bname);
                setSprice(sprice);
                

	    }
            
             public Vehicle(Thumbnail album,String p_num,String vid,String vname,String type,String bname,String sdate,String edate,String odays,String otime) {
                setAlbum(album);
                setPnum(p_num);
                setVname(vname);
	        setVid(vid);
                setType(type);
                setBname(bname);
                setSdate(sdate);
                setEdate(edate);
                setOdays(odays);
                setOtime(otime);
                

	    }
	 
	    //For Artist
            
              public void setPnum(String pn){
	        pnum.set(pn);
	    }
	    public String getPnum(){
	        return pnum.get();
	    }
	    public StringProperty PnumProperty(){
	        return pnum;
	    }
            
             
                       
	    public void setVid(String v_id){
	        vid.set(v_id);
	    }
	    public String getVid(){
	        return vid.get();
	    }
	    public StringProperty VidProperty(){
	        return vid;
	    }
            
            
             public void setVname(String v_name){
	        vname.set(v_name);
	    }
	    public String getVname(){
	        return vname.get();
	    }
	    public StringProperty VnameProperty(){
	        return vname;
	    }
	 
	  public void setType(String ty){
	        type.set(ty);
	    }
	    public String getType(){
	        return type.get();
	    }
	    public StringProperty TypeProperty(){
	        return type;
	    }
            
             public void setBname(String b_name){
	        bname.set(b_name);
	    }
	    public String getBname(){
	        return bname.get();
	    }
	    public StringProperty BnameProperty(){
	        return bname;
	    }
	 
	  public void setSprice(String sp){
	        sprice.set(sp);
	    }
	    public String getSprice(){
	        return sprice.get();
	    }
	    public StringProperty SpriceProperty(){
	        return sprice;
	    }
            
           public void setSdate(String sd){
	        sdate.set(sd);
	    }
	    public String getSdate(){
	        return sdate.get();
	    }
	    public StringProperty SdateProperty(){
	        return sdate;
	    }
              
            public void setEdate(String ed){
	        edate.set(ed);
	    }
	    public String getEdate(){
	        return edate.get();
	    }
	    public StringProperty EdateProperty(){
	        return edate;
	    }
            
            public void setOdays(String od){
	        odays.set(od);
	    }
	    public String getOdays(){
	        return odays.get();
	    }
	    public StringProperty OdaysProperty(){
	        return odays;
	    }
             
            public void setOtime(String ot){
	        otime.set(ot);
	    }
	    public String getOtime(){
	        return otime.get();
	    }
	    public StringProperty OtimeProperty(){
	        return otime;
	    }
            
	    //For Album
	    public void setAlbum(Thumbnail alb){
	        albumArt.set(alb);
	    }
	    public Object getAlbum(){
	        return albumArt.get();
	    }
	    public ObjectProperty albumProperty(){
	        return albumArt;
	    }
	 

    
    public Vehicle (int vehicleID) {
        //using try-with-resources to avoid closing resources (boiler plate code)
        try (Connection con = SQLConnection.getConnection();
                ResultSet rs = con.createStatement().executeQuery("SELECT * FROM vehicle WHERE vehicle_id=" + vehicleID)) {
             
            while(rs.next()) {
                System.out.println("Looking for vehicle with ID " + vehicleID);
                this.id = vehicleID;
                this.name = rs.getString("vehicle_name");
                this.category = rs.getString("vehicle_category");
                this.vehicleType = rs.getString("vehicle_type");
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
    public String getVehicleType() {
        return vehicleType;
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

