package ee.ut.math.tvt.salessystem.ui.controllers;

import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.logic.ShoppingCart;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Encapsulates everything that has to do with the purchase tab (the tab
 * labelled "History" in the menu).
 */
public class HistoryController implements Initializable {

    private Logger log;

    public HistoryController(Logger log) {
        this.log = log;
    }

    private TextField startDateField;

    private TextField endDateField;

    private Button showLast10Button;
    private Button shoWBetweenDates;

    private Button showAllButton;

    private TableView<ShoppingCart> perchaseTableView;

    private SalesSystemDAO dao;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: implement
    }


}
