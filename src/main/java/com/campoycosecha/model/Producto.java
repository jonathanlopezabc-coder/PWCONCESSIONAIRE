package com.campoycosecha.model;

import java.io.Serializable;

public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String codigo;
    private String nombre;
    private String categoria;
    private double precio;
    private int stock;
    private String unidadMedida;

    public Producto() {}

    public Producto(String codigo, String nombre, String categoria, double precio, int stock, String unidadMedida) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.unidadMedida = unidadMedida;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    @Override
    public String toString() {
        return "[" + codigo + "] " + nombre + " (" + categoria + ") - Stock: " + stock + " " + unidadMedida;
    }
}