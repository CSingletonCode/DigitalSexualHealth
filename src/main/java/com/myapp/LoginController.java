package com.myapp;

import javafx.collections.ObservableList;
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

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private PasswordField passwordHiddenField;

    @FXML
    private Button toggleBtn;

    @FXML
    private Line eyeSlash;

    @FXML
    private CheckBox stayLoggedIn;

    public void initialize(){
        passwordField.setManaged(false);
        passwordField.setVisible(false);
        passwordField.textProperty().bindBidirectional(passwordHiddenField.textProperty());
    }

    @FXML
    void handleLogin(ActionEvent event) throws IOException {
        if (!validateinputs()){
            return;
        }
        String email = emailField.getText();
        String password = passwordHiddenField.getText();
        if (DatabaseManager.validateLogin(email, password)){
            DatabaseManager.fetchAndStartSession(email);
            if (stayLoggedIn.isSelected()) {
                sessionManager.setSession();
            }
            switchScene(event, "/homepage.fxml");
        } else {
            showAlert("Invalid email or password");
        }
    }

    @FXML
    void handleToggle(){
        eyeSlash.setVisible(!passwordField.isVisible());
        if (passwordHiddenField.isVisible()){
            passwordHiddenField.setVisible(false);
            passwordHiddenField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
        } else {
            passwordHiddenField.setVisible(true);
            passwordHiddenField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
        }
    }

    private boolean validateinputs(){
        boolean isValid = true;
        StringBuilder Errors = new StringBuilder();

        String email = emailField.getText().trim();
        String password = passwordHiddenField.getText().trim();
        if (!email.matches("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$")){
            setErrorStyle(emailField);
            Errors.append("Please enter a valid email address.\n");
            isValid = false;
        } else {
            clearErrorStyle(emailField);
        }

        if (password.isEmpty()) {
            setErrorStyle(passwordHiddenField);
            Errors.append("Please enter a password.\n");
            isValid = false;
        } else {
            clearErrorStyle(passwordHiddenField);
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
        alert.getDialogPane().setPrefWidth(250);
        alert.showAndWait();
    }

    @FXML
    void handleSignUp(ActionEvent event) {
        System.out.println("User clicked Sign Up");
        switchScene(event, "/signup.fxml");
    }

    @FXML
    void handleForgotLink(ActionEvent event) {
        switchScene(event, "/forgotPassword.fxml");
    }

    // 3. The Helper Method to Switch Scenes
    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 360, 640);
            ObservableList<String> sheets = scene.getStylesheets();
            sheets.clear();
            sheets.add(ThemeManager.class.getResource("/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load FXML file: " + fxmlFile);
        }
    }
}