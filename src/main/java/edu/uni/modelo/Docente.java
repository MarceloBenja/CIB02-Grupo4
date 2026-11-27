package edu.uni.modelo;

/** Docente que dicta las secciones. Hereda de Persona todos sus datos. */
public class Docente extends Persona {

    public Docente(int id, String nombres, String apellidos, String correo) {
        super(id, nombres, apellidos, correo);
    }

    @Override
    public String getRol() {
        return "Docente";
    }
}
