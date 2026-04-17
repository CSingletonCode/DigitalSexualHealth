package com.myapp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

public class AdviceController {
    @FXML private VBox adviceList;
    @FXML private ToggleGroup filterGroup;

    private List<HealthResource> resources = new ArrayList<>();

    public void initialize() {
        loadResources();
        displayResources("All");
    }

    public void loadResources() {
        String filePath = "localdata/adviceResources.json";
        java.io.File jsonFile = new java.io.File(filePath);

        if (!jsonFile.exists()) {
            System.err.println("JSON NOT FOUND AT: " + jsonFile.getAbsolutePath());
            return;
        }

        try (Reader reader = new java.io.FileReader(jsonFile)) {
            resources = new Gson().fromJson(reader, new TypeToken<List<HealthResource>>() {}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleFilter(ActionEvent event) {
        ToggleButton selected = (ToggleButton) filterGroup.getSelectedToggle();
        if (selected != null) {
            displayResources(selected.getText());
        }
    }
    @FXML
     private void displayResources(String category) {
        adviceList.getChildren().clear();
        for (HealthResource res : resources) {
            if (category.equals("All") || res.category.equals(category)) {
                adviceList.getChildren().add(createAdviceCard(res));
            }
        }
     }

     private VBox createAdviceCard(HealthResource res) {
        VBox card = new VBox(10);
        card.getStyleClass().add("evaluation-card");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(res.title);
        title.getStyleClass().add("evaluation-sub-header");

        header.getChildren().addAll(title);

        Label source = new Label(res.source);
        source.setWrapText(true);
        source.getStyleClass().add("text-silent");

        Label urlLabel = new Label("Website: " + res.url);
        urlLabel.getStyleClass().add("text-silent");
        urlLabel.setWrapText(true);

        card.getChildren().addAll(header, source, urlLabel);
        return card;
     }


     @FXML
     private void handleBackToHome(ActionEvent event) {
        handleBackNavigation(event, "/homepage.fxml");
    }

    private void handleBackNavigation(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            ThemeManager.applyTheme(scene, null, null);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load FXML file: " + fxmlFile);
        }
    }
}


