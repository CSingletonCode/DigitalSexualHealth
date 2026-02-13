package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ClinicController {
    @FXML
    private Button returnButton;

    @FXML
    private VBox clinicListContainer;

    @FXML
    public void initialize() {
        returnButton.setOnAction(this::handleReturn);

        loadClinics();
    }

    private void loadClinics() {
        if (clinicListContainer != null) {
            clinicListContainer.getChildren().clear();
        }

        List<Clinic> clinics = DatabaseManager.getClinics();

        for (Clinic clinic : clinics) {
            VBox card = createClinicCard(clinic, this);
            clinicListContainer.getChildren().add(card);
        }
    }
    private void handleReturn(ActionEvent event) {
        switchScene(event, "/appointmentSchedule.fxml", null);
    }

    public static VBox createClinicCard(Clinic clinic, ClinicController controller) {
        VBox card = new VBox();
        card.setSpacing(8);
        card.getStyleClass().add("schedule-card");
        card.setPadding(new Insets(15, 15, 15, 15));
        card.setPrefSize(282, 143);

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(10.0);

        Label nameLabel = new Label(clinic.getName());
        nameLabel.getStyleClass().add("card-date-header");
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        Label distanceLabel = new Label(clinic.getStringDistance() + " miles away");
        distanceLabel.setTextFill(Color.BLACK);
        header.getChildren().addAll(nameLabel, headerSpacer, distanceLabel);

        Label addressLabel = new Label(clinic.getAddress());
        addressLabel.getStyleClass().add("card-detail-text");
        addressLabel.setWrapText(true);

        HBox actionRow = new HBox();
        actionRow.setAlignment(Pos.CENTER);
        actionRow.setSpacing(10.0);
        actionRow.setPrefHeight(38.0);

        Region buttonSpacer = new Region();
        buttonSpacer.setPrefWidth(82.0);

        Button bookBtn = new Button("Book");
        bookBtn.getStyleClass().add("button-small");
        bookBtn.setOnAction(event -> controller.switchScene(event, "/appointment.fxml", clinic));

        Button infoBtn = new Button("More Info");
        infoBtn.getStyleClass().add("button-small");
        infoBtn.setStyle("-fx-background-color: #5e6b70;");
        infoBtn.setOnAction(event -> controller.switchScene(event, "/clinicInfoPage.fxml", clinic));

        actionRow.getChildren().addAll(buttonSpacer, bookBtn, infoBtn);

        card.getChildren().addAll(header, addressLabel, actionRow);

        return card;
    }


    private void switchScene(ActionEvent event, String fxmlFile, Clinic clinic) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            if (!(clinic == null)){
                Object nextController = loader.getController();
                if (nextController instanceof AppointmentPage) {
                    ((AppointmentPage) nextController).setClinicData(clinic);
                } else if (nextController instanceof clinicInfoController) {
                    ((clinicInfoController) nextController).setClinicData(clinic);
                }
            }

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
