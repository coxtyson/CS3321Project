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
        vboxTransactionControls = new VBox();
        vboxTransactionControls.setId("vboxTransactionControls");


        add(vboxItems, 0, 0);
        add(vboxTransactionControls, 1, 0);


        Label lblCheckoutDate = new Label("Checkout Date");
        DatePicker dateCheckout = new DatePicker();
        Label lblReturnDate = new Label("Return Date");
        DatePicker dateReturn = new DatePicker();
        Button btnPrint = new Button("Print Receipt");

        checkoutDate = LocalDate.now();
        returnDate = checkoutDate.plusDays(1);

        dateCheckout.setValue(checkoutDate);
        dateReturn.setValue(returnDate);

        vboxTransactionControls.getChildren().addAll(
                lblCheckoutDate,
                dateCheckout,
                lblReturnDate,
                dateReturn,
                btnPrint
        );

        ColumnConstraints mainCol = new ColumnConstraints();
        ColumnConstraints controlCol = new ColumnConstraints();

        mainCol.setPercentWidth(60);
        controlCol.setPercentWidth(40);

        getColumnConstraints().addAll(mainCol, controlCol);

        dateCheckout.valueProperty().addListener((ov, oldValue, newValue) -> { checkoutDate = newValue; });
        dateCheckout.valueProperty().addListener((ov, oldValue, newValue) -> { returnDate = newValue; });

        btnPrint.setOnAction(event -> {
            if(checkoutDate == null){
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Error");
                dialog.getDialogPane().getButtonTypes().add(new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE));
                dialog.setContentText("Please select a checkout date");

                dialog.showAndWait();
                return;
            }

            if(returnDate == null){
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Error");
                dialog.getDialogPane().getButtonTypes().add(new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE));
                dialog.setContentText("Please select a return date");

                dialog.showAndWait();
                return;
            }

            Transaction transaction = new Transaction(DataManager.getActiveCustomer(), DataManager.getCart(), checkoutDate, returnDate);

            Receipt receipt = new Receipt();
            receipt.setCustomer(DataManager.getActiveCustomer());
            receipt.setItems(DataManager.getCart());
            receipt.setTransaction(transaction);

            // commented out printing to a printer
            /*
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
            */
            // saves the reciept to a pdf
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
}
