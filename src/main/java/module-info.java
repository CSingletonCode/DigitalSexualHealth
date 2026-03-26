module com.myapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires com.sun.jna;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires de.mkammerer.argon2.nolibs;

    requires com.google.gson;

    opens com.myapp to javafx.fxml, com.fasterxml.jackson.databind, com.google.gson;
    exports com.myapp;
}