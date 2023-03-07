package OACRental.UI;

import OACRental.DataManager;
import OACRental.Product;
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
