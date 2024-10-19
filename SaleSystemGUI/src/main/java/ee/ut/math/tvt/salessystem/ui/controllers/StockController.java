package ee.ut.math.tvt.salessystem.ui.controllers;

import com.sun.javafx.collections.ObservableListWrapper;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


import java.net.URL;
import java.util.ResourceBundle;

public class StockController implements Initializable {

    private final SalesSystemDAO dao;

    @FXML
    private Button addItem;
    @FXML
    private TableView<StockItem> warehouseTableView;
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TableColumn<StockItem, Long> idColumn;

    @FXML
    private TableColumn<StockItem, String> nameColumn;

    @FXML
    private TableColumn<StockItem, String> priceColumn;

    @FXML
    private TableColumn<StockItem, String> quantityColumn;
    

    public StockController(SalesSystemDAO dao) {
        this.dao = dao;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TextFormatter<Double> priceFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("\\d*\\.?\\d{0,2}")) {
                return change;
            }
            return null;
        });
        priceField.setTextFormatter(priceFormatter);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        
        refreshStockItems();
        // TODO refresh view after adding new items
    }

    @FXML
    public void refreshButtonClicked() {
        refreshStockItems();
    }

    private void refreshStockItems() {
        warehouseTableView.setItems(FXCollections.observableList(dao.findStockItems()));
        warehouseTableView.refresh();
    }

    @FXML
    public void addProduct() {
        try {
            // Retrieve values from input fields
            long barCode = Long.parseLong(idField.getText());
            String name = nameField.getText();
            String description = descriptionField.getText();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());

            // Validate input
            if (name.isEmpty()) {
                showAlert(AlertType.ERROR, "Invalid Input", "Product name cannot be empty.");
                return;
            }

            if (dao.barcodeExists(barcode)) {
                showAlert(AlertType.ERROR, "Duplicate Barcode", "Barcode already exists.");
                return;
            }

            if (price < 0) {
                showAlert(AlertType.ERROR, "Invalid Input", "Price cannot be negative.");
                return;
            }

            if (quantity < 0) {
                StockItem existingItem = dao.findStockItem(barcode);
                if (existingItem != null) {
                    int newQuantity = existingItem.getQuantity() + quantity;
                    if (newQuantity < 0) {
                        showAlert(AlertType.ERROR, "Invalid Input", "Quantity cannot be negative.");
                    } else {
                        existingItem.setQuantity(newQuantity);
                        dao.saveStockItem(existingItem);
                        showAlert(AlertType.INFORMATION, "Stock updated", "Stock quantity updated successfully.");
                    }
                } else {
                    showAlert(AlertType.ERROR, "Invalid Input", "Item with this barcode doesn't exist.");
                }
                refreshStockItems();
                return;
            }
            
            // Create a new StockItem
            StockItem newItem = new StockItem(barCode, name, "", price, quantity);

            dao.saveStockItem(newItem);  // Save new item using DAO

            // Clear input fields
            idField.clear();
            nameField.clear();
            priceField.clear();
            quantityField.clear();

            refreshStockItems();  // Refresh the table to show the new product
        } catch (NumberFormatException e) {
            // Handle invalid input format (e.g., price or quantity is not a number)
            System.out.println("Invalid input: " + e.getMessage());
        }
    }
}
