package edu.uni.datos;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Punto único de conexión a SQL Server.
 *
 * <p>Los datos de conexión se leen del archivo db.properties de la carpeta desde la que se
 * ejecuta la aplicación. Ese archivo está en .gitignore para que ninguna contraseña llegue al
 * repositorio; db.properties.example muestra su formato.
 */
public final class ConexionBD {
    private static final Path ARCHIVO = Path.of("db.properties");
    private static final String PROP_URL = "db.url";
    private static final String PROP_USUARIO = "db.usuario";
    private static final String PROP_CLAVE = "db.clave";

    private static Connection conexion;

    private ConexionBD() {
    }

    /**
     * Devuelve la conexión abierta, o abre una nueva si no existe o ya se cerró.
     *
     * @return conexión a la base de datos del proyecto
     * @throws SQLException si falta db.properties o el servidor rechaza la conexión
     */
    public static synchronized Connection obtener() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            Properties datos = leerConfiguracion();
            conexion = DriverManager.getConnection(
                    requerido(datos, PROP_URL),
                    requerido(datos, PROP_USUARIO),
                    requerido(datos, PROP_CLAVE));
        }
        return conexion;
    }

    private static Properties leerConfiguracion() throws SQLException {
        if (!Files.isRegularFile(ARCHIVO)) {
            throw new SQLException("No se encontró " + ARCHIVO.toAbsolutePath()
                    + ". Copie db.properties.example como db.properties y complete sus datos.");
        }
        Properties datos = new Properties();
        try (Reader lector = Files.newBufferedReader(ARCHIVO, StandardCharsets.UTF_8)) {
            datos.load(lector);
        } catch (IOException e) {
            throw new SQLException("No se pudo leer " + ARCHIVO.toAbsolutePath(), e);
        }
        return datos;
    }

    private static String requerido(Properties datos, String propiedad) throws SQLException {
        String valor = datos.getProperty(propiedad);
        if (valor == null || valor.isBlank()) {
            throw new SQLException("Falta el valor de " + propiedad + " en " + ARCHIVO);
        }
        return valor.strip();
    }
}
