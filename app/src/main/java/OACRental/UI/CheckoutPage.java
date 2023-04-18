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
    LocalDate checkoutDate;
    LocalDate returnDate;

    public CheckoutPage(TaskView parent) {
        this.parent = parent;
        vboxItems = new VBox();

        add(vboxItems, 0, 0);

        // Gets the
        TilePane dateSelectionTile = new TilePane();
        DatePicker datePicker = new DatePicker();
        dateSelectionTile.getChildren().add(datePicker);
        add(dateSelectionTile, 2, 0);


        Button checkoutDateBtn = new Button("Checkout Date");
        checkoutDateBtn.setOnAction(event -> {
            checkoutDate = datePicker.getValue();
        });
        add(checkoutDateBtn, 2,1);

        Button returnDateBtn = new Button("Return Date");
        returnDateBtn.setOnAction(event -> {
            returnDate = datePicker.getValue();
        });
        add(returnDateBtn, 2,2);

        Button btn = new Button("Print");
        btn.setOnAction(event -> {
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
