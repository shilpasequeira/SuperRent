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
    public Thumbnail(String filename) {
                
                this.filename = filename;
	       	    }
    public String getFilename() {
	        return filename;
	    }
	    public void setFilename(String filename) {
	        this.filename = filename;
	    }
}
