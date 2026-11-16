package edu.utsa.cs3443.sceneit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import edu.utsa.cs3443.sceneit.controller.WelcomeScreenController;
import edu.utsa.cs3443.sceneit.model.CsvStorage;
import edu.utsa.cs3443.sceneit.model.Manager;
import java.nio.file.Path;

public class SceneitApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Initialize the storage and manager
        CsvStorage storage = new CsvStorage(Path.of("data"));
        Manager manager = storage.load();

        // Create the FXMLLoader and set it to the welcome screen, create the scene
        FXMLLoader fxmlLoader = new FXMLLoader(SceneitApplication.class.getResource("/edu.utsa.cs3443/layouts/Welcome_Screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        // Get the main screen controller and set the manager to the one we made
        WelcomeScreenController controller = fxmlLoader.getController();
        controller.setManager(manager);

        // Launch the app
        stage.setTitle("Sceneit");
        stage.setScene(scene);
        stage.show();
    }
}
