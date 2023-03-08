package OACRental.UI;

import OACRental.DataManager;
import OACRental.Product;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.util.List;

public class InventoryViewerPage extends BorderPane{

    TextField txtProductName;
    TextField txtProductSize;
    TaskView parent;
    GridPane grdProducts;

    public InventoryViewerPage(TaskView parent) {
        setId("pageInventory");
        this.parent = parent;
        this.grdProducts = new GridPane();

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

        setCenter(textBoxes);

        Button btnSearch = new Button("Search Inventory");
        btnSearch.setOnAction(event -> this.search());


        grdProducts.setGridLinesVisible(true);
        grdProducts.setHgap(25);
        grdProducts.setVgap(10);

        ColumnConstraints columnName = new ColumnConstraints();
        ColumnConstraints columnSize = new ColumnConstraints();
        ColumnConstraints columnQuantity = new ColumnConstraints();
        ColumnConstraints columnPrice = new ColumnConstraints();

        grdProducts.autosize();

        setBottom(grdProducts);
    }

    private void onType() {
        search();
    }

    private void search() {
        //lookup product in inventory, generate grid with results
        grdProducts.getChildren().clear();

        List<Product> matches = DataManager.searchInventory(
                txtProductName.getText(),
                txtProductSize.getText()
        );

        int rowindex = 0;
        for(Product prod : matches)
        {

            //TODO add loop that adds a new row to the grid of products in inventory, adds the product to the row, and displays it to the user
            //productGrid.addRow(productGrid.getRowCount() + 1);
            grdProducts.add(new Label(prod.getPrettyName()), 0, rowindex);
            rowindex++;
        }
    }

}
