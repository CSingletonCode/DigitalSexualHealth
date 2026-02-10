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
import java.time.LocalDate;

public class AppointmentPage {

    @FXML private Button returnButton;
    @FXML private Label currentClinic;
    @FXML private Label currentClinicAddress;
    @FXML private DatePicker appointmentDatePicker;
    @FXML private ComboBox<String> appointmentTimeBox;
    @FXML private TextArea appointmentPurposeText;
    @FXML private Button appointmentSubmitButton;

    @FXML
    public void initialize() {
        // Database placeholder
        loadClinicData();

        appointmentTimeBox.getItems().addAll("09:00", "10:00", "11:00", "14:00", "15:00");
        returnButton.setOnAction(event -> handleReturn(event));
        appointmentSubmitButton.setOnAction(event -> handleSubmit());

        // Disable past dates in DatePicker
        appointmentDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
    }

    private void loadClinicData() {
        // Placeholder for Database
        currentClinic.setText("Clinic 1");
        currentClinicAddress.setText("36 Burgess Road, Southampton, SO16 5NE");
    }

    private void handleReturn(ActionEvent event) {
        switchScene(event,"/appointmentSchedule.fxml");
    }

    private void handleSubmit() {
        // Check if all fields are filled
        if (validateFields()) {
            // Show success popup
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Appointment booked successfully!");
            alert.showAndWait();

            // Close the current window
            Stage stage = (Stage) appointmentSubmitButton.getScene().getWindow();
            stage.close();
        } else {
            // Show error if fields are missing
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Form");
            alert.setHeaderText("Missing Information");
            alert.setContentText("Please ensure all fields are filled before submitting.");
            alert.showAndWait();
        }
    }

    private boolean validateFields() {
        boolean dateSelected = appointmentDatePicker.getValue() != null;
        boolean timeSelected = appointmentTimeBox.getValue() != null;
        boolean purposeFilled = !appointmentPurposeText.getText().trim().isEmpty();

        return dateSelected && timeSelected && purposeFilled;
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
            System.out.println("Could not load FXML file: " + fxmlFile);
        }
    }
}
