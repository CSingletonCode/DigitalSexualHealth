module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.myapp to javafx.fxml;
    exports com.myapp;
}