/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent;

import ca.ubc.icics.mss.superrent.clerk.rentreserve.RentReserveFormController;
import ca.ubc.icics.mss.superrent.clerk.returns.ReturnFormController;
import ca.ubc.icics.mss.superrent.login.LoginViewController;
import ca.ubc.icics.mss.superrent.manager.ManagerDashboardController;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author warrior
 */
public class applicationAccess {

    static Stage primaryStage;

    public void setViewForUser(String role, Stage primaryStage) throws IOException {

        if (role.equals("MANAGER")) {
            this.primaryStage = primaryStage;

            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("manager/ManagerDashboard.fxml"));
            AnchorPane myPane = (AnchorPane) myLoader.load();
            Scene myScene = new Scene(myPane);
            myPane.getChildren().add(FXMLLoader.load(getClass().getResource("LogoutView.fxml")));
            primaryStage.setScene(myScene);
            primaryStage.show();
        }

        if (role.equals("CLERK")) {
            this.primaryStage = primaryStage;
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("clerk/ClerkDashboard.fxml"));
            AnchorPane myPane = (AnchorPane) myLoader.load();
            myPane.getChildren().add(FXMLLoader.load(getClass().getResource("LogoutView.fxml")));
            Scene myScene = new Scene(myPane);
            primaryStage.setScene(myScene);
            primaryStage.show();
        }

        if (role.equals("ADMIN")) {
            this.primaryStage = primaryStage;
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
            myLoader.getClass().getResource("LogoutView.fxml");
            AnchorPane myPane = (AnchorPane) myLoader.load();
            myPane.getChildren().add(FXMLLoader.load(getClass().getResource("LogoutView.fxml")));
            Scene myScene = new Scene(myPane);
            primaryStage.setScene(myScene);
            primaryStage.show();
        }

    }

    @FXML
    private void logOutButton(ActionEvent event) throws IOException {
        this.primaryStage = primaryStage;
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("login/LoginView.fxml"));
        AnchorPane myPane = (AnchorPane) myLoader.load();
        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);
        primaryStage.show();
    }

}
