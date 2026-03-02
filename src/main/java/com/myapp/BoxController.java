package com.myapp;

import javafx.scene.control.Label;
import javafx.fxml.FXML;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class BoxController {

    @FXML
    private Label nameLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label descriptionLabel;
    private TrackerController trackerController;
    private String id;

    public void setTrackerController(TrackerController trackerController) {
        this.trackerController = trackerController;
    }

    public void setData(SymptomEntry symptom) {
        if (!Objects.equals(symptom.getName(), "")) {
            nameLabel.setText(symptom.getName());
        } else {
            nameLabel.setText("No Name");
        }
        if (!Objects.equals(symptom.getDate(), "")) {
            dateLabel.setText(symptom.getDate());
        } else {
            dateLabel.setText("No Date");
        }
        if (!Objects.equals(symptom.getDescription(), "")) {
            descriptionLabel.setText(symptom.getDescription());
        } else {
            descriptionLabel.setText("No Description");
        }
        id = symptom.getId();
    }

    @FXML
    private void delete(){
            try {
                ArrayList<SymptomEntry> tracked = SymptomsDatabase.getAllSymptoms();
                String userID = String.valueOf(userSession.getInstance().getUserId());
                ArrayList<SymptomEntry> filteredList = new ArrayList<>();
                for (SymptomEntry symptomEntry : tracked) {
                    if (symptomEntry.getUserID().equals(userID)) {
                        filteredList.add(symptomEntry);
                    }
                }
                for (SymptomEntry symptom : filteredList) {
                    if (symptom.getId().equals(id)) {
                        tracked.remove(symptom);
                        break;
                    }
                }
                trackerController.symptomsDatabase.setAllSymptoms(tracked);
                trackerController.displaySymptoms();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
    }
}
