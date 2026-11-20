package com.campoycosecha;

import javafx.application.Application;
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
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class App extends Application {

    private Stage stagePrincipal;
    private List<ItemCatalogo> productos = new ArrayList<>();
    private List<ItemCatalogo> carrito = new ArrayList<>();
    private GridPane gridProductos;
    private Button btnCarrito;

    private static class ItemCatalogo {
        String nombre;
        double precio;
        int stock;
        String rutaImagen;

        ItemCatalogo(String nombre, double precio, int stock, String rutaImagen) {
            this.nombre = nombre;
            this.precio = precio;
            this.stock = stock;
            this.rutaImagen = rutaImagen;
        }
    }

    @Override
    public void start(Stage stage) {
        this.stagePrincipal = stage;
        cargarProductosFigma();
        mostrarLogin();
    }

    private void cargarProductosFigma() {
        productos.clear();
        productos.add(new ItemCatalogo("Abono Orgánico 50kg", 95000, 40, "/imagenes/abundagro.jpg"));
        productos.add(new ItemCatalogo("Bomba de Espalda 20L", 650000, 8, "/imagenes/fulmigadora.jpg"));
        productos.add(new ItemCatalogo("Semillas Maíz 25kg", 180000, 15, "/imagenes/semilla.png"));
        productos.add(new ItemCatalogo("Manguera de Riego 50m", 120000, 20, "/imagenes/manguera.png"));
        productos.add(new ItemCatalogo("Fertilizante 10-30-10", 110000, 30, "/imagenes/ferilizante.png"));
        productos.add(new ItemCatalogo("Machete Rastrojero 22\"", 45000, 50, "/imagenes/machete.jpg"));
    }

    private String formatearCOP(double valor) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("es", "CO"));
        return "$" + nf.format(valor) + " COP";
    }

    // ==================== PANTALLA 1: LOGIN ====================
    private void mostrarLogin() {
        VBox tarjeta = new VBox(16);
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setPadding(new Insets(40, 45, 40, 45));
        tarjeta.setMaxWidth(380);
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
        txtPass.setMaxWidth(220);
        txtPass.setStyle("-fx-background-radius: 6; -fx-background-color: #E0E0E0;");

        Label lblError = new Label();
        lblError.setTextFill(Color.web("#B71C1C"));
        lblError.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

        Button btnIngresar = new Button("Ingresar");
        btnIngresar.setPrefWidth(260);
        btnIngresar.setPrefHeight(40);
        btnIngresar.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        btnIngresar.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;");

        btnIngresar.setOnAction(e -> {
            if (txtCorreo.getText().trim().isEmpty() || txtPass.getText().trim().isEmpty()) {
                lblError.setText("Por favor ingresa correo y contraseña");
            } else {
                mostrarCatalogo();
            }
        });

        tarjeta.getChildren().addAll(lblMarca, lblTitulo, txtCorreo, txtPass, btnIngresar, lblError);

        StackPane fondo = new StackPane(tarjeta);
        fondo.setStyle("-fx-background-color: #FFFFFF;");

        Scene scene = new Scene(fondo, 980, 660);
        stagePrincipal.setTitle("Campo y Cosecha - Iniciar Sesión");
        stagePrincipal.setScene(scene);
        stagePrincipal.show();
    }

    // ==================== PANTALLA 2: CATÁLOGO ====================
    private void mostrarCatalogo() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FFFFFF;");

        HBox barraSuperior = new HBox(25);
        barraSuperior.setAlignment(Pos.CENTER_LEFT);
        barraSuperior.setPadding(new Insets(14, 30, 14, 30));
        barraSuperior.setStyle("-fx-background-color: #1A1A1A;");

        Label lblLogo = new Label("Campo y Cosecha");
        lblLogo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        lblLogo.setTextFill(Color.web("#2E7D32"));

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar insumos, maquinaria...");
        txtBuscar.setPrefWidth(380);
        txtBuscar.setPrefHeight(36);
        txtBuscar.setStyle("-fx-background-radius: 18; -fx-padding: 0 15 0 15;");
        txtBuscar.textProperty().addListener((obs, viejo, nuevo) -> renderizarProductos(nuevo));

        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        btnCarrito = new Button("🛒 Carrito (" + carrito.size() + ")");
        btnCarrito.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        btnCarrito.setStyle("-fx-background-color: white; -fx-background-radius: 16; -fx-padding: 8 18 8 18; -fx-cursor: hand;");
        btnCarrito.setOnAction(e -> abrirDetalleCarrito());

        Button btnSalir = new Button("Salir");
        btnSalir.setStyle("-fx-background-color: #C62828; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 14 8 14; -fx-cursor: hand;");
        btnSalir.setOnAction(e -> mostrarLogin());

        barraSuperior.getChildren().addAll(lblLogo, txtBuscar, espaciador, btnCarrito, btnSalir);
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
        stagePrincipal.setTitle("Campo y Cosecha - Catálogo de Productos");
        stagePrincipal.setScene(scene);
    }

    private void abrirDetalleCarrito() {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Carrito de Compras - Campo y Cosecha");
        if (carrito.isEmpty()) {
            alerta.setHeaderText("Tu carrito está vacío");
            alerta.setContentText("Agrega insumos desde el catálogo para verlos aquí.");
        } else {
            StringBuilder detalle = new StringBuilder();
            double total = 0;
            for (int i = 0; i < carrito.size(); i++) {
                ItemCatalogo item = carrito.get(i);
                detalle.append(i + 1).append(". ").append(item.nombre)
                       .append(" — ").append(formatearCOP(item.precio)).append("\n");
                total += item.precio;
            }
            detalle.append("\nTotal a pagar: ").append(formatearCOP(total));
            alerta.setHeaderText("Productos en tu carrito (" + carrito.size() + ")");
            alerta.setContentText(detalle.toString());
        }
        alerta.showAndWait();
    }

    private void renderizarProductos(String filtro) {
        gridProductos.getChildren().clear();
        String texto = filtro == null ? "" : filtro.trim().toLowerCase();

        int col = 0;
        int fila = 0;

        for (ItemCatalogo item : productos) {
            if (!texto.isEmpty() && !item.nombre.toLowerCase().contains(texto)) {
                continue;
            }

            VBox tarjeta = new VBox(8);
            tarjeta.setAlignment(Pos.CENTER_LEFT);
            tarjeta.setPadding(new Insets(12));
            tarjeta.setPrefWidth(220);

            ImageView imgView = new ImageView();
            imgView.setFitWidth(150);
            imgView.setFitHeight(150);
            imgView.setPreserveRatio(true);

            var stream = getClass().getResourceAsStream(item.rutaImagen);
            if (stream != null) {
                imgView.setImage(new Image(stream));
            }

            StackPane contenedorImg = new StackPane(imgView);
            contenedorImg.setPrefSize(170, 160);

            Label lblNombre = new Label(item.nombre);
            lblNombre.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

            Label lblPrecio = new Label("Precio: " + formatearCOP(item.precio));
            lblPrecio.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
            lblPrecio.setTextFill(Color.web("#2E7D32"));

            Label lblStock = new Label("Stock: " + item.stock + " disp.");
            lblStock.setFont(Font.font("Segoe UI", 11));
            lblStock.setTextFill(Color.GRAY);

            Button btnAgregar = new Button("Agregar al carrito");
            btnAgregar.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
            btnAgregar.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 7 16 7 16; -fx-cursor: hand;");
            btnAgregar.setOnAction(e -> {
                carrito.add(item);
                btnCarrito.setText("🛒 Carrito (" + carrito.size() + ")");
            });

            tarjeta.getChildren().addAll(contenedorImg, lblNombre, lblPrecio, lblStock, btnAgregar);
            gridProductos.add(tarjeta, col, fila);

            col++;
            if (col == 3) {
                col = 0;
                fila++;
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}