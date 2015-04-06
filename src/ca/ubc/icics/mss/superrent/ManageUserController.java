/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent;

import ca.ubc.icics.mss.superrent.database.SQLConnection;
import ca.ubc.icics.mss.superrent.manager.reports.Report;
import ca.ubc.icics.mss.superrent.manager.reports.ReportsViewController;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static java.time.Clock.system;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author warrior
 */
public class ManageUserController implements Initializable {

    @FXML
    private TextField UserNameAdd;

    @FXML
    private TextField FirstNameAdd;

    @FXML
    private TextField LastNameAdd;

    @FXML
    private TextField PasswordAdd;

    @FXML
    private Button SubmitButtonAdd;

    @FXML
    private Button Remove;

    @FXML
    private MenuButton RoleItemAdd;

    @FXML
    private MenuItem ManagerItem;

    @FXML
    private MenuItem ClerkItem;

    @FXML
    private RadioButton AddUserID;

    @FXML
    private RadioButton RemoveUserID;

    @FXML
    private RadioButton EditUser;

    @FXML
    private Pane PaneUserForm;

    @FXML
    private Label SelectUserLabel;

    @FXML
    private Label usernamealreadyexit;

    @FXML
    private Label userAdded;

    @FXML
    private MenuButton UserItem;

    boolean userNameAvailable = false;
    Connection con = null;
    Statement st = null;
    private PreparedStatement preparedStatement;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ToggleGroup RentReturn = new ToggleGroup();
        EditUser.setToggleGroup(RentReturn);
        RemoveUserID.setToggleGroup(RentReturn);
        AddUserID.setToggleGroup(RentReturn);
        setVisibility();
        con = SQLConnection.getConnection();

        UserNameAdd.setOnMouseMoved((event) -> {
            String SQL = "SELECT * from user where username = '" + UserNameAdd.getText() + "'";
            try {
                ResultSet rs = con.createStatement().executeQuery(SQL);
                while (rs.next()) {
                    userNameAvailable = true;
                    usernamealreadyexit.setVisible(true);
                    usernamealreadyexit.setText("user already exit");
                    UserNameAdd.setPromptText("already available - change");
                    UserNameAdd.setStyle("-fx-text-inner-color: red;");
                    FirstNameAdd.setDisable(true);
                    LastNameAdd.setDisable(true);
                    PasswordAdd.setDisable(true);
                    SubmitButtonAdd.setDisable(true);
                }
                if (!userNameAvailable && UserNameAdd.getText() != null && UserNameAdd.getText().length() >= 1 && !UserNameAdd.getText().equals("already available - change")) {

                    if (UserNameAdd.getText().contains("@") && UserNameAdd.getText().contains(".")) {
                        UserNameAdd.setStyle("-fx-text-inner-color: green;");
                        FirstNameAdd.setDisable(false);
                        usernamealreadyexit.setVisible(false);
                    }
                    else
                    {
                         usernamealreadyexit.setVisible(true);
                        usernamealreadyexit.setText("enter valid email id");
                        FirstNameAdd.setDisable(true);
                    }
                }
                userNameAvailable = false;
            } catch (SQLException ex) {
                Logger.getLogger(ReportsViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        FirstNameAdd.setOnMouseMoved((event) -> {
            if (FirstNameAdd.getText().length() <= 0) {
                FirstNameAdd.setPromptText("Enter Name");
                FirstNameAdd.setStyle("-fx-text-inner-color: red;");
            } else if (!FirstNameAdd.getText().equals("Enter Name")) {
                FirstNameAdd.setStyle("-fx-text-inner-color: green;");
                LastNameAdd.setDisable(false);
            }

        });

        LastNameAdd.setOnMouseMoved((event) -> {
            if (LastNameAdd.getText().length() <= 0) {
                LastNameAdd.setPromptText("Enter Name");
                LastNameAdd.setStyle("-fx-text-inner-color: red;");
            } else if (!LastNameAdd.getText().equals("Enter Name")) {
                LastNameAdd.setStyle("-fx-text-inner-color: green;");
                PasswordAdd.setDisable(false);
            }
        });
        PasswordAdd.setOnMouseMoved((event) -> {
            if (PasswordAdd.getText().length() <= 0) {
                PasswordAdd.setPromptText("Enter Password");
                PasswordAdd.setStyle("-fx-text-inner-color: red;");
            } else if (!PasswordAdd.getText().equals("Enter Password")) {
                PasswordAdd.setStyle("-fx-text-inner-color: green;");
                RoleItemAdd.setDisable(false);
            }
        });

    }

    void setVisibility() {
        UserNameAdd.setText("");
        PaneUserForm.setVisible(false);
        FirstNameAdd.setDisable(true);
        FirstNameAdd.setText("");
        LastNameAdd.setDisable(true);
        LastNameAdd.setText("");
        PasswordAdd.setDisable(true);
        PasswordAdd.setText("");
        SubmitButtonAdd.setDisable(true);

        RoleItemAdd.setDisable(true);
        UserItem.setVisible(false);
        SelectUserLabel.setVisible(false);
        usernamealreadyexit.setVisible(false);
        userAdded.setVisible(false);
    }

    @FXML
    public void userVerificationAction(ActionEvent event) {

    }

    @FXML
    public void managerroleAction(ActionEvent event) {
        RoleItemAdd.setText(ManagerItem.getText());
        SubmitButtonAdd.setDisable(false);
    }

    @FXML
    public void clerkroleAction(ActionEvent event) {
        RoleItemAdd.setText(ClerkItem.getText());
        SubmitButtonAdd.setDisable(false);
    }

    @FXML
    public void submitButton(ActionEvent event) {
        RoleItemAdd.setText(ManagerItem.getText());
        SubmitButtonAdd.setDisable(false);
        String SQL = "insert into user values('" + UserNameAdd.getText() + "','" + PasswordAdd.getText() + "','" + FirstNameAdd.getText() + "','" + LastNameAdd.getText() + "','" + RoleItemAdd.getText() + "')";
        try {
            preparedStatement = (PreparedStatement) con.prepareStatement(SQL);
            preparedStatement.executeUpdate();
            setVisibility();
            userAdded.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(ReportsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void addUserAction(ActionEvent event) {
        setVisibility();
        Remove.setVisible(false);
        PaneUserForm.setVisible(true);
    }

    @FXML
    public void removeUserAction(ActionEvent event) {
        setVisibility();
        UserItem.setVisible(true);
        SelectUserLabel.setVisible(true);
    }

    @FXML
    public void editUserAction(ActionEvent event) {
        setVisibility();
        UserItem.setVisible(true);
        SelectUserLabel.setVisible(true);
    }
}
