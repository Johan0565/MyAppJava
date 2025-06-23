module myappjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires transitive javafx.base;

    opens myappjava.Controllers to javafx.fxml;
    exports myappjava.Controllers;
    exports myappjava;
}