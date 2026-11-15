package edu.utsa.cs3443.sceneit.controller;
import edu.utsa.cs3443.sceneit.model.Manager;
import edu.utsa.cs3443.sceneit.model.WatchStatus;
import edu.utsa.cs3443.sceneit.model.Media;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

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

                setText(null);
                setGraphic(null);

                if (empty || media == null) {
                    return;
                }

                String text = media.getTitle();
                if (media.getRating() != null) {
                    text += " · " + media.getRating() + "/5";
                }

                Label title = new Label(text);
                title.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(title, Priority.ALWAYS);

                MenuItem edit = new MenuItem("Edit");
                MenuItem delete = new MenuItem("Delete");
                MenuButton options = new MenuButton("⋮", null, edit, delete);

                HBox row = new HBox(10, title, options);
                row.setAlignment(Pos.CENTER_LEFT);

                setGraphic(row);
            }
        });
    }
}
