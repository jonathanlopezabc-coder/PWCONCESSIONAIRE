package com.campoycosecha.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private String correo;
    private String password;
    private String rol; // "ADMIN" o "CLIENTE"
    private List<String> listaDeseosCodigos = new ArrayList<>();

    public Usuario() {}

    public Usuario(String nombre, String correo, String password, String rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
        this.listaDeseosCodigos = new ArrayList<>();
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public List<String> getListaDeseosCodigos() {
        if (listaDeseosCodigos == null) listaDeseosCodigos = new ArrayList<>();
        return listaDeseosCodigos;
    }

    public void setListaDeseosCodigos(List<String> listaDeseosCodigos) {
        this.listaDeseosCodigos = listaDeseosCodigos;
    }
}