package com.example.proyectotaqueria.vistas;

import com.example.proyectotaqueria.exportacion.TicketClass;
import com.example.proyectotaqueria.modelos.*;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;

import static com.example.proyectotaqueria.modelos.MesaDAO.obtenerMesaPorNumero;

public class Orden extends Stage {
    private TableView<OrdenTemporal> tableView;
    private int numeroMesa;
    private int idEmpleado;
    private int idPago;
    private Map<Integer, Button> botonesMesas;
    private ComidaDAO comidaDAO = new ComidaDAO();
    public static final String DEST4 = "results/ticket.pdf";

    public Orden(int numeroMesa, int idEmpleado, ObservableList<OrdenTemporal> listaOrdenesTemporales, ComidaDAO comida, Map<Integer, Button> botonesMesas) {
        this.numeroMesa = numeroMesa;
        this.idEmpleado = idEmpleado;
        this.botonesMesas = botonesMesas; // Asignar el mapa de botonesMesas recibido como parámetro
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
            }
        });

        Button botonPagar = new Button("Pagar");
        botonPagar.setOnAction(event -> pagarOrden(botonesMesas));
        VBox vBox = new VBox(tableView, tabPane, botonAceptar, botonPagar);
        Scene scene = new Scene(vBox);
        this.setScene(scene);
    }

    protected void onViewReportPDF() {
        try {
            File file = new File(DEST4);
            file.getParentFile().mkdirs();
            new TicketClass().createPdf(DEST4);
            openFile(DEST4);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void openFile(String filename) {
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(filename);
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void aceptarOrden() {

        // Cambiar el color del botón de la mesa a rojo
        Button botonMesa = botonesMesas.get(numeroMesa);
        if (botonMesa != null) {
            botonMesa.setStyle("-fx-background-color: red");
        }

        // Cerrar la ventana de órdenes
        this.close();
    }

    private int insertarPago(int idTipoPago, double total, String descripcion) {
        try {
            PagoDAO pago = new PagoDAO();
            pago.setNombrePago("Pago de orden");
            pago.setTotal(total);
            pago.setIdTipoPago(idTipoPago);
            pago.setDescripcion(descripcion);

            // Insertar el pago en la base de datos
            int idPagoGenerado = pago.insertar();

            if (idPagoGenerado != 0) {
                // Si se insertó correctamente, devolver el idPago generado
                return idPagoGenerado;
            } else {
                // Si no se pudo obtener el idPago generado, mostrar un mensaje de error
                mostrarError("Error al insertar pago", "No se pudo obtener el ID de pago");
                return 0;
            }
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir durante la inserción del pago
            e.printStackTrace();
            return 0;
        }
    }



    private void insertarOrdenBD(int idPago) {
        ObservableList<OrdenTemporal> ordenesTemporales = tableView.getItems();
        boolean errores = false;
        for (OrdenTemporal ordenTemporal : ordenesTemporales) {
            if (ordenTemporal.getIdComida() != 0) {
                OrdenDAO ordenDAO = new OrdenDAO();
                ordenDAO.setIdEmpleado(ordenTemporal.getIdEmpleado());
                ordenDAO.setIdPago(idPago);
                ordenDAO.setFecha(LocalDate.now().toString());

                // Aquí debes obtener el número de mesa desde la orden temporal
                int numeroMesa = ordenTemporal.getNumeroMesa(); // Asegúrate de tener este método en tu clase OrdenTemporal

                // Verificar la existencia de la mesa
                if (existeMesa(numeroMesa)) {
                    MesaDAO mesa = obtenerMesaPorNumero(numeroMesa); // Método para obtener el objeto MesaDAO correspondiente al número de mesa
                    ordenDAO.setNoMesa(mesa.getNoMesa()); // Establecer el número de mesa en el objeto OrdenDAO

                if (ordenTemporal.getItem() instanceof ComidaDAO) {
                        ComidaDAO comida = (ComidaDAO) ordenTemporal.getItem();
                        if (comidaDAO.existeComida(comida.getIdComida())) {
                            ordenDAO.setIdComida(comida.getIdComida());
                            ordenDAO.insertar();
                        } else {
                            errores = true;
                            mostrarError("Error al insertar orden", "La comida seleccionada no existe en la base de datos.");
                        }
                    }
                    System.out.println("ID de comida: " + ordenDAO.getIdComida());
                } else {
                    errores = true;
                    mostrarError("Error al insertar orden", "La mesa seleccionada no existe.");
                }
            }
        }

        if (!errores) {
            mostrarMensaje("Órdenes guardadas con éxito");
        }
    }

    // Método para verificar la existencia de la mesa
    private boolean existeMesa(int numeroMesa) {
        // Aquí debes implementar la lógica para verificar si la mesa existe en la base de datos
        // Retorna true si la mesa existe, de lo contrario, retorna false
        // Por ahora, vamos a simular que la mesa siempre existe
        return true;
    }

    private void pagarOrden(Map<Integer, Button> botonesMesas) {
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
                onViewReportPDF();

                // Cambiar el color del botón de mesa a verde y limpiar la tabla
                Button botonMesa = botonesMesas.get(numeroMesa);
                if (botonMesa != null) {
                    botonMesa.setStyle("-fx-background-color: green");
                }
                tableView.getItems().clear();
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


    private GridPane crearContenidoPestana(String nombreCategoria) {
        GridPane contenidoPestana = new GridPane();
        contenidoPestana.setAlignment(Pos.CENTER);
        contenidoPestana.setHgap(10);
        contenidoPestana.setVgap(10);
        contenidoPestana.setPadding(new Insets(20));

        int fila = 0;
        int columna = 0;

        ObservableList<ComidaDAO> listaComidas = comidaDAO.consultarPorCategoria(nombreCategoria);

        for (ComidaDAO comida : listaComidas) {
            Button botonComida = new Button(comida.getNombre());
            try {
                String foto = comida.getFoto();
                if (foto != null && !foto.isEmpty()) {
                    String rutaImagen = "/images/" + foto;
                    InputStream inputStream = getClass().getResourceAsStream(rutaImagen);
                    if (inputStream != null) {
                        Image image = new Image(inputStream);
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(100);
                        imageView.setFitHeight(100);
                        botonComida.setGraphic(imageView);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            botonComida.setOnAction(event -> {
                ComidaDAO comidaSeleccionada = listaComidas.get(GridPane.getRowIndex((Button) event.getSource()));
                OrdenTemporal ordenTemporalComida = new OrdenTemporal(idEmpleado, comidaSeleccionada, comidaSeleccionada.getPrecio(), 1, numeroMesa);
                if (comidaDAO.existeComida(comidaSeleccionada.getIdComida())) {
                    ordenTemporalComida.setIdComida(comidaSeleccionada.getIdComida());
                    OrdenesManager.getInstance().insertarOrdenTemporal(ordenTemporalComida);
                    tableView.getItems().add(ordenTemporalComida);
                } else {
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
