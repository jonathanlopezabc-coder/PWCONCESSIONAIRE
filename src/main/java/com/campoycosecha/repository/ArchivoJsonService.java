package com.campoycosecha.repository;

import com.campoycosecha.model.Producto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ArchivoJsonService {
    private static final String RUTA_ARCHIVO = "inventario_campoycosecha.json";
    private final Gson gson;

    public ArchivoJsonService() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void guardarCatalogo(List<Producto> productos) throws IOException {
        try (Writer writer = new FileWriter(RUTA_ARCHIVO)) {
            gson.toJson(productos, writer);
        }
    }

    public List<Producto> cargarCatalogo() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return new ArrayList<>();

        try (Reader reader = new FileReader(archivo)) {
            Type tipoLista = new TypeToken<ArrayList<Producto>>() {}.getType();
            List<Producto> lista = gson.fromJson(reader, tipoLista);
            return (lista != null) ? lista : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}