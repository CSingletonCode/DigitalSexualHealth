package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    private Clinic clinic;

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

        appointmentTimeBox.getItems().addAll("09:00", "10:00", "11:00", "14:00", "15:00");

        appointmentDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
    }

    private void handleReturn(ActionEvent event) {
        switchScene(event,"/clinicPage.fxml");
    }

    private void handleSubmit(ActionEvent event) {

        if (!validateFields()) {
            showAlert("Incomplete Form","Missing Information", "Please ensure all fields are filled before submitting.");
            return;
        }

        try {
            //load confirmation fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/appointmentConfirmation.fxml"));
            Parent root = loader.load();

            Stage ownerStage = (Stage) appointmentSubmitButton.getScene().getWindow();

            AppointmentConfirmationController controller = loader.getController();

            controller.setClinic(currentClinic.getText() + "\n" + currentClinicAddress.getText());
            controller.setClinicID(this.clinic.getId());
            controller.setDate(appointmentDatePicker.getValue().toString());
            controller.setTime(appointmentTimeBox.getValue());
            controller.setPurpose(appointmentPurposeText.getText().trim());

            //create popup stage
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(ownerStage);

            stage.setOnShown(e -> {
                double ownerContentX = ownerStage.getX() + ownerStage.getScene().getX();
                double ownerContentY = ownerStage.getY() + ownerStage.getScene().getY();

                double ownerWidth = ownerStage.getScene().getWidth();
                double ownerHeight = ownerStage.getScene().getHeight();

                stage.setX(ownerContentX + (ownerWidth - stage.getWidth()) / 2);
                stage.setY(ownerContentY + (ownerHeight - stage.getHeight()) / 2);
            });

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", null, "Unable to open confirmation window.");
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
            ThemeManager.applyTheme(scene);
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
        alert.getDialogPane().setPrefWidth(200);
        alert.showAndWait();
    }
}
