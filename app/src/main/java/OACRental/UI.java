package OACRental;


import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import java.util.ArrayList;

public class UI extends Application {
    ArrayList<TaskView> tasks;

    BorderPane mainpane;
    ArrayList<Button> viewbuttons;
    VBox buttonsContainer;


    public Scene initUI() {
        tasks = new ArrayList<>() {{
            add(new CheckoutTask());
            add(new SettingsTask());
        }};

        mainpane = new BorderPane();
        viewbuttons = new ArrayList<>();
        buttonsContainer = new VBox();

        // Sanity check to make sure it's reasonably sized to hold buttons
        buttonsContainer.setMinHeight(100);

        // Generate a button for each task view that displays that task
        for (int i = 0; i < tasks.size(); i++) {
            Button btn = new Button();
            TaskView task = tasks.get(i);
            btn.setText(task.taskName());
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(event -> mainpane.setCenter(task));

            buttonsContainer.getChildren().add(btn);
        }


        mainpane.setLeft(buttonsContainer);
        mainpane.setCenter(tasks.get(0));

        return new Scene(mainpane);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("OAC Rental");

        Scene scene = initUI();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
