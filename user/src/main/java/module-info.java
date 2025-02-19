module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens com.example.demo2 to javafx.fxml;

    exports com.example.demo2.controller;
    opens com.example.demo2.controller to javafx.fxml;
    exports com.example.demo2.models;
    opens com.example.demo2.models to javafx.fxml;
    exports com.example.demo2.test;
    opens com.example.demo2.test to javafx.fxml;
    exports com.example.demo2.utils;
    opens com.example.demo2.utils to javafx.fxml;
}