/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent;

import ca.ubc.icics.mss.superrent.database.SQLConnection;
import ca.ubc.icics.mss.superrent.manager.ManagerDashboardController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author warrior
 */
public class MainViewController implements Initializable {
    
    @FXML
    private AnchorPane AnchorPaneAdmin;
    
    @FXML
    private Button ManageUserButton;
    
    @FXML
    private Button AccessManagerButton;
    
    @FXML
    private Button AccessCustomerButton;
    
     @FXML 
    private Pane content;
    
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            
    }   
    
     @FXML
    public void managerUserButtonAction(ActionEvent event) {
      try {
            // TODO
            content.getChildren().add(FXMLLoader.load(getClass().getResource("ManageUserView.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(ManagerDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
     @FXML
    public void accessManagerButtonAction(ActionEvent event) {
        
    }
    
     @FXML
    public void accessCustomerButtonAction(ActionEvent event) {
        
    }
    
}
