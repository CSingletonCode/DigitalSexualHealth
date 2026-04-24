package com.myapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;

import java.time.LocalDate;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;

public class Sprint3Test extends ApplicationTest {
    private static final Logger log = LoggerFactory.getLogger(Sprint3Test.class);
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
    @DisplayName("US12-WB-12.1")
    void testAdviceLoad() {
        loginAsTestUser("example@email.com");
        verifyThat("#adviceCard", NodeMatchers.isVisible());
        clickOn("#adviceCard");
        sleep(500);
        verifyThat(".header-text", LabeledMatchers.hasText("Advice"));

        VBox adviceList = lookup("#adviceList").queryAs(VBox.class);
        assertEquals(9, adviceList.getChildren().size(), "All 9 resources should be loaded");
    }

    @Test
    @DisplayName("US12-WB-12.2")
    void adviceContentTest() {
        loginAsTestUser("example@email.com");
        clickOn("#adviceCard");
        sleep(400);

        verifyThat("NHS Sexual Health", NodeMatchers.isVisible());
        verifyThat("World Health Organisation", NodeMatchers.isVisible());
        verifyThat("Website: https://www.bpas.org/", NodeMatchers.isVisible());
    }

    @Test
    @DisplayName("US12-WB-12.3")
    void testBackToHome() {
        loginAsTestUser("example@email.com");
        clickOn("#adviceCard");
        sleep(400);

        verifyThat("#returnButton", NodeMatchers.isVisible());
        clickOn("#returnButton");
        sleep(400);

        verifyThat("#adviceCard", NodeMatchers.isVisible());
    }

    @Test
    @DisplayName("US13-WB-13.1")
    void adviceFilterTest() {
        loginAsTestUser("example@email.com");
        clickOn("#adviceCard");
        sleep(400);

        clickOn("LGBTQ+");
        sleep(400);
        VBox adviceList = lookup("#adviceList").queryAs(VBox.class);
        assertEquals(3, adviceList.getChildren().size(), "LGBTQ+ filter shows 3 items");
        verifyThat("LGBT Hero", NodeMatchers.isVisible());

        clickOn("Youth");
        sleep(400);
        assertEquals(3, adviceList.getChildren().size(), "Youth filter shows 3 items");
        verifyThat("Brook", NodeMatchers.isVisible());

        clickOn("All");
        sleep(400);
        assertEquals(9, adviceList.getChildren().size(), "All filter shows 9 items");
    }

    @Test
    @DisplayName("US16-WB-16.1")
    void testNotificationLoad() {
        loginAsTestUser("example@email.com");

        verifyThat("#notificationBell", NodeMatchers.isVisible());
        clickOn("#notificationBell");
        sleep(500);

        verifyThat("Notifications", LabeledMatchers.hasText("Notifications"));
    }

    @Test
    @DisplayName("US16-WB-16.2")
    void testDisabledNotifications() {
        loginAsTestUser("example@email.com");
        clickOn("#settings");
        sleep(400);

        clickOn("#notificationToggle");
        clickOn("#returnButton");
        sleep(400);
        clickOn("#notificationBell");
        sleep(400);

        verifyThat("Notifications are turned off", LabeledMatchers.hasText("Notifications are turned off"));
    }

    @Test
    @DisplayName("US16-WB-16.3")
    void notificationAfterBooking() {
        loginAsTestUser("example@email.com");
        TutorialController.getInstance().stop();
        clickOn("#apptCard");
        clickOn("#bookAppointmentButton");
        clickOn("#bookBtn_1");

        DatePicker datePicker = lookup("#appointmentDatePicker").queryAs(DatePicker.class);
        interact(() -> datePicker.setValue(LocalDate.of(2027, 8, 8)));

        ComboBox<String> timeBox = lookup("#appointmentTimeBox").queryAs(ComboBox.class);
        interact(() -> {
            timeBox.show();
            timeBox.getSelectionModel().clearSelection();
            timeBox.getSelectionModel().select("09:45");
        });
        sleep(500);
        doubleClickOn("#appointmentPurposeText");
        eraseText(100);

        write("Routine Checkup for Testing");
        clickOn("#appointmentSubmitButton");

        sleep(5000);
        clickOn("#confirmButton");

        sleep(400);
        clickOn("OK");

        sleep(800);
        clickOn("#returnButton");
        clickOn("#notificationBell");
        sleep(400);
        VBox notifContainer = lookup("#notificationsContainer").queryAs(VBox.class);

        assertFalse(notifContainer.getChildren().isEmpty(), "Container should have a new appointment");
        Node firstChild = notifContainer.getChildren().get(0);
        assertTrue(firstChild instanceof VBox, "First child should be a notification card");
        assertTrue(firstChild.getStyleClass().contains("unread"), "New notification should be unread");
    }

    @Test
    @DisplayName("US16-WB-16.4")
    void notificationsReturnButtonTest() {
        loginAsTestUser("example@email.com");
        clickOn("#notificationBell");
        sleep(400);

        verifyThat("#returnButton", NodeMatchers.isVisible());
        clickOn("#returnButton");
        sleep(400);
        verifyThat("#notificationBell", NodeMatchers.isVisible());
    }

    @Test
    @DisplayName("US16-WB-16.5")
    void testNotificationBadge() {
        loginAsTestUser("example@email.com");
        sleep(500);
        assertNotNull(lookup("#notificationBadge").query(), "Badge should exist");
        verifyThat("#notificationBadge", LabeledMatchers.hasText("0"));
    }

    /* Partition & Boundary Testing based on D6 Feedback for evaluation form */
    private int calculateRiskScore(String[] ans, int allergyCount) {
        int score = 0;
        for (int i = 0; i < ans.length; i++) {
            int questionNum = i + 1;
            String answer = ans[i];
            if (answer == null) continue;

            if (Arrays.asList(1, 4, 6, 7, 8, 9, 10, 11, 12).contains(questionNum)) {
                if (answer.equals("Yes")) score += 1;
                else if (answer.equals("Not Sure")) score += 2;
                else if (answer.equals("No")) score += 3;
            } else if (Arrays.asList(2, 3, 5).contains(questionNum)) {
                if (answer.equals("Yes")) score += 3;
                else if (answer.equals("Not Sure")) score += 2;
                else if (answer.equals("No")) score += 1;
            }
        }
        return score + (allergyCount * 4);
    }

    @Test
    @DisplayName("US8-WB-8.7")
    void testLowPartition() {
        String[] answers = {"Yes", "Not Sure", "Not Sure", "Yes", "Not Sure", "Yes", "Yes", "Yes", "Yes", "Yes", "Yes", "Yes"};
        int score = calculateRiskScore(answers, 0);
        assertTrue(score >= 12 && score <= 20, "Score should be in Low range");
    }

    @Test
    @DisplayName("US8-WB-8.8")
    void testMediumPartition() {
        String[] answers = {"Yes", "No", "No", "Yes", "No", "No", "Not Sure", "Not Sure", "Not Sure", "No", "No", "No"};
        int score = calculateRiskScore(answers, 0);
        assertTrue(score >= 21 && score <= 26, "Score should be in Medium range");
    }

    @Test
    @DisplayName("US8-WB-8.9")
    void testHighPartition() {
        String[] answers = {"Yes", "Yes", "Yes", "No", "Yes", "No", "No", "No", "No", "No", "No", "No"};
        int score = calculateRiskScore(answers, 3);
        assertTrue(score >= 27, "Score should be in High range");
    }

    @Test
    @DisplayName("US8-WB-8.10")
    void testLowToMedium() {
        String[] low = {"Yes", "Not Sure", "Not Sure", "Yes", "Not Sure", "Yes", "Yes", "No", "Yes", "Not Sure", "No", "Yes"};
        String[] med = {"Yes", "Not Sure", "Not Sure", "Yes", "Not Sure", "Yes", "Yes", "No", "Yes", "Not Sure", "No", "Not Sure"};
        assertEquals(20, calculateRiskScore(low, 0));
        assertEquals(21, calculateRiskScore(med, 0));
    }

    @Test
    @DisplayName("US8-WB-8.11")
    void testMediumToHigh() {
        String[] med = {"No", "No", "No", "Not Sure", "No", "No", "Not Sure", "Not Sure", "Not Sure", "No", "No", "No"};
        String[] high = {"No", "No", "Not Sure", "Not Sure", "No", "No", "Not Sure", "Not Sure", "Not Sure", "No", "No", "No"};
        assertEquals(26, calculateRiskScore(med, 0));
        assertEquals(27, calculateRiskScore(high, 0));
    }

    @Test
    @DisplayName("US15-WB-15.1")
    void testingEvaluation() {
        loginAsTestUser("example@email.com");

        clickOn("#evaluationCard");
        verifyThat("#pageIndicatorLabel", LabeledMatchers.hasText("Page 1 / 4"));
        clickOn("#q1No");
        clickOn("#q2Yes");
        clickOn("#q3Yes");
        clickOn("#nextButton");
        sleep(400);
        verifyThat("#pageIndicatorLabel", LabeledMatchers.hasText("Page 2 / 4"));
        clickOn("#q1No");
        clickOn("#q2Yes");
        clickOn("#q3No");
        clickOn("#nextButton");
        sleep(400);
        verifyThat("#pageIndicatorLabel", LabeledMatchers.hasText("Page 3 / 4"));
        clickOn("#q1No");
        clickOn("#q2No");
        clickOn("#q3No");
        clickOn("#nextButton");
        sleep(400);
        clickOn("#q1No");
        clickOn("#q2No");
        clickOn("#q3No");

        verifyThat("#pageIndicatorLabel", LabeledMatchers.hasText("Page 4 / 4"));
        clickOn("#nextButton");
        sleep(400);
        clickOn("Book an appointment here.");
        sleep(400);
    }

    @Test
    @DisplayName("US15-WB-15.2")
    void testSubmitSymptom() {
        loginAsTestUser("example@email.com");

        clickOn("#symptomsCard");
        clickOn("#addSymptomButton");
        sleep(1000);
        clickOn("#nameField");
        verifyThat("#optionsScroll", NodeMatchers.isVisible());
        clickOn("Test Symptom 1");
        DatePicker datePicker = lookup("#dateField").queryAs(DatePicker.class);
        interact(() -> {
            datePicker.setValue(LocalDate.of(2026, 4, 10));
        });
        clickOn("#descriptionField").write("test");
        clickOn("#enterButton");
        sleep(400);
        clickOn("#addSymptomButton");
        sleep(1000);
        clickOn("#nameField");
        verifyThat("#optionsScroll", NodeMatchers.isVisible());
        clickOn("Test Symptom 1");
        DatePicker datePicker2 = lookup("#dateField").queryAs(DatePicker.class);
        interact(() -> {
            datePicker2.setValue(LocalDate.of(2026, 4, 10));
        });
        clickOn("#descriptionField").write("test");
        clickOn("#enterButton");
        sleep(400);
        clickOn("#addSymptomButton");
        sleep(1000);
        clickOn("#nameField");
        verifyThat("#optionsScroll", NodeMatchers.isVisible());
        clickOn("Test Symptom 1");
        DatePicker datePicker3 = lookup("#dateField").queryAs(DatePicker.class);
        interact(() -> {
            datePicker3.setValue(LocalDate.of(2026, 4, 10));
        });
        clickOn("#descriptionField").write("test");
        clickOn("#enterButton");
        sleep(400);
        clickOn("Book an appointment here.");
        sleep(400);
        System.out.println("Test US15-WB-15.2 passed.");
    }

    @Test
    @DisplayName("US15.3-WB-15.3")
    void testClinicCost() {
        loginAsTestUser("example@email.com");
        clickOn("#apptCard");
        clickOn("#bookAppointmentButton");
        clickOn("#infoBtn_1");
        sleep(400);
        verifyThat("#clinicCost", NodeMatchers.isVisible());
        sleep(1000);
        System.out.println("Test US15-WB-15.3 passed.");
    }

    @Test
    @DisplayName("US5.3-WB-3.1")
    void testClinicMap() {
        loginAsTestUser("example@email.com");
        clickOn("#apptCard");
        clickOn("#bookAppointmentButton");
        clickOn("#showMap");
        sleep(2000);
        WebView webView = lookup("#webView").queryAs(WebView.class);
        WebEngine engine = webView.getEngine();
        int dbCount = DatabaseManager.getClinics(null).size();

        Callable<Integer> query = () -> {
            Object result = engine.executeScript("markers.length");
            return (result instanceof Number) ? ((Number) result).intValue() : 0;
        };

        CompletableFuture<Integer> futureCount = new CompletableFuture<>();
        Platform.runLater(() -> {
            try {
                Object result = engine.executeScript("markers.length");
                int count = (result instanceof Number) ? ((Number) result).intValue() : 0;
                futureCount.complete(count);
            } catch (Exception e) {
                futureCount.completeExceptionally(e);
            }
        });

        int mapCount = futureCount.join();
        assertEquals(dbCount, mapCount);
    }
    @Test
    @DisplayName("US5.3-WB-3.2")
    void testLocationChange() throws SQLException {
        loginAsTestUser("example@email.com");
        clickOn("#settings");
        clickOn("#ChangePos");
        sleep(2000);
        doubleClickOn("#webView");

        WebView webView = lookup("#webView").queryAs(WebView.class);
        interact(() -> {
            String jsScript =
                    "if (window.javaConnector) {" +
                            "    window.javaConnector.onMapClick(50.123, -1.456);" +
                            "} else {" +
                            "    console.error('Java Connector not found!');" +
                            "}";

            webView.getEngine().executeScript(jsScript);
        });
        sleep(500);
        clickOn("#saveButton");

        assertEquals(50.123, userSession.getInstance().getMyLat(), 0.001);
        assertEquals(-1.456, userSession.getInstance().getMyLon(), 0.001);
    }

    @Test
    @DisplayName("US7-WB-7.1")
    void testFontWorksOnAndOff(){
        loginAsTestUser("example@email.com");
        clickOn("#settings");
        Label node = lookup("#SizeLabel").query();
        double size = node.getFont().getSize();
        assertEquals(12, size);
        verifyThat("#TextSizeToggle", NodeMatchers.isVisible());
        clickOn("#TextSizeToggle");
        size = node.getFont().getSize();
        assertEquals(16, size);
        clickOn("#TextSizeToggle");
        size = node.getFont().getSize();
        assertEquals(12, size);
        System.out.println("Test US7-WB-7.1 passed.");
    }

    @Test
    @DisplayName("US7-WB-7.2")
    void testFontStaysOn() throws TimeoutException {
        loginAsTestUser("example@email.com");
        clickOn("#settings");
        clickOn("#TextSizeToggle");
        Label node = lookup("#SizeLabel").query();
        double size = node.getFont().getSize();
        assertEquals(16, size);
        verifyThat("#returnButton", NodeMatchers.isVisible());
        clickOn("#returnButton");
        FxToolkit.cleanupStages();
        sleep(400);
        // The app is reopened
        FxToolkit.setupApplication(App.class);
        sleep(400);
        clickOn("#pinHiddenField");
        eraseText(50);
        write("1234");
        clickOn("#loginButton");
        clickOn("#settings");
        node = lookup("#SizeLabel").query();
        size = node.getFont().getSize();
        assertEquals(16, size);
        clickOn("#TextSizeToggle");
        System.out.println("Test US7-WB-7.2 passed.");
    }

    @Test
    @DisplayName("US7-WB-7.3")
    void testFontOtherPage(){
        loginAsTestUser("example@email.com");
        clickOn("#settings");
        clickOn("#TextSizeToggle");
        Label node = lookup("#SizeLabel").query();
        double size = node.getFont().getSize();
        assertEquals(16, size);
        clickOn("#returnButton");
        Label homenode = lookup("#symptom_log_label").query();
        double homesize = homenode.getFont().getSize();
        assertEquals(16, homesize);
        clickOn("#settings");
        clickOn("#TextSizeToggle");
    }
}
