package com.myapp;

import javafx.collections.ObservableList;
import javafx.scene.Scene;

public class ThemeManager {
    public static void applyTheme(Scene scene, Boolean highContrast) {
        ObservableList<String> sheets = scene.getStylesheets();
        sheets.clear();

        sheets.add(ThemeManager.class.getResource("/style.css").toExternalForm());
        if (highContrast == null) {
            if (userSession.getInstance().isHighContrast()) {
                sheets.add(ThemeManager.class.getResource("/high-contrast.css").toExternalForm());
                scene.setFill(javafx.scene.paint.Color.BLACK);
            } else {
                scene.setFill(javafx.scene.paint.Color.WHITE);
            }
        } else if (highContrast) {
            sheets.add(ThemeManager.class.getResource("/high-contrast.css").toExternalForm());
            scene.setFill(javafx.scene.paint.Color.BLACK);
        } else {
            scene.setFill(javafx.scene.paint.Color.WHITE);
        }

    }
}
