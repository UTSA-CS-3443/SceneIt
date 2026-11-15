package edu.utsa.cs3443.sceneit.controller;

import edu.utsa.cs3443.sceneit.model.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.Arrays;

public class AddEditEntryController {

    @FXML private Label formTitleLabel;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextField ratingField;
    @FXML private TextField titleField;
    @FXML private TextField yearField;
    @FXML private TextField genreTextField;
    //@FXML private TextField directorField;
    @FXML private TextArea sceneTextArea;
    @FXML private VBox sceneItTextAreaBox;
    @FXML private DatePicker datePicker;

    //State Variable to track if we are Editing an existing item
    private Media currentMedia;
    private Manager manager;

    @FXML
    public void initialize(){
        //populate combo box
        typeComboBox.getItems().addAll("Movie", "TV Show");
        statusComboBox.getItems().addAll("Want to Watch", "Watched");

        //default values for brand new entry
        typeComboBox.setValue("Movie");
        statusComboBox.setValue("Want to Watch");
        datePicker.setValue(LocalDate.now());

        //default sceneIt area to disabled since default is want to watch
        sceneTextArea.setDisable(true);
    }


    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public void setCurrentStatus(WatchStatus status) {
        if (status == WatchStatus.WATCHED) {
            statusComboBox.setValue("Watched");
        } else {
            statusComboBox.setValue("Want to Watch");
        }

        updateSceneAccess();
    }

    /**
     * Called when opening the form.
     * Pass null for Add Mode, or an existing Media object for Edit Mode.
     */
    public void setMedia(Media media){
        this.currentMedia = media;

        if(media == null){
            //add mode
            formTitleLabel.setText("Add Entry");
        } else {
            //edit mode
            formTitleLabel.setText("Edit Entry");

            //prefill fields with existing data
            titleField.setText(media.getTitle());
            datePicker.setValue(media.getDate());
            typeComboBox.setValue(media.getType());
            yearField.setText(String.valueOf(media.getReleaseYear()));

            // Safe to call getGenres() because media is guaranteed not to be null here
            if(media.getGenres() != null && !media.getGenres().isEmpty()){
                genreTextField.setText(String.join(", ", media.getGenres()));
            }

            if (media.getWatchStatus() == WatchStatus.WATCHED){
                statusComboBox.setValue("Watched");
                if(media.getRating() != null){
                    ratingField.setText(String.valueOf(media.getRating()));
                }
                if(media.getFavoriteScene() != null){
                    sceneTextArea.setText(media.getFavoriteScene().getDescription());
                }
                updateSceneAccess();
            }
        }
    }

    @FXML
    private void handleStatusChange(ActionEvent event) {
        updateSceneAccess();

        // Resizes the window to make it bigger if going from "Want to Watch" to "Watched"
        if (statusComboBox.getScene() != null
            && statusComboBox.getScene().getWindow() != null) {

            Stage stage = (Stage) statusComboBox.getScene().getWindow();
            stage.sizeToScene();
        }
    }

    /**
     * Controls whether the SceneIt section is visible and interactive
     * depending on whether the media has been watched.
     */
    private void updateSceneAccess() {
        String status = statusComboBox.getValue();
        boolean isWatched = "Watched".equals(status);

        sceneItTextAreaBox.setVisible(isWatched);
        sceneItTextAreaBox.setManaged(isWatched); // This collapses the layout space when hidden
        sceneTextArea.setDisable(!isWatched); // Makes Sceneit text box disabled if entry is not watched
        ratingField.setDisable(!isWatched);  // Disable rating field if entry is not "Watched"
        if (!isWatched) {
            sceneTextArea.clear();
            ratingField.clear();
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        String title = titleField.getText().trim();
        LocalDate date = datePicker.getValue();
        String type = typeComboBox.getValue();
        String statusString = statusComboBox.getValue();
        String yearText = yearField.getText().trim();
        //String director = directorField.getText().trim();
        String ratingText = ratingField.getText().trim();
        String genreText = genreTextField.getText().trim();
        String sceneText = sceneTextArea.getText().trim();

        //validate title
        if (title.isEmpty()) {
            showError("Missing Title", "Please enter a title for the media.");
            return;
        }

        // validate date
        if (date == null) {
            showError("Missing Date", "Please enter a date.");
            return;
        }

        //validate release year
        int year;
        try {
            year = Integer.parseInt(yearText);
            if (year <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showError("Invalid Year", "Please enter a valid positive year (e.g., 2026).");
            return;
        }

        //validate genre and parse
        if (genreText.isEmpty()) {
            showError("Missing Genre", "Please enter at least one genre (e.g., Action, Drama).");
            return;
        }
        List<String> genres = Arrays.asList(genreText.split("\\s*,\\s*"));

        //map status string to WatchStatus Enum
        WatchStatus watchStatus = "Watched".equals(statusString)
                ? WatchStatus.WATCHED
                : WatchStatus.WANT_TO_WATCH;

        //parse and validate rating
        Double rating = null;
        if (!ratingText.isEmpty()) {
            try {
                rating = Double.parseDouble(ratingText);
                if (rating < 1.0 || rating > 5.0) {
                    showError("Invalid Rating", "Rating must be between 1.0 and 5.0.");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Invalid Rating", "Please enter a valid decimal number for the rating (e.g., 4.5).");
                return;
            }
        }

        // enforce model rules (Watched requires rating; Want to Watch forbids rating)
        if (watchStatus == WatchStatus.WATCHED && rating == null) {
            showError("Rating Required", "Watched media must have a rating between 1.0 and 5.0.");
            return;
        }
        if (watchStatus == WatchStatus.WANT_TO_WATCH && rating != null) {
            showError("Invalid Rating", "Want to Watch media cannot have a rating.");
            return;
        }

        //create or update media object
        try {
            if (currentMedia == null) {
                //add mode
                //use managers method to get the next unique ID safely
                int generatedId = manager.getNextId();
                Media newMedia;

                if ("Movie".equals(type)) {
                    newMedia = new Movie(generatedId, date, title, year, genres, watchStatus, rating, "");
                } else {
                    newMedia = new TvShow(generatedId, date, title, year, genres, watchStatus, rating, "");
                }

                if (watchStatus == WatchStatus.WATCHED && !sceneText.isEmpty()) {
                    newMedia.setFavoriteScene(new FavoriteScene(sceneText));
                }

                // passes the media to manager list
                manager.addMedia(newMedia);

            } else {
                // edit mode
                currentMedia.setTitle(title);
                currentMedia.setDate(date);
                currentMedia.setReleaseYear(year);
                currentMedia.setGenres(genres);

                if (watchStatus == WatchStatus.WATCHED) {
                    currentMedia.markWatched(rating);
                    if (!sceneText.isEmpty()) {
                        currentMedia.setFavoriteScene(new FavoriteScene(sceneText));
                    } else {
                        currentMedia.removeFavoriteScene();
                    }
                } else {
                    currentMedia.removeFavoriteScene();
                }
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            showError("Validation Error", e.getMessage());
            return;
        }

        closeWindow();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
