package OACRental.UI;

import OACRental.Customer;
import OACRental.DataManager;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.util.List;

public class CustomerAddPage extends GridPane implements Page {
    TextField txtFirstName;
    TextField txtLastName;
    TextField txtID;
    TextField txtPhone;
    TextField txtEmail;

    VBox vFoundCustomers;

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

        ColumnConstraints leftCol = new ColumnConstraints();
        ColumnConstraints rightCol = new ColumnConstraints();

        leftCol.setPercentWidth(80);
        rightCol.setPercentWidth(20);

        getColumnConstraints().addAll(leftCol, rightCol);

        add(textBoxes, 0, 0);

        vFoundCustomers = new VBox();
        vFoundCustomers.setId("hboxFoundCustomers");

        ScrollPane scrllFoundCustomers = new ScrollPane(vFoundCustomers);

        add(scrllFoundCustomers, 1, 0);
        setMargin(scrllFoundCustomers, new Insets(0, 0, 0, 20)); // TODO: move this to style.css
    }

    private void onType() {
        List<Customer> matches = DataManager.searchCustomers(
                txtFirstName.getText(),
                txtLastName.getText(),
                txtID.getText(),
                txtPhone.getText(),
                txtEmail.getText(),
                true
        );

        vFoundCustomers.getChildren().clear();


        for (Customer cust : matches) {
            Button btn = new Button(String.format("%s %s\n%s", cust.getFirstName(), cust.getLastName(), cust.getID()));

            btn.setOnAction(event-> {
                DataManager.setActiveCustomer(cust);
                parent.nextPage();
            });

            vFoundCustomers.getChildren().add(btn);
        }
    }

    private void createCustomer() {
        DataManager.createCustomer(txtFirstName.getText(), txtLastName.getText(), txtID.getText(), txtPhone.getText(), txtEmail.getText());
        parent.nextPage();
    }

    @Override
    public void update() {
        // TODO: maybe leave this blank, but perhaps pull active customer here and update textboxes?
    }
}
