package edu.utsa.cs3443.sceneit.controller;
import edu.utsa.cs3443.sceneit.model.Manager;
import edu.utsa.cs3443.sceneit.model.WatchStatus;
import edu.utsa.cs3443.sceneit.model.Media;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import edu.utsa.cs3443.sceneit.model.CsvStorage;
import javafx.stage.Modality;
import java.nio.file.Path;

import java.io.IOException;

public class MainScreenController {
    private Manager manager;
    @FXML
    private ListView<Media> mediaListView;

    public void setManager(Manager manager) {
        this.manager = manager;
        showWatched(); // By default, we show the Watched list as entry to the main-screen
    }

    @FXML
    private void showWatched() {
        mediaListView.getItems().setAll(manager.getByStatus(WatchStatus.WATCHED));
    }
    @FXML
    private void showWantToWatch() {
        mediaListView.getItems().setAll(manager.getByStatus(WatchStatus.WANT_TO_WATCH));
    }
    @FXML
    private void showSceneIts() {
        mediaListView.getItems().setAll(manager.getSceneIts());
    }

    @FXML
    private void handleAddEntryButton(ActionEvent event) {
        try {
            //load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu.utsa.cs3443/layouts/Add_Edit_Entry_Screen.fxml"));
            Parent root = loader.load();

            //get the controller and add manager class
            AddEditEntryController controller = loader.getController();
            controller.setManager(this.manager);
            controller.setMedia(null);

            //open the popup window
            Stage stage = new Stage();
            stage.setTitle("Add New Entry");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            //save to CSV storage
            CsvStorage storage = new CsvStorage(Path.of("data"));
            storage.save(this.manager);

            //refresh your main screen UI list (re-load the watched items or keep current view)
            showWatched();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        mediaListView.setCellFactory(listView -> new ListCell<Media>() {
            @Override
            protected void updateItem(Media media, boolean empty) {
                super.updateItem(media, empty);

                if (empty || media == null) {
                    setText(null);
                    return;
                }

                Double rating = media.getRating();
                if (rating == null) {
                    setText(media.getTitle());
                } else {
                    setText(media.getTitle() + " · " + rating + "/5");
                }
            }
        });
    }
}
