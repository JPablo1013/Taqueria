package com.example.proyectotaqueria.vistas;

import com.example.proyectotaqueria.modelos.CategoriaDAO;
import com.example.proyectotaqueria.modelos.ComidaDAO;
import com.example.proyectotaqueria.modelos.BebidaDAO; // Importa la clase BebidaDAO
import com.example.proyectotaqueria.modelos.Conexion;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

public class ComidaTaqueria extends Pane {
    private Panel pnlPrincipal;
    private BorderPane bpnPrincipal;
    private VBox vbxPrincipal;
    private ToolBar tblMenu;
    private Scene escena;
    private TableView<ComidaDAO> tblComidas;
    private TableView<CategoriaDAO> tblCategorias;
    private TableView<BebidaDAO> tblBebidas;
    private TextField txtNombreComida;
    private TextField txtPrecioComida;
    private ComboBox<CategoriaDAO> cmbCategorias;
    private Button btnAddComida;
    private Button btnAddCategoria;
    private Button btnAddBebida; // Agrega el nuevo botón para añadir bebidas

    public ComidaTaqueria() {
        CrearUI();
    }

    private void CrearUI() {
        // Establecer la conexión a la base de datos
        Conexion.crearConexion();

        ImageView imvComida = new ImageView(getClass().getResource("/images/employee.png").toString());
        imvComida.setFitHeight(50);
        imvComida.setFitWidth(50);

        btnAddComida = new Button();
        btnAddComida.setOnAction(event -> guardarComida());
        btnAddComida.setPrefSize(50, 50);
        btnAddComida.setGraphic(imvComida);

        // Botón para agregar categoría
        ImageView imvCategoria = new ImageView(getClass().getResource("/images/employee.png").toString());
        imvCategoria.setFitHeight(50);
        imvCategoria.setFitWidth(50);

        btnAddCategoria = new Button();
        btnAddCategoria.setOnAction(event -> new CategoriaForm(tblCategorias, null));
        btnAddCategoria.setPrefSize(50, 50);
        btnAddCategoria.setGraphic(imvCategoria);

        // Botón para agregar bebida
        ImageView imvBebida = new ImageView(getClass().getResource("/images/employee.png").toString());
        imvBebida.setFitHeight(50);
        imvBebida.setFitWidth(50);

        btnAddBebida = new Button();
        btnAddBebida.setOnAction(event -> new BebidaForm(tblBebidas, null)); // Llama a la clase BebidaForm
        btnAddBebida.setPrefSize(50, 50);
        btnAddBebida.setGraphic(imvBebida);

        tblMenu = new ToolBar(btnAddComida, btnAddCategoria, btnAddBebida); // Agrega el nuevo botón al menú

        CreateTable();

        bpnPrincipal = new BorderPane();
        bpnPrincipal.setTop(tblMenu);

        // Crear tabla para categorías
        tblCategorias = new TableView<>();

        TableColumn<CategoriaDAO, String> tbcCategoria = new TableColumn<>("Categoría");
        tbcCategoria.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        tblCategorias.getColumns().addAll(tbcCategoria);
        bpnPrincipal.setLeft(tblCategorias);

        // Crear tabla para bebidas
        tblBebidas = new TableView<>();

        TableColumn<BebidaDAO, String> tbcNombreBebida = new TableColumn<>("Nombre");
        tbcNombreBebida.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<BebidaDAO, Double> tbcPrecioBebida = new TableColumn<>("Precio");
        tbcPrecioBebida.setCellValueFactory(new PropertyValueFactory<>("precio"));

        tblBebidas.getColumns().addAll(tbcNombreBebida, tbcPrecioBebida);

        // Ajusta el tamaño de la tabla de bebidas
        tblBebidas.setPrefWidth(200);

        bpnPrincipal.setRight(tblBebidas);

        bpnPrincipal.setCenter(tblComidas);

        pnlPrincipal = new Panel("Taqueria");
        pnlPrincipal.setBody(bpnPrincipal);
        pnlPrincipal.getStyleClass().add("panel-info");

        escena = new Scene(pnlPrincipal, 600, 400);
        escena.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        this.getChildren().add(pnlPrincipal);

        cargarCategorias();
        cargarBebidas();
    }

    private void CreateTable() {
        ComidaDAO objComida = new ComidaDAO();
        tblComidas = new TableView<>();

        TableColumn<ComidaDAO, String> tbcNombre = new TableColumn<>("Nombre");
        tbcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<ComidaDAO, String> tbcPrecio = new TableColumn<>("Precio");
        tbcPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        TableColumn<ComidaDAO, Integer> tbcCategoria = new TableColumn<>("Categoría");
        tbcCategoria.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));

        tblComidas.getColumns().addAll(tbcNombre, tbcPrecio, tbcCategoria);
        tblComidas.setItems(objComida.consultar());
    }

    private void cargarCategorias() {
        // Cargar las categorías desde la base de datos utilizando CategoriaDAO
        CategoriaDAO categoria = new CategoriaDAO();
        ObservableList<CategoriaDAO> categorias = categoria.consultar();
        if (categorias != null) {
            tblCategorias.setItems(categorias);
        }
    }

    private void cargarBebidas() {
        // Cargar las bebidas desde la base de datos utilizando BebidaDAO
        BebidaDAO bebida = new BebidaDAO();
        ObservableList<BebidaDAO> bebidas = bebida.consultar();
        if (bebidas != null) {
            tblBebidas.setItems(bebidas);
        }
    }

    private void guardarComida() {
        // Crear una instancia de ComidaForm y pasar la tabla de comidas y la lista de categorías
        ComidaForm comidaForm = new ComidaForm(tblComidas, null, tblCategorias.getItems());
    }
}
