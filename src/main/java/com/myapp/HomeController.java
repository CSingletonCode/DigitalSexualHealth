package com.myapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class HomeController {

    @FXML private HBox logOut;
    @FXML private Label name_label;
    @FXML private VBox symptomsCard, chatCard, evaluationCard, apptCard;

    public void initialize() {
        String firstName = userSession.getInstance().getFirstName();
        name_label.setText("back, " + firstName);
    }
    @FXML
    private void handleNavigation(MouseEvent event) {
        Object source = event.getSource();
        String targetFxml = "";

        if (source == symptomsCard) {
            targetFxml = "/symptoms.fxml";
        } else if(source == chatCard) {
            targetFxml = "/chat.fxml";
        } else if(source == evaluationCard) {
            targetFxml = "/evaluation.fxml";
        } else if(source == apptCard) {
            targetFxml = "/appointmentSchedule.fxml";
        } else if(source == logOut){
            userSession.cleanUserSession();
            targetFxml = "/login.fxml";
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
