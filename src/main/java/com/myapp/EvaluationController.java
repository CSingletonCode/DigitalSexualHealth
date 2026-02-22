package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class EvaluationController {
    @FXML private Label q1Label, q2Label, q3Label, nextLabel;
    @FXML private Button nextButton;
    @FXML private ToggleGroup group1, group2, group3;

    private int currentPage = 0;
    private String[] userAnswers = new String[12];
    private final List<String> questions = Arrays.asList(
            "Question 1?",
            "Question 2?",
            "Question 3?",
            "Question 4?",
            "Question 5?",
            "Question 6?",
            "Question 7?",
            "Question 8?",
            "Question 9?",
            "Question 10?",
            "Question 11?",
            "Question 12?"
    );

    public void initialize() {
        nextButton.setDisable(true);

        addSelectionListener(group1);
        addSelectionListener(group2);
        addSelectionListener(group3);

        updatePageContent();
    }

    private void addSelectionListener(ToggleGroup group) {
        group.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            validateSelections();
        });
    }

    private void validateSelections() {
        boolean allSelected = (group1.getSelectedToggle() != null) &&
                              (group2.getSelectedToggle() != null) &&
                              (group3.getSelectedToggle() != null);
        nextButton.setDisable(!allSelected);
    }

    private void updatePageContent() {
        int startIndex = currentPage * 3;

        q1Label.setText(questions.get(startIndex));
        q2Label.setText(questions.get(startIndex + 1));
        q3Label.setText(questions.get(startIndex + 2));

        restoreToggleSelection(group1, userAnswers[startIndex]);
        restoreToggleSelection(group2, userAnswers[startIndex + 1]);
        restoreToggleSelection(group3, userAnswers[startIndex + 2]);

        validateSelections();

        if (currentPage == 3) {
            nextLabel.setText("Finish");
            nextButton.setText(">");
            nextButton.setStyle("-fx-background-color: #9444E5;");
        } else {
            nextButton.setText("Next");
            nextButton.setText(">");
            nextButton.setStyle("");
        }
    }

    private void restoreToggleSelection(ToggleGroup group, String savedAnswer) {
        if (savedAnswer == null) {
            group.selectToggle(null);
            return;
        }

        for (Toggle toggle : group.getToggles()) {
            ToggleButton button = (ToggleButton) toggle;
            if (button.getText().equals(savedAnswer)) {
                group.selectToggle(toggle);
                break;
            }
        }
    }

    @FXML
    private void handleNextPage(ActionEvent event) {
        saveCurrentPageAnswers();

        if (currentPage < 3) {
            currentPage++;
            updatePageContent();
        } else {
            System.out.println("Evaluation Completed. Answers: " + Arrays.toString(userAnswers));
            handleBackNavigation(event, "/homepage.fxml");
        }
    }

    @FXML
    private void handlePrevPage() {
        if (currentPage > 0) {
            saveCurrentPageAnswers();
            currentPage--;
            updatePageContent();
        }
    }

    private void saveCurrentPageAnswers() {
        int startIndex = currentPage * 3;

        if (group1.getSelectedToggle() != null)
            userAnswers[startIndex] = ((ToggleButton)group1.getSelectedToggle()).getText();
        if (group2.getSelectedToggle() != null)
            userAnswers[startIndex + 1] = ((ToggleButton)group2.getSelectedToggle()).getText();
        if (group3.getSelectedToggle() != null)
            userAnswers[startIndex + 2] = ((ToggleButton)group3.getSelectedToggle()).getText();
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
            ThemeManager.applyTheme(scene);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load FXML file: " + fxmlFile);
        }
    }
}
