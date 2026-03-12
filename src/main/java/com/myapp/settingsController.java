package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class settingsController {

    @FXML private Button returnButton;

    @FXML private ToggleButton highContrastToggle;

    @FXML private ToggleButton TextSizeToggle;

    @FXML
    public void initialize() {
        returnButton.setOnAction(this::handleReturn);

        highContrastToggle.setSelected(userSession.getInstance().isHighContrast());

        highContrastToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            userSession.getInstance().setHighContrast(newVal);
            try {
                sessionManager.setSession();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ThemeManager.applyTheme(highContrastToggle.getScene(),null, null);
        });

        TextSizeToggle.setSelected(userSession.getInstance().isLargeText());
        TextSizeToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            userSession.getInstance().setLargeText(newVal);
            try {
                sessionManager.setSession();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ThemeManager.applyTheme(TextSizeToggle.getScene(),null, null);

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
            ThemeManager.applyTheme(scene,null, null);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load FXML file: " + fxmlFile);
        }
    }
    @FXML
    private void addPIN(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pinPopup.fxml"));
        Parent newSymForm = fxmlLoader.load();
        Scene newForm = new Scene(newSymForm);
        ThemeManager.applyTheme(newForm,null, null);
        Stage popupStage = new Stage();
        popupStage.setScene(newForm);
        popupStage.initStyle(StageStyle.UNDECORATED);
        popupStage.initModality(Modality.APPLICATION_MODAL);

        PINPopupController controller = fxmlLoader.getController();
        controller.setStage(popupStage);

        popupStage.show();
    }
}
