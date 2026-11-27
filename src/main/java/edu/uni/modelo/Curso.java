package edu.uni.modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Curso de la oferta académica.
 *
 * <p>Usa propiedades JavaFX para que la tabla de la vista se actualice sola cuando cambia un
 * valor, sin refrescarla a mano.
 */
public class Curso {
    private final IntegerProperty idCurso = new SimpleIntegerProperty();
    private final StringProperty codigo = new SimpleStringProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final IntegerProperty creditos = new SimpleIntegerProperty();
    private final IntegerProperty ciclo = new SimpleIntegerProperty();

    public Curso(int idCurso, String codigo, String nombre, int creditos, int ciclo) {
        this.idCurso.set(idCurso);
        this.codigo.set(codigo);
        this.nombre.set(nombre);
        this.creditos.set(creditos);
        this.ciclo.set(ciclo);
    }

    public int getIdCurso() {
        return idCurso.get();
    }

    public String getCodigo() {
        return codigo.get();
    }

    public String getNombre() {
        return nombre.get();
    }

    public int getCreditos() {
        return creditos.get();
    }

    public int getCiclo() {
        return ciclo.get();
    }

    public StringProperty codigoProperty() {
        return codigo;
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public IntegerProperty creditosProperty() {
        return creditos;
    }

    public IntegerProperty cicloProperty() {
        return ciclo;
    }
}
