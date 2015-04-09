/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent;

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
public class AdminDashboardController implements Initializable {

    @FXML 
    private Pane content;
  
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void manageUserButtonAction(ActionEvent event) {
        try { 
            
            content.getChildren().clear();
            content.getChildren().add(FXMLLoader.load(getClass().getResource("ManageUserView.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(AdminDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void accessClerkButtonAction(ActionEvent event) {
        try {    
           
            content.getChildren().clear();
            content.getChildren().add(FXMLLoader.load(getClass().getResource("clerk/clerkMainView.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(AdminDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void accessManagerButtonAction(ActionEvent event) {
        try {     
        
            content.getChildren().clear();
            content.getChildren().add(FXMLLoader.load(getClass().getResource("manager/ManagerDashboard.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(AdminDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
