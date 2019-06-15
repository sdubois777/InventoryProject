/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Model.Inventory;
import static Model.Inventory.getAllParts;
import static Model.Inventory.getAllProducts;
import Model.Part;
import Model.Product;
import static Views.MainScreen.productModifyIndex;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hypas
 */
public class ModifyProducts implements Initializable {
//All parts used from the fxml
    @FXML
    private TextField ModifyProductIDField;
    @FXML
    private TextField ModifyProductMinField;
    @FXML
    private TextField ModifyProductMaxField;
    @FXML
    private TextField ModifyProductPriceField;
    @FXML
    private TextField ModifyProductInvField;
    @FXML
    private TextField ModifyProductNameField;
    @FXML
    private TextField ModifyProductSearchField;
    @FXML
    private TableView<Part> ModifyProductSearchTable;
    @FXML
    private TableColumn<Part, Integer> ModifyProductSearchIDColumn;
    @FXML
    private TableColumn<Part, String> ModifyProductSearchNameColumn;
    @FXML
    private TableColumn<Part, Integer> ModifyProductSearchInvColumn;
    @FXML
    private TableColumn<Part, Double> ModifyProductSearchPriceColumn;
    @FXML
    private TableView<Part> ModifyProductAddTable;
    @FXML
    private TableColumn<Part, Integer> ModifyProductAddIDColumn;
    @FXML
    private TableColumn<Part, String> ModifyProductAddNameColumn;
    @FXML
    private TableColumn<Part, Integer> ModifyProductAddInvColumn;
    @FXML
    private TableColumn<Part, Double> ModifyProductAddPriceColumn;

    /**
     * Initializes the controller class.
     */
    
    private ObservableList<Part> currentParts = FXCollections.observableArrayList();
    private int productIndex = productModifyIndex();
    private int productID;
    //Sets the tables for parts that exist and parts that are added to the current product
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Product product = getAllProducts().get(productIndex);
        productID = getAllProducts().get(productIndex).getProductID();
        System.out.println("Product ID " + productID + " is available.");
        ModifyProductIDField.setText("AUTO GEN: " + productID);
        ModifyProductNameField.setText(product.getProductName());
        ModifyProductInvField.setText(Integer.toString(product.getProductStock()));
        ModifyProductPriceField.setText(Double.toString(product.getProductPrice()));
        ModifyProductMinField.setText(Integer.toString(product.getProductMin()));
        ModifyProductMaxField.setText(Integer.toString(product.getProductMax()));
        currentParts = product.getProductParts();
        ModifyProductAddIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        ModifyProductAddNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        ModifyProductAddInvColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        ModifyProductAddPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        ModifyProductSearchIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        ModifyProductSearchNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        ModifyProductSearchInvColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        ModifyProductSearchPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        
        updateSearchPartTable();
        updateCurrentPartTable();
    }    
//Searches the list of available parts
    @FXML
    void ModifyProductSearchPartButton(ActionEvent event) {
        String searchParts = ModifyProductSearchField.getText();
        int partIndex = -1;
        if (Inventory.lookupPart(searchParts) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search");
            alert.setHeaderText("Part not found");
            alert.setContentText("Couldn't find part");
            alert.showAndWait();
        } else {
            partIndex = Inventory.lookupPart(searchParts);
            Part searchPart = Inventory.getAllParts().get(partIndex);
            ObservableList<Part> searchPartList = FXCollections.observableArrayList();
            searchPartList.add(searchPart);
            ModifyProductAddTable.setItems(searchPartList);
        }
    }
//Adds selected part to the product
    @FXML
    void ModifyProductAddButton(ActionEvent event) {
        Part part = ModifyProductSearchTable.getSelectionModel().getSelectedItem();
        currentParts.add(part);
        updateCurrentPartTable();
    }
//Deletes a part from the product
    @FXML
    void ModifyProductDeleteButton(ActionEvent event) {
        Part part = ModifyProductAddTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm Part Delete");
        alert.setContentText("Are you sure you want to remove " + part.getPartName() + "from current product?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
        currentParts.remove(part);
        updateCurrentPartTable();
        }
    }
//Cancels modification of product
    @FXML
    void ModifyProductCancelButton(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Are you sure you want to cancel update of product " + ModifyProductNameField.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK) {
            Parent productCancel = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
            Scene scene = new Scene(productCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }
//Saves the newly modified product and runs the validator across it
    @FXML
    void ModifyProductSaveButton(ActionEvent event) throws IOException {
        String productName = ModifyProductNameField.getText();
        String productInv = ModifyProductInvField.getText();
        int prodInv = Integer.parseInt(productInv);
        String productPrice = ModifyProductPriceField.getText();
        double prodPrice = Double.parseDouble(productPrice);
        String productMax = ModifyProductMaxField.getText();
        int prodMax = Integer.parseInt(productMax);
        String productMin = ModifyProductMinField.getText();
        int prodMin = Integer.parseInt(productMin);
        
        try {                        
            if (Product.validateNewProduct(productName, prodMin, prodMax, prodInv, prodPrice, currentParts) == true) {
                System.out.println("Product Name: " + productName);
                Product newProduct = new Product();
                
                newProduct.setProductID(productID);
                newProduct.setProductName(productName);
                newProduct.setProductPrice(Double.parseDouble(productPrice));
                newProduct.setProductStock(Integer.parseInt(productInv));
                newProduct.setProductMin(Integer.parseInt(productMin));
                newProduct.setProductMax(Integer.parseInt(productMax));
                newProduct.setProductParts(currentParts);
                Inventory.updateProduct(productIndex, newProduct);
                
                Parent saveProduct = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
                Scene scene = new Scene(saveProduct);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }            
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Adding Part");
            alert.setContentText("Could not add part");
            alert.showAndWait();
        }
    }
//Updates all parts table as well as included parts table
    public void updateCurrentPartTable() {
        ModifyProductAddTable.setItems(currentParts);
    }
    
    public void updateSearchPartTable() {
        ModifyProductSearchTable.setItems(getAllParts());
    }
}
