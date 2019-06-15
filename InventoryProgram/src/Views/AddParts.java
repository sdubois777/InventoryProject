/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Model.InhousePart;
import Model.OutsourcedPart;
import Model.Inventory;
import Model.Part;

/**
 *
 * @author hypas
 */
public class AddParts implements Initializable {
    //Parts used from the fxml
    @FXML
    private Label AddPartIHOLabel;
    @FXML
    private TextField AddPartIDField;
    @FXML
    private TextField AddPartNameField;
    @FXML
    private TextField AddPartInvField;
    @FXML
    private TextField AddPartPriceField;
    @FXML
    private TextField AddPartMinField;
    @FXML
    private TextField AddPartMaxField;
    @FXML
    private TextField AddPartIHOField;
    
    private boolean isOutsourced;
    private int partID;
    
    //Changes the label text based on which radio buttion is selected
    @FXML
    void AddPartInHouseRadio(ActionEvent event) {
        isOutsourced = false;
        AddPartIHOLabel.setText("Machine ID");
    }
    
    @FXML
    void AddPartOutsourcedRadio(ActionEvent event) {
        isOutsourced = true;
        AddPartIHOLabel.setText("Company Name");
    }
    //Saves the part based on In-House or Outsourced
    @FXML
    void AddPartSaveButton(ActionEvent event) throws IOException {
        String partName = AddPartNameField.getText();
        String partInv = AddPartInvField.getText();
        int partInventory = Integer.parseInt(partInv);
        String partPrice = AddPartPriceField.getText();
        double partPriced = Double.parseDouble(partPrice);
        String partMin = AddPartMinField.getText();
        int partMinimum = Integer.parseInt(partMin);
        String partMax = AddPartMaxField.getText();
        int partMaximum = Integer.parseInt(partMax);
        String partSource = AddPartIHOField.getText();
        
        try {                        
            if(Part.validateNewPart(partName, partMinimum, partMaximum, partInventory, partPriced) == true) {
                if (isOutsourced == false) {
                    InhousePart ihPart = new InhousePart();
                    
                    ihPart.setPartID(partID);
                    ihPart.setPartName(partName);
                    ihPart.setPartPrice(partPriced);
                    ihPart.setPartStock(partInventory);
                    ihPart.setPartMin(partMinimum);
                    ihPart.setPartMax(partMaximum);
                    ihPart.setMachineID(Integer.parseInt(partSource));
                    Inventory.addPart(ihPart);
                } else {
                    OutsourcedPart osPart = new OutsourcedPart();
                    
                    osPart.setPartID(partID);
                    osPart.setPartName(partName);
                    osPart.setPartPrice(partPriced);
                    osPart.setPartStock(partInventory);
                    osPart.setPartMin(partMinimum);
                    osPart.setPartMax(partMaximum);
                    osPart.setCompanyName(partSource);
                    Inventory.addPart(osPart);
                }
                
                Parent partSave = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
                Scene scene = new Scene(partSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Part");
            alert.setHeaderText("Error");
            alert.setContentText("Cannot have blank fields");
            alert.showAndWait();
        }
    }
    //Exits the add part screen and returns to main
    @FXML
    void AddPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Are you sure you want to cancel adding part " + AddPartNameField.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK) {
            Parent partCancel = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
            Scene scene = new Scene(partCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            System.out.println("Canceled");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partID = Inventory.getPartCount();
        AddPartIDField.setText("AUTO GEN: " + partID);
    }
}
