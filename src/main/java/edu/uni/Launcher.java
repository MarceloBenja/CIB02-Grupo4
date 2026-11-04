package edu.uni;

import javafx.application.Application;

/** Punto de entrada que no hereda de Application; permite ejecutar el .jar fuera de IntelliJ. */
public final class Launcher {
    private Launcher() {
    }

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}
