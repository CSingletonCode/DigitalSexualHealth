package com.myapp;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.ArrayList;

public class PopupController {
    private SymptomsDatabase symptomsDatabase;
    private RegisteredDatabase registeredDatabase;
    private Stage stage;
    private TrackerController trackerController;
    private EventHandler<MouseEvent> clickOutsideFilter;

    @FXML
    private TextField nameField;
    @FXML
    private DatePicker dateField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private VBox optionsBox;
    @FXML
    private ScrollPane optionsScroll;
    @FXML
    private Button cancelButton;
    @FXML
    private Button enterButton;
    @FXML
    private Pane root;

    public void setName(String name){
        nameField.setText(name);
    }

    public void initialize() throws IOException{
        nameField.setFocusTraversable(false);
        dateField.setFocusTraversable(false);
        root.setFocusTraversable(true);
        descriptionField.setFocusTraversable(false);
        this.registeredDatabase = new RegisteredDatabase();

        dateField.getEditor().focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) {
                dateField.setValue(dateField.getConverter().fromString(dateField.getEditor().getText()));
            }
        });
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
    private void cancel() {
        this.stage.close();
        trackerController.setDimLayerVisible(false);
    }

    @FXML
    private void submit() throws IOException{
        String check = checkValid();
        if (check.equals("valid")){
            String userID = String.valueOf(userSession.getInstance().getUserId());
            SymptomEntry newSymptom = new SymptomEntry(nameField.getText(), dateField.getValue().toString(), descriptionField.getText(), userID,false);
            symptomsDatabase.recordSymptom(newSymptom);
            trackerController.displaySymptoms();
            this.stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(check);
            alert.showAndWait();
        }
    }

    private String checkValid(){
        if (nameField.getText().isEmpty()){
            return "Enter a name";
        }
        if (dateField.getValue().toString().isEmpty()){
            return "Enter a valid date";
        }
        if (descriptionField.getText().isEmpty()){
            return "Enter a description";
        }
        return "valid";
    }

    @FXML
    private void showDropDown(){
        System.out.println(nameField.getText());
        optionsBox.getChildren().clear();
        optionsScroll.setVisible(true);
        optionsBox.setVisible(true);
        lockOut();
        updateOptions();

        clickOutsideFilter = event -> {
            if (!optionsScroll.getBoundsInParent().contains(event.getX(), event.getY()) &&
                    !nameField.getBoundsInParent().contains(event.getX(), event.getY())) {
                root.requestFocus();
                hideDropDown();
                nameField.setText("");
                unlock();
            }
        };

        nameField.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, clickOutsideFilter);

        nameField.textProperty().addListener((obs, oldValue, newValue) -> {
            optionsBox.getChildren().clear();
            try {
                ArrayList<String> registered = this.registeredDatabase.getRegistered();
                for (String symptom : registered) {
                    if (symptom.toLowerCase().startsWith(nameField.getText().toLowerCase()) || nameField.getText().isEmpty()) {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/RegisteredOption.fxml"));
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
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/RegisteredOption.fxml"));
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
        nameField.getScene().removeEventFilter(MouseEvent.MOUSE_PRESSED, clickOutsideFilter);
    }
}