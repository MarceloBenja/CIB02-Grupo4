package edu.uni.datos;

import edu.uni.modelo.Rol;
import edu.uni.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/** Acceso a la tabla Usuario para el inicio de sesión (HU-01). */
public class UsuarioDAO {
    /**
     * Sal de relleno para cuando el usuario no existe. Se deriva igual la contraseña para que la
     * respuesta tarde lo mismo y no delate qué usuarios existen.
     */
    private static final byte[] SAL_RELLENO = new byte[ClaveSegura.BYTES_SAL];

    /**
     * Comprueba el usuario y la contraseña contra la base de datos.
     *
     * @param usuario nombre de usuario escrito en el formulario
     * @param clave contraseña escrita en el formulario
     * @return el usuario si las credenciales son correctas y la cuenta está activa; vacío si no
     * @throws SQLException si falla la conexión o la consulta
     */
    public Optional<Usuario> autenticar(String usuario, char[] clave) throws SQLException {
        String sql = "SELECT idUsuario, usuario, rol, sal, claveHash, iteraciones "
                + "FROM Usuario WHERE usuario = ? AND activo = 1";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    // El resultado no importa: solo se iguala el tiempo de respuesta.
                    ClaveSegura.derivar(clave, SAL_RELLENO, ClaveSegura.ITERACIONES);
                    return Optional.empty();
                }
                if (!ClaveSegura.coincide(clave, rs.getBytes("sal"), rs.getInt("iteraciones"),
                        rs.getBytes("claveHash"))) {
                    return Optional.empty();
                }
                return Optional.of(new Usuario(rs.getInt("idUsuario"), rs.getString("usuario"),
                        Rol.valueOf(rs.getString("rol"))));
            }
        }
    }
}
