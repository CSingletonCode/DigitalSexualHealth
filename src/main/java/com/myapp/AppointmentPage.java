package com.myapp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppointmentPage {

    @FXML private Button returnButton;
    @FXML private Label currentClinic;
    @FXML private Label currentClinicAddress;
    @FXML private DatePicker appointmentDatePicker;
    @FXML private ComboBox<String> appointmentTimeBox;
    @FXML private TextArea appointmentPurposeText;
    @FXML private Button appointmentSubmitButton;
    private Clinic clinic;
    private List<String> allTimeSlots;
    @FXML private VBox tutorialOverlay;
    @FXML private Label tutorialText;
    @FXML private Button tutorialNextButton;
    @FXML private StackPane confirmationOverlay;
    @FXML private Label nameLabel;
    @FXML private Label ageLabel;
    @FXML private Label genderLabel;
    @FXML private Label clinicDetails;
    @FXML private Label dateLabel;
    @FXML private Label timeLabel;
    @FXML private Label purposeLabel;
    @FXML private Button backButton;
    @FXML private Button confirmButton;
    @FXML private DatePicker fakeDatePicker;
    @FXML private ComboBox<String> fakeTimeBox;
    @FXML private TextArea fakePurposeText;
    @FXML private Button fakeSubmitButton;
    @FXML private Label helpButton;

    public void setClinicData(Clinic cl) {
        this.clinic = cl;
        if (clinic != null) {
            currentClinic.setText(clinic.getName());
            currentClinicAddress.setText(clinic.getAddress());
        }
    }

    @FXML
    public void initialize() {
        returnButton.setOnAction(this::handleReturn);
        appointmentSubmitButton.setOnAction(this::handleSubmit);
        backButton.setOnAction(this::handleBack);
        confirmButton.setOnAction(this::handleConfirm);
        helpButton.setOnMouseClicked(event -> {
            TutorialController.getInstance().start();
            TutorialController.getInstance().nextStep();
            TutorialController.getInstance().nextStep();
            startAppointmentTutorial();
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        allTimeSlots = Stream.iterate(LocalTime.of(9, 0),
                        t -> !t.isAfter(LocalTime.of(17, 0)),
                        t -> t.plusMinutes(15))
                .map(t -> t.format(formatter))
                .collect(Collectors.toList());

        appointmentDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        appointmentDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            updateAvailableTimes(newDate);
        });

        if (TutorialController.getInstance().isActive()
                && TutorialController.getInstance().getStep() >= 2) {

            startAppointmentTutorial();
        }

        confirmationOverlay.setVisible(false);
    }

    private void handleReturn(ActionEvent event) {
        switchScene(event,"/clinicPage.fxml");
    }

    private void handleSubmit(ActionEvent event) {
        if (!validateFields()) {
            showAlert("Incomplete Form","Missing Information", "Please ensure all fields are filled before submitting.");
            return;
        }

        if (TutorialController.getInstance().isActive()
                && TutorialController.getInstance().getStep() == 2) {
            tutorialOverlay.setVisible(false);
            returnButton.setDisable(false);
        }

        String fullName = userSession.getInstance().getFirstName() + " " + userSession.getInstance().getLastName();
        nameLabel.setText(fullName);

        LocalDate dob = LocalDate.parse(userSession.getInstance().getDob());
        ageLabel.setText(String.valueOf(Period.between(dob, LocalDate.now()).getYears()));
        genderLabel.setText(userSession.getInstance().getGender());

        clinicDetails.setText(currentClinic.getText() + "\n" + currentClinicAddress.getText());
        dateLabel.setText(appointmentDatePicker.getValue().toString());
        timeLabel.setText(appointmentTimeBox.getValue());
        purposeLabel.setText(appointmentPurposeText.getText().trim());

        // Show the Overlay
        confirmationOverlay.setVisible(true);

        DatabaseManager.addNotification(
                userSession.getInstance().getUserId(),
                "Booking Confirmed",
                "Your appointment at " + currentClinic.getText() + " on " + appointmentDatePicker.getValue().toString() + " has been booked successfully."
        );
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
            ThemeManager.applyTheme(scene,null, null);
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
        alert.getDialogPane().setPrefWidth(250);
        alert.showAndWait();
    }

    private void updateAvailableTimes(LocalDate selectedDate) {

        appointmentTimeBox.getItems().clear();

        if (selectedDate == null) return;

        LocalDate today = LocalDate.now();

        // If Not today → show all times
        if (!selectedDate.equals(today)) {
            appointmentTimeBox.getItems().addAll(allTimeSlots);
            return;
        }

        // If today → filter times after now
        LocalTime now = LocalTime.now();

        for (String timeSlot : allTimeSlots) {
            LocalTime slotTime = LocalTime.parse(timeSlot);

            if (slotTime.isAfter(now)) {
                appointmentTimeBox.getItems().add(timeSlot);
            }
        }

        if (appointmentTimeBox.getItems().isEmpty()) {
            showAlert("No Available Slots", null, "There are no available time slots remaining for today.");
        }

        // Clear previous selection if invalid
        appointmentTimeBox.setValue(null);
    }

    private void startAppointmentTutorial() {

        tutorialOverlay.setVisible(true);
        returnButton.setDisable(true);

        Platform.runLater(() -> {
            if (tutorialOverlay.getScene() == null) return;
            StackPane root = (StackPane) tutorialOverlay.getParent();

            // Helper to position clones
            syncClone(appointmentDatePicker, fakeDatePicker, root);
            syncClone(appointmentTimeBox, fakeTimeBox, root);
            syncClone(appointmentPurposeText, fakePurposeText, root);
            syncClone(appointmentSubmitButton, fakeSubmitButton, root);

            // Hide real ones while tutorial is active
            appointmentDatePicker.setVisible(false);
            appointmentTimeBox.setVisible(false);
            appointmentPurposeText.setVisible(false);
            appointmentSubmitButton.setVisible(false);
        });

        tutorialText.setText("Step 4:\nSelect a date, choose a time, enter your purpose, then click Submit.");

        tutorialNextButton.setDisable(false);
        tutorialNextButton.setOnAction(e -> {
            TutorialController.getInstance().stop();
            tutorialOverlay.setVisible(false);
            returnButton.setDisable(false);

            fakeDatePicker.setVisible(false);
            fakeTimeBox.setVisible(false);
            fakePurposeText.setVisible(false);
            fakeSubmitButton.setVisible(false);

            appointmentDatePicker.setVisible(true);
            appointmentTimeBox.setVisible(true);
            appointmentPurposeText.setVisible(true);
            appointmentSubmitButton.setVisible(true);
            switchScene(new ActionEvent(returnButton, null), "/appointmentSchedule.fxml");
        });
    }

    private void syncClone(Node real, Node fake, StackPane root) {
        // 1. Get exact pixel dimensions
        Bounds boundsInScene = real.localToScene(real.getBoundsInLocal());
        Bounds boundsInRoot = root.sceneToLocal(boundsInScene);

        double w = boundsInScene.getWidth();
        double h = boundsInScene.getHeight();

        if (fake instanceof DatePicker) fake.getStyleClass().add("date-picker");
        else if (fake instanceof ComboBox) fake.getStyleClass().add("combo-box");
        else if (fake instanceof TextArea) fake.getStyleClass().add("text-area");
        else if (fake instanceof Button) {
            fake.getStyleClass().add(".submit-button-large");
            ((Button) fake).setAlignment(Pos.CENTER); // Fix text alignment
        }

        // 3. LOCK DIMENSIONS: This stops window enlargement and fixes icon scaling
        if (fake instanceof Region) {
            Region r = (Region) fake;
            r.setMinWidth(w);
            r.setPrefWidth(w);
            r.setMaxWidth(w);

            r.setMinHeight(h);
            r.setPrefHeight(h);
            r.setMaxHeight(h);
        }

        // 4. Reset padding/insets to 0 to prevent internal shifting
        fake.setStyle("-fx-background-insets: 0; -fx-padding: 0;");

        // 5. Apply Translation
        fake.setTranslateX(boundsInRoot.getMinX());
        fake.setTranslateY(boundsInRoot.getMinY());

        fake.setVisible(true);
        fake.toFront();
    }

    private void handleBack(ActionEvent event) {
        confirmationOverlay.setVisible(false);
    }

    //confirm button -> submit appointment
    private void handleConfirm(ActionEvent event) {

        boolean success = DatabaseManager.insertAppointment(
                dateLabel.getText(),
                timeLabel.getText(),
                clinic.getId(),
                purposeLabel.getText().trim()
        );

        if (!success) {
            showAlert("Error", null, "Appointment booking failed. Try again later.");
            confirmationOverlay.setVisible(false);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Appointment booked successfully!");
        alert.getDialogPane().setPrefWidth(250);
        alert.showAndWait();

        switchScene(event,"/appointmentSchedule.fxml");
    }
}
