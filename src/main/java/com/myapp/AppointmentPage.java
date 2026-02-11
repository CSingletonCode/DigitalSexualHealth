package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class AppointmentPage {

    @FXML private Button returnButton;
    @FXML private Label currentClinic;
    @FXML private Label currentClinicAddress;
    @FXML private DatePicker appointmentDatePicker;
    @FXML private ComboBox<String> appointmentTimeBox;
    @FXML private TextArea appointmentPurposeText;
    @FXML private Button appointmentSubmitButton;
    @FXML private VBox clinicListContainer, closestClinicBox;
    @FXML private ScrollPane clinicScrollPane;
    private int selected_clinic_id = -1;
    List<Clinic> clinics;

    @FXML
    public void initialize() {
        double myLat = 50.9097;
        double myLon = -1.4044;
        clinics = DatabaseManager.getClinics(myLat,myLon);
        if (!clinics.isEmpty()) {
            Clinic closest = clinics.get(0);
            updateHeader(closest);

            for (Clinic clinic : clinics) {
                clinicListContainer.getChildren().add(createListRow(clinic));
            }
        }

        appointmentTimeBox.getItems().addAll("09:00", "10:00", "11:00", "14:00", "15:00");
        returnButton.setOnAction(this::handleReturn);
        appointmentSubmitButton.setOnAction(this::handleSubmit);

        // Disable past dates in DatePicker
        appointmentDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
    }

    @FXML
    private void toggleClinicList() {
        boolean isVisible = clinicScrollPane.isVisible();
        clinicScrollPane.setVisible(!isVisible);
        clinicScrollPane.setManaged(!isVisible);
    }

    private HBox createListRow(Clinic clinic) {
        HBox row = new HBox(10);
        row.getStyleClass().add("clinic-list-row");

        VBox textData = new VBox(2);

        Label name = new Label(clinic.getName());
        name.getStyleClass().add("clinic-name-small");

        Label dist = new Label(String.format("%.1f km away", clinic.getDistance()));
        dist.getStyleClass().add("clinic-distance-tag");

        textData.getChildren().addAll(name, dist);
        row.getChildren().add(textData);

        row.setOnMouseClicked(e -> {
            updateHeader(clinic);
            toggleClinicList();
        });

        return row;
    }

    private void updateHeader(Clinic clinic) {
        currentClinic.setText(clinic.getName());
        currentClinicAddress.setText(clinic.getAddress() + " (" + String.format("%.1f", clinic.getDistance()) + " km)");
        selected_clinic_id = clinic.getId();
    }

    private void handleReturn(ActionEvent event) {
        switchScene(event,"/appointmentSchedule.fxml");
    }

    private void handleSubmit(ActionEvent event) {
        // Check if all fields are filled
        if (validateFields()) {

            boolean success = DatabaseManager.insertAppointment(
                    appointmentDatePicker.getValue().toString(),
                    appointmentTimeBox.getValue(),
                    selected_clinic_id,
                    appointmentPurposeText.getText().trim()
                    );
            if (success) {
                showAlert("Success",null,"Appointment booked successfully!");
            } else {
                showAlert("Error",null,"Appointment booking failed. Try again later.");
            }

            // Close the current window
            switchScene(event,"/appointmentSchedule.fxml");
        } else {
            // Show error if fields are missing
            showAlert("Incomplete Form","Missing Information","Please ensure all fields are filled before submitting.");
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

    private void showAlert(String title, String Header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(Header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
