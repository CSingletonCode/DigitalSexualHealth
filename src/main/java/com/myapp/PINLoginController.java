package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.TextField;

public class PINLoginController {

    @FXML private TextField pinField;

    String email;

    public void initialize() throws IOException{
        pinField.setFocusTraversable(false);
        String[] current_session = sessionManager.getSession();
        email = current_session[0];
    }

    @FXML private void handleLogin(ActionEvent event) throws IOException {
        boolean correct = DatabaseManager.validatePIN(pinField.getText(), false,sessionManager.getId());
        if (correct) {
            String email = sessionManager.getEmail();
            DatabaseManager.fetchAndStartSession(email);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homepage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML private void toPassword(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoginPassword.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML private void changeAccount(ActionEvent event) throws IOException{
        sessionManager.clearSession();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
