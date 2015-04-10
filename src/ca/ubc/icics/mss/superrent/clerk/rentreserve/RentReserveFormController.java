/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.clerk.rentreserve;

import ca.ubc.icics.mss.superrent.clerk.customer.Customer;
import ca.ubc.icics.mss.superrent.clerk.customer.CustomerRepository;
import ca.ubc.icics.mss.superrent.clerk.vehiclelist.AdditionalEquipment;
import ca.ubc.icics.mss.superrent.clerk.vehiclelist.AdditionalEquipmentRepository;
import ca.ubc.icics.mss.superrent.clerk.vehiclelist.Vehicle;
import ca.ubc.icics.mss.superrent.validation.Validate;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author warrior
 */
public class RentReserveFormController implements Initializable  {
    private static Rent rentModel;
    private static Reserve reserveModel;
    private static Vehicle vehicleModel;
    private Customer customerModel;
    private static Timestamp startDateTime;
    private static Timestamp endDateTime;
    
    final int H=3600000, D=86400000, W=604800000;
    private static final String RENT = "RENT";
    private static final String RESERVE = "RESERVE";
    private static String mode = RENT;
    private String[] TIMINGS;
    
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
    @FXML private Label driversLicenseLabel;
    @FXML private TextField driversLicenseField;
    @FXML private Label driversLicenseErrorLabel;
    @FXML private Label creditCardLabel;
    @FXML private TextField creditCardField;
    @FXML private Label creditCardErrorLabel;
    @FXML private Label creditExpiryLabel;
    @FXML private ComboBox creditExpiryMonth;
    @FXML private ComboBox creditExpiryYear;
    @FXML private Label creditExpiryErrorLabel;
    @FXML private CheckBox applyMembershipCheckBox;
    @FXML private CheckBox isRoadStarCheckBox;
    @FXML private Label startDateTimeLabel;
    @FXML private Label endDateTimeLabel;
    @FXML private Label rentalPeriodLabel;
    @FXML private Label odometerReadingLabel;
    @FXML private TextField odometerReadingField;
    @FXML private Label odometerReadingErrorLabel;
    @FXML private CheckBox equipmentCheckBox1;
    @FXML private CheckBox equipmentCheckBox2;
    @FXML private Button estimateButton;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;
    @FXML private Label estimateLabel;
    @FXML private Label clubMemberPts;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        initialiseAdditionalEquipment();
        
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        startDateTimeLabel.setText(dateTimeFormat.format(startDateTime));
        endDateTimeLabel.setText(dateTimeFormat.format(endDateTime));
        rentalPeriodLabel.setText(getRentalTimePeriod());
        
        // Setup the UI elements based on the mode.
        if (mode.equals(RENT)) {
            odometerReadingLabel.setVisible(true);
            odometerReadingField.setVisible(true);
            driversLicenseLabel.setVisible(true);
            driversLicenseField.setVisible(true);
            creditCardLabel.setVisible(true);
            creditCardField.setVisible(true);
            creditExpiryLabel.setVisible(true);
            creditExpiryMonth.setVisible(true);
            creditExpiryYear.setVisible(true);
            
            // Initalise month and year dropdowns.
            for (int i = 1; i <= 12; i++) {
                String month = "" + i;
                if (i < 10) {
                    month = "0" + month;
                }
                creditExpiryMonth.getItems().add(month);
            }
            
            for (int i = 2015; i < 2030; i++) {
                creditExpiryYear.getItems().add("" + i);
            }
            
            if (reserveModel != null) {
                initialiseFieldsWithReservation(reserveModel);
            }
        }
    }
    
    /**
     * 
     * @param vehicleID
     * @param start
     * @param end
     */
    public static void setModeRent(int vehicleID, Timestamp start, Timestamp end) {
        mode = RENT;
        vehicleModel = new Vehicle(vehicleID);
        startDateTime = start;
        endDateTime = end;
    }
    
    /**
     * 
     * @param resID
     */
    public static void setModeRentWithReservation(int resID) {
        mode = RENT;
        reserveModel = new Reserve(resID);
        vehicleModel = reserveModel.getVehicle();
        startDateTime = reserveModel.getStartDateTime();
        endDateTime = reserveModel.getEndDateTime();
    }
    
    /**
     * 
     * @param vehicleID
     * @param start
     * @param end
     */
    public static void setModeReserve(int vehicleID, Timestamp start, Timestamp end) {
        mode = RESERVE;
        vehicleModel = new Vehicle(vehicleID);
        startDateTime = start;
        endDateTime = end;
    }
    
    /**
     * Method is called when a phone number is added.
     * @param phone 
     */
    @FXML private void phoneNumberEntered() {
        System.out.println("phoneNumberEntered");
        // Check if the phone number is a valid format.
        if (Validate.isValidPhoneNumber(phoneField, phoneErrorLabel)) {
            // Convert the phone number to integer and search for a customer
            // with that phone number.
            customerModel = CustomerRepository.searchForCustomerByPhone(
                    phoneField.getText().trim());
            
            // If a customer exists, populate the fields with his values.
            if (customerModel != null) {
                //phoneField.setDisable(true);
                firstNameField.setText(customerModel.getFirstName());
                lastNameField.setText(customerModel.getLastName());
                addressField.setText(customerModel.getAddress());
                cityField.setText(customerModel.getCity());
                pincodeField.setText(customerModel.getPincode());

                // Display the club member details
                if (customerModel.getIsClubMember()) {
                    applyMembershipCheckBox.selectedProperty().set(true);
                    applyMembershipCheckBox.setDisable(true);
                    clubMemberPts.setText("Points : " + 
                            Integer.toString(customerModel.getPoints()));
                    clubMemberPts.setVisible(true);
                } 
                // Display the apply for membership checkbox.
                else {
                    applyMembershipCheckBox.setDisable(false);
                    applyMembershipCheckBox.selectedProperty().set(false);
                    clubMemberPts.setVisible(false);
                }

                // Display roadstar badge is customer is a roadstar
                if (customerModel.getIsRoadStar()) {
                    isRoadStarCheckBox.setDisable(true);
                    isRoadStarCheckBox.selectedProperty().set(true);
                } 
                // Else display the checkbox to set the customer as a roadstar
                else {
                    isRoadStarCheckBox.setDisable(false);
                    isRoadStarCheckBox.selectedProperty().set(false);
                }
            } 
            // If no customer exists, enable the fields to enter information.
            else {
                firstNameField.setDisable(false);
                //firstNameField.setText("");
                lastNameField.setDisable(false);
                //lastNameField.setText("");
                addressField.setDisable(false);
                //addressField.setText("");
                cityField.setDisable(false);
                //cityField.setText("");
                pincodeField.setDisable(false);
                //pincodeField.setText("");
                isRoadStarCheckBox.setDisable(false);
                //isRoadStarCheckBox.setVisible(true);
                //isRoadStarCheckBox.setSelected(false);
                applyMembershipCheckBox.setDisable(false);
                //applyMembershipCheckBox.setVisible(true);
                //applyMembershipCheckBox.setSelected(false);
                //clubMemberPts.setText("");
                //customerModel = null;
            }
        }
    }
    
    /**
    * Called when the user clicks on the estimate button.
    */
    @FXML private void estimateButtonPressed() {
        
        if (validateForm()) {
            int currentEstimate = 0;

            if (customerModel == null) {
                customerModel = new Customer(firstNameField.getText(), 
                        lastNameField.getText(), addressField.getText(), 
                        phoneField.getText().trim(), 
                        cityField.getText(), pincodeField.getText(), 
                        isRoadStarCheckBox.selectedProperty().getValue(), 
                        applyMembershipCheckBox.selectedProperty().getValue());
            }

            if (customerModel != null && vehicleModel != null) {

                // Populate the additional equipment array.
                ArrayList<Integer> additionalEquipmentIDs = new ArrayList();
                if (equipmentCheckBox1.selectedProperty().getValue()) {
                    additionalEquipmentIDs.add((Integer) equipmentCheckBox1.getUserData());
                }
                if (equipmentCheckBox2.selectedProperty().getValue()) {
                    additionalEquipmentIDs.add((Integer) equipmentCheckBox2.getUserData());
                }

                if (mode.equals(RENT)) {
                    rentModel = new Rent(customerModel.getID(), vehicleModel.getID(),
                    0, startDateTime, endDateTime, 
                    Long.parseLong(odometerReadingField.getText().trim()), 
                    driversLicenseField.getText(), creditCardField.getText().trim(), 
                    Integer.parseInt(creditExpiryMonth.getValue().toString()), 
                    Integer.parseInt(creditExpiryYear.getValue().toString()),
                    additionalEquipmentIDs);

                    currentEstimate = rentModel.getEstimate();
                } else {
                    reserveModel = new Reserve(customerModel.getID(), vehicleModel.getID(),
                    startDateTime, endDateTime, additionalEquipmentIDs);

                    currentEstimate = reserveModel.getEstimate();
                }

                estimateLabel.setText("CAD " + currentEstimate);
            }
        }
    }
    
    /**
    * Called when the user clicks on the cancel button.
    */
    @FXML private void cancelButtonPressed() {
        errorMessageField.setText("");
        phoneField.setText("");
        phoneField.setDisable(false);
        phoneErrorLabel.setText("");
        firstNameField.setText("");
        firstNameField.setDisable(true);
        firstNameErrorLabel.setText("");
        lastNameField.setText("");
        lastNameField.setDisable(true);
        lastNameErrorLabel.setText("");
        addressField.setText("");
        addressField.setDisable(true);
        addressErrorLabel.setText("");
        cityField.setText("");
        cityField.setDisable(true);
        cityErrorLabel.setText("");
        pincodeField.setText("");
        pincodeField.setDisable(true);
        pincodeErrorLabel.setText("");
        equipmentCheckBox1.setSelected(false);
        equipmentCheckBox2.setSelected(false);
        isRoadStarCheckBox.setSelected(false);
        isRoadStarCheckBox.setDisable(true);
        applyMembershipCheckBox.setSelected(false);
        applyMembershipCheckBox.setDisable(true);
        clubMemberPts.setText("");
        estimateLabel.setText("");
        
        if (mode.equals(RENT)) {
            odometerReadingField.setText("");
            odometerReadingErrorLabel.setText("");
            driversLicenseField.setText("");
            driversLicenseErrorLabel.setText("");
            creditCardField.setText("");
            creditCardErrorLabel.setText("");
            creditExpiryMonth.getSelectionModel().clearSelection();
            creditExpiryMonth.valueProperty().set(null);
            creditExpiryYear.getSelectionModel().clearSelection();
            creditExpiryYear.valueProperty().set(null);
            creditExpiryErrorLabel.setText("");
        }
        
        customerModel = null;
    }
    
    /**
    * Called when the user clicks on the confirm button.
    */
    @FXML private void confirmButtonPressed() {
        
        if (validateForm()) {
            estimateButtonPressed();

            if (mode.equals(RENT)) {
                rentModel.confirmRental();
                estimateLabel.setText("Confirmation # " + rentModel.getID());
            } else {
                reserveModel.confirmReservation();
                estimateLabel.setText("Confirmation # " + reserveModel.getID());
            }
            
            estimateButton.setDisable(true);
            confirmButton.setDisable(true);
            cancelButton.setVisible(false);

            phoneField.setDisable(true);
            firstNameField.setDisable(true);
            lastNameField.setDisable(true);
            addressField.setDisable(true);
            cityField.setDisable(true);
            pincodeField.setDisable(true);
            equipmentCheckBox1.setDisable(true);
            equipmentCheckBox2.setDisable(true);
            isRoadStarCheckBox.setDisable(true);
            applyMembershipCheckBox.setDisable(true);
            clubMemberPts.setDisable(true);
            estimateLabel.setDisable(true);

            if (mode.equals(RENT)) {
                odometerReadingField.setDisable(true);
                driversLicenseField.setDisable(true);
                creditCardField.setDisable(true);
                creditExpiryMonth.setDisable(true);
                creditExpiryMonth.setDisable(true);
                creditExpiryYear.setDisable(true);
                creditExpiryYear.setDisable(true);
            }
        }
    }
    
    /**
     * Validates all fields in the form
     * @return true or false
     */
    private boolean validateForm() {
        boolean valid = false;
        
        // Validate each field of the form.
        if (Validate.isValidPhoneNumber(phoneField, phoneErrorLabel)) {
            phoneNumberEntered();
        } else {
            return false;
        }
        
        if (mode.equals(RENT)) {
            valid = !Validate.isEmptyTextField(firstNameField, firstNameErrorLabel) &&
                    !Validate.isEmptyTextField(lastNameField, lastNameErrorLabel) &&
                    !Validate.isEmptyTextField(addressField, addressErrorLabel) &&
                    !Validate.isEmptyTextField(cityField, cityErrorLabel) &&
                    !Validate.isEmptyTextField(pincodeField, pincodeErrorLabel) &&
                    !Validate.isEmptyTextField(odometerReadingField, odometerReadingErrorLabel) &&
                    !Validate.isEmptyTextField(driversLicenseField, driversLicenseErrorLabel) &&
                    !Validate.isEmptyTextField(creditCardField, creditCardErrorLabel) &&
                    !Validate.isEmptyComboBox(creditExpiryMonth, creditExpiryErrorLabel) &&
                    !Validate.isEmptyComboBox(creditExpiryYear, creditExpiryErrorLabel);
        } else {
            valid = !Validate.isEmptyTextField(firstNameField, firstNameErrorLabel) &&
                    !Validate.isEmptyTextField(lastNameField, lastNameErrorLabel) &&
                    !Validate.isEmptyTextField(addressField, addressErrorLabel) &&
                    !Validate.isEmptyTextField(cityField, cityErrorLabel) &&
                    !Validate.isEmptyTextField(pincodeField, pincodeErrorLabel);
        }
        
        if (valid) {
            errorMessageField.setVisible(false);
        } else {
            errorMessageField.setText("Please check the errors.");
        }
        
        return valid;
    }
    
    /**
     * Initializes all fields with the reservation info.
     * @param reservation model of the reservation
     */
    private void initialiseFieldsWithReservation(Reserve reservation) {

        phoneField.setText(reservation.getCustomer().getPhone());
        firstNameField.setText(reservation.getCustomer().getFirstName());
        lastNameField.setText(reservation.getCustomer().getLastName());
        addressField.setText(reservation.getCustomer().getAddress());
        cityField.setText(reservation.getCustomer().getCity());
        pincodeField.setText(reservation.getCustomer().getPincode());

        ArrayList<AdditionalEquipment> equipment = reservation.getAdditionalEquipment();
        int count = 0;
        while (count < equipment.size()) {
            AdditionalEquipment additionalEquipment = equipment.get(count);
            if (count == 0) {
                this.equipmentCheckBox1.setUserData(additionalEquipment.getID());
                this.equipmentCheckBox1.setText(additionalEquipment.getName());
                equipmentCheckBox1.selectedProperty().set(true);
            } else if (count == 1) {
                this.equipmentCheckBox2.setUserData(additionalEquipment.getID());
                this.equipmentCheckBox2.setText(additionalEquipment.getName());
                equipmentCheckBox2.selectedProperty().set(true);
            }
            count++;
        }

        if (reservation.getCustomer().getIsRoadStar()) {
            isRoadStarCheckBox.selectedProperty().set(true);
        }
        if (reservation.getCustomer().getIsClubMember()) {
            applyMembershipCheckBox.selectedProperty().set(true);
            clubMemberPts.setText("Points : " + 
                    reservation.getCustomer().getPoints());
        }

        estimateLabel.setText("" + reservation.getEstimate());
    }
    
    private void initialiseAdditionalEquipment() {
        // Set up the addtional equipment field based on vehicle type.
        if (vehicleModel != null) {
            // Based on the vehicle type load the addtional equipment.
            ArrayList<AdditionalEquipment> additionalEquipmentList = 
                    AdditionalEquipmentRepository.getAvailableAdditionalEquipment(vehicleModel);
            
            int count = 0;
            while(count < additionalEquipmentList.size()) {
                AdditionalEquipment additionalEquipment = additionalEquipmentList.get(count);
                
                if (count == 0) {
                    this.equipmentCheckBox1.setUserData(additionalEquipment.getID());
                    this.equipmentCheckBox1.setText(additionalEquipment.getName());
                } else if (count == 1) {
                    this.equipmentCheckBox2.setUserData(additionalEquipment.getID());
                    this.equipmentCheckBox2.setText(additionalEquipment.getName());
                }
                count++;
            }
            
            if (count > 0) {
                equipmentCheckBox1.setVisible(true);
                equipmentCheckBox2.setVisible(true);
            }
        }
    }
    
    /**
     * Calculates the rental time period and returns the string representation.
     * @return 
     */
    public String getRentalTimePeriod() {
        String timePeriod = "";
        
        long difference = endDateTime.getTime() - startDateTime.getTime();
        
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
