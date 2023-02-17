package OACRental.UI;


import OACRental.DataManager;
import OACRental.SettingsManager;
import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.util.ArrayList;

public class UI extends Application {
    ArrayList<TaskView> tasks;
    TaskView currentTask;

    GridPane mainpane;


    private void bindTask(TaskView view) {
        for (Node child : mainpane.getChildren()) {
            if (child instanceof TaskView) {
                mainpane.getChildren().remove(child);
                break;
            }
        }

        mainpane.add(view, 1, 1);
        currentTask = view;
    }

    private Scene initUI() {
        tasks = new ArrayList<>() {{
            add(new CheckoutTask());
            add(new SettingsTask());
        }};


        mainpane = new GridPane();
        mainpane.setId("grdMain");
        currentTask = null;

        VBox vboxTasks = new VBox();
        HBox hboxNav = new HBox();

        vboxTasks.setId("vboxMainTaskButtons");
        hboxNav.setId("hboxMainNavButtons");

        Button btnBack = new Button("<--");
        btnBack.setId("btnNavBack");
        btnBack.setOnAction(event -> {
            if (currentTask != null) {
                currentTask.prevPage();
            }
        });

        hboxNav.getChildren().add(btnBack);


        // Generate a button for each task view that displays that task
        for (TaskView taskView : tasks) {
            Button btn = new Button();
            btn.setText(taskView.taskName());
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(event -> bindTask(taskView));
            btn.setId("btnTask" + taskView.taskName());

            vboxTasks.getChildren().add(btn);
        }


        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();

        col1.setPrefWidth(100);
        col2.setFillWidth(true);
        col2.setHgrow(Priority.ALWAYS);

        mainpane.getColumnConstraints().addAll(col1, col2);

        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();

        row1.setPrefHeight(35);

        row2.setFillHeight(true);
        row2.setVgrow(Priority.ALWAYS);

        mainpane.getRowConstraints().addAll(row1, row2);

        mainpane.add(hboxNav, 1, 0, 1, 1);
        mainpane.add(vboxTasks, 0, 0, 1, 2);

        bindTask(tasks.get(0));

        return new Scene(mainpane);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("OAC Rental");

        Scene scene = initUI();

        scene.getStylesheets().add("style.css");

        primaryStage.setScene(scene);

        SettingsManager.loadOrCreateSettings();

        try {
            DataManager.connectToDatabase(
                    (String) SettingsManager.getSetting("database-url"),
                    (int) SettingsManager.getSetting("database-port"),
                    (String) SettingsManager.getSetting("database-name"),
                    (String) SettingsManager.getSetting("database-username"),
                    (String) SettingsManager.getSetting("database-password")
            );
        }
        catch (Exception ex) {
            System.out.println("Failed initial connection to database with reason:");
            System.out.println(ex.getMessage());
        }

        primaryStage.show();
    }
}
