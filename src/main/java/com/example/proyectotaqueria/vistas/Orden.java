package com.example.proyectotaqueria.vistas;

import com.example.proyectotaqueria.modelos.CategoriaDAO;
import com.example.proyectotaqueria.modelos.ComidaDAO;
import com.example.proyectotaqueria.modelos.OrdenTemporal;
import com.example.proyectotaqueria.modelos.OrdenesManager;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Orden extends Stage {
    private TableView<OrdenTemporal> tableView;
    private int numeroMesa;
    private int idEmpleado;

    public Orden(int numeroMesa, int idEmpleado, ObservableList<OrdenTemporal> listaOrdenesTemporales) {
        this.numeroMesa = numeroMesa;
        this.idEmpleado = idEmpleado;
        crearUI(listaOrdenesTemporales);
        this.setTitle("Órdenes de Mesa " + numeroMesa);
    }

    private void crearUI(ObservableList<OrdenTemporal> listaOrdenesTemporales) {
        tableView = new TableView<>();
        TableColumn<OrdenTemporal, String> comidaColumna = new TableColumn<>("Comida");
        TableColumn<OrdenTemporal, Float> precioColumna = new TableColumn<>("Precio");
        TableColumn<OrdenTemporal, Integer> cantidadColumna = new TableColumn<>("Cantidad");

        comidaColumna.setCellValueFactory(data -> data.getValue().nombreComidaProperty());
        precioColumna.setCellValueFactory(data -> data.getValue().precioProperty().asObject());
        cantidadColumna.setCellValueFactory(data -> data.getValue().cantidadProperty().asObject());

        tableView.getColumns().addAll(comidaColumna, precioColumna, cantidadColumna);
        tableView.setItems(listaOrdenesTemporales);

        TabPane tabPane = new TabPane();
        tabPane.setPadding(new Insets(20));

        CategoriaDAO categoriaDAO = new CategoriaDAO();
        ObservableList<CategoriaDAO> listaCategorias = categoriaDAO.consultar();

        for (CategoriaDAO categoria : listaCategorias) {
            Tab tab = new Tab(categoria.getNombre());
            tab.setContent(crearContenidoPestana(categoria.getNombre()));
            tabPane.getTabs().add(tab);
        }

        Button botonAceptar = new Button("Aceptar");
        botonAceptar.setOnAction(event -> aceptarOrden());

// Cambiamos el nombre de la variable en este evento
        this.setOnCloseRequest(event -> {
            ObservableList<OrdenTemporal> ordenesTemporales = tableView.getItems();
            if (!ordenesTemporales.isEmpty()) {
                OrdenTemporal ultimaOrden = ordenesTemporales.get(ordenesTemporales.size() - 1);
                // Realizar alguna acción con la última orden temporal, por ejemplo:
                System.out.println("La última orden temporal es: " + ultimaOrden);
            }
        });

        VBox vBox = new VBox(tableView, tabPane, botonAceptar);
        Scene scene = new Scene(vBox);
        this.setScene(scene);
    }

    private void aceptarOrden() {
        try {
            // Guardar las órdenes temporales en el OrdenesManager
            for (OrdenTemporal ordenTemporal : tableView.getItems()) {
                OrdenesManager.getInstance().insertarOrdenTemporal(ordenTemporal);
            }
            // Mostrar mensaje de éxito
            mostrarMensaje("Órdenes guardadas con éxito");

            // Cerrar la ventana de órdenes
            this.close();
        } catch (Exception e) {
            mostrarError("Error al guardar las órdenes", e.getMessage());
        }
    }


    public OrdenTemporal getOrdenTemporal() {
        // Devuelve la última orden temporal agregada a la tabla (si hay alguna)
        ObservableList<OrdenTemporal> ordenesTemporales = tableView.getItems();
        int size = ordenesTemporales.size();
        return size > 0 ? ordenesTemporales.get(size - 1) : null;
    }


    private GridPane crearContenidoPestana(String nombreCategoria) {
        GridPane contenidoPestana = new GridPane();
        contenidoPestana.setAlignment(Pos.CENTER);
        contenidoPestana.setHgap(10);
        contenidoPestana.setVgap(10);
        contenidoPestana.setPadding(new Insets(20));

        ComidaDAO comidaDAO = new ComidaDAO();
        ObservableList<ComidaDAO> listaComidas = comidaDAO.consultarPorCategoria(nombreCategoria);

        int fila = 0;
        int columna = 0;
        for (ComidaDAO comida : listaComidas) {
            Button botonComida = new Button(comida.getNombre());
            botonComida.setOnAction(event -> {
                OrdenTemporal ordenTemporal = new OrdenTemporal(idEmpleado, comida, comida.getPrecio(), 1, numeroMesa);
                OrdenesManager.getInstance().insertarOrdenTemporal(ordenTemporal);
                tableView.getItems().add(ordenTemporal); // Agregar la orden a la tabla
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

    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

