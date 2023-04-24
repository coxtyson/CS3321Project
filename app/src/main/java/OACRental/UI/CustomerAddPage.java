package OACRental.UI;

import OACRental.Customer;
import OACRental.DataManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.util.List;

public class CustomerAddPage extends GridPane implements Page {
    TextField txtFirstName;
    TextField txtLastName;
    TextField txtID;
    TextField txtPhone;
    TextField txtEmail;

    ListView<Customer> lstCustomers;

    TaskView parent;

    public CustomerAddPage(TaskView parent) {
        setId("pageCustomer");
        this.parent = parent;

        VBox textBoxes = new VBox();
        textBoxes.setId("vboxEntryFields");

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

        vFirst.setId("vboxFirstName");
        vLast.setId("vboxLastName");
        vID.setId("vboxID");
        vPhone.setId("vboxPhone");
        vEmail.setId("vboxEmail");

        Button btnAddCust = new Button("Add Customer");
        btnAddCust.setOnAction(event -> this.createCustomer());
        btnAddCust.setId("btnAddCustomer");

        textBoxes.getChildren().addAll(vFirst, vLast, vID, vPhone, vEmail, btnAddCust);


        ColumnConstraints leftCol = new ColumnConstraints();
        ColumnConstraints rightCol = new ColumnConstraints();

        leftCol.setPercentWidth(80);
        rightCol.setPercentWidth(20);

        getColumnConstraints().addAll(leftCol, rightCol);

        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100);

        getRowConstraints().add(row);

        add(textBoxes, 0, 0);

        lstCustomers = new ListView<>();
        lstCustomers.setId("lstCustomers");

        // Set the cell factor, which is responsible for generating the text displayed from the customer objects
        lstCustomers.setCellFactory(new Callback<ListView<Customer>, ListCell<Customer>>() {
            @Override
            public ListCell<Customer> call(ListView<Customer> customerListView) {
                return new CustomerCell();
            }
        });

        // Set the selection listener, so when a value is selected, we go to the next page
        lstCustomers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                DataManager.setActiveCustomer(newValue);
                parent.nextPage();
            }
        });


        ScrollPane scrllFoundCustomers = new ScrollPane(lstCustomers);
        scrllFoundCustomers.setId("scrllFoundCustomers");
        scrllFoundCustomers.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrllFoundCustomers.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scrllFoundCustomers.fitToHeightProperty().set(true);
        scrllFoundCustomers.fitToWidthProperty().set(true);

        add(scrllFoundCustomers, 1, 0);

        setMargin(scrllFoundCustomers, new Insets(0, 0, 0, 20));
    }

    private void onType() {
        List<Customer> matches = DataManager.searchCustomers(
                txtFirstName.getText(),
                txtLastName.getText(),
                txtPhone.getText(),
                txtID.getText(),
                txtEmail.getText(),
                true
        );

        // The reason the logic here is so complicated is to reduce the number of updates
        // needed on the ListView. It tends to flicker kind of annoyingly when you do it a lot.

        if (matches.isEmpty() && !lstCustomers.getItems().isEmpty()) {  // Case 1: We have no results, clear it
            lstCustomers.getItems().clear();
        }
        else {                                                          // Case 2: We have some results, only update if they differ from current results
            boolean needsReset = false;
            for (Customer cust : matches) {
                boolean alreadyPresent = false;

                for (Customer pres : lstCustomers.getItems()) {
                    if (cust.equals(pres)) {
                        alreadyPresent = true;
                        break;
                    }
                }

                if (!alreadyPresent) {
                    needsReset = true;
                    break;
                }
            }

            if (needsReset) {
                lstCustomers.getItems().clear();
                lstCustomers.getItems().addAll(matches);
            }
        }
    }

    private void createCustomer() {
        DataManager.createCustomer(txtFirstName.getText(), txtLastName.getText(), txtID.getText(), txtPhone.getText(), txtEmail.getText());
        parent.nextPage();
    }

    @Override
    public void update() {
        // The lstCustomers selection is cleared here because ListViews will remember what they've selected,
        // and will only fire the listener set above when the selection actually changes, so if the user were
        // to select a customer, then go back, that customer would be selected, and therefore no longer able
        // to be clicked to go forward. The update function only fires when a page is first viewed, so this
        // shouldn't cause any problems here
        lstCustomers.getSelectionModel().clearSelection();

        // Clear the active customer on this page's load. That way if the user hits the back arrow,
        // the active customer is cleared. This is one approach to handling this, but I figured
        // it made sense if perhaps, you wanted to add a little text thing up in the nav bar
        // saying which customer you're checking out for, to clear it for this page.
        DataManager.setActiveCustomer(null);
    }
}
