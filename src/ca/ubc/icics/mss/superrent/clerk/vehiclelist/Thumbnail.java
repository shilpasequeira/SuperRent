/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.clerk.vehiclelist;

import java.io.InputStream;
import javafx.scene.image.Image;

/**
 *
 * @author Avinash
 */
public class Thumbnail {
   private Image is;
   private String filename;
   private String VehicleId;
    public Thumbnail(String filename, String vid,InputStream is) {
                
                this.filename = filename;
                this.VehicleId = vid;
                this.is = new Image(is);
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
            
            public Image getImage() {
	        return is;
	    }
	    public void setImage(InputStream is ){
	        this.is = new Image(is);
	    }
            
}
