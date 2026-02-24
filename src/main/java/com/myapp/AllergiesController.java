package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.CheckBox;

public class AllergiesController {

    @FXML private VBox allergyList;

    @FXML
    public void initialize() {
        for (Node node : allergyList.getChildren()) {
            if (node instanceof CheckBox checkbox) {
                if (EvaluationData.selectedAllergies.contains(checkbox.getText())) {
                    checkbox.setSelected(true);
                }

                checkbox.selectedProperty().addListener((obs, wasSelected, nowSelected) -> {
                    if (nowSelected) {
                        EvaluationData.selectedAllergies.add(checkbox.getText());
                    } else {
                        EvaluationData.selectedAllergies.remove(checkbox.getText());
                    }
                });
            }
        }
    }

    @FXML
    private void handleBacktoEvaluation(ActionEvent event) {
        EvaluationData.saveDataToFile();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evaluation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            ThemeManager.applyTheme(scene);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load Evaluation FXML file");
        }
    }
}
