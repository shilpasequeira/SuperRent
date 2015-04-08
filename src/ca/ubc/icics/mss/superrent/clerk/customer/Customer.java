/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.clerk.customer;

import ca.ubc.icics.mss.superrent.database.SQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author warrior
 */
public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String city;
    private String pincode;
    private boolean isRoadStar;
    private boolean isClubMember;
    private Timestamp clubMemberJoiningDate;
    private Timestamp clubMemberExpiryDate;
    private int points;
    
    /**
     * 
     * @return 
     */
    public int getID() {
        return this.id;
    }
    
    /**
     * 
     * @return 
     */
    public String getFirstName() {
        return this.firstName;
    }
    
    /**
     * 
     * @return 
     */
    public String getLastName() {
        return this.lastName;
    }
    
    /**
     * 
     * @return 
     */
    public String getAddress() {
        return this.address;
    }
    
    /**
     * 
     * @return 
     */
    public String getPhone() {
        return this.phone;
    }
    
    /**
     * 
     * @return 
     */
    public String getCity() {
        return this.city;
    }
    
    /**
     * 
     * @return 
     */
    public String getPincode() {
        return this.pincode;
    }
    
    /**
     * 
     * @return 
     */
    public boolean getIsRoadStar() {
        return this.isRoadStar;
    }
    
    /**
     * 
     * @return 
     */
    public boolean getIsClubMember() {
        return this.isClubMember;
    }
    
    /**
     * 
     * @return 
     */
    public Timestamp getClubMemberJoiningDate() {
        return this.clubMemberJoiningDate;
    }
    
    /**
     * 
     * @return 
     */
    public Timestamp getClubMemberExpiryDate() {
        return this.clubMemberExpiryDate;
    }
    
    /**
     * 
     * @return 
     */
    public int getPoints() {
        return this.points;
    }
    
    /**
     * 
     * @param customerID 
     */
    public Customer(int customerID) {
        //using try-with-resources to avoid closing resources (boiler plate code)
        try (Connection con = SQLConnection.getConnection();
                ResultSet rs = con.createStatement().executeQuery("SELECT * FROM customer WHERE customer_id=" + customerID);
                ResultSet clubRS = con.createStatement().executeQuery("SELECT * FROM club_member WHERE customer_id=" + customerID)) {
             
            while(rs.next()){
                this.id = rs.getInt("customer_id");
                this.firstName = rs.getString("first_name");
                this.lastName = rs.getString("last_name");
                this.address = rs.getString("address");
                this.phone = rs.getString("phone_no");
                this.city = rs.getString("city");
                this.pincode = rs.getString("pincode");
                this.isRoadStar = rs.getBoolean("is_roadstar");
                
                while (clubRS.next()) {
                    this.isClubMember = true;
                    this.points = clubRS.getInt("points");
                    this.clubMemberJoiningDate = clubRS.getTimestamp("joining_date");
                    this.clubMemberExpiryDate = clubRS.getTimestamp("expiry_date");
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    /**
     * Create a new customer or fetch one that has the same phone number
     * 
     * @param firstName
     * @param lastName
     * @param address
     * @param phone
     * @param city
     * @param pincode
     * @param isRoadStar
     * @param isClubMember 
     */
    public Customer(String firstName, String lastName, String address, String phone, 
            String city, String pincode, boolean isRoadStar, boolean isClubMember) {
        
        //using try-with-resources to avoid closing resources (boiler plate code)
        try (ResultSet existingCustomer = 
                SQLConnection.getConnection().createStatement().executeQuery(
                    "SELECT * FROM customer WHERE phone_no=" + phone)) {
            
            int count = 0;
            int customerID = 0;

            // Store the ID of the existing customer.
            while(existingCustomer.next()) {
                count++;
                customerID = existingCustomer.getInt(1);
            }

            // If there are no records that match that phone number.
            if (count == 0) {
                try (PreparedStatement pstatement = 
                        SQLConnection.getConnection().prepareStatement(""
                        + "INSERT INTO customer (phone_no, first_name, "
                        + "last_name, address, city, pincode, is_roadStar) "
                        + "values (?, ?, ?, ?, ?, ?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
                    pstatement.setString(1, phone);
                    pstatement.setString(2, firstName);
                    pstatement.setString(3, lastName);
                    pstatement.setString(4, address);
                    pstatement.setString(5, city);
                    pstatement.setString(6, pincode);
                    pstatement.setBoolean(7, isRoadStar);
                    
                    pstatement.executeUpdate();
                    
                    try (ResultSet result = pstatement.getGeneratedKeys()) {
                        while (result.next()) {
                            customerID = result.getInt(1);
                        }
                    }
                }
            }
            // If customer already exists and is a roadstar, update his field
            else {
                try (PreparedStatement updateCustomer = 
                        SQLConnection.getConnection().prepareStatement(""
                        + "UPDATE customer SET is_roadStar=1 where customer_id = ?")) {
                    updateCustomer.setInt(1, customerID);
                    updateCustomer.executeUpdate();
                }
            }

            // Save the values of the customer.
            this.id = customerID;
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.phone = phone;
            this.city = city;
            this.pincode = pincode;
            this.isRoadStar = isRoadStar;
            this.isClubMember = isClubMember;

            // If the customer wants to be a club member, then add him.
            if (isClubMember) {
                try (PreparedStatement clubMemberStatement = 
                        SQLConnection.getConnection().prepareStatement(""
                        + "INSERT INTO club_member (customer_id, points, "
                                + "joining_date, expiry_date) "
                        + "values (?, ?, ?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
                    clubMemberStatement.setInt(1, customerID);
                    clubMemberStatement.setInt(2, 500);
                    
                    this.points = 500;
                    
                    // Add the joining date and the current date time and
                    // the expiry and one year from then.
                    LocalDateTime now = LocalDateTime.now();
                    clubMemberStatement.setTimestamp(3, Timestamp.valueOf(now));
                    clubMemberStatement.setTimestamp(4, Timestamp.valueOf(now.plusYears(1)));
                    clubMemberStatement.executeUpdate();
                    
                    this.clubMemberJoiningDate = Timestamp.valueOf(now);
                    this.clubMemberExpiryDate = Timestamp.valueOf(now.plusYears(1));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void updateCustomer(String firstName, String lastName, String address, String phone, 
            String city, String pincode, boolean isRoadStar, boolean isClubMember) {
        try (PreparedStatement pstatement = 
                SQLConnection.getConnection().prepareStatement(""
                + "UPDATE customer SET phone_no=?, first_name=?, "
                + "last_name=?, address=?, city=?, pincode=?, is_roadStar=? WHERE customer_id=?")) {
            pstatement.setString(1, phone);
            pstatement.setString(2, firstName);
            pstatement.setString(3, lastName);
            pstatement.setString(4, address);
            pstatement.setString(5, city);
            pstatement.setString(6, pincode);
            pstatement.setBoolean(7, isRoadStar);
            pstatement.setInt(8, id);

            pstatement.executeUpdate();
            
            // If the customer wants to be a club member, then add him.
            if (isClubMember && !getIsClubMember()) {
                try (PreparedStatement clubMemberStatement = 
                        SQLConnection.getConnection().prepareStatement(""
                        + "INSERT INTO club_member (customer_id, points, "
                                + "joining_date, expiry_date) "
                        + "values (?, ?, ?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
                    clubMemberStatement.setInt(1, id);
                    clubMemberStatement.setInt(2, 500);
                    
                    this.points = 500;
                    
                    // Add the joining date and the current date time and
                    // the expiry and one year from then.
                    LocalDateTime now = LocalDateTime.now();
                    clubMemberStatement.setTimestamp(3, Timestamp.valueOf(now));
                    clubMemberStatement.setTimestamp(4, Timestamp.valueOf(now.plusYears(1)));
                    clubMemberStatement.executeUpdate();
                    
                    this.clubMemberJoiningDate = Timestamp.valueOf(now);
                    this.clubMemberExpiryDate = Timestamp.valueOf(now.plusYears(1));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
