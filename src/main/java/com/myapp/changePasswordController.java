package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class changePasswordController {

    @FXML
    private TextField passwordField;

    @FXML
    private TextField password1Field;

    @FXML
    void handleChange(ActionEvent event) {
        if (!validateinputs()){
            return;
        }
        switchScene(event, "/login.fxml");
    }

    private boolean validateinputs(){
        boolean isValid = true;
        StringBuilder Errors = new StringBuilder();


        String password = passwordField.getText().trim();
        if (password.length() < 8){
            setErrorStyle(passwordField);
            Errors.append("Password must be at least 8 characters.\n");
            isValid = false;
        } else {
            clearErrorStyle(passwordField);
        }
        String password1 = password1Field.getText().trim();
        if (!password1.equals(password)){
            setErrorStyle(password1Field);
            Errors.append("Passwords do not match.\n");
            isValid = false;
        } else {
            clearErrorStyle(password1Field);
        }

        if (!isValid){
            showAlert("Please Rectify the Following:",Errors.toString());
        } else {
            showAlert("Password Change Successful!","Please login again.");
        }
        return isValid;
    }

    private void setErrorStyle(Control node) {
        node.setStyle("-fx-border-color: #ff4444; -fx-border-width: 2px; -fx-border-radius: 5px;");
    }

    private void clearErrorStyle(Control node) {
        node.setStyle(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Password Change");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
