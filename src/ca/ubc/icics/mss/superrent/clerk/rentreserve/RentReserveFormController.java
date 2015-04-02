/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.clerk.rentreserve;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author warrior
 */
public class RentReserveFormController implements Initializable {
    private static final int RENT = 0;
    private static final int RESERVE = 1;
    
    @FXML private Label firstNameLabel;
    @FXML private TextField firstNameField;
    @FXML private Label lastNameLabel;
    @FXML private TextField lastNameField;
    @FXML private Label addressLabel;
    @FXML private TextField addressField;
    @FXML private Label cityLabel;
    @FXML private TextField cityField;
    @FXML private Label pincodeLabel;
    @FXML private TextField pincodeField;
    @FXML private Label phoneLabel;
    @FXML private TextField phoneField;
    @FXML private Label driversLicenseLabel;
    @FXML private TextField driversLicenseField;
    @FXML private Label creditCardLabel;
    @FXML private TextField creditCardField;
    @FXML private Label creditExpiryLabel;
    @FXML private TextField creditExpiryField;
    @FXML private CheckBox applyMembershipCheckBox;
    @FXML private CheckBox isRoadStarCheckBox;
    @FXML private Label startDateLabel;
    @FXML private DatePicker startDateField;
    @FXML private Label startTimeLabel;
    @FXML private ChoiceBox startTimeField;
    @FXML private Label endDateLabel;
    @FXML private DatePicker endDateField;
    @FXML private Label endTimeLabel;
    @FXML private ChoiceBox endTimeField;
    @FXML private Label additionalEquipmentLabel;
    @FXML private ChoiceBox additionalEquipmentField;
    @FXML private Button estimateButton;
    @FXML private Label estimateLabel;

    private int mode = RENT;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (mode == RENT) {
            this.driversLicenseLabel.setVisible(true);
            this.driversLicenseField.setVisible(true);
            this.creditCardLabel.setVisible(true);
            this.creditCardField.setVisible(true);
            this.creditExpiryLabel.setVisible(true);
            this.creditExpiryField.setVisible(true);
        }
        
        
        
    }
    
    public void setModeRent() {
        this.mode = RENT;
    }
    
    public void setModeReserve() {
        this.mode = RESERVE;
    }
    
}
