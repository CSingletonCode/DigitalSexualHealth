package com.myapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    boolean isHighContrast = false;
    boolean isLargeText = false;
    String page = "/Login.fxml";
    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file

        String[] current_session = sessionManager.getSession();
        if (current_session != null) {
            if (current_session[1].equals("true")) {
                page = "/LoginPIN.fxml";
            } else {
                page = "/LoginPassword.fxml";
            }
            // System.out.println(current_session[1]);
            if (current_session[3].equals("true")) {
                isHighContrast = true;
            }
            if (current_session[4].equals("true")) {
                isLargeText = true;
            }
        }

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(page));
        Parent root = fxmlLoader.load();

        DatabaseManager.initialiseDatabase();

        // Create the scene (Width, Height)
        Scene scene = new Scene(root, 335, 600);
        ThemeManager.applyTheme(scene,isHighContrast, isLargeText);

        // Set the window title
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}