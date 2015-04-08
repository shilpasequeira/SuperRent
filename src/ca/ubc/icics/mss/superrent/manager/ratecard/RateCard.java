/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.manager.ratecard;

//import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Zhiming
 */
public class RateCard {
    public String Type;
    public String Category;
    public double HourlyRate;
    public double DailyRate;
    public double WeeklyRate;
    public double DailyPremium;
    public double HourlyPremium;
    public double WeeklyPremium;
    public double AEDailyRate;
    public double AEHourlyRate;
    
    public double NumAvail;
    //Type
    public void setType (String type) {
        this.Type = type;
    }
    public String getType() {
        return Type;
    }
    
    //Category
    public void setCategory(String cat) {
        this.Category = cat;
    }
    public String getCategory() {
        return Category;
    }
    //Hourly Rate
    public void setHourlyRate(double hourlyrate) {
        HourlyRate = hourlyrate;
    }
    public double getHourlyRate() {
        return HourlyRate;
    }
    
    //Daily Rate
    public void setDailyRate(double dailyrate) {
        this.DailyRate = dailyrate;
    }
    public double getDailyRate() {
        return DailyRate;
    }
    
    //Weekly Rate
    public void setWeeklyRate(double weeklyrate) {
        this.WeeklyRate = weeklyrate;
    }
    public double getWeeklyRate() {
        return WeeklyRate;
    }
    
    //Daily Premium
    public void setDailyPremium(double dailypremium) {
        this.DailyPremium = dailypremium;
    }
    public double getDailyPremium(){
        return DailyPremium;
    }
    
    //Hourly Premium
    public void setHourlyPremium(double hourlypremium) {
        this.HourlyPremium = hourlypremium;
    }
    public double getHourlyPremium() {
        return HourlyPremium;
    }
    
    //Weekly Premium
    public void setWeeklyPremium(double weeklypremium) {
        this.WeeklyPremium = weeklypremium;
    }
    public double getWeeklyPremium() {
        return WeeklyPremium;
    }
    
    //Additional Equitment Daily Rate
    public void setAEDailyRate(double aedailyrate) {
        this.AEDailyRate = aedailyrate;
    }
    public double getAEDailyRate() {
        return AEDailyRate;
    }
    
    //Additional Equitment Hourly Rate
    public void setAEHourlyRate(double aehourlyrate) {
        this.AEHourlyRate = aehourlyrate;
    }
    public double getAEHourlyRate() {
        return AEHourlyRate;
    }
    
    public void setNumAvail(double numavail) {
        this.NumAvail = numavail;
    }
    public double getNumAvail() {
        return NumAvail;
    }
}
