package edu.utsa.cs3443.sceneit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneitApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneitApplication.class.getResource("/edu.utsa.cs3443/layouts/layouts/welcome-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Sceneit");
        stage.setScene(scene);
        stage.show();
    }
}
