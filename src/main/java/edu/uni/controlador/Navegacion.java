package edu.uni.controlador;

import edu.uni.modelo.Usuario;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Cambia entre la pantalla de inicio de sesión y la ventana principal, en la misma ventana. */
public final class Navegacion {
    private static final String VISTA_LOGIN = "/edu/uni/vista/Login.fxml";
    private static final String VISTA_PRINCIPAL = "/edu/uni/vista/Principal.fxml";
    private static final String TITULO = "Sistema de Matrícula Universitaria";
    private static final double ANCHO_LOGIN = 420;
    private static final double ALTO_LOGIN = 340;
    private static final double ANCHO_PRINCIPAL = 900;
    private static final double ALTO_PRINCIPAL = 600;

    private Navegacion() {
    }

    /**
     * Muestra la pantalla de inicio de sesión.
     *
     * @param ventana ventana de la aplicación
     * @throws IOException si no se puede cargar la vista
     */
    public static void mostrarLogin(Stage ventana) throws IOException {
        Parent raiz = FXMLLoader.load(Navegacion.class.getResource(VISTA_LOGIN));
        ventana.setScene(new Scene(raiz, ANCHO_LOGIN, ALTO_LOGIN));
        ventana.setTitle(TITULO + ": iniciar sesión");
        ventana.centerOnScreen();
    }

    /**
     * Muestra la ventana principal para el usuario que acaba de iniciar sesión.
     *
     * @param ventana ventana de la aplicación
     * @param usuario usuario autenticado
     * @throws IOException si no se puede cargar la vista
     */
    public static void mostrarPrincipal(Stage ventana, Usuario usuario) throws IOException {
        FXMLLoader cargador = new FXMLLoader(Navegacion.class.getResource(VISTA_PRINCIPAL));
        Parent raiz = cargador.load();
        PrincipalController principal = cargador.getController();
        principal.iniciarSesion(usuario);
        ventana.setScene(new Scene(raiz, ANCHO_PRINCIPAL, ALTO_PRINCIPAL));
        ventana.setTitle(TITULO);
        ventana.centerOnScreen();
    }
}
