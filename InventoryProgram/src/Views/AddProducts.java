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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Model.Inventory;
import Model.Part;
import Model.Product;
import static Model.Inventory.getAllParts;


/**
 *
 * @author hypas
 */
public class AddProducts implements Initializable {
    private final ObservableList<Part> includedParts = FXCollections.observableArrayList();
    private int productID;
    
    //All parts used from the fxml
    @FXML
    private TextField AddProductIDField;
    @FXML
    private TextField AddProductNameField;
    @FXML
    private TextField AddProductPriceField;
    @FXML
    private TextField AddProductInvField;
    @FXML
    private TextField AddProductMinField;
    @FXML
    private TextField AddProductMaxField;
    @FXML
    private TextField AddProductSearchPartField;
    @FXML
    private TableView<Part> AddProductAddPartTable;
    @FXML
    private TableColumn<Part, Integer> AddProductAddPartIDColumn;
    @FXML
    private TableColumn<Part, String> AddProductAddPartNameColumn;
    @FXML
    private TableColumn<Part, Integer> AddProductAddPartInvColumn;
    @FXML
    private TableColumn<Part, Double> AddProductAddPartPriceColumn;
    @FXML
    private TableView<Part> AddProductPartsIncludedTable;
    @FXML
    private TableColumn<Part, Integer> AddProductPartsIncludedIDColumn;
    @FXML
    private TableColumn<Part, String> AddProductPartsIncludedNameColumn;
    @FXML
    private TableColumn<Part, Integer> AddProductPartsIncludedInvColumn;
    @FXML
    private TableColumn<Part, Double> AddProductPartsIncludedPriceColumn;
    
    
    public AddProducts() {
        
    }
    //Searches for a part and alerts if it isn't found
    @FXML
    void AddProductSearchPartButton(ActionEvent event) {
        
        String searchPart = AddProductSearchPartField.getText();
        int partIndex = -1;
        
        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search");
            alert.setHeaderText("Invalid Part");
            alert.setContentText("Part not found");
            alert.showAndWait();
        } else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getAllParts().get(partIndex);
            ObservableList<Part> tempProductList = FXCollections.observableArrayList();
            tempProductList.add(tempPart);
            AddProductAddPartTable.setItems(tempProductList);
        }
    }
    
    @FXML
    void AddProductAddPartButton(ActionEvent event) {
        Part part = AddProductAddPartTable.getSelectionModel().getSelectedItem();
        includedParts.add(part);
        updateIncludedParts();
    }
    //Removes a part from the product being created
    @FXML
    void AddProductDeletePartButton(ActionEvent event) {
        Part part = AddProductPartsIncludedTable.getSelectionModel().getSelectedItem();
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm Delete Part");
        alert.setContentText("Are you sure you want to delete part " + part + " from product?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.YES) {
            System.out.println("Removed part from product");
            includedParts.remove(part);
        }
    }
    //Saves the new product and runs the validator across it
    @FXML
    void AddProductSaveButton(ActionEvent event) throws IOException {
        
        String productName = AddProductNameField.getText();
        String productInv = AddProductInvField.getText();
        int prodInv = Integer.parseInt(productInv);
        String productPrice = AddProductPriceField.getText();
        double prodPrice = Double.parseDouble(productPrice);
        String productMin = AddProductMinField.getText();
        int prodMin = Integer.parseInt(productMin);
        String productMax = AddProductMaxField.getText();
        int prodMax = Integer.parseInt(productMax);
        
        try {                       
            if (Product.validateNewProduct(productName, prodMin, prodMax, prodInv, prodPrice, includedParts) == true) {
                System.out.println("Product Name: " + productName);
                Product newProduct = new Product();
                
                newProduct.setProductID(productID);
                newProduct.setProductName(productName);
                newProduct.setProductPrice(Double.parseDouble(productPrice));
                newProduct.setProductStock(Integer.parseInt(productInv));
                newProduct.setProductMin(Integer.parseInt(productMin));
                newProduct.setProductMax(Integer.parseInt(productMax));
                newProduct.setProductParts(includedParts);
                Inventory.addProduct(newProduct);
                
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
    
    @FXML
    void AddProductCancelButton(ActionEvent event) throws IOException {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Are you sure you want to cancel new product " + AddProductNameField.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK) {
            Parent partCancel = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
            Scene scene = new Scene(partCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }
    //Fills in the part inventory table when opening this page
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AddProductAddPartIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        AddProductAddPartNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        AddProductAddPartInvColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        AddProductAddPartPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        AddProductPartsIncludedIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        AddProductPartsIncludedNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        AddProductPartsIncludedInvColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        AddProductPartsIncludedPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        
        updatePartTable();
        updateIncludedParts();
        
        productID = Inventory.getProductCount();
        AddProductIDField.setText("Auto Gen: " + productID);
    }
    
    public void updatePartTable() {
        AddProductAddPartTable.setItems(getAllParts());
    }
    
    public void updateIncludedParts() {
        AddProductPartsIncludedTable.setItems(includedParts);
    }
}
