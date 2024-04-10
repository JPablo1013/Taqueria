package com.example.proyectotaqueria.vistas;

import com.example.proyectotaqueria.modelos.MesaDAO;
import com.example.proyectotaqueria.modelos.Conexion;
import com.example.proyectotaqueria.modelos.OrdenDAO;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class Mesas extends Pane {

    private OrdenDAO ordenDAO; // Instancia de la clase OrdenDAO
    private int idEmpleado; // Id del empleado actual (necesario para la inserción en la tabla)

    public Mesas(int idEmpleado) { // Recibe el id del empleado como parámetro
        this.idEmpleado = idEmpleado;
        CrearUI();
    }

    private void CrearUI() {
        Conexion.crearConexion();
        ordenDAO = new OrdenDAO(); // Inicializar la instancia de la clase OrdenDAO

        // Crear un GridPane para organizar los botones de las mesas
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));

        // Obtener la lista de mesas desde la base de datos
        ObservableList<MesaDAO> listaMesas = new MesaDAO().consultar();

        // Crear y agregar botones de las mesas al GridPane
        int numFilas = 5;
        int numColumnas = 5;
        for (MesaDAO mesa : listaMesas) {
            Button botonMesa = new Button("Mesa " + mesa.getNoMesa());
            botonMesa.setPrefSize(80, 80);
            botonMesa.setStyle("-fx-background-color: " + (mesa.isOcupada() ? "red" : "green"));

            // Modifica la acción del botón para abrir una nueva ventana con la clase Ordenes
            botonMesa.setOnAction(event -> abrirOrdenes());

            gridPane.add(botonMesa, (mesa.getNoMesa() - 1) % numColumnas, (mesa.getNoMesa() - 1) / numFilas);
        }

        // Organizar elementos en un HBox
        HBox hBox = new HBox(gridPane);
        hBox.setAlignment(Pos.CENTER);

        // Establecer el HBox como contenido de este Pane
        getChildren().add(hBox);
    }

    private void abrirOrdenes() {
        Orden orden = new Orden();
        orden.show(); // Mostrar la instancia de Orden
    }

}
