/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.login;

import ca.ubc.icics.mss.superrent.Main;
import ca.ubc.icics.mss.superrent.database.SQLConnection;
import static ca.ubc.icics.mss.superrent.validation.Validate.isEmptyTextField;
import static ca.ubc.icics.mss.superrent.validation.md5Check.md5;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
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
    private TextField tf_username;
    @FXML
    private PasswordField tf_password;
    @FXML
    private Label label_error;
  //  @FXML
  //  private Label label_error_password;
    @FXML
    private Button loginButton;

    Connection con = null;
    Statement st = null;
    boolean validUser = false;
    private PreparedStatement preparedStatement;
    boolean sign_in = false;
    private String password = "";
    private String userName = " ";
    private boolean loginSuccessful = false;
    private String userType = "";
    private Object stage;
    private Main objMain;
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        label_error.setVisible(false);
        
        // md5 encryption check //
        
        if (md5(tf_password.getText()).equals(password)) {
            objMain.setUser(userType);
            loginSuccessful = true;
        } else 
        {
            label_error.setVisible(true);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        label_error.setVisible(false);

        tf_password.setDisable(true);
        loginButton.setDisable(true);

        con = SQLConnection.getConnection();

        tf_username.setOnAction((event) -> {

            if (isEmptyTextField(tf_username) == false && tf_username.getText().length() > 2 && tf_username.getText().contains("@") && tf_username.getText().contains(".")) {

                String SQL = "SELECT * from user where username = '" + tf_username.getText() + "'";
                try {
                    ResultSet rs = con.createStatement().executeQuery(SQL);
                    while (rs.next()) {
                        validUser = true;
                        label_error.setVisible(false);
                        tf_password.setDisable(false);
                        password = rs.getString("password");
                        userName = rs.getString("username");
                        userType = rs.getString("role");
                    }

                    if (!validUser) {
                        label_error.setVisible(true);
                    }
                    validUser = false;
                } catch (SQLException ex) {
                    Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if(tf_username.getText().length() > 2) {
                label_error.setVisible(true);
            }
        });

        tf_password.setOnAction((event) -> {
            if (tf_password.getText().length() > 1) {
                loginButton.setDisable(false);
            }

        });
    }

    public void setPrevStage(Main obj) {
        this.objMain = obj;
    }
}