package com.example.proyectotaqueria.vistas;

import com.example.proyectotaqueria.modelos.Conexion;
import com.example.proyectotaqueria.modelos.MesaDAO;
import com.example.proyectotaqueria.modelos.OrdenTemporal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Mesas extends Pane {
    private Map<Integer, ObservableList<OrdenTemporal>> ordenesPorMesa;
    private Map<Integer, Button> botonesMesas;

    public Mesas() {
        ordenesPorMesa = new HashMap<>();
        botonesMesas = new HashMap<>();
        Conexion.crearConexion(); // Inicializar la conexión a la base de datos
        crearUI(); // Crear la interfaz de usuario
    }

    private void crearUI() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));

        try {
            ObservableList<MesaDAO> listaMesas = new MesaDAO().consultar();

            int numFilas = 5;
            int numColumnas = 5;
            for (MesaDAO mesa : listaMesas) {
                Button botonMesa = new Button("Mesa " + mesa.getNoMesa());
                botonMesa.setPrefSize(80, 80);
                botonMesa.setStyle("-fx-background-color: " + (mesa.isOcupada() ? "red" : "green"));
                botonMesa.setOnAction(event -> mesaSeleccionada(mesa.getNoMesa()));

                botonesMesas.put(mesa.getNoMesa(), botonMesa);
                ordenesPorMesa.put(mesa.getNoMesa(), FXCollections.observableArrayList());

                gridPane.add(botonMesa, (mesa.getNoMesa() - 1) % numColumnas, (mesa.getNoMesa() - 1) / numFilas);
            }
        } catch (Exception e) {
            mostrarError("Error al cargar las mesas", e.getMessage());
        }

        HBox hBox = new HBox(gridPane);
        hBox.setAlignment(Pos.CENTER);
        getChildren().add(hBox);
    }

    private void mesaSeleccionada(int numeroMesa) {
        abrirOrdenes(numeroMesa);
    }

    private void abrirOrdenes(int numeroMesa) {
        Stage ventanaOrdenes = new Stage();
        ventanaOrdenes.initModality(Modality.APPLICATION_MODAL);
        ventanaOrdenes.setTitle("Órdenes");

        try {
            ObservableList<OrdenTemporal> listaOrdenesTemporales = ordenesPorMesa.get(numeroMesa);
            int idEmpleado = 4; // Aquí debes obtener el ID del empleado actual
            Orden orden = new Orden(numeroMesa, idEmpleado, listaOrdenesTemporales);
            orden.initOwner(getScene().getWindow()); // Establecer la ventana de Mesas como propietaria de la ventana Orden

            orden.showAndWait(); // Mostrar la ventana de órdenes y esperar a que se cierre

            // Actualizar el color del botón de la mesa después de cerrar la ventana de órdenes
            actualizarBotonMesa(numeroMesa);
        } catch (Exception e) {
            mostrarError("Error al abrir las órdenes", e.getMessage());
        }
    }

    private void actualizarBotonMesa(int numeroMesa) {
        Button botonMesa = botonesMesas.get(numeroMesa);
        if (botonMesa != null) {
            botonMesa.setStyle("-fx-background-color: green"); // Cambiar el color del botón a verde
        }
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

