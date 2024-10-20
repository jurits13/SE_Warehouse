package ee.ut.math.tvt.salessystem.ui.controllers;

import com.sun.javafx.collections.ObservableListWrapper;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.util.ResourceBundle;

public class StockController implements Initializable {

    private final SalesSystemDAO dao;

    @FXML
    private Button addItem;
    @FXML
    private TableView<StockItem> warehouseTableView;
    @FXML
    private TextField barCodeField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField quantityField;
    @FXML
    private TableColumn<StockItem, Long> idColumn;
    @FXML
    private TableColumn<StockItem, String> nameColumn;
    @FXML
    private TableColumn<StockItem, String> priceColumn;
    @FXML
    private TableColumn<StockItem, String> quantityColumn;
    @FXML
    private TextField descriptionField;

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

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
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
    public void addItemEventHandler() {
        try {
            long barCode = Long.parseLong(barCodeField.getText());
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());

            // Validate name
            if (name.isEmpty()) {
                showAlert(AlertType.ERROR, "Invalid Input", "Product name cannot be empty.");
                return;
            }

            if (dao.barcodeExists(barCode)) {
                showAlert(AlertType.ERROR, "Duplicate Barcode", "Barcode already exists.");
                return;
            }

            if (quantity < 0) {
                try {
                    dao.updateStockQuantity(barCode, quantity);
                    showAlert(AlertType.INFORMATION, "Stock Updated", "Stock quantity updated Successfully.");
                    refreshStockItems();
                } catch (IllegalArgumentException e) {
                    showAlert(AlertType.ERROR, "Invalid Input", "Product quantity cannot be negative.");
                }
                return;
            }


            StockItem newItem = new StockItem(barCode, name, "", price, quantity);

            dao.saveStockItem(newItem);  // Save new item using DAO

            // Clear input fields
            barCodeField.clear();
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
