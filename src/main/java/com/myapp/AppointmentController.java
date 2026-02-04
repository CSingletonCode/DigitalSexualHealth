package com.myapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppointmentController extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(AppointmentController.class.getResource("/appointment.fxml"));
        Parent root = fxmlLoader.load();

        // Create the scene (Width, Height)
        scene = new Scene(root, 335, 600);

        // Set the window title
        stage.setTitle("Appointment Page");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}