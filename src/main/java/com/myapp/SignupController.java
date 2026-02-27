package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.io.IOException;

public class SignupController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private PasswordField passwordHiddenField;

    @FXML
    private TextField passwordField1;

    @FXML
    private PasswordField passwordHiddenField1;

    @FXML
    private Button continueButton;

    @FXML
    private Button toggleBtn;

    @FXML
    private Button toggleBtnConfirm;

    @FXML
    private Line eyeSlash;

    @FXML
    private Line eyeSlash1;

    @FXML
    void handleCreateAccount(ActionEvent event) {

        if (!validateinputs()){
            return;
        }

        switchScene(event, "/signup2.fxml");
    }

    public void initialize(){
        passwordField.setManaged(false);
        passwordField.setVisible(false);
        passwordField.textProperty().bindBidirectional(passwordHiddenField.textProperty());
        passwordField1.setManaged(false);
        passwordField1.setVisible(false);
        passwordField1.textProperty().bindBidirectional(passwordHiddenField1.textProperty());
    }

    private boolean validateinputs() {
        boolean isValid = true;
        StringBuilder Errors = new StringBuilder();

        String email = emailField.getText().trim();
        if (!email.matches("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$")){
            setErrorStyle(emailField);
            Errors.append("Please enter a valid email address.\n");
            isValid = false;
        } else if (DatabaseManager.emailExists(email)) {
            setErrorStyle(emailField);
            Errors.append("This email is already registered.\n");
            isValid = false;
        }
        else {
            clearErrorStyle(emailField);
        }

        String password = passwordField.getText().trim();
        if (password.length() < 8){
            setErrorStyle(passwordField);
            Errors.append("Password must be at least 8 characters.\n");
            isValid = false;
        } else {
            clearErrorStyle(passwordField);
        }
        String password1 = passwordField1.getText().trim();
        if (!password1.equals(password)){
            setErrorStyle(passwordField1);
            Errors.append("Passwords do not match.\n");
            isValid = false;
        } else {
            clearErrorStyle(passwordField1);
        }

        if (!isValid){
            showAlert(Errors.toString());
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
            passwordHiddenField1.setVisible(false);
            passwordHiddenField1.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordField1.setVisible(true);
            passwordField1.setManaged(true);
        } else {
            passwordHiddenField.setVisible(true);
            passwordHiddenField.setManaged(true);
            passwordHiddenField1.setVisible(true);
            passwordHiddenField1.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
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

            Signup2Controller nextController = loader.getController();
            User partialUser = new User();
            partialUser.setEmail(emailField.getText());
            partialUser.setPassword(passwordField.getText());
            nextController.setUserData(partialUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}