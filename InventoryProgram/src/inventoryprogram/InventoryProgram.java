/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventoryprogram;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Views.MainScreen;
import static javafx.application.Application.launch;

/**
 *
 * @author hypas
 */
public class InventoryProgram extends Application {
    Stage window;
    private AnchorPane MainScreenView;
    //Starts up the main page
    public void startMainScreen() throws IOException {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(InventoryProgram.class.getResource("/Views/MainPage.fxml"));
        AnchorPane MainScreenView = (AnchorPane) loader.load();
        
        Scene scene = new Scene(MainScreenView);
        
        window.setScene(scene);
        window.show();
    }
    //Shows the main page
    public void showMainScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(InventoryProgram.class.getResource("/Views/MainPage.fxml"));
        
        AnchorPane MainScreenView = (AnchorPane) loader.load();
        MainScreen controller = loader.getController();
        controller.setMainApp(this);
        
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        window.setTitle("Inventory System");        
        startMainScreen();
        showMainScreen();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
