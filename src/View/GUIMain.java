package View;

import View.GUIController.Select;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GUIMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader program = new FXMLLoader();
        program.setLocation(getClass().getResource("FXML/SelectProgram.fxml"));
        GridPane gridPane = program.load();

        Select controller = program.getController();

        controller.setParentStage(primaryStage);

        Scene scene = new Scene(gridPane, 1024, 720);

        primaryStage.setTitle("Select");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}