package com.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.fxml.FXML;
import java.io.IOException;
import java.util.ArrayList;

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
        for (SymptomEntry symptomEntry : filteredList){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SymptomBox.fxml"));
            Node symptomBox = fxmlLoader.load();
            BoxController boxController = fxmlLoader.getController();
            boxController.setTrackerController(this);
            boxController.setData(symptomEntry);
            scrollbox.getChildren().add(symptomBox);
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
}