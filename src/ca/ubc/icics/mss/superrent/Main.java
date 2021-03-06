/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent;
import ca.ubc.icics.mss.superrent.login.LoginViewController;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author warrior
 */
public class Main extends Application {

    private  Stage primaryStage;
    private applicationAccess applicationAccess;

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        final File file = new File("flag");
        final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        final FileLock fileLock = randomAccessFile.getChannel().tryLock();

        System.out.print(fileLock == null);
        if (fileLock == null) {
            Platform.exit();
        }
        
        
        
        this.primaryStage = primaryStage;
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("login/LoginView.fxml"));
        AnchorPane myPane = (AnchorPane) myLoader.load();
        LoginViewController controller = (LoginViewController) myLoader.getController();
        controller.setPrevStage(this);
        Scene myScene = new Scene(myPane,800,800,Color.BLACK);
        primaryStage.setScene(myScene);
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent arg0) {
                try {
                    fileLock.release();
                    randomAccessFile.close();
                    System.out.println("Closing");
                } catch (Exception ex) {
                    System.out.print(ex.getMessage());
                }

            }
        });
        
        primaryStage.show();
    }

    public void setUser(String role) throws IOException {
        applicationAccess = new applicationAccess();
       // primaryStage.close();
        applicationAccess.setViewForUser(role,primaryStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
