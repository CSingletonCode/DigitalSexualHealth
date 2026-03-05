package com.myapp;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class PINPopupController {

    @FXML private TextField firstPINField;
    @FXML private TextField secondPINField;
    @FXML private Button confirmButton;
    @FXML private Button backButton;
    private Stage stage;
    private String error;
    private String title;

    public void initialize() {
        firstPINField.setFocusTraversable(false);
        secondPINField.setFocusTraversable(false);
        confirmButton.setFocusTraversable(false);
        backButton.setFocusTraversable(false);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private boolean validate(){
        boolean valid = true;
        if (firstPINField.getText().isEmpty() || secondPINField.getText().isEmpty()) {
            error = "Entry required for both fields";
            valid = false;
        } else if (!firstPINField.getText().trim().equals(secondPINField.getText().trim())) {
            error = "First PIN and Second PIN are not the same";
            valid = false;
        } else if (!firstPINField.getText().matches("\\d{4}")) {
            error = "First PIN must be a valid 4 digit number";
            valid = false;
        }
        return valid;
    }

    @FXML
    private void back(){
        this.stage.close();
    }

    private void showAlert(String header,String message, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void submit() throws IOException {
        if (validate()){
            System.out.println(firstPINField.getText().trim());
            DatabaseManager.storePIN(firstPINField.getText().trim());
            sessionManager.setSession();
            showAlert("PIN Successfully Added", "Older PINs are now invalid.", "Success");
            this.stage.close();
        } else {
            showAlert("PIN Creation Failed", error, "Error");
        }
    }
}