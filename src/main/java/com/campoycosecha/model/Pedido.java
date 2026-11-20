package com.campoycosecha.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Pedido {
    private static int contador = 1000;
    private String idPedido;
    private String cliente;
    private List<Producto> productos;
    private double total;
    private String fechaHora;

    public Pedido(String cliente, List<Producto> productos) {
        this.idPedido = "PED-" + (++contador);
        this.cliente = cliente;
        this.productos = productos;
        this.total = productos.stream().mapToDouble(Producto::getPrecio).sum();
        this.fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getIdPedido() { return idPedido; }
    public String getCliente() { return cliente; }
    public List<Producto> getProductos() { return productos; }
    public double getTotal() { return total; }
    public String getFechaHora() { return fechaHora; }

    @Override
    public String toString() {
        return idPedido + " | Agricultor: " + cliente + " | Ítems: " + productos.size() + " | Total: $" + String.format("%.2f", total);
    }
}