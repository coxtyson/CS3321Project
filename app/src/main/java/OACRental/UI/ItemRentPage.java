package OACRental.UI;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class ItemRentPage extends BorderPane {
    TaskView parent;
    public ItemRentPage(TaskView parent) {
        setId("pageItemRent");
        this.parent = parent;

        GridPane grdProducts = new GridPane();
        StackPane stkCart = new StackPane();
    }
}
