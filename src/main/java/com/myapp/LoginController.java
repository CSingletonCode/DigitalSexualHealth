package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    // 2. The Button/Hyperlink Action Methods
    @FXML
    void handleLogin(ActionEvent event) {
        if (!validateinputs()){
            return;
        }
        System.out.println("Username: " + emailField.getText());
        // switchScene(event, "/dashboard.fxml");
    }

    private boolean validateinputs(){
        boolean isValid = true;
        StringBuilder Errors = new StringBuilder();

        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        if (!email.matches("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$")){
            setErrorStyle(emailField);
            Errors.append("Please enter a valid email address.\n");
            isValid = false;
        } else {
            clearErrorStyle(emailField);
        }

        if (password.isEmpty()) {
            setErrorStyle(passwordField);
            Errors.append("Please enter a password.\n");
            isValid = false;
        } else {
            clearErrorStyle(passwordField);
        }

        if (!isValid){
            showAlert(Errors.toString());
        }

        return isValid;
    }

    private void setErrorStyle(Control node) {
        node.setStyle("-fx-border-color: #ff4444; -fx-border-width: 2px; -fx-border-radius: 5px;");
    }

    private void clearErrorStyle(Control node) {
        node.setStyle(null);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Registration Error");
        alert.setHeaderText("Please Rectify the Following:");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void handleSignUp(ActionEvent event) {
        System.out.println("User clicked Sign Up!");
        switchScene(event, "/signup.fxml");
    }

    @FXML
    void handleForgotLink(ActionEvent event) {
        System.out.println("User clicked Forgot Password!");
    }

    // 3. The Helper Method to Switch Scenes
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
            System.out.println("Could not load FXML file: " + fxmlFile);
        }
    }
}