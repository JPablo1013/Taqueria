package com.example.proyectotaqueria.vistas;

import com.example.proyectotaqueria.modelos.CategoriaDAO;
import com.example.proyectotaqueria.modelos.ComidaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Orden extends Stage {

    private TableView<String> tableView; // Tabla para mostrar la información

    public Orden() {
        CrearUI();
        this.setTitle("Inserciones de Usuario");
        // No necesitas mostrar la ventana aquí, se mostrará después de configurar la escena
    }

    private void CrearUI() {
        // Crear TableView para mostrar información adicional
        tableView = new TableView<>();
        TableColumn<String, String> comidaColumna = new TableColumn<>("Comida");
        TableColumn<String, String> precioColumna = new TableColumn<>("Precio");
        TableColumn<String, String> cantidadColumna = new TableColumn<>("Cantidad");

        // Configurar las celdas de la tabla
        comidaColumna.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        precioColumna.setCellValueFactory(data -> new SimpleStringProperty("<vacío>"));
        cantidadColumna.setCellValueFactory(data -> new SimpleStringProperty("<vacío>"));

        // Agregar las columnas a la tabla
        tableView.getColumns().addAll(comidaColumna, precioColumna, cantidadColumna);

        // Crear un TabPane para organizar las pestañas
        TabPane tabPane = new TabPane();
        tabPane.setPadding(new Insets(20));

        // Obtener la lista de categorías desde la base de datos
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        ObservableList<CategoriaDAO> listaCategorias = categoriaDAO.consultar();

        // Crear una pestaña para cada categoría
        for (CategoriaDAO categoria : listaCategorias) {
            Tab tab = new Tab(categoria.getNombre());
            tab.setContent(crearContenidoPestana(categoria.getNombre()));
            tabPane.getTabs().add(tab);
        }

        // Organizar elementos en un VBox final
        VBox vBox = new VBox(tableView, tabPane);
        vBox.setAlignment(Pos.CENTER);

        // Crear la escena y establecerla en la ventana
        Scene escena = new Scene(vBox);
        this.setScene(escena);
    }

    // Método para crear el contenido de cada pestaña
    private GridPane crearContenidoPestana(String nombreCategoria) {
        GridPane contenidoPestana = new GridPane();
        contenidoPestana.setAlignment(Pos.CENTER);
        contenidoPestana.setHgap(10);
        contenidoPestana.setVgap(10);
        contenidoPestana.setPadding(new Insets(20));

        // Obtener la lista de comidas para la categoría seleccionada
        ComidaDAO comidaDAO = new ComidaDAO();
        ObservableList<ComidaDAO> listaComidas = comidaDAO.consultarPorCategoria(nombreCategoria);

        // Crear y agregar botones a la pestaña
        int fila = 0;
        int columna = 0;
        for (ComidaDAO comida : listaComidas) {
            Button botonComida = new Button(comida.getNombre());
            botonComida.setOnAction(event -> {
                // Aquí puedes agregar la lógica para agregar la comida a la tabla y a la base de datos si es necesario
            });
            contenidoPestana.add(botonComida, columna, fila);
            columna++;
            if (columna == 3) {
                columna = 0;
                fila++;
            }
        }

        return contenidoPestana;
    }
}
