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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author warrior
 */
public class Rent {
    private SQLConnection scon = new SQLConnection();
    final int H=3600000, D=86400000, W=604800000;
    private int customerID;
    private int vehicleID;
    private String plateNumber;
    private int reserveID;
    private int id;
    private Timestamp startDateTime;
    private Timestamp endDateTime;
    private int estimate;
    private String creditCardNumber;
    private int creditCardExpiryMonth;
    private int creditCardExpiryYear;
    private String driversLicense;
    private long odometerReading;
    private ArrayList<AdditionalEquipment> additionalEquipment;
    
    /**
     * 
     */
    public Rent () {
    }
    
    /**
     * Create a rent object from the rent ID
     * 
     * @param rentID
     */
    public Rent (int rentID) {
        this.additionalEquipment = new ArrayList<>();
        
        //using try-with-resources to avoid closing resources (boiler plate code)
        try (Connection con = scon.getConnection();
                ResultSet rs = con.createStatement().executeQuery("SELECT * FROM rent WHERE "
                        + "rent_id=" + rentID);
                
                ResultSet additionalRS = con.createStatement().executeQuery("SELECT * FROM rented_equipment WHERE "
                        + "rent_id=" + rentID)) {
            
            // Add the rental info to the rent model
            while(rs.next()){
                this.id = rs.getInt("rent_id");
                this.customerID = rs.getInt("customer_id");
                this.vehicleID = rs.getInt("vehicle_id");
                this.reserveID = rs.getInt("reserve_id");
                this.startDateTime = rs.getTimestamp("start_date_time");
                this.endDateTime = rs.getTimestamp("end_date_time");
                this.estimate = rs.getInt("estimate");
                this.odometerReading = rs.getInt("odometer_reading");
                this.driversLicense = rs.getString("drivers_license");
                this.creditCardNumber = rs.getString("credit_card_no");
                this.creditCardExpiryMonth = rs.getInt("credit_card_expiry_month");
                this.creditCardExpiryYear = rs.getInt("credit_card_expiry_year");
                Vehicle vehicle = new Vehicle(this.vehicleID);
                plateNumber = vehicle.getPlateNumber();
            }
            
            // Add the additional equipment if any to the rent model
            while (additionalRS.next()) {
                this.additionalEquipment.add(new AdditionalEquipment(additionalRS.getInt("equipment_id")));
            }
        } catch (SQLException e) {
            Logger.getLogger(Reserve.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    /**
     * Create a rent object with these parameters.
     * 
     * @param customerID
     * @param vehicleID
     * @param reserveID
     * @param startDateTime
     * @param endDateTime
     * @param odometerReading
     * @param driversLicense
     * @param creditCardNumber
     * @param creditCardExpiryMonth
     * @param creditCardExpiryYear
     * @param additionalEquipmentIDs 
     */
    public Rent (int customerID, int vehicleID, int reserveID, Timestamp startDateTime, 
            Timestamp endDateTime, long odometerReading, 
            String driversLicense, String creditCardNumber, 
            int creditCardExpiryMonth, int creditCardExpiryYear, 
            ArrayList<Integer> additionalEquipmentIDs) {
        this.customerID = customerID;
        this.reserveID = reserveID;
        this.vehicleID = vehicleID;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.odometerReading = odometerReading;
        this.driversLicense = driversLicense;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiryMonth = creditCardExpiryMonth;
        this.creditCardExpiryYear = creditCardExpiryYear;
        this.additionalEquipment = new ArrayList<>();
        
        int count = 0;
        while(count < additionalEquipmentIDs.size()) {
            int equipment_id = additionalEquipmentIDs.get(count);
            this.additionalEquipment.add(new AdditionalEquipment(equipment_id));
            count++;
        }
        
        Vehicle vehicle = new Vehicle(this.vehicleID);
        plateNumber = vehicle.getPlateNumber();
        
        // Calculate the estimated price.
        this.estimate = estimatePrice();
    }
    
    /**
     * Inserts the rental entry into the database.
     */
    public void confirmRental () {
        //using try-with-resources to avoid closing resources (boiler plate code)
        try (Connection con = scon.getConnection()) {
            try (PreparedStatement pstatement = con.prepareStatement(""
                    + "INSERT INTO rent (customer_id, vehicle_id, "
                    + "start_date_time, end_date_time, drivers_license, "
                    + "credit_card_no, credit_card_expiry_month, "
                    + "credit_card_expiry_year, odometer_reading, estimate) "
                    + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
                System.out.println("INSERT INTO rent (customer_id, vehicle_id, "
                            + "start_date_time, end_date_time, drivers_license, credit_card_no, credit_card_expiry,"
                            + "odometer_reading, estimate) "
                            + "values (" + customerID + "," + vehicleID + "," + 
                        startDateTime + "," + endDateTime + "," + driversLicense + 
                        "," + creditCardNumber + "," + creditCardExpiryMonth + "," + 
                        "," + creditCardExpiryYear + "," + 
                        odometerReading + "," + estimate + ")");

                pstatement.setInt(1, customerID);
                pstatement.setInt(2, vehicleID);
                pstatement.setTimestamp(3, startDateTime);
                pstatement.setTimestamp(4, endDateTime);
                pstatement.setString(5, driversLicense);
                pstatement.setString(6, creditCardNumber);
                pstatement.setInt(7, creditCardExpiryMonth);
                pstatement.setInt(8, creditCardExpiryYear);
                pstatement.setLong(9, odometerReading);
                pstatement.setInt(10, estimate);

                pstatement.executeUpdate();

                // If rent was successful
                try (ResultSet result = pstatement.getGeneratedKeys()) {
                    while (result.next()) {
                        this.id = result.getInt(1);
                        
                        // Add the additional equipment to the rent model
                        if (additionalEquipment != null) {
                            int count = 0;
                            while(count < additionalEquipment.size()) {
                                AdditionalEquipment equipment = additionalEquipment.get(count);
                                
                                try (PreparedStatement additionalStatement = con.prepareStatement(""
                                    + "INSERT INTO rented_equipment (rent_id, equipment_id)"
                                    + "values (?, ?)")) {
                                    additionalStatement.setInt(1, this.id);
                                    additionalStatement.setInt(2, equipment.getID());
                                    
                                    additionalStatement.executeUpdate();
                                }
                                
                                Vehicle vehicle = new Vehicle(vehicleID);
                                try (PreparedStatement updateEquipmentStockStatement = 
                                        con.prepareStatement("UPDATE branch_equipment "
                                                + "SET equipment_stock = equipment_stock - 1" 
                                                + " WHERE equipment_id = ?" 
                                                + " AND branch_id = ?")) {
                                    updateEquipmentStockStatement.setInt(1, equipment.getID());
                                    updateEquipmentStockStatement.setInt(2, vehicle.getBranchID());
                                    
                                    updateEquipmentStockStatement.executeUpdate();
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
    
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    
    public int getCustomerID() {
        return customerID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }
    
    public int getVehicleID() {
        return vehicleID;
    }

    public void setReserveID(int reserveID) {
        this.reserveID = reserveID;
    }
    
    public int getReserveID() {
        return reserveID;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setStartDateTime(Timestamp startdate) {
        this.startDateTime = startdate;
    }

    public void setEndDateTime(Timestamp enddate) {
        this.endDateTime = enddate;
    }

    public void setEstimate(int estimate) {
        this.estimate = estimate;
    }

    public void setCardExpiryMonth(int expirymonth) {
        this.creditCardExpiryMonth = expirymonth;
    }

    public void setCardExpiryYear(int expiryyear) {
        this.creditCardExpiryYear = expiryyear;
    }

    public void setCardNumber(String cardnum) {
        this.creditCardNumber = cardnum;
    }

    public void setDriversLicense(String dl) {
        this.driversLicense = dl;
    }

    public void setOdometerReading(long odomread) {
        this.odometerReading = odomread;
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
    public Timestamp getStartDateTime() {
        return startDateTime;
    }
    
    /**
     * 
     * @return 
     */
    public Timestamp getEndDateTime() {
        return endDateTime;
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
    
    /**
     * 
     * @return 
     */
    public String getDriversLicense() {
        return driversLicense;
    }
    
    
    /**
     * 
     * @return 
     */
    public String getCardNumber() {
        return creditCardNumber;
    }
    
    /**
     * 
     * @return 
     */
    public int getCardExpiryMonth() {
        return creditCardExpiryMonth;
    }
    
    /**
     * 
     * @return 
     */
    public int getCardExpiryYear() {
        return creditCardExpiryYear;
    }
    
    /**
     * 
     * @return 
     */
    public Reserve getReservation() {
        Reserve reserve = new Reserve(reserveID);
        return reserve;
    }
    
    /**
     * 
     * @return 
     */
    public long getOdometerReading() {
        return odometerReading;
    }

    public String getPlateNumber() {
        return plateNumber;
    }
}
