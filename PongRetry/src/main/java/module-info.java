module com.example.pongretry {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens com.example.pongretry to javafx.fxml;
    exports com.example.pongretry;
    exports com.example.pongretry.view;
    opens com.example.pongretry.view to javafx.fxml;
    exports com.example.pongretry.controllers;
    opens com.example.pongretry.controllers to javafx.fxml;
    exports com.example.pongretry.model;
    opens com.example.pongretry.model to javafx.fxml;
    exports com.example.pongretry.DAO;
    opens com.example.pongretry.DAO to javafx.fxml;
}