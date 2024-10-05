module bank.client.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires lombok;
    requires com.google.gson;

    opens by.iba.bank to javafx.fxml, javafx.base;
    opens by.iba.bank.model.entity to com.google.gson, javafx.base;
    opens by.iba.bank.model.data to com.google.gson, javafx.base;
    opens by.iba.bank.model.messages to com.google.gson;
    exports  by.iba.bank;
    exports  by.iba.bank.controller;
    exports  by.iba.bank.model.entity;
    opens by.iba.bank.controller to javafx.fxml;
    exports by.iba.bank.component;
    opens by.iba.bank.component to javafx.fxml;
}