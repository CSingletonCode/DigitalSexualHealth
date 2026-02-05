package com.myapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

public class TrackerController{

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

        popupStage.show();
    }
}