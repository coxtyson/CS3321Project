package OACRental.UI;

import OACRental.DataManager;
import OACRental.Product;
import OACRental.Receipt;
import OACRental.Transaction;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.Scene;

import java.awt.event.ActionEvent;
import java.beans.EventHandler;
import java.util.Date;
import java.time.LocalDate;

import java.util.List;

public class CheckoutPage extends GridPane implements Page {
    TaskView parent;
    List<Product> cart;

    VBox vboxItems;
    VBox vboxTransactionControls;
    LocalDate checkoutDate;
    LocalDate returnDate;

    public CheckoutPage(TaskView parent) {
        this.parent = parent;
        setId("pageCheckout");
        vboxItems = new VBox();
        vboxItems.setId("vboxItemsBought");
        vboxTransactionControls = new VBox();
        vboxTransactionControls.setId("vboxTransactionControls");


        add(vboxItems, 0, 0);
        add(vboxTransactionControls, 1, 0);


        Label lblCheckoutDate = new Label("Checkout Date");
        DatePicker dateCheckout = new DatePicker();
        Label lblReturnDate = new Label("Return Date");
        DatePicker dateReturn = new DatePicker();
        Button btnPrint = new Button("Print Receipt");
        Button btnSave = new Button("Save Receipt");

        lblCheckoutDate.setId("lblCheckoutDate");
        dateCheckout.setId("dateCheckout");
        lblReturnDate.setId("lblReturnDate");
        dateReturn.setId("dateReturn");
        btnPrint.setId("btnPrint");
        btnSave.setId("btnSave");

        checkoutDate = LocalDate.now();
        returnDate = checkoutDate.plusDays(1);

        dateCheckout.setValue(checkoutDate);
        dateReturn.setValue(returnDate);

        vboxTransactionControls.getChildren().addAll(
                lblCheckoutDate,
                dateCheckout,
                lblReturnDate,
                dateReturn,
                new HBox(btnPrint, btnSave)
        );

        ColumnConstraints mainCol = new ColumnConstraints();
        ColumnConstraints controlCol = new ColumnConstraints();

        mainCol.setPercentWidth(60);
        controlCol.setPercentWidth(40);

        getColumnConstraints().addAll(mainCol, controlCol);

        dateCheckout.valueProperty().addListener((ov, oldValue, newValue) -> { checkoutDate = newValue; });
        dateReturn.valueProperty().addListener((ov, oldValue, newValue) -> { returnDate = newValue; });

        btnPrint.setOnAction(event -> {
            printReceipt(processTransaction());
        });

        btnSave.setOnAction(event -> {
            saveReceipt(processTransaction());
        });
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

    private Receipt processTransaction() {
        if(checkoutDate == null){
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Error");
            dialog.getDialogPane().getButtonTypes().add(new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE));
            dialog.setContentText("Please select a checkout date");

            dialog.showAndWait();
            return null;
        }

        if(returnDate == null){
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Error");
            dialog.getDialogPane().getButtonTypes().add(new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE));
            dialog.setContentText("Please select a return date");

            dialog.showAndWait();
            return null;
        }

        Transaction transaction = new Transaction(DataManager.getActiveCustomer(), DataManager.getCart(), checkoutDate, returnDate);

        Receipt receipt = new Receipt();
        receipt.setCustomer(DataManager.getActiveCustomer());
        receipt.setItems(DataManager.getCart());
        receipt.setTransaction(transaction);

        return receipt;
    }

    private void printReceipt(Receipt receipt) {
        if (receipt == null) {
            return;
        }

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
    }

    private void saveReceipt(Receipt receipt) {
        if (receipt == null) {
            return;
        }

        try {
            receipt.save("OACRecieptTest.pdf");
        }
        catch (Exception ex) {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Error");
            dialog.getDialogPane().getButtonTypes().add(new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE));
            dialog.setContentText("Saving could not be completed due to the following error: \n\n" + ex.toString());

            dialog.showAndWait();
        }
    }
}
