package OACRental.UI;

import OACRental.DataManager;
import OACRental.Price;
import OACRental.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.List;

public class InventoryViewerPage extends GridPane implements Page {

    TextField txtProductName;
    TextField txtProductSize;
    TaskView parent;
    //GridPane grdProducts;

    TableView<Product> tblProducts;

    ObservableList<Product> observableProducts;
    ScrollPane scrllProducts;

    public InventoryViewerPage(TaskView parent) {
        setId("pageInventory");
        this.parent = parent;

        observableProducts = FXCollections.observableArrayList();


        Label lblProductName = new Label("Product Name");
        Label lblProductSize = new Label("Product Size");

        lblProductName.setId("lblProductName");
        lblProductSize.setId("lblProductSize");

        txtProductName = new TextField();
        txtProductSize = new TextField();

        txtProductSize.setId("txtProductSize");
        txtProductName.setId("txtProductName");

        txtProductName.textProperty().addListener((observable -> this.onType()));
        txtProductSize.textProperty().addListener((observable -> this.onType()));

        VBox vboxProdName = new VBox();
        VBox vboxProdSize = new VBox();
        VBox vboxProdSearch = new VBox();

        vboxProdName.setId("vboxProdName");
        vboxProdSize.setId("vboxProdSize");
        vboxProdSearch.setId("vboxProdSearch");

        vboxProdName.getChildren().addAll(lblProductName, txtProductName);
        vboxProdSize.getChildren().addAll(lblProductSize, txtProductSize);
        vboxProdSearch.getChildren().addAll(vboxProdName, vboxProdSize);

        tblProducts = new TableView<>();
        tblProducts.setId("tblProducts");

        setupTableView();


        scrllProducts = new ScrollPane();
        scrllProducts.setId("scrllInventoryProducts");
        scrllProducts.setContent(tblProducts);
        scrllProducts.fitToWidthProperty().set(true);
        scrllProducts.fitToHeightProperty().set(true);

        ColumnConstraints onlyCol = new ColumnConstraints();
        onlyCol.setPercentWidth(100);

        getColumnConstraints().add(onlyCol);

        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();

        row1.setPercentHeight(20);
        row2.setPercentHeight(80);

        getRowConstraints().addAll(row1, row2);

        add(vboxProdSearch, 0, 0);
        add(scrllProducts, 0, 1);

        setHalignment(vboxProdSearch, HPos.CENTER);
        setHalignment(scrllProducts, HPos.CENTER);

        // Now, you may wonder what this negative margin does. Good question. I just know it makes it less ugly
        setMargin(vboxProdSearch, new Insets(-50));
        setMargin(tblProducts, new Insets(-50));

        search();
    }

    private void setupTableView() {

        // Here's a breakdown of what this is doing

        // Create a column for the table
        TableColumn firstCol = new TableColumn("Name");

        // Set the value factory, which is in charge of getting the text to put in the boxes from the item.
        // So in this case, it's supposed to pull the product name from the Product object and put in the box
        firstCol.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));

        // Set the cell factory, which is in charge of making the actual cells. So this factory makes text
        // box cells that you can type into
        firstCol.setCellFactory(TextFieldTableCell.forTableColumn());

        // Set the onEditCommit callback, so when you're done typing and hit "enter", the product is actually updated
        firstCol.setOnEditCommit(e -> {
            try {
                var event = (TableColumn.CellEditEvent<Product, String>) e;
                Product prod = event.getTableView().getItems().get(event.getTablePosition().getRow());
                prod.setName(event.getNewValue());
                updateProduct(prod);
            }
            catch (Exception ex) {
                displayError(ex);
            }
        });



        // The rest of the cells follow a similar pattern, though the ones with more exotic data types
        // necessitated some custom implementations to convert back and forth between strings



        TableColumn secondCol = new TableColumn("Size");
        secondCol.setCellValueFactory(new PropertyValueFactory<Product, String>("size"));
        secondCol.setCellFactory(TextFieldTableCell.forTableColumn());
        secondCol.setOnEditCommit(e -> {
            try {
                var event = (TableColumn.CellEditEvent<Product, String>) e;
                Product prod = event.getTableView().getItems().get(event.getTablePosition().getRow());
                prod.setSize(event.getNewValue());
                updateProduct(prod);
            }
            catch (Exception ex) {
                displayError(ex);
            }
        });

        TableColumn thirdCol = new TableColumn("Quantity");
        thirdCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantity"));
        thirdCol.setOnEditCommit(e -> {
            try {
                var event = (TableColumn.CellEditEvent<Product, Integer>) e;
                Product prod = event.getTableView().getItems().get(event.getTablePosition().getRow());
                prod.setQuantity(event.getNewValue());
                updateProduct(prod);
            }
            catch (Exception ex) {
                displayError(ex);
            }
        });

        // This shit is some real voodoo magic imma be honest that I wrote by decompiling JavaFX and
        // reverse engineering their shit

        // Basically, Tables need a "Cell Factory" to produce "Cell" objects, which contain the text,
        // in the table. So this is a custom implementation of a "Callback" object, which can produce
        // "TableCell" objects, which in this case I chose to return "TextFieldTableCell" because
        // it's nicely editable, for "TableColumn" objects.

        // But wait! The TextFieldTableCell needs a "StringConverter" object to be able to convert
        // the type to and from a string, cause it can only draw strings. So in my lambda,
        // I have _another_ lambda for a StringConverter which converts ints to strings and vice versa

        // Isn't Java cool.
        thirdCol.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn tableColumn) {
                var field = new TextFieldTableCell<Product, Integer>();
                field.converterProperty().set(new StringConverter<Integer>() {
                    @Override
                    public String toString(Integer integer) {
                        return integer.toString();
                    }

                    @Override
                    public Integer fromString(String s) {
                        return Integer.parseInt(s);
                    }
                });

                return field;
            }
        });

        TableColumn fourthCol = new TableColumn("Price");
        fourthCol.setCellValueFactory(new PropertyValueFactory<Product, Price>("price"));


        fourthCol.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn tableColumn) {
                var field = new TextFieldTableCell<Product, Price>();

                field.converterProperty().set(new StringConverter<Price>() {
                    @Override
                    public String toString(Price price) {
                        return price.toString();
                    }

                    @Override
                    public Price fromString(String s) {
                        return new Price(s);
                    }
                });

                return field;
            }
        });

        fourthCol.setOnEditCommit(e -> {
            try {
                var event = (TableColumn.CellEditEvent<Product, Price>) e;
                Product prod = event.getTableView().getItems().get(event.getTablePosition().getRow());
                prod.setPrice(event.getNewValue());
                updateProduct(prod);
            }
            catch (Exception ex) {
                displayError(ex);
            }
        });

        tblProducts.getColumns().addAll(
                firstCol,
                secondCol,
                thirdCol,
                fourthCol
        );

        tblProducts.setItems(observableProducts);
        tblProducts.setEditable(true);
    }

    private void updateProduct(Product pr)  {
        try {
            DataManager.updateProduct(pr);
        }
        catch (Exception ex) {
            displayError(ex);
        }
    }

    private void displayError(Exception ex) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Error");
        dialog.getDialogPane().getButtonTypes().add(new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE));
        dialog.setContentText("The product could not be updated due to the following error:\n\n" + ex.getMessage());

        dialog.showAndWait();
    }


    private void onType() {
        search();
    }

    private void search() {
        observableProducts.clear();

        var nameText = txtProductName.getText();
        var sizeText = txtProductSize.getText();

        List<Product> products = nameText.isEmpty() && sizeText.isEmpty() ? DataManager.getAllProducts() : DataManager.searchInventory(nameText, sizeText);

        observableProducts.addAll(products);
    }

    @Override
    public void update() {
        // Perhaps unnecessary
    }
}
