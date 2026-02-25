package com.myapp;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class PINPopupController {

    @FXML private TextField firstPINField;
    @FXML private TextField secondPINField;
    @FXML private Button confirmButton;
    private String error;
    private boolean valid = true;

    public void initialize() {
        firstPINField.setFocusTraversable(false);
        secondPINField.setFocusTraversable(false);
        confirmButton.setFocusTraversable(false);
    }

    private boolean validate(){
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
    private void submit() throws IOException {
        if (validate()){
            System.out.println(firstPINField.getText().trim());
            DatabaseManager.storePIN(firstPINField.getText().trim());
            sessionManager.setSession();
        } else {
            System.out.println(error);
        }
    }
}
