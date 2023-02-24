package OACRental.UI;

import OACRental.DataManager;
import OACRental.Product;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import org.checkerframework.checker.units.qual.C;

import javax.xml.crypto.Data;
import java.util.List;

public class ItemRentPage extends GridPane {
    List<Product> productList;

    ScrollPane scrllProducts;
    ScrollPane scrllCart;

    GridPane grdCart;
    GridPane grdProducts;
    VBox vboxCart;

    TaskView parent;
    public ItemRentPage(TaskView parent) {
        setId("pageItemRent");
        this.parent = parent;

        // Set up the page layout
        ColumnConstraints leftCol = new ColumnConstraints();
        ColumnConstraints rightCol = new ColumnConstraints();

        leftCol.setPercentWidth(80);
        rightCol.setPercentWidth(20);

        getColumnConstraints().addAll(leftCol, rightCol);

        RowConstraints onlyRow = new RowConstraints();
        onlyRow.setPercentHeight(100);

        getRowConstraints().add(onlyRow);


        // Create the panels and their child controls

        grdProducts = new GridPane();
        grdProducts.setId("grdProducts");

        grdCart = new GridPane();
        grdCart.setId("grdCart");
        vboxCart = new VBox();
        vboxCart.setId("vboxCart");
        scrllProducts = new ScrollPane();
        scrllProducts.setId("scrllProducts");
        scrllCart = new ScrollPane();
        scrllCart.setId("scrllCart");

        Label lblCartHeader = new Label("Cart");
        Button btnCartCheckout = new Button("Checkout");

        scrllCart.setContent(vboxCart);
        grdCart.add(lblCartHeader, 0, 0);
        grdCart.add(scrllCart, 0, 1);
        grdCart.add(btnCartCheckout, 0, 2);

        GridPane.setHalignment(lblCartHeader, HPos.CENTER);
        GridPane.setHalignment(btnCartCheckout, HPos.CENTER);

        ColumnConstraints cartCol = new ColumnConstraints();
        cartCol.setPercentWidth(100);
        grdCart.getColumnConstraints().add(cartCol);

        RowConstraints cartItemsRow = new RowConstraints();
        cartItemsRow.setPercentHeight(90);
        grdCart.getRowConstraints().addAll(
                new RowConstraints(10),
                cartItemsRow,
                new RowConstraints(10)
        );


        updateCart();

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
            if (!prod.isActive()) {
                continue;
            }

            Label lblProdName = new Label(prod.getName());
            Label lblProdSize = new Label(prod.getSize());
            Label lblProdPrice = new Label(prod.getPrice().toString());
            Label lblProdQty = new Label(Integer.toString(prod.getQuantity()));
            Button btnProdAdd = new Button("Add to cart");

            btnProdAdd.setOnAction(event -> {
                DataManager.addProductToCart(prod);
                updateCart();
            });

            grdProducts.add(lblProdName, 0, row);
            grdProducts.add(lblProdSize, 1, row);
            grdProducts.add(lblProdPrice, 2, row);
            grdProducts.add(lblProdQty, 3, row);
            grdProducts.add(btnProdAdd, 4, row);

            row++;
        }

        setMargin(scrllProducts, new Insets(20, 0, 20, 20));
        setMargin(scrllCart, new Insets(20));


        scrllProducts.setContent(grdProducts);

        scrllProducts.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrllProducts.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);


        add(scrllProducts, 0, 0);
        add(grdCart, 1, 0);
    }

    private void updateCart() {
        vboxCart.getChildren().clear();

        List<Product> cart = DataManager.getCart();

        for (Product prod : cart) {
            Label lblProduct = new Label();

            if (prod.getSize() != null && !prod.getSize().isEmpty()) {
                lblProduct.setText(prod.getName() + " - " + prod.getSize() + "\n" + prod.getPrice());
            }
            else {
                lblProduct.setText(prod.getName() + "\n" + prod.getPrice());
            }

            vboxCart.getChildren().add(lblProduct);
        }
    }
}
