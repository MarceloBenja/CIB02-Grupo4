package edu.uni.modelo;

/** Roles de usuario del sistema, los mismos de la Semana 2 y de la restricción CK_Usuario_rol. */
public enum Rol {
    ESTUDIANTE("Estudiante"),
    DOCENTE("Docente"),
    REGISTROS("Responsable de registros académicos"),
    ADMINISTRADOR("Administrador del sistema");

    private final String descripcion;

    Rol(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Indica si el rol mantiene la oferta académica (CU-07): cursos, secciones y horarios.
     *
     * @return true para el responsable de registros y el administrador
     */
    public boolean puedeGestionarOferta() {
        return this == REGISTROS || this == ADMINISTRADOR;
    }
}
