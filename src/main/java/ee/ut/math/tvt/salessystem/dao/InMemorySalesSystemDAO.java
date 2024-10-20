package ee.ut.math.tvt.salessystem.dao;

import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;

import java.util.ArrayList;
import java.util.List;

public class InMemorySalesSystemDAO implements SalesSystemDAO {

    private final List<StockItem> stockItemList;
    private final List<SoldItem> soldItemList;

    public InMemorySalesSystemDAO() {
        List<StockItem> items = new ArrayList<StockItem>();
        items.add(new StockItem(1L, "Lays chips", "Potato chips", 11.0, 5));
        items.add(new StockItem(2L, "Chupa-chups", "Sweets", 8.0, 8));
        items.add(new StockItem(3L, "Frankfurters", "Beer sausages", 15.0, 12));
        items.add(new StockItem(4L, "Free Beer", "Student's delight", 0.0, 100));
        this.stockItemList = items;
        this.soldItemList = new ArrayList<>();
    }

    @Override
    public List<StockItem> findStockItems() {
        return stockItemList;
    }

    @Override
    public StockItem findStockItem(long id) {
        for (StockItem item : stockItemList) {
            if (item.getId() == id)
                return item;
        }
        return null;
    }

    @Override
    public void saveSoldItem(SoldItem item) {
        soldItemList.add(item);
    }

    @Override
    public void saveStockItem(StockItem stockItem) {
        // Check if the stock item already exists using the barcode
        StockItem existingItem = findStockItemByBarcode(stockItem.getId());

        if (existingItem != null) {
            // Update the quantity if the item exists
            existingItem.setQuantity(existingItem.getQuantity() + stockItem.getQuantity());
        } else {
            // If it doesn't exist, add it as a new item
            stockItemList.add(stockItem);
        }
    }


    @Override
    public void beginTransaction() {
    }

    @Override
    public void rollbackTransaction() {
    }

    @Override
    public void commitTransaction() {
    }

    @Override
    public boolean barcodeExists(Long barcode) {
        return findStockItemByBarcode(barcode) != null;
    }

    @Override
    public void updateStockQuantity(Long barCode, int quantityChange) {
        StockItem existingItem = findStockItemByBarcode(barCode);
        if (existingItem == null) {
            throw new IllegalArgumentException("Item with this barcode doesn't exist.");
        }

        int newQuantity = existingItem.getQuantity() + quantityChange;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        existingItem.setQuantity(newQuantity);
    }


    // New method to find stock item by barcode
    public StockItem findStockItemByBarcode(long barcode) {
        for (StockItem item : stockItemList) {
            if (item.getId().equals(barcode)) {
                return item;
            }
        }
        return null; // Return null if no item with that barcode is found
    }
}
