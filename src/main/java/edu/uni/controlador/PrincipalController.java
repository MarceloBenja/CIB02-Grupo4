package edu.uni.controlador;

import edu.uni.modelo.Usuario;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/** Controla el menú de la ventana principal. */
public class PrincipalController {
    private static final System.Logger LOG = System.getLogger(PrincipalController.class.getName());
    private static final String VISTA_CURSOS = "/edu/uni/vista/Curso.fxml";

    @FXML private BorderPane panelPrincipal;
    @FXML private Label lblSesion;
    @FXML private MenuItem itemCursos;
    @FXML private MenuItem itemSecciones;

    /**
     * Prepara la ventana para el usuario que inició sesión. Solo el responsable de registros y el
     * administrador pueden mantener la oferta académica; para los demás esas opciones quedan
     * deshabilitadas (HU-01: cada usuario ve solo lo que le corresponde).
     *
     * @param usuario usuario autenticado
     */
    public void iniciarSesion(Usuario usuario) {
        lblSesion.setText("Sesión iniciada: " + usuario.getUsuario() + " ("
                + usuario.getRol().getDescripcion() + ")");
        boolean gestionaOferta = usuario.getRol().puedeGestionarOferta();
        itemCursos.setDisable(!gestionaOferta);
        itemSecciones.setDisable(!gestionaOferta);
    }

    @FXML
    private void onCerrarSesion() {
        try {
            Navegacion.mostrarLogin((Stage) panelPrincipal.getScene().getWindow());
        } catch (IOException e) {
            LOG.log(System.Logger.Level.ERROR, "No se pudo volver al inicio de sesión", e);
            new Alert(Alert.AlertType.ERROR, "No se pudo cerrar la sesión.").showAndWait();
        }
    }

    @FXML
    private void onSalir() {
        Platform.exit();
    }

    /** Opciones de módulos que se construyen en los próximos sprints. */
    @FXML
    private void onEnConstruccion(ActionEvent evento) {
        String opcion = ((MenuItem) evento.getSource()).getText();
        new Alert(Alert.AlertType.INFORMATION,
                "La opción \"" + opcion + "\" estará disponible en un próximo sprint.").showAndWait();
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
