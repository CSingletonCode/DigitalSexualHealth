package com.myapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class HomeController {
    @FXML private VBox symptomsCard, chatCard, evaluationCard, apptCard;

    @FXML
    private void handleNavigation(MouseEvent event) {
        VBox source = (VBox) event.getSource();
        String targetFxml = "";

        if (source == symptomsCard) {
            targetFxml = "/symptoms.fxml";
        } else if(source == chatCard) {
            targetFxml = "/chat.fxml";
        } else if(source == evaluationCard) {
            targetFxml = "/evaluation.fxml";
        } else if(source == apptCard) {
            targetFxml = "/appointmentSchedule.fxml";
        }

        if (!targetFxml.isEmpty()) {
        loadPage(event, targetFxml);
        }
    }

    private void loadPage(MouseEvent event, String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Could not load " + fxml + ". Make sure the file exists.");
            e.printStackTrace();
        }
    }
}
