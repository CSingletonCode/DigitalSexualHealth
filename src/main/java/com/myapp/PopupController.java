package com.myapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

public class PopupController {
    private SymptomsDatabase symptomsDatabase;
    private Stage stage;
    private TrackerController trackerController;

    @FXML
    private TextField nameField;
    @FXML
    private TextField dateField;
    @FXML
    private TextField descriptionField;
    @FXML
    private VBox optionsBox;
    @FXML
    private ScrollPane optionsScroll;

    public void setDatabase(SymptomsDatabase symptomsDatabase) {
        this.symptomsDatabase = symptomsDatabase;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setTrackerController(TrackerController trackerController) {
        this.trackerController = trackerController;
    }

    @FXML
    private void cancel() throws IOException{
        this.stage.close();
    }

    @FXML
    private void submit() throws IOException{
        SymptomEntry newSymptom = new SymptomEntry(nameField.getText(), dateField.getText(), descriptionField.getText());
        symptomsDatabase.recordSymptom(newSymptom);
        trackerController.displaySymptoms();
        this.stage.close();
    }

    @FXML
    private void showDropDown() throws IOException{
        System.out.println("Show drop down");
        optionsScroll.setVisible(true);
        optionsBox.setVisible(true);

    }
}