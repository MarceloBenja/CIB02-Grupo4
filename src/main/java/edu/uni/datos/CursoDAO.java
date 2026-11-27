package edu.uni.datos;

import edu.uni.modelo.Curso;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Acceso a la tabla Curso. Todas las sentencias usan parámetros, nunca texto concatenado. */
public class CursoDAO {
    /** Código de error de SQL Server al violar una restricción PRIMARY KEY o UNIQUE. */
    private static final int ERROR_CLAVE_DUPLICADA = 2627;
    /** Código de error de SQL Server al insertar una fila repetida en un índice único. */
    private static final int ERROR_INDICE_DUPLICADO = 2601;

    /**
     * Devuelve todos los cursos ordenados por ciclo y código.
     *
     * @return lista observable para la tabla de la vista
     * @throws SQLException si falla la conexión o la consulta
     */
    public ObservableList<Curso> listar() throws SQLException {
        ObservableList<Curso> lista = FXCollections.observableArrayList();
        String sql = "SELECT idCurso, codigo, nombre, creditos, ciclo FROM Curso ORDER BY ciclo, codigo";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Curso(rs.getInt("idCurso"), rs.getString("codigo"),
                        rs.getString("nombre"), rs.getInt("creditos"), rs.getInt("ciclo")));
            }
        }
        return lista;
    }

    /**
     * Registra un curso nuevo.
     *
     * @param curso datos del curso; su idCurso se ignora porque lo genera la base de datos
     * @throws SQLException si el código ya existe o falla la conexión
     */
    public void insertar(Curso curso) throws SQLException {
        String sql = "INSERT INTO Curso (codigo, nombre, creditos, ciclo) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, curso.getCodigo());
            ps.setString(2, curso.getNombre());
            ps.setInt(3, curso.getCreditos());
            ps.setInt(4, curso.getCiclo());
            ps.executeUpdate();
        }
    }

    /**
     * Modifica los datos de un curso existente, ubicado por su idCurso.
     *
     * @param curso datos nuevos del curso
     * @throws SQLException si el curso ya no existe, el código se repite o falla la conexión
     */
    public void actualizar(Curso curso) throws SQLException {
        String sql = "UPDATE Curso SET codigo = ?, nombre = ?, creditos = ?, ciclo = ? WHERE idCurso = ?";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, curso.getCodigo());
            ps.setString(2, curso.getNombre());
            ps.setInt(3, curso.getCreditos());
            ps.setInt(4, curso.getCiclo());
            ps.setInt(5, curso.getIdCurso());
            if (ps.executeUpdate() == 0) {
                throw new SQLException("No existe el curso con idCurso " + curso.getIdCurso());
            }
        }
    }

    /**
     * Elimina un curso.
     *
     * @param idCurso identificador del curso
     * @throws SQLException si el curso ya no existe o falla la conexión
     */
    public void eliminar(int idCurso) throws SQLException {
        String sql = "DELETE FROM Curso WHERE idCurso = ?";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCurso);
            if (ps.executeUpdate() == 0) {
                throw new SQLException("No existe el curso con idCurso " + idCurso);
            }
        }
    }

    /**
     * Indica si el error se debe a un código de curso repetido.
     *
     * @param e excepción lanzada por insertar o actualizar
     * @return true si SQL Server rechazó la fila por duplicar un valor único
     */
    public static boolean esCodigoDuplicado(SQLException e) {
        return e.getErrorCode() == ERROR_CLAVE_DUPLICADA || e.getErrorCode() == ERROR_INDICE_DUPLICADO;
    }
}
