package com.myapp;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import java.sql.*;
import java.time.LocalDate;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;

public class Sprint1Test extends ApplicationTest {

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

        verifyThat("#passwordHiddenField", NodeMatchers.isVisible());
        clickOn("#passwordHiddenField");
        eraseText(50);
        write("password");

        verifyThat("#loginButton", NodeMatchers.isVisible());
        clickOn("#loginButton");

        // Wait for login navigation to finish
        sleep(100);
    }

    @Test
    @DisplayName("US2-WB-2.5")
    void testInvalidLogin() {
        // Enter invalid login details and click login
        verifyThat("#emailField", NodeMatchers.isVisible());
        clickOn("#emailField");
        eraseText(50);
        write("invalid@email.com");

        verifyThat("#passwordHiddenField", NodeMatchers.isVisible());
        clickOn("#passwordHiddenField");
        eraseText(50);
        write("password");

        verifyThat("#loginButton", NodeMatchers.isVisible());
        clickOn("#loginButton");

        verifyThat("Invalid email or password", NodeMatchers.isVisible());
    }

    @Test
    @DisplayName("US3-WB-3.3")
    void testSubmitAppointment() throws SQLException {
        loginAsTestUser();

        clickOn("#apptCard");
        clickOn("#bookAppointmentButton");
        clickOn("#bookBtn_1");

        // 1. Fill in the form fields using fx:id selectors
        DatePicker datePicker = lookup("#appointmentDatePicker").queryAs(DatePicker.class);
        interact(() -> datePicker.setValue(LocalDate.of(2026, 3, 20)));

        ComboBox<String> comboBox = lookup("#appointmentTimeBox").queryAs(ComboBox.class);
        interact(() -> comboBox.setValue("14:00"));

        doubleClickOn("#appointmentPurposeText");
        eraseText(100);
        write("Routine Checkup for Testing");

        // 2. Click the Submit button
        clickOn("#appointmentSubmitButton");

        sleep(5000);

        clickOn("#confirmButton");

        sleep(2000);

        // 3. Verify the UI handles the submission (Success popup check)
        verifyThat("Appointment booked successfully!", NodeMatchers.isVisible());
        clickOn("OK"); // Close the Alert dialog

        // 4. Database Verification
        try (Connection conn = DriverManager.getConnection(url)) {
            String query = "SELECT * FROM appointments WHERE purpose = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "Routine Checkup for Testing");

            ResultSet rs = pstmt.executeQuery();

            // Record found
            assertTrue(rs.next(), "Database should contain the submitted appointment.");
            assertEquals("Routine Checkup for Testing", rs.getString("purpose"), "Purpose in DB should match input.");
        }

        System.out.println("Test US3-WB-3.3 passed.");
    }

    @Test
    @DisplayName("US3-WB-3.4")
    void testSubmitInvalidAppointment() {
        loginAsTestUser();

        clickOn("#apptCard");
        clickOn("#bookAppointmentButton");
        clickOn("#bookBtn_1");

        // 2. Click the Submit button
        clickOn("#appointmentSubmitButton");

        sleep(100);

        // 3. Verify the UI handles the submission (Success popup check)
        verifyThat("Please ensure all fields are filled before submitting.", NodeMatchers.isVisible());
        clickOn("OK"); // Close the Alert dialog
    }

        @Test
        @DisplayName("US1-WB-1.2")
        void testSubmitSymptom() {
            loginAsTestUser();

            clickOn("#symptomsCard");
            clickOn("#addSymptomButton");
            sleep(1000);
            clickOn("#nameField");
            verifyThat("#optionsScroll", NodeMatchers.isVisible());
            clickOn("Test Symptom 1");
            clickOn("#dateField").write("10-02-2026");
            clickOn("#descriptionField").write("test");
            clickOn("#enterButton");

            System.out.println("Test US1-WB-1.2 passed.");
        }

    @Test
    @DisplayName("US4-WB-4.1")
    void testSymptomValidation() {
        loginAsTestUser();

        clickOn("#symptomsCard");
        clickOn("#addSymptomButton");
        clickOn("#enterButton");

        verifyThat("Error", NodeMatchers.isVisible());

        System.out.println("Test US4-WB-4.1 passed.");
    }

    @Test
    @DisplayName("US2-WB-2.3")
    void testUserDatabase() throws SQLException {

        verifyThat("#signupLink", NodeMatchers.isVisible());
        clickOn("#signupLink");

        // Fill signup form safely
        verifyThat("#emailField", NodeMatchers.isVisible());
        clickOn("#emailField");
        eraseText(50);
        Random rand = new Random();
        String randomString = String.valueOf(rand.nextInt(100000) + 1);
        write("test" + randomString + "@email.com");

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

        // Wait for page navigation
        sleep(100);

        verifyThat("#firstName", NodeMatchers.isVisible());
        clickOn("#firstName");
        eraseText(50);
        write("test");

        verifyThat("#lastName", NodeMatchers.isVisible());
        clickOn("#lastName");
        eraseText(50);
        write("test");

        DatePicker dobPicker = lookup("#dobBox").queryAs(DatePicker.class);
        interact(() -> dobPicker.setValue(LocalDate.of(2000, 8, 15)));

        ComboBox<String> genderBox = lookup("#genderBox").queryAs(ComboBox.class);
        interact(() -> genderBox.setValue("Man"));

        verifyThat("#continueButton", NodeMatchers.isVisible());
        clickOn("#continueButton");

        // Wait for next page
        sleep(100);

        verifyThat("#continueButton", NodeMatchers.isVisible());
        clickOn("#continueButton");

        // Wait for final page to complete
        sleep(100);

        // Verify user in DB
        try (Connection conn = DriverManager.getConnection(url)) {
            String query = "SELECT * FROM users WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "test" + randomString + "@email.com");

            ResultSet rs = pstmt.executeQuery();

            // Record found
            assertTrue(rs.next(), "Database should contain the submitted user.");
            assertEquals("test" + randomString + "@email.com", rs.getString("email"), "User not added to database.");
        }

        System.out.println("Test US2-WB-2.3 passed.");
    }
}
