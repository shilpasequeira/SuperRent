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
