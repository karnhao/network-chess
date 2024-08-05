module ku.cs {
    requires transitive javafx.controls;
    requires javafx.fxml;


    opens ku.cs.controllers to javafx.fxml;
    exports ku.cs.controllers;

    opens ku.cs.chessNetwork to javafx.fxml;
    exports ku.cs.chessNetwork;

    opens ku.cs.services to javafx.fxml;
    exports ku.cs.services;

    opens ku.cs.models to javafx.fxml;
    exports ku.cs.models;
}