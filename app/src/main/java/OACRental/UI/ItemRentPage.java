package OACRental.UI;

import OACRental.DataManager;
import OACRental.Product;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.*;

import java.util.List;

public class ItemRentPage extends BorderPane {
    List<Product> productList;

    TaskView parent;
    public ItemRentPage(TaskView parent) {
        setId("pageItemRent");
        this.parent = parent;

        GridPane grdProducts = new GridPane();
        grdProducts.setId("grdProducts");
        StackPane stkCart = new StackPane();
        stkCart.setId("stkCart");

        productList = DataManager.getAllProducts();

        ColumnConstraints nameCol = new ColumnConstraints();
        ColumnConstraints sizeCol = new ColumnConstraints();
        ColumnConstraints priceCol = new ColumnConstraints();
        ColumnConstraints qtyCol = new ColumnConstraints();
        ColumnConstraints btnCol = new ColumnConstraints();

        nameCol.setPercentWidth(20);
        sizeCol.setPercentWidth(20);
        priceCol.setPercentWidth(20);
        qtyCol.setPercentWidth(20);
        btnCol.setPercentWidth(20);

        grdProducts.getColumnConstraints().addAll(nameCol, sizeCol, priceCol, qtyCol, btnCol);

        grdProducts.add(new Label("Product"), 0, 0);
        grdProducts.add(new Label("Size"), 1, 0);
        grdProducts.add(new Label("Price"), 2, 0);
        grdProducts.add(new Label("Qty Available"), 3, 0);

        int row = 1;
        for (var prod : productList) {
            Label lblProdName = new Label(prod.getName());
            Label lblProdSize = new Label(prod.getSize());
            Label lblProdPrice = new Label(prod.getPrice().toString());
            Label lblProdQty = new Label(Integer.toString(prod.getQuantity()));
            Button btnProdAdd = new Button("Add to cart");

            grdProducts.add(lblProdName, 0, row);
            grdProducts.add(lblProdSize, 1, row);
            grdProducts.add(lblProdPrice, 2, row);
            grdProducts.add(lblProdQty, 3, row);
            grdProducts.add(btnProdAdd, 4, row);

            row++;
        }


        grdProducts.setGridLinesVisible(true);

        setCenter(grdProducts);
        setMargin(grdProducts, new Insets(20));
        setRight(stkCart);
    }
}
