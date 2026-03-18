package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdviceController {
    @FXML private VBox adviceListContainer;
    @FXML private ToggleGroup filterGroup;
    @FXML private Label infoLabel;

    private final List<AdviceResource> resources = new ArrayList<>();
    public void initialize() {
        resources.add(new AdviceResource("Sexual Health FAQ", "General guidance on symptoms, testing, and common concerns,", "All"));
        resources.add(new AdviceResource("LGBTQ+ support", "Resources for inclusive healthcare and community support groups.", "LGBTQ+"));
        resources.add(new AdviceResource("Youth Services", "Confidential help and clinics specifically for those under 25.", "Youth"));
        resources.add(new AdviceResource("PrEP Information", "A guide to Pre-Exposure Prophylaxis for HIV prevention", "LGBTQ+"));
        resources.add(new AdviceResource("Consent & Relationships", "Simple and informative guide on communication and safety.", "All"));

        renderAdvice("All");

        filterGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                renderAdvice(((ToggleButton) newVal).getText());
            }
        });
    }

     private void renderAdvice(String category) {
        adviceListContainer.getChildren().clear();
        for (AdviceResource res : resources) {
            if (category.equals("All") || res.getCategory().equals(category)) {
                adviceListContainer.getChildren().add(createAdviceCard(res));
            }
        }

        if (infoLabel != null) {
            infoLabel.setText("Showing: " + category);
        }
     }

     private VBox createAdviceCard(AdviceResource res) {
        VBox card = new VBox(10);
        card.getStyleClass().add("evaluation-card");

        Label title = new Label(res.getTitle());
        title.getStyleClass().add("evaluation-sub-header");

        Label description = new Label(res.getDescription());
        description.setWrapText(true);
        description.getStyleClass().add("text-silent");

        card.getChildren().addAll(title, description);
        return card;
     }

     @FXML
     private void handleFilter(ActionEvent event) {
        ToggleButton selected = (ToggleButton) filterGroup.getSelectedToggle();
        if (selected != null) {
            renderAdvice(selected.getText());
        }
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
            ThemeManager.applyTheme(scene, null);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load FXML file: " + fxmlFile);
        }
    }

    private static class AdviceResource {
        private final String title, description, category;
        public AdviceResource(String title, String description, String category) {
            this.title = title;
            this.description = description;
            this.category = category;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getCategory() { return category; }

    }
}


