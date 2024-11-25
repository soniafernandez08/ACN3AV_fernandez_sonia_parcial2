package dao;

import models.Transaccion;
import utils.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaccionDAO {
    public void registrarTransaccion(int cuentaId, String tipo, double monto) {
        String query = "INSERT INTO transacciones (cuenta_id, tipo, monto) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, cuentaId);
            stmt.setString(2, tipo);
            stmt.setDouble(3, monto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Transaccion> obtenerTransaccionesPorCuentaId(int cuentaId) {
        List<Transaccion> transacciones = new ArrayList<>();
        String query = "SELECT * FROM transacciones WHERE cuenta_id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, cuentaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String tipo = rs.getString("tipo");
                double monto = rs.getDouble("monto");
                transacciones.add(new Transaccion(tipo, monto));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transacciones;
    }
}
