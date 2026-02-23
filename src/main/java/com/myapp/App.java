package com.myapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    String page = "/Login.fxml";

    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file

        String[] current_session = sessionManager.getSession();
        if (current_session != null) {
            if (current_session[1].equals("true")) {
                page = "/PINLoginController.fxml";
            } else {
                page = "/WordLoginController.fxml";
            }
        }

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(page));
        Parent root = fxmlLoader.load();

        DatabaseManager.initialiseDatabase();

        // Create the scene (Width, Height)
        scene = new Scene(root, 335, 600);

        // Set the window title
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}