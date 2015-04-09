/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.validation;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author anmolpreet Sandhu
 */
public class md5Check {
    public static String md5(String password)
    {
    		try {
    		MessageDigest md = MessageDigest.getInstance("MD5");
        	md.update(password.getBytes());
        	byte bData[] = md.digest();
        	
        	StringBuffer buf = new StringBuffer();
        	for(int i = 0; i<bData.length; i++)
        	{
        		buf.append(Integer.toString((bData[i] & 0xff) + 0x100, 16).substring(1));
        	}
                return buf.toString();
    	}
    	catch(NoSuchAlgorithmException ae) {
    		ae.printStackTrace();
    	}
        return "";
    }
}
