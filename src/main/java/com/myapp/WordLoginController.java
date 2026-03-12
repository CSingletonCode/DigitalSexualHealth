package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.io.IOException;

public class WordLoginController {

    @FXML private AnchorPane PasswordLoginPage;

    @FXML private TextField passwordField;

    @FXML private Hyperlink switchToPIN;

    @FXML private Button toggleBtn;

    @FXML private Button loginButton;

    @FXML private PasswordField passwordHiddenField;

    @FXML private Line eyeSlash;

    String email;

    public void initialize() throws IOException{
        passwordField.setFocusTraversable(false);
        passwordField.setManaged(false);
        passwordField.setVisible(false);
        passwordField.textProperty().bindBidirectional(passwordHiddenField.textProperty());
        String[] current_session = sessionManager.getSession();
        if (current_session[1].equals("false")) {
            switchToPIN.setVisible(false);
            switchToPIN.setManaged(false);
        }

        email = current_session[0];
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

    @FXML private void handleLogin(ActionEvent event) throws IOException {
        String email = sessionManager.getEmail();
        boolean correct = DatabaseManager.validateLogin(email, passwordHiddenField.getText());
        if (correct) {
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
            alert.setContentText("Password is Incorrect");
            alert.showAndWait();
        }
    }

    @FXML private void toPIN(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoginPIN.fxml"));
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
