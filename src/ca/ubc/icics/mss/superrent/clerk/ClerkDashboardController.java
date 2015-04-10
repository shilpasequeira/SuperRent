/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.clerk;

import ca.ubc.icics.mss.superrent.manager.ratecard.RateCard;
import ca.ubc.icics.mss.superrent.clerk.customer.CustomerViewController;
import ca.ubc.icics.mss.superrent.clerk.customer.*;
import ca.ubc.icics.mss.superrent.clerk.vehiclelist.VehicleListViewController;
import ca.ubc.icics.mss.superrent.database.SQLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;  
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
/**
 * FXML Controller class
 *
 * @author warrior
 */
public class ClerkDashboardController implements Initializable {
      
    @FXML
    private RadioButton carRadio;
    @FXML
    private RadioButton truckRadio;
    @FXML
    private ChoiceBox branch;
    @FXML
    private Pane HP;
    @FXML
    private Pane RC;
    @FXML
    private Pane content;
    @FXML
    private TableView sdata;
    @FXML
    private TextField phoneNum;
    @FXML
    private Label validPhoneNum;
    
    private String selectedBranch;
    private TableColumn<RateCard, String> category;// = new TableColumn<>("type");
    private TableColumn<RateCard, String> hourlyrate;// = new TableColumn<>("hourlyrate");
    private TableColumn<RateCard, String> dailyrate;// = new TableColumn<>("dailyrate");
    private TableColumn<RateCard, String> weeklyrate;// = new TableColumn<>("weeklyrate");
    private TableColumn<RateCard, String> numavail;// = new TableColumn<>("numavail");
    
    private ObservableList RCList = FXCollections.observableArrayList();
    private ObservableList BName = FXCollections.observableArrayList();
    
    public void oPenRC () throws SQLException, ClassNotFoundException {
        System.out.println(selectedBranch);
        RCList.clear();
        try (Connection con = SQLConnection.getConnection();
                ResultSet rs = con.createStatement().executeQuery("select vehicle_category.vehicle_category, "
                        + "hourly_rate, daily_rate, weekly_rate, count(vehicle_id) "
                    + "from vehicle, vehicle_category, branch "
                    + "where vehicle.vehicle_category = vehicle_category.vehicle_category "
                    + "and branch.branch_id = vehicle.branch_id "
                    + "and branch.city like '"+selectedBranch+"' "
                    + "group by vehicle_category.vehicle_category;")){
            while(rs.next()) {
                RateCard rc = new RateCard();
                rc.setCategory(rs.getString("vehicle_category.vehicle_category"));
                rc.setHourlyRate(rs.getFloat("hourly_rate"));
                rc.setDailyRate(rs.getFloat("daily_rate"));
                rc.setWeeklyRate(rs.getFloat("weekly_rate"));
                RCList.add(rc);
            }
        }catch (SQLException e){
            System.out.println("abc");
        }
        content.getChildren().clear();
        HP.setVisible(false);
        RC.setVisible(true);
    }
    
    public void HomePage () {
        HP.setVisible(true);
        RC.setVisible(false);
        content.getChildren().clear();
    }
    
    public void oPenCusView() throws IOException, SQLException {
        String phone_number = phoneNum.getText();
        CustomerViewController.setPhoneNum(phone_number);
        
        try (Connection con = SQLConnection.getConnection();
                ResultSet rs = con.createStatement().executeQuery("select * from "
                        + "customer where phone_no = "+phone_number+";")) {
            
            if(rs.next()){
                validPhoneNum.setText("");
                content.getChildren().clear();
                content.getChildren().add(FXMLLoader.load(Customer.class.getResource("CustomerView.fxml")));
                HP.setVisible(false);
                RC.setVisible(false);
            }
            else{
                System.out.println("no such phone#...");
                validPhoneNum.setText("Nonexist Phone NO");
            }
        } catch (SQLException e) {
            Logger.getLogger(ClerkDashboardController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void openRentView(ActionEvent e) {
        HP.setVisible(false);
        RC.setVisible(false);
        try {           
            content.getChildren().clear();
            VehicleListViewController.setModeRent();
            content.getChildren().add(FXMLLoader.load(getClass().getResource("vehiclelist/VehicleListView.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(ClerkDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void openReserveView(ActionEvent e) {
        HP.setVisible(false);
        RC.setVisible(false);
        try {           
            content.getChildren().clear();
            VehicleListViewController.setModeReserve();
            content.getChildren().add(FXMLLoader.load(getClass().getResource("vehiclelist/VehicleListView.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(ClerkDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void openReportView(ActionEvent e) {
        HP.setVisible(false);
        RC.setVisible(false);
        try {     
            VehicleListViewController.setModeReport();
            content.getChildren().clear();
            content.getChildren().add(FXMLLoader.load(getClass().getResource("vehiclelist/VehicleListView.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(ClerkDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try (Connection con = SQLConnection.getConnection();
                ResultSet rs = con.createStatement().executeQuery("select * from branch;");) {
        
            while(rs.next()) {
                String bname = rs.getString("city");
                BName.add(bname);
            }
        }catch (SQLException e){
            System.out.println("abc");
        }
   
        //branch.getItems().addAll(BName);
        branch.setItems(BName);
        branch.setTooltip(new Tooltip("Select the branch"));
        branch.getSelectionModel().selectFirst();
        selectedBranch = (String)BName.get(0);
        
        //group the two radiobutton
        
        
        sdata.setItems(RCList);
        
        category = new TableColumn<>("Category");
        category.setMinWidth(141);
        category.setCellValueFactory(new PropertyValueFactory("Category"));
        
        dailyrate = new TableColumn<>("Hourly Rate");
        dailyrate.setMinWidth(141);
        dailyrate.setCellValueFactory(new PropertyValueFactory("DailyRate"));
        
        weeklyrate = new TableColumn<>("Weekly Rate");
        weeklyrate.setMinWidth(141);
        weeklyrate.setCellValueFactory(new PropertyValueFactory("WeeklyRate"));
        
        hourlyrate = new TableColumn<>("Hourly Rate");
        hourlyrate.setMinWidth(141);
        hourlyrate.setCellValueFactory(new PropertyValueFactory("HourlyRate"));
        
        numavail = new TableColumn<>("NumAvail");
        numavail.setMinWidth(141);
        numavail.setCellValueFactory(new PropertyValueFactory("NumAvail"));
        
        sdata.setItems(RCList);
        sdata.getColumns().addAll(category, hourlyrate, dailyrate, weeklyrate, numavail);
        
        branch.getSelectionModel().selectedIndexProperty().addListener(new
        ChangeListener<Number>() {
            public void changed(ObservableValue ov,
                Number value, Number new_value) {
                    //label.setText(greetings[new_value.intValue()]);
                    selectedBranch = (String) BName.get(new_value.intValue());
                    System.out.println(selectedBranch);
                    RCList.clear();
                    try (Connection con = SQLConnection.getConnection();
                            ResultSet rs = con.createStatement().executeQuery("select vehicle_category.vehicle_category, hourly_rate, daily_rate, weekly_rate, count(vehicle_id) "
                                            + "from vehicle, vehicle_category, branch "
                                            + "where vehicle.vehicle_category = vehicle_category.vehicle_category "
                                            + "and branch.branch_id = vehicle.branch_id "
                                            + "and branch.city like '"+selectedBranch+"' "
                                            + "group by vehicle_category.vehicle_category;");) {

                        while(rs.next()) {
                            RateCard rc = new RateCard();
                            rc.setCategory(rs.getString("vehicle_category.vehicle_category"));
                            rc.setHourlyRate(rs.getFloat("hourly_rate"));
                            rc.setDailyRate(rs.getFloat("daily_rate"));
                            rc.setWeeklyRate(rs.getFloat("weekly_rate"));
                            rc.setNumAvail(rs.getFloat("count(vehicle_id)"));
                            RCList.add(rc);
                        }
                    }catch (SQLException e){
                        System.out.println("abc");
                    }
            }
        });
    }    
    
}
