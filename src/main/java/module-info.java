module edu.uni.matricula {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;

    opens edu.uni.controlador to javafx.fxml;
    exports edu.uni;
}
