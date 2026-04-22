package com.myapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import javafx.scene.layout.VBox;

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
}
