/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.clerk.rentreserve;

import ca.ubc.icics.mss.superrent.clerk.customer.Customer;
import ca.ubc.icics.mss.superrent.clerk.vehiclelist.AdditionalEquipment;
import ca.ubc.icics.mss.superrent.clerk.vehiclelist.Vehicle;
import ca.ubc.icics.mss.superrent.database.SQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author warrior
 */
public class Reserve {
    private SQLConnection scon = new SQLConnection();
    final int H=3600000, D=86400000, W=604800000;
    private int customerID;
    private int vehicleID;
    private int id;
    private Timestamp startDateTime;
    private Timestamp endDateTime;
    private int estimate;
    private ArrayList<AdditionalEquipment> additionalEquipment;
    
    /**
     * 
     */
    public Reserve () {
    }
    
    /**
     * Create a reserve object from the reserve ID
     * 
     * @param reserveID
     */
    public Reserve (int reserveID) {
        this.additionalEquipment = new ArrayList<>();
        
        //using try-with-resources to avoid closing resources (boiler plate code)
        try (Connection con = scon.getConnection();
                ResultSet rs = con.createStatement().executeQuery("SELECT * FROM reserve WHERE "
                        + "reserve_id=" + reserveID);
                
                ResultSet additionalRS = con.createStatement().executeQuery("SELECT * FROM reserved_equipment WHERE "
                        + "reserve_id=" + reserveID)) {
            
            // Add the reservation info to the reserve model
            while(rs.next()){
                this.id = rs.getInt("reserve_id");
                this.customerID = rs.getInt("customer_id");
                this.vehicleID = rs.getInt("vehicle_id");
                this.startDateTime = rs.getTimestamp("start_date_time");
                this.endDateTime = rs.getTimestamp("end_date_time");
                this.estimate = rs.getInt("estimate");
            }
            
            // Add the additional equipment if any to the reserve model
            while (additionalRS.next()) {
                this.additionalEquipment.add(new AdditionalEquipment(additionalRS.getInt("equipment_id")));
            }
        } catch (SQLException e) {
            Logger.getLogger(Reserve.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    /**
     * Create a reserve object with these parameters.
     * 
     * @param customerID
     * @param vehicleID
     * @param startDateTime
     * @param endDateTime
     * @param additionalEquipmentIDs 
     */
    public Reserve (int customerID, int vehicleID, Timestamp startDateTime, 
            Timestamp endDateTime, ArrayList<Integer> additionalEquipmentIDs) {
        this.customerID = customerID;
        this.vehicleID = vehicleID;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.additionalEquipment = new ArrayList<>();
        
        int count = 0;
        while(count < additionalEquipmentIDs.size()) {
            int equipment_id = additionalEquipmentIDs.get(count);
            this.additionalEquipment.add(new AdditionalEquipment(equipment_id));
            count++;
        }
        
        // Calculate the estimated price.
        this.estimate = estimatePrice();
    }
    
    /**
     * Inserts the reserve entry into the database.
     */
    public void confirmReservation () {
        //using try-with-resources to avoid closing resources (boiler plate code)
        try (Connection con = scon.getConnection()) {
            try (PreparedStatement pstatement = con.prepareStatement(""
                    + "INSERT INTO reserve (customer_id, vehicle_id, "
                    + "start_date_time, end_date_time, estimate) "
                    + "values (?, ?, ?, ?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
                System.out.println("INSERT INTO reserve (customer_id, vehicle_id, "
                            + "start_date_time, end_date_time, estimate) "
                            + "values (" + customerID + "," + vehicleID + "," + 
                        startDateTime + "," + endDateTime + "," + estimate + ")");

                pstatement.setInt(1, customerID);
                pstatement.setInt(2, vehicleID);
                pstatement.setTimestamp(3, startDateTime);
                pstatement.setTimestamp(4, endDateTime);
                pstatement.setInt(5, estimate);

                pstatement.executeUpdate();

                // If reserve was successful
                try (ResultSet result = pstatement.getGeneratedKeys()) {
                    while (result.next()) {
                        this.id = result.getInt(1);
                        
                        // Add the additional equipment to the reserve model
                        if (additionalEquipment != null) {
                            int count = 0;
                            while(count < additionalEquipment.size()) {
                                AdditionalEquipment equipment = additionalEquipment.get(count);
                                
                                try (PreparedStatement additionalStatement = con.prepareStatement(""
                                    + "INSERT INTO reserved_equipment (reserve_id, equipment_id)"
                                    + "values (?, ?)")) {
                                    additionalStatement.setInt(1, this.id);
                                    additionalStatement.setInt(2, equipment.getID());
                                }
                                count++;
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(Reserve.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private int estimatePrice() {
        // Get the vehicle category and type from the vehicle ID.
        Vehicle vehicle = new Vehicle(vehicleID);
        int hourlyRate = vehicle.getHourlyRate();
        int dailyRate = vehicle.getDailyRate();
        int weeklyRate = vehicle.getWeeklyRate();
        int hourlyPremium = vehicle.getHourlyPremium();
        int dailyPremium = vehicle.getDailyPremium();
        int weeklyPremium = vehicle.getWeeklyPremium();
        int equipDailyRate = 0;
        int equipHourlyRate = 0;
        
        // If any additional equipment have been added get their rates.
        int count = 0;
        while (count < additionalEquipment.size()) {
            AdditionalEquipment equipment = additionalEquipment.get(count);
            equipDailyRate += equipment.getDailyRate();
            equipHourlyRate += equipment.getHourlyRate();
            count++;
        }
        
        long difference = endDateTime.getTime() - startDateTime.getTime();
        
        int weeks = (int) difference / W;
        difference %= W; 
        int days = (int) difference / D;
        difference %= D; 
        int hours = (int) difference / H;
        difference %= H;
        
        int estimatedCost = weeks * weeklyRate + days * dailyRate + hours * hourlyRate;
        
        System.out.println("estimatedCost " + estimatedCost);
        int premiumEstimate = weeks * weeklyPremium + days * dailyPremium + 
                hours * hourlyPremium;
        System.out.println("premiumEstimate " + premiumEstimate);
        // Check whether the customer is a roadstar or not.
        Customer customer = new Customer(customerID);
        if (customer.getIsRoadStar()) {
            estimatedCost += premiumEstimate / 2;
        } else {
            estimatedCost += premiumEstimate;
        }
        
        // Calculate the additional equipment cost.
        estimatedCost += (weeks * 7 + days) * equipDailyRate;
        estimatedCost += hours * equipHourlyRate;
        
        return estimatedCost;
    }
    
    public void setReserveID (int reserveId) {
        this.id = reserveId;
    }
    
    public int getReserveID () {
        return id;
    }
    
    public void setCustomerID (int customerId) {
        this.customerID = customerId;
    }
    
    public int getCustomerID () {
        return customerID;
    }
    
    public void setVehicleID (int vehicleId) {
        this.vehicleID = vehicleId;
    }
    
    public int getVehicleID () {
        return vehicleID;
    }
    
    public void setStartDateTime(Timestamp startdate) {
        this.startDateTime = startdate;
    }
    
    public Timestamp getStartDateTime() {
        return startDateTime;
    }
    
    public void setEndDateTime(Timestamp enddate) {
        this.endDateTime = enddate;
    }
    
    public Timestamp getEndDateTime() {
        return endDateTime;
    }
    
    public void setEstimate(int estimate) {
        this.estimate = estimate;
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
    public LocalDate getStartDate() {
        return startDateTime.toLocalDateTime().toLocalDate();
    }
    
    /**
     * 
     * @return 
     */
    public String getStartTime() {
        String time = startDateTime.toLocalDateTime().getHour() 
                + ":" + startDateTime.toLocalDateTime().getMinute();
        
        return time;
    }
    
    /**
     * 
     * @return 
     */
    public LocalDate getEndDate() {
        return endDateTime.toLocalDateTime().toLocalDate();
    }
    
    /**
     * 
     * @return 
     */
    public String getEndTime() {
        String time = endDateTime.toLocalDateTime().getHour() + ":" + 
                endDateTime.toLocalDateTime().getMinute();
        
        return time;
    }
    
    /**
     * 
     * @return 
     */
    public ArrayList getAdditionalEquipment() {
        return additionalEquipment;
    }
    
    /**
     * 
     * @return 
     */
    public Customer getCustomer() {
        Customer customer = new Customer(customerID);
        return customer;
    }
    
    /**
     * 
     * @return 
     */
    public Vehicle getVehicle() {
        Vehicle vehicle = new Vehicle(vehicleID);
        return vehicle;
    }
    
    /**
     * 
     * @return 
     */
    public int getEstimate() {
        return estimate;
    }

    
    public void cancelReservation (int resId) {
        try(Connection con = scon.getConnection();
                Statement stmt = con.createStatement();){
            stmt.executeUpdate(
                    "delete from reserve where reserve_id = "
                            + resId+";");
        }catch (SQLException e){
            System.out.println("update fail");
        }
    }
    
}
