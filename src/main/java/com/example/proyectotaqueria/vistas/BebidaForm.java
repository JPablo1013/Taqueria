package com.example.proyectotaqueria.vistas;

import com.example.proyectotaqueria.modelos.BebidaDAO;
import com.example.proyectotaqueria.modelos.Conexion;
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

public class BebidaForm extends Stage {
    private TableView<BebidaDAO> tblBebidas;
    private BebidaDAO objBebida;
    private Scene scene;
    private TextField txtNombre;
    private TextField txtCantidad;
    private TextField txtPrecio;
    private Button btnSave;
    private VBox vBox;

    public BebidaForm(TableView<BebidaDAO> tableView, BebidaDAO bebida) {
        tblBebidas = tableView;
        this.objBebida = (bebida == null) ? new BebidaDAO() : bebida;
        createUI();
        this.setTitle("Inserción de Bebida");
        this.setScene(scene);
        this.show();
    }

    private void createUI() {
        vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);

        txtNombre = new TextField();
        txtNombre.setPromptText("Nombre de la bebida");
        vBox.getChildren().add(txtNombre);

        txtCantidad = new TextField();
        txtCantidad.setPromptText("Cantidad");
        vBox.getChildren().add(txtCantidad);

        txtPrecio = new TextField();
        txtPrecio.setPromptText("Precio");
        vBox.getChildren().add(txtPrecio);

        fillForm();

        btnSave = new Button("Guardar");
        btnSave.setOnAction(event -> {
            try {
                saveBebida();
            } catch (SQLException e) {
                System.err.println("Error al guardar la bebida en la base de datos: " + e.getMessage());
            }
        });
        vBox.getChildren().add(btnSave);

        scene = new Scene(vBox, 200, 200);
    }

    private void fillForm() {
        // No se necesita llenar el formulario para la inserción de una nueva bebida.
    }

    private void saveBebida() throws SQLException {
        objBebida.setNombre(txtNombre.getText());
        objBebida.setCantidad(Integer.parseInt(txtCantidad.getText()));
        objBebida.setPrecio(Float.parseFloat(txtPrecio.getText()));

        objBebida.insertar(); // Llamamos al método de inserción directamente

        System.out.println("Bebida insertada correctamente.");

        tblBebidas.setItems(objBebida.consultar());
        tblBebidas.refresh();

        txtNombre.clear();
        txtCantidad.clear();
        txtPrecio.clear();
    }
}
