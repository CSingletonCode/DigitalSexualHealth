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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ClinicController {
    @FXML private Button returnButton;
    @FXML private VBox clinicListContainer;
    @FXML private VBox tutorialOverlay;
    @FXML private Label tutorialText;
    @FXML private Button tutorialNextButton;
    @FXML private Button tutorialSkipButton;
    @FXML private Button fakeBookButton;
    @FXML private Button fakeInfoButton;
    @FXML private Label helpButton;
    @FXML private ComboBox<String> costFilter;
    @FXML private Button showMap;

    @FXML
    public void initialize() {
        returnButton.setOnAction(this::handleReturn);
        showMap.setOnAction(this::openMapPage);
        helpButton.setOnMouseClicked(event -> {
            TutorialController.getInstance().start();
            TutorialController.getInstance().nextStep();
            startClinicTutorial();
        });

        costFilter.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                loadClinics(newValue);
            }
        });

        costFilter.getSelectionModel().select("All");

        if (TutorialController.getInstance().isActive()
                && TutorialController.getInstance().getStep() >= 1
                && TutorialController.getInstance().getStep() <= 2) {

            startClinicTutorial();
        }
    }

    private void loadClinics(String filter) {
        if (clinicListContainer != null) {
            clinicListContainer.getChildren().clear();
        }

        List<Clinic> clinics;
        if (filter == null || filter.isEmpty() || filter.equals("All")) {
            clinics = DatabaseManager.getClinics(null);
        } else {
            clinics = DatabaseManager.getClinics(filter);
        }

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
        card.setPrefSize(282, 125);

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(10.0);

        Label nameLabel = new Label(clinic.getName());
        nameLabel.getStyleClass().add("card-date-header");

        Label distanceLabel = new Label(clinic.getStringDistance() + " miles away");
        distanceLabel.setTextFill(Color.BLACK);
        header.getChildren().addAll(nameLabel);

        Label addressLabel = new Label(clinic.getAddress());
        addressLabel.getStyleClass().add("card-detail-text");
        addressLabel.setWrapText(true);

        HBox actionRow = new HBox();
        actionRow.setAlignment(Pos.CENTER_RIGHT);
        actionRow.setSpacing(10.0);
        actionRow.setPrefHeight(38.0);

        Button bookBtn = new Button("Book");
        bookBtn.getStyleClass().add("button-small");
        bookBtn.setOnAction(event -> {

            if (TutorialController.getInstance().isActive()
                    && TutorialController.getInstance().getStep() == 1) {
                return; // Block booking during tutorial Step 1
            }

            controller.switchScene(event, "/appointment.fxml", clinic);
        });
        bookBtn.setId("bookBtn_" + clinic.getId());

        Button infoBtn = new Button("More Info");
        infoBtn.getStyleClass().add("button-small");
        infoBtn.setStyle("-fx-background-color: #5e6b70;");
        infoBtn.setOnAction(event -> controller.switchScene(event, "/clinicInfoPage.fxml", clinic));

        actionRow.getChildren().addAll(distanceLabel, bookBtn, infoBtn);

        card.getChildren().addAll(header, addressLabel, actionRow);

        return card;
    }

    private void openMapPage(ActionEvent event) {
        switchScene(event, "/clinicMap.fxml", null);
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
            ThemeManager.applyTheme(scene,null);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load FXML file: " + fxmlFile);
        }
    }

    private void startClinicTutorial() {

        tutorialOverlay.setVisible(true);
        returnButton.setDisable(true);

        int step = TutorialController.getInstance().getStep();

        Platform.runLater(() -> {

            // Get root StackPane
            StackPane root = (StackPane) tutorialOverlay.getParent();

            // Always target the FIRST clinic card
            if (clinicListContainer.getChildren().isEmpty()) return;

            VBox firstCard = (VBox) clinicListContainer.getChildren().get(0);
            HBox actionRow = (HBox) firstCard.getChildren().get(2);

            Button realBookBtn = (Button) actionRow.getChildren().get(1);
            Button realInfoBtn = (Button) actionRow.getChildren().get(2);

            if (step == 1) {
                Bounds bounds = realBookBtn.localToScene(realBookBtn.getBoundsInLocal());
                Bounds localBounds = root.sceneToLocal(bounds);

                fakeBookButton.setMinWidth(bounds.getWidth());
                fakeBookButton.setPrefWidth(bounds.getWidth());
                fakeBookButton.setMinHeight(bounds.getHeight());
                fakeBookButton.setPrefHeight(bounds.getHeight());

                // 3. Position using Translations
                fakeBookButton.setTranslateX(localBounds.getMinX());
                fakeBookButton.setTranslateY(localBounds.getMinY());

                fakeBookButton.setVisible(true);
                fakeBookButton.toFront();
                realBookBtn.setVisible(false);

            } else if (step == 2) {

                Bounds bounds = realInfoBtn.localToScene(realInfoBtn.getBoundsInLocal());
                Bounds localBounds = root.sceneToLocal(bounds);

                fakeInfoButton.setMinWidth(bounds.getWidth());
                fakeInfoButton.setPrefWidth(bounds.getWidth());
                fakeInfoButton.setMinHeight(bounds.getHeight());
                fakeInfoButton.setPrefHeight(bounds.getHeight());

                fakeInfoButton.setTranslateX(localBounds.getMinX());
                fakeInfoButton.setTranslateY(localBounds.getMinY());

                fakeInfoButton.setStyle("-fx-background-color: #5e6b70;");

                fakeInfoButton.setVisible(true);
                fakeInfoButton.toFront();
                realInfoBtn.setVisible(false);
            }
        });

        if (step == 1) {

            tutorialText.setText(
                    "Step 2:\nClick the 'Book' button to book an appointment."
            );

            tutorialNextButton.setOnAction(e -> {
                fakeBookButton.setVisible(false);

                if (!clinicListContainer.getChildren().isEmpty()) {
                    VBox firstCard = (VBox) clinicListContainer.getChildren().get(0);
                    HBox actionRow = (HBox) firstCard.getChildren().get(2);
                    actionRow.getChildren().get(1).setVisible(true); // Index 1 is the real Book button
                }

                TutorialController.getInstance().nextStep();
                startClinicTutorial();
            });

        } else if (step == 2) {

            tutorialText.setText(
                    "Step 3:\nClick the 'More Info' button to view clinic details."
            );

            tutorialNextButton.setOnAction(e -> {

                fakeInfoButton.setVisible(false);

                TutorialController.getInstance().nextStep();
                tutorialOverlay.setVisible(false);
                returnButton.setDisable(false);

                switchScene(new ActionEvent(returnButton, null),
                        "/appointment.fxml",
                        null);
            });
        }

        tutorialSkipButton.setOnAction(e -> skipTutorial());
    }

    private void skipTutorial() {

        TutorialController.getInstance().stop();

        tutorialOverlay.setVisible(false);
        returnButton.setDisable(false);

        fakeBookButton.setVisible(false);
        fakeInfoButton.setVisible(false);

        // Restore real buttons if hidden
        if (!clinicListContainer.getChildren().isEmpty()) {
            VBox firstCard = (VBox) clinicListContainer.getChildren().get(0);
            HBox actionRow = (HBox) firstCard.getChildren().get(2);

            actionRow.getChildren().get(1).setVisible(true); // Book
            actionRow.getChildren().get(2).setVisible(true); // Info
        }
    }

}
