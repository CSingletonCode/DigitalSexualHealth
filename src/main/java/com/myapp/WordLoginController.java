package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class WordLoginController {

    @FXML private TextField passwordField;

    @FXML private Hyperlink switchToPIN;

    String email;

    public void initialize() throws IOException{
        passwordField.setFocusTraversable(false);
        String[] current_session = sessionManager.getSession();
        if (current_session[1].equals("false")) {
            switchToPIN.setVisible(false);
            switchToPIN.setManaged(false);
        }

        email = current_session[0];

    }

    @FXML private void handleLogin(){}

    @FXML private void toPIN(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoginPIN.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML private void changeAccount(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
