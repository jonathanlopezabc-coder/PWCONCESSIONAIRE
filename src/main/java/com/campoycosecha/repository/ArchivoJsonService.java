package com.campoycosecha.repository;

import com.campoycosecha.model.Pedido;
import com.campoycosecha.model.Producto;
import com.campoycosecha.model.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ArchivoJsonService {
    private static final String RUTA_PRODUCTOS = "inventario_campoycosecha.json";
    private static final String RUTA_USUARIOS = "usuarios_campoycosecha.json";
    private static final String RUTA_PEDIDOS = "pedidos_campoycosecha.json";
    private final Gson gson;

    public ArchivoJsonService() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    // --- PRODUCTOS ---
    public void guardarCatalogo(List<Producto> productos) {
        try (Writer writer = new FileWriter(RUTA_PRODUCTOS)) {
            gson.toJson(productos, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Producto> cargarCatalogo() {
        File archivo = new File(RUTA_PRODUCTOS);
        if (!archivo.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(archivo)) {
            Type tipoLista = new TypeToken<ArrayList<Producto>>() {}.getType();
            List<Producto> lista = gson.fromJson(reader, tipoLista);
            return (lista != null) ? lista : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    // --- USUARIOS ---
    public void guardarUsuarios(List<Usuario> usuarios) {
        try (Writer writer = new FileWriter(RUTA_USUARIOS)) {
            gson.toJson(usuarios, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Usuario> cargarUsuarios() {
        File archivo = new File(RUTA_USUARIOS);
        if (!archivo.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(archivo)) {
            Type tipoLista = new TypeToken<ArrayList<Usuario>>() {}.getType();
            List<Usuario> lista = gson.fromJson(reader, tipoLista);
            return (lista != null) ? lista : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    // --- PEDIDOS (HISTORIAL) ---
    public void guardarPedidos(List<Pedido> pedidos) {
        try (Writer writer = new FileWriter(RUTA_PEDIDOS)) {
            gson.toJson(pedidos, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Pedido> cargarPedidos() {
        File archivo = new File(RUTA_PEDIDOS);
        if (!archivo.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(archivo)) {
            Type tipoLista = new TypeToken<ArrayList<Pedido>>() {}.getType();
            List<Pedido> lista = gson.fromJson(reader, tipoLista);
            return (lista != null) ? lista : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}