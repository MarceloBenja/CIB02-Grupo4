package edu.uni.datos;

import edu.uni.modelo.Distribuidor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class DistribuidorDAO {

    public ObservableList<Distribuidor> listar() {
        ObservableList<Distribuidor> lista = FXCollections.observableArrayList();
        String sql = "SELECT id, nombre, ruc FROM Distribuidor";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Distribuidor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("ruc")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}