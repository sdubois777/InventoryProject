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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
/**
 *
 * @author hypas
 */
public class Product {
    
    private static ObservableList<Part> parts = FXCollections.observableArrayList();
    private final IntegerProperty productID;
    private final StringProperty productName;
    private final DoubleProperty productPrice;
    private final IntegerProperty productStock;
    private final IntegerProperty productMin;
    private final IntegerProperty productMax;
    
    public Product() {
        productID = new SimpleIntegerProperty();
        productName = new SimpleStringProperty();
        productPrice = new SimpleDoubleProperty();
        productStock = new SimpleIntegerProperty();
        productMin = new SimpleIntegerProperty();
        productMax = new SimpleIntegerProperty();
    }
    
    //setters
    public void setProductID(int productID) {
        this.productID.set(productID);
    }
    
    public void setProductName(String productName) {
        this.productName.set(productName);        
    }
    
    public void setProductPrice(double productPrice) {
        this.productPrice.set(productPrice);
    }
    
    public void setProductStock(int productStock) {
        this.productStock.set(productStock);
    }
    
    public void setProductMin(int productMin) {
        this.productMin.set(productMin);
    }
    
    public void setProductMax(int productMax) {
        this.productMax.set(productMax);
    }
    
    public void setProductParts(ObservableList<Part> parts) {
        Product.parts = parts;
    }
    
    //getters
    public IntegerProperty productIDProperty() {
        return productID;
    }
    
    public StringProperty productNameProperty() {
        return productName;
    }
    
    public DoubleProperty productPriceProperty() {
        return productPrice;
    }
    
    public IntegerProperty productInvProperty() {
        return productStock;
    }
    
    public int getProductID() {
        return this.productID.get();
    }
    
    public String getProductName() {
        return this.productName.get();
    }
    
    public double getProductPrice() {
        return this.productPrice.get();
    }
    
    public int getProductStock() {
        return this.productStock.get();
    }
    
    public int getProductMin() {
        return this.productMin.get();
    }
    
    public int getProductMax() {
        return this.productMax.get();
    }
    
    public ObservableList getProductParts() {
        return parts;
    }
    //This is all of the exception handling for adding or modifying products
    public static boolean validateNewProduct(String name, int min, int max, int inv, double price, ObservableList<Part> parts) {
        boolean isValid = true;
        double sumParts = 0.00;
        for (int i = 0; i < parts.size(); i++) {
            sumParts = sumParts + parts.get(i).getPartPrice();
        }
        if (name.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Name");
            alert.setContentText("You must enter a name");
            alert.showAndWait();
            isValid = false;
        } else {
            if (min < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.NONE);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Min");
                alert.setContentText("Minimum amount must be greater than 0");
                alert.showAndWait();
                isValid = false;
            } else {
                if (price < 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initModality(Modality.NONE);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Price");
                    alert.setContentText("Price must be greater than 0");
                    alert.showAndWait();
                    isValid = false;
                } else {
                    if (min > max) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.initModality(Modality.NONE);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid Min");
                        alert.setContentText("Minimum cannot be greater than the max");
                        alert.showAndWait();
                        isValid = false;
                    } else {
                        if (inv < min || inv > max) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.initModality(Modality.NONE);
                            alert.setTitle("Error");
                            alert.setHeaderText("Invalid Inv");
                            alert.setContentText("Inventory amount must be between min and max");
                            alert.showAndWait();
                            isValid = false;
                        } else {
                            if (parts.size() < 1) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.initModality(Modality.NONE);
                                alert.setTitle("Error");
                                alert.setHeaderText("No Parts");
                                alert.setContentText("Product must contain at lest 1 part");
                                alert.showAndWait();
                                isValid = false;
                            } else {
                                if (sumParts > price) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.initModality(Modality.NONE);
                                    alert.setTitle("Error");
                                    alert.setHeaderText("Invalid Price");
                                    alert.setContentText("Product price must be greater than sum of parts price");
                                    alert.showAndWait();
                                    isValid = false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return isValid;
    }
}
