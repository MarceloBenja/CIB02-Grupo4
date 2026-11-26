package edu.uni.modelo;

import java.util.regex.Pattern;

/** Estudiante que se matricula. Agrega a Persona su código UNI. */
public class Estudiante extends Persona {
    /** Ocho dígitos y una letra mayúscula, igual que la restricción CK_Estudiante_codigo. */
    private static final Pattern FORMATO_CODIGO = Pattern.compile("\\d{8}[A-Z]");
    /** Los cuatro primeros dígitos del código son el año de ingreso. */
    private static final int DIGITOS_ANIO = 4;

    private final String codigo;

    /**
     * Crea un estudiante.
     *
     * @param id identificador en la base de datos
     * @param codigo código UNI, por ejemplo 20261001A
     * @param nombres nombres del estudiante
     * @param apellidos apellidos del estudiante
     * @param correo correo institucional
     * @throws IllegalArgumentException si el código no tiene el formato UNI
     */
    public Estudiante(int id, String codigo, String nombres, String apellidos, String correo) {
        super(id, nombres, apellidos, correo);
        if (codigo == null || !FORMATO_CODIGO.matcher(codigo).matches()) {
            throw new IllegalArgumentException(
                    "El código " + codigo + " no tiene el formato UNI (8 dígitos y una letra).");
        }
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    /**
     * Devuelve el año en que el estudiante ingresó a la universidad.
     *
     * @return los cuatro primeros dígitos del código; para 20261001A, 2026
     */
    public int getAnioIngreso() {
        return Integer.parseInt(codigo.substring(0, DIGITOS_ANIO));
    }

    @Override
    public String getRol() {
        return "Estudiante";
    }

    @Override
    public String toString() {
        return super.toString() + ", código " + codigo;
    }
}
