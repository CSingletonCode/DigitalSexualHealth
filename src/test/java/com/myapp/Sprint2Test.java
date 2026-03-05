package com.myapp;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.util.Random;
import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;

import java.sql.*;
import java.time.LocalDate;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;


public class Sprint2Test extends ApplicationTest{
    private static final Logger log = LoggerFactory.getLogger(Sprint2Test.class);
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

    private void loginAsTestUser(String email) {
        // Enter valid login details and click login
        verifyThat("#emailField", NodeMatchers.isVisible());
        clickOn("#emailField");
        eraseText(50);
        write(email);

        verifyThat("#passwordHiddenField", NodeMatchers.isVisible());
        clickOn("#passwordHiddenField");
        eraseText(50);
        write("password");

        verifyThat("#stayLoggedIn", NodeMatchers.isVisible());
        clickOn("#stayLoggedIn");

        verifyThat("#loginButton", NodeMatchers.isVisible());
        clickOn("#loginButton");

        // Wait for login navigation to finish
        sleep(100);
    }

    private void loginStayLoggedOut() {
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

    private void setPIN(String firstPIN, String secondPIN) {
        verifyThat("#settings", NodeMatchers.isVisible());
        clickOn("#settings");
        verifyThat("#AddPINButton", NodeMatchers.isVisible());
        clickOn("#AddPINButton");
        verifyThat("#firstPINField", NodeMatchers.isVisible());
        verifyThat("#secondPINField", NodeMatchers.isVisible());
        clickOn("#firstPINField");
        eraseText(50);
        write(firstPIN);
        clickOn("#secondPINField");
        eraseText(50);
        write(secondPIN);
        verifyThat("#confirmButton", NodeMatchers.isVisible());
        clickOn("#confirmButton");
    }

    @Test
    @DisplayName("US6-WB-6.1")
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

        System.out.println("Test US6-WB-6.1 passed.");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    @DisplayName("US9-WB-9.1")
    void testApppointmentConfirmation(){
        loginAsTestUser("example@email.com");

        clickOn("#apptCard");
        clickOn("#bookAppointmentButton");
        clickOn("#bookBtn_1");

        // 1. Fill in the form fields using fx:id selectors
        DatePicker datePicker = lookup("#appointmentDatePicker").queryAs(DatePicker.class);
        interact(() -> datePicker.setValue(LocalDate.of(2028, 3, 20)));

        //ComboBox<String> comboBox = lookup("#appointmentTimeBox").queryAs(ComboBox.class);
        //interact(() -> comboBox.setValue("14:00"));
        clickOn("#appointmentTimeBox");
        clickOn("14:00");

        doubleClickOn("#appointmentPurposeText");
        eraseText(100);
        write("Routine Checkup for Testing");

        // 2. Click the Submit button
        clickOn("#appointmentSubmitButton");

        sleep(5000);

        clickOn("#confirmButton");

        sleep(2000);

        System.out.println("Test US9-WB-9.1 passed.");
    }

    @Test
    @DisplayName("US18-WB-18.1")
    void testAppointmentHelp(){
        loginAsTestUser("example@email.com");
        clickOn("#apptCard");
        clickOn("#helpButton");
        clickOn("#tutorialNextButton");
        sleep(1000);
        clickOn("#tutorialNextButton");
        sleep(1000);
        clickOn("#tutorialNextButton");
        sleep(1000);
        clickOn("#tutorialNextButton");
        sleep(2000);

        System.out.println("Test US18-WB-18.1 passed.");
    }

    @Test
    @DisplayName("US17-WB-17.1")
    void testAllergyInputAndPersistence() {
        loginAsTestUser("example@email.com");

        //Navigate to Evaluation
        clickOn("#evaluationCard");
        clickOn("#allergiesButton");
        verifyThat("#allergyList", NodeMatchers.isVisible());
        //Select an Allergy then press Cancel
        clickOn("Latex");
        clickOn("←");
        sleep(500);

        //Return to allergies to verify it didn't save
        clickOn("#allergiesButton");
        sleep(500);

        long checkCount = lookup(".checkbox").queryAll().stream()
                .map(n -> (CheckBox) n)
                .filter(CheckBox::isSelected)
                .count();
        assertEquals(0, checkCount, "Incorrectly saved during a cancel event");

        //Validation testing - persistence
        clickOn("Latex");
        clickOn("Penicillin");
        clickOn("#saveAllergies");
        sleep(500);

        //Verify update
        assertTrue(EvaluationData.selectedAllergies.contains("Latex"));
        assertTrue(EvaluationData.selectedAllergies.contains("Penicillin"));

        //Check JSON file is in correct place
        File newFile = new File("localdata/allergies.json");
        assertTrue(newFile.exists(), "JSON file should be created.");
    }

    @Test
    @DisplayName("US17-WB-17.2")
    void testAllergyReloads() {
        interact (() -> {
            EvaluationData.selectedAllergies.clear();
            EvaluationData.selectedAllergies.add("Lidocaine");
        });

        loginAsTestUser("example@email.com");
        clickOn("#evaluationCard");
        sleep(400);
        clickOn("#allergiesButton");

        org.testfx.util.WaitForAsyncUtils.waitForFxEvents();
        interact(() -> {
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 3000) {
                try {
                    CheckBox checkbox = lookup("Lidocaine").queryAs(CheckBox.class);
                    if (checkbox.isSelected()) return;
                } catch (Exception e) {
                    sleep(100);
                }
            }
        });
        CheckBox penicillinBox = lookup("Lidocaine").queryAs(CheckBox.class);
        assertTrue(penicillinBox.isSelected(), "Failed to load allergy data.");
        clickOn("#saveAllergies");
        sleep(400);
    }

    @Test
    @DisplayName("US17-WB-17.3")
    void testEmptyAllergySave() {
        loginAsTestUser("example@email.com");
        clickOn("#evaluationCard");
        clickOn("#allergiesButton");
        interact(() -> EvaluationData.selectedAllergies.clear());
        clickOn("#saveAllergies");
        verifyThat("#allergiesButton", NodeMatchers.isVisible());
    }

    @Test
    @DisplayName("US8-WB-8.1")
    void testRiskValueCalculation() {
        //Partition test of zero selections
        interact(() -> EvaluationData.selectedAllergies.clear());
        int scoreZero = EvaluationData.selectedAllergies.size() * 4;
        assertEquals(0, scoreZero);

        //Moderate Selections
        interact(() -> {
            EvaluationData.selectedAllergies.add("Latex");
            EvaluationData.selectedAllergies.add("Penicillin");
        });
        int scoreEight = EvaluationData.selectedAllergies.size() * 4;
        assertEquals(8, scoreEight, "Score should be +4 points per allergy");

        //High risk identification boundary
        interact(() -> {
            EvaluationData.selectedAllergies.add("Lidocaine");
            EvaluationData.selectedAllergies.add("Fragrances");
            EvaluationData.selectedAllergies.add("Nickel");
            EvaluationData.selectedAllergies.add("Ibuprofen");
            EvaluationData.selectedAllergies.add("Adhesive tape");
        });

        int finalScore = EvaluationData.selectedAllergies.size() * 4;
        assertTrue(finalScore >= 27, "Final score should cross high risk boundary");
    }
    @Test
    @DisplayName("US8-WB-8.2")
    void testingEvaluationBackFunctionality() {
        loginAsTestUser("example@email.com");
        clickOn("#evaluationCard");
        verifyThat("#q1Yes", NodeMatchers.isVisible());
        clickOn("#q1Yes");

        verifyThat("#q2No", NodeMatchers.isVisible());
        clickOn("#q2No");

        verifyThat("#q3Maybe", NodeMatchers.isVisible());
        clickOn("#q3Maybe");

        verifyThat("#pageIndicatorLabel", LabeledMatchers.hasText("Page 1 / 4"));
        clickOn("#nextButton");
        verifyThat("#pageIndicatorLabel", LabeledMatchers.hasText("Page 2 / 4"));
        clickOn("<");
        ToggleButton q1YesButton = lookup("#q1Yes").queryAs(ToggleButton.class);
        assertTrue(q1YesButton.isSelected(), "Selection was lost when navigating");
    }

    @Test
    @DisplayName("US8-WB-8.3")
    void testingEvaluation() {
        loginAsTestUser("example@email.com");

        clickOn("#evaluationCard");
        for (int i=1; i <= 4; i++) {
            String pageNo = "Page " + i + " / 4";
            verifyThat("#pageIndicatorLabel", LabeledMatchers.hasText(pageNo));
            clickOn("#q1Yes");
            clickOn("#q2No");
            clickOn("#q3Maybe");

            if (i < 4) {
                clickOn("#nextButton");
                sleep(400);
            }
        }
        verifyThat("#pageIndicatorLabel", LabeledMatchers.hasText("Page 4 / 4"));
        clickOn("#nextButton");
        sleep(400);
        clickOn("OK");
        sleep(400);
        verifyThat("#evaluationCard", NodeMatchers.isVisible());
    }

    @Test
    @DisplayName("US8-WB-8.4")
    void testRiskBoundaries() {
        EvaluationController newCase = new EvaluationController();
        assertEquals("Low risk", newCase.checkRisk(20));
        assertEquals("Medium risk", newCase.checkRisk(21));
        assertEquals("Medium risk", newCase.checkRisk(26));
        assertEquals("High risk", newCase.checkRisk(27));
    }

    @Test
    @DisplayName("US8-WB-8.5")
    void testRiskCategories() {
        EvaluationController newCase = new EvaluationController();
        assertEquals("Low risk", newCase.checkRisk(10));
        assertEquals("Medium risk", newCase.checkRisk(24));
        assertEquals("High risk", newCase.checkRisk(40));
    }

    @Test
    @DisplayName("US8-WB-8.6")
    void negativeScore() {
        EvaluationController newCase = new EvaluationController();
        assertEquals("Low risk", newCase.checkRisk(-5));
    }

    @Test
    @DisplayName("US11-WB-11.1")
    // User selects stay logged in, does not set a PIN.
    void testStayLoggedIn() throws TimeoutException {
        // The user logs in, selecting stay logged in
        loginAsTestUser("examplePW@email.com");
        sleep(400);
        // The app closes
        FxToolkit.cleanupStages();
        sleep(400);
        // The app is reopened
        FxToolkit.setupApplication(App.class);
        sleep(400);
        if (!lookup("pinHiddenField").queryAll().isEmpty()) {
            clickOn("#swichToPassword");
        }
        sleep(400);
        // Check that the correct returning user page is selected
        verifyThat("#passwordHiddenField", NodeMatchers.isVisible());
        clickOn("#passwordHiddenField");
        eraseText(50);
        // Logs in from this page
        write("password");
        verifyThat("#loginButton", NodeMatchers.isVisible());
        clickOn("#loginButton");
        // Check the home page correctly opens
        verifyThat("#symptomsCard", NodeMatchers.isVisible());
        System.out.println("US11-WB-11.1 - Pass");
    }

    @Test
    @DisplayName("US11-WB-11.2")// User selects stay logged in, and sets a valid PIN.
    void testStayLoggedInWithPIN() throws TimeoutException {
        // The user logs in
        loginAsTestUser("example@email.com");
        // The user sets a new PIN
        setPIN("1234","1234");
        verifyThat("#returnButton", NodeMatchers.isVisible());
        clickOn("#returnButton");
        sleep(400);
        // The app closes
        FxToolkit.cleanupStages();
        sleep(400);
        // The app is reopened
        FxToolkit.setupApplication(App.class);
        sleep(400);
        verifyThat("#pinHiddenField", NodeMatchers.isVisible());
        clickOn("#pinHiddenField");
        eraseText(50);
        write("1234");
        verifyThat("#loginButton", NodeMatchers.isVisible());
        clickOn("#loginButton");
        // Check the home page correctly opens
        verifyThat("#symptomsCard", NodeMatchers.isVisible());
        System.out.println("US11-WB-11.2 - Pass");
    }
}








