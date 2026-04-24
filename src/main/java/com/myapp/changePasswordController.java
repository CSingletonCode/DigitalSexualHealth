package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;

public class changePasswordController {

    @FXML
    private TextField passwordField;

    @FXML
    private TextField passwordHiddenField;

    @FXML
    private TextField passwordField1;

    @FXML
    private TextField passwordHiddenField1;

    @FXML
    private Line eyeSlash,eyeSlash1;

    private String email;

    @FXML private Button returnButton;

    @FXML
    void handleChange(ActionEvent event) {
        if (validateinputs()){
            DatabaseManager.changePassword(passwordHiddenField.getText().trim(),email);
            switchScene(event, "/login.fxml");
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void initialize() {
        returnButton.setOnAction(this::handleReturn);
        passwordField.setManaged(false);
        passwordField.setVisible(false);
        passwordField.textProperty().bindBidirectional(passwordHiddenField.textProperty());
        passwordField1.setManaged(false);
        passwordField1.setVisible(false);
        passwordField1.textProperty().bindBidirectional(passwordHiddenField1.textProperty());
    }

    private void handleReturn(ActionEvent event) {
        switchScene(event, "/login.fxml");
    }

    private boolean validateinputs(){
        boolean isValid = true;
        StringBuilder Errors = new StringBuilder();


        String password = passwordHiddenField.getText().trim();
        if (password.length() < 8){
            setErrorStyle(passwordHiddenField);
            Errors.append("Password must be at least 8 characters.\n");
            isValid = false;
        } else {
            clearErrorStyle(passwordHiddenField);
        }
        String password1 = passwordHiddenField1.getText().trim();
        if (!password1.equals(password)){
            setErrorStyle(passwordHiddenField1);
            Errors.append("Passwords do not match.\n");
            isValid = false;
        } else {
            clearErrorStyle(passwordHiddenField1);
        }

        if (!isValid){
            showAlert("Please Rectify the Following:",Errors.toString());
        } else {
            showAlert("Password Change Successful!","Please login again.");
        }
        return isValid;
    }

    @FXML
    void handleToggle(){
        eyeSlash.setVisible(!passwordField.isVisible());
        eyeSlash1.setVisible(!passwordField.isVisible());
        if (passwordHiddenField.isVisible()){
            passwordHiddenField.setVisible(false);
            passwordHiddenField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordHiddenField1.setVisible(false);
            passwordHiddenField1.setManaged(false);
            passwordField1.setVisible(true);
            passwordField1.setManaged(true);
        } else {
            passwordHiddenField.setVisible(true);
            passwordHiddenField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            passwordHiddenField1.setVisible(true);
            passwordHiddenField1.setManaged(true);
            passwordField1.setVisible(false);
            passwordField1.setManaged(false);
        }
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
        alert.getDialogPane().setPrefWidth(250);
        alert.showAndWait();
    }

    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 360, 640);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
