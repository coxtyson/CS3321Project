package OACRental;

import javafx.scene.layout.Pane;

public abstract class TaskView extends Pane {

    public TaskView() {
        // For visual appeal, restrict a task view to a minimum size
        setMinHeight(200);
        setMinWidth(200);
    }

    public abstract String taskName();
    public abstract int pageCount();
    public abstract int currentPage();
    public abstract void nextPage();
    public abstract void prevPage();
    public abstract void jumpPage(int page);
}
