package edu.uni.controlador;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

/** Muestra los mensajes de la aplicación en cuadros de diálogo con el texto completo. */
final class Avisos {

    private Avisos() {
    }

    /**
     * Muestra un aviso y espera la respuesta. Deja que el cuadro crezca en alto: si no, en Windows
     * los mensajes de más de una línea se cortan con puntos suspensivos.
     *
     * @param alerta cuadro de diálogo ya preparado
     * @return el botón que pulsó el usuario
     */
    static Optional<ButtonType> mostrar(Alert alerta) {
        alerta.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        return alerta.showAndWait();
    }

    /**
     * Muestra un mensaje con un solo botón para cerrarlo.
     *
     * @param tipo tipo de mensaje (información, advertencia o error)
     * @param texto mensaje para el usuario
     */
    static void mostrar(Alert.AlertType tipo, String texto) {
        mostrar(new Alert(tipo, texto));
    }
}
