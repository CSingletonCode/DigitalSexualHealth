package com.myapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;
import java.util.Objects;

public class BoxController {

    @FXML
    private Label nameLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label descriptionLabel;

    public void setData(SymptomEntry symptom) {
        if (!Objects.equals(symptom.getName(), "")) {
            nameLabel.setText(symptom.getName());
        } else {nameLabel.setText("No Name");}
        if (!Objects.equals(symptom.getDate(), "")) {
            dateLabel.setText(symptom.getDate());
        } else {dateLabel.setText("No Date");}
        if (!Objects.equals(symptom.getDescription(), "")) {
            descriptionLabel.setText(symptom.getDescription());
        } else {descriptionLabel.setText("No Description");}



    }
}
