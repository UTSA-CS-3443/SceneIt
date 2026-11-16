package edu.utsa.cs3443.sceneit.controller;
import edu.utsa.cs3443.sceneit.model.Manager;
import edu.utsa.cs3443.sceneit.model.WatchStatus;
import edu.utsa.cs3443.sceneit.model.Media;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import edu.utsa.cs3443.sceneit.model.CsvStorage;
import javafx.stage.Modality;
import javafx.scene.input.MouseButton;
import java.io.IOException;
import java.nio.file.Path;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class MainScreenController {

    private Manager manager;
    @FXML
    private ListView<Media> mediaListView;
    @FXML
    private Button addEntryButton;
    private WatchStatus currentWatchStatus = WatchStatus.WATCHED;

    public void setManager(Manager manager) {
        this.manager = manager;
        showWatched(); // By default, we show the Watched list as entry to the main-screen
    }

    @FXML
    private void showWatched() {
        currentWatchStatus = WatchStatus.WATCHED;
        mediaListView.getItems().setAll(manager.getByStatus(WatchStatus.WATCHED));
        addEntryButton.setVisible(true);
        addEntryButton.setManaged(true);
    }
    @FXML
    private void showWantToWatch() {
        currentWatchStatus = WatchStatus.WANT_TO_WATCH;
        mediaListView.getItems().setAll(manager.getByStatus(WatchStatus.WANT_TO_WATCH));
        addEntryButton.setVisible(true);
        addEntryButton.setManaged(true);
    }
    @FXML
    private void showSceneIts() {
        currentWatchStatus = WatchStatus.SCENEIT;
        mediaListView.getItems().setAll(manager.getSceneIts());
        addEntryButton.setVisible(false);
        addEntryButton.setManaged(false);
    }

    @FXML
    private void handleAddEntryButton(ActionEvent event) {
        openEntryForm(null);
    }
    private void handleEditEntryButton(Media media) {
        openEntryForm(media);
    }
    private void handleDeleteEntryButton(Media media) {
        ButtonType deleteButton = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);

        // Create the alert message based on the following checks:
        String message;
        // If it's Sceneit, we delete only the Sceneit
        if (currentWatchStatus == WatchStatus.SCENEIT) {
            message = "Remove the favorite scene for \"" + media.getTitle()
                    + "\"? The Watched entry will be kept.";
        } else { // If it's Watched/Want to Watch, check if the Sceneit exists, and
                // assign message accordingly
            if (media.getFavoriteScene() != null) {
                message = "Delete \"" + media.getTitle()
                        + "\" and its Sceneit?";
            } else { // Has no sceneit
                message = "Delete \"" + media.getTitle() + "\"?";
            }
        }

        // Create the alert
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                message, deleteButton, ButtonType.CANCEL);

        confirmation.setTitle("Delete entry");
        confirmation.setHeaderText("This cannot be undone");
        confirmation.initOwner(mediaListView.getScene().getWindow());

        ButtonType choice = confirmation.showAndWait().orElse(ButtonType.CANCEL);
        if (choice != deleteButton) {
            return;
        }

        // Remove the media
        if (currentWatchStatus == WatchStatus.SCENEIT) {
            media.removeFavoriteScene();
        } else {
            manager.removeMedia(media);
        }

        // Save the files
        try {
            CsvStorage storage = new CsvStorage(Path.of("data"));
            storage.save(manager);
        } catch (IOException e) {
            Alert error = new Alert(Alert.AlertType.ERROR, "There was an error while attempting to save the files");
            error.showAndWait();
        }

        // Refresh the view after
        if (currentWatchStatus == WatchStatus.WATCHED) {
            showWatched();
        } else if  (currentWatchStatus == WatchStatus.WANT_TO_WATCH) {
            showWantToWatch();
        } else {
            showSceneIts();
        }
    }

    private void openEntryForm(Media media) {
        try {
            // Load FXML file for the add/edit entry layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu.utsa.cs3443/layouts/Add_Edit_Entry_Screen.fxml"));
            Parent root = loader.load();

            // Get controller for FXML layout and manager class
            AddEditEntryController controller = loader.getController();
            controller.setManager(this.manager);

            // Pass the current entry (null opens "Add" mode, otherwise if passed an entry,
            // then it will open the "Edit" mode)
            controller.setMedia(media);

            if (media == null) {
                // We use the current tab for the status to add new entry for this category
                controller.setCurrentStatus(currentWatchStatus);
            }

            // open the popup window
            Stage stage = new Stage();

            // Assign the correct title based on the mode
            if (media == null) {
                stage.setTitle("Add New Entry");
            } else {
                stage.setTitle("Edit Entry");
            }
            // Create scene and show it
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // save to CSV storage
            CsvStorage storage = new CsvStorage(Path.of("data"));
            storage.save(this.manager);

            // refresh your main screen UI list (based on whichever view you were currently on)
            if (currentWatchStatus == WatchStatus.WATCHED) {
                showWatched();
            } else if (currentWatchStatus == WatchStatus.WANT_TO_WATCH) {
                showWantToWatch();
            } else {
                showSceneIts();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openViewEntryView(Media media) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/edu.utsa.cs3443/layouts/view-entry.fxml"
            ));
            Parent root = loader.load();

            ViewEntryController controller = loader.getController();
            controller.setMedia(media);

            Stage stage = new Stage();
            stage.setTitle("View Entry");
            stage.setScene(new Scene(root));
            stage.initOwner(mediaListView.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            Alert error = new Alert(Alert.AlertType.ERROR,
                    "Error opening the entry: " + e.getMessage()
            );
            error.showAndWait();
        }
    }

    private void openSceneitView(Media media) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/edu.utsa.cs3443/layouts/view-sceneit.fxml"
            ));
            Parent root = loader.load();

            ViewSceneitController controller = loader.getController();
            controller.setMedia(media);

            Stage stage = new Stage();
            stage.setTitle("View Sceneit");
            stage.setScene(new Scene(root));
            stage.initOwner(mediaListView.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            Alert error = new Alert(Alert.AlertType.ERROR,
                    "Error opening the Sceneit: " + e.getMessage()
            );
            error.showAndWait();
        }
    }
    @FXML
    void initialize() {
        mediaListView.setCellFactory(listView -> new ListCell<Media>() {
            @Override
            protected void updateItem(Media media, boolean empty) {
                super.updateItem(media, empty);

                // If the cell is empty, remove whatever we had in it
                if (empty || media == null) {
                    setGraphic(null);
                    return;
                }


                // Build text for the cell (row item)
                String text = media.getTitle();
                if (media.getRating() != null) {
                    text += " · " + media.getRating() + "/5";
                }

                // Create a label item with the text we just made
                Label title = new Label(text);
                // Make the label have no max width
                title.setMaxWidth(Double.MAX_VALUE);
                // Make the label have a HBox layout
                HBox.setHgrow(title, Priority.ALWAYS);

                // Create the two menu choices that will pop up when clicking three dots button on each cell
                MenuItem edit = new MenuItem("Edit");
                MenuItem delete = new MenuItem("Delete");

                edit.setOnAction(event -> handleEditEntryButton(media));
                delete.setOnAction(event -> handleDeleteEntryButton(media));

                MenuButton options = new MenuButton("•••", null, edit, delete);

                // Arrange and display row
                HBox row = new HBox(10, title, options);
                row.setAlignment(Pos.CENTER_LEFT);
                setGraphic(row);

                setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY
                            && event.getClickCount() == 2
                            && !isEmpty()
                            && getItem() != null) {


                        // Upon double clicking an entry, check if it is a Sceneit
                        // If so, then open the separate view FXML for it
                        if (currentWatchStatus == WatchStatus.SCENEIT) {
                            openSceneitView(getItem());
                        } else { // Otherwise, it is "Watched" or "Want to Watch" so we can open the view for them
                            // This works because openViewEntryView works for both lists, all it does is hide Rating
                            // it is a "Want to Watch"
                            openViewEntryView(getItem());
                        }
                    }
                });
            }
        });
    }
}
