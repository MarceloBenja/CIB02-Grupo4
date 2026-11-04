package edu.uni.controlador;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

/** Controla el menú de la ventana principal. */
public class PrincipalController {

    @FXML
    private void onSalir() {
        Platform.exit();
    }

    @FXML
    private void onAcercaDe() {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Acerca de");
        alerta.setHeaderText("Sistema de Matrícula Universitaria");
        alerta.setContentText("Ingeniería de Software (CIB02), Grupo 4. FIEE, UNI, ciclo 2026-2.");
        alerta.showAndWait();
    }
}
