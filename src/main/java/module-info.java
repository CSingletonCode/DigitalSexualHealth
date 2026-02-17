module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires com.sun.jna;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires de.mkammerer.argon2.nolibs;

    opens com.myapp to javafx.fxml, com.fasterxml.jackson.databind;
    exports com.myapp;
}