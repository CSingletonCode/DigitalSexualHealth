package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        System.out.println("User clicked Login!");
        System.out.println("Username: " + emailField.getText());
        // switchScene(event, "/dashboard.fxml");
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