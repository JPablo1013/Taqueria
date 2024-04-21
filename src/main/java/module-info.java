module com.example.proyectotaqueria {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.proyectotaqueria to javafx.fxml;
    exports com.example.proyectotaqueria;

    requires java.sql;
    requires mysql.connector.j;

    requires java.desktop;
    requires kernel;
    requires io;
    requires layout;
    requires org.jfree.jfreechart;
    requires javafx.swing;

    opens  com.example.proyectotaqueria.modelos;
}