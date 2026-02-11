module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    opens com.myapp to javafx.fxml, com.fasterxml.jackson.databind;
    exports com.myapp;
}