package OACRental.UI;

import javafx.scene.Node;
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
        if (page >= 0 && page <= pages.size() - 1) {
            getChildren().clear();
            getChildren().add(pages.get(page));
        }
        else {
            throw new IllegalArgumentException("Illegal page number %d in CheckouTask".formatted(page));
        }
    }
}
