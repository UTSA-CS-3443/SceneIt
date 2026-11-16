package edu.utsa.cs3443.sceneit.controller;

import edu.utsa.cs3443.sceneit.model.Media;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class ViewEntryController {
    @FXML
    private Label titleLabel;
    @FXML
    private Label mediaTypeLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label releaseYearLabel;
    @FXML
    private Label ratingLabel;
    @FXML
    private HBox ratingBox;
    @FXML
    private Label genresLabel;
    @FXML
    private TextArea notesTextArea;

    public void setMedia(Media media) {
        // Populate all labels
        titleLabel.setText(media.getTitle());

        // Special logic for type & status
        String type = media.getType();
        if (type.equals("MOVIE")) {
            mediaTypeLabel.setText("Movie");
        } else {
            mediaTypeLabel.setText("TV Show");
        }
        String watchStatus = media.getWatchStatus().toString();
        if  (watchStatus.equals("WATCHED")) {
            statusLabel.setText("Watched");
        } else {
            statusLabel.setText("Want to Watch");
        }

        releaseYearLabel.setText(
                String.valueOf(media.getReleaseYear())
        );
        if (media.getRating() != null) { // Watched entry (rating)
            ratingLabel.setText(String.valueOf(media.getRating()));
            ratingBox.setVisible(true);
            ratingBox.setManaged(true);
        } else { // Want to watch entry (no rating)
            ratingLabel.setText("");
            ratingBox.setVisible(false);
            ratingBox.setManaged(false);
        }
        genresLabel.setText(String.join(", ", media.getGenres()));

        notesTextArea.setText(media.getNotes());
        notesTextArea.setWrapText(true);
        notesTextArea.setEditable(false);
    }

    @FXML
    private void handleBackButton(){
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }


}
