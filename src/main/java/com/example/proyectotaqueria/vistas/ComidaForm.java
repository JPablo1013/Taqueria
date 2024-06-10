package com.example.proyectotaqueria.vistas;

import com.example.proyectotaqueria.modelos.CategoriaDAO;
import com.example.proyectotaqueria.modelos.ComidaDAO;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.UUID;

public class ComidaForm extends Stage {
    private TableView<ComidaDAO> tblComidas;
    private ComidaDAO objComida;
    private ObservableList<CategoriaDAO> categorias;
    private String[] prompts = {"Nombre del platillo", "Categoría", "Precio"};
    private Scene scene;
    private TextField txtNombre;
    private TextField txtPrecio;
    private ComboBox<CategoriaDAO> cmbCategoria;
    private Button btnSave;
    private VBox vBox;

    private Button btnSeleccionarImagen;
    private File imagenSeleccionada;

    public ComidaForm(TableView<ComidaDAO> tableView, ComidaDAO comida, ObservableList<CategoriaDAO> categorias) {
        tblComidas = tableView;
        this.objComida = (comida == null) ? new ComidaDAO() : comida;
        if (categorias != null) {
            this.categorias = categorias;
            createUI();
            this.setTitle("Inserciones de Comida");
            this.setScene(scene);
            this.show();
        } else {
            System.err.println("La lista de categorías es nula.");
        }
    }

    private void createUI() {
        vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);

        txtNombre = new TextField();
        txtNombre.setPromptText(prompts[0]);
        vBox.getChildren().add(txtNombre);

        cmbCategoria = new ComboBox<>();
        cmbCategoria.setPromptText(prompts[1]);
        cmbCategoria.setItems(categorias);
        vBox.getChildren().add(cmbCategoria);

        txtPrecio = new TextField();
        txtPrecio.setPromptText(prompts[2]);
        vBox.getChildren().add(txtPrecio);

        btnSeleccionarImagen = new Button("Seleccionar Imagen");
        btnSeleccionarImagen.setOnAction(event -> seleccionarImagen());
        vBox.getChildren().add(btnSeleccionarImagen);

        fillForm();

        btnSave = new Button("Guardar");
        btnSave.setOnAction(event -> {
            try {
                saveComida();
            } catch (SQLException | IOException e) {
                System.err.println("Error al guardar la comida en la base de datos: " + e.getMessage());
            }
        });
        vBox.getChildren().add(btnSave);

        scene = new Scene(vBox, 200, 200);
    }

    private void fillForm() {
        // No se necesita llenar el formulario para la inserción de una nueva comida.
    }

    private void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );
        imagenSeleccionada = fileChooser.showOpenDialog(this);

        if (imagenSeleccionada != null) {
            System.out.println("Imagen seleccionada: " + imagenSeleccionada.getAbsolutePath());
        } else {
            System.out.println("No se seleccionó ninguna imagen.");
        }
    }

    private void saveComida() throws SQLException, IOException {
        objComida.setNombre(txtNombre.getText());
        objComida.setPrecio(Float.parseFloat(txtPrecio.getText()));

        if (cmbCategoria.getValue() == null) {
            System.err.println("No se ha seleccionado ninguna categoría.");
            return; // Salir del método si no se ha seleccionado ninguna categoría
        }

        if (imagenSeleccionada != null) {
            // Generar un nombre único para la imagen
            String nombreImagen = UUID.randomUUID().toString() + ".jpg";
            // Obtener la ruta de destino donde se guardará la imagen en la carpeta /images
            String rutaDestino = "C:\\Users\\Pillin\\IdeaProjects\\proyectoTaqueria\\src\\main\\resources\\images\\" + nombreImagen;
            // Copiar el archivo de la imagen seleccionada a la carpeta de destino
            Files.copy(imagenSeleccionada.toPath(), Path.of(rutaDestino));
            // Guardar el nombre de la imagen en la base de datos
            objComida.setFoto(nombreImagen);
            System.out.println("Imagen guardada en la carpeta /images y en la base de datos.");
        } else {
            System.out.println("No se ha seleccionado ninguna imagen.");
        }


        objComida.setIdCategoria(cmbCategoria.getValue().getIdCategoria());

        objComida.insertar(objComida.getFoto()); // Llamamos al método de inserción con el nombre de la imagen

        System.out.println("Comida insertada correctamente.");

        tblComidas.setItems(objComida.consultar());
        tblComidas.refresh();

        txtNombre.clear();
        txtPrecio.clear();
        cmbCategoria.getSelectionModel().clearSelection();
    }

}
