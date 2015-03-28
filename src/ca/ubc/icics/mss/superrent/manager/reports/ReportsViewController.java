/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.manager.reports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Scale;
import javafx.stage.DirectoryChooser;

/**
 * report Controller class
 *
 * @author inderpal
 */
public class ReportsViewController implements Initializable {

    //sql connection variables
    Connection con = null;
    Statement st = null;

    //FXML variables 
    @FXML
    private RadioButton RentButton;
    @FXML
    private RadioButton ReturnButton;
    @FXML
    private Button ExportButton;
    @FXML
    private RadioButton CarButton;
    @FXML
    private RadioButton TruckButton;
    @FXML
    private SplitMenuButton SelectBranch;
    @FXML
    private TableView<Report> RentTable;
    @FXML
    private TableColumn<Report, String> VehicleID;
    @FXML
    private TableColumn<Report, String> Category;
    @FXML
    private GridPane GridFromDate;
    @FXML
    private GridPane GridToDate;
    @FXML
    private Label CostLabel;

    //extra columns for rwnt and reserve report
    private final TableColumn<Report, Date> RentalDate = new TableColumn<>("Rental Date");
    private final TableColumn<Report, String> ReturnDate = new TableColumn<>("Return Date");
    private final TableColumn<Report, String> EstimatedCost = new TableColumn<>("Estimated Cost");
    private final TableColumn<Report, String> Branch = new TableColumn<>("Branch");
    private final TableColumn<Report, String> AmountPaid = new TableColumn<>("Amount Paid");
    private final TableColumn<Report, String> branch = new TableColumn<>("Branch");
    private final TableColumn<Report, String> amountPaid = new TableColumn<>("Amount Paid");

    //lists to collect data
    private final ObservableList<CheckMenuItem> menuItems = FXCollections.observableArrayList();
    private final ObservableList<Report> rentList = FXCollections.observableArrayList();
    private final ObservableList<costReport> costList = FXCollections.observableArrayList();
    private final List<String> citiesSelected = new ArrayList<>();
    private String carTruckSelection;
    private String rentreserveSelection;

    @FXML
    private TableView<CountReport> ReportTableCount;
    @FXML
    private TableColumn<CountReport, String> ReportTableCountBranch;
    @FXML
    private TableColumn<CountReport, String> ReportTableCountCategory;
    @FXML
    private TableColumn<CountReport, String> ReportTableCountCount;

    //vehicle count table
    private final ObservableList<CountReport> countList = FXCollections.observableArrayList();

    @FXML
    private TableView<costReport> costTable;
    @FXML
    private TableColumn<costReport, String> costTableBranch;
    @FXML
    private TableColumn<costReport, String> costTableCost;

    // Create the DatePicker.
    DatePicker datePickerFrom = new DatePicker();
    DatePicker datePickerTo = new DatePicker();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //get sql connection
        try {
            con = DriverManager.getConnection("jdbc:mysql://dbserver.mss.icics.ubc.ca/team02", "team02", "s0ftw@re");
        } catch (SQLException ex) {
            Logger.getLogger(ReportsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //date picker action initialization
        datePickerFrom.setOnAction(event -> {
            LocalDate date = datePickerFrom.getValue();
        });
        datePickerTo.setOnAction(event -> {
            LocalDate date = datePickerTo.getValue();
        });
        GridFromDate.getChildren().add(datePickerFrom);
        GridToDate.getChildren().add(datePickerTo);
        
        //toggle for rent return 
        ToggleGroup RentReturn = new ToggleGroup();
        RentButton.setToggleGroup(RentReturn);
        ReturnButton.setToggleGroup(RentReturn);
        RentButton.setSelected(true);
        rentreserveSelection = "Rent";
        
        //toggle for car truck
        ToggleGroup CarTruck = new ToggleGroup();
        CarButton.setToggleGroup(CarTruck);
        TruckButton.setToggleGroup(CarTruck);
        CarButton.setSelected(true);
        carTruckSelection = "c";
        
        //column name set
        VehicleID.setCellValueFactory(new PropertyValueFactory("VehicleID"));
        Category.setCellValueFactory(new PropertyValueFactory("Category"));
        RentalDate.setCellValueFactory(new PropertyValueFactory("RentDate"));
        ReturnDate.setCellValueFactory(new PropertyValueFactory("ReturnDate"));
        EstimatedCost.setCellValueFactory(new PropertyValueFactory("EstimatedCost"));
        branch.setCellValueFactory(new PropertyValueFactory("Branch"));
        AmountPaid.setCellValueFactory(new PropertyValueFactory("AmountPaid"));
        costTableBranch.setCellValueFactory(new PropertyValueFactory("Branch"));
        costTableCost.setCellValueFactory(new PropertyValueFactory("TotalCost"));
        ReportTableCountBranch.setCellValueFactory(new PropertyValueFactory("Branch"));
        ReportTableCountCategory.setCellValueFactory(new PropertyValueFactory("Category"));
        ReportTableCountCount.setCellValueFactory(new PropertyValueFactory("Count"));
        
        //set items for table
        costTable.setItems(costList);
        ReportTableCount.setItems(countList);       
        RentTable.setItems(rentList);

        //set option for branch selection
        CheckMenuItem menuItem = new CheckMenuItem("Select Branch");
        menuItem.setOnAction((ActionEvent actionEvent) -> {
            SelectBranch.getItems().stream().map((item) -> (CheckMenuItem) item).filter((checkMenuItem) -> (checkMenuItem.isSelected())).forEach((checkMenuItem) -> {
                SelectBranch.setText(checkMenuItem.getText());
            });
        });
        menuItems.add(menuItem);
        SelectBranch.getItems().addAll(menuItems);

    }

    @FXML
    private void RentButtonAction(ActionEvent event) {
        rentreserveSelection = "Rent";
        updateTables();
    }

    @FXML
    private void ReturnButtonAction(ActionEvent event) {
        rentreserveSelection = "Return";
        updateTables();
    }

    @FXML
    private void SelectBranchAction(ActionEvent event) {
        try {
            st = (Statement) con.createStatement();
            
            //clear all lists
            menuItems.clear();
            countList.clear();
            SelectBranch.getItems().clear();
            
            //get branch names
            String SQL = "SELECT * from mbranch";
            ResultSet rs = con.createStatement().executeQuery(SQL);
            while (rs.next()) {
                Report rep = new Report();
                rep.setBranch(rs.getString("branch_city"));
                CheckMenuItem items = new CheckMenuItem(rep.getBranch());
                items.setOnAction((ActionEvent actionEvent) -> {
                    updateTables();
                });
                //add items for splitmenuitems
                menuItems.add(items);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReportsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //add items in branch
        SelectBranch.getItems().addAll(menuItems);
    }

    @FXML
    private void ExportButtonAction(ActionEvent event) throws IOException {

        Writer writer = null;
        try {
            DirectoryChooser dc = new DirectoryChooser();
            File file = dc.showDialog(null);
            if (file != null) {
                file = new File(file.getAbsolutePath() + "/rent.csv");
            }
            // File file = new File("C:\\Users\\warrior\\Desktop\\rent.csv");
            writer = new BufferedWriter(new FileWriter(file));
            String textgap= "Main Report for vehicle\n";
            writer.write(textgap);
            for (Report objReport : rentList) {
                String text = "";
                  if (rentreserveSelection.equals("Rent")) { 
                       
                       text = objReport.getVehicleID()+","+ objReport.getCategory() + "," + objReport.getRentDate()+","+ objReport.getReturnDate()+","+ objReport.getEstimatedCost()+"\n";
                  }
                  else
                  {      text = objReport.getVehicleID()+","+ objReport.getCategory() + objReport.getReturnDate()+","+ objReport.getEstimatedCost()+"\n";
                 
                  }
            

                writer.write(text);
            }
            String textCount= "Count Report\n";
            writer.write(textCount);
             for (CountReport objReport : countList) {
                String text = "";
                text = objReport.getBranch()+","+ objReport.getCategory() + "," + objReport.getCount()+"\n";
                writer.write(text);
            }
            String textCost = "Cost Report\n";
            writer.write(textCost);
              for (costReport objReport : costList) {
                String text = "";
                text = objReport.getBranch()+","+ objReport.getTotalCost()+"\n";
                writer.write(text);
            }
            
        } catch (Exception ex) {
        } finally {
            writer.flush();
            writer.close();
        }

    }

    @FXML
    private void CarButtonAction(ActionEvent event) {
        carTruckSelection = "c";
        updateTables();
    }

    @FXML
    private void TruckButtonAction(ActionEvent event) {
        carTruckSelection = "t";
        updateTables();
    }   

    @FXML
    private void GoButtonAction(ActionEvent event) {
        String dd = String.valueOf(datePickerFrom.getValue());
        String dd2 = String.valueOf(datePickerTo.getValue());
        updateTables();
    }
    
    //update all tables data as per selection
    void updateTables() {
        ///clear cities selected list
        citiesSelected.clear();
        for(MenuItem item : SelectBranch.getItems()) {
            CheckMenuItem checkMenuItem = (CheckMenuItem) item;
            if (checkMenuItem.isSelected()) {
                citiesSelected.add(checkMenuItem.getText());
                String display = String.valueOf(citiesSelected.size());
                SelectBranch.setText(display + " Selected");
            }
        }
        //renttable updation
        if (rentreserveSelection.equals("Rent")) { 
            
            //add columns
            if (!RentTable.getColumns().contains(RentalDate)) {
                RentalDate.setVisible(true);
                ReturnDate.setVisible(true);
                EstimatedCost.setVisible(true);
                RentTable.getColumns().addAll(RentalDate, ReturnDate, EstimatedCost);
            }
            
            try {
                
                int cost = 0;
                st = (Statement) con.createStatement();
                //clear all lists
                rentList.clear();
                costList.clear();
                countList.clear();
                
                //display branch column only if sities selected are more than one
                if (citiesSelected.size() > 1) {
                    if (!RentTable.getColumns().contains(branch)) {
                        branch.setVisible(true);
                        RentTable.getColumns().addAll(branch);
                    }
                }
                
                for (Iterator it = citiesSelected.iterator(); it.hasNext();) {
                    String city = it.next().toString();
                    String branchQuery = "SELECT * FROM mbranch where branch_city = '" + city + "'";
                    ResultSet branchSet = con.createStatement().executeQuery(branchQuery);

                    while (branchSet.next()) {
                        
                        String joinedTables = "SELECT * FROM mvehicle INNER JOIN mrentreserve ON mvehicle.vehicle_id=mrentreserve.vehicle_id AND mvehicle.branch_id = '" + branchSet.getString("branch_id") + "' AND mvehicle.vehicle_type = '" + carTruckSelection + "' AND mrentreserve.rentreservebooking_date between '" + datePickerFrom.getValue() + "' AND '" + datePickerTo.getValue() + "'  ";
                        ResultSet joinedSet = con.createStatement().executeQuery(joinedTables);
                        //for count
                        ResultSet countSet = con.createStatement().executeQuery(joinedTables);

                        while (countSet.next()) {
                            CountReport ObjCountReport = new CountReport();
                            ObjCountReport.setBranch(city);
                            ObjCountReport.setCategory(countSet.getString("vehicle_category"));
                            ObjCountReport.setCount("1");
                            boolean notAvailable = false;
                            int setCount;
                            Iterator<CountReport> it2 = countList.iterator();
                            
                            //calculate vehicle count
                            while (it2.hasNext()) {
                                CountReport ObjCountReport2 = it2.next();
                                if (ObjCountReport2.getBranch().equals(city) && ObjCountReport2.getCategory().equals(countSet.getString("vehicle_category"))) {
                                    setCount = Integer.valueOf(ObjCountReport2.getCount());
                                    setCount++;
                                    notAvailable = true;
                                    ObjCountReport.setCount(String.valueOf(setCount));
                                    countList.remove(ObjCountReport2);
                                    countList.add(ObjCountReport);
                                }
                            }
                            if (!notAvailable) {
                                countList.add(ObjCountReport);
                            }
                        }

                        PreparedStatement costStatement = con.prepareStatement("SELECT SUM(rentreserve_cost) AS pp FROM mvehicle INNER JOIN mrentreserve ON mvehicle.vehicle_id=mrentreserve.vehicle_id AND mvehicle.branch_id = '" + branchSet.getString("branch_id") + "'AND mvehicle.vehicle_type = '" + carTruckSelection + "'");

                        //cost table
                        ResultSet costSet = costStatement.executeQuery();
                        if (costSet != null) {
                            costSet.next();
                            String sum = costSet.getString(1);
                            costReport costReportObj = new costReport();
                            costReportObj.setBranch(city);
                            costReportObj.setTotalCost(sum);
                            costList.add(costReportObj);
                            cost = cost + costSet.getInt(1);
                            CostLabel.setText(String.valueOf(cost));
                        }
                        //maintable
                        while (joinedSet.next()) {
                            Report rep1 = new Report();
                            rep1.setVehicleID(joinedSet.getString("vehicle_id"));
                            rep1.setCategory(joinedSet.getString("vehicle_category"));
                            rep1.setEstimatedCost(joinedSet.getString("rentreserve_cost"));
                            rep1.setRentDate(joinedSet.getDate("rentreservebooking_date"));
                            rep1.setReturnDate(joinedSet.getDate("rentreservereturn_date"));
                            rep1.setBranch(city);
                            rentList.add(rep1);
                        }

                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(ReportsViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        else {
            
            //rent column set
            if (!RentTable.getColumns().contains(RentalDate)) 
            {
                ReturnDate.setVisible(true);
                EstimatedCost.setVisible(true);
                RentTable.getColumns().addAll(RentalDate, EstimatedCost);
            }
            try {
                int cost = 0;
                st = (Statement) con.createStatement();
                rentList.clear();
                costList.clear();
                countList.clear();
                if (citiesSelected.size() > 1) {
                    if (!RentTable.getColumns().contains(branch)) {
                        branch.setVisible(true);
                        RentTable.getColumns().addAll(branch);
                    }
                }
                
                for (Iterator it = citiesSelected.iterator(); it.hasNext();) {
                    String city = it.next().toString();
                    String branchSt = "SELECT * FROM mbranch where branch_city = '" + city + "'";
                    ResultSet branchSet = con.createStatement().executeQuery(branchSt);

                    while (branchSet.next()) {
                       
                        String joinedTable = "SELECT * FROM mvehicle INNER JOIN mrentreserve ON mvehicle.vehicle_id=mrentreserve.vehicle_id INNER JOIN mreturn ON mrentreserve.rentreserve_id=mreturn.rentreserve_id AND mvehicle.branch_id = '" + branchSet.getString("branch_id") + "' AND mvehicle.vehicle_type = '" + carTruckSelection + "'";
                        ResultSet joinedSetForTableUpdate = con.createStatement().executeQuery(joinedTable);
                        ResultSet joinedSetForCalculation = con.createStatement().executeQuery(joinedTable);
                        
                        while (joinedSetForCalculation.next()) {
                            
                            CountReport ObjCountReport = new CountReport();
                            ObjCountReport.setBranch(city);
                            ObjCountReport.setCategory(joinedSetForCalculation.getString("vehicle_category"));
                            ObjCountReport.setCount("1");
                            boolean notAvailable = false;
                            int setCount;
                            
                            Iterator<CountReport> ite = countList.iterator();
                            while (ite.hasNext()) {
                                CountReport ObjCountReport2 = ite.next();
                                if (ObjCountReport2.getBranch().equals(city) && ObjCountReport2.getCategory().equals(joinedSetForCalculation.getString("vehicle_category"))) {
                                    setCount = Integer.valueOf(ObjCountReport2.getCount());
                                    setCount++;
                                    notAvailable = true;
                                    ObjCountReport.setCount(String.valueOf(setCount));
                                    countList.remove(ObjCountReport2);
                                    countList.add(ObjCountReport);
                                }
                            }
                            if (!notAvailable) {
                                countList.add(ObjCountReport);
                            }
                        }
                        
                        PreparedStatement statement = con.prepareStatement("SELECT SUM(rentreserve_cost) AS pp FROM mvehicle INNER JOIN mrentreserve ON mvehicle.vehicle_id=mrentreserve.vehicle_id INNER JOIN mreturn ON mrentreserve.rentreserve_id=mreturn.rentreserve_id AND mvehicle.branch_id = '" + branchSet.getString("branch_id") + "' AND mvehicle.vehicle_type = '" + carTruckSelection + "'");

                        //cost table                  
                        ResultSet result = statement.executeQuery();
                        if (result != null) {
                            result.next();
                            String sum = result.getString(1);
                            costReport costReportObj = new costReport();
                            costReportObj.setBranch(city);
                            costReportObj.setTotalCost(sum);
                            costList.add(costReportObj);
                            cost = cost + result.getInt(1);
                            CostLabel.setText(String.valueOf(cost));
                        }
                        //maintable
                        while (joinedSetForTableUpdate.next()) {
                            Report objReport = new Report();
                            objReport.setVehicleID(joinedSetForTableUpdate.getString("vehicle_id"));
                            objReport.setCategory(joinedSetForTableUpdate.getString("vehicle_category"));
                            objReport.setEstimatedCost(joinedSetForTableUpdate.getString("rentreserve_cost"));
                            objReport.setReturnDate(joinedSetForTableUpdate.getDate("rentreservereturn_date"));
                            objReport.setBranch(city);
                            rentList.add(objReport);
                        }

                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(ReportsViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
