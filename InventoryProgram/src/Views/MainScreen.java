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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import inventoryprogram.InventoryProgram;
import Model.Inventory;
import Model.Part;
import static Model.Inventory.validatePartDelete;
import Model.Product;
import static Model.Inventory.getAllParts;
import static Model.Inventory.deletePart;
import static Model.Inventory.getAllProducts;
import static Model.Inventory.deleteProduct;
import javafx.scene.control.Button;

/**
 *
 * @author hypas
 */
public class MainScreen implements Initializable {
    //All parts of the fxml that are used
    @FXML
    private TableView<Part> MainPartsTable;
    @FXML
    private TableColumn<Part, Integer> MainPartIDColumn;
    @FXML
    private TableColumn<Part, String> MainPartNameColumn;
    @FXML
    private TableColumn<Part, Integer> MainPartInvColumn;
    @FXML
    private TableColumn<Part, Double> MainPartPriceColumn;
    @FXML
    private TableView<Product> MainProductsTable;
    @FXML
    private TableColumn<Product, Integer> MainProductIDColumn;
    @FXML
    private TableColumn<Product, String> MainProductNameColumn;
    @FXML
    private TableColumn<Product, Integer> MainProductInvColumn;
    @FXML
    private TableColumn<Product, Double> MainProductPriceColumn;
    @FXML
    private Button MainProductSearch;
    @FXML
    private Button MainPartSearch;
    @FXML
    private TextField MainPartsSearchField;
    @FXML
    private TextField MainProductSearchField;
    
    private static Part modifyPart;
    private static int modifyPartIndex;
    private static Product modifyProduct;
    private static int modifyProductIndex;
    
    public static int partModifyIndex() {
        return modifyPartIndex;
    }
    
    public static int productModifyIndex() {
        return modifyProductIndex;
    }
    
    public MainScreen() {
        
    }
    //Exits the application
    @FXML
    void MainExitClicked(ActionEvent event) {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
    //Loads the add part page
    @FXML
    void MainAddPartClicked(ActionEvent event) throws IOException {
        Parent addPart = FXMLLoader.load(getClass().getResource("AddPartScreen.fxml"));
        Scene scene = new Scene(addPart);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    //Loads the add product page
    @FXML
    void MainAddProductClicked(ActionEvent event) throws IOException {
        if (getAllParts().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("No parts");
            alert.setContentText("No parts available to make new product.");
            alert.showAndWait();
        } else {
        Parent addProduct = FXMLLoader.load(getClass().getResource("AddProductScreen.fxml"));
        Scene scene = new Scene(addProduct);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        }
    }
    //Loads the modify part page
    @FXML
    void MainModifyPartClicked(ActionEvent event) throws IOException {
        if (MainPartsTable.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No part");
            alert.setHeaderText("Must select a part");
            alert.setContentText("No part was selected");
            alert.showAndWait();
        } else {
            modifyPart = MainPartsTable.getSelectionModel().getSelectedItem();
            modifyPartIndex = getAllParts().indexOf(modifyPart);

            Parent modifyParts = FXMLLoader.load(getClass().getResource("ModifyPartScreen.fxml"));
            Scene scene = new Scene(modifyParts);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }
    //Loads the modify product page
    @FXML
    void MainModifyProductClicked(ActionEvent event) throws IOException {
        if (MainProductsTable.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No product");
            alert.setHeaderText("Must select a product");
            alert.setContentText("No product was selected");
            alert.showAndWait();
        } else {
            modifyProduct = MainProductsTable.getSelectionModel().getSelectedItem();
            modifyProductIndex = getAllProducts().indexOf(modifyProduct);

            Parent modifyProducts = FXMLLoader.load(getClass().getResource("ModifyProductScreen.fxml"));
            Scene scene = new Scene(modifyProducts);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }
    //Searches the table of available products
    @FXML
    void MainSearchProductsClicked(ActionEvent event) throws IOException {
        
        String searchProducts = MainProductSearchField.getText();
        int productIndex = -1;
        if (MainProductSearch.getText() == "Clear") {
            updateProductTable();
            MainProductSearch.setText("Search");
        } else {
            if (Inventory.lookupProduct(searchProducts) == -1) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Search Error");
                alert.setHeaderText(searchProducts + " not found");
                alert.setContentText("No matches found");
                alert.showAndWait();
            } else {
                MainProductSearch.setText("Clear");
                productIndex = Inventory.lookupProduct(searchProducts);
                Product tempProduct = Inventory.getAllProducts().get(productIndex);
                ObservableList<Product> tempProductList = FXCollections.observableArrayList();
                tempProductList.add(tempProduct);
                MainProductsTable.setItems(tempProductList);
            }        
        }
    }
    //Deletes a product from the table
    @FXML
    void MainDeleteProductClicked(ActionEvent event) throws IOException {
        Product product = MainProductsTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Delete Product");
        alert.setHeaderText("Confirm");
        alert.setContentText("Are you sure you want to delete " + product + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            deleteProduct(product);
            updateProductTable();
            System.out.println("Product" + product + " was removed.");
        }
    }
    //Searches the parts table, button switches to a "clear" function after searching to reset the table
    @FXML
    void MainSearchPartsClicked(ActionEvent event) throws IOException {
        
        String searchPart = MainPartsSearchField.getText();
        int partIndex = -1;
        if (MainPartSearch.getText() == "Clear") {
            updatePartTable();
            MainPartSearch.setText("Search");
        } else {
            if (Inventory.lookupPart(searchPart) == -1) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Search Error");
                alert.setHeaderText(searchPart + " not found");
                alert.setContentText("No matches found");
                alert.showAndWait();
            } else {
                MainPartSearch.setText("Clear");
                partIndex = Inventory.lookupPart(searchPart);
                Part tempPart = Inventory.getAllParts().get(partIndex);
                ObservableList<Part> tempPartList = FXCollections.observableArrayList();
                tempPartList.add(tempPart);
                MainPartsTable.setItems(tempPartList);
            }
        }
    }
    //Removes a part from the table
    @FXML
    void MainDeletePartClicked(ActionEvent event) throws IOException {
        Part part = MainPartsTable.getSelectionModel().getSelectedItem();
        if (validatePartDelete(part)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Part Delete Error");
            alert.setHeaderText("Unable to delete part.");
            alert.setContentText("This part is used in a product.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Delete Product");
            alert.setHeaderText("Confirm");
            alert.setContentText("Are you sure you want to delete " + part + "?");
            Optional<ButtonType> result = alert.showAndWait();
            
            if (result.get() == ButtonType.OK) {
                deletePart(part);
                updatePartTable();
                System.out.println(part.getPartName() + " was removed.");
            } else {
                System.out.println(part.getPartName() + " was not removed.");
            }
        }
    }
    //Fills in the parts and products tables whenever this page is reloaded
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainPartIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        MainPartNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        MainPartInvColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        MainPartPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        MainProductIDColumn.setCellValueFactory(cellData -> cellData.getValue().productIDProperty().asObject());
        MainProductNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        MainProductInvColumn.setCellValueFactory(cellData -> cellData.getValue().productInvProperty().asObject());
        MainProductPriceColumn.setCellValueFactory(cellData -> cellData.getValue().productPriceProperty().asObject());
        updatePartTable();
        updateProductTable();
    }
    //Update methods for parts and products tables
    public void updatePartTable() {
        MainPartsTable.setItems(getAllParts());
    }
    
    public void updateProductTable() {
        MainProductsTable.setItems(getAllProducts());
    }
    
    public void setMainApp(InventoryProgram mainApp) {
        updatePartTable();
        updateProductTable();
    }
    
}
