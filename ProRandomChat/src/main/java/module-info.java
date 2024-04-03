module com.example.prorandomchata {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.prorandomchata.Controller to javafx.fxml;
//    opens com.example.prorandomchata.Model to javafx.fxml;
//    opens com.example.prorandomchata.View to javafx.fxml;
//    exports com.example.prorandomchata.Model;
    exports com.example.prorandomchata.Controller;
//    exports com.example.prorandomchata.View;
    exports com.example.prorandomchata;
    opens com.example.prorandomchata to javafx.fxml;
//    exports com.example.prorandomchata.Model;
//    opens com.example.prorandomchata.Model to javafx.fxml;
}