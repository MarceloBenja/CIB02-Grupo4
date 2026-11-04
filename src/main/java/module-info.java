module edu.uni.matricula {
    requires javafx.controls;
    requires javafx.fxml;

    opens edu.uni.controlador to javafx.fxml;
    exports edu.uni;
}
