package com.myapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Login extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("/login.fxml"));
        Parent root = fxmlLoader.load();
        
        DatabaseManager.initialiseDatabase();

        // Create the scene (Width, Height)
        scene = new Scene(root, 335, 600);

        // Set the window title
        stage.setTitle("Digital Health System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}