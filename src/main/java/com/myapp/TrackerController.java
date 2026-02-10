package com.myapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class TrackerController{

    public SymptomsDatabase symptomsDatabase;

    @FXML
    private ScrollPane scroll;
    @FXML
    private VBox scrollbox;

    @FXML
    public void initialize() throws IOException{
        this.symptomsDatabase = new SymptomsDatabase();
        displaySymptoms();
    }

    @FXML
    private void addNew() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(SymptomsPage.class.getResource("/newSymptom.fxml"));
        Parent newSymForm = fxmlLoader.load();
        Scene newForm = new Scene(newSymForm);

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
        ArrayList<SymptomEntry> tracked = this.symptomsDatabase.getAllSymptoms();
        for (SymptomEntry symptomEntry : tracked){
            FXMLLoader fxmlLoader = new FXMLLoader(SymptomsPage.class.getResource("/SymptomBox.fxml"));
            Node symptomBox = fxmlLoader.load();
            BoxController boxController = fxmlLoader.getController();
            boxController.setTrackerController(this);
            boxController.setData(symptomEntry);
            scrollbox.getChildren().add(symptomBox);
        }
    }

    @FXML
    public void goHome() throws IOException{}
}