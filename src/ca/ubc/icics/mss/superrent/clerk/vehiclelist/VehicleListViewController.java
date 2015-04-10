/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.clerk.vehiclelist;

import ca.ubc.icics.mss.superrent.database.SQLConnection;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import ca.ubc.icics.mss.superrent.clerk.rentreserve.RentReserveFormController;
import javafx.scene.control.Label;
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
    
    @FXML private ComboBox start_time;
    
    @FXML private ComboBox end_time;
    
    @FXML private DatePicker startDateField;
    @FXML private DatePicker endDateField;
    @FXML private Label Title;
    
    private TableView table;
    private TableView tableTruck;
    private String currentButton;
    
    private static final String RENT = "RENT";
    private static final String RESERVE = "RESERVE";
    private static final String REPORT = "REPORT";
    private static String mode = REPORT;
    private String[] TIMINGS;
    
    
    public Statement statement = null;
    public PreparedStatement prstatement = null;
    public ResultSet rs = null;
    public String que = null;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initialiseTimePickers();
        initialiseDatePickers();
        VehicleRepository Access = new VehicleRepository();
        ArrayList<String>  list =   Access.getNames("Branch");
        ObservableList<String> options  = FXCollections.observableArrayList(list);
        branch.setItems(options);
        ArrayList<String> listc = Access.getNames("Category");
        ObservableList<String> optionsc  = FXCollections.observableArrayList(listc);
        category.setItems(optionsc);
    
      
        table = getTableAvailabiltyView("All Locations","All Category","CAR");
        table.setPrefWidth(670);
        tabCar.setContent(table);
        tableTruck = getTableAvailabiltyView("All Locations","All Category","TRUCK");
        tableTruck.setPrefWidth(670);
        tabTruck.setContent(tableTruck);
        currentButton = "Available";
        if (mode == RENT || mode == RESERVE )
        {
            Available.setVisible(false);
            Overdue.setVisible(false);
            Sale.setVisible(false);
            Title.setVisible(true);
        }
        else
        {
            Available.setVisible(true);
            Overdue.setVisible(true);
            Sale.setVisible(true);
            Title.setVisible(false);
        }
        
        
    }
    
    public static void setModeRent() {
        mode = RENT;
    }
    
    public static void setModeReserve() {
        mode = RESERVE;
    }
    
    public static void setModeReport() {
        mode = REPORT;
    }
    
    @FXML
    private void OverdueButtonAction(ActionEvent event) {
       
       currentButton = Overdue.getText();
       table = getTableOverdueView("All Locations","All Category","CAR");
       table.setPrefWidth(670);
       tabCar.setContent(table);
       tableTruck = getTableOverdueView("All Locations","All Category","CAR");
       tableTruck.setPrefWidth(670);
       tabTruck.setContent(tableTruck);
        
    }
    
    
    @FXML
    private void SalesButtonAction(ActionEvent event) {
       currentButton = Sale.getText();
       table = getTableSalesView("All Locations","All Category","CAR");
       table.setPrefWidth(670);
       tabCar.setContent(table);
       tableTruck = getTableSalesView("All Locations","All Category","TRUCK");
       tableTruck.setPrefWidth(670);
       tabTruck.setContent(tableTruck);
    }

    
    @FXML
    private void AvailabiltyButtonAction(ActionEvent event) {
       currentButton = Available.getText();
       table = getTableAvailabiltyView("All Locations","All Category","CAR");
       table.setPrefWidth(670);
       tabCar.setContent(table);
       tableTruck = getTableAvailabiltyView("All Locations","All Category","TRUCK");
       tableTruck.setPrefWidth(670);
       tabTruck.setContent(tableTruck);
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
           
      
           
  
       if ( currentButton.equals("Available"))
       {
       table = getTableAvailabiltyView(b_name,cat,"CAR");
       tableTruck = getTableAvailabiltyView(b_name,cat,"TRUCK");
       }
       else if ( currentButton.equals("Overdue"))
       {
       table = getTableOverdueView(b_name,cat,"CAR");
       tableTruck = getTableOverdueView(b_name,cat,"TRUCK");
       }
       else if ( currentButton.equals("For Sale"))
       {
       table = getTableSalesView(b_name,cat,"CAR");
       tableTruck = getTableSalesView(b_name,cat,"TRUCK");
       }
       table.setPrefWidth(670);
       tabCar.setContent(table);
       tableTruck.setPrefWidth(670);
       tabTruck.setContent(tableTruck);
       
    }
    
    
     private void initialiseTimePickers() {
        // Initialise time combo box.
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream(System.getProperty("user.dir")
                    + "/src/ca/ubc/icics/mss/superrent/database/config.properties");
            props.load(fis);
            TIMINGS = props.getProperty("TIMINGS").split(",");
            start_time.getItems().addAll((Object[]) TIMINGS);
            start_time.setValue(TIMINGS[0]);
            end_time.getItems().addAll((Object[]) TIMINGS);
            end_time.setValue(TIMINGS[0]);
        } catch (IOException e) {
            start_time.setVisible(false);
            end_time.setVisible(false);
            Logger.getLogger(VehicleListViewController.class.getName()).
                    log(Level.SEVERE, null, e);
        }
    }
    
    
    /**
     * Converts the value of the date picker and time selector to a timestamp
     * 
     * @param date Date picker object
     * @param time ComboBox object that holds times in string format.
     * @return Timestamp
     */
    private Timestamp getTimestamp(DatePicker date, ComboBox time) {
        String timeSplit[] = time.getValue().toString().split(":");
        int hour = 0;
        int min = 0;
        
        try {
            hour = Integer.parseInt(timeSplit[0].trim());
            min = Integer.parseInt(timeSplit[1].trim());
        } catch (NumberFormatException ne) {
        }
        LocalDateTime localdatetime = date.getValue().atTime(hour, min);
        return Timestamp.valueOf(localdatetime);
        
    }
    
    
    private void initialiseDatePickers() {
        // Initialise the date pickers
        final Callback<DatePicker, DateCell> startDayCellFactory = 
                new Callback<DatePicker, DateCell>() {
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.compareTo(LocalDate.now()) < 0) {
                                    setDisable(true);
                                }
                            }
                        };
                    }
                };
        startDateField.setDayCellFactory(startDayCellFactory);
        startDateField.setValue(LocalDate.now());
        
        final Callback<DatePicker, DateCell> endDayCellFactory = 
                new Callback<DatePicker, DateCell>() {
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.compareTo(LocalDate.now()) < 0 ||
                                    (startDateField.getValue() != null && item.compareTo(startDateField.getValue()) < 0)) {
                                    setDisable(true);
                                }
                            }
                        };
                    }
                };
        endDateField.setDayCellFactory(endDayCellFactory);
        endDateField.setValue(LocalDate.now().plusDays(10));
    }
    
    
    @FXML private void startDateChanged() {
        if (startDateField.getValue() != null) {
            endDateField.setValue(startDateField.getValue());
        }
    }
    
    /**
     * 
     */
    @FXML private void endDateChanged() {
        /*if (startDateField.getValue() != null && endDateField.getValue() != null && 
                !startDateField.getValue().equals(endDateField.getValue())) {
            end_time.getItems().removeAll((Object[]) TIMINGS);
            end_time.getItems().addAll((Object[]) TIMINGS);
        }*/
    }
    
    /**
     * If the start and end dates are the same, do not show timings less than
     * the start time selected in the end time combo box.
     */
    @FXML private void startTimeChanged() {
        /*if (startDateField.getValue() != null && endDateField.getValue() != null
                && startDateField.getValue().equals(endDateField.getValue())) {
            end_time.getItems().removeAll((Object[]) TIMINGS);
            
            int selectedIndex = 0;
            for (int i = 0; i < TIMINGS.length; i++) {
                if (TIMINGS[i].equals(start_time.getValue())) {
                    selectedIndex = i;
                }
            }

            for (int i = selectedIndex + 1; i < TIMINGS.length; i++) {
                end_time.getItems().add(TIMINGS[i]);
            }
        } else if (startDateField.getValue() != null && endDateField.getValue() != null
                && !startDateField.getValue().equals(endDateField.getValue())) {
            end_time.getItems().addAll((Object[]) TIMINGS);
        }*/
    }
    
    
    public TableView getTableSalesView(String b_name,String cat ,String typ)
   {
       
       TableView<Vehicle> table = new TableView<Vehicle>();
	table.setTableMenuButtonVisible(true);
        
       
        
       TableColumn albumArt = new TableColumn("Album Art");
	albumArt.setCellValueFactory(new PropertyValueFactory("album"));
	albumArt.setPrefWidth(200);
        
         
         TableColumn pnum = new TableColumn("PlateNumber");
	pnum.setCellValueFactory(new PropertyValueFactory("pnum"));
	pnum.setPrefWidth(70);
	
        
	TableColumn vid = new TableColumn("Vehilce ID");
	vid.setCellValueFactory(new PropertyValueFactory("vid"));
	vid.setPrefWidth(70);
	
        
        TableColumn vname = new TableColumn("Vehicle Name");
	vname.setCellValueFactory(new PropertyValueFactory("vname"));
	vname.setPrefWidth(70);
        
        TableColumn type = new TableColumn("Type");
	type.setCellValueFactory(new PropertyValueFactory("type"));
	type.setPrefWidth(70);
	
        TableColumn bname = new TableColumn("Branch Name");
	bname.setCellValueFactory(new PropertyValueFactory("bname"));
	bname.setPrefWidth(70);
	
	
        TableColumn sprice = new TableColumn("Sale Price");
	sprice.setCellValueFactory(new PropertyValueFactory("sprice"));
	sprice.setPrefWidth(70);
        
        
        
        
        albumArt.setCellFactory(new Callback<TableColumn<Vehicle,Thumbnail>,TableCell<Vehicle,Thumbnail>>(){       
	    @Override
	    public TableCell<Vehicle, Thumbnail> call(TableColumn<Vehicle, Thumbnail> param) {               
	        TableCell<Vehicle, Thumbnail> cell = new TableCell<Vehicle, Thumbnail>(){
	            ImageView imageview = new ImageView();
                      @Override
	            public void updateItem(Thumbnail item, boolean empty) {                       
	                if(item!=null){                           
	                                         
	                    imageview.setFitHeight(75);
	                    imageview.setFitWidth(75);
	                    imageview.setImage(new Image(VehicleRepository.class.getResource("img").toString()+"/"+item.getFilename()));
                            Button button = new Button("", imageview);
                            button.setId(item.getVehicleId());
                            button.setContentDisplay(ContentDisplay.TOP);
                            button.setOnAction(new EventHandler<ActionEvent>() {
                           @Override
                            public void handle(ActionEvent event) {
                                 Button x = (Button) event.getSource();
                                 System.out.println(x.getId());
            }
        }); 
	                    
	                    //SETTING ALL THE GRAPHICS COMPONENT FOR CELL
	                    setGraphic(button);
	                }
	            }
	        };
	                      
	        return cell;
	    }
	 
	});
         //ADDING ALL THE COLUMNS TO TABLEVIEW
        
            table.getColumns().addAll(albumArt,pnum,vid,vname,type,bname,sprice);        
   
           
        try
        {
            
           if (b_name.equals("All Locations") && cat.equals("All Category"))
            que = "select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.city,s.for_sale_price  from team02.vehicle v left outer join team02.branch b on (v.branch_id = b.branch_id) inner join team02.for_sale_vehicle s on (s.vehicle_id = v.vehicle_id)  where v.vehicle_type = '" + typ +"'" ;   
           else if (!b_name.equals("All Locations") && !cat.equals("All Category"))
            que = "select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.city,s.for_sale_price  from team02.vehicle v left outer join team02.branch b on (v.branch_id = b.branch_id) inner join team02.for_sale_vehicle s on (s.vehicle_id = v.vehicle_id)  where v.vehicle_type = '" + typ +"' and v.vehicle_category ='" + cat + "' and b.city ='" + b_name + "'"  ;    
           else if (!b_name.equals("All Locations"))
            que = "select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.city,s.for_sale_price  from team02.vehicle v left outer join team02.branch b on (v.branch_id = b.branch_id) inner join team02.for_sale_vehicle s on (s.vehicle_id = v.vehicle_id)  where v.vehicle_type = '" + typ +"' and b.city ='" + b_name + "'"  ;       
           else
            que = "select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.city,s.for_sale_price  from team02.vehicle v left outer join team02.branch b on (v.branch_id = b.branch_id) inner join team02.for_sale_vehicle s on (s.vehicle_id = v.vehicle_id)  where v.vehicle_type = '" + typ +"' and v.vehicle_category ='" + cat + "'" ;    
            
            
           
           Connection con = SQLConnection.getConnection();
                ResultSet rs = con.createStatement().executeQuery(que);
                
        //ADDING ROWS INTO TABLEVIEW
        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();
        
        while(rs.next()){ 
            Thumbnail al = new Thumbnail (rs.getString(4)+".jpg",rs.getString(2)); 
            Vehicle m = new Vehicle(al,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)); 
            vehicles.add(m); 
        }        
        table.setItems(vehicles); 
        }
        catch(Exception e){
	              Logger.getLogger(VehicleRepository.class.getName()).log(Level.SEVERE, null, e);           
	          }
        return table;
   }
           
  
  public TableView getTableAvailabiltyView(String b_name,String cat,String typ)
   {
       
        TableView<Vehicle> table = new TableView<Vehicle>();
	table.setTableMenuButtonVisible(true);
        
        TableColumn albumArt = new TableColumn("Album Art");
	albumArt.setCellValueFactory(new PropertyValueFactory("album"));
	albumArt.setPrefWidth(100);
        
        TableColumn pnum = new TableColumn("PlateNumber");
	pnum.setCellValueFactory(new PropertyValueFactory("pnum"));
	pnum.setPrefWidth(70);
        
	TableColumn vid = new TableColumn("Vehilce ID");
	vid.setCellValueFactory(new PropertyValueFactory("vid"));
	vid.setPrefWidth(70);
        
        TableColumn vname = new TableColumn("Vehicle Name");
	vname.setCellValueFactory(new PropertyValueFactory("vname"));
	vname.setPrefWidth(70);
        
        TableColumn type = new TableColumn("Type");
	type.setCellValueFactory(new PropertyValueFactory("type"));
	type.setPrefWidth(70);
	
        TableColumn bname = new TableColumn("Branch Name");
	bname.setCellValueFactory(new PropertyValueFactory("bname"));
	bname.setPrefWidth(70);
        
        albumArt.setCellFactory(new Callback<TableColumn<Vehicle,Thumbnail>,TableCell<Vehicle,Thumbnail>>(){       
	    @Override
	    public TableCell<Vehicle, Thumbnail> call(TableColumn<Vehicle, Thumbnail> param) {               
	        TableCell<Vehicle, Thumbnail> cell;
                cell = new TableCell<Vehicle, Thumbnail>(){
                    ImageView imageview = new ImageView();
                    @Override
                    public void updateItem(Thumbnail item, boolean empty) {
                        if(item!=null){
                            
                            imageview.setFitHeight(75);
                            imageview.setFitWidth(75);
                            imageview.setImage(new Image(VehicleRepository.class.getResource("img").toString()+"/"+item.getFilename()));
                            Button button = new Button("", imageview);
                            button.setId(item.getVehicleId());
                            button.setContentDisplay(ContentDisplay.TOP);
                            
                            button.setOnAction(new EventHandler<ActionEvent>() {
                           @Override
                            public void handle(ActionEvent event) {
                                 Button x = (Button) event.getSource();
                               try {
                                   handlerMethod(event);
                               } catch (IOException ex) {
                                   Logger.getLogger(VehicleListViewController.class.getName()).log(Level.SEVERE, null, ex);
                               }
            }
        }); 
                            
                            
                            //SETTING ALL THE GRAPHICS COMPONENT FOR CELL
                            setGraphic(button);
                        }
                    }
                };
	                     
	        return cell;
	    }
	 
	});
         //ADDING ALL THE COLUMNS TO TABLEVIEW
        
            table.getColumns().addAll(albumArt,pnum,vid,vname,type,bname);        
        
        try
        {
            if (b_name.equals("All Locations") && cat.equals("All Category"))
           que   = "select all_vehicles.plate_number,all_vehicles.vehicle_id,all_vehicles.vehicle_name,all_vehicles.vehicle_category,all_vehicles.city from\n" +
" (select distinct v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.city,vt.daily_rate,vt.hourly_rate,vt.weekly_rate,vt.daily_premium,vt.hourly_premium,vt.weekly_premium,v.vehicle_manufactured_year,v.vehicle_type from vehicle v inner join branch b on (b.branch_id = v.branch_id) left outer join for_sale_vehicle fsv\n" +
"on (v.vehicle_id = fsv.vehicle_id) \n" +
"inner join vehicle_category vt on (v.vehicle_category = vt.vehicle_category)\n" +
"where fsv.vehicle_id is null ) all_vehicles\n" +
"left outer join \n" +
"(select vehicle_id from reserve where end_date_time between '" + getTimestamp(startDateField, start_time).toString() + "' and '" + getTimestamp(endDateField, end_time).toString() + "' ) res\n" +
"on (res.vehicle_id = all_vehicles.vehicle_id)\n" +
"left outer join\n" +
"(select vehicle_id from rent where end_date_time between '" + getTimestamp(startDateField, start_time).toString() + "' and '" + getTimestamp(endDateField, end_time).toString() + "' ) r\n" +
"on (r.vehicle_id = all_vehicles.vehicle_id)\n" +
"where res.vehicle_id is null and r.vehicle_id is null and  all_vehicles.vehicle_type = '" +typ +"'";
          else if (!b_name.equals("All Locations") && !cat.equals("All Category"))
que   = "select all_vehicles.plate_number,all_vehicles.vehicle_id,all_vehicles.vehicle_name,all_vehicles.vehicle_category,all_vehicles.city from\n" +
" (select distinct v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.city,vt.daily_rate,vt.hourly_rate,vt.weekly_rate,vt.daily_premium,vt.hourly_premium,vt.weekly_premium,v.vehicle_manufactured_year,v.vehicle_type from vehicle v inner join branch b on (b.branch_id = v.branch_id) left outer join for_sale_vehicle fsv\n" +
"on (v.vehicle_id = fsv.vehicle_id) \n" +
"inner join vehicle_category vt on (v.vehicle_category = vt.vehicle_category)\n" +
"where fsv.vehicle_id is null ) all_vehicles\n" +
"left outer join \n" +
"(select vehicle_id from reserve where end_date_time between '" + getTimestamp(startDateField, start_time).toString() + "' and '" + getTimestamp(endDateField, end_time).toString() + "' ) res\n" +
"on (res.vehicle_id = all_vehicles.vehicle_id)\n" +
"left outer join\n" +
"(select vehicle_id from rent where end_date_time between '" + getTimestamp(startDateField, start_time).toString() + "' and '" + getTimestamp(endDateField, end_time).toString() + "' ) r\n" +
"on (r.vehicle_id = all_vehicles.vehicle_id)\n" +
"where res.vehicle_id is null and r.vehicle_id is null and  all_vehicles.vehicle_type = '" +typ +"' and all_vehicles.vehicle_category='" +cat+ "' and all_vehicles.city = '" +b_name + "'" ; 
              else if (!b_name.equals("All Locations"))
que    = "select all_vehicles.plate_number,all_vehicles.vehicle_id,all_vehicles.vehicle_name,all_vehicles.vehicle_category,all_vehicles.city from\n" +
" (select distinct v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.city,vt.daily_rate,vt.hourly_rate,vt.weekly_rate,vt.daily_premium,vt.hourly_premium,vt.weekly_premium,v.vehicle_manufactured_year,v.vehicle_type from vehicle v inner join branch b on (b.branch_id = v.branch_id) left outer join for_sale_vehicle fsv\n" +
"on (v.vehicle_id = fsv.vehicle_id) \n" +
"inner join vehicle_category vt on (v.vehicle_category = vt.vehicle_category)\n" +
"where fsv.vehicle_id is null ) all_vehicles\n" +
"left outer join \n" +
"(select vehicle_id from reserve where end_date_time between '" + getTimestamp(startDateField, start_time).toString() + "' and '" + getTimestamp(endDateField, end_time).toString() + "' ) res\n" +
"on (res.vehicle_id = all_vehicles.vehicle_id)\n" +
"left outer join\n" +
"(select vehicle_id from rent where end_date_time between '" + getTimestamp(startDateField, start_time).toString() + "' and '" + getTimestamp(endDateField, end_time).toString() + "' ) r\n" +
"on (r.vehicle_id = all_vehicles.vehicle_id)\n" +
"where res.vehicle_id is null and r.vehicle_id is null and  all_vehicles.vehicle_type = '" +typ +"' and all_vehicles.city = '" +b_name + "'" ; 
            else
                  que   = "select all_vehicles.plate_number,all_vehicles.vehicle_id,all_vehicles.vehicle_name,all_vehicles.vehicle_category,all_vehicles.city from\n" +
" (select distinct v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.city,vt.daily_rate,vt.hourly_rate,vt.weekly_rate,vt.daily_premium,vt.hourly_premium,vt.weekly_premium,v.vehicle_manufactured_year,v.vehicle_type from vehicle v inner join branch b on (b.branch_id = v.branch_id) left outer join for_sale_vehicle fsv\n" +
"on (v.vehicle_id = fsv.vehicle_id) \n" +
"inner join vehicle_category vt on (v.vehicle_category = vt.vehicle_category)\n" +
"where fsv.vehicle_id is null ) all_vehicles\n" +
"left outer join \n" +
"(select vehicle_id from reserve where end_date_time between '" + getTimestamp(startDateField, start_time).toString() + "' and '" + getTimestamp(endDateField, end_time).toString() + "' ) res\n" +
"on (res.vehicle_id = all_vehicles.vehicle_id)\n" +
"left outer join\n" +
"(select vehicle_id from rent where end_date_time between '" + getTimestamp(startDateField, start_time).toString() + "' and '" + getTimestamp(endDateField, end_time).toString() + "' ) r\n" +
"on (r.vehicle_id = all_vehicles.vehicle_id)\n" +
"where res.vehicle_id is null and r.vehicle_id is null and  all_vehicles.vehicle_type = '" +typ +"' and all_vehicles.vehicle_category='" +cat+ "'" ; 

                  
 
          Connection con = SQLConnection.getConnection();
          ResultSet rs = con.createStatement().executeQuery(que);
                
         
                                 
        //ADDING ROWS INTO TABLEVIEW
        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();
        
        while(rs.next()){ 
            Thumbnail al = new Thumbnail (rs.getString(4)+".jpg",rs.getString(2)); 
            Vehicle m = new Vehicle(al,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)); 
            vehicles.add(m); 
        }        
       
        table.setItems(vehicles); 
        }
        catch(Exception e){
	              Logger.getLogger(VehicleRepository.class.getName()).log(Level.SEVERE, null, e);          
	          }
        return table;
   }
  
  
   public TableView getTableOverdueView(String b_name,String cat,String typ)
   {
       
       TableView<Vehicle> table = new TableView<Vehicle>();
	table.setTableMenuButtonVisible(true);
        
       
        
       TableColumn albumArt = new TableColumn("Album Art");
	albumArt.setCellValueFactory(new PropertyValueFactory("album"));
	albumArt.setPrefWidth(200);
        
         
        TableColumn pnum = new TableColumn("PlateNumber");
	pnum.setCellValueFactory(new PropertyValueFactory("pnum"));
	pnum.setPrefWidth(70);
	
        
	TableColumn vid = new TableColumn("Vehilce ID");
	vid.setCellValueFactory(new PropertyValueFactory("vid"));
	vid.setPrefWidth(70);
	
        
        TableColumn vname = new TableColumn("Vehicle Name");
	vname.setCellValueFactory(new PropertyValueFactory("vname"));
	vname.setPrefWidth(70);
        
        TableColumn type = new TableColumn("Type");
	type.setCellValueFactory(new PropertyValueFactory("type"));
	type.setPrefWidth(70);
	
        TableColumn bname = new TableColumn("Branch Name");
	bname.setCellValueFactory(new PropertyValueFactory("bname"));
	bname.setPrefWidth(70);
	
    
	
        TableColumn sdate = new TableColumn("Start Date");
	sdate.setCellValueFactory(new PropertyValueFactory("sdate"));
	sdate.setPrefWidth(70);
        
        TableColumn edate = new TableColumn("End Date");
	edate.setCellValueFactory(new PropertyValueFactory("edate"));
	edate.setPrefWidth(70);
        
        TableColumn odays = new TableColumn("overdue days");
	odays.setCellValueFactory(new PropertyValueFactory("odays"));
	odays.setPrefWidth(70);
        
        TableColumn otime = new TableColumn("overdue time");
	otime.setCellValueFactory(new PropertyValueFactory("otime"));
	otime.setPrefWidth(70);
        
        
        albumArt.setCellFactory(new Callback<TableColumn<Vehicle,Thumbnail>,TableCell<Vehicle,Thumbnail>>(){       
	    @Override
	    public TableCell<Vehicle, Thumbnail> call(TableColumn<Vehicle, Thumbnail> param) {               
	        TableCell<Vehicle, Thumbnail> cell = new TableCell<Vehicle, Thumbnail>(){
	            ImageView imageview = new ImageView();
                      @Override
	            public void updateItem(Thumbnail item, boolean empty) {                       
	                if(item!=null){                           
	                                         
	                    imageview.setFitHeight(75);
	                    imageview.setFitWidth(75);
	                    imageview.setImage(new Image(VehicleRepository.class.getResource("img").toString()+"/"+item.getFilename()));
	                    Button button = new Button("", imageview);
                            button.setId(item.getVehicleId());
                            button.setContentDisplay(ContentDisplay.TOP);
                            button.setOnAction(new EventHandler<ActionEvent>() {
                           @Override
                            public void handle(ActionEvent event) {
                                 Button x = (Button) event.getSource();
                                 System.out.println(x.getId());
            }
        }); 
	                    
	                    //SETTING ALL THE GRAPHICS COMPONENT FOR CELL
	                    setGraphic(button);
	                }
	            }
	        };
	                     
	        return cell;
	    }
	 
	});
         //ADDING ALL THE COLUMNS TO TABLEVIEW
        
            table.getColumns().addAll(albumArt,pnum,vid,vname,type,bname,sdate,edate,odays,otime);        
        
        try
        {
            if (b_name.equals("All Locations") && cat.equals("All Category"))
           que = "select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.city,r.start_date_time,r.end_date_time,(current_date - date(end_date_time)) no_days,timediff(current_time,time(end_date_time)) time_due  from rent r inner join vehicle v on ( v.vehicle_id = r.vehicle_id) inner join branch b on(v.branch_id = b.branch_id) left outer join returns re on (r.rent_id = re.rent_id) where re.return_id is null and date(end_date_time) <= current_date and time(end_date_time) < current_time and v.vehicle_type = '" + typ + "'";  
           else if (!b_name.equals("All Locations") && !cat.equals("All Category"))
           que = "select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.city,r.start_date_time,r.end_date_time,(current_date - date(end_date_time)) no_days,timediff(current_time,time(end_date_time)) time_due  from rent r inner join vehicle v on ( v.vehicle_id = r.vehicle_id) inner join branch b on(v.branch_id = b.branch_id) left outer join returns re on (r.rent_id = re.rent_id) where re.return_id is null and date(end_date_time) <= current_date and time(end_date_time) < current_time and v.vehicle_type = '" + typ + "' and b.city = '" + b_name + "' and v.vehicle_category = '" + cat  +"'" ;  

           else if (!b_name.equals("All Locations"))
           que = "select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.city,r.start_date_time,r.end_date_time,(current_date - date(end_date_time)) no_days,timediff(current_time,time(end_date_time)) time_due  from rent r inner join vehicle v on ( v.vehicle_id = r.vehicle_id) inner join branch b on(v.branch_id = b.branch_id) left outer join returns re on (r.rent_id = re.rent_id) where re.return_id is null and date(end_date_time) <= current_date and time(end_date_time) < current_time and v.vehicle_type = '" + typ + "' and b.city = '" + b_name + "'";  

           else
           que = "select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.city,r.start_date_time,r.end_date_time,(current_date - date(end_date_time)) no_days,timediff(current_time,time(end_date_time)) time_due  from rent r inner join vehicle v on ( v.vehicle_id = r.vehicle_id) inner join branch b on(v.branch_id = b.branch_id) left outer join returns re on (r.rent_id = re.rent_id) where re.return_id is null and date(end_date_time) <= current_date and time(end_date_time) < current_time and v.vehicle_type = '" + typ + "' and v.vehicle_category = '" +cat +"'";  

            
           
            Connection con = SQLConnection.getConnection();
                ResultSet rs = con.createStatement().executeQuery(que);
                
         
                                 
        //ADDING ROWS INTO TABLEVIEW
        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();
        
        while(rs.next()){ 
            Thumbnail al = new Thumbnail (rs.getString(4)+".jpg",rs.getString(2)); 
            Vehicle m = new Vehicle(al,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)); 
            vehicles.add(m); 
        }        
        table.setItems(vehicles); 
        }
        catch(Exception e){
	          Logger.getLogger(VehicleRepository.class.getName()).log(Level.SEVERE, null, e);           
	          }
        return table;
   }
  
   
   
    //button.setOnAction(new EventHandler<ActionEvent>() {
                           
    public void handlerMethod(ActionEvent event) throws IOException {
        Button x = (Button) event.getSource();
        Stage rentReserveStage = new Stage();
        
    switch (mode) {
        case "RENT":
            RentReserveFormController.setModeRent(Integer.parseInt(x.getId()), 
                    getTimestamp(startDateField, start_time),
                    getTimestamp(endDateField, end_time));
            break;
        case "RESERVE":
            RentReserveFormController.setModeReserve(Integer.parseInt(x.getId()), 
                    getTimestamp(startDateField, start_time),
                    getTimestamp(endDateField, end_time));
            break;
    }
        
        FXMLLoader myLoader = new FXMLLoader(RentReserveFormController.class.getResource("RentReserveForm.fxml"));
        AnchorPane myPane = (AnchorPane) myLoader.load();        
        Scene myScene = new Scene(myPane);
        rentReserveStage.setScene(myScene);
        rentReserveStage.show();                     
    }
}
