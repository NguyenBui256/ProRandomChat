module com.example.prorandomchata {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.prorandomchata to javafx.fxml;
    exports com.example.prorandomchata;
}