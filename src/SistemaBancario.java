import models.CajeroAutomatico;
import utils.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class SistemaBancario {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //TO DO: Para el final separar la logica en los directorios correspondientes como el de service

        // Llama al metodo de crear administradores predefinidos si no existen
        //TO DO: Cambiar para el final y generar un registro de los admin. Tambien validar si ya se registraron o no y de acuerdo a eso lanzar un mensaje de si existen o no
        crearAdminsPredefinidos();

        // Inicia el cajero con un monto en especifico
        //TO DO: Revisar si es necesario esta instancia
        CajeroAutomatico cajero = new CajeroAutomatico(5000);

        boolean salir = false;
        while (!salir) {
            System.out.println("\n**** Bienvenido al Cajero Java **** \n");
            System.out.println("\n-------- Menú Principal ---------");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Registrar empleado (ADMIN)");
            System.out.println("3. Iniciar sesión como cliente");
            System.out.println("4. Iniciar sesión como empleado");
            System.out.println("5. Salir");
            System.out.print("Elija una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    registrarCliente(scanner);
                    break;
                case 2:
                    // Verifica si el empleado tiene rol de admin
                    System.out.print("Ingrese usuario ADMIN: ");
                    String adminUsuario = scanner.nextLine();
                    System.out.print("Ingrese contraseña: ");
                    String adminContrasena = scanner.nextLine();

                    if (validarAdmin(adminUsuario, adminContrasena)) {
                        registrarEmpleado(scanner);
                    } else {
                        System.out.println("Acceso denegado. Solo un ADMIN puede registrar empleados.");
                    }
                    break;

                case 3:
                    iniciarSesionCliente(scanner, new CajeroAutomatico());
                    break;

                case 4:
                    iniciarSesionEmpleado(scanner, new CajeroAutomatico());
                    break;
                case 5:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
        scanner.close();
    }

    private static void crearAdminsPredefinidos() {
        String[][] admins = {
                {"admin3", "admin123", "ADMIN"},
                {"admin4", "admin456", "ADMIN"},
        };

        String sqlInsertAdmin = "INSERT IGNORE INTO Empleados (nombre, usuario, contrasena, rol) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsertAdmin)) {

            for (String[] admin : admins) {
                pstmt.setString(1, admin[0]);  // Nombre
                pstmt.setString(2, admin[0]);  // Usuario
                pstmt.setString(3, admin[1]);  // Contraseña
                pstmt.setString(4, admin[2]);  // Rol

                pstmt.addBatch();
            }
            pstmt.executeBatch();

            System.out.println("Administradores creados con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al crear administradores: " + e.getMessage());
        }
    }



    private static void registrarEmpleado(Scanner scanner) {
        System.out.println("\n-------- Registro de Empleado --------");
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();
        System.out.print("Nombre de usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();
        System.out.print("Rol (ADMIN/SUPERVISOR/CAJERO): ");
        String rol = scanner.nextLine().toUpperCase();

        String sql = "INSERT INTO Empleados (nombre, usuario, contrasena, rol) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, usuario);
            stmt.setString(3, contrasena);
            stmt.setString(4, rol);
            int filasInsertadas = stmt.executeUpdate();

            if (filasInsertadas > 0) {
                System.out.println("Empleado registrado con éxito.");
            } else {
                System.out.println("Error al registrar el empleado.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
//Valida si el usuario tiene rol de admin o no
    private static boolean validarAdmin(String usuario, String contrasena) {
        String sql = "SELECT rol FROM Empleados WHERE usuario = ? AND contrasena = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);

            var rs = stmt.executeQuery();
            if (rs.next()) {
                String rol = rs.getString("rol");
                return rol.equals("ADMIN");
            }
        } catch (Exception e) {
            System.err.println("Error al validar el administrador: " + e.getMessage());
        }

        return false;
    }




    private static void registrarCliente(Scanner scanner) {
        System.out.println("\n-------- Registro de Cliente --------");
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();
        System.out.print("Nombre de usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        String crearClienteSQL = "INSERT INTO Clientes (nombre, usuario, contrasena) VALUES (?, ?, ?)";
        String crearCuentaSQL = "INSERT INTO Cuentas (cliente_id, saldo) VALUES (LAST_INSERT_ID(), 0.00)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmtCliente = conn.prepareStatement(crearClienteSQL);
             PreparedStatement stmtCuenta = conn.prepareStatement(crearCuentaSQL)) {


            stmtCliente.setString(1, nombre);
            stmtCliente.setString(2, usuario);
            stmtCliente.setString(3, contrasena);
            int filasInsertadas = stmtCliente.executeUpdate();

            // Crea una cuenta asociada a un cliente
            if (filasInsertadas > 0) {
                stmtCuenta.executeUpdate();
                System.out.println("Cliente y cuenta registrados con éxito.");
            } else {
                System.out.println("Error al registrar el cliente.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void iniciarSesionCliente(Scanner scanner, CajeroAutomatico cajero) {
        System.out.println("\n-------- Inicio de Sesión Cliente --------");
        System.out.print("Usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        String sql = """
            SELECT c.id, c.saldo, cl.nombre
            FROM Cuentas c
            JOIN Clientes cl ON c.cliente_id = cl.id
            WHERE cl.usuario = ? AND cl.contrasena = ?
        """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);

            var rs = stmt.executeQuery();
            if (rs.next()) {
                int cuentaId = rs.getInt("id");
                double saldo = rs.getDouble("saldo");
                String nombre = rs.getString("nombre");

                System.out.println("Bienvenido, " + nombre);
                clienteMenu(scanner, cuentaId, saldo, cajero);
            } else {
                System.out.println("Credenciales incorrectas.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void iniciarSesionEmpleado(Scanner scanner, CajeroAutomatico cajero) {
        System.out.println("\n-------- Inicio de Sesión Empleado --------");
        System.out.print("Usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        String sql = """
        SELECT id, nombre, rol
        FROM Empleados
        WHERE usuario = ? AND contrasena = ?
    """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);

            var rs = stmt.executeQuery();
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String rol = rs.getString("rol");

                System.out.println("Bienvenido, " + nombre + " (Rol: " + rol + ")");
                empleadoMenu(scanner, cajero, rol);
            } else {
                System.out.println("Credenciales incorrectas.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void clienteMenu(Scanner scanner, int cuentaId, double saldo, CajeroAutomatico cajero) {
        boolean salir = false;
        while (!salir) {

            //TO DO: Realziar las transferencias a otra cuenta
            System.out.println("\n-------- Menú Cliente --------");
            System.out.println("1. Consultar saldo");
            System.out.println("2. Retirar dinero");
            System.out.println("3. Depositar dinero");
            System.out.println("4. Salir");
            System.out.print("Elija una opción: ");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("Saldo actual: " + saldo);
                    break;
                case 2:
                    System.out.print("Monto a retirar: ");
                    double montoRetiro = scanner.nextDouble();
                    if (cajero.retirarDinero(cuentaId, montoRetiro)) {
                        saldo -= montoRetiro;
                        System.out.println("Retiro exitoso. Nuevo saldo: " + saldo);
                    } else {
                        System.out.println("No se pudo realizar el retiro.");
                    }
                    break;
                case 3:
                    System.out.print("Monto a depositar: ");
                    double montoDeposito = scanner.nextDouble();
                    cajero.depositarDinero(cuentaId, montoDeposito);
                    saldo += montoDeposito;
                    System.out.println("Depósito exitoso. Nuevo saldo: " + saldo);
                    break;
                case 4:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void empleadoMenu(Scanner scanner, CajeroAutomatico cajero, String rol) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n-------- Menú Empleado --------");
            System.out.println("1. Consultar transacciones");
            if (rol.equals("ADMIN") || rol.equals("SUPERVISOR")) {
                System.out.println("2. Reponer dinero en cajero");
            }
            System.out.println("3. Salir");
            System.out.print("Elija una opción: ");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    cajero.consultarTransacciones();
                    break;
                case 2:
                    if (rol.equals("ADMIN") || rol.equals("SUPERVISOR")) {
                        System.out.print("Monto a reponer: ");
                        double montoReponer = scanner.nextDouble();
                        cajero.reponerDinero(montoReponer);
                        System.out.println("Dinero repuesto con éxito.");
                    } else {
                        System.out.println("No tienes permisos para esta opción.");
                    }
                    break;
                case 3:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

}