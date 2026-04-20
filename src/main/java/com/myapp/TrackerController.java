package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TrackerController{

    public SymptomsDatabase symptomsDatabase;

    @FXML
    private ScrollPane scroll;
    @FXML
    private VBox scrollbox;

    @FXML
    private Button returnButton;

    @FXML
    public void initialize() throws IOException{
        this.symptomsDatabase = new SymptomsDatabase();
        displaySymptoms();
    }

    @FXML
    private void addNew() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/newSymptom.fxml"));
        Parent newSymForm = fxmlLoader.load();
        Scene newForm = new Scene(newSymForm);
        ThemeManager.applyTheme(newForm,null, null);
        Stage popupStage = new Stage();
        popupStage.setScene(newForm);
        popupStage.initStyle(StageStyle.UNDECORATED);
        popupStage.initModality(Modality.APPLICATION_MODAL);

        PopupController controller = fxmlLoader.getController();
        controller.setStage(popupStage);
        controller.setSymptomsDatabase(this.symptomsDatabase);

        controller.setTrackerController(this);

        popupStage.show();
    }

    @FXML
    public void displaySymptoms() throws IOException{
        scrollbox.getChildren().clear();
        ArrayList<SymptomEntry> tracked = SymptomsDatabase.getAllSymptoms();
        String userID = String.valueOf(userSession.getInstance().getUserId());
        ArrayList<SymptomEntry> filteredList = new ArrayList<>();
        for (SymptomEntry symptomEntry : tracked) {
            if (symptomEntry.getUserID().equals(userID)) {
                filteredList.add(symptomEntry);
            }
        }
        Set<String> uniqueNames = new HashSet<>();
        for (SymptomEntry symptomEntry : filteredList){
            uniqueNames.add(symptomEntry.getName());
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SymptomBox.fxml"));
            Node symptomBox = fxmlLoader.load();
            BoxController boxController = fxmlLoader.getController();
            boxController.setTrackerController(this);
            boxController.setData(symptomEntry);
            scrollbox.getChildren().add(symptomBox);
        }
        for (String name: uniqueNames){
            int count = 0;
            SymptomEntry firstInstance = null;
            for (SymptomEntry symptomEntry : filteredList){
                if (symptomEntry.getName().equals(name)){
                    if (!symptomEntry.isChecked()) {
                        count++;
                        if (firstInstance == null){
                            firstInstance = symptomEntry;
                    }
                    }
                }
            }
            System.out.println("count: " + count);
            if (count > 2){
                firstInstance.setChecked(true);
                displayHelp(name);
                symptomsDatabase.setAllSymptoms(tracked);
            }
        }
    }

    private void displayHelp(String name) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Appointment Recommended");
        alert.setHeaderText("Symptom logged 3 times.");
        javafx.scene.control.ButtonType appointmentButton = new javafx.scene.control.ButtonType("Book an appointment here.");
        alert.getButtonTypes().setAll(appointmentButton, javafx.scene.control.ButtonType.OK);
        javafx.scene.control.DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        dialogPane.getStyleClass().add("root");

        alert.setContentText("You have logged the symptom "+ name +" 3 times. \n It is recommended to consult a physician.");
        java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == appointmentButton) {
            Stage stage = (Stage) returnButton.getScene().getWindow();
            goToStage(stage, "/clinicPage.fxml");
        }
    }

    @FXML
    public void goHome(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/homepage.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        ThemeManager.applyTheme(scene,null, null);
        stage.setScene(scene);
        stage.show();
    }

    private void goToStage(Stage stage, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            ThemeManager.applyTheme(scene, null);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Could not load FXML: " + fxmlFile);
            e.printStackTrace();
        }
    }
}