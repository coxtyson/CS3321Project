package OACRental.UI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CustomerAddPage extends BorderPane {
    public CustomerAddPage(TaskView parent) {
        setId("pageCustomer");

        VBox textBoxes = new VBox();
        textBoxes.setSpacing(50);

        VBox vFirst = new VBox(
                new Label("First Name"),
                new TextField()
        );

        VBox vLast = new VBox(
                new Label("Last Name"),
                new TextField()
        );

        VBox vID = new VBox(
                new Label("BengalID/Driver's License"),
                new TextField()
        );

        VBox vPhone = new VBox(
                new Label("Phone Number"),
                new TextField()
        );

        VBox vEmail = new VBox(
                new Label("Email"),
                new TextField()
        );

        Button btnAddCust = new Button("Add Customer");
        btnAddCust.setOnAction(event -> parent.nextPage());

        textBoxes.getChildren().addAll(vFirst, vLast, vID, vPhone, vEmail, btnAddCust);

        textBoxes.setMaxWidth(1000);

        setCenter(textBoxes);

        HBox foundcusts = new HBox();
        foundcusts.getChildren().add(new Button("Guy with really long name\nID: 29272"));

        setRight(foundcusts);
        setMargin(foundcusts, new Insets(0, 0, 0, 20));
    }
}
