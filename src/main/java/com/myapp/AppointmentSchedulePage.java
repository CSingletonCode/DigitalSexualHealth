package com.myapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class AppointmentSchedulePage {

    @FXML
    private Button returnButton;

    @FXML
    private Button bookAppointmentButton;

    @FXML
    private VBox appointmentListContainer;

    @FXML
    public void initialize() {
        returnButton.setOnAction(event -> handleReturn());
        bookAppointmentButton.setOnAction(event -> handleBookAppointment());

        // Database placeholder
        loadAppointments();
    }

    private void handleReturn() {
        // switch page
        // return to previous page
    }

    private void handleBookAppointment() {
        // switch page
        // go to next page
    }

    private void loadAppointments() {
        if (appointmentListContainer != null) {
            appointmentListContainer.getChildren().clear();
        }

        // Database placeholder
        int databaseRecordCount = 1;

        for (int i = 0; i < databaseRecordCount; i++) {

            // Database placeholder

            VBox card = createAppointmentCard(
                    "16th December 2025",
                    "UPCOMING",
                    "Clinic 1",
                    "14:00",
                    "38, Burgess Road, SO16 5NE"
            );

            appointmentListContainer.getChildren().add(card);
        }
    }

    private VBox createAppointmentCard(String date, String status, String name, String time, String address) {
        VBox card = new VBox();
        card.setSpacing(8.0);
        card.getStyleClass().add("schedule-card");
        card.setPadding(new Insets(15, 15, 15, 15));

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(10.0);

        Label dateLabel = new Label(date);
        dateLabel.getStyleClass().add("card-date-header");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label statusBadge = new Label(status);
        statusBadge.getStyleClass().add("status-badge");
        if ("UPCOMING".equalsIgnoreCase(status)) {
            statusBadge.getStyleClass().add("upcoming");
        } else {
            statusBadge.getStyleClass().add("past");
        }

        header.getChildren().addAll(dateLabel, spacer, statusBadge);

        Label nameLabel = new Label("Name: " + name);
        nameLabel.getStyleClass().add("card-detail-text");

        Label timeLabel = new Label("Time: " + time);
        timeLabel.getStyleClass().add("card-detail-text");

        Label addressLabel = new Label("Address: " + address);
        addressLabel.getStyleClass().add("card-detail-text");
        addressLabel.setWrapText(true);

        card.getChildren().addAll(header, nameLabel, timeLabel, addressLabel);

        return card;
    }
}