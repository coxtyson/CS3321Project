package OACRental.UI;

import OACRental.DataManager;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CustomerAddPage extends BorderPane {
    TextField txtFirstName;
    TextField txtLastName;
    TextField txtID;
    TextField txtPhone;
    TextField txtEmail;

    TaskView parent;

    public CustomerAddPage(TaskView parent) {
        setId("pageCustomer");
        this.parent = parent;

        VBox textBoxes = new VBox();
        textBoxes.setSpacing(50);

        Label lblFirstName = new Label("First Name");
        Label lblLastName = new Label("Last Name");
        Label lblID = new Label("BengalID/Driver's License");
        Label lblPhone = new Label("Phone");
        Label lblEmail = new Label("Email");

        lblFirstName.setId("lblFirstName");
        lblLastName.setId("lblLastName");
        lblID.setId("lblID");
        lblPhone.setId("lblPhone");
        lblEmail.setId("lblEmail");

        txtFirstName = new TextField();
        txtLastName = new TextField();
        txtID = new TextField();
        txtPhone = new TextField();
        txtEmail = new TextField();

        txtFirstName.setId("txtFirstName");
        txtLastName.setId("txtLastName");
        txtID.setId("txtID");
        txtPhone.setId("txtPhone");
        txtEmail.setId("txtEmail");

        txtFirstName.textProperty().addListener((observable -> this.onType()));
        txtLastName.textProperty().addListener((observable -> this.onType()));
        txtID.textProperty().addListener((observable -> this.onType()));
        txtPhone.textProperty().addListener((observable -> this.onType()));
        txtEmail.textProperty().addListener((observable -> this.onType()));

        VBox vFirst = new VBox(lblFirstName, txtFirstName);
        VBox vLast = new VBox(lblLastName, txtLastName);
        VBox vID = new VBox(lblID, txtID);
        VBox vPhone = new VBox(lblPhone, txtPhone);
        VBox vEmail = new VBox(lblEmail, txtEmail);

        Button btnAddCust = new Button("Add Customer");
        btnAddCust.setOnAction(event -> this.createCustomer());

        textBoxes.getChildren().addAll(vFirst, vLast, vID, vPhone, vEmail, btnAddCust);

        textBoxes.setMaxWidth(1000);

        setCenter(textBoxes);

        HBox foundcusts = new HBox();
        foundcusts.getChildren().add(new Button("Guy with really long name\nID: 29272"));

        setRight(foundcusts);
        setMargin(foundcusts, new Insets(0, 0, 0, 20));
    }

    private void onType() {
        // DataManager - lookup customers, generate button
    }

    private void createCustomer() {
        // DataManager - create customer
        parent.nextPage();
    }
}
