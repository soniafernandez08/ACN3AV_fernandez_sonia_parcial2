package models;

public class Transaccion {
    private String tipo;
    private double monto;

    public Transaccion(String tipo, double monto) {
        this.tipo = tipo;
        this.monto = monto;
    }

    @Override
    public String toString() {
        return "Tipo: " + tipo + ", Monto: $" + monto;
    }
}
