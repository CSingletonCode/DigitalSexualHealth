package com.myapp;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.util.List;

public class ClinicMap {
    @FXML
    private WebView webView;
    private WebEngine engine;
    @FXML
    private Button returnButton;
    @FXML
    private Button saveButton;

    private String mapMode = "clinic";
    private double selectedLat, selectedLng;

    List<Clinic> clinics;
    private MapBridge mapBridge;

    public void setMapMode(String mode) {
        this.mapMode = mode;
    }

    public void initialize() {
        returnButton.setOnAction(this::handleReturn);

        saveButton.setVisible(false);
        saveButton.setManaged(false);

        engine = webView.getEngine();
        engine.load(getClass().getResource("/map.html").toExternalForm());

        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) engine.executeScript("window");

                this.mapBridge = new MapBridge(this::handleMapSelection, this::handleClinicSelection);
                window.setMember("javaConnector", mapBridge);

                engine.executeScript(
                        "if (typeof setMapMode === 'function') {" +
                                "    setMapMode('" + mapMode + "');" +
                                "} else {" +
                                "    console.error('Java Error: setMapMode function is not defined yet!');" +
                                "}");

                if (mapMode.equals("picker")) {
                    saveButton.setVisible(true);
                    saveButton.setManaged(true);
                }

                double startLat = userSession.getInstance().getMyLat();
                double startLng = userSession.getInstance().getMyLon();

                if (startLat == 0) { startLat = 50.9097; startLng = -1.4044; }

                engine.executeScript(String.format(java.util.Locale.US, "map.setView([%f, %f], 12);", startLat, startLng));

                displayExistingClinics();
            }
        });
    }

    private void handleClinicSelection(String s) {
        javafx.application.Platform.runLater(() -> {
            try {
                int id = Integer.parseInt(s);
                javafx.event.ActionEvent manualEvent = new javafx.event.ActionEvent(webView, javafx.event.Event.NULL_SOURCE_TARGET);

                if (clinics != null) {
                    for (Clinic clinic : clinics) {
                        if (clinic.getId() == id) {
                            switchScene(manualEvent, "/clinicInfoPage.fxml", clinic);
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    String returningPage = "/clinicPage.fxml";

    public void setReturningPage(String returningPage) {
        this.returningPage = returningPage;
    }

    private void handleReturn(ActionEvent actionEvent) {
        switchScene(actionEvent, returningPage,null);
    }

    private void handleMapSelection(double lat, double lng) {
        if (mapMode.equals("picker")) {
            this.selectedLat = lat;
            this.selectedLng = lng;
        }
    }

    private void displayExistingClinics() {
        clinics = DatabaseManager.getClinics(null);
        for (Clinic clinic : clinics) {
            engine.executeScript(String.format("addMarker(%f, %f, `%s`, '%s')",
                    clinic.getLatitude(), clinic.getLongitude(), clinic.getName(), clinic.getId()));
        }
    }

    @FXML
    private void handleSaveAndExit(ActionEvent event) {
        if (selectedLat != 0) {
            DatabaseManager.changeLocation(selectedLat, selectedLng);
            userSession.getInstance().setMyLat(selectedLat);
            userSession.getInstance().setMyLon(selectedLng);

            handleReturn(event);
        }
    }

    private void switchScene(ActionEvent event, String fxmlFile, Clinic clinic) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            if (!(clinic == null)){
                clinicInfoController nextController = loader.getController();
                nextController.setClinicData(clinic);
                nextController.setReturningPage("/clinicMap.fxml");
            }

            Scene scene = new Scene(root, 360, 640);
            ThemeManager.applyTheme(scene,null, null);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load FXML file: " + fxmlFile);
        }
    }
}
