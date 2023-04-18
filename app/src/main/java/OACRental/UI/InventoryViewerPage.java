package OACRental.UI;

import OACRental.DataManager;
import OACRental.Product;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

import java.util.List;

public class InventoryViewerPage extends GridPane implements Page {

    TextField txtProductName;
    TextField txtProductSize;
    TaskView parent;
    GridPane grdProducts;

    ScrollPane scrllProducts;

    public InventoryViewerPage(TaskView parent) {
        setId("pageInventory");
        this.parent = parent;

        grdProducts = new GridPane();
        grdProducts.setId("grdInventoryProducts");

        scrllProducts = new ScrollPane();
        scrllProducts.setContent(grdProducts);

        VBox textBoxes = new VBox();
        textBoxes.setSpacing(25);

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

        VBox vProductName = new VBox(lblProductName, txtProductName);
        VBox vProductSize = new VBox(lblProductSize, txtProductSize);

        textBoxes.getChildren().addAll(vProductName, vProductSize);
        textBoxes.setMaxWidth(250);

        RowConstraints filterRow = new RowConstraints();
        RowConstraints tableRow = new RowConstraints();
        filterRow.setPercentHeight(20);
        tableRow.setPercentHeight(80);

        getRowConstraints().addAll(filterRow, tableRow);

        ColumnConstraints onlyCol = new ColumnConstraints();
        onlyCol.setPercentWidth(100);

        getColumnConstraints().add(onlyCol);

        add(textBoxes, 0, 0);

        setHalignment(textBoxes, HPos.CENTER);


        Button btnSearch = new Button("Search Inventory");
        btnSearch.setOnAction(event -> this.search());


        //grdProducts.setGridLinesVisible(true);
        grdProducts.setHgap(25);
        grdProducts.setVgap(10);

        ColumnConstraints columnName = new ColumnConstraints();
        ColumnConstraints columnSize = new ColumnConstraints();
        ColumnConstraints columnQuantity = new ColumnConstraints();
        ColumnConstraints columnPrice = new ColumnConstraints();

        columnName.setPercentWidth(25);
        columnSize.setPercentWidth(25);
        columnQuantity.setPercentWidth(25);
        columnPrice.setPercentWidth(25);

        grdProducts.getColumnConstraints().addAll(columnName, columnSize, columnPrice, columnQuantity);

        grdProducts.autosize();



        add(scrllProducts, 0, 1);
        setHalignment(scrllProducts, HPos.CENTER);

        search();
    }

    private void onType() {
        search();
    }

    private void search() {
        //lookup product in inventory, generate grid with results
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
    }

    @Override
    public void update() {
        // Perhaps unnecessary
    }
}
