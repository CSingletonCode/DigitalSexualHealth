package com.myapp;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;   // ✅ ADDED
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;       // ✅ ADDED
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentSchedulePage {

    @FXML private Button returnButton;
    @FXML private Button bookAppointmentButton;
    @FXML private VBox appointmentListContainer;
    @FXML private Pane dimLayer;
    @FXML private VBox tutorialOverlay;
    @FXML private Label tutorialText;
    @FXML private Button tutorialNextButton;
    @FXML private Button tutorialSkipButton;
    @FXML private Button fakeBookButton;
    @FXML private Label helpButton;

    @FXML
    public void initialize() {
        returnButton.setOnAction(this::handleReturn);
        bookAppointmentButton.setOnAction(this::handleBookAppointment);
        helpButton.setOnMouseClicked(event -> {
            TutorialController.getInstance().start();
            startTutorial();
        });

        loadAppointments();

        if (!DatabaseManager.isAppointmentTutorialCompleted(userSession.getInstance().getUserId())) {
            TutorialController.getInstance().start();
            startTutorial();
        }
    }

    private void handleReturn(ActionEvent event) {
        switchScene(event, "/homepage.fxml");
    }

    private void handleBookAppointment(ActionEvent event) {
        if (TutorialController.getInstance().isActive()) {
            TutorialController.getInstance().nextStep();
            tutorialOverlay.setVisible(false);
            returnButton.setDisable(false);
        }

        switchScene(event,"/clinicPage.fxml");
    }

    private void loadAppointments() {
        if (appointmentListContainer != null) {
            appointmentListContainer.getChildren().clear();
        }

        List<VBox> cards = DatabaseManager.pullAppointments();

        cards.sort((a, b) -> {
            Label dateLabelA = (Label) a.lookup(".card-date-header");
            Label dateLabelB = (Label) b.lookup(".card-date-header");
            LocalDate dateA = LocalDate.parse(dateLabelA.getText());
            LocalDate dateB = LocalDate.parse(dateLabelB.getText());

            int cmp = dateB.compareTo(dateA);
            if (cmp != 0) return cmp;

            Label timeLabelA = (Label) a.getChildren().stream()
                    .filter(node -> node instanceof Label && ((Label) node).getText().startsWith("Time:"))
                    .map(node -> (Label) node)
                    .findFirst()
                    .orElse(null);

            Label timeLabelB = (Label) b.getChildren().stream()
                    .filter(node -> node instanceof Label && ((Label) node).getText().startsWith("Time:"))
                    .map(node -> (Label) node)
                    .findFirst()
                    .orElse(null);

            if (timeLabelA == null || timeLabelB == null) return 0;

            String tA = timeLabelA.getText().substring(6);
            String tB = timeLabelB.getText().substring(6);

            return tB.compareTo(tA);
        });

        appointmentListContainer.getChildren().addAll(cards);
    }

    public static String getAppStatus(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate apptDate = LocalDate.parse(date, formatter);
            LocalDate today = LocalDate.now();

            if (apptDate.isBefore(today)) return "Past";
            if (apptDate.isAfter(today)) return "Upcoming";
            return "Today";
        } catch (Exception e) {
            System.err.println("Failed to parse date: " + date);
            return "Unknown";
        }
    }

    public static VBox createAppointmentCard(String date, String status, String name, String time, String address) {
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

    private void startTutorial() {

        dimLayer.setVisible(true);
        tutorialOverlay.setVisible(true);
        returnButton.setDisable(true);

        Platform.runLater(() -> {

            // Get the button's bounds in SCENE coordinates
            Bounds sceneBounds = bookAppointmentButton.localToScene(
                    bookAppointmentButton.getBoundsInLocal()
            );

            // Convert scene coordinates into StackPane (root) coordinates
            StackPane root = (StackPane) dimLayer.getParent();

            Bounds localBounds = root.sceneToLocal(sceneBounds);

            // Apply exact same position & size
            fakeBookButton.setTranslateX(localBounds.getMinX());
            fakeBookButton.setTranslateY(localBounds.getMinY());
            fakeBookButton.setPrefWidth(bookAppointmentButton.getWidth());
            fakeBookButton.setPrefHeight(bookAppointmentButton.getHeight());

            fakeBookButton.setVisible(true);
            fakeBookButton.toFront();
            bookAppointmentButton.setVisible(false);
        });

        tutorialText.setText("Step 1:\nClick 'Book an Appointment' to begin booking.");

        tutorialNextButton.setDisable(false);
        tutorialNextButton.setOnAction(e -> {

            TutorialController.getInstance().nextStep();

            tutorialOverlay.setVisible(false);
            returnButton.setDisable(false);

            fakeBookButton.setVisible(false);
            bookAppointmentButton.setVisible(true);

            switchScene(new ActionEvent(returnButton, null), "/clinicPage.fxml");
        });

        tutorialSkipButton.setOnAction(e -> skipTutorial());
    }

    private void skipTutorial() {
        TutorialController.getInstance().stop();
        dimLayer.setVisible(false);
        tutorialOverlay.setVisible(false);
        returnButton.setDisable(false);

        fakeBookButton.setVisible(false);
        bookAppointmentButton.setVisible(true);
    }
}
