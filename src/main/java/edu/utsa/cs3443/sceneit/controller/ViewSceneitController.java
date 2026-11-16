package edu.utsa.cs3443.sceneit.controller;

import edu.utsa.cs3443.sceneit.model.FavoriteScene;
import edu.utsa.cs3443.sceneit.model.Media;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ViewSceneitController {
    @FXML
    private Label titleLabel;
    @FXML
    private Label mediaTypeLabel;
    @FXML
    private Label releaseYearLabel;
    @FXML
    private Label ratingLabel;
    @FXML
    private Label genresLabel;
    @FXML
    private TextArea sceneItTextArea;

    public void setMedia(Media media) {
        // Populate the fields
        titleLabel.setText(media.getTitle());
        if (media.getType().equals("MOVIE")) {
            mediaTypeLabel.setText("Movie");
        } else {
            mediaTypeLabel.setText("TV Show");
        }
        releaseYearLabel.setText(String.valueOf(media.getReleaseYear()));
        ratingLabel.setText(String.valueOf(media.getRating()));
        genresLabel.setText(String.join(", ", media.getGenres()));

        if (media.getFavoriteScene() != null) {
            sceneItTextArea.setText(
                    media.getFavoriteScene().getDescription()
            );
        }
        sceneItTextArea.setWrapText(true);
        sceneItTextArea.setEditable(false);

    }

    @FXML
    private void handleBackButton() {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }
}
