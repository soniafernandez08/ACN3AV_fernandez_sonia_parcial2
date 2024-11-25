package models;

public class Cuenta {
    private double saldo;

    public Cuenta(double saldoInicial) {
        this.saldo = saldoInicial;
    }

    public double getSaldo() {
        return saldo;
    }

    public boolean retirar(double monto) {
        if (monto > saldo) {
            return false;
        }
        saldo -= monto;
        return true;
    }

    public void depositar(double monto) {
        saldo += monto;
    }

    public String consultarSaldo() {
        return "Saldo actual: $" + saldo;
    }
}

