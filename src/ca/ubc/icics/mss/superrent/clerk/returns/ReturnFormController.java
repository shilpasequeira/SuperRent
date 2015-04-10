/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.clerk.returns;

import ca.ubc.icics.mss.superrent.clerk.customer.Customer;
import ca.ubc.icics.mss.superrent.clerk.rentreserve.Rent;
import ca.ubc.icics.mss.superrent.clerk.vehiclelist.Vehicle;
import ca.ubc.icics.mss.superrent.validation.Validate;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author warrior
 */
public class ReturnFormController implements Initializable {
    private Return returnModel;
    private static Rent rentModel;
    private String[] PAY_METHOD = {"CASH", "CREDIT"};
    
    @FXML private AnchorPane anchorPane;
    @FXML private Label errorMessageField;
    @FXML private Label returnInvoiceNumber;
    @FXML private Label startDateTime;
    @FXML private Label endDateTime;
    @FXML private Label rentalTimePeriod;
    @FXML private Label vehicleType;
    @FXML private Label vehiclePlateNo;
    @FXML private Label vehicleName;
    @FXML private Label grossTotal;
    @FXML private Label additonalEquipmentCost;
    @FXML private Label insuranceCost;
    @FXML private Label grandTotal;
    @FXML private CheckBox payWithPointsCheckbox;
    @FXML private Label pointsUsed;
    @FXML private TextField odometerReadingField;
    @FXML private Label odometerReadingErrorLabel;
    @FXML private CheckBox tankFullCheckbox;
    @FXML private Label paymentMethodLabel;
    @FXML private ComboBox paymentMethod;
    @FXML private Label paymentMethodErrorLabel;
    @FXML private Button paymentSuccessFullButton;
    @FXML private Button printButton;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Get a return model with the rent ID.
        returnModel = new Return(rentModel);
        
        // Initialise all fields with rental info.
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        startDateTime.setText(format.format(rentModel.getStartDateTime()));
        endDateTime.setText(format.format(new Date()));
        rentalTimePeriod.setText(returnModel.getRentalTimePeriod());
        
        Vehicle vehicleModel = rentModel.getVehicle();
        vehicleType.setText(vehicleModel.getType());
        vehiclePlateNo.setText(vehicleModel.getPlateNumber());
        vehicleName.setText(vehicleModel.getName());
        
        paymentMethod.getItems().addAll((Object[]) PAY_METHOD);
        
        // If customer is a club member, and has enough points, show the
        // pay with points checkbox.
        Customer customerModel = rentModel.getCustomer();
        if (customerModel.getIsClubMember() && customerModel.getPoints() > 0) {
            payWithPointsCheckbox.setVisible(true);
            pointsUsed.setVisible(true);
            pointsUsed.setText("You have " + customerModel.getPoints() + " points.");
        }
        
        // Display the calculated amount in the text box
        grossTotal.setText(returnModel.getGrossTotal() + " CAD");
        additonalEquipmentCost.setText(returnModel.getEquipmentCost() + " CAD");
        insuranceCost.setText(returnModel.getInsuranceCost() + " CAD");
        grandTotal.setText(returnModel.calculateGrandTotal() + " CAD");
    }
    
    /**
     * 
     * @param rentalID
     */
    public static void setRentID(int rentalID) {
        rentModel = new Rent(rentalID);
    }
    
    @FXML private void payWithPointsChecked() {
        returnModel.setIsPayWithPoints(payWithPointsCheckbox.isSelected());
        int grandTotalCost = returnModel.calculateGrandTotal();
        
        grandTotal.setText(grandTotalCost + " CAD");
        pointsUsed.setText(" - " + returnModel.getPointsUsed() + " points");
        
        if (grandTotalCost == 0) {
            paymentMethod.setDisable(true);
        }
    }
    
    @FXML private void paymentSuccessFullButtonPressed() {
        boolean valid = false;
        int grandTotalCost = returnModel.calculateGrandTotal();
        
        if (grandTotalCost > 0) {
            valid = !Validate.isEmptyTextField(odometerReadingField, odometerReadingErrorLabel) &&
                    !Validate.isEmptyComboBox(paymentMethod, paymentMethodErrorLabel);
        } else {
            valid = !Validate.isEmptyTextField(odometerReadingField, odometerReadingErrorLabel);
        }
        
        if (valid) {
            errorMessageField.setVisible(false);
            //grandTotal.setText(returnModel.getGrandTotal() + " CAD");
            //pointsUsed.setText(returnModel.getPointsUsed() + " points used.");
            
            String payMethod = "NONE";
            if (paymentMethod.getValue() != null) {
                payMethod = paymentMethod.getValue().toString();
            }
            
            returnModel.confirmReturn(
                    Integer.parseInt(odometerReadingField.getText().trim()), 
                    tankFullCheckbox.selectedProperty().getValue(), true,
                    payMethod);
            
            printButton.setVisible(true);
            paymentSuccessFullButton.setDisable(true);
        } else {
            errorMessageField.setText("Please check the errors.");
        }
    }
    
    @FXML private void printButtonPressed() {
        Printer printer = Printer.getDefaultPrinter();
        Stage dialogStage = new Stage(StageStyle.DECORATED);            
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if (job != null) {                    
            boolean showDialog = job.showPageSetupDialog(dialogStage);
            if (showDialog) {                        
                /*anchorPane.setScaleX(0.60);
                anchorPane.setScaleY(0.60);
                anchorPane.setTranslateX(-220);
                anchorPane.setTranslateY(-70);*/

                boolean success = job.printPage(anchorPane);
                if (success) {
                    job.endJob(); 
                } 
                /*(anchorPane.setTranslateX(0);
                anchorPane.setTranslateY(0);               
                anchorPane.setScaleX(1.0);
                anchorPane.setScaleY(1.0);*/                                       
            }    
        }
    }
}
