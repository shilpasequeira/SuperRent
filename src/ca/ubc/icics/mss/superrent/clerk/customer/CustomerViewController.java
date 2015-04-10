/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.clerk.customer;

import ca.ubc.icics.mss.superrent.clerk.rentreserve.Rent;
import ca.ubc.icics.mss.superrent.clerk.rentreserve.RentReserveFormController;
import ca.ubc.icics.mss.superrent.clerk.rentreserve.Reserve;
import ca.ubc.icics.mss.superrent.clerk.returns.Return;
import ca.ubc.icics.mss.superrent.clerk.returns.ReturnFormController;
import ca.ubc.icics.mss.superrent.database.SQLConnection;
import ca.ubc.icics.mss.superrent.validation.Validate;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author warrior
 */

public class CustomerViewController implements Initializable {

    @FXML
    private TableView rentTable;
    @FXML
    private TableView reserveTable;
    @FXML
    private TableView returnTable;
    @FXML
    private AnchorPane InfoCus;
    @FXML private Label errorMessageField;
    @FXML private Label firstNameLabel;
    @FXML private TextField firstNameField;
    @FXML private Label firstNameErrorLabel;
    @FXML private Label lastNameLabel;
    @FXML private TextField lastNameField;
    @FXML private Label lastNameErrorLabel;
    @FXML private Label addressLabel;
    @FXML private TextField addressField;
    @FXML private Label addressErrorLabel;
    @FXML private Label cityLabel;
    @FXML private TextField cityField;
    @FXML private Label cityErrorLabel;
    @FXML private Label pincodeLabel;
    @FXML private TextField pincodeField;
    @FXML private Label pincodeErrorLabel;
    @FXML private Label phoneLabel;
    @FXML private TextField phoneField;
    @FXML private Label phoneErrorLabel;
    @FXML private Label clubMemberPts;
    @FXML
    private CheckBox isRoadStarCheckBox;
    @FXML
    private CheckBox applyMembershipCheckBox;
    
    
    private Customer customerModel;
    
    //rent table columns
    private TableColumn<Rent, Integer> rtID;
    private TableColumn<Rent, Integer> rtcusID;
    private TableColumn<Rent, Integer> rtvehicleID;
    private TableColumn<Rent, Integer> rtrsID;
    private TableColumn<Rent, Timestamp> rtSDT;
    private TableColumn<Rent, Timestamp> rtEDT;
    private TableColumn<Rent, Integer> rtEstimate;
    private TableColumn<Rent, String> rtcreditCN;
    private TableColumn<Rent, Integer> rtcreditCEM;
    private TableColumn<Rent, Integer> rtcreditCEY;
    private TableColumn<Rent, String> rtdriverL;
    private TableColumn<Rent, Long> rtodometerR;
    private ObservableList rtInfolist = FXCollections.observableArrayList();
    //reserve table columns
    private TableColumn<Reserve, Integer> rsID;
    private TableColumn<Reserve, Integer> rscusID;
    private TableColumn<Reserve, Integer> rsvehicleID;
    private TableColumn<Reserve, Timestamp> rsSDT;
    private TableColumn<Reserve, Timestamp> rsEDT;
    private TableColumn<Reserve, Integer> rsEstimate;
    private ObservableList rsInfolist = FXCollections.observableArrayList();
    //returns table columns
    private TableColumn<Return, Integer> rtnID;
    private TableColumn<Return, Integer> rtnrtID;
    private TableColumn<Return, Integer> rtnodometerR;
    private TableColumn<Return, Integer> rtnTF;
    private TableColumn<Return, Integer> rtnAmount;
    private TableColumn<Return, Integer> rtnPaid;
    private TableColumn<Return, Integer> rtnPointUsed;
    private TableColumn<Return, Timestamp> rtnTransacTime;
    private ObservableList rtnInfolist = FXCollections.observableArrayList();
    
    private static String PhoneNumber;
    public static void setPhoneNum(String number) {
        PhoneNumber = number;
    }
    
    public void ContinueReturn() {
        try {
            Rent rentModel = (Rent) rentTable.getSelectionModel().getSelectedItem();
            System.out.println("return this vehicle.. " + rentModel.getID());
            
            Stage returnStage = new Stage();
            ReturnFormController.setRentID(rentModel.getID());
            FXMLLoader myLoader = new FXMLLoader(ReturnFormController.class.getResource("ReturnForm.fxml"));
            AnchorPane myPane = (AnchorPane) myLoader.load();
            Scene myScene = new Scene(myPane);
            returnStage.setScene(myScene);
            returnStage.show();
        } catch (IOException ex) {
            Logger.getLogger(CustomerViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ContinueRent() {
        System.out.println("rent this vehicle..");
        try {
            Reserve reserveModel = (Reserve) reserveTable.getSelectionModel().getSelectedItem();
            System.out.println("return this vehicle.. " + reserveModel.getID());
            
            Stage returnStage = new Stage();
            RentReserveFormController.setModeRentWithReservation(reserveModel.getReserveID());
            FXMLLoader myLoader = new FXMLLoader(RentReserveFormController.class.getResource("RentReserveForm.fxml"));
            AnchorPane myPane = (AnchorPane) myLoader.load();
            Scene myScene = new Scene(myPane);
            returnStage.setScene(myScene);
            returnStage.show();
        } catch (IOException ex) {
            Logger.getLogger(CustomerViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void CancelReserve() throws ClassNotFoundException {
        System.out.println("cancel this reservation");
        Reserve reserveModel = (Reserve) reserveTable.getSelectionModel().getSelectedItem();
        reserveModel.cancelReservation(reserveModel.getReserveID());
        iniReserveData();
    }
    
    public void iniReserveData() throws ClassNotFoundException {
        rsInfolist.clear();
        try(Connection con = SQLConnection.getConnection();
                ResultSet rs = con.createStatement().executeQuery("select reserve_id,"
                        + " reserve.customer_id, vehicle_id, start_date_time,"
                        + " end_date_time, estimate from customer, reserve where"
                        + " phone_no = "+PhoneNumber+" and customer.customer_id ="
                        + " reserve.customer_id;");){
            while(rs.next()){
                Reserve rsv = new Reserve();
                rsv.setCustomerID(rs.getInt("reserve.customer_id"));
                rsv.setReserveID(rs.getInt("reserve_id"));
                rsv.setVehicleID(rs.getInt("vehicle_id"));
                rsv.setStartDateTime(rs.getTimestamp("start_date_time"));
                rsv.setEndDateTime(rs.getTimestamp("end_date_time"));
                rsv.setEstimate(rs.getInt("estimate"));
                rsInfolist.add(rsv);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void iniReturnData() throws ClassNotFoundException {
        rtnInfolist.clear();
        try (Connection con = SQLConnection.getConnection();
                ResultSet rs = con.createStatement().executeQuery(
                        "select return_id, returns.rent_id, returns.odometer_reading, "
                                + "returns.tank_full, amount, points_used, is_paid, "
                                + "returns.transaction_timestamp from customer, rent, "
                                + "returns where phone_no = "+PhoneNumber+
                                " and customer.customer_id = rent.customer_id "
                                + "and rent.rent_id = returns.rent_id;");) {
            
            while(rs.next()) {
                Return rtn = new Return(rs.getInt("return_id"));
                rtnInfolist.add(rtn);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void iniRentData() throws ClassNotFoundException {
        rtInfolist.clear();
        
        try (Connection con = SQLConnection.getConnection();
                ResultSet rs = con.createStatement().executeQuery(
                        "select rent_id, rent.customer_id, vehicle_id, "
                                + "reserve_id, start_date_time, end_date_time, "
                                + "drivers_license, credit_card_no, credit_card_expiry_month, "
                                + "credit_card_expiry_year, rent.odometer_reading, "
                                + "estimate, rent.transaction_timestamp from rent, "
                                + "customer where rent.customer_id = customer.customer_id "
                                + "and phone_no = "+PhoneNumber+";");) {
            while(rs.next()){
                Rent rt = new Rent(rs.getInt("rent_id"));
                rtInfolist.add(rt);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void iniCusInfo() {
        customerModel = CustomerRepository.searchForCustomerByPhone(
                    PhoneNumber);
        phoneField.setText(PhoneNumber);
        firstNameField.setText(customerModel.getFirstName());
        lastNameField.setText(customerModel.getLastName());
        addressField.setText(customerModel.getAddress());
        cityField.setText(customerModel.getCity());
        pincodeField.setText(customerModel.getPincode());
        isRoadStarCheckBox.setSelected(customerModel.getIsRoadStar());
        applyMembershipCheckBox.setSelected(customerModel.getIsClubMember());
        clubMemberPts.setText("Points : "+customerModel.getPoints());
        //phoneNumberEntered();
    }
    
    @FXML private void phoneNumberEntered() {
        System.out.println("phoneNumberEntered");
        // Check if the phone number is a valid format.
        if (Validate.isValidPhoneNumber(phoneField, phoneErrorLabel)) {
            // Convert the phone number to integer and search for a customer
            // with that phone number.
            Customer customerModel1 = CustomerRepository.searchForCustomerByPhone(
                    phoneField.getText().trim());
            if(customerModel1 != null && customerModel1.getID() != customerModel.getID()){
                this.errorMessageField.setText("This phone number already exists");
            }
            else{
                this.errorMessageField.setText("");
            }
        }
    }
    
    public void saveCustomer() {
        boolean valid = Validate.isValidPhoneNumber(phoneField, phoneErrorLabel) && 
                !Validate.isEmptyTextField(firstNameField, firstNameErrorLabel) &&
                    !Validate.isEmptyTextField(lastNameField, lastNameErrorLabel) &&
                    !Validate.isEmptyTextField(addressField, addressErrorLabel) &&
                    !Validate.isEmptyTextField(cityField, cityErrorLabel) &&
                    !Validate.isEmptyTextField(pincodeField, pincodeErrorLabel);
        if (valid) {
            errorMessageField.setVisible(false);
            customerModel.updateCustomer(firstNameField.getText().trim(), lastNameField.getText().trim(), 
                    addressField.getText().trim(), phoneField.getText().trim(), cityField.getText().trim(),
                    pincodeField.getText().trim(), isRoadStarCheckBox.selectedProperty().getValue(), 
                    applyMembershipCheckBox.selectedProperty().getValue());
            if(applyMembershipCheckBox.selectedProperty().getValue()){
                clubMemberPts.setText("Points : "+customerModel.getPoints());
            }
            else{
                clubMemberPts.setText("");
            }
        } else {
            errorMessageField.setText("Please check the errors.");
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println(PhoneNumber);
        //============================Info======================================
        iniCusInfo();
        //============================rent Table================================
        rtID = new TableColumn("rentID");
        //rType.setMinWidth(141.4);
        rtID.setCellValueFactory(new PropertyValueFactory("ID"));
        rtcusID = new TableColumn("customerID");
        rtcusID.setCellValueFactory(new PropertyValueFactory("CustomerID"));
        rtvehicleID = new TableColumn("vehicleID");
        rtvehicleID.setCellValueFactory(new PropertyValueFactory("VehicleID"));
        rtrsID = new TableColumn("Reserve ID");
        rtrsID.setCellValueFactory(new PropertyValueFactory("ReserveID"));
        rtSDT = new TableColumn("Start Date & Time");
        rtSDT.setCellValueFactory(new PropertyValueFactory("StartDateTime"));
        rtEDT = new TableColumn("End Date & Time");
        rtEDT.setCellValueFactory(new PropertyValueFactory("EndDateTime"));
        rtEstimate = new TableColumn("estimate");
        rtEstimate.setCellValueFactory(new PropertyValueFactory("Estimate"));
        rtcreditCN = new TableColumn("CreditCard#");
        rtcreditCN.setCellValueFactory(new PropertyValueFactory("CardNumber"));
        rtcreditCEM = new TableColumn("CreditCardExpiryMonth");
        rtcreditCEM.setCellValueFactory(new PropertyValueFactory("CardExpiryMonth"));
        rtcreditCEY = new TableColumn("CreditCardExpiryYear");
        rtcreditCEY.setCellValueFactory(new PropertyValueFactory("CardExpiryYear"));
        rtdriverL = new TableColumn("DriversLicense");
        rtdriverL.setCellValueFactory(new PropertyValueFactory("DriversLicense"));
        rtodometerR = new TableColumn("OdometerReading");
        rtodometerR.setCellValueFactory(new PropertyValueFactory("OdometerReading"));
        
        rentTable.setItems(rtInfolist);
        rentTable.getColumns().addAll(rtID, rtcusID, rtvehicleID, rtrsID, rtSDT,
                rtEDT, rtEstimate, rtcreditCN, rtcreditCEM, rtcreditCEY, rtdriverL, 
                rtodometerR);
        //=============================================================================
        
        //==========================reserve Table======================================
        rsID = new TableColumn("reserveID");
        rsID.setMaxWidth(132);
        rsID.setCellValueFactory(new PropertyValueFactory("ReserveID"));
        rscusID = new TableColumn("customeID");
        rscusID.setMaxWidth(132);
        rscusID.setCellValueFactory(new PropertyValueFactory("CustomerID"));
        rsvehicleID = new TableColumn("vehicleID");
        rsvehicleID.setMaxWidth(132);
        rsvehicleID.setCellValueFactory(new PropertyValueFactory("VehicleID"));
        rsSDT = new TableColumn("start date");
        rsSDT.setMinWidth(132);
        rsSDT.setCellValueFactory(new PropertyValueFactory("StartDateTime"));
        rsEDT = new TableColumn("end date");
        rsEDT.setMinWidth(132);
        rsEDT.setCellValueFactory(new PropertyValueFactory("EndDateTime"));
        rsEstimate = new TableColumn("estimate");
        rsEstimate.setMaxWidth(132);
        rsEstimate.setCellValueFactory(new PropertyValueFactory("Estimate"));
        
        reserveTable.setItems(rsInfolist);
        reserveTable.getColumns().addAll(rsID, rscusID, rsvehicleID, rsSDT, rsEDT, rsEstimate);
        
        //=============================================================================
        
        //==========================return Table======================================
        rtnID = new TableColumn("Return ID");
        rtnID.setCellValueFactory(new PropertyValueFactory("id"));
        rtnrtID = new TableColumn("Rental ID");
        rtnrtID.setCellValueFactory(new PropertyValueFactory("RentID"));
        rtnodometerR = new TableColumn("Odometer Reading");
        rtnodometerR.setCellValueFactory(new PropertyValueFactory("OdometerReading"));
        rtnTF = new TableColumn("TankFull");
        rtnTF.setCellValueFactory(new PropertyValueFactory("TankFull"));
        rtnAmount = new TableColumn("Grand Total");
        rtnAmount.setCellValueFactory(new PropertyValueFactory("GrandTotal"));
        rtnPaid = new TableColumn("IsPaid");
        rtnPaid.setCellValueFactory(new PropertyValueFactory("IsPaid"));
        rtnPointUsed = new TableColumn("PointUsed");
        rtnPointUsed.setCellValueFactory(new PropertyValueFactory("PointUsed"));
        
        returnTable.setItems(rtnInfolist);
        returnTable.getColumns().addAll(rtnID, rtnrtID, rtnodometerR, rtnTF, rtnAmount, rtnPaid, rtnPointUsed);
        //=============================================================================
        
        
    }    
    
}
