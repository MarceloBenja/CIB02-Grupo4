package edu.uni.controlador;

import edu.uni.modelo.Usuario;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/** Controla el menú de la ventana principal. */
public class PrincipalController {
    private static final System.Logger LOG = System.getLogger(PrincipalController.class.getName());
    private static final String VISTA_CURSOS = "/edu/uni/vista/Curso.fxml";

    @FXML private BorderPane panelPrincipal;
    @FXML private Label lblSesion;

    /**
     * Prepara la ventana para el usuario que inició sesión.
     *
     * @param usuario usuario autenticado
     */
    public void iniciarSesion(Usuario usuario) {
        lblSesion.setText("Sesión iniciada: " + usuario.getUsuario() + " ("
                + usuario.getRol().getDescripcion() + ")");
    }

    @FXML
    private void onSalir() {
        Platform.exit();
    }

    /** Muestra el mantenimiento de cursos en el centro de la ventana principal. */
    @FXML
    private void onCursos() {
        try {
            Parent vista = FXMLLoader.load(PrincipalController.class.getResource(VISTA_CURSOS));
            panelPrincipal.setCenter(vista);
        } catch (IOException e) {
            LOG.log(System.Logger.Level.ERROR, "No se pudo abrir la vista de cursos", e);
            new Alert(Alert.AlertType.ERROR, "No se pudo abrir la pantalla de cursos.").showAndWait();
        }
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
