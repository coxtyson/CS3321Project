package OACRental;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javafx.scene.control.TextField;
import java.util.ArrayList;

public class CheckoutTask extends TaskView {
    ArrayList<BorderPane> pages;
    int currentPage;


    public CheckoutTask() {
        currentPage = 0;
        pages = new ArrayList<>();

        BorderPane page1 = new BorderPane();
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

        textBoxes.getChildren().addAll(vFirst, vLast, vID, vPhone, vEmail);

        page1.setCenter(textBoxes);
        pages.add(page1);

        jumpPage(0);
    }

    @Override
    public String taskName() {
        return "Checkout";
    }

    @Override
    public int pageCount() {
        return 3;
    }

    @Override
    public int currentPage() {
        return this.currentPage;
    }

    @Override
    public void nextPage() {
        if (currentPage < pages.size() - 1) {
            currentPage++;

        }
    }

    @Override
    public void prevPage() {

    }

    @Override
    public void jumpPage(int page) throws IllegalArgumentException {
        if (page >= 0 && page <= pages.size() - 1) {
            getChildren().add(pages.get(page));
        }
        else {
            throw new IllegalArgumentException("Illegal page number %d in CheckouTask".formatted(page));
        }
    }
}
