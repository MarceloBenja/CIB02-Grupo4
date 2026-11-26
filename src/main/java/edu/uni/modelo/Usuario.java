package edu.uni.modelo;

/** Usuario que inició sesión. No guarda la contraseña: solo quién es y qué rol tiene. */
public class Usuario {
    private final int idUsuario;
    private final String usuario;
    private final Rol rol;

    public Usuario(int idUsuario, String usuario, Rol rol) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.rol = rol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public Rol getRol() {
        return rol;
    }
}
