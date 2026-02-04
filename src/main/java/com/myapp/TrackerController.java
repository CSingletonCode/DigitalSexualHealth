package com.myapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

public class TrackerController{

    @FXML
    private Button addSymptomButton;

    @FXML
    private Button enterSymptomButton;

    private static Scene scene;

    @FXML
    private void addNew() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(SymptomsPage.class.getResource("/newSymptom.fxml"));
        Parent newSymForm = fxmlLoader.load();
        scene = new Scene(newSymForm);

        Stage popupStage = new Stage();
        popupStage.setScene(scene);
        popupStage.initStyle(StageStyle.UNDECORATED);
        popupStage.show();
    }

    @FXML
    private void submit() {

    }
}