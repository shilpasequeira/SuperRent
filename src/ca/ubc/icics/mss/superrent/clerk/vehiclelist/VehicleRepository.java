/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ca.ubc.icics.mss.superrent.clerk.vehiclelist;

import ca.ubc.icics.mss.superrent.database.SQLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 *
 * @author Avinash
 */
public class VehicleRepository {
    
    public Statement statement = null;
    public PreparedStatement prstatement = null;
    public ResultSet rs = null;
    public String que = null;
    
   
  
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
            que = "select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.location,s.for_sale_price  from team02.vehicle v left outer join team02.branch b on (v.branch_id = b.branch_id) inner join team02.for_sale_vehicle s on (s.vehicle_id = v.vehicle_id)  where v.vehicle_type = '" + typ +"'" ;   
           else if (!b_name.equals("All Locations") && !cat.equals("All Category"))
            que = "select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.location,s.for_sale_price  from team02.vehicle v left outer join team02.branch b on (v.branch_id = b.branch_id) inner join team02.for_sale_vehicle s on (s.vehicle_id = v.vehicle_id)  where v.vehicle_type = '" + typ +"' and v.vehicle_category ='" + cat + "' and b.location ='" + b_name + "'"  ;    
           else if (!b_name.equals("All Locations"))
            que = "select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.location,s.for_sale_price  from team02.vehicle v left outer join team02.branch b on (v.branch_id = b.branch_id) inner join team02.for_sale_vehicle s on (s.vehicle_id = v.vehicle_id)  where v.vehicle_type = '" + typ +"' and b.location ='" + b_name + "'"  ;       
           else
            que = "select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.location,s.for_sale_price  from team02.vehicle v left outer join team02.branch b on (v.branch_id = b.branch_id) inner join team02.for_sale_vehicle s on (s.vehicle_id = v.vehicle_id)  where v.vehicle_type = '" + typ +"' and v.vehicle_category ='" + cat + "'" ;    
            
            
           
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
        
            table.getColumns().addAll(albumArt,pnum,vid,vname,type,bname);        
        
        try
        {
          /* if (b_name.equals("All Locations") && cat.equals("All Category"))
            que = "select all_vehicles.plate_number,all_vehicles.vehicle_id,all_vehicles.vehicle_name,all_vehicles.vehicle_category,all_vehicles.location from (select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.location from vehicle v inner join branch b on (b.branch_id = v.branch_id) left outer join for_sale_vehicle fsv\n" +
"on (v.vehicle_id = fsv.vehicle_id) \n" +
"where fsv.vehicle_id is null ) all_vehicles\n" +
"left outer join \n" +
"(select vehicle_id from reserve where end_date_time between current_timestamp and TIMESTAMPADD(DAY,10,current_timestamp)) res\n" +
"on (res.vehicle_id = all_vehicles.vehicle_id)\n" +
"left outer join\n" +
"(select vehicle_id from rent where end_date_time between current_timestamp and TIMESTAMPADD(DAY,10,current_timestamp)) r\n" +
"on (r.vehicle_id = all_vehicles.vehicle_id)\n" +
"where res.vehicle_id is null and r.vehicle_id is null" ;   
           else if (!b_name.equals("All Locations") && !cat.equals("All Category"))
            que = "select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.location,s.for_sale_price  from team02.vehicle v left outer join team02.branch b on (v.branch_id = b.branch_id) inner join team02.for_sale_vehicle s on (s.vehicle_id = v.vehicle_id)  where v.vehicle_type = 'CAR' and v.vehicle_category ='" + cat + "' and b.location ='" + b_name + "'"  ;    
           else if (!b_name.equals("All Locations"))
            que = "select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.location,s.for_sale_price  from team02.vehicle v left outer join team02.branch b on (v.branch_id = b.branch_id) inner join team02.for_sale_vehicle s on (s.vehicle_id = v.vehicle_id)  where v.vehicle_type = 'CAR' and b.location ='" + b_name + "'"  ;       
           else
            que = "select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.location,s.for_sale_price  from team02.vehicle v left outer join team02.branch b on (v.branch_id = b.branch_id) inner join team02.for_sale_vehicle s on (s.vehicle_id = v.vehicle_id)  where v.vehicle_type = 'CAR' and v.vehicle_category ='" + cat + "'" ;    
           
            */
            
            que = "select all_vehicles.plate_number,all_vehicles.vehicle_id,all_vehicles.vehicle_name,all_vehicles.vehicle_category,all_vehicles.location from (select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.location from vehicle v inner join branch b on (b.branch_id = v.branch_id) left outer join for_sale_vehicle fsv\n" +
"on (v.vehicle_id = fsv.vehicle_id) \n" +
"where fsv.vehicle_id is null ) all_vehicles\n" +
"left outer join \n" +
"(select vehicle_id from reserve where end_date_time between current_timestamp and TIMESTAMPADD(DAY,10,current_timestamp)) res\n" +
"on (res.vehicle_id = all_vehicles.vehicle_id)\n" +
"left outer join\n" +
"(select vehicle_id from rent where end_date_time between current_timestamp and TIMESTAMPADD(DAY,10,current_timestamp)) r\n" +
"on (r.vehicle_id = all_vehicles.vehicle_id)\n" +
"where res.vehicle_id is null and r.vehicle_id is null" ; 
          
 
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
           que = "select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.location,r.start_date_time,r.end_date_time,(current_date - date(end_date_time)) no_days,timediff(current_time,time(end_date_time)) time_due  from rent r inner join vehicle v on ( v.vehicle_id = r.vehicle_id) inner join branch b on(v.branch_id = b.branch_id) left outer join returns re on (r.rent_id = re.rent_id) where re.return_id is null and date(end_date_time) <= current_date and time(end_date_time) < current_time and v.vehicle_type = '" + typ + "'";  
           else if (!b_name.equals("All Locations") && !cat.equals("All Category"))
           que = "select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.location,r.start_date_time,r.end_date_time,(current_date - date(end_date_time)) no_days,timediff(current_time,time(end_date_time)) time_due  from rent r inner join vehicle v on ( v.vehicle_id = r.vehicle_id) inner join branch b on(v.branch_id = b.branch_id) left outer join returns re on (r.rent_id = re.rent_id) where re.return_id is null and date(end_date_time) <= current_date and time(end_date_time) < current_time and v.vehicle_type = '" + typ + "' and b.location = '" + b_name + "' and v.vehicle_category = '" + cat  +"'" ;  

           else if (!b_name.equals("All Locations"))
           que = "select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.location,r.start_date_time,r.end_date_time,(current_date - date(end_date_time)) no_days,timediff(current_time,time(end_date_time)) time_due  from rent r inner join vehicle v on ( v.vehicle_id = r.vehicle_id) inner join branch b on(v.branch_id = b.branch_id) left outer join returns re on (r.rent_id = re.rent_id) where re.return_id is null and date(end_date_time) <= current_date and time(end_date_time) < current_time and v.vehicle_type = '" + typ + "' and b.location = '" + b_name + "'";  

           else
           que = "select v.plate_number,v.vehicle_id,v.vehicle_name,v.vehicle_category,b.location,r.start_date_time,r.end_date_time,(current_date - date(end_date_time)) no_days,timediff(current_time,time(end_date_time)) time_due  from rent r inner join vehicle v on ( v.vehicle_id = r.vehicle_id) inner join branch b on(v.branch_id = b.branch_id) left outer join returns re on (r.rent_id = re.rent_id) where re.return_id is null and date(end_date_time) <= current_date and time(end_date_time) < current_time and v.vehicle_type = '" + typ + "' and v.vehicle_category = '" +cat +"'";  

            
           
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
  
           
         
    
    public ArrayList<String> getNames(String type) {
         ArrayList<String> al = new ArrayList();
         
        switch (type) {
            case "Branch":
                que = "SELECT distinct location FROM team02.branch";
                al.add("All Locations");
                break;
            case "Category":
                que = "SELECT distinct vehicle_category FROM team02.vehicle_category";
                al.add("All Category");
                break;
        }
        prstatement = null;
        
         
        try {
             Connection con = SQLConnection.getConnection();
             ResultSet rs = con.createStatement().executeQuery(que);
            while(rs.next()) {
                al.add(rs.getString(1));
            }
             return al;
        } catch (SQLException e) {
            Logger.getLogger(VehicleRepository.class.getName()).log(Level.SEVERE, null, e); 
            return null;
        } finally {
            prstatement = null;
        }
    }
    
}
