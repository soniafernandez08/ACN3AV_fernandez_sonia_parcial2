package models;

import java.util.ArrayList;
import java.util.List;

public class CajeroAutomatico {
    private double dineroDisponible;
    private List<String> transacciones;


    public CajeroAutomatico(double dineroInicial) {
        this.dineroDisponible = dineroInicial;
        this.transacciones = new ArrayList<>();
    }


    public CajeroAutomatico() {
        this.transacciones = new ArrayList<>();
    }

    public boolean retirarDinero(int clienteId, double monto) {
        if (monto > 0 && monto <= dineroDisponible) {
            dineroDisponible -= monto;
            registrarTransaccion("Cliente ID: " + clienteId + " retiró: " + monto);
            return true;
        }
        return false;
    }

    public void depositarDinero(int clienteId, double monto) {
        if (monto > 0) {
            dineroDisponible += monto;
            registrarTransaccion("Cliente ID: " + clienteId + " depositó: " + monto);
        }
    }

    public void reponerDinero(double monto) {
        if (monto > 0) {
            dineroDisponible += monto;
            registrarTransaccion("Empleado repuso: " + monto);
        }
    }

    public void consultarTransacciones() {
        System.out.println("\n--- Transacciones recientes ---");
        for (String transaccion : transacciones) {
            System.out.println(transaccion);
        }
    }

    private void registrarTransaccion(String detalle) {
        transacciones.add(detalle);
    }

    public double getDineroDisponible() {
        return dineroDisponible;
    }
}
