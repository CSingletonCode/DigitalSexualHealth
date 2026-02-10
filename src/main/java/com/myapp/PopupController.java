package com.myapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PopupController {
    private SymptomsDatabase symptomsDatabase;
    private RegisteredDatabase registeredDatabase;
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
    @FXML
    private Button cancelButton;
    @FXML
    private Button enterButton;

    public void setName(String name){
        nameField.setText(name);
    }

    public void initialize() throws IOException{
        nameField.setFocusTraversable(false);
        dateField.setFocusTraversable(false);
        descriptionField.setFocusTraversable(false);
        this.registeredDatabase = new RegisteredDatabase();
    }

    public void setSymptomsDatabase(SymptomsDatabase symptomsDatabase) {
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
    private void showDropDown(){
        System.out.println(nameField.getText());
        optionsBox.getChildren().clear();
        optionsScroll.setVisible(true);
        optionsBox.setVisible(true);
        lockOut();
        updateOptions();

        nameField.textProperty().addListener((obs, oldValue, newValue) -> {
            optionsBox.getChildren().clear();
            try {
                ArrayList<String> registered = this.registeredDatabase.getRegistered();
                for (String symptom : registered) {
                    if (symptom.toLowerCase().startsWith(nameField.getText().toLowerCase()) || nameField.getText().isEmpty()) {
                        FXMLLoader fxmlLoader = new FXMLLoader(SymptomsPage.class.getResource("/RegisteredOption.fxml"));
                        Node option = fxmlLoader.load();
                        OptionController optionController = fxmlLoader.getController();
                        optionController.setPopup(this);
                        optionController.setOption(symptom);
                        optionsBox.getChildren().add(option);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateOptions() {
        try {
            ArrayList<String> registered = this.registeredDatabase.getRegistered();
            for (String symptom : registered) {
                if (symptom.toLowerCase().startsWith(nameField.getText().toLowerCase()) || nameField.getText().isEmpty()) {
                    FXMLLoader fxmlLoader = new FXMLLoader(SymptomsPage.class.getResource("/RegisteredOption.fxml"));
                    Node option = fxmlLoader.load();
                    OptionController optionController = fxmlLoader.getController();
                    optionController.setPopup(this);
                    optionController.setOption(symptom);
                    optionsBox.getChildren().add(option);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void lockOut(){
        dateField.setDisable(true);
        descriptionField.setDisable(true);
        cancelButton.setDisable(true);
        enterButton.setDisable(true);
    }

    public void unlock(){
        dateField.setDisable(false);
        descriptionField.setDisable(false);
        cancelButton.setDisable(false);
        enterButton.setDisable(false);
    }

    public void hideDropDown(){
        optionsBox.setVisible(false);
        optionsScroll.setVisible(false);
    }
}