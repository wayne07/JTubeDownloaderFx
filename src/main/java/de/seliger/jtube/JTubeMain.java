package de.seliger.jtube;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JTubeMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(JTubeMain.class.getResource("jtube.fxml"));
        primaryStage.setTitle("JTubeDownloader");
        primaryStage.setScene(new Scene(root, 800, 550));
        primaryStage.show();
    }
}
