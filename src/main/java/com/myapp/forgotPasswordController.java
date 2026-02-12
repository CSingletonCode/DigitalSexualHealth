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

public class forgotPasswordController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField codeField;

    @FXML
    void handleContinue(ActionEvent event) {
        if (!validateinputs()){
            return;
        }
        switchScene(event, "/changePassword.fxml");
    }

    @FXML
    void sendCode(ActionEvent event) {
    }

    private boolean validateinputs() {
        boolean isValid = true;
        StringBuilder Errors = new StringBuilder();

        String email = emailField.getText().trim();
        if (!email.matches("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$")) {
            setErrorStyle(emailField);
            Errors.append("Please enter a valid email address.\n");
            isValid = false;
        } else {
            clearErrorStyle(emailField);

        }
        String code = codeField.getText().trim();
        if (!checkCode(code)){
            setErrorStyle(codeField);
            Errors.append("Invalid Code");
            isValid = false;
        } else {
            clearErrorStyle(codeField);
        }

        if (!isValid){
            showAlert(Errors.toString());
        }
        return isValid;
    }

    private boolean checkCode(String code){
        return true;
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

    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            ThemeManager.applyTheme(scene);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
