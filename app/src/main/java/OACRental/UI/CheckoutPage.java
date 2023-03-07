package OACRental.UI;

import OACRental.DataManager;
import OACRental.Product;
import OACRental.Receipt;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.util.List;

public class CheckoutPage extends GridPane implements Page {
    TaskView parent;
    List<Product> cart;

    VBox vboxItems;

    public CheckoutPage(TaskView parent) {
        this.parent = parent;
        vboxItems = new VBox();

        add(vboxItems, 0, 0);

        Button btn = new Button("Print");
        btn.setOnAction(event -> {
            Receipt receipt = new Receipt();
            receipt.setCustomer(DataManager.getActiveCustomer());
            receipt.setItems(DataManager.getCart());

            try {
                receipt.print();
            }
            catch (Exception ex) {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Error");
                dialog.getDialogPane().getButtonTypes().add(new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE));
                dialog.setContentText("Printing could not be completed due to the following error: \n\n" + ex.toString());

                dialog.showAndWait();
            }
        });

        add(btn, 0, 1);
    }

    @Override
    public void update() {
        cart = DataManager.getCart();
        vboxItems.getChildren().clear();

        for (Product prod : cart) {
            Label lblProduct = new Label(prod.getPrettyName() + "\n" + prod.getPrice());
            vboxItems.getChildren().add(lblProduct);
        }
    }
}
