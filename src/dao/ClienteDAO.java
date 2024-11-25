package dao;

import models.Cliente;
import models.Cuenta;
import utils.ConexionDB;

import java.sql.*;

public class ClienteDAO {
    public Cliente obtenerClientePorUsuario(String usuario) {
        String query = "SELECT * FROM clientes WHERE usuario = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, usuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String contrasena = rs.getString("contrasena");
                int clienteId = rs.getInt("id");

                Cuenta cuenta = obtenerCuentaPorClienteId(clienteId);
                return new Cliente(nombre, usuario, contrasena, cuenta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Cuenta obtenerCuentaPorClienteId(int clienteId) {
        String query = "SELECT * FROM cuentas WHERE cliente_id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double saldo = rs.getDouble("saldo");
                return new Cuenta(saldo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void actualizarSaldo(int clienteId, double nuevoSaldo) {
        String query = "UPDATE cuentas SET saldo = ? WHERE cliente_id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, nuevoSaldo);
            stmt.setInt(2, clienteId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

