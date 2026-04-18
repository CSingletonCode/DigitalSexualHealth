package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class updateInfoController {

    @FXML private Button returnButton;
    @FXML private TextField first_name, last_name, email;
    @FXML private DatePicker dobBox;
    @FXML private ComboBox<String> genderBox;
    @FXML private Button firstNameButton, lastNameButton, emailButton, dobBoxButton, genderBoxButton;
    String userEmail;

    @FXML
    public void initialize() {
        returnButton.setOnAction(this::handleReturn);
        userSession user = userSession.getInstance();
        first_name.setText(user.getFirstName());
        last_name.setText(user.getLastName());
        userEmail = user.getEmail();
        email.setText(userEmail);

        String storedDate = user.getDob();

        if (storedDate != null && !storedDate.isEmpty()) {
            try {
                LocalDate dateObject = LocalDate.parse(storedDate);
                dobBox.setValue(dateObject);
            } catch (DateTimeParseException e) {
                System.err.println("Could not parse date: " + storedDate);
            }
        }

        dobBox.setDisable(true);
        dobBox.setStyle("-fx-opacity: 1;");

        genderBox.getItems().addAll("Woman",
                "Man",
                "Non-binary",
                "Transgender Woman",
                "Transgender Man",
                "Intersex",
                "Prefer not to say",
                "Another identity");

        genderBox.setValue(user.getGender());
        genderBox.setDisable(true);
        genderBox.setStyle("-fx-opacity: 1;");

        firstNameButton.setOnAction(e -> toggleTextField(first_name, firstNameButton, "first_name"));
        lastNameButton.setOnAction(e -> toggleTextField(last_name, lastNameButton, "last_name"));
        emailButton.setOnAction(e -> toggleTextField(email, emailButton, "email"));

        dobBoxButton.setOnAction(e -> toggleNode(dobBox, dobBoxButton, "dob"));
        genderBoxButton.setOnAction(e -> toggleNode(genderBox, genderBoxButton, "gender"));
    }

    private void toggleTextField(TextField field, Button btn, String fieldName) {
        if (!field.isEditable()) {
            field.setEditable(true);
            field.requestFocus();
            btn.setText("Confirm");
        } else {
            saveData(fieldName, field.getText());
            field.setEditable(false);
            btn.setText("Edit");
        }
    }

    private void toggleNode(javafx.scene.layout.Region node, Button btn, String fieldName) {
        if (node.isDisable()) {
            node.setDisable(false);
            node.setStyle("-fx-opacity: 1; -fx-border-color: #0078d7;");
            btn.setText("Confirm");
        } else {
            String value = "";
            if (node instanceof DatePicker) value = ((DatePicker) node).getValue().toString();
            if (node instanceof ComboBox) value = ((ComboBox<String>) node).getValue();

            saveData(fieldName, value);
            node.setDisable(true);
            node.setStyle("-fx-opacity: 1; -fx-border-color: transparent;");
            btn.setText("Edit");
        }
    }

    private void saveData(String fieldName, String value) {
        if (DatabaseManager.updateData(value, fieldName)){
            DatabaseManager.fetchAndStartSession(userEmail);
        }
    }

    private void handleReturn(ActionEvent event) {
        switchScene(event, "/settings.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            ThemeManager.applyTheme(scene,null);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load FXML file: " + fxmlFile);
        }
    }

}
