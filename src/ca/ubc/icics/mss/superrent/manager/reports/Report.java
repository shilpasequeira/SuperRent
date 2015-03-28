/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.manager.reports;

import java.util.Date;

/**
 *
 * @author warrior
 */
public class Report {

    public String VehicleID;
    public Date ReturnDate;
    public String AmountPaid;
    public String Branch;
    public Date RentDate;
    public String EstimatedCost;
    public String Category;
    public String BranchID;
    public String TotalCost;
    
      public String getTotalCost() {
        return TotalCost;
    }

    public void setTotalCost(String ID) {
        this.TotalCost = ID;
    }
    
     public String getBranchID() {
        return BranchID;
    }

    public void setBranchID(String ID) {
        this.BranchID = ID;
    }
    public String getVehicleID() {
        return VehicleID;
    }

    public void setVehicleID(String ID) {
        this.VehicleID = ID;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        this.Category = category;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        this.Branch = branch;
    }

    public String getAmountPaid() {
        return AmountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.AmountPaid = amountPaid;
    }

    public Date getReturnDate() {
        return ReturnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.ReturnDate = returnDate;
    }

    public Date getRentDate() {
        return RentDate;
    }

    public void setRentDate(Date rentDate) {
        this.RentDate = rentDate;
    }

    public String getEstimatedCost() {
        return EstimatedCost;
    }

    public void setEstimatedCost(String estimatedCost) {
        this.EstimatedCost = estimatedCost;
    }

}
