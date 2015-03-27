/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.database;
import java.sql.*; 
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Avinash
 */
public class SQLConnection {
  private static SQLConnection mycon = null;
    private static String username;
    private static String password;
    protected Connection con = null;
    protected boolean driverLoaded = false;


    /*
     * The constructor is declared protected so that only subclasses
     * can access it.
     */ 
    protected SQLConnection() {

    }

    
    
    public static SQLConnection getInstance() {
    if (mycon == null) {
    mycon = new SQLConnection(); 
    }

    return mycon;
    }


  
    public boolean connect(String username, String password) {
    try{
    
    String url = "jdbc:mysql://dbserver.mss.icics.ubc.ca/team02";

    if (!driverLoaded) {
                  
                    DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                    driverLoaded = true; 
    }
                
    con = DriverManager.getConnection(url, username, password);

    con.setAutoCommit(false);
                
               
                SQLConnection.username = username;
                SQLConnection.password = password;
    return true; 
    } catch (SQLException ex) {
    return false; 
    }
    }


    /*
     * Returns the connection
     */
    public Connection getConnection() {
    return con; 
    }

    /**
     * 
     * close the current connection and returns a new connection
     */
    public Connection refreshConnection() {
        // close the current connection
        if (con!=null) {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(SQLConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
      
        if (this.connect(SQLConnection.username, SQLConnection.password)==true) {
            System.out.println("Successfully refesh the connection to database!");
            return con;
        } else {
            System.out.println("Failed to get an new connection to database!");
            return null;
        }

    }

    /*
     * Sets the connection
     */
    public void setConnection(Connection connect) {
    con = connect; 
    }


    /*
     * Returns true if the driver is loaded; false otherwise
     */ 
    public boolean isDriverLoaded() {
    return driverLoaded; 
    }


    /*
     * This method allows members of this class to clean up after itself and
     * before it is garbage collected. It is called by the garbage collector.
     */ 
    protected void finalize() throws Throwable {	
    if (con != null) {
    con.close();
    }

    
    super.finalize();	
    }  
}
