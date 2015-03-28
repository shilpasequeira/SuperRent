/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.manager.reports;


/**
 *
 * @author warrior
 */
public class costReport {

    public String Branch;
    public String TotalCost;
    
      public String getTotalCost() {
        return TotalCost;
    }

    public void setTotalCost(String ID) {
        this.TotalCost = ID;
    }
    
   
    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        this.Branch = branch;
    }


}
