package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.io.IOException;

public class settingsController {

    @FXML private Button returnButton;

    @FXML private ToggleButton highContrastToggle;

    @FXML
    public void initialize() {
        returnButton.setOnAction(this::handleReturn);

        highContrastToggle.setSelected(userSession.getInstance().isHighContrast());

        highContrastToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            userSession.getInstance().setHighContrast(newVal);
            ThemeManager.applyTheme(highContrastToggle.getScene());
        });
    }
    private void handleReturn(ActionEvent event) {
        switchScene(event, "/homepage.fxml");
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
