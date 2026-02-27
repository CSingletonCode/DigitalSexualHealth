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
import java.time.LocalDate;
import java.time.Period;

public class Signup2Controller {

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private DatePicker dobBox;
    @FXML
    private ComboBox<String> genderBox;

    private User currentUser;

    @FXML
    public void initialize() {
        // This adds items when the screen loads
        genderBox.getItems().addAll("Woman",
                "Man",
                "Non-binary",
                "Transgender Woman",
                "Transgender Man",
                "Intersex",
                "Prefer not to say",
                "Another identity");
    }

    @FXML
    void handleCreateAccount(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }
        switchScene(event, "/TermsAndConditions.fxml");
    }

    public void setUserData(User currentUser) {
        this.currentUser = currentUser;
    }

    private Boolean validateInputs(){
        boolean isValid = true;
        StringBuilder Errors = new StringBuilder();

        String firstN = firstName.getText().trim();
        String lastN = lastName.getText().trim();
        if (firstN.isEmpty()){
            setErrorStyle(firstName);
            Errors.append("First name cannot be empty.\n");
            isValid = false;
        } else {
            clearErrorStyle(firstName);
        }
        if (lastN.isEmpty()){
            setErrorStyle(lastName);
            Errors.append("Last name cannot be empty.\n");
            isValid = false;
        } else {
            clearErrorStyle(lastName);
        }

        if (dobBox.getValue() == null){
            setErrorStyle(dobBox);
            Errors.append("Date of Birth cannot be empty.\n");
            isValid = false;
        } else {
            if (Period.between(dobBox.getValue(), LocalDate.now()).getYears() < 16){
                setErrorStyle(dobBox);
                Errors.append("You must be at least 16 years old.\n");
                isValid = false;
            } else if (dobBox.getValue().isAfter(LocalDate.now())){
                setErrorStyle(dobBox);
                Errors.append("Date of Birth cannot be in the future.\n");
                isValid = false;
            } else {
                clearErrorStyle(dobBox);
            }
        }

        if (genderBox.getValue() == null){
            setErrorStyle(genderBox);
            Errors.append("Please select a gender identity.\n");
            isValid = false;
        } else {
            clearErrorStyle(genderBox);
        }

        if (!isValid){
            showAlert(Errors.toString());
        }

        return isValid;
    }

    private void setErrorStyle(Control node) {
        node.setStyle("-fx-border-color: #ff4444; -fx-border-width: 2px; -fx-border-radius: 5px;");
    }

    private void clearErrorStyle(Control node) {
        node.setStyle(null);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Registration Error");
        alert.setHeaderText("Please Rectify the Following:");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void back(ActionEvent event) {
        switchScene(event, "/signup.fxml");
    }

    // Reuse the helper method
    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            if (fxmlFile.equals("/TermsAndConditions.fxml")) {
                TermsAndConditionsController nextController = loader.getController();
                currentUser.setFirstName(firstName.getText());
                currentUser.setLastName(lastName.getText());
                currentUser.setDob(dobBox.getValue().toString());
                currentUser.setGender(genderBox.getValue());
                nextController.setUserData(currentUser);
            } else if (fxmlFile.equals("/signup.fxml")){
                SignupController nextController = loader.getController();
                nextController.setText(currentUser.getEmail(), currentUser.getPassword());
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}