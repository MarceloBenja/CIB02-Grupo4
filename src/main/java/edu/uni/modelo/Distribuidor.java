package edu.uni.modelo;

import javafx.beans.property.*;

public class Distribuidor {
    private final IntegerProperty id;
    private final StringProperty nombre;
    private final StringProperty ruc;

    public Distribuidor(int id, String nombre, String ruc) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.ruc = new SimpleStringProperty(ruc);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public void setId(int id) { this.id.set(id); }

    public String getNombre() { return nombre.get(); }
    public StringProperty nombreProperty() { return nombre; }
    public void setNombre(String nombre) { this.nombre.set(nombre); }

    public String getRuc() { return ruc.get(); }
    public StringProperty rucProperty() { return ruc; }
    public void setRuc(String ruc) { this.ruc.set(ruc); }
}