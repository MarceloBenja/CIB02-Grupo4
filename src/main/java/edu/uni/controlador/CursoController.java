package edu.uni.controlador;

import edu.uni.datos.CursoDAO;
import edu.uni.modelo.Curso;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/** Controla la pantalla de mantenimiento de cursos de la oferta académica (HU-02, CU-07). */
public class CursoController implements Initializable {
    private static final System.Logger LOG = System.getLogger(CursoController.class.getName());
    private static final int CODIGO_MAX = 10;
    private static final int NOMBRE_MAX = 100;
    private static final int CREDITOS_MIN = 1;
    private static final int CREDITOS_MAX = 6;
    private static final int CICLO_MIN = 1;
    private static final int CICLO_MAX = 10;

    @FXML private TableView<Curso> tablaCursos;
    @FXML private TableColumn<Curso, String> colCodigo;
    @FXML private TableColumn<Curso, String> colNombre;
    @FXML private TableColumn<Curso, Number> colCreditos;
    @FXML private TableColumn<Curso, Number> colCiclo;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCreditos;
    @FXML private TextField txtCiclo;

    private final CursoDAO dao = new CursoDAO();
    private Curso seleccionado;

    @Override
    public void initialize(URL url, ResourceBundle recursos) {
        colCodigo.setCellValueFactory(celda -> celda.getValue().codigoProperty());
        colNombre.setCellValueFactory(celda -> celda.getValue().nombreProperty());
        colCreditos.setCellValueFactory(celda -> celda.getValue().creditosProperty());
        colCiclo.setCellValueFactory(celda -> celda.getValue().cicloProperty());
        tablaCursos.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, nuevo) -> mostrarEnFormulario(nuevo));
        cargarTabla();
    }

    private void cargarTabla() {
        try {
            tablaCursos.setItems(dao.listar());
        } catch (SQLException e) {
            mostrarError("No se pudo cargar la lista de cursos.", e);
        }
    }

    private void mostrarEnFormulario(Curso curso) {
        seleccionado = curso;
        if (curso == null) {
            return;
        }
        txtCodigo.setText(curso.getCodigo());
        txtNombre.setText(curso.getNombre());
        txtCreditos.setText(String.valueOf(curso.getCreditos()));
        txtCiclo.setText(String.valueOf(curso.getCiclo()));
    }

    @FXML
    private void onNuevo() {
        tablaCursos.getSelectionModel().clearSelection();
        seleccionado = null;
        txtCodigo.clear();
        txtNombre.clear();
        txtCreditos.clear();
        txtCiclo.clear();
        txtCodigo.requestFocus();
    }

    @FXML
    private void onGuardar() {
        String error = validar();
        if (error != null) {
            new Alert(Alert.AlertType.WARNING, error).showAndWait();
            return;
        }
        Curso curso = leerFormulario();
        try {
            if (seleccionado == null) {
                dao.insertar(curso);
            } else {
                dao.actualizar(curso);
            }
            cargarTabla();
            onNuevo();
        } catch (SQLException e) {
            if (CursoDAO.esCodigoDuplicado(e)) {
                new Alert(Alert.AlertType.WARNING,
                        "Ya existe un curso con el código " + curso.getCodigo() + ".").showAndWait();
            } else {
                mostrarError("No se pudo guardar el curso.", e);
            }
        }
    }

    @FXML
    private void onEliminar() {
        if (seleccionado == null) {
            new Alert(Alert.AlertType.INFORMATION, "Seleccione en la tabla el curso que desea eliminar.")
                    .showAndWait();
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar el curso " + seleccionado.getCodigo() + "?", ButtonType.YES, ButtonType.NO);
        if (confirmacion.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }
        try {
            dao.eliminar(seleccionado.getIdCurso());
            cargarTabla();
            onNuevo();
        } catch (SQLException e) {
            if (CursoDAO.tieneSecciones(e)) {
                new Alert(Alert.AlertType.WARNING, "No se puede eliminar el curso "
                        + seleccionado.getCodigo() + " porque tiene secciones registradas.").showAndWait();
            } else {
                mostrarError("No se pudo eliminar el curso.", e);
            }
        }
    }

    private Curso leerFormulario() {
        int id = seleccionado == null ? 0 : seleccionado.getIdCurso();
        return new Curso(id,
                txtCodigo.getText().strip().toUpperCase(Locale.ROOT),
                txtNombre.getText().strip(),
                Integer.parseInt(txtCreditos.getText().strip()),
                Integer.parseInt(txtCiclo.getText().strip()));
    }

    /** Devuelve el primer problema del formulario, o null si los datos son válidos. */
    private String validar() {
        String codigo = txtCodigo.getText().strip();
        String nombre = txtNombre.getText().strip();
        if (codigo.isEmpty() || nombre.isEmpty()) {
            return "El código y el nombre son obligatorios.";
        }
        if (codigo.length() > CODIGO_MAX || nombre.length() > NOMBRE_MAX) {
            return "El código admite hasta " + CODIGO_MAX + " caracteres y el nombre hasta "
                    + NOMBRE_MAX + ".";
        }
        if (!enRango(txtCreditos.getText(), CREDITOS_MIN, CREDITOS_MAX)) {
            return "Los créditos deben ser un número entero de " + CREDITOS_MIN + " a " + CREDITOS_MAX + ".";
        }
        if (!enRango(txtCiclo.getText(), CICLO_MIN, CICLO_MAX)) {
            return "El ciclo debe ser un número entero de " + CICLO_MIN + " a " + CICLO_MAX + ".";
        }
        return null;
    }

    private static boolean enRango(String texto, int minimo, int maximo) {
        try {
            int valor = Integer.parseInt(texto.strip());
            return valor >= minimo && valor <= maximo;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** Muestra un mensaje genérico y deja el detalle técnico en el registro, como pide la guía 5. */
    private void mostrarError(String mensaje, Exception e) {
        LOG.log(System.Logger.Level.ERROR, mensaje, e);
        new Alert(Alert.AlertType.ERROR, mensaje + " Intente de nuevo o avise al administrador.")
                .showAndWait();
    }
}
