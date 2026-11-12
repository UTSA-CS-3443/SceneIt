package edu.utsa.cs3443.sceneit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class WelcomeScreenController {

    @FXML
    private void handleContinueButton(ActionEvent event){
        try{
            //load the Main_Screen.fxml file from resource folder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("edu.utsa.cs3443/layouts/main-screen.fxml"));
            Parent root = loader.load();

            //get the current stage (window) from the button click event
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //create and set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
