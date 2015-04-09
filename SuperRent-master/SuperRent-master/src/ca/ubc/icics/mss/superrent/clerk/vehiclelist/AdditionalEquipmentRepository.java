/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.clerk.vehiclelist;

import ca.ubc.icics.mss.superrent.database.SQLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shilpa
 */
public class AdditionalEquipmentRepository {
    /**
     * Get the available additional equipment for a vehicle.
     * @param vehicle
     * @return 
     */
    public static ArrayList getAvailableAdditionalEquipment(Vehicle vehicle) {
        ArrayList<AdditionalEquipment> additionalEquipmentList = new ArrayList();
        
        try (Connection con = SQLConnection.getConnection();
                ResultSet rs = con.createStatement().executeQuery(
                        "SELECT * FROM additional_equipment WHERE "
                                + "equipment_type = '" + vehicle.getType() + "'"
                                + " AND branch_id = " + vehicle.getBranchID()
                                + " AND equipment_stock > 0")) {
            
            while(rs.next()){
                AdditionalEquipment additionalEquipment = new AdditionalEquipment();
                additionalEquipment.setID(rs.getInt("equipment_id"));
                additionalEquipment.setName(rs.getString("equipment_name"));
                additionalEquipment.setType(rs.getString("equipment_type"));
                additionalEquipment.setDailyRate(rs.getInt("equipment_daily_rate"));
                additionalEquipment.setHourlyRate(rs.getInt("equipment_hourly_rate"));
                additionalEquipment.setStock(rs.getInt("equipment_stock"));
                additionalEquipment.setBranchID(rs.getInt("branch_id"));
                additionalEquipmentList.add(additionalEquipment);
            }
        } catch (SQLException e) {
            Logger.getLogger(AdditionalEquipmentRepository.class.getName()).log(Level.SEVERE, null, e);
        }
        return additionalEquipmentList;
    }
}
