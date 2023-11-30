package OACRental.UI;

import OACRental.DataManager;
import OACRental.Price;
import OACRental.Product;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class AddItemPage extends GridPane implements Page {
    TextField txtProductName;
    TextField txtProductSize;
    TextField txtProductQuantity;
    TextField txtProductPrice;
    Button btnAddProduct;
    TaskView parent;
    CheckBox cbIsBundle;

    public AddItemPage(TaskView parent){
        setId("pageInventory");
        this.parent = parent;

        VBox textBoxes = new VBox();
        textBoxes.setId("vboxEntryFields");

        Label lblProductName = new Label("Product Name");
        Label lblProductSize = new Label("Product Size");
        Label lblProductQuantity = new Label("Product Quantity");
        Label lblProductPrice = new Label("Product Price");
        Label lblIsBundle = new Label("Is Bundle");

        lblProductName.setId("lblProductName");
        lblProductSize.setId("lblProductSize");
        lblProductQuantity.setId("lblProductQuantity");
        lblProductPrice.setId("lblProductPrice");
        lblIsBundle.setId("lblIsBundle");

        txtProductName = new TextField();
        txtProductSize = new TextField();
        txtProductQuantity = new TextField();
        txtProductPrice = new TextField();

        txtProductSize.setId("txtProductSize");
        txtProductName.setId("txtProductName");
        txtProductQuantity.setId("txtProductQuantity");
        txtProductPrice.setId("txtProductPrice");

        cbIsBundle = new CheckBox("Bundle");
        cbIsBundle.setId("cbIsBundle");
        cbIsBundle.setIndeterminate(true);


        VBox vNewProd = new VBox();

        btnAddProduct = new Button("Add Item");
        btnAddProduct.setOnAction(event -> this.addProcuct());
        btnAddProduct.setId("btnAddProduct");

        vNewProd.getChildren().addAll(lblProductName, txtProductName);
        vNewProd.getChildren().addAll(lblProductSize, txtProductSize);
        vNewProd.getChildren().addAll(lblProductQuantity, txtProductQuantity);
        vNewProd.getChildren().addAll(lblProductPrice, txtProductPrice);
        vNewProd.getChildren().addAll(cbIsBundle);
        vNewProd.getChildren().addAll(btnAddProduct);

        ColumnConstraints onlyCol = new ColumnConstraints();
        onlyCol.setPercentWidth(100);

        getColumnConstraints().add(onlyCol);

        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();

        row1.setPercentHeight(20);
        row2.setPercentHeight(80);

        getRowConstraints().addAll(row1, row2);

        add(vNewProd, 0, 1);

        setHalignment(vNewProd, HPos.CENTER);

        // Now, you may wonder what this negative margin does. Good question. I just know it makes it less ugly
        setMargin(vNewProd, new Insets(-50));

        // When add product button is pressed, addProduct function is called.
        btnAddProduct.setOnAction(event -> {
            addProcuct();
        });
    }

    @Override
    public void update() {

    }

    private void addProcuct(){
        String name = txtProductName.getText();
        String size = txtProductSize.getText();
        int quantity = Integer.parseInt(txtProductQuantity.getText());
        Price price = new Price(txtProductPrice.getText());
        boolean isBundle = cbIsBundle.isSelected();
        Product newProduct = new Product(name, size,quantity, price, isBundle, true);
        DataManager.addProduct(newProduct);
    }


}
