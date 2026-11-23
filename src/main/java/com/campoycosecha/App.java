package com.campoycosecha;

import com.campoycosecha.model.Pedido;
import com.campoycosecha.model.Producto;
import com.campoycosecha.model.Usuario;
import com.campoycosecha.repository.ArchivoJsonService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class App extends Application {

    private Stage stagePrincipal;
    private final ArchivoJsonService jsonService = new ArchivoJsonService();

    private List<Producto> productos = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Pedido> pedidos = new ArrayList<>();
    private List<Producto> carrito = new ArrayList<>();

    private Usuario usuarioActual;
    private GridPane gridProductos;
    private Button btnCarrito;
    private Button btnDeseos;

    @Override
    public void start(Stage stage) {
        this.stagePrincipal = stage;
        inicializarDatos();
        mostrarLogin();
    }

    private void inicializarDatos() {
        productos = jsonService.cargarCatalogo();
        if (productos == null || productos.isEmpty()) {
            productos = new ArrayList<>();
            productos.add(new Producto("P-001", "Abono Orgánico 50kg", "Insumos", 95000, 40, "Bulto", "/imagenes/abundagro.jpg"));
            productos.add(new Producto("P-002", "Bomba de Espalda 20L", "Maquinaria", 650000, 8, "Unidad", "/imagenes/fulmigadora.jpg"));
            productos.add(new Producto("P-003", "Semillas Maíz 25kg", "Semillas", 180000, 15, "Bulto", "/imagenes/semilla.png"));
            productos.add(new Producto("P-004", "Manguera de Riego 50m", "Riego", 120000, 20, "Rollo", "/imagenes/manguera.png"));
            productos.add(new Producto("P-005", "Fertilizante 10-30-10", "Fertilizantes", 110000, 30, "Bulto", "/imagenes/ferilizante.png"));
            productos.add(new Producto("P-006", "Machete Rastrojero 22\"", "Herramientas", 45000, 50, "Unidad", "/imagenes/machete.jpg"));
            jsonService.guardarCatalogo(productos);
        }

        usuarios = jsonService.cargarUsuarios();
        if (usuarios == null) {
            usuarios = new ArrayList<>();
        }
        boolean existeAdmin = usuarios.stream().anyMatch(u -> "admin@campocosecha.com".equalsIgnoreCase(u.getCorreo()));
        boolean existeCliente = usuarios.stream().anyMatch(u -> "jonathan@campocosecha.com".equalsIgnoreCase(u.getCorreo()));

        if (!existeAdmin) {
            usuarios.add(new Usuario("Administrador Campo", "admin@campocosecha.com", "1234", "ADMIN"));
        }
        if (!existeCliente) {
            usuarios.add(new Usuario("Jonathan López", "jonathan@campocosecha.com", "1234", "CLIENTE"));
        }
        jsonService.guardarUsuarios(usuarios);

        pedidos = jsonService.cargarPedidos();
        if (pedidos == null) {
            pedidos = new ArrayList<>();
        }
    }

    private String formatearCOP(double valor) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("es", "CO"));
        return "$" + nf.format(valor) + " COP";
    }

    // ==================== 1. PANTALLA LOGIN Y REGISTRO ====================
    private void mostrarLogin() {
        usuarioActual = null;
        carrito.clear();

        VBox tarjeta = new VBox(14);
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setPadding(new Insets(35, 45, 35, 45));
        tarjeta.setMaxWidth(400);
        tarjeta.setStyle("-fx-background-color: #BDBDBD; -fx-background-radius: 14;");

        Label lblMarca = new Label("Campo y Cosecha.");
        lblMarca.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        lblMarca.setTextFill(Color.web("#1B5E20"));

        Label lblTitulo = new Label("Iniciar Sesión");
        lblTitulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblTitulo.setTextFill(Color.BLACK);

        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("Correo electrónico");
        txtCorreo.setPrefHeight(38);
        txtCorreo.setStyle("-fx-background-radius: 8; -fx-background-color: #E0E0E0;");

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Contraseña");
        txtPass.setPrefHeight(38);
        txtPass.setStyle("-fx-background-radius: 8; -fx-background-color: #E0E0E0;");

        Label lblError = new Label();
        lblError.setTextFill(Color.web("#B71C1C"));
        lblError.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        lblError.setWrapText(true);

        Button btnIngresar = new Button("Ingresar");
        btnIngresar.setPrefWidth(280);
        btnIngresar.setPrefHeight(40);
        btnIngresar.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        btnIngresar.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;");

        Button btnRegistrar = new Button("Crear cuenta nueva (Cliente)");
        btnRegistrar.setStyle("-fx-background-color: transparent; -fx-text-fill: #1B5E20; -fx-underline: true; -fx-cursor: hand;");
        btnRegistrar.setOnAction(e -> abrirDialogoRegistro());

        Label lblAyuda = new Label("Demo Admin: admin@campocosecha.com / 1234\nDemo Cliente: jonathan@campocosecha.com / 1234");
        lblAyuda.setFont(Font.font("Segoe UI", 11));
        lblAyuda.setTextFill(Color.web("#333333"));
        lblAyuda.setAlignment(Pos.CENTER);

        btnIngresar.setOnAction(e -> {
            String correo = txtCorreo.getText().trim();
            String pass = txtPass.getText().trim();

            if (correo.isEmpty() || pass.isEmpty()) {
                lblError.setText("Por favor ingresa correo y contraseña.");
                return;
            }

            Usuario encontrado = usuarios.stream()
                    .filter(u -> u.getCorreo() != null && u.getCorreo().equalsIgnoreCase(correo)
                            && u.getPassword() != null && u.getPassword().equals(pass))
                    .findFirst()
                    .orElse(null);

            if (encontrado == null) {
                lblError.setText("Credenciales incorrectas. Verifica tu correo o contraseña.");
            } else {
                usuarioActual = encontrado;
                try {
                    if ("ADMIN".equalsIgnoreCase(usuarioActual.getRol())) {
                        mostrarPanelAdmin();
                    } else {
                        mostrarCatalogoCliente();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    lblError.setText("Error al abrir ventana: " + ex.getMessage());
                }
            }
        });

        tarjeta.getChildren().addAll(lblMarca, lblTitulo, txtCorreo, txtPass, btnIngresar, btnRegistrar, lblError, lblAyuda);

        StackPane fondo = new StackPane(tarjeta);
        fondo.setStyle("-fx-background-color: #FFFFFF;");

        Scene scene = new Scene(fondo, 980, 660);
        stagePrincipal.setTitle("Campo y Cosecha - Iniciar Sesión");
        stagePrincipal.setScene(scene);
        stagePrincipal.show();
    }

    private void abrirDialogoRegistro() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Registro de Agricultor / Cliente");

        VBox box = new VBox(12);
        box.setPadding(new Insets(25));
        box.setAlignment(Pos.CENTER);

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre completo");
        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("Correo electrónico");
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Contraseña");

        Label lblMsg = new Label();
        lblMsg.setTextFill(Color.RED);

        Button btnGuardar = new Button("Registrarme");
        btnGuardar.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-weight: bold;");
        btnGuardar.setOnAction(e -> {
            if (txtNombre.getText().isBlank() || txtCorreo.getText().isBlank() || txtPass.getText().isBlank()) {
                lblMsg.setText("Completa todos los campos.");
                return;
            }
            boolean existe = usuarios.stream().anyMatch(u -> u.getCorreo().equalsIgnoreCase(txtCorreo.getText().trim()));
            if (existe) {
                lblMsg.setText("Ese correo ya está registrado.");
                return;
            }
            usuarios.add(new Usuario(txtNombre.getText().trim(), txtCorreo.getText().trim(), txtPass.getText().trim(), "CLIENTE"));
            jsonService.guardarUsuarios(usuarios);
            dialog.close();
            mostrarAlerta("Registro exitoso", "Cuenta creada. Ya puedes iniciar sesión.");
        });

        box.getChildren().addAll(new Label("Nueva Cuenta - Campo y Cosecha"), txtNombre, txtCorreo, txtPass, btnGuardar, lblMsg);
        dialog.setScene(new Scene(box, 340, 290));
        dialog.showAndWait();
    }

    // ==================== 2. PANEL ADMINISTRADOR ====================
    private void mostrarPanelAdmin() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F5F5F5;");

        HBox top = new HBox(20);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(14, 30, 14, 30));
        top.setStyle("-fx-background-color: #1A1A1A;");

        Label lblLogo = new Label("Campo y Cosecha | Panel Administrador");
        lblLogo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        lblLogo.setTextFill(Color.web("#4CAF50"));

        Region esp = new Region();
        HBox.setHgrow(esp, Priority.ALWAYS);

        Button btnVerPedidos = new Button("📋 Ventas Totales (" + pedidos.size() + ")");
        btnVerPedidos.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-font-weight: bold; -fx-cursor: hand;");
        btnVerPedidos.setOnAction(e -> abrirHistorialCompras(true));

        Button btnSalir = new Button("Cerrar Sesión");
        btnSalir.setStyle("-fx-background-color: #C62828; -fx-text-fill: white; -fx-background-radius: 14; -fx-cursor: hand;");
        btnSalir.setOnAction(e -> mostrarLogin());

        top.getChildren().addAll(lblLogo, esp, btnVerPedidos, btnSalir);
        root.setTop(top);

        ListView<Producto> listaAdmin = new ListView<>(FXCollections.observableArrayList(productos));
        listaAdmin.setPrefWidth(560);

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setPrefWidth(340);
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label lblForm = new Label("Gestión de Inventario");
        lblForm.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        TextField txtCodigo = new TextField();
        txtCodigo.setPromptText("Código (ej. P-007)");
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre del producto");
        TextField txtCategoria = new TextField();
        txtCategoria.setPromptText("Categoría (Insumos, Semillas...)");
        TextField txtPrecio = new TextField();
        txtPrecio.setPromptText("Precio COP (ej. 85000)");
        TextField txtStock = new TextField();
        txtStock.setPromptText("Stock disponible (ej. 25)");
        TextField txtUnidad = new TextField();
        txtUnidad.setPromptText("Unidad (Bulto, Unidad, Litro)");
        TextField txtImg = new TextField("/imagenes/abundagro.jpg");
        txtImg.setPromptText("Ruta imagen");

        listaAdmin.getSelectionModel().selectedItemProperty().addListener((obs, oldV, sel) -> {
            if (sel != null) {
                txtCodigo.setText(sel.getCodigo());
                txtNombre.setText(sel.getNombre());
                txtCategoria.setText(sel.getCategoria());
                txtPrecio.setText(String.valueOf((int) sel.getPrecio()));
                txtStock.setText(String.valueOf(sel.getStock()));
                txtUnidad.setText(sel.getUnidadMedida());
                txtImg.setText(sel.getRutaImagen() != null ? sel.getRutaImagen() : "/imagenes/abundagro.jpg");
            }
        });

        Button btnAgregar = new Button("➕ Agregar Nuevo");
        btnAgregar.setMaxWidth(Double.MAX_VALUE);
        btnAgregar.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnAgregar.setOnAction(e -> {
            try {
                Producto p = new Producto(
                        txtCodigo.getText().trim(),
                        txtNombre.getText().trim(),
                        txtCategoria.getText().trim(),
                        Double.parseDouble(txtPrecio.getText().trim()),
                        Integer.parseInt(txtStock.getText().trim()),
                        txtUnidad.getText().trim(),
                        txtImg.getText().trim()
                );
                productos.add(p);
                jsonService.guardarCatalogo(productos);
                listaAdmin.setItems(FXCollections.observableArrayList(productos));
                mostrarAlerta("Éxito", "Producto agregado al catálogo.");
            } catch (Exception ex) {
                mostrarAlerta("Error", "Verifica que precio y stock sean números válidos.");
            }
        });

        Button btnActualizar = new Button("✏️ Actualizar Seleccionado");
        btnActualizar.setMaxWidth(Double.MAX_VALUE);
        btnActualizar.setStyle("-fx-background-color: #1565C0; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnActualizar.setOnAction(e -> {
            Producto sel = listaAdmin.getSelectionModel().getSelectedItem();
            if (sel == null) {
                mostrarAlerta("Aviso", "Selecciona un producto de la lista.");
                return;
            }
            try {
                sel.setCodigo(txtCodigo.getText().trim());
                sel.setNombre(txtNombre.getText().trim());
                sel.setCategoria(txtCategoria.getText().trim());
                sel.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
                sel.setStock(Integer.parseInt(txtStock.getText().trim()));
                sel.setUnidadMedida(txtUnidad.getText().trim());
                sel.setRutaImagen(txtImg.getText().trim());
                jsonService.guardarCatalogo(productos);
                listaAdmin.refresh();
                mostrarAlerta("Actualizado", "Producto actualizado correctamente.");
            } catch (Exception ex) {
                mostrarAlerta("Error", "Datos numéricos inválidos.");
            }
        });

        Button btnEliminar = new Button("🗑️ Eliminar Seleccionado");
        btnEliminar.setMaxWidth(Double.MAX_VALUE);
        btnEliminar.setStyle("-fx-background-color: #C62828; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnEliminar.setOnAction(e -> {
            Producto sel = listaAdmin.getSelectionModel().getSelectedItem();
            if (sel != null) {
                productos.remove(sel);
                jsonService.guardarCatalogo(productos);
                listaAdmin.setItems(FXCollections.observableArrayList(productos));
            }
        });

        form.getChildren().addAll(lblForm, txtCodigo, txtNombre, txtCategoria, txtPrecio, txtStock, txtUnidad, txtImg,
                btnAgregar, btnActualizar, btnEliminar);

        HBox centro = new HBox(25, listaAdmin, form);
        centro.setPadding(new Insets(25));
        centro.setAlignment(Pos.CENTER);
        root.setCenter(centro);

        stagePrincipal.setTitle("Campo y Cosecha - Administración");
        stagePrincipal.setScene(new Scene(root, 980, 680));
    }

    // ==================== 3. PANTALLA CLIENTE (CATÁLOGO + DESEOS + CARRITO + HISTORIAL) ====================
    private void mostrarCatalogoCliente() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FFFFFF;");

        HBox barraSuperior = new HBox(14);
        barraSuperior.setAlignment(Pos.CENTER_LEFT);
        barraSuperior.setPadding(new Insets(14, 25, 14, 25));
        barraSuperior.setStyle("-fx-background-color: #1A1A1A;");

        Label lblLogo = new Label("Campo y Cosecha");
        lblLogo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        lblLogo.setTextFill(Color.web("#2E7D32"));

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar insumos, maquinaria...");
        txtBuscar.setPrefWidth(270);
        txtBuscar.setPrefHeight(36);
        txtBuscar.setStyle("-fx-background-radius: 18; -fx-padding: 0 15 0 15;");
        txtBuscar.textProperty().addListener((obs, viejo, nuevo) -> renderizarProductos(nuevo));

        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        btnDeseos = new Button("♥ Deseos (" + usuarioActual.getListaDeseosCodigos().size() + ")");
        btnDeseos.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        btnDeseos.setStyle("-fx-background-color: #FCE4EC; -fx-text-fill: #C2185B; -fx-background-radius: 16; -fx-padding: 8 14 8 14; -fx-cursor: hand;");
        btnDeseos.setOnAction(e -> abrirListaDeseos());

        Button btnHistorial = new Button("📦 Mis Compras");
        btnHistorial.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        btnHistorial.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B5E20; -fx-background-radius: 16; -fx-padding: 8 14 8 14; -fx-cursor: hand;");
        btnHistorial.setOnAction(e -> abrirHistorialCompras(false));

        btnCarrito = new Button("🛒 Carrito (" + carrito.size() + ")");
        btnCarrito.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        btnCarrito.setStyle("-fx-background-color: white; -fx-background-radius: 16; -fx-padding: 8 15 8 15; -fx-cursor: hand;");
        btnCarrito.setOnAction(e -> abrirDetalleCarrito());

        Button btnSalir = new Button("Salir");
        btnSalir.setStyle("-fx-background-color: #C62828; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 14 8 14; -fx-cursor: hand;");
        btnSalir.setOnAction(e -> mostrarLogin());

        barraSuperior.getChildren().addAll(lblLogo, txtBuscar, espaciador, btnDeseos, btnHistorial, btnCarrito, btnSalir);
        root.setTop(barraSuperior);

        gridProductos = new GridPane();
        gridProductos.setHgap(45);
        gridProductos.setVgap(30);
        gridProductos.setPadding(new Insets(30, 50, 30, 50));
        gridProductos.setAlignment(Pos.TOP_CENTER);

        renderizarProductos("");

        ScrollPane scroll = new ScrollPane(gridProductos);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #FFFFFF; -fx-border-color: transparent;");
        root.setCenter(scroll);

        Scene scene = new Scene(root, 980, 680);
        stagePrincipal.setTitle("Campo y Cosecha - Bienvenido " + usuarioActual.getNombre());
        stagePrincipal.setScene(scene);
    }

    private void renderizarProductos(String filtro) {
        gridProductos.getChildren().clear();
        String texto = filtro == null ? "" : filtro.trim().toLowerCase();

        int col = 0;
        int fila = 0;

        for (Producto item : productos) {
            String nombreProd = item.getNombre() != null ? item.getNombre() : "";
            String catProd = item.getCategoria() != null ? item.getCategoria() : "";

            if (!texto.isEmpty() && !nombreProd.toLowerCase().contains(texto)
                    && !catProd.toLowerCase().contains(texto)) {
                continue;
            }

            VBox tarjeta = new VBox(8);
            tarjeta.setAlignment(Pos.CENTER_LEFT);
            tarjeta.setPadding(new Insets(12));
            tarjeta.setPrefWidth(235);
            tarjeta.setStyle("-fx-border-color: #EEEEEE; -fx-border-radius: 10; -fx-background-radius: 10;");

            ImageView imgView = new ImageView();
            imgView.setFitWidth(150);
            imgView.setFitHeight(150);
            imgView.setPreserveRatio(true);

            if (item.getRutaImagen() != null && !item.getRutaImagen().isBlank()) {
                var stream = getClass().getResourceAsStream(item.getRutaImagen());
                if (stream != null) {
                    imgView.setImage(new Image(stream));
                }
            }

            StackPane contenedorImg = new StackPane(imgView);
            contenedorImg.setPrefSize(180, 160);

            Label lblNombre = new Label(nombreProd);
            lblNombre.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

            Label lblPrecio = new Label("Precio: " + formatearCOP(item.getPrecio()));
            lblPrecio.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
            lblPrecio.setTextFill(Color.web("#2E7D32"));

            String unidad = item.getUnidadMedida() != null ? item.getUnidadMedida() : "unid.";
            Label lblStock = new Label("Stock: " + item.getStock() + " " + unidad);
            lblStock.setFont(Font.font("Segoe UI", 11));
            lblStock.setTextFill(item.getStock() > 0 ? Color.GRAY : Color.RED);

            Button btnAgregar = new Button(item.getStock() > 0 ? "Agregar al carrito" : "Agotado");
            btnAgregar.setDisable(item.getStock() <= 0);
            btnAgregar.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
            btnAgregar.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 7 12 7 12; -fx-cursor: hand;");
            btnAgregar.setOnAction(e -> {
                long enCarrito = carrito.stream().filter(p -> p.getCodigo().equals(item.getCodigo())).count();
                if (enCarrito >= item.getStock()) {
                    mostrarAlerta("Stock máximo", "No hay más unidades disponibles de " + item.getNombre());
                    return;
                }
                carrito.add(item);
                btnCarrito.setText("🛒 Carrito (" + carrito.size() + ")");
            });

            boolean esFavorito = usuarioActual.getListaDeseosCodigos().contains(item.getCodigo());
            Button btnFav = new Button(esFavorito ? "♥" : "♡");
            btnFav.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
            btnFav.setStyle("-fx-background-color: #FCE4EC; -fx-text-fill: #C2185B; -fx-background-radius: 6; -fx-padding: 5 10 5 10; -fx-cursor: hand;");
            btnFav.setOnAction(e -> {
                List<String> deseos = usuarioActual.getListaDeseosCodigos();
                if (deseos.contains(item.getCodigo())) {
                    deseos.remove(item.getCodigo());
                    btnFav.setText("♡");
                } else {
                    deseos.add(item.getCodigo());
                    btnFav.setText("♥");
                }
                jsonService.guardarUsuarios(usuarios);
                btnDeseos.setText("♥ Deseos (" + deseos.size() + ")");
            });

            HBox acciones = new HBox(8, btnAgregar, btnFav);
            tarjeta.getChildren().addAll(contenedorImg, lblNombre, lblPrecio, lblStock, acciones);
            gridProductos.add(tarjeta, col, fila);

            col++;
            if (col == 3) {
                col = 0;
                fila++;
            }
        }
    }

    // ==================== 4. CARRITO DE COMPRA Y FINALIZAR PEDIDO ====================
    private void abrirDetalleCarrito() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Carrito de Compras - Campo y Cosecha");

        VBox box = new VBox(14);
        box.setPadding(new Insets(20));

        Label lblTitulo = new Label("Tu Carrito (" + carrito.size() + " productos)");
        lblTitulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));

        ListView<String> listaVista = new ListView<>();
        Runnable refrescar = () -> {
            listaVista.getItems().clear();
            for (Producto p : carrito) {
                listaVista.getItems().add(p.getNombre() + " — " + formatearCOP(p.getPrecio()));
            }
        };
        refrescar.run();

        double total = carrito.stream().mapToDouble(Producto::getPrecio).sum();
        Label lblTotal = new Label("Total a pagar: " + formatearCOP(total));
        lblTotal.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        lblTotal.setTextFill(Color.web("#1B5E20"));

        Button btnQuitar = new Button("Quitar seleccionado");
        btnQuitar.setOnAction(e -> {
            int idx = listaVista.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                carrito.remove(idx);
                refrescar.run();
                double nuevoTotal = carrito.stream().mapToDouble(Producto::getPrecio).sum();
                lblTotal.setText("Total a pagar: " + formatearCOP(nuevoTotal));
                lblTitulo.setText("Tu Carrito (" + carrito.size() + " productos)");
                btnCarrito.setText("🛒 Carrito (" + carrito.size() + ")");
            }
        });

        Button btnComprar = new Button("✅ Finalizar Compra");
        btnComprar.setDisable(carrito.isEmpty());
        btnComprar.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 18 8 18; -fx-cursor: hand;");
        btnComprar.setOnAction(e -> {
            for (Producto itemCarrito : carrito) {
                for (Producto prodCatalogo : productos) {
                    if (prodCatalogo.getCodigo().equals(itemCarrito.getCodigo())) {
                        prodCatalogo.setStock(Math.max(0, prodCatalogo.getStock() - 1));
                    }
                }
            }
            jsonService.guardarCatalogo(productos);

            Pedido nuevoPedido = new Pedido(usuarioActual.getCorreo(), new ArrayList<>(carrito));
            pedidos.add(nuevoPedido);
            jsonService.guardarPedidos(pedidos);

            carrito.clear();
            btnCarrito.setText("🛒 Carrito (0)");
            renderizarProductos("");
            dialog.close();
            mostrarAlerta("¡Compra Exitosa!", "Pedido registrado con código " + nuevoPedido.getIdPedido()
                    + "\nPuedes revisarlo en 'Mis Compras'.");
        });

        HBox botones = new HBox(12, btnQuitar, btnComprar);
        box.getChildren().addAll(lblTitulo, listaVista, lblTotal, botones);

        dialog.setScene(new Scene(box, 450, 380));
        dialog.showAndWait();
    }

    // ==================== 5. LISTA DE DESEOS ====================
    private void abrirListaDeseos() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Lista de Deseos - " + usuarioActual.getNombre());

        VBox box = new VBox(14);
        box.setPadding(new Insets(20));

        Label lblTitulo = new Label("♥ Tus Productos Guardados");
        lblTitulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        List<Producto> favoritos = productos.stream()
                .filter(p -> usuarioActual.getListaDeseosCodigos().contains(p.getCodigo()))
                .collect(Collectors.toList());

        ListView<Producto> listaFav = new ListView<>(FXCollections.observableArrayList(favoritos));

        Button btnMoverCarrito = new Button("🛒 Mover al Carrito");
        btnMoverCarrito.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-weight: bold;");
        btnMoverCarrito.setOnAction(e -> {
            Producto sel = listaFav.getSelectionModel().getSelectedItem();
            if (sel != null && sel.getStock() > 0) {
                carrito.add(sel);
                btnCarrito.setText("🛒 Carrito (" + carrito.size() + ")");
                mostrarAlerta("Agregado", sel.getNombre() + " se añadió a tu carrito.");
            }
        });

        Button btnEliminarDeseo = new Button("Quitar de deseos");
        btnEliminarDeseo.setOnAction(e -> {
            Producto sel = listaFav.getSelectionModel().getSelectedItem();
            if (sel != null) {
                usuarioActual.getListaDeseosCodigos().remove(sel.getCodigo());
                jsonService.guardarUsuarios(usuarios);
                listaFav.getItems().remove(sel);
                btnDeseos.setText("♥ Deseos (" + usuarioActual.getListaDeseosCodigos().size() + ")");
                renderizarProductos("");
            }
        });

        box.getChildren().addAll(lblTitulo, listaFav, new HBox(10, btnMoverCarrito, btnEliminarDeseo));
        dialog.setScene(new Scene(box, 460, 360));
        dialog.showAndWait();
    }

    // ==================== 6. HISTORIAL DE COMPRAS ====================
    private void abrirHistorialCompras(boolean verTodosAdmin) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(verTodosAdmin ? "Historial Global de Ventas" : "Historial de Compras");

        VBox box = new VBox(12);
        box.setPadding(new Insets(20));

        List<Pedido> filtrados = verTodosAdmin ? pedidos : pedidos.stream()
                .filter(p -> p.getCliente() != null && p.getCliente().equalsIgnoreCase(usuarioActual.getCorreo()))
                .collect(Collectors.toList());

        TextArea area = new TextArea();
        area.setEditable(false);
        area.setPrefHeight(280);

        if (filtrados.isEmpty()) {
            area.setText("Aún no hay compras registradas.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Pedido p : filtrados) {
                sb.append("========================================\n")
                  .append("Pedido: ").append(p.getIdPedido()).append(" | Fecha: ").append(p.getFechaHora()).append("\n")
                  .append("Cliente: ").append(p.getCliente()).append("\n")
                  .append("Productos:\n");
                for (Producto prod : p.getProductos()) {
                    sb.append("   - ").append(prod.getNombre()).append(" (").append(formatearCOP(prod.getPrecio())).append(")\n");
                }
                sb.append("TOTAL PAGADO: ").append(formatearCOP(p.getTotal())).append("\n\n");
            }
            area.setText(sb.toString());
        }

        box.getChildren().addAll(new Label("📦 Registro de Pedidos"), area);
        dialog.setScene(new Scene(box, 500, 360));
        dialog.showAndWait();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
   
   }
    }