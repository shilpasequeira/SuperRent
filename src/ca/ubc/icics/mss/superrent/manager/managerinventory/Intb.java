/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.manager.managerinventory;

import java.io.InputStream;
import java.util.Date;
import javafx.scene.image.Image;

/**
 *
 * @author Xinwei
 */
public class Intb {
    public String VehicleID;
    public String PlateNumber;
    public String Year;
    public String Branch;
    public String Category;
    public String Type;
    public String Name;
    public InputStream Im;
    public String Status;
    
    public Intb(){
        
    }
    public String getYear(){
        return Year;
    }
    public void setYear(String s){
        this.Year=s;
    }
    public String getPlateNumber(){
        return PlateNumber;
    }
    public void setPlateNumber(String s){
        this.PlateNumber=s;
    }
    public InputStream getIm(){
        return Im;
    }
    public void setIm(InputStream s){
        this.Im=s;
    }
    public String getBranch() {
        return Branch;
    }
    public void setStatus(String s){
        this.Status=s;
        
    }
    public String getStatus(){
        return Status;
    }
    
    public String getName(){
        return Name;
    }
    
    
    public String getCategory() {
        return Category;
    }


    public String getVehicleID() {
        return VehicleID;
    }
    
    public String getType(){
        return Type;
    }

    public void setType(String a) {
        this.Type=a;
    }
    
    public void setBranch(String a){
        this.Branch=a;
    }
    
    public void setName(String a){
        this.Name=a; 
    }
    
    
    public void setCategory(String a) {
        this.Category=a;
    }


    public void setVehicleID(String a) {
        this.VehicleID=a;
    }
    




}
