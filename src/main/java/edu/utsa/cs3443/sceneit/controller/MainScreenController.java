package edu.utsa.cs3443.sceneit.controller;
import edu.utsa.cs3443.sceneit.model.Manager;
import edu.utsa.cs3443.sceneit.model.WatchStatus;
import edu.utsa.cs3443.sceneit.model.Media;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;

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
