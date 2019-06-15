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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Model.InhousePart;
import Model.Inventory;
import Model.OutsourcedPart;
import Model.Part;
import static Views.MainScreen.partModifyIndex;
import static Model.Inventory.getAllParts;


/**
 *
 * @author hypas
 */
public class ModifyParts implements Initializable {
    
    private boolean isOutsourced;
    int partIndex = partModifyIndex();
    private int partID;
    //All parts used from the fxml
    @FXML
    private TextField ModifyPartIDField;
    @FXML
    private TextField ModifyPartNameField;
    @FXML
    private TextField ModifyPartInvField;
    @FXML
    private TextField ModifyPartPriceField;
    @FXML
    private TextField ModifyPartMinField;
    @FXML
    private TextField ModifyPartMaxField;
    @FXML
    private TextField ModifyPartIHOField;
    @FXML
    private Label ModifyPartIHOLabel;
    @FXML
    private RadioButton ModifyPartInHouseRadio;
    @FXML
    private RadioButton ModifyPartOutsourcedRadio;
    
    //Changes the label text based on In-House or Outsourced
    @FXML
    void ModifyPartOutsourcedRadio(ActionEvent event) {
        isOutsourced = true;
        ModifyPartIHOLabel.setText("Company Name");
    }
    
    @FXML
    void ModifyPartInHouseRadio(ActionEvent event) {
        isOutsourced = false;
        ModifyPartIHOLabel.setText("Machine ID");
    }
    //Exits back to the main page without saving
    @FXML
    void ModifyPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm Cancel Modify");
        alert.setContentText("Are you sure you want to cancel modification of part " + ModifyPartNameField.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.out.println("Cancelled");
            Parent partCancel = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
            Scene scene = new Scene(partCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            System.out.println("Complete part modify");
        }
    }
    //Saves a part based on In-House or Outsourced
    @FXML
    void ModifyPartSave(ActionEvent event) throws IOException {
        String partName = ModifyPartNameField.getText();
        String partInventory = ModifyPartInvField.getText();
        int partInv = Integer.parseInt(partInventory);
        String partPrice = ModifyPartPriceField.getText();
        double partPriced = Double.parseDouble(partPrice);
        String partMin = ModifyPartMinField.getText();
        int partMinimum = Integer.parseInt(partMin);
        String partMax = ModifyPartMaxField.getText();
        int partMaximum = Integer.parseInt(partMax);
        String partIHO = ModifyPartIHOField.getText();
        
        try {                       
            if (Part.validateNewPart(partName, partMinimum, partMaximum, partInv, partPriced) == true) {
                if (isOutsourced == true) {
                    OutsourcedPart outsourcedPart = new OutsourcedPart();
                    outsourcedPart.setPartID(partID);
                    outsourcedPart.setPartName(partName);
                    outsourcedPart.setPartPrice(Double.parseDouble(partPrice));
                    outsourcedPart.setPartStock(Integer.parseInt(partInventory));
                    outsourcedPart.setPartMin(Integer.parseInt(partMin));
                    outsourcedPart.setPartMax(Integer.parseInt(partMax));
                    outsourcedPart.setCompanyName(partIHO);
                    Inventory.updatePart(partIndex, outsourcedPart);
                } else {
                    InhousePart inhousePart = new InhousePart();
                    inhousePart.setPartID(partID);
                    inhousePart.setPartName(partName);
                    inhousePart.setPartPrice(Double.parseDouble(partPrice));
                    inhousePart.setPartStock(Integer.parseInt(partInventory));
                    inhousePart.setPartMin(Integer.parseInt(partMin));
                    inhousePart.setPartMax(Integer.parseInt(partMax));
                    inhousePart.setMachineID(Integer.parseInt(partIHO));
                    Inventory.updatePart(partIndex, inhousePart);                
                }
                Parent modifyProductCancel = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
                Scene scene = new Scene(modifyProductCancel);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        } catch (NumberFormatException e) {
            System.out.println("Blank Fields");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Part");
            alert.setHeaderText("Error");
            alert.setContentText("Cannot have blank fields");
            alert.showAndWait();
        } 
    }
    //Fills in the Parts table
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Part part = getAllParts().get(partIndex);
        partID = getAllParts().get(partIndex).getPartID();
        ModifyPartIDField.setText("Part ID: " + partID);
        ModifyPartNameField.setText(part.getPartName());
        ModifyPartInvField.setText(Integer.toString(part.getPartStock()));
        ModifyPartPriceField.setText(Double.toString(part.getPartPrice()));
        ModifyPartMinField.setText(Integer.toString(part.getPartMin()));
        ModifyPartMaxField.setText(Integer.toString(part.getPartMax()));
        if (part instanceof InhousePart) {
            ModifyPartIHOField.setText(Integer.toString(((InhousePart) getAllParts().get(partIndex)).getMachineID()));
            ModifyPartIHOLabel.setText("MachineID");
            ModifyPartInHouseRadio.setSelected(true);
            
        } else {
            ModifyPartIHOField.setText(((OutsourcedPart) getAllParts().get(partIndex)).getCompanyName());
            ModifyPartIHOLabel.setText("Company Name");
            ModifyPartOutsourcedRadio.setSelected(true);
            
        }
    }
}
