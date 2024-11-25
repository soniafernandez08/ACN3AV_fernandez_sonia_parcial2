package utils;

import java.sql.Connection;
import java.sql.Statement;

public class CrearTablas {

    public static void crearTablas() {
        String crearEmpleados = """
            CREATE TABLE IF NOT EXISTS Empleados(
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL,
                usuario VARCHAR(50) NOT NULL UNIQUE,
                contrasena VARCHAR(50) NOT NULL,
                rol ENUM('ADMIN', 'SUPERVISOR', 'CAJERO') NOT NULL
            );
        """;

        String crearClientes = """
            CREATE TABLE IF NOT EXISTS Clientes (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL,
                usuario VARCHAR(50) NOT NULL UNIQUE,
                contrasena VARCHAR(50) NOT NULL
            );
        """;

        String crearCuentas = """
            CREATE TABLE IF NOT EXISTS Cuentas (
                id INT AUTO_INCREMENT PRIMARY KEY,
                cliente_id INT NOT NULL,
                saldo DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
                FOREIGN KEY (cliente_id) REFERENCES Clientes(id)
            );
        """;

        String crearTransacciones = """
            CREATE TABLE IF NOT EXISTS Transacciones (
                id INT AUTO_INCREMENT PRIMARY KEY,
                cuenta_id INT NOT NULL,
                tipo VARCHAR(50) NOT NULL,
                monto DECIMAL(10, 2) NOT NULL,
                fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (cuenta_id) REFERENCES Cuentas(id)
            );
        """;

        try (Connection connection = ConexionDB.getConnection();
             Statement statement = connection.createStatement()) {

            // Crear tablas
            statement.execute(crearClientes);
            statement.execute(crearCuentas);
            statement.execute(crearTransacciones);
            statement.execute(crearEmpleados);

            System.out.println("Tablas creadas exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al crear las tablas: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        crearTablas();
    }
}

