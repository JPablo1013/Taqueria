package com.example.proyectotaqueria.vistas;

import com.example.proyectotaqueria.modelos.*;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Orden extends Stage {
    private TableView<OrdenTemporal> tableView;
    private int numeroMesa;
    private int idEmpleado;
    private int idPago;
    private ComidaDAO comidaDAO = new ComidaDAO();
    private BebidaDAO bebidaDAO = new BebidaDAO();

    public Orden(int numeroMesa, int idEmpleado, ObservableList<OrdenTemporal> listaOrdenesTemporales, ComidaDAO comida) {
        OrdenTemporal ordenTemporal = new OrdenTemporal(idEmpleado, comida, comida.getPrecio(), 1, numeroMesa);
        ordenTemporal.setIdComida(comida.getIdComida());
        ordenTemporal.setIdBebida(0); // No hay bebida asociada, entonces se establece como 0

        this.numeroMesa = numeroMesa;
        this.idEmpleado = idEmpleado;
        crearUI(listaOrdenesTemporales);
        this.setTitle("Órdenes de Mesa " + numeroMesa);
    }


    private void crearUI(ObservableList<OrdenTemporal> listaOrdenesTemporales) {
        Conexion.crearConexion();

        tableView = new TableView<>();
        TableColumn<OrdenTemporal, String> comidaColumna = new TableColumn<>("Comida");
        TableColumn<OrdenTemporal, Float> precioColumna = new TableColumn<>("Precio");
        TableColumn<OrdenTemporal, Integer> cantidadColumna = new TableColumn<>("Cantidad");

        comidaColumna.setCellValueFactory(data -> data.getValue().nombreItemProperty());
        precioColumna.setCellValueFactory(data -> data.getValue().precioProperty().asObject());
        cantidadColumna.setCellValueFactory(data -> data.getValue().cantidadProperty().asObject());

        tableView.getColumns().addAll(comidaColumna, precioColumna, cantidadColumna);
        tableView.setItems(listaOrdenesTemporales);

        TabPane tabPane = new TabPane();
        tabPane.setPadding(new Insets(20));

        // Crear pestaña para bebidas
        Tab tabBebidas = new Tab("Bebidas");
        tabBebidas.setContent(crearContenidoPestana("Bebida"));
        tabPane.getTabs().add(tabBebidas);

        CategoriaDAO categoriaDAO = new CategoriaDAO();
        ObservableList<CategoriaDAO> listaCategorias = categoriaDAO.consultar();

        for (CategoriaDAO categoria : listaCategorias) {
            Tab tab = new Tab(categoria.getNombre());
            tab.setContent(crearContenidoPestana(categoria.getNombre()));
            tabPane.getTabs().add(tab);
        }

        Button botonAceptar = new Button("Aceptar");
        botonAceptar.setOnAction(event -> aceptarOrden());

        this.setOnCloseRequest(event -> {
            ObservableList<OrdenTemporal> ordenesTemporales = tableView.getItems();
            if (!ordenesTemporales.isEmpty()) {
                OrdenTemporal ultimaOrden = ordenesTemporales.get(ordenesTemporales.size() - 1);
                System.out.println("ID de comida: " + ultimaOrden.getIdComida());
                System.out.println("ID de bebida: " + ultimaOrden.getIdBebida());
            }
        });

        Button botonPagar = new Button("Pagar");
        botonPagar.setOnAction(event -> pagarOrden());
        VBox vBox = new VBox(tableView, tabPane, botonAceptar, botonPagar);
        Scene scene = new Scene(vBox);
        this.setScene(scene);
    }

    private void pagarOrden() {
        Stage ventanaPago = new Stage();
        ventanaPago.initModality(Modality.APPLICATION_MODAL);
        ventanaPago.setTitle("Pagar Orden");

        ComboBox<String> metodoPagoComboBox = new ComboBox<>();
        metodoPagoComboBox.setPromptText("Seleccione Método de Pago");

        TipoPagoDAO tipoPagoDAO = new TipoPagoDAO();
        ObservableList<TipoPagoDAO> tiposPago = tipoPagoDAO.consultar();
        for (TipoPagoDAO tipoPago : tiposPago) {
            metodoPagoComboBox.getItems().add(tipoPago.getNombre());
        }

        TextField cantidadField = new TextField();
        Button botonConfirmar = new Button("Confirmar Pago");

        botonConfirmar.setOnAction(event -> {
            String metodoPago = metodoPagoComboBox.getValue();
            String cantidad = cantidadField.getText();
            if (metodoPago != null && !metodoPago.isEmpty() && cantidad != null && !cantidad.isEmpty()) {
                confirmarPago(ventanaPago, metodoPago, cantidad);
            } else {
                mostrarError("Error", "Seleccione un método de pago y especifique la cantidad pagada.");
            }
        });

        VBox layoutPago = new VBox(10);
        layoutPago.getChildren().addAll(new Label("Método de Pago:"), metodoPagoComboBox, new Label("Cantidad:"), cantidadField, botonConfirmar);
        layoutPago.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layoutPago, 300, 200);
        ventanaPago.setScene(scene);
        ventanaPago.show();
    }

    private void confirmarPago(Stage ventanaPago, String metodoPago, String cantidad) {
        try {
            int cantidadPagada = Integer.parseInt(cantidad);

            insertarTipoPagoSiNoExiste(metodoPago);

            int idTipoPago = obtenerIdTipoPago(metodoPago);
            double total = calcularTotalOrden();
            idPago = insertarPago(idTipoPago, total, "Pago por orden en mesa " + numeroMesa);

            // Aquí capturas el ID generado
            if (idPago != 0) {
                insertarOrdenBD(idPago);
                ventanaPago.close();
                mostrarMensaje("Orden pagada con éxito");
            } else {
                mostrarError("Error al pagar la orden", "No se pudo obtener el ID de pago");
            }
        } catch (NumberFormatException e) {
            mostrarError("Error en la cantidad", "Ingrese una cantidad válida");
        } catch (Exception e) {
            mostrarError("Error al pagar la orden", e.getMessage());
        }
    }



    private void aceptarOrden() {
        try {
            for (OrdenTemporal ordenTemporal : tableView.getItems()) {
                OrdenesManager.getInstance().insertarOrdenTemporal(ordenTemporal);
            }

            idPago = 0; // Otra opción es inicializar idPago en la parte superior de la clase
            insertarOrdenBD(idPago);

            mostrarMensaje("Órdenes guardadas con éxito");

            this.close();
        } catch (Exception e) {
            mostrarError("Error al guardar las órdenes", e.getMessage());
        }
    }

    private void insertarTipoPagoSiNoExiste(String metodoPago) {
        TipoPagoDAO tipoPago = new TipoPagoDAO();
        tipoPago.setNombre(metodoPago);
        ObservableList<TipoPagoDAO> tiposPago = tipoPago.consultar();
        if (tiposPago.isEmpty()) {
            tipoPago.insertar();
        }
    }

    private int obtenerIdTipoPago(String metodoPago) {
        TipoPagoDAO tipoPago = new TipoPagoDAO();
        tipoPago.setNombre(metodoPago);
        ObservableList<TipoPagoDAO> tiposPago = tipoPago.consultar();
        return tiposPago.get(0).getIdTipoPago();
    }

    private double calcularTotalOrden() {
        ObservableList<OrdenTemporal> ordenesTemporales = tableView.getItems();
        double total = 0;
        for (OrdenTemporal ordenTemporal : ordenesTemporales) {
            total += ordenTemporal.getPrecio() * ordenTemporal.getCantidad();
        }
        return total;
    }

    private int insertarPago(int idTipoPago, double total, String descripcion) {
        try {
            PagoDAO pago = new PagoDAO();
            pago.setNombrePago("Pago de orden");
            pago.setTotal(total);
            pago.setIdTipoPago(idTipoPago);
            pago.setDescripcion(descripcion);

            // Llamar al método insertar de PagoDAO
            return pago.insertar();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void insertarOrdenBD(int idPago) {
        ObservableList<OrdenTemporal> ordenesTemporales = tableView.getItems();
        boolean errores = false; // Bandera para controlar errores
        for (OrdenTemporal ordenTemporal : ordenesTemporales) {
            // Verificar si tanto el ID de comida como el ID de bebida son diferentes de 0
            if (ordenTemporal.getIdComida() != 0 || ordenTemporal.getIdBebida() != 0) {
                OrdenDAO ordenDAO = new OrdenDAO();
                ordenDAO.setIdEmpleado(ordenTemporal.getIdEmpleado());
                ordenDAO.setIdPago(idPago);
                ordenDAO.setFecha(LocalDate.now().toString());

                if (ordenTemporal.getItem() instanceof ComidaDAO) {
                    ComidaDAO comida = (ComidaDAO) ordenTemporal.getItem();
                    if (comidaDAO.existeComida(comida.getIdComida())) {
                        ordenDAO.setIdComida(comida.getIdComida());
                        if (ordenTemporal.getIdBebida() == 0) {
                            ordenDAO.setIdBebida(1);
                        } else {
                            ordenDAO.setIdBebida(ordenTemporal.getIdBebida());
                        }
                        ordenDAO.insertar(); // Insertar orden
                    } else {
                        // Si la comida no existe, omite esta orden y muestra un mensaje
                        errores = true;
                        mostrarError("Error al insertar orden", "La comida seleccionada no existe en la base de datos.");
                    }
                } else if (ordenTemporal.getItem() instanceof BebidaDAO) {
                    BebidaDAO bebida = (BebidaDAO) ordenTemporal.getItem();
                    if (bebidaDAO.existeBebida(bebida.getIdBebida())) {
                        if (ordenTemporal.getIdComida() == 0) {
                            ordenDAO.setIdComida(1);
                        } else {
                            ordenDAO.setIdComida(ordenTemporal.getIdComida());
                        }
                        ordenDAO.setIdBebida(bebida.getIdBebida());
                        ordenDAO.insertar(); // Insertar orden
                    } else {
                        // Si la bebida no existe, omite esta orden y muestra un mensaje
                        errores = true;
                        mostrarError("Error al insertar orden", "La bebida seleccionada no existe en la base de datos.");
                    }
                }
                System.out.println("ID de comida: " + ordenDAO.getIdComida());
                System.out.println("ID de bebida: " + ordenDAO.getIdBebida());
            }
        }

        if (!errores) {
            mostrarMensaje("Órdenes guardadas con éxito");
        }
    }


    private GridPane crearContenidoPestana(String nombreCategoria) {
        GridPane contenidoPestana = new GridPane();
        contenidoPestana.setAlignment(Pos.CENTER);
        contenidoPestana.setHgap(10);
        contenidoPestana.setVgap(10);
        contenidoPestana.setPadding(new Insets(20));

        int fila = 0;
        int columna = 0;

        if (nombreCategoria.equals("Bebida")) {
            BebidaDAO bebidaDAO = new BebidaDAO();
            ObservableList<BebidaDAO> listaBebidas = bebidaDAO.consultar();

            for (BebidaDAO bebida : listaBebidas) {
                Button botonBebida = new Button(bebida.getNombre());
                botonBebida.setOnAction(event -> {
                    BebidaDAO bebidaSeleccionada = listaBebidas.get(GridPane.getRowIndex((Button) event.getSource()));
                    OrdenTemporal ordenTemporalBebida = new OrdenTemporal(idEmpleado, bebidaSeleccionada, bebidaSeleccionada.getPrecio(), 1, numeroMesa);
                    if (bebidaDAO.existeBebida(bebidaSeleccionada.getIdBebida())) {
                        ordenTemporalBebida.setIdComida(0);
                        ordenTemporalBebida.setIdBebida(bebidaSeleccionada.getIdBebida());
                        OrdenesManager.getInstance().insertarOrdenTemporal(ordenTemporalBebida);
                        tableView.getItems().add(ordenTemporalBebida);
                    } else {
                        // Si la bebida no existe, omite esta orden y muestra un mensaje
                        mostrarError("Error al insertar orden", "La bebida seleccionada no existe en la base de datos.");
                    }
                });

                contenidoPestana.add(botonBebida, columna, fila);
                columna++;
                if (columna == 3) {
                    columna = 0;
                    fila++;
                }
            }
        } else {
            ObservableList<ComidaDAO> listaComidas = comidaDAO.consultarPorCategoria(nombreCategoria);

            for (ComidaDAO comida : listaComidas) {
                Button botonComida = new Button(comida.getNombre());
                botonComida.setOnAction(event -> {
                    ComidaDAO comidaSeleccionada = listaComidas.get(GridPane.getRowIndex((Button) event.getSource()));
                    OrdenTemporal ordenTemporalComida = new OrdenTemporal(idEmpleado, comidaSeleccionada, comidaSeleccionada.getPrecio(), 1, numeroMesa);
                    if (comidaDAO.existeComida(comidaSeleccionada.getIdComida())) {
                        ordenTemporalComida.setIdComida(comidaSeleccionada.getIdComida());
                        ordenTemporalComida.setIdBebida(0);
                        OrdenesManager.getInstance().insertarOrdenTemporal(ordenTemporalComida);
                        tableView.getItems().add(ordenTemporalComida);
                    } else {
                        // Si la comida no existe, omite esta orden y muestra un mensaje
                        mostrarError("Error al insertar orden", "La comida seleccionada no existe en la base de datos.");
                    }
                });

                contenidoPestana.add(botonComida, columna, fila);
                columna++;
                if (columna == 3) {
                    columna = 0;
                    fila++;
                }
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
