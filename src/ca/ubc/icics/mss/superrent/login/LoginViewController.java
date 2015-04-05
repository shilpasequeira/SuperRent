/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.login;

import ca.ubc.icics.mss.superrent.database.SQLConnection;
import static ca.ubc.icics.mss.superrent.validation.Validate.isEmptyTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;

/**
 * FXML Controller class
 *
 * @author anmolpreet sandhu
 */
public class LoginViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label welcome_text;
    @FXML
    private AnchorPane Home_pg;
    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField tf_password;
    @FXML
    private Label label_error;
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
       
        tf_username.setPromptText("Your Username");
        tf_password.setPromptText("Your password");
  //      Parent home_parent = FXMLLoader.load(getClass().getResource("ca.ubc.icics.mss.superrent/MainView.fxml"));
  //      Scene home_scene = new Scene(home_parent);
  //      Stage login_stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        if(isValidValues() != false)
        {
    //        login_stage.hide();
      //      login_stage.setScene(home_scene);
        //    login_stage.show();
             System.out.println("Let's gooooooo!");
        }
        else {
            tf_username.clear();
            tf_password.clear();
            label_error.setText("Invalid USERNAME/PASSWORD combination, Please try again!");
        }
    }
    
    private boolean isValidValues() {
        boolean sign_in = false;
     //   System.out.println("SELECT * from accounts where USERNAME= " + "'" + tf_username.getText() + "'"
     //           + " AND PASSWORD= " + "'" + tf_password.getText() + "'" );
      //  Connection con = null;
     //   Statement smt = null;
       // label_error.setText(tf_username.getText());
        if(isEmptyTextField(tf_username) != false) // && isEmptyTextField(tf_password) != true)
        {
             /*
            usin try-with-resources to avoid closing resources
        */
        
            try (Connection con = SQLConnection.getConnection();
                Statement smt = con.createStatement();
                ResultSet rs = smt.executeQuery("SELECT username,password from user where username= " + "'" + tf_username.getText() + "'"
                + " AND password= " + "'" + tf_password.getText() + "'" );
                ) {
            /*
            con = DriverManager.getConnection("jdbc:mysql://localhost/test","root","");
            con.setAutoCommit(sign_in);
            
            System.out.println("Database connected successfully");
            smt = con.createStatement();
            
            ResultSet rs = smt.executeQuery("SELECT * from accounts where USERNAME= " + "'" + tf_username.getText() + "'"
                + " AND PASSWORD= " + "'" + tf_password.getText() + "'" );
            */
                
            while(rs.next()) {
                if(rs.getString("username") != null && rs.getString("password") != null) {
                    String username = rs.getString("username");
                    System.out.println("username = " + username);
                    String password = rs.getString("password");
                    System.out.println("password = " + password);
                    sign_in = true;
                    
                }
            }
           // rs.close();
           // smt.close();
           // con.close();
             } catch(SQLException e) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
           // System.err.println(e.getClass().getName() + ": "+e.getMessage());
            //    System.exit(0);
             }
               label_error.setText("Successfully done!");
        }
        else {
            label_error.setText("Please insert required values.");
        }
        
        return sign_in;
    }
            
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
