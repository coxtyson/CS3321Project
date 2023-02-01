package OACRental;


import javafx.application.Application;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class UI extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World");
        Button btn = new Button("Hello World");

        btn.setOnAction(event -> { System.out.println("Hello!"); });

        StackPane pane = new StackPane();
        pane.getChildren().add(btn);

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
