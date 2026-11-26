package edu.uni;

import edu.uni.controlador.Navegacion;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

/** Aplicación JavaFX del Sistema de Matrícula Universitaria: empieza por el inicio de sesión. */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Navegacion.mostrarLogin(stage);
        stage.show();
    }
}
