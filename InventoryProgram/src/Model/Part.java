/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
/**
 *
 * @author hypas
 */
public class Part {
    
    private final StringProperty partName;
    private final IntegerProperty partID;
    private final DoubleProperty partPrice;
    private final IntegerProperty partStock;
    private final IntegerProperty partMin;
    private final IntegerProperty partMax;
    
    public Part() {
        partID = new SimpleIntegerProperty();
        partName = new SimpleStringProperty();
        partPrice = new SimpleDoubleProperty();
        partStock = new SimpleIntegerProperty();
        partMin = new SimpleIntegerProperty();
        partMax = new SimpleIntegerProperty();
    }
    
    public IntegerProperty partIDProperty() {
        return partID;
    }

    public StringProperty partNameProperty() {
        return partName;
    }

    public DoubleProperty partPriceProperty() {
        return partPrice;
    }

    public IntegerProperty partInvProperty() {
        return partStock;
    }
    
    //setters
    
    public void setPartID(int partID) {
        this.partID.set(partID);
    }
    
    public void setPartName(String partName) {
        this.partName.set(partName);
    }
    
    public void setPartPrice(double partPrice) {
        this.partPrice.set(partPrice);
    }
    
    public void setPartStock(int partStock) {
        this.partStock.set(partStock);
    }
    
    public void setPartMin(int partMin) {
        this.partMin.set(partMin);
    }
    
    public void setPartMax(int partMax) {
        this.partMax.set(partMax);
    }
    
    //getters
    
    public int getPartID() {
        return this.partID.get();
    }
    
    public String getPartName() {
        return this.partName.get();
    }
    
    public double getPartPrice() {
        return this.partPrice.get();
    }
    
    public int getPartStock() {
        return this.partStock.get();
    }
    
    public int getPartMin() {
        return this.partMin.get();
    }
    
    public int getPartMax() {
        return this.partMax.get();
    }
    //This is all of the exception handling for adding or modifying parts
    public static boolean validateNewPart(String name, int min, int max, int inv, double price) {
        boolean isValid = true;
        
        if (name.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Name");
            alert.setContentText("You must enter a name");
            alert.showAndWait();
            isValid = false;
        }
        if (min < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Min");
            alert.setContentText("Minimum amount must be greater than 0");
            alert.showAndWait();
            isValid = false;
        }
        if (price < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Price");
            alert.setContentText("Price must be greater than 0");
            alert.showAndWait();
            isValid = false;
        }
        if (min > max) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Min");
            alert.setContentText("Minimum cannot be greater than the max");
            alert.showAndWait();
            isValid = false;
        }
        if (inv < min || inv > max) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Inv");
            alert.setContentText("Inventory amount must be between min and max");
            alert.showAndWait();
            isValid = false;
        }
        
        return isValid;
    }
}
