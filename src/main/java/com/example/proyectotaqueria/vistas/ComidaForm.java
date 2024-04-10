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
import javafx.stage.Stage;

import java.sql.SQLException;

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

        fillForm();

        btnSave = new Button("Guardar");
        btnSave.setOnAction(event -> {
            try {
                saveComida();
            } catch (SQLException e) {
                System.err.println("Error al guardar la comida en la base de datos: " + e.getMessage());
            }
        });
        vBox.getChildren().add(btnSave);

        scene = new Scene(vBox, 200, 200);
    }

    private void fillForm() {
        // No se necesita llenar el formulario para la inserción de una nueva comida.
    }

    private void saveComida() throws SQLException {
        objComida.setNombre(txtNombre.getText());
        objComida.setPrecio(Float.parseFloat(txtPrecio.getText()));

        if (cmbCategoria.getValue() == null) {
            System.err.println("No se ha seleccionado ninguna categoría.");
            return; // Salir del método si no se ha seleccionado ninguna categoría
        }

        objComida.setIdCategoria(cmbCategoria.getValue().getIdCategoria());

        objComida.insertar(); // Llamamos al método de inserción directamente

        System.out.println("Comida insertada correctamente.");

        tblComidas.setItems(objComida.consultar());
        tblComidas.refresh();

        txtNombre.clear();
        txtPrecio.clear();
        cmbCategoria.getSelectionModel().clearSelection();
    }
}

