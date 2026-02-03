package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class Signup2Controller {

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private DatePicker dobBox;
    @FXML
    private ComboBox<String> genderBox;

    @FXML
    public void initialize() {
        // This adds items when the screen loads
        genderBox.getItems().addAll("Woman",
                "Man",
                "Non-binary",
                "Transgender Woman",
                "Transgender Man",
                "Intersex",
                "Prefer not to say",
                "Another identity");
    }

    @FXML
    void handleCreateAccount(ActionEvent event) {
        System.out.println("Create Account");
        switchScene(event, "/TermsAndConditions.fxml");
    }

    // Reuse the helper method
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