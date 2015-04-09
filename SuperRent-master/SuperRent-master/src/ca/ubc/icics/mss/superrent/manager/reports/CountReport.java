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
public class CountReport {
    public String Branch;
    public String Count;
    public String Category;
    
      public String getCategory() {
        return Category;
    }

    public void setCategory(String ID) {
        this.Category = ID;
    }
    public String getCount() {
        return Count;
    }

    public void setCount(String branch) {
        this.Count = branch;
    }
   
    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        this.Branch = branch;
    }
}
