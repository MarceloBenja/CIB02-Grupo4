package edu.uni.demo;

import edu.uni.modelo.Docente;
import edu.uni.modelo.Estudiante;
import edu.uni.modelo.Persona;
import java.util.List;

/**
 * Programa de consola que muestra la herencia del modelo (guía 3).
 *
 * <p>No forma parte de la aplicación: se ejecuta aparte, en IntelliJ con clic derecho sobre esta
 * clase y Run. Los datos son los de ejemplo de db-backup/03_datos_ejemplo.sql.
 */
public final class MainHerencia {

    private MainHerencia() {
    }

    public static void main(String[] args) {
        Estudiante lucia =
                new Estudiante(1, "20261001A", "Lucía", "Huamán Torres", "lucia.huaman.t@uni.pe");
        Docente julia = new Docente(1, "Julia", "Ramos Ortiz", "jramos@uni.edu.pe");

        System.out.println("1. Comportamiento heredado de Persona (getNombreCompleto):");
        System.out.println("   " + lucia.getNombreCompleto());
        System.out.println("   " + julia.getNombreCompleto());

        System.out.println("2. Comportamiento propio de Estudiante (código y año de ingreso):");
        System.out.println("   Código " + lucia.getCodigo() + ", ingresó en " + lucia.getAnioIngreso());

        System.out.println("3. Polimorfismo: en una lista de Persona, cada objeto responde según su clase:");
        List<Persona> personas = List.of(lucia, julia);
        for (Persona persona : personas) {
            System.out.println("   " + persona);
        }

        System.out.println("4. Validación propia de Estudiante:");
        try {
            new Estudiante(2, "2026X01AB", "Prueba", "Código Inválido", "prueba@uni.pe");
        } catch (IllegalArgumentException e) {
            System.out.println("   Rechazado: " + e.getMessage());
        }
    }
}
