package models;

import models.Cuenta;

public class Cliente {
    private String nombre;
    private String usuario;
    private String contrasena;
    private Cuenta cuenta;

    public Cliente(String nombre, String usuario, String contrasena, Cuenta cuenta) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.cuenta = cuenta;
    }

    public String getUsuario() {
        return usuario;
    }

    public boolean autenticar(String contrasena) {
        return this.contrasena.equals(contrasena);
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    @Override
    public String toString() {
        return "Cliente: " + nombre + ", Usuario: " + usuario;
    }
}
