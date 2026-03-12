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
import java.io.FileWriter;
import java.util.stream.Collectors;

public class EvaluationController {
    @FXML private Label q1Label, q2Label, q3Label, nextLabel, pageIndicatorLabel;
    @FXML private Button nextButton;
    @FXML private ToggleGroup group1, group2, group3;

    private int currentPage = 0;
    private String[] userAnswers = new String[12];
    private final List<String> questions = Arrays.asList(
            "Have you had a sexual health check-up in the last year?", //Q1
            "Do you currently experience any unusual symptoms (pains, sores)?", //Q2
            "Have you been sexually active in the last 12 months?", //Q3
            "Are you and your partner using a reliable form of contraception, if needed?", //Q4
            "Have you had any new sexual partners in the past 3 months?", //Q5
            "Are you comfortable discussing sexual health issues with your partner?", //Q6
            "Are you aware of PReP as a way to prevent HIV?", //Q7
            "Have you ever been tested for an STI (Sexually Transmitted Infection)?", //Q8
            "Have you received recommended vaccinations for HPV and Hepatitis B?", //Q9
            "Do you feel as though you have enough information to make informed choices?", //10
            "Do you have a regular 'check up' routine for sexual health?", //Q11
            "Do you feel that your current healthcare provider is non-judgemental and inclusive?" //Q12
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

        if (pageIndicatorLabel != null) {
            pageIndicatorLabel.setText("Page " + (currentPage +1)+ " / 4");
        }

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

    private void evaluationCompletionAlert(int score) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Evaluation Complete");
        alert.setHeaderText("These are your results");

        javafx.scene.control.DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        dialogPane.getStyleClass().add("root");

        String levelRisk = checkRisk(score);

        String allergy;
        if (EvaluationData.selectedAllergies.isEmpty()) {
            allergy = "None recorded.";
        } else {
            allergy = String.join(", ", EvaluationData.selectedAllergies);
        }

        String msgOutput = String.format(
                "Risk Level: %s \n\nRecorded Allergies: \n%s",
                levelRisk, allergy
        );
        alert.setContentText(msgOutput);
        alert.showAndWait();

    }

    @FXML
    private void handleNextPage(ActionEvent event) {
        saveCurrentPageAnswers();

        if (currentPage < 3) {
            currentPage++;
            updatePageContent();
        } else {
            int userRiskScore = calculateRiskScore();
            EvaluationData.riskScore = userRiskScore;
            EvaluationData.saveDataToFile();
            evaluationCompletionAlert(userRiskScore);
            System.out.println("Data saved to JSON.");
            System.out.println("Evaluation Completed. Answers: " + Arrays.toString(userAnswers));
            System.out.println("Final Risk Score: " + userRiskScore);
            System.out.println("Risk level: " + checkRisk(userRiskScore));
            handleBackNavigation(event, "/homepage.fxml");
        }
    }

    private int calculateRiskScore() {
        int riskScore = 0;
        for (int i = 0; i < userAnswers.length; i++) {
            String answer = userAnswers[i];
            int  questionNum = i + 1;

            if (answer == null) continue;
            switch (questionNum) {
                //Questions where Yes = 1, Not sure = 2 and No = 3
                case 1: case 4: case 6: case 7: case 8: case 10: case 11: case 12:
                    if (answer.equals("Yes")) riskScore += 1;
                    else if (answer.equals("Not Sure")) riskScore += 2;
                    else if (answer.equals("No")) riskScore += 3;
                    break;

                    //Questions where Yes = 3, Not sure = 2 and No = 1
                case 2: case 3: case 5: case 9:
                    if (answer.equals("Yes")) riskScore += 3;
                    else if (answer.equals("Not Sure")) riskScore += 2;
                    else if (answer.equals("No")) riskScore += 1;
                    break;
            }
        }
        riskScore += (EvaluationData.selectedAllergies.size() * 4);
        return riskScore;
    }

    String checkRisk(int riskScore) {
        if (riskScore < 21) {
            return "Low risk";
        } else if (riskScore < 27) {
            return "Medium risk";
        } else{
            return "High risk";
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
    private void handleGoToAllergies(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/allergies.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            ThemeManager.applyTheme(scene,null, null);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load Allergies FXML file: ");
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
            ThemeManager.applyTheme(scene,null, null);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load FXML file: " + fxmlFile);
        }
    }
}