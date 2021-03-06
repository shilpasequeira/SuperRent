/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.manager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author warrior
 */
public class ManagerDashboardController implements Initializable {

    
    @FXML 
    private Pane content;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
 }  
    
    
    @FXML
    private void ReportButtonAction(ActionEvent event) {
        try {           
            content.getChildren().clear();
            content.getChildren().add(FXMLLoader.load(getClass().getResource("reports/ReportsView.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(ManagerDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     @FXML
    private void manageInventoryButtonAction(ActionEvent event) {
        try {   
            content.getChildren().clear();
            content.getChildren().add(FXMLLoader.load(getClass().getResource("managerinventory/InventoryManage.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(ManagerDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void ratecardButtonAction(ActionEvent event) {
        try {  
            content.getChildren().clear();
            content.getChildren().add(FXMLLoader.load(getClass().getResource("ratecard/ratecard.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(ManagerDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
