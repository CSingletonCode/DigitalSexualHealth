package com.myapp;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;


public class Sprint2Test extends ApplicationTest{
    private Stage primaryStage;
    private String url = "jdbc:sqlite:app_database.db";

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        // Load the specific page directly
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @BeforeEach
    void resetToLogin() {
        // Reset UI back to login.fxml before every test
        interact(() -> {
            try {
                Parent freshLoginRoot = FXMLLoader.load(getClass().getResource("/login.fxml"));
                primaryStage.getScene().setRoot(freshLoginRoot);
                primaryStage.show();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Small wait to ensure UI is fully ready before the test starts
        sleep(100);
    }

    private void loginAsTestUser() {
        // Enter valid login details and click login
        verifyThat("#emailField", NodeMatchers.isVisible());
        clickOn("#emailField");
        eraseText(50);
        write("example@email.com");

        verifyThat("#passwordField", NodeMatchers.isVisible());
        clickOn("#passwordField");
        eraseText(50);
        write("password");

        verifyThat("#stayLoggedIn", NodeMatchers.isVisible());
        clickOn("#stayLoggedIn");

        verifyThat("#loginButton", NodeMatchers.isVisible());
        clickOn("#loginButton");

        // Wait for login navigation to finish
        sleep(100);
    }

    @Test
    @DisplayName("US6-WB-1.1")
    void testHashing() {
        Random rand = new Random();
        clickOn("#signupLink");
        verifyThat("#emailField", NodeMatchers.isVisible());
        clickOn("#emailField");
        eraseText(50);
        String randomString = String.valueOf(rand.nextInt(100000) + 1);
        write("example" + randomString + "@email.com");

        verifyThat("#passwordHiddenField", NodeMatchers.isVisible());
        clickOn("#passwordHiddenField");
        eraseText(50);
        write("password");

        verifyThat("#passwordHiddenField1", NodeMatchers.isVisible());
        clickOn("#passwordHiddenField1");
        eraseText(50);
        write("password");

        verifyThat("#continueButton", NodeMatchers.isVisible());
        clickOn("#continueButton");

        sleep(1000);

        verifyThat("#firstName", NodeMatchers.isVisible());
        clickOn("#firstName");
        eraseText(50);
        write("firstName");

        verifyThat("#lastName", NodeMatchers.isVisible());
        clickOn("#lastName");
        eraseText(50);
        write("lastName");

        DatePicker datePicker = lookup("#dobBox").queryAs(DatePicker.class);
        interact(() -> datePicker.setValue(LocalDate.of(2000, 3, 20)));

        verifyThat("#genderBox", NodeMatchers.isVisible());
        clickOn("#genderBox");
        clickOn("Man");

        verifyThat("#continueButton", NodeMatchers.isVisible());
        clickOn("#continueButton");

        sleep(2000);

        clickOn("#continueButton");
        clickOn("OK");

        try (Connection conn = DriverManager.getConnection(url)){
            String query = "SELECT password from users WHERE email = ?";
            PreparedStatement psmt = conn.prepareStatement(query);
            psmt.setString(1,"example" + randomString + "@email.com");

            ResultSet rs = psmt.executeQuery();

            while (rs.next()) {
                String password = rs.getString("password");
                System.out.println(password);
                assertNotEquals("password", password);
            }

        System.out.println("Test US6-WB-1.1 passed.");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}


