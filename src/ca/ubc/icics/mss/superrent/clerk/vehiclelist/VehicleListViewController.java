/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.clerk.vehiclelist;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Avinash
 */
public class VehicleListViewController implements Initializable {
@FXML 
   private SplitMenuButton SelectBranch;
    
    @FXML 
   private SplitMenuButton SelectCategory;
    
    @FXML
    private TabPane tabPane;
    
    @FXML
    private Tab tabCar; 
    
    @FXML
    private Tab tabTruck;
    
    @FXML
    private Button Overdue;
    
    @FXML
    private Button Sale;
    
    @FXML
    private Button Available;
            
    @FXML
    private Button Search;        
    
    @FXML
    private ComboBox branch;
    
    @FXML
    private ComboBox category;
    
    
    
    private TableView table; 
    private String currentButton;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    VehicleRepository Access = new VehicleRepository();
    ArrayList<String>  list =   Access.getNames("Branch");
    ObservableList<String> options  = FXCollections.observableArrayList(list);
    branch.setItems(options);
    ArrayList<String> listc = Access.getNames("Category");
    ObservableList<String> optionsc  = FXCollections.observableArrayList(listc);
    category.setItems(optionsc);
    
      
    table = Access.getTableAvailabiltyView("All Locations","All Category","CAR");
    table.setPrefWidth(670);
    tabCar.setContent(table);
    currentButton = "Available";
    System.out.println (branch.getValue());
    
    
 }  
    
//select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.city,r.start_date_time,r.end_date_time,(current_date - date(end_date_time)) no_days,timediff(current_time,time(end_date_time)) time_due  from rent r inner join vehicle v on ( v.vehicle_id = r.vehicle_id) inner join branch b on(v.branch_id = b.branch_id) left outer join returns re on (r.rent_id = re.rent_id) where re.return_id is null and date(end_date_time) <= current_date and time(end_date_time) < current_time and v.vehicle_type = 'CAR';

    
    @FXML
    private void OverdueButtonAction(ActionEvent event) {
       
       currentButton = Overdue.getText();
       VehicleRepository OverdueAccess = new VehicleRepository();
       table = OverdueAccess.getTableOverdueView("All Locations","All Category","CAR");
       table.setPrefWidth(670);
       tabCar.setContent(table);
        
    }
    
    
    @FXML
    private void SalesButtonAction(ActionEvent event) {
       currentButton = Sale.getText();
       VehicleRepository SalesAccess = new VehicleRepository();
       table = SalesAccess.getTableSalesView("All Locations","All Category","CAR");
       table.setPrefWidth(670);
       tabCar.setContent(table);
    }

    
    @FXML
    private void AvailabiltyButtonAction(ActionEvent event) {
       currentButton = Available.getText();
       VehicleRepository AvailabilityAccess = new VehicleRepository();
       table = AvailabilityAccess.getTableAvailabiltyView("All Locations","All Category","CAR");
       table.setPrefWidth(670);
       tabCar.setContent(table);
    }
    
     @FXML
    private void SearchButtonAction(ActionEvent event) {
         String b_name,cat;
        if ( branch.getValue() == null)
              b_name = "All Locations";
        else
              b_name = branch.getValue().toString();
        if ( category.getValue() == null )
             cat = "All Category";
        else
             cat = category.getValue().toString();
           
      
           
       VehicleRepository SearchAccess = new VehicleRepository();
       if ( currentButton.equals("Available"))
       table = SearchAccess.getTableAvailabiltyView(b_name,cat,"CAR");
       else if ( currentButton.equals("Overdue"))
       table = SearchAccess.getTableOverdueView(b_name,cat,"CAR");
       else if ( currentButton.equals("For Sale"))
       table = SearchAccess.getTableSalesView(b_name,cat,"CAR");
       table.setPrefWidth(670);
       tabCar.setContent(table);
    }
    
}
