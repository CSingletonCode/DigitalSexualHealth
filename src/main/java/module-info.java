module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.myapp to javafx.fxml;
    exports com.myapp;
}