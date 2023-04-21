package OACRental.UI;

import OACRental.DataManager;
import OACRental.Price;
import OACRental.Product;
import com.google.common.collect.Table;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import javafx.util.StringConverter;

import javax.swing.*;
import java.util.List;

public class InventoryViewerPage extends GridPane implements Page {

    TextField txtProductName;
    TextField txtProductSize;
    TaskView parent;
    //GridPane grdProducts;

    TableView<Product> tblProducts;

    ScrollPane scrllProducts;

    public InventoryViewerPage(TaskView parent) {
        setId("pageInventory");
        this.parent = parent;

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

        TableColumn firstCol = new TableColumn("Name");
        firstCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        firstCol.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn secondCol = new TableColumn("Size");
        secondCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        secondCol.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn thirdCol = new TableColumn("Quantity");
        thirdCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

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
        fourthCol.setCellValueFactory(new PropertyValueFactory<>("price"));

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

        tblProducts.getColumns().addAll(
                firstCol,
                secondCol,
                thirdCol,
                fourthCol
        );

        tblProducts.setEditable(true);

        scrllProducts = new ScrollPane();
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

    private void onType() {
        search();
    }

    private void search() {
        tblProducts.getItems().clear();

        var nameText = txtProductName.getText();
        var sizeText = txtProductSize.getText();

        List<Product> products = nameText.isEmpty() && sizeText.isEmpty() ? DataManager.getAllProducts() : DataManager.searchInventory(nameText, sizeText);

        tblProducts.getItems().addAll(products);

        //lookup product in inventory, generate grid with results
        /*
        grdProducts.getChildren().clear();

        var nameText = txtProductName.getText();
        var sizeText = txtProductSize.getText();

        List<Product> products = null;

        if (nameText.isEmpty() && sizeText.isEmpty()) {
            products = DataManager.getAllProducts();
        }
        else {
            products = DataManager.searchInventory(nameText, sizeText);
        }

        if (products == null) {
            return; // Extra safety check, in case something goes wrong internally
        }

        int rowindex = 0;
        for(Product prod : products) {
            TextField txtName = new TextField(prod.getName());
            TextField txtSize = new TextField(prod.getSize());
            TextField txtPrice = new TextField(prod.getPrice().toString());
            TextField txtQty = new TextField(Integer.toString(prod.getQuantity()));

            grdProducts.add(txtName, 0, rowindex);
            grdProducts.add(txtSize, 1, rowindex);
            grdProducts.add(txtPrice, 2, rowindex);
            grdProducts.add(txtQty, 3, rowindex);

            rowindex++;
        }
         */
    }

    @Override
    public void update() {
        // Perhaps unnecessary
    }
}
