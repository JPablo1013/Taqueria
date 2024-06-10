package com.example.proyectotaqueria.vistas;

import com.example.proyectotaqueria.modelos.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Mesas extends Pane {
    protected Map<Integer, ObservableList<OrdenTemporal>> ordenesPorMesa;
    protected Map<Integer, Button> botonesMesas;

    public Mesas() {
        ordenesPorMesa = new HashMap<>();
        botonesMesas = new HashMap<>();
        Conexion.crearConexion(); // Inicializar la conexión a la base de datos
        //solicitarNombreEmpleado(); // Solicitar el nombre del empleado y obtener su ID
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
        // Cambiar el color del botón de mesa a rojo cuando se selecciona la mesa
        Button botonMesa = botonesMesas.get(numeroMesa);
        if (botonMesa != null) {
            botonMesa.setStyle("-fx-background-color: red");
        }
    }

    private void abrirOrdenes(int numeroMesa) {
        Stage ventanaOrdenes = new Stage();
        ventanaOrdenes.initModality(Modality.APPLICATION_MODAL);
        ventanaOrdenes.setTitle("Órdenes");

        boolean empleadoValido = false;
        int idEmpleado = -1;

        while (!empleadoValido) {
            // Solicitar el nombre del empleado
            String nombreEmpleado = obtenerNombreEmpleado();

            // Obtener el ID del empleado utilizando su nombre
            idEmpleado = obtenerIdEmpleado(nombreEmpleado);

            // Verificar si el ID del empleado es válido
            if (idEmpleado != -1) {
                empleadoValido = true;
            }
        }

        try {
            ObservableList<OrdenTemporal> listaOrdenesTemporales = ordenesPorMesa.get(numeroMesa);
            ComidaDAO comida = new ComidaDAO(); // Instancia vacía de ComidaDAO
            Orden orden = new Orden(numeroMesa, idEmpleado, listaOrdenesTemporales, comida, botonesMesas);
            orden.initOwner(getScene().getWindow());

            orden.showAndWait();

            // Actualizar el color del botón de la mesa después de cerrar la ventana de órdenes
            actualizarBotonMesa(numeroMesa);
        } catch (Exception e) {
            mostrarError("Error al abrir las órdenes", e.getMessage());
        }
    }

    private int obtenerIdEmpleado(String nombreEmpleado) {
        EmpleadosDAO empleadosDAO = new EmpleadosDAO();
        int idEmpleado = empleadosDAO.getIdEmpleadoPorNombre(nombreEmpleado);
        if (idEmpleado == -1) {
            // Si el ID devuelto es -1, significa que el nombre del empleado no es válido.
            // No necesitas mostrar un mensaje de error aquí, simplemente devuelve -1.
            return -1;
        }
        return idEmpleado;
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

    private String obtenerNombreEmpleado() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nombre del Empleado");
        dialog.setHeaderText("Por favor, ingresa tu nombre:");

        Optional<String> result = dialog.showAndWait();
        return result.orElse(""); // Si el usuario no ingresa ningún nombre, se devuelve una cadena vacía
    }
}
