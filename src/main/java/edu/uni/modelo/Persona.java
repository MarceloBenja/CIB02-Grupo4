package edu.uni.modelo;

/**
 * Datos comunes de las personas registradas en el sistema: estudiantes y docentes.
 *
 * <p>Es abstracta porque en el sistema nadie es solo una persona: siempre es un estudiante o un
 * docente, y cada subclase indica su rol.
 */
public abstract class Persona {
    private final int id;
    private final String nombres;
    private final String apellidos;
    private final String correo;

    protected Persona(int id, String nombres, String apellidos, String correo) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
    }

    public int getId() {
        return id;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    /**
     * Devuelve el nombre en el formato de las listas del sistema.
     *
     * @return apellidos y nombres separados por una coma, por ejemplo "Ramos Ortiz, Julia"
     */
    public String getNombreCompleto() {
        return apellidos + ", " + nombres;
    }

    /**
     * Indica qué papel cumple la persona en el sistema.
     *
     * @return el rol, por ejemplo "Estudiante" o "Docente"
     */
    public abstract String getRol();

    @Override
    public String toString() {
        return getRol() + ": " + getNombreCompleto() + " <" + correo + ">";
    }
}
