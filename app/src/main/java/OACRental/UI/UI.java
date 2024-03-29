package OACRental.UI;


import OACRental.DataManager;
import OACRental.SettingsManager;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UI extends Application {
    ArrayList<TaskView> tasks;
    TaskView currentTask;

    static Scene scene; // Static, this app only ever uses one scene
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
            add(new InventoryTask());
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

        col1.setMinWidth(100);  // This is set to min width instead of prefwidth to 100% guarantee the nav buttons are always visible
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

        SettingsManager.loadOrCreateSettings();

        try {
            DataManager.connectToDatabase(
                    (String) SettingsManager.getSettingValue("Database URL"),
                    (int) SettingsManager.getSettingValue("Database Port"),
                    (String) SettingsManager.getSettingValue("Database Name"),
                    (String) SettingsManager.getSettingValue("Database Username"),
                    (String) SettingsManager.getSettingValue("Database Password")
            );
        }
        catch (Exception ex) {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Error");
            dialog.getDialogPane().getButtonTypes().add(new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE));
            dialog.setContentText("Failed to connect with the database.\nAre your database settings correct?\n\nError:\n\n" + ex.toString());

            dialog.showAndWait();
        }

        scene = initUI();

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.CLOSE_BRACKET) {  // Keycode close bracket is the ']' symbol
                this.debugDumpSceneHierarchy(scene.getRoot(), 0, null);
            }
        });

        setStyle((String) SettingsManager.getSettingValue("Theme"));

        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void setStyle(String name) {
        URL u = UI.class.getResource("/" + name + ".css");

        if (u == null) {
            System.out.println("Not applying style " + name + ", file not found");
            return;
        }


        if (!scene.getStylesheets().isEmpty()) {
            scene.getStylesheets().clear();
        }

        scene.getStylesheets().add(name + ".css");
    }

    private void debugDumpSceneHierarchy(Node node, int depth, List<String> hierarchy_ids) {
        List<String> ids = null;

        ids = Objects.requireNonNullElseGet(hierarchy_ids, ArrayList::new);

        for (String previd : ids) {
            if (previd.equals(node.getId())) {
                throw new RuntimeException("Two nodes have the same id of " + previd);
            }
            else if (node.getId() != null) {
                ids.add(node.getId());
            }
        }

        StringBuilder linebuilder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            linebuilder.append("-");
        }

        String classname = node.getClass().getName();
        String[] namecomps = classname.split("\\.");
        String singlewordname = namecomps[namecomps.length - 1];

        linebuilder.append(" ");
        linebuilder.append("[").append(singlewordname).append("]");
        linebuilder.append("[").append(node.getId()).append("]");

        System.out.println(linebuilder.toString());

        if (node instanceof Parent) {
            Parent parent = (Parent) node;

            for (Node child : parent.getChildrenUnmodifiable()) {
                debugDumpSceneHierarchy(child, depth + 1, ids);
            }
        }
    }

}
