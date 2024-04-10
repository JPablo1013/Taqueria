package com.example.proyectotaqueria.vistas;

import com.example.proyectotaqueria.modelos.CategoriaDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CategoriaForm extends Stage {
    private TableView<CategoriaDAO> tblCategorias;
    private CategoriaDAO objCategoria;
    private String[] prompts = {"Nombre de la categoría"};
    private Scene scene;
    private TextField txtNombre;
    private Button btnSave;
    private VBox vBox;

    public CategoriaForm(TableView<CategoriaDAO> tableView, CategoriaDAO categoria) {
        tblCategorias = tableView;
        this.objCategoria = (categoria == null) ? new CategoriaDAO() : categoria;
        createUI();
        this.setTitle("Inserciones de Categoría");
        this.setScene(scene);
        this.show();
    }

    private void createUI() {
        vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);

        txtNombre = new TextField();
        txtNombre.setPromptText(prompts[0]);
        vBox.getChildren().add(txtNombre);

        fillForm();

        btnSave = new Button("Guardar");
        btnSave.setOnAction(event -> saveCategoria());
        vBox.getChildren().add(btnSave);

        scene = new Scene(vBox, 200, 200);
    }

    private void fillForm() {
        txtNombre.setText(objCategoria.getNombre());
    }

    private void saveCategoria() {
        objCategoria.setNombre(txtNombre.getText());
        objCategoria.insertar(); // Llamamos al método de inserción directamente
        tblCategorias.setItems(objCategoria.consultar());
        tblCategorias.refresh();
        txtNombre.clear();
    }
}
