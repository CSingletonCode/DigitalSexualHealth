package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

import static com.myapp.DatabaseManager.saveToDatabase;

public class TermsAndConditionsController {

    private User currentUser;

    @FXML
    void handleCreateAccount(ActionEvent event) {
        System.out.println("Create Account");
        if (!createAcc()){
            showAlert("Account Creation Failed","Please try again later.");
            return;
        }
        showAlert( "Account Created","Please login through the main page.");
        switchScene(event, "/login.fxml");
    }

    public void setUserData(User currentUser) {
        this.currentUser = currentUser;
    }

    private void showAlert(String title,String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Registration");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean createAcc() {
        return saveToDatabase(currentUser);
    }

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
        }
    }
}
