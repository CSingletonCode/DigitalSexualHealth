package com.myapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

public class OptionController {
    @FXML
    private Label optionLabel;
    private PopupController popupController;

    public void setPopup(PopupController popupController) {
        this.popupController = popupController;
    }

    @FXML
    private void pickOption(){
        this.popupController.hideDropDown();
        this.popupController.setName(optionLabel.getText());
    }

    public void setOption(String option){
        optionLabel.setText(option);
    }
}
