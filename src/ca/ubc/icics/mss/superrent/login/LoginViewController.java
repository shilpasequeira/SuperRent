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
import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBuilder;
import javafx.util.Duration;

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
    
    @FXML
    private Pane p;

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
    private static Main objMain;
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        label_error.setVisible(false);
        
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
        con = SQLConnection.getConnection();
        
        String Santa_Claus_Is_Coming_To_Town =
                "Super Rent a Car System, LLC, better known as Super, is an \n" +
                "American car rental company headquartered in Parsippany-Troy\n" + 
                "Hills, New Jersey, United States.[1] Super, Budget Rent a Car\n" +
                "and Budget Truck Rental are all units of Super Budget Group.\n\n" + 
          
                "Super Budget Group operates the Super brand in North America,\n" +
                "Latin America, the Caribbean, India, Australia and, New Zealand.\n" +
                "Recently Super has acquired Super Europe plc which once was a \n" +
                "separate corporation licensing the Super brand. Super is the\n"+
                "second largest car rental agency inthe world preceded by Hertz\n"+
                "Corporation.\n\n" +

                "Since the late 1970s, Super has featured mainly General Motors\n"+
                "(GM) vehicles such as Chevrolet and Cadillac, but today also\n" +
                "rents popular non-GM brands including Ford and Toyota.\n\n" +

                "Super is a leading rental car provider to the commercial\n" +
                "segment serving business travellers at major airports around\n"+
                "the world, and to leisure travellers at off-airport locations.\n" +
                "Many of the off-airport locations are franchised operations \n" +
                "rather than company-owned and -operated, as is the case with\n"+ 
                "most airport locations. Super was the first car rental business\n" + 
                "to be located at an airport.";
               
        Text textSong;
           textSong = TextBuilder.create()
                   .text(Santa_Claus_Is_Coming_To_Town)
                   .layoutX(50)
                   .textOrigin(VPos.TOP)
                   .textAlignment(TextAlignment.JUSTIFY)
                  // .fill(Color.BLUE)
                   //          .font(Font.font("SansSerif", FontPosture.ITALIC, 18))
                   .build();
         
        TranslateTransition translateTransition = TranslateTransitionBuilder.create()
                .node(textSong)
                .fromY(400)
                .toY(-400)
                .duration(new Duration(101000))
                .interpolator(Interpolator.LINEAR)
                .cycleCount(Timeline.INDEFINITE)
                .build();
                 p.getChildren().add(textSong);
         translateTransition.play();        
    }

    public void setPrevStage(Main obj) {
        this.objMain = obj;
    }
}