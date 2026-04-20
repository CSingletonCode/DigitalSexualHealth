package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class NotificationsController {

    @FXML
    private Button returnButton;

    @FXML
    private VBox notificationsContainer;

    @FXML
    public void initialize() {
        returnButton.setOnAction(this::handleReturn);

        loadNotifications();
    }

    private void addNotificationCard(String title, String message, String time) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(14));
        card.setStyle(
                "-fx-background-color: #F7F8FA;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #E2E6EA;" +
                        "-fx-border-radius: 12;"
        );
        card.setMaxWidth(Double.MAX_VALUE);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #6C54B6;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(220);
        titleLabel.setMaxHeight(36);

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #444444;");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(220);
        messageLabel.setMaxHeight(36);

        Label timeLabel = new Label(time);
        timeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666666;");

        card.getChildren().addAll(titleLabel, messageLabel, timeLabel);

        notificationsContainer.getChildren().add(card);
        VBox.setVgrow(card, Priority.NEVER);
    }

    private void handleReturn(ActionEvent event) {
        switchScene(event, "/homepage.fxml", null);
    }

    private void switchScene(ActionEvent event, String fxmlFile, Clinic clinic) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            if (clinic != null) {
                Object nextController = loader.getController();
                if (nextController instanceof AppointmentPage) {
                    ((AppointmentPage) nextController).setClinicData(clinic);
                } else if (nextController instanceof clinicInfoController) {
                    ((clinicInfoController) nextController).setClinicData(clinic);
                }
            }

            Scene scene = new Scene(root);
            ThemeManager.applyTheme(scene, null,null);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load FXML file: " + fxmlFile);
        }
    }

    private void loadNotifications() {
        notificationsContainer.getChildren().clear();

        List<NotificationItem> notifications = DatabaseManager.pullNotifications();

        if (notifications.isEmpty()) {
            Label emptyLabel = new Label("No notifications yet");
            emptyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #777777;");
            notificationsContainer.getChildren().add(emptyLabel);
            return;
        }

        for (NotificationItem notification : notifications) {
            VBox card = createNotificationCard(
                    notification.getTitle(),
                    notification.getMessage(),
                    notification.getCreatedAt(),
                    notification.isRead()
            );
            notificationsContainer.getChildren().add(card);
        }

        DatabaseManager.markAllNotificationsAsRead(userSession.getInstance().getUserId());
    }

    private VBox createNotificationCard(String title, String message, String createdAt, boolean isRead) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(12));
        card.setMaxWidth(260);

        String backgroundColor = isRead ? "#F7F8FA" : "#EAF4FF";
        String borderColor = isRead ? "#E2E6EA" : "#B9D8FF";

        card.setStyle(
                "-fx-background-color: " + backgroundColor + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: " + borderColor + ";" +
                        "-fx-border-radius: 12;"
        );

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #6C54B6;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(236); // a bit less than card width because of padding

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555555;");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(236);

        Label timeLabel = new Label(formatNotificationTime(createdAt));
        timeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #777777;");

        card.getChildren().addAll(titleLabel, messageLabel, timeLabel);

        return card;
    }

    private String formatNotificationTime(String createdAt) {
        try {
            java.time.format.DateTimeFormatter inputFormatter =
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            java.time.LocalDateTime dateTime =
                    java.time.LocalDateTime.parse(createdAt, inputFormatter);

            java.time.LocalDate today = java.time.LocalDate.now();
            java.time.LocalDate notificationDate = dateTime.toLocalDate();

            if (notificationDate.equals(today)) {
                return "Today at " + dateTime.toLocalTime().withSecond(0).withNano(0);
            } else if (notificationDate.equals(today.minusDays(1))) {
                return "Yesterday";
            } else {
                return notificationDate.toString();
            }
        } catch (Exception e) {
            return createdAt;
        }
    }
}