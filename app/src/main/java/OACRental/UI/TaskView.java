package OACRental.UI;

import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

public abstract class TaskView extends StackPane {
    private ArrayList<Node> pages;
    private int currentPage;

    public TaskView() {
        // For visual appeal, restrict a task view to a minimum size
        setMinHeight(100);
        setMinWidth(100);
        setId("task" + taskName());

        pages = new ArrayList<>();
        currentPage = 0;
    }

    public abstract String taskName();

    protected void addPage(Node page) {
        pages.add(page);
    }

    protected void removePage(Node page) {
        pages.remove(page);
    }

    public int pageCount() {
        return pages.size();
    }

    public int currentPage() {
        if (pages.isEmpty()) {
            return -1;
        }
        else {
            return currentPage;
        }
    }

    public void nextPage() {
        if (currentPage < pages.size() - 1) {
            currentPage++;
            jumpPage(currentPage);
        }
    }

    public void prevPage() {
        if (currentPage > 0) {
            currentPage--;
            jumpPage(currentPage);
        }
    }

    public void jumpPage(int page) {
        try {
            if (page >= 0 && page <= pages.size() - 1) {
                getChildren().clear();

                Node pageObject = pages.get(page);
                getChildren().add(pageObject);

                if (pageObject instanceof Page) {
                    ((Page) pageObject).update();
                }
            } else {
                throw new IllegalArgumentException("Illegal page number %d in CheckouTask".formatted(page));
            }
        }
        catch (Exception ex) {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Error");
            dialog.getDialogPane().getButtonTypes().add(new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE));
            dialog.setContentText("The page failed to load due to the following error:\n\n" + ex.getMessage());
            dialog.showAndWait();
        }
    }
}
