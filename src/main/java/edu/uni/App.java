package edu.uni;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Aplicación JavaFX del Sistema de Matrícula Universitaria: abre la ventana principal. */
public class App extends Application {
    private static final String VISTA_PRINCIPAL = "/edu/uni/vista/Principal.fxml";
    private static final String TITULO = "Sistema de Matrícula Universitaria";
    private static final double ANCHO = 900;
    private static final double ALTO = 600;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader cargador = new FXMLLoader(App.class.getResource(VISTA_PRINCIPAL));
        stage.setScene(new Scene(cargador.load(), ANCHO, ALTO));
        stage.setTitle(TITULO);
        stage.show();
    }
}
