package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class clinicInfoController {
    @FXML
    private Button returnButton,bookButton;

    @FXML
    Label clinicName,clinicUrgentPhone,clinicPhone,clinicEmail,clinicAddress;

    @FXML
    Label Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday;

    private Clinic clinic;

    public void setClinicData(Clinic cl) {
        this.clinic = cl;
        if (clinic != null){
            clinicName.setText(clinic.getName());
            clinicUrgentPhone.setText(clinic.getUrgentPhone());
            clinicPhone.setText(clinic.getPhone());
            clinicEmail.setText(clinic.getEmail());
            clinicAddress.setText(clinic.getAddress());

            List<String> hoursList = clinic.getHours();
            Label[] dayLabels = {Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday};
            for (int i = 0; i < dayLabels.length; i++) {
                if (i < hoursList.size()) {
                    String hours = hoursList.get(i);
                    dayLabels[i].setText((hours == null || hours.isBlank()) ? "CLOSED" : hours);
                }
            }
        }
    }

    public void initialize() {
        returnButton.setOnAction(this::handleReturn);
        bookButton.setOnAction(event -> switchScene(event, "/appointment.fxml", clinic));
    }

    private void handleReturn(ActionEvent event) {
        switchScene(event, "/clinicPage.fxml", null);
    }

    private void switchScene(ActionEvent event, String fxmlFile, Clinic clinic) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            if (!(clinic == null)){
                AppointmentPage nextController = loader.getController();
                nextController.setClinicData(clinic);
            }

            Scene scene = new Scene(root);
            ThemeManager.applyTheme(scene,null, null);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load FXML file: " + fxmlFile);
        }
    }
}
