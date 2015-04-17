/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.manager.ratecard;

import ca.ubc.icics.mss.superrent.database.SQLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Zhiming
 */
public class RatecardController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private TableView rcTable;
    @FXML
    private TextField search_field;
    @FXML
    private ChoiceBox branchOption;
    @FXML
    private Button addRates;
    @FXML
    private AnchorPane ancPane;
    @FXML
    private Pane editPage;
    @FXML
    private Pane addPage;
    @FXML
    private Pane mainPage;
    @FXML
    private TextField etype;
    @FXML
    private TextField ehr;
    @FXML
    private TextField edr;
    @FXML
    private TextField ewr;
    @FXML
    private TextField ecategory;
    @FXML
    private TextField ehp;
    @FXML
    private TextField edp;
    @FXML
    private TextField ewp;
    @FXML
    private TextField eaehr;
    @FXML
    private TextField eaedr;
    @FXML
    private TextField atype;
    @FXML
    private ChoiceBox atypebox;
    @FXML
    private TextField ahr;
    @FXML
    private TextField adr;
    @FXML
    private TextField awr;
    @FXML
    private TextField ahp;
    @FXML
    private TextField adp;
    @FXML
    private TextField awp;
    @FXML
    private TextField aaehr;
    @FXML
    private TextField aaedr;
    @FXML 
    private TextField acategory;
    
    private String selectedBranch;
    private TableColumn<RateCard, String> rType;// = new TableColumn<>("type");
    private TableColumn<RateCard, String> rCategory;
    private TableColumn<RateCard, String> rHourlyRate;// = new TableColumn<>("hourlyrate");
    private TableColumn<RateCard, String> rDailyRate;// = new TableColumn<>("dailyrate");
    private TableColumn<RateCard, String> rWeeklyRate;// = new TableColumn<>("weeklyrate");
    private TableColumn<RateCard, String> rDailyPremium;
    private TableColumn<RateCard, String> rWeeklyPremium;
    private TableColumn<RateCard, String> rHourlyPremium;
    private TableColumn<RateCard, String> rAEDailyRate;
    private TableColumn<RateCard, String> rAEHourlyRate;
    
    private ObservableList<RateCard> ratelist = FXCollections.observableArrayList();
    private ObservableList BName = FXCollections.observableArrayList();
    
    //private Object branch;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
 
        rType = new TableColumn("Type");
        //rType.setMinWidth(141.4);
        rType.setCellValueFactory(new PropertyValueFactory("type"));
 
        rCategory = new TableColumn("Category");
        //rCategory.setMinWidth(141.4);
        rCategory.setCellValueFactory(new PropertyValueFactory("Category"));
        
        rHourlyRate = new TableColumn("Hourly Rate");
        //rHourlyRate.setMinWidth(141.4);
        rHourlyRate.setCellValueFactory(new PropertyValueFactory("HourlyRate"));
        
        rDailyRate = new TableColumn("Daily Rate");
        //rDailyRate.setMinWidth(141.4);
        rDailyRate.setCellValueFactory(new PropertyValueFactory("DailyRate"));
        
        rWeeklyRate = new TableColumn("Weekly Rate");
        //rWeeklyRate.setMinWidth(141.4);
        rWeeklyRate.setCellValueFactory(new PropertyValueFactory("WeeklyRate"));
        
        rDailyPremium = new TableColumn("Daily Premium");
        //rDailyPremium.setMinWidth(141.4);
        rDailyPremium.setCellValueFactory(new PropertyValueFactory("DailyPremium"));
        
        rWeeklyPremium = new TableColumn("Weekly Premium");
        //rWeeklyPremium.setMinWidth(141.4);
        rWeeklyPremium.setCellValueFactory(new PropertyValueFactory("WeeklyPremium"));
        
        rHourlyPremium = new TableColumn("Hourly Premium");
        //rHourlyPremium.setMinWidth(141.4);
        rHourlyPremium.setCellValueFactory(new PropertyValueFactory("HourlyPremium"));
        
        rcTable.setItems(ratelist);
        rcTable.getColumns().addAll(rType, rCategory, rHourlyRate, rDailyRate, rWeeklyRate, rDailyPremium, rWeeklyPremium, rHourlyPremium/*, rAEDailyRate, rAEHourlyRate*/);
        branchOption.setVisible(false);
        //System.out.println("abc");
        
        BName.add("CAR");
        BName.add("TRUCK");
        atypebox.setItems(BName);
        atypebox.getSelectionModel().selectFirst();
        selectedBranch = (String)BName.get(0);
        
        String sql2 = "select * from vehicle_category;";
        SQLConnection s = new SQLConnection();
        try (Connection con = s.getConnection();
                Statement stmnt = con.createStatement();) {
            ResultSet rs = stmnt.executeQuery(sql2);
            
            while(rs.next()) {
                RateCard rc = new RateCard();
                rc.setType(rs.getString("vehicle_type"));
                rc.setCategory(rs.getString("vehicle_category"));
                rc.setHourlyRate(rs.getFloat("hourly_rate"));
                rc.setDailyRate(rs.getFloat("daily_rate"));
                rc.setWeeklyRate(rs.getFloat("weekly_rate"));
                rc.setDailyPremium(rs.getFloat("daily_premium"));
                rc.setHourlyPremium(rs.getFloat("hourly_premium"));
                rc.setWeeklyPremium(rs.getFloat("weekly_premium"));
                //rc.setAEDailyRate(rs.getFloat("additional_equipment_daily_rate"));
                //rc.setAEHourlyRate(rs.getFloat("additional_equipment_hourly_rate"));
                
                ratelist.add(rc);
            }
        }catch (SQLException e){
            System.out.println("Initial failed");
        }
    }
    
    public void filter() {
        
        String searchContent = search_field.getText();
        System.out.println(search_field.getText());
        ratelist.clear();
        SQLConnection s = new SQLConnection();
        try (Connection con = s.getConnection();
                ResultSet rs2 = con.createStatement().executeQuery("select * "
                        + "from vehicle_category where vehicle_category like '%"
                        +searchContent+"%';");) {
             
            while(rs2.next()){
                RateCard rc = new RateCard();
                rc.setType(rs2.getString("vehicle_type"));
                rc.setCategory(rs2.getString("vehicle_category"));
                rc.setHourlyRate(rs2.getFloat("hourly_rate"));
                rc.setDailyRate(rs2.getFloat("daily_rate"));
                rc.setWeeklyRate(rs2.getFloat("weekly_rate"));
                rc.setDailyPremium(rs2.getFloat("daily_premium"));
                rc.setHourlyPremium(rs2.getFloat("hourly_premium"));
                rc.setWeeklyPremium(rs2.getFloat("weekly_premium"));
                ratelist.add(rc);
            }
        }catch (SQLException e){
            System.out.println("filter SQL fail");
        }
    }
    
    public void edit(){
        RateCard rc = (RateCard)rcTable.getSelectionModel().getSelectedItem();
        if(rc == null) {
            System.out.println("null object");
        }else{
            mainPage.setVisible(false);
            editPage.setVisible(true);
            etype.setText(rc.Type);
            etype.setEditable(false);
            ecategory.setText(rc.Category);
            ecategory.setEditable(false);
            ehr.setText(Double.toString(rc.HourlyRate));
            edr.setText(Double.toString(rc.DailyRate));
            ewr.setText(Double.toString(rc.WeeklyRate));
            ehp.setText(Double.toString(rc.HourlyPremium));
            edp.setText(Double.toString(rc.DailyPremium));
            ewp.setText(Double.toString(rc.WeeklyPremium));
        }
    }
    
    public void add() {
        mainPage.setVisible(false);
        addPage.setVisible(true);
    }
    
    public void addConfirm() {
        System.out.println(atypebox.getValue());
//        String add_type = atype.getText().toUpperCase();
        String add_type = atypebox.getValue().toString();
        String add_category = acategory.getText();
        double add_daily_rate = Double.valueOf(adr.getText());
        double add_weekly_rate = Double.valueOf(awr.getText());
        double add_hourly_rate = Double.valueOf(ahr.getText());
        double add_daily_premium = Double.valueOf(adp.getText());
        double add_weekly_premium = Double.valueOf(awp.getText());
        double add_hourly_premium = Double.valueOf(ahp.getText());
        SQLConnection s = new SQLConnection();
        try (Connection con = s.getConnection();
                Statement stmnt = con.createStatement();) {

            stmnt.executeUpdate(" insert into vehicle_category(vehicle_type, "
                    + "vehicle_category, daily_rate, weekly_rate, hourly_rate, "
                    + "daily_premium, weekly_premium, hourly_premium) "
                    + "value('"+add_type+"', '"+add_category+"', "+add_daily_rate+
                    ", "+add_weekly_rate+", "+add_hourly_rate+", "+add_daily_premium+
                    ", "+add_weekly_premium+", "+add_hourly_premium+");");

            stmnt.close();
            con.close();
        } catch (SQLException e){
            System.out.println("insert fail");
        }
        refresh();
        addCancel();
    }
    
    public void addCancel() {
        mainPage.setVisible(true);
        addPage.setVisible(false);
    }
    
    public void editConfirm() {
        String edit_type = etype.getText();
        String edit_category = ecategory.getText();
        double edit_hr = Double.valueOf(ehr.getText());
        double edit_dr = Double.valueOf(edr.getText());
        double edit_wr = Double.valueOf(ewr.getText());
        double edit_hp = Double.valueOf(ehp.getText());
        double edit_dp = Double.valueOf(edp.getText());
        double edit_wp = Double.valueOf(ewp.getText());
        SQLConnection s = new SQLConnection();
        try (Connection con = s.getConnection();
                Statement stmnt = con.createStatement();) {
            stmnt.executeUpdate("update vehicle_category set vehicle_type = '" + 
                            edit_type + "', daily_rate = "+edit_dr+
                            ", weekly_rate = "+edit_wr+", hourly_rate = "
                            +edit_hr+", daily_premium = "+edit_dp+", weekly_premium = "
                            +edit_wp+", hourly_premium = "+edit_hp+
                            " where vehicle_category = '"+edit_category+"';");
        } catch (SQLException e){
            System.out.println("update fail");
        }
        refresh();
        editCancel();
    }
    
    public void editCancel() {
        mainPage.setVisible(true);
        editPage.setVisible(false);
    }
    
    public void remove() {
        RateCard rc = (RateCard)rcTable.getSelectionModel().getSelectedItem();
        SQLConnection s = new SQLConnection();
        try (Connection con = s.getConnection();
                Statement stmnt = con.createStatement();) {
            stmnt.executeUpdate(
                    "delete from vehicle_category where vehicle_category = '"
                            + rc.Category+"';");
        }catch (SQLException e){
            System.out.println("update fail");
        }
        
        refresh();
    }
    
    public void refresh() {
        ratelist.clear();
        SQLConnection s = new SQLConnection();
        try (Connection con = s.getConnection();
            ResultSet rs = con.createStatement().executeQuery(
                    "select * from vehicle_category;");) {
            while(rs.next()) {
                RateCard rc = new RateCard();
                rc.setType(rs.getString("vehicle_type"));
                rc.setCategory(rs.getString("vehicle_category"));
                rc.setHourlyRate(rs.getFloat("hourly_rate"));
                rc.setDailyRate(rs.getFloat("daily_rate"));
                rc.setWeeklyRate(rs.getFloat("weekly_rate"));
                rc.setDailyPremium(rs.getFloat("daily_premium"));
                rc.setHourlyPremium(rs.getFloat("hourly_premium"));
                rc.setWeeklyPremium(rs.getFloat("weekly_premium"));
                ratelist.add(rc);
            }
        } catch (SQLException e){
            System.out.println("abc");
        }
    }
}
