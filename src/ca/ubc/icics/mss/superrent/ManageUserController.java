/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent;

import ca.ubc.icics.mss.superrent.database.SQLConnection;
import ca.ubc.icics.mss.superrent.manager.reports.Report;
import ca.ubc.icics.mss.superrent.manager.reports.ReportsViewController;
import static ca.ubc.icics.mss.superrent.validation.md5Check.md5;
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
    private TextField cityTB;
    @FXML
    private TextField LocationTB;

    @FXML
    private TextField LastNameAdd;

    @FXML
    private TextField PasswordAdd;

    @FXML
    private Button SubmitButtonAdd;

    @FXML
    private Button Remove;

    @FXML
    private Button ModifyButtonAdd;
    @FXML
    private Button EditButton;

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
    private RadioButton AddBranch;

    @FXML
    private Pane PaneUserForm;

    @FXML
    private Label SelectUserLabel;

    @FXML
    private Label usernamealreadyexit;

    @FXML
    private Label userAdded;

    @FXML
    private Label BranchError;
    ;

    @FXML
    private TextField SearchUserTextBox;
    @FXML
    private Label UserNameLabel;

    boolean userNameAvailable = false;
    Connection con = null;
    Statement st = null;
    private PreparedStatement preparedStatement;

    @FXML
    private Pane BranchPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ToggleGroup RentReturn = new ToggleGroup();
        EditUser.setToggleGroup(RentReturn);
        RemoveUserID.setToggleGroup(RentReturn);
        AddUserID.setToggleGroup(RentReturn);
        AddBranch.setToggleGroup(RentReturn);
        setVisibility();

        BranchPane.setVisible(false);
        UserNameAdd.setOnMouseMoved((event) -> {
            SQLConnection obj = new SQLConnection();
            con = obj.getConnection();
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
                    } else {
                        usernamealreadyexit.setVisible(true);
                        usernamealreadyexit.setText("enter valid email id");
                        FirstNameAdd.setDisable(true);
                    }
                }
                userNameAvailable = false;
                con.close();
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
        SearchUserTextBox.setVisible(false);
        UserNameLabel.setVisible(false);
        RoleItemAdd.setDisable(true);
        EditButton.setVisible(false);
        SelectUserLabel.setVisible(false);
        usernamealreadyexit.setVisible(false);
        userAdded.setVisible(false);
        Remove.setVisible(false);
        ModifyButtonAdd.setVisible(false);
        ModifyButtonAdd.setDisable(true);
        BranchPane.setVisible(false);
    }

    @FXML
    public void branchAddAction(ActionEvent event) {

        String SQL = "insert into branch(location) values('" + cityTB.getText() + "," + LocationTB.getText() + "')";
        try {
            SQLConnection obj = new SQLConnection();
            con = obj.getConnection();
            preparedStatement = (PreparedStatement) con.prepareStatement(SQL);
            preparedStatement.executeUpdate();
            BranchError.setText("Branch Added");
            con.commit();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReportsViewController.class.getName()).log(Level.SEVERE, null, ex);
            BranchError.setText("Location already available");
        }

    }

    @FXML
    public void branchRadioAddAction(ActionEvent event) {
        setVisibility();
        BranchPane.setVisible(true);
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
        String SQL = "insert into user values('" + UserNameAdd.getText() + "','" + md5(PasswordAdd.getText()) + "','" + FirstNameAdd.getText() + "','" + LastNameAdd.getText() + "','" + RoleItemAdd.getText() + "')";
        try {
            SQLConnection obj = new SQLConnection();
            con = obj.getConnection();
            preparedStatement = (PreparedStatement) con.prepareStatement(SQL);
            preparedStatement.executeUpdate();
            setVisibility();
            userAdded.setVisible(true);
            con.commit();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReportsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void addUserAction(ActionEvent event) {
        setVisibility();
        Remove.setVisible(false);
        PaneUserForm.setVisible(true);
        ModifyButtonAdd.setVisible(false);
    }

    @FXML
    public void removeUserAction(ActionEvent event) {
        setVisibility();

        SelectUserLabel.setVisible(true);
        SearchUserTextBox.setVisible(true);
        UserNameLabel.setVisible(false);

        Remove.setVisible(true);

    }

    @FXML
    public void removeButtonAction(ActionEvent event) {

        UserNameLabel.setVisible(false);
        boolean userFound = false;
        String SQL = "SELECT * from user where username = '" + SearchUserTextBox.getText() + "'";
        try {
            SQLConnection obj = new SQLConnection();
            con = obj.getConnection();
            ResultSet rs = con.createStatement().executeQuery(SQL);
            while (rs.next()) {
                userFound = true;
                String SQ = "delete from user where username = '" + SearchUserTextBox.getText() + "'";
                con.createStatement().executeUpdate(SQ);
                UserNameLabel.setVisible(true);
                UserNameLabel.setText("User Deleted");
                SearchUserTextBox.clear();
            }
            if (!userFound) {
                UserNameLabel.setVisible(true);
                UserNameLabel.setText("User Not Found");
            }
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(ReportsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void editUserAction(ActionEvent event) {
        setVisibility();
        SubmitButtonAdd.setVisible(false);
        SelectUserLabel.setVisible(true);
        SearchUserTextBox.setVisible(true);
        EditButton.setVisible(true);
        ModifyButtonAdd.setVisible(true);
        ModifyButtonAdd.setDisable(false);
        SubmitButtonAdd.setDisable(true);
    }

    @FXML
    public void modifyButtonAction(ActionEvent event) {

        UserNameLabel.setVisible(false);

        try {
            SQLConnection obj = new SQLConnection();
            con = obj.getConnection();
            String SQLFN = "update user set first_name = '" + FirstNameAdd.getText() + "' where username = '" + UserNameAdd.getText() + "' ";
            String SQLLN = "update user set last_name = '" + LastNameAdd.getText() + "' where username = '" + UserNameAdd.getText() + "' ";
            String SQLPW = "update user set password = '" + PasswordAdd.getText() + "' where username = '" + UserNameAdd.getText() + "' ";
            String SQLR = "update user set role = '" + RoleItemAdd.getText() + "' where username = '" + UserNameAdd.getText() + "' ";

            con.createStatement().executeUpdate(SQLFN);
            con.createStatement().executeUpdate(SQLLN);
            con.createStatement().executeUpdate(SQLPW);
            con.createStatement().executeUpdate(SQLR);
            setVisibility();
            userAdded.setVisible(true);
            con.commit();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(ReportsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    public void editButtonAction(ActionEvent event) {

        UserNameLabel.setVisible(false);
        boolean userFound = false;
        String SQL = "SELECT * from user where username = '" + SearchUserTextBox.getText() + "'";
        try {
            SQLConnection obj = new SQLConnection();
            con = obj.getConnection();
            ResultSet rs = con.createStatement().executeQuery(SQL);
            while (rs.next()) {
                userFound = true;
                UserNameLabel.setVisible(true);
                UserNameLabel.setText("User Found");
                PaneUserForm.setVisible(true);

                UserNameAdd.setText(SearchUserTextBox.getText());
                UserNameAdd.setDisable(true);
                FirstNameAdd.setDisable(false);
                FirstNameAdd.setText(rs.getString("first_name"));
                LastNameAdd.setDisable(false);
                LastNameAdd.setText(rs.getString("last_name"));
                PasswordAdd.setDisable(false);
                PasswordAdd.setText(rs.getString("password"));
                RoleItemAdd.setText(rs.getString("role"));

                SearchUserTextBox.clear();
                con.close();
            }
            if (!userFound) {
                UserNameLabel.setVisible(true);
                UserNameLabel.setText("User Not Found");
            }

        } catch (SQLException ex) {
            Logger.getLogger(ReportsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
