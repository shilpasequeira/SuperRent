/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent;

import ca.ubc.icics.mss.superrent.login.LoginViewController;
import ca.ubc.icics.mss.superrent.manager.ManagerDashboardController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author warrior
 */
public class applicationAccess {

    private Stage primaryStage;
   
    public void setViewForUser(String role) throws IOException {
        
        
        if(role.equals("MANAGER"))
        {
        this.primaryStage = new Stage();
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("manager/ManagerDashboard.fxml"));
        AnchorPane myPane = (AnchorPane) myLoader.load();        
        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);
        primaryStage.show();
        }
        
        if(role.equals("CLERK"))
        {
        this.primaryStage = new Stage();
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("clerk/ClerkDashboard.fxml"));
        AnchorPane myPane = (AnchorPane) myLoader.load();        
        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);
        primaryStage.show();
        }
        
        if(role.equals("ADMIN"))
        {
        this.primaryStage = new Stage();
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
        AnchorPane myPane = (AnchorPane) myLoader.load();        
        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);
        primaryStage.show();
        }

    }

}
