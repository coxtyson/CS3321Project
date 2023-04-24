package OACRental.UI;

import javafx.scene.control.TableCell;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import java.util.function.Function;

/**
 * This class is for use in JavaFX Tableviews. It contains a button and calls a function when
 * clicked. Inspired by https://stackoverflow.com/questions/29489366/how-to-add-button-in-javafx-table-view
 *
 * @param <S> The type the parent table contains
 */
public class ButtonCell<S> extends TableCell<S, Button> {
    private final Button button;

    public ButtonCell(String label, Function<S, S> fuction) {
        this.getStyleClass().add("table-button"); // All table buttons can be edited under .table-button css

        this.button = new Button(label);
        this.button.setOnAction(e -> fuction.apply(getReferencedItem()));
    }

    private S getReferencedItem() {
        return (S) getTableView().getItems().get(getIndex());
    }

    public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(String label, Function<S, S> function) {
        return param -> new ButtonCell<>(label, function);
    }

    @Override
    public void updateItem(Button item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        }
        else {
            setGraphic(button);
        }
    }
}
