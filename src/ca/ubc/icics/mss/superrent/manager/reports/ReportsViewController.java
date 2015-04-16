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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
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
import javafx.scene.layout.Pane;
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
    @FXML
    private Label CostLabelTC;
    @FXML
    private Pane errorPaneID;
    //extra columns for rwnt and reserve report
    private final TableColumn<Report, Date> RentalDate = new TableColumn<>("Rental Date");
    private final TableColumn<Report, String> ReturnDate = new TableColumn<>("Return Date");
    private final TableColumn<Report, String> EstimatedCost = new TableColumn<>("Amount Estimated");
    private final TableColumn<Report, String> Branch = new TableColumn<>("Branch");
    private final TableColumn<Report, String> AmountPaid = new TableColumn<>("Customer Paid");
    private final TableColumn<Report, String> branch = new TableColumn<>("Branch");
    private final TableColumn<Report, String> amountPaid = new TableColumn<>("Customer Paid");
    private final TableColumn<Report, String> vehicleName = new TableColumn<>("Name");

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
    List<CountReport> countList = new CopyOnWriteArrayList<CountReport>();

    private final ObservableList<CountReport> countListBound = FXCollections.observableArrayList();

    @FXML
    private TableView<costReport> costTable;
    @FXML
    private TableColumn<costReport, String> costTableBranch;
    @FXML
    private TableColumn<costReport, String> costTableCost;
    @FXML
    private BarChart BarChartID;
    @FXML
    private PieChart PieChartCount;

    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
   // ObservableList<BarChart.Data> BarChartData = FXCollections.observableArrayList();

    // Create the DatePicker.
    DatePicker datePickerFrom = new DatePicker(LocalDate.now());
    DatePicker datePickerTo = new DatePicker(LocalDate.now());

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //get sql connection
       
        errorPaneID.setVisible(false);
        PieChartCount.setTitle("Branch Count");
        PieChartCount.setVisible(false);
        PieChartCount.setData(pieChartData);
        PieChartCount.setLabelLineLength(10);
        PieChartCount.setLegendSide(Side.LEFT);
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
        carTruckSelection = "car";

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
        vehicleName.setCellValueFactory(new PropertyValueFactory("Name"));
        //set items for table
        costTable.setItems(costList);
        ReportTableCount.setItems(countListBound);
        RentTable.setItems(rentList);
        BarChartID.setVisible(false);
        CostLabel.setVisible(false);
        ExportButton.setVisible(false);
        costTable.setVisible(false);
        ReportTableCount.setVisible(false);
        RentTable.setVisible(false);
        CostLabelTC.setVisible(false);
        BarChartID.setTitle("Branch Amount");
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
    public void RentButtonAction(ActionEvent event) {
        rentreserveSelection = "Rent";
        updateTables();
    }

    @FXML
    public void ReturnButtonAction(ActionEvent event) {
        rentreserveSelection = "Return";
        updateTables();
    }

    @FXML
    public void SelectBranchAction(ActionEvent event) {
        try {
               try {
            con = DriverManager.getConnection("jdbc:mysql://dbserver.mss.icics.ubc.ca/team02", "team02", "s0ftw@re");
        } catch (SQLException ex) {
            Logger.getLogger(ReportsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
            st = (Statement) con.createStatement();

            //clear all lists
            menuItems.clear();
            countList.clear();
            SelectBranch.getItems().clear();
            CheckMenuItem items;
            items = new CheckMenuItem("All Branches");

            items.setOnAction((ActionEvent actionEvent) -> {
                updateTables();
            });
            menuItems.add(items);
          
            //get branch names
            String SQL = "SELECT * from branch";
            ResultSet rs = con.createStatement().executeQuery(SQL);
            while (rs.next()) {
                Report rep = new Report();
                rep.setBranch(rs.getString("location"));
                items = new CheckMenuItem(rep.getBranch());
                items.setOnAction((ActionEvent actionEvent) -> {
                    updateTables();
                });
                //add items for splitmenuitems
                menuItems.add(items);
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReportsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //add items in branch
        SelectBranch.getItems().addAll(menuItems);
    }

    @FXML
    public void ExportButtonAction(ActionEvent event) throws IOException {
        String textgapdot = "  . \n";
        String columns;
        Writer writer = null;
        String FileName;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        Calendar cal = Calendar.getInstance();

        if (rentreserveSelection.equals("Rent")) {
            FileName = date.toString().substring(0, 6) + "Rent.csv";
        } else {
            FileName = date.toString().substring(0, 6) + "Return.csv";

        }
        try {
            DirectoryChooser dc = new DirectoryChooser();
            File file = dc.showDialog(null);
            if (file != null) {

                file = new File(file.getAbsolutePath() + "/" + FileName);
            }
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(textgapdot);
            String textgap = " Report for vehicle\n";
            writer.write(textgap);
            if (rentreserveSelection.equals("Rent")) {
                columns = "ID" + "," + "Category" + "," + " Rent Date" + "," + "Name" + "," + "Estimated Price" + "," + "Branch" + "," + "\n";
                writer.write(columns);
            } else {
                columns = "ID" + "," + "Category" + "," + " Return Date" + "," + "Name" + "," + "Estimated Price" + "," + "Branch" + "," + "\n";
                writer.write(columns);
            }
            for (Report objReport : rentList) {
                String text = "";
                if (rentreserveSelection.equals("Rent")) {

                    text = objReport.getVehicleID() + "," + objReport.getCategory() + "," + objReport.getRentDate() + "," + objReport.getName() + "," + objReport.getEstimatedCost() + "," + objReport.getBranch() + "\n";
                } else {
                    text = objReport.getVehicleID() + "," + objReport.getCategory() + "," + objReport.getReturnDate() + "," + objReport.getName() + "," + objReport.getAmountPaid() + "," + objReport.getBranch() + "\n";

                }

                writer.write(text);
            }
            writer.write(textgapdot);
            String textCount = "Count Report\n";
            writer.write(textCount);
            columns = "Branch" + "," + "Category" + "," + "Count" + "," + "\n";
            writer.write(columns);

            for (CountReport objReport : countList) {
                String text;
                text = objReport.getBranch() + "," + objReport.getCategory() + "," + objReport.getCount() + "\n";
                writer.write(text);
            }
            writer.write(textgapdot);
            String textCost = "Cost Report\n";
            writer.write(textCost);
            columns = "Branch" + "," + "Cost" + "," + "\n";
            writer.write(columns);

            for (costReport objReport : costList) {
                String text;
                text = objReport.getBranch() + "," + objReport.getTotalCost() + "\n";
                writer.write(text);
            }
            columns = "Total Cost =" + "," + CostLabel.getText() + "," + "\n";
            writer.write(columns);
        } catch (Exception ex) {
        } finally {

            writer.flush();
            writer.close();
        }
    }

    @FXML
    public void CarButtonAction(ActionEvent event) {
        carTruckSelection = "car";
        updateTables();
    }

    @FXML
    public void TruckButtonAction(ActionEvent event) {
        carTruckSelection = "truck";
        updateTables();
    }

    @FXML
    public void GoButtonAction(ActionEvent event) {

        if (citiesSelected.size() > 1) {
            updateTables();
        }
    }

    //update all tables data as per selection
    public void updateTables() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://dbserver.mss.icics.ubc.ca/team02", "team02", "s0ftw@re");
        } catch (SQLException ex) {
            Logger.getLogger(ReportsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        errorPaneID.setVisible(false);
        PieChartCount.setVisible(true);
        boolean ifallbranchesselected = false;
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        for (MenuItem item : SelectBranch.getItems()) {
            CheckMenuItem checkMenuItem = (CheckMenuItem) item;
            if (checkMenuItem.isSelected()) {
                if (checkMenuItem.getText().equals("All Branches")) {
                    ifallbranchesselected = true;
                    SelectBranch.setText("All Branches");
                }
            }
        }
        citiesSelected.clear();
         
        ///clear cities selected list
        if (ifallbranchesselected == false) {

            for (MenuItem item : SelectBranch.getItems()) {
                CheckMenuItem checkMenuItem = (CheckMenuItem) item;
                if (checkMenuItem.isSelected()) {
                    citiesSelected.add(checkMenuItem.getText());
                    String display = String.valueOf(citiesSelected.size());
                    SelectBranch.setText(display + " Selected");
                }
            }
        } else {
            String SQL = "SELECT * from branch";
            try {
                ResultSet rs = con.createStatement().executeQuery(SQL);
                while (rs.next()) {
                    Report rep = new Report();
                    rep.setBranch(rs.getString("location"));

                    //add items for splitmenuitems
                    citiesSelected.add(rep.getBranch());
                }
            } catch (SQLException ex) {
                Logger.getLogger(ReportsViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //renttable updation
        if (rentreserveSelection.equals("Rent")) {

            try {

                int cost = 0;
                st = (Statement) con.createStatement();
                //clear all lists
                rentList.clear();
                costList.clear();
                countList.clear();
                pieChartData.clear();

                //display branch column only if sities selected are more than one
                // if (citiesSelected.size() > 1)
                {
                    if (!RentTable.getColumns().contains(branch)) {
                        branch.setVisible(true);
                        RentTable.getColumns().addAll(branch, vehicleName);
                    }
                }

                for (Iterator it = citiesSelected.iterator(); it.hasNext();) {
                    String branchName = it.next().toString();

                    String branchQuery = "SELECT * FROM branch where location = '" + branchName + "'";
                    ResultSet branchSet = con.createStatement().executeQuery(branchQuery);

                    while (branchSet.next()) {

                        String joinedTables = "SELECT * FROM vehicle INNER JOIN rent ON vehicle.vehicle_id=rent.vehicle_id AND vehicle.branch_id = '" + branchSet.getString("branch_id") + "' AND vehicle.vehicle_type = '" + carTruckSelection + "'AND rent.start_date_time between '" + datePickerFrom.getValue() + "' AND '" + datePickerTo.getValue().plusDays(1) + "'  ";
                        ResultSet joinedSet = con.createStatement().executeQuery(joinedTables);
                        //for count
                        ResultSet countSet = con.createStatement().executeQuery(joinedTables);

                        while (countSet.next()) {

                            CountReport ObjCountReport = new CountReport();
                            ObjCountReport.setBranch(branchName);
                            ObjCountReport.setCategory(countSet.getString("vehicle_category"));
                            ObjCountReport.setCount("1");
                            boolean notAvailable = false;
                            int setCount;
                            Iterator<CountReport> it2 = countList.iterator();

                            //calculate vehicle count
                            while (it2.hasNext()) {
                                CountReport ObjCountReport2 = it2.next();
                                if (ObjCountReport2.getBranch().equals(branchName) && ObjCountReport2.getCategory().equals(countSet.getString("vehicle_category"))) {
                                    setCount = Integer.valueOf(ObjCountReport2.getCount());
                                    setCount++;
                                    notAvailable = true;
                                    ObjCountReport.setCount(String.valueOf(setCount));
                                    countList.remove(ObjCountReport2);
                                    pieChartData.remove(new PieChart.Data(ObjCountReport.getCategory(), Integer.valueOf(ObjCountReport.getCount())));

                                    countList.add(ObjCountReport);
                                    pieChartData.add(new PieChart.Data(ObjCountReport.getCategory() + " " + branchName, Integer.valueOf(ObjCountReport.getCount())));

                                }
                            }
                            if (!notAvailable) {
                                countList.add(ObjCountReport);
                                pieChartData.add(new PieChart.Data(ObjCountReport.getCategory(), Integer.valueOf(ObjCountReport.getCount())));

                            }

                        }

                        PreparedStatement costStatement = con.prepareStatement("SELECT SUM(estimate) AS pp FROM  vehicle INNER JOIN rent ON vehicle.vehicle_id=rent.vehicle_id AND vehicle.branch_id = '" + branchSet.getString("branch_id") + "' AND vehicle.vehicle_type = '" + carTruckSelection + "'AND rent.start_date_time between '" + datePickerFrom.getValue() + "' AND '" + datePickerTo.getValue().plusDays(1) + "'  ");

                        //cost table
                        ResultSet costSet = costStatement.executeQuery();
                        if (costSet != null) {

                            costSet.next();
                            int sum = costSet.getInt(1);
                            costReport costReportObj = new costReport();
                            costReportObj.setBranch(branchName);
                            costReportObj.setTotalCost(String.valueOf(sum));
                            costList.add(costReportObj);

                            series1.getData().add(new XYChart.Data(branchName, sum));

                            cost = cost + costSet.getInt(1);
                            CostLabel.setText(String.valueOf(cost));
                        }

                        //maintable
                        while (joinedSet.next()) {

                            RentTable.setVisible(true);
                            CostLabel.setVisible(true);
                            ExportButton.setVisible(true);
                            CostLabelTC.setVisible(true);
                            ReportTableCount.setVisible(true);
                            RentalDate.setVisible(true);
                            ReturnDate.setVisible(false);
                            costTable.setVisible(true);
                            EstimatedCost.setVisible(true);
                            //add columns
                            if (!RentTable.getColumns().contains(RentalDate)) {

                                RentTable.getColumns().addAll(RentalDate, ReturnDate, EstimatedCost);
                            }
                            Report rep1 = new Report();
                            rep1.setName(joinedSet.getString("vehicle_name"));
                            rep1.setVehicleID(joinedSet.getString("vehicle_id"));
                            rep1.setCategory(joinedSet.getString("vehicle_category"));
                            rep1.setEstimatedCost(joinedSet.getString("estimate"));
                            rep1.setRentDate(joinedSet.getDate("start_date_time"));
                            rep1.setReturnDate(joinedSet.getDate("end_date_time"));
                            rep1.setBranch(branchName);
                            rentList.add(rep1);
                        }

                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(ReportsViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (rentList.size() >= 1) {
                BarChartID.setVisible(true);
                BarChartID.getData().retainAll();
                BarChartID.getData().add(series1);
            }
        } else {

            try {
                int cost = 0;
                st = (Statement) con.createStatement();
                rentList.clear();
                costList.clear();
                countList.clear();
                if (citiesSelected.size() > 1) {
                    if (!RentTable.getColumns().contains(branch)) {
                        branch.setVisible(true);
                        RentTable.getColumns().addAll(branch, vehicleName);
                    }
                }

                for (Iterator it = citiesSelected.iterator(); it.hasNext();) {
                    String branchName = it.next().toString();

                    String branchSt = "SELECT * FROM branch where location = '" + branchName + "'";
                    ResultSet branchSet = con.createStatement().executeQuery(branchSt);

                    while (branchSet.next()) {

                        String joinedTable = "SELECT * FROM vehicle INNER JOIN rent ON vehicle.vehicle_id=rent.vehicle_id INNER JOIN returns ON returns.rent_id=rent.rent_id AND vehicle.branch_id = '" + branchSet.getString("branch_id") + "' AND vehicle.vehicle_type = '" + carTruckSelection + "'AND returns.transaction_timestamp between '" + datePickerFrom.getValue() + "' AND '" + datePickerTo.getValue().plusDays(1) + "'  ";
                        ResultSet joinedSetForTableUpdate = con.createStatement().executeQuery(joinedTable);
                        ResultSet joinedSetForCalculation = con.createStatement().executeQuery(joinedTable);

                        while (joinedSetForCalculation.next()) {

                            CountReport ObjCountReport = new CountReport();
                            ObjCountReport.setBranch(branchName);
                            ObjCountReport.setCategory(joinedSetForCalculation.getString("vehicle_category"));
                            ObjCountReport.setCount("1");
                            boolean notAvailable = false;
                            int setCount;

                            Iterator<CountReport> ite = countList.iterator();
                            while (ite.hasNext()) {
                                CountReport ObjCountReport2 = ite.next();
                                if (ObjCountReport2.getBranch().equals(branchName) && ObjCountReport2.getCategory().equals(joinedSetForCalculation.getString("vehicle_category"))) {
                                    setCount = Integer.valueOf(ObjCountReport2.getCount());
                                    setCount++;
                                    notAvailable = true;
                                    ObjCountReport.setCount(String.valueOf(setCount));
                                    countList.remove(ObjCountReport2);
                                    countList.add(ObjCountReport);

                                    pieChartData.add(new PieChart.Data(ObjCountReport.getCategory() + " " + branchName, Integer.valueOf(ObjCountReport.getCount())));

                                }
                            }
                            if (!notAvailable) {
                                countList.add(ObjCountReport);
                                pieChartData.add(new PieChart.Data(ObjCountReport.getCategory(), Integer.valueOf(ObjCountReport.getCount())));

                            }
                        }

                        PreparedStatement statement = con.prepareStatement("SELECT SUM(amount) AS pp FROM vehicle INNER JOIN rent ON vehicle.vehicle_id=rent.vehicle_id INNER JOIN returns ON returns.rent_id=rent.rent_id AND vehicle.branch_id = '" + branchSet.getString("branch_id") + "' AND vehicle.vehicle_type = '" + carTruckSelection + "'AND returns.transaction_timestamp between '" + datePickerFrom.getValue() + "' AND '" + datePickerTo.getValue().plusDays(1) + "'  ");

                        //cost table                  
                        ResultSet result = statement.executeQuery();
                        if (result != null) {

                            result.next();
                            int sum = result.getInt(1);
                            costReport costReportObj = new costReport();
                            costReportObj.setBranch(branchName);
                            costReportObj.setTotalCost(String.valueOf(sum));
                            costList.add(costReportObj);
                            series2.getData().add(new XYChart.Data(branchName, sum));
                            cost = cost + result.getInt(1);
                            CostLabel.setText(String.valueOf(cost));
                        }
                        //maintable
                        while (joinedSetForTableUpdate.next()) {
                            costTable.setVisible(true);
                            ReportTableCount.setVisible(true);
                            RentTable.setVisible(true);
                            CostLabel.setVisible(true);
                            ExportButton.setVisible(true);
                            CostLabelTC.setVisible(true);
                            ReturnDate.setVisible(true);
                            RentalDate.setVisible(false);
                            EstimatedCost.setVisible(true);
                            //rent column set
                            if (!RentTable.getColumns().contains(RentalDate)) {

                                RentTable.getColumns().addAll(RentalDate, EstimatedCost);
                            }
                            Report objReport = new Report();
                            objReport.setName(joinedSetForTableUpdate.getString("vehicle_name"));
                            objReport.setVehicleID(joinedSetForTableUpdate.getString("vehicle_id"));
                            objReport.setCategory(joinedSetForTableUpdate.getString("vehicle_category"));
                            objReport.setEstimatedCost(joinedSetForTableUpdate.getString("amount"));
                            objReport.setReturnDate(joinedSetForTableUpdate.getDate("end_date_time"));
                            objReport.setBranch(branchName);
                            rentList.add(objReport);
                        }

                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(ReportsViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (rentList.size() >= 1) {
                BarChartID.setVisible(true);
                BarChartID.getData().retainAll();
                BarChartID.getData().add(series2);
            }
        }
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReportsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (rentList.size() <= 0) {
            PieChartCount.setVisible(false);
            RentTable.setVisible(false);
            ExportButton.setVisible(false);
            ReportTableCount.setVisible(false);
            costTable.setVisible(false);
            BarChartID.setVisible(false);
            CostLabel.setVisible(false);
            CostLabelTC.setVisible(false);
            errorPaneID.setVisible(true);
        }
        countListBound.clear();
        for (Iterator<CountReport> it = countList.iterator(); it.hasNext();) {
            CountReport obj = it.next();
            countListBound.add(obj);

        }
    }

}
