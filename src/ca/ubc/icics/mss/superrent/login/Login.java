/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.login;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 *
 * @author anmolpreet sandhu
 */
public class Login extends Application {
        /*
        Image img = new Image("http://goo.gl/YsuGV");
        ImageView view = new ImageView(img);
        root.getChildren().add(view);
        */
         @Override
    public void start(final Stage primaryStage) throws Exception {
        
                   
                    Parent root = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
                    Scene login_Scene = new Scene(root, 430, 400);
                    primaryStage.setScene(login_Scene);
                    primaryStage.show();
                }
            
 
    public static void main(String[] args) {
        launch(args);
    }
}  

