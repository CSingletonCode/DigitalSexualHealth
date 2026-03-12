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

public class PINLoginController {

    @FXML private TextField pinField;

    @FXML private PasswordField pinHiddenField;

    @FXML private Button toggleBtn;

    @FXML private Line eyeSlash;

    String email;

    public void initialize() throws IOException{
        pinField.setFocusTraversable(false);
        pinField.setManaged(false);
        pinField.setVisible(false);
        pinField.textProperty().bindBidirectional(pinHiddenField.textProperty());
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
            ThemeManager.applyTheme(scene,null, null);
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText("Login Failed");
            alert.setContentText("PIN is Incorrect");
            alert.showAndWait();
        }
    }

    @FXML
    void handleToggle(){
        eyeSlash.setVisible(!pinField.isVisible());
        if (pinHiddenField.isVisible()){
            pinHiddenField.setVisible(false);
            pinHiddenField.setManaged(false);
            pinField.setVisible(true);
            pinField.setManaged(true);
        } else {
            pinHiddenField.setVisible(true);
            pinHiddenField.setManaged(true);
            pinField.setVisible(false);
            pinField.setManaged(false);
        }
    }

    @FXML private void toPassword(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoginPassword.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        ThemeManager.applyTheme(scene,sessionManager.isHighContrast(), sessionManager.isLargeText());
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
