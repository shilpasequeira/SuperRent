/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.clerk.vehiclelist;

/**
 *
 * @author Avinash
 */
public class Thumbnail {
   private String filename;
   private String VehicleId;
    public Thumbnail(String filename, String vid) {
                
                this.filename = filename;
                this.VehicleId = vid;
	       	    }
    public String getFilename() {
	        return filename;
	    }
	    public void setFilename(String filename) {
	        this.filename = filename;
	    }
            
            public String getVehicleId() {
	        return VehicleId;
	    }
	    public void setVehicleId(String vid ){
	        this.VehicleId = vid;
	    }
}
