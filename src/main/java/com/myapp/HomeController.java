package com.myapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.util.ArrayList;

public class HomeController {

    @FXML private Label logOut;
    @FXML private Label settings;
    @FXML private Label name_label;
    @FXML private Label symptom_log_label;
    @FXML private Label next_appt_label;
    @FXML private VBox symptomsCard, adviceCard, evaluationCard, apptCard;

    public void initialize() throws IOException {
        String firstName = userSession.getInstance().getFirstName();

        String nextAppt = DatabaseManager.getNextAppt();

        name_label.setText("back, " + firstName);
        ArrayList<SymptomEntry> symptomList = SymptomsDatabase.getAllSymptoms();
        String lastLog;
        if (!symptomList.isEmpty()) {
            lastLog = symptomList.get(symptomList.size() - 1).getName();
        } else {
            lastLog = "No symptoms found";
        }

        symptom_log_label.setText("Last symptom log: " + lastLog);
        next_appt_label.setText("Next appointment: " + nextAppt);

    }
    @FXML
    private void handleNavigation(MouseEvent event) throws IOException {
        Object source = event.getSource();
        String targetFxml = "";

        if (source == symptomsCard) {
            targetFxml = "/symptoms.fxml";
        } else if(source == adviceCard) {
            targetFxml = "/advice.fxml";
        } else if(source == evaluationCard) {
            targetFxml = "/evaluation.fxml";
        } else if(source == apptCard) {
            targetFxml = "/appointmentSchedule.fxml";
        } else if(source == logOut){
            sessionManager.clearSession();
            userSession.cleanUserSession();
            targetFxml = "/login.fxml";
        } else if(source == settings){
            targetFxml = "/settings.fxml";
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
            if (!fxml.equals("/login.fxml")){
                ThemeManager.applyTheme(scene);
            }

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Could not load " + fxml + ". Make sure the file exists.");
            e.printStackTrace();
        }
    }
}
