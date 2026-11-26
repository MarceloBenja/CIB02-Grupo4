package edu.uni.controlador;

import edu.uni.datos.UsuarioDAO;
import edu.uni.modelo.Usuario;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/** Controla la pantalla de inicio de sesión (HU-01). */
public class LoginController {
    private static final System.Logger LOG = System.getLogger(LoginController.class.getName());

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtClave;
    @FXML private Label lblMensaje;

    private final UsuarioDAO dao = new UsuarioDAO();

    /**
     * Valida las credenciales y, si son correctas, abre la ventana principal.
     *
     * <p>El mensaje es el mismo si el usuario no existe o si la contraseña es incorrecta, para no
     * dar pistas a quien intente adivinar credenciales. Si falla la base de datos, la pantalla
     * muestra un mensaje general y el detalle técnico queda solo en la consola (guía 5).
     */
    @FXML
    private void onIngresar() {
        String usuario = txtUsuario.getText().strip();
        char[] clave = txtClave.getText().toCharArray();
        txtClave.clear();
        try {
            if (usuario.isEmpty() || clave.length == 0) {
                lblMensaje.setText("Ingrese usuario y contraseña.");
                return;
            }
            Optional<Usuario> encontrado = dao.autenticar(usuario, clave);
            if (encontrado.isEmpty()) {
                lblMensaje.setText("Usuario o contraseña incorrectos.");
                return;
            }
            Navegacion.mostrarPrincipal((Stage) txtUsuario.getScene().getWindow(), encontrado.get());
        } catch (SQLException e) {
            LOG.log(System.Logger.Level.ERROR, "No se pudo consultar la tabla Usuario", e);
            lblMensaje.setText("No se pudo conectar con la base de datos. "
                    + "Intente de nuevo o avise al administrador.");
        } catch (IOException e) {
            LOG.log(System.Logger.Level.ERROR, "No se pudo abrir la ventana principal", e);
            lblMensaje.setText("No se pudo abrir la ventana principal.");
        } finally {
            Arrays.fill(clave, '\0');
        }
    }
}
