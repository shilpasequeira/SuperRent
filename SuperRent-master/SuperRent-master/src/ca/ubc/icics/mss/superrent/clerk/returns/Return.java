/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.clerk.returns;

import ca.ubc.icics.mss.superrent.clerk.rentreserve.Rent;
import ca.ubc.icics.mss.superrent.clerk.vehiclelist.AdditionalEquipment;
import ca.ubc.icics.mss.superrent.clerk.vehiclelist.Vehicle;
import ca.ubc.icics.mss.superrent.database.SQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author warrior
 */
public class Return {
    final int H=3600000, D=86400000, W=604800000;
    private int id;
    private int rentID;
    private Rent rentModel;
    private Vehicle vehicleModel;
    private int odometerReading;
    private boolean tankFull;
    private int grandTotal;
    private boolean isPayWithPoints;
    private int pointsUsed = 0;
    private boolean isPaid;
    private String paymentMethod;
    
    /**
     * Create a return object from the reserve ID
     * 
     * @param returnID
     */
    public Return (int returnID) {
        
        //using try-with-resources to avoid closing resources (boiler plate code)
        try (Connection con = SQLConnection.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM returns WHERE "
                        + "return_id=" + returnID);) {
            
            // Add the return info to the return model
            while(rs.next()){
                this.id = rs.getInt("return_id");
                this.rentID = rs.getInt("rent_id");
                this.odometerReading = rs.getInt("odometer_reading");
                this.tankFull = rs.getBoolean("tank_full");
                this.grandTotal = rs.getInt("amount");
                this.pointsUsed = rs.getInt("points_used");
                this.isPaid = rs.getBoolean("is_paid");
                this.paymentMethod = rs.getString("payment_method");
            }
        } catch (SQLException e) {
            Logger.getLogger(Return.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    /**
     * Create a return object with amount calculated.
     * 
     * @param rentModel rent model to use.
     */
    public Return (Rent rentModel) {
        this.rentID = rentModel.getID();
        this.rentModel = rentModel;
        this.vehicleModel = rentModel.getVehicle();
    }
    
    /**
     * Inserts the return entry into the database.
     * @param odometerReading
     * @param tankFull
     * @param paid
     * @param paymentMethod
     */
    public void confirmReturn (int odometerReading, boolean tankFull, boolean paid,
            String paymentMethod) {
        this.odometerReading = odometerReading;
        this.tankFull = tankFull;
        this.isPaid = paid;
        this.paymentMethod = paymentMethod;
        
        grandTotal = getGrandTotal();
        
        //using try-with-resources to avoid closing resources (boiler plate code)
        try (Connection con = SQLConnection.getConnection()) {
            try (PreparedStatement pstatement = con.prepareStatement(""
                    + "INSERT INTO returns (rent_id, odometer_reading, "
                    + "tank_full, amount, points_used, is_paid, payment_method) "
                    + "values (?, ?, ?, ?, ?, ?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
                System.out.println("INSERT INTO returns (rent_id, odometer_reading, "
                    + "tank_full, amount, points_used, is_paid, payment_method)"
                            + "values (" + rentID + "," + odometerReading + "," + 
                        tankFull + "," + grandTotal + "," + pointsUsed + "," + paid + "," + paymentMethod + ")");

                pstatement.setInt(1, rentID);
                pstatement.setInt(2, odometerReading);
                pstatement.setBoolean(3, tankFull);
                pstatement.setInt(4, grandTotal);
                pstatement.setInt(5, pointsUsed);
                pstatement.setBoolean(6, paid);
                pstatement.setString(7, paymentMethod);

                pstatement.executeUpdate();

                // If return was successful
                try (ResultSet result = pstatement.getGeneratedKeys()) {
                    while (result.next()) {
                        this.id = result.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(Return.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public int getId() {
        return id;
    }

    public int getRentID() {
        return rentID;
    }

    public int getOdometerReading() {
        return odometerReading;
    }

    public boolean isTankFull() {
        return tankFull;
    }

    public int getGrandTotal() {
        return grandTotal;
    }

    public int calculateGrandTotal() {
        grandTotal = calculateGrossTotal() + calculateInsuranceCost() + calculateEquipmentTotal();
        int newTotal = 0;
        if (isPayWithPoints) {
            newTotal = grandTotal - rentModel.getCustomer().getPoints();
            pointsUsed = rentModel.getCustomer().getPoints();
            
            if (newTotal < 0) {
                pointsUsed = grandTotal;
                newTotal = 0;
            }
            
            grandTotal = newTotal;
        }
        return grandTotal;
    }

    public int getGrossTotal() {
        return calculateGrossTotal();
    }

    public int getInsuranceCost() {
        return calculateInsuranceCost();
    }

    public int getEquipmentCost() {
        return calculateEquipmentTotal();
    }

    public int getPointsUsed() {
        return pointsUsed;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setIsPayWithPoints(boolean isPayWithPoints) {
        this.isPayWithPoints = isPayWithPoints;
    }
    
    public void setID(int returnId) {
        this.id = returnId;
    }
    
    public int getID() {
        return id;
    }
    
    public void setRentID(int rentId) {
        this.rentID = rentId;
    }
    
    public void setOdometerReading(int odomread) {
        odometerReading = odomread;
    }
    
    public void setTankFull(boolean tankfull) {
        this.tankFull = tankfull;
    }
    
    public void setGrandTotal(int amount) {
        this.grandTotal = amount;
    }
    
    public void setPointUsed(int pointused) {
        this.pointsUsed = pointused;
    }
    
    public int getPointUsed() {
        return pointsUsed;
    }
    
    public void setIsPaid(boolean ispaid) {
        isPaid = ispaid;
    }
    public boolean getIsPaid() {
        return isPaid;
    }
    
    /**
     * Calculates only the cost of the vehicle over the time period
     * @return
     */
    private int calculateGrossTotal() {
        int hourlyRate = vehicleModel.getHourlyRate();
        int dailyRate = vehicleModel.getDailyRate();
        int weeklyRate = vehicleModel.getWeeklyRate();
        
        long difference = Timestamp.valueOf(LocalDateTime.now()).getTime() - 
                rentModel.getStartDateTime().getTime();
        
        int weeks = (int) difference / W;
        difference %= W; 
        int days = (int) difference / D;
        difference %= D; 
        int hours = (int) difference / H;
        difference %= H;
        
        return weeks * weeklyRate + days * dailyRate + hours * hourlyRate;
    }
    
    /**
     * Calculates only the cost of the vehicle over the time period
     * @return
     */
    private int calculateEquipmentTotal() {
        int equipDailyRate = 0;
        int equipHourlyRate = 0;
        
        ArrayList<AdditionalEquipment> additionalEquipment = rentModel.getAdditionalEquipment();
        // If any additional equipment have been added get their rates.
        while (additionalEquipment.iterator().hasNext()) {
            AdditionalEquipment equipment = additionalEquipment.iterator().next();
            equipDailyRate += equipment.getDailyRate();
            equipHourlyRate += equipment.getHourlyRate();
        }
        
        long difference = Timestamp.valueOf(LocalDateTime.now()).getTime() - 
                rentModel.getStartDateTime().getTime();
        
        int weeks = (int) difference / W;
        difference %= W; 
        int days = (int) difference / D;
        difference %= D; 
        int hours = (int) difference / H;
        difference %= H;
        
        // Calculate the additional equipment cost.
        return (weeks * 7 + days) * equipDailyRate +
                hours * equipHourlyRate;
    }
    
    /**
     * 
     * @return 
     */
    private int calculateInsuranceCost() {
        // Get the vehicle category and type from the vehicle ID.
        int hourlyPremium = vehicleModel.getHourlyPremium();
        int dailyPremium = vehicleModel.getDailyPremium();
        int weeklyPremium = vehicleModel.getWeeklyPremium();
        
        long difference = Timestamp.valueOf(LocalDateTime.now()).getTime() - 
                rentModel.getStartDateTime().getTime();
        
        int weeks = (int) difference / W;
        difference %= W; 
        int days = (int) difference / D;
        difference %= D; 
        int hours = (int) difference / H;
        difference %= H;
        
        int insurance = weeks * weeklyPremium + days * dailyPremium + 
                hours * hourlyPremium;
        
        // Check whether the customer is a roadstar or not.
        if (rentModel.getCustomer().getIsRoadStar()) {
            insurance = insurance / 2;
        }
        
        return insurance;
    }
    
    /**
     * Calculates the rental time period and returns the string representation.
     * @return 
     */
    public String getRentalTimePeriod() {
        String timePeriod = "";
        
        long difference = Timestamp.valueOf(LocalDateTime.now()).getTime() - 
                rentModel.getStartDateTime().getTime();
        
        int weeks = (int) difference / W;
        difference %= W; 
        if (weeks > 0) {
            timePeriod += weeks + " Week" + (weeks > 1 ? "s " : " " );
        }
        
        int days = (int) difference / D;
        difference %= D;
        if (days > 0) {
            timePeriod += days + " Day" + (days > 1 ? "s " : " " );
        }
        
        int hours = (int) difference / H;
        difference %= H;
        if (hours > 0) {
            timePeriod += hours + " Hour" + (hours > 1 ? "s " : " " );
        }
        
        
        return timePeriod;
    }
}
