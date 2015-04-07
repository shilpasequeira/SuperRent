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
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Callback;

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
    @FXML private Label startDateLabel;
    @FXML private DatePicker startDateField;
    @FXML private Label startDateErrorLabel;
    @FXML private Label startTimeLabel;
    @FXML private ComboBox startTimeField;
    @FXML private Label startTimeErrorLabel;
    @FXML private Label endDateLabel;
    @FXML private DatePicker endDateField;
    @FXML private Label endDateErrorLabel;
    @FXML private Label endTimeLabel;
    @FXML private ComboBox endTimeField;
    @FXML private Label endTimeErrorLabel;
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
        initialiseDatePickers();
        initialiseTimePickers();
        
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
     */
    public static void setModeRent(int vehicleID) {
        mode = RENT;
        vehicleModel = new Vehicle(vehicleID);
    }
    
    /**
     * 
     * @param resID
     */
    public static void setModeRentWithReservation(int resID) {
        mode = RENT;
        reserveModel = new Reserve(resID);
        vehicleModel = reserveModel.getVehicle();
    }
    
    /**
     * 
     * @param vehicleID
     */
    public static void setModeReserve(int vehicleID) {
        mode = RESERVE;
        vehicleModel = new Vehicle(vehicleID);
    }
    
    /**
     * 
     */
    @FXML private void startDateChanged() {
        if (startDateField.getValue() != null) {
            endDateField.setValue(startDateField.getValue());
        }
    }
    
    /**
     * 
     */
    @FXML private void endDateChanged() {
        if (startDateField.getValue() != null && endDateField.getValue() != null && 
                !startDateField.getValue().equals(endDateField.getValue())) {
            endTimeField.getItems().removeAll((Object[]) TIMINGS);
            endTimeField.getItems().addAll((Object[]) TIMINGS);
        }
    }
    
    /**
     * If the start and end dates are the same, do not show timings less than
     * the start time selected in the end time combo box.
     */
    @FXML private void startTimeChanged() {
        if (startDateField.getValue() != null && endDateField.getValue() != null
                && startDateField.getValue().equals(endDateField.getValue())) {
            endTimeField.getItems().removeAll((Object[]) TIMINGS);
            
            int selectedIndex = 0;
            for (int i = 0; i < TIMINGS.length; i++) {
                if (TIMINGS[i].equals(startTimeField.getValue())) {
                    selectedIndex = i;
                }
            }

            for (int i = selectedIndex + 1; i < TIMINGS.length; i++) {
                endTimeField.getItems().add(TIMINGS[i]);
            }
        } else if (startDateField.getValue() != null && endDateField.getValue() != null
                && !startDateField.getValue().equals(endDateField.getValue())) {
            endTimeField.getItems().addAll((Object[]) TIMINGS);
        }
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
                phoneField.setDisable(true);
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
                firstNameField.setText("");
                lastNameField.setDisable(false);
                lastNameField.setText("");
                addressField.setDisable(false);
                addressField.setText("");
                cityField.setDisable(false);
                cityField.setText("");
                pincodeField.setDisable(false);
                pincodeField.setText("");
                isRoadStarCheckBox.setDisable(false);
                isRoadStarCheckBox.setVisible(true);
                isRoadStarCheckBox.setSelected(false);
                applyMembershipCheckBox.setDisable(false);
                applyMembershipCheckBox.setVisible(true);
                applyMembershipCheckBox.setSelected(false);
                clubMemberPts.setText("");
                customerModel = null;
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
                    getTimestamp(startDateField, startTimeField), 
                    getTimestamp(endDateField, endTimeField), 
                    Long.parseLong(odometerReadingField.getText().trim()), 
                    driversLicenseField.getText(), creditCardField.getText().trim(), 
                    Integer.parseInt(creditExpiryMonth.getValue().toString()), 
                    Integer.parseInt(creditExpiryYear.getValue().toString()),
                    additionalEquipmentIDs);

                    currentEstimate = rentModel.getEstimate();
                } else {
                    reserveModel = new Reserve(customerModel.getID(), vehicleModel.getID(),
                    getTimestamp(startDateField, startTimeField), 
                    getTimestamp(endDateField, endTimeField),
                    additionalEquipmentIDs);

                    currentEstimate = reserveModel.getEstimate();
                }

                this.estimateLabel.setText("CAD " + currentEstimate);
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
        firstNameErrorLabel.setText("");
        lastNameField.setText("");
        lastNameErrorLabel.setText("");
        addressField.setText("");
        addressErrorLabel.setText("");
        cityField.setText("");
        cityErrorLabel.setText("");
        pincodeField.setText("");
        pincodeErrorLabel.setText("");
        startDateField.setValue(null);
        startDateField.getEditor().clear();
        startDateErrorLabel.setText("");
        endDateField.setValue(null);
        endDateField.getEditor().clear();
        endDateErrorLabel.setText("");
        startTimeField.getSelectionModel().clearSelection();
        startTimeField.valueProperty().set(null);
        startTimeErrorLabel.setText("");
        endTimeField.getSelectionModel().clearSelection();
        endTimeField.valueProperty().set(null);
        endTimeErrorLabel.setText("");
        equipmentCheckBox1.setSelected(false);
        equipmentCheckBox2.setSelected(false);
        isRoadStarCheckBox.setSelected(false);
        isRoadStarCheckBox.setDisable(false);
        applyMembershipCheckBox.setSelected(false);
        applyMembershipCheckBox.setDisable(false);
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
                estimateLabel.setText("Rental Confirmed");
            } else {
                reserveModel.confirmReservation();
                estimateLabel.setText("Reservation Confirmed");
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
            startDateField.setDisable(true);
            startDateField.setDisable(true);
            endDateField.setDisable(true);
            endDateField.setDisable(true);
            startTimeField.setDisable(true);
            startTimeField.setDisable(true);
            endTimeField.setDisable(true);
            endTimeField.setDisable(true);
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
                    !Validate.isEmptyDateField(startDateField, startDateErrorLabel) &&
                    !Validate.isEmptyDateField(endDateField, endDateErrorLabel) &&
                    !Validate.isEmptyComboBox(startTimeField, startTimeErrorLabel) &&
                    !Validate.isEmptyComboBox(endTimeField, endTimeErrorLabel) &&
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
                    !Validate.isEmptyTextField(pincodeField, pincodeErrorLabel) &&
                    !Validate.isEmptyDateField(startDateField, startDateErrorLabel) &&
                    !Validate.isEmptyDateField(endDateField, endDateErrorLabel) &&
                    !Validate.isEmptyComboBox(startTimeField, startTimeErrorLabel) &&
                    !Validate.isEmptyComboBox(endTimeField, endTimeErrorLabel);
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
        startDateField.setValue(reservation.getStartDate());
        endDateField.setValue(reservation.getEndDate());
        startTimeField.getSelectionModel().select(reservation.getStartTime());
        endTimeField.getSelectionModel().select(reservation.getEndTime());

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
    
    /**
     * Initializes all fields with the reservation info.
     * @param rental model of the rent
     */
    /*private void initialiseFieldsWithRental(Rent rental) {

        phoneField.setText(rental.getCustomer().getPhone());
        firstNameField.setText(rental.getCustomer().getFirstName());
        lastNameField.setText(rental.getCustomer().getLastName());
        addressField.setText(rental.getCustomer().getAddress());
        cityField.setText(rental.getCustomer().getCity());
        pincodeField.setText(rental.getCustomer().getPincode());
        startDateField.setValue(rental.getStartDate());
        endDateField.setValue(rental.getEndDate());
        startTimeField.getSelectionModel().select(rental.getStartTime());
        endTimeField.getSelectionModel().select(rental.getEndTime());

        ArrayList<AdditionalEquipment> equipment = rental.getAdditionalEquipment();
        while (equipment.iterator().hasNext()) {
            int count = 0;
            while(equipment.iterator().hasNext()) {
                AdditionalEquipment additionalEquipment = equipment.iterator().next();
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
        }

        if (rental.getCustomer().getIsRoadStar()) {
            isRoadStarCheckBox.selectedProperty().set(true);
        }
        if (rental.getCustomer().getIsClubMember()) {
            applyMembershipCheckBox.selectedProperty().set(true);
            clubMemberPts.setText("Points : " + 
                    rental.getCustomer().getPoints());
        }
        
        odometerReadingField.setText("" + rental.getOdometerReading());
        driversLicenseField.setText(rental.getDriversLicense());
        creditCardField.setText(rental.getCardNumber());
        
        String expiryMonth = "" + rental.getCardExpiryMonth();
        if (rental.getCardExpiryMonth() < 10) {
            expiryMonth += "0" + expiryMonth;
        }
        creditExpiryMonth.selectionModelProperty().set(expiryMonth);
        creditExpiryYear.selectionModelProperty().set("" + rental.getCardExpiryYear());
        
        estimateLabel.setText("" + rental.getEstimate());
    }*/
    
    /**
     * 
     */
    private void initialiseDatePickers() {
        // Initialise the date pickers
        final Callback<DatePicker, DateCell> startDayCellFactory = 
                new Callback<DatePicker, DateCell>() {
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.compareTo(LocalDate.now()) < 0) {
                                    setDisable(true);
                                }
                            }
                        };
                    }
                };
        startDateField.setDayCellFactory(startDayCellFactory);
        
        final Callback<DatePicker, DateCell> endDayCellFactory = 
                new Callback<DatePicker, DateCell>() {
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.compareTo(LocalDate.now()) < 0 ||
                                    (startDateField.getValue() != null && item.compareTo(startDateField.getValue()) < 0)) {
                                    setDisable(true);
                                }
                            }
                        };
                    }
                };
        endDateField.setDayCellFactory(endDayCellFactory);
    }
    
    /**
     * 
     */
    private void initialiseTimePickers() {
        // Initialise time combo box.
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream(System.getProperty("user.dir")
                    + "/src/ca/ubc/icics/mss/superrent/database/config.properties");
            props.load(fis);
            TIMINGS = props.getProperty("TIMINGS").split(",");
            startTimeField.getItems().addAll((Object[]) TIMINGS);
            endTimeField.getItems().addAll((Object[]) TIMINGS);
        } catch (IOException e) {
            startTimeField.setVisible(false);
            endTimeField.setVisible(false);
            Logger.getLogger(RentReserveFormController.class.getName()).
                    log(Level.SEVERE, null, e);
        }
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
     * Converts the value of the date picker and time selector to a timestamp
     * 
     * @param date Date picker object
     * @param time ComboBox object that holds times in string format.
     * @return Timestamp
     */
    private Timestamp getTimestamp(DatePicker date, ComboBox time) {
        String timeSplit[] = time.getValue().toString().split(":");
        int hour = 0;
        int min = 0;
        
        try {
            hour = Integer.parseInt(timeSplit[0].trim());
            min = Integer.parseInt(timeSplit[1].trim());
        } catch (NumberFormatException ne) {
        }
        LocalDateTime localdatetime = date.getValue().atTime(hour, min);
        return Timestamp.valueOf(localdatetime);
    }
}
