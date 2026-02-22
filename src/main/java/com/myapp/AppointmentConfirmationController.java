package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;

public class AppointmentConfirmationController {
    @FXML private Label nameLabel;
    @FXML private Label ageLabel;
    @FXML private Label genderLabel;
    @FXML private Label clinicDetails;
    @FXML private Label dateLabel;
    @FXML private Label timeLabel;
    @FXML private Label purposeLabel;
    @FXML private Button backButton;
    @FXML private Button confirmButton;
    private Integer clinicID;

    @FXML
    public void initialize() {
        backButton.setOnAction(this::handleBack);
        confirmButton.setOnAction(this::handleConfirm);

        String name = userSession.getInstance().getFirstName() + " " + userSession.getInstance().getLastName();
        nameLabel.setText(name);

        LocalDate dob = LocalDate.parse(userSession.getInstance().getDob());
        String age = String.valueOf(Period.between(dob, LocalDate.now()).getYears());
        ageLabel.setText(age);

        genderLabel.setText(userSession.getInstance().getGender());
    }

    public void setClinic (String clinic) {
        clinicDetails.setText(clinic);
    }

    public void setClinicID (Integer clinicID) {
        this.clinicID = clinicID;
    }

    public void setDate(String date) {
        dateLabel.setText(date);
    }

    public void setTime(String time) {
        timeLabel.setText(time);
    }

    public void setPurpose(String purpose) {
        purposeLabel.setText(purpose);
    }

    //back button -> close popup
    private void handleBack(ActionEvent event) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    //confirm button -> submit appointment
    private void handleConfirm(ActionEvent event) {

        boolean success = DatabaseManager.insertAppointment(
                dateLabel.getText(),
                timeLabel.getText(),
                clinicID,
                purposeLabel.getText().trim()
        );

        Stage popupStage = (Stage) confirmButton.getScene().getWindow();

        if (!success) {
            showAlert("Error", null, "Appointment booking failed. Try again later.");
            popupStage.close();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Appointment booked successfully!");
        alert.getDialogPane().setPrefWidth(250);
        alert.initOwner(popupStage);
        alert.showAndWait();

        popupStage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/appointmentSchedule.fxml"));
            Parent root = loader.load();

            Stage mainStage = (Stage) popupStage.getOwner();

            Scene scene = new Scene(root);
            ThemeManager.applyTheme(scene);
            mainStage.setScene(scene);
            mainStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", null, "Could not load appointment schedule.");
        }
    }

    private void showAlert(String title, String Header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(Header);
        alert.setContentText(message);
        alert.getDialogPane().setPrefWidth(250);
        alert.showAndWait();
    }

    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            ThemeManager.applyTheme(scene);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load FXML file: " + fxmlFile);
        }
    }
}
