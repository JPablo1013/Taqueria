package com.example.proyectotaqueria.vistas;

import com.example.proyectotaqueria.exportacion.GraficosPdf;
import com.example.proyectotaqueria.modelos.OrdenDAO;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Graficos extends BorderPane {

    private Button btnGeneratePDF;

    public Graficos() {
        this.setLeft(createProductosMasVendidosPanel());
        this.setCenter(createVentasPorDiaPanel());
        this.setRight(createEmpleadoMasVentasPanel());

        // Botón para generar el PDF
        btnGeneratePDF = new Button("Generar PDF");
        btnGeneratePDF.setOnAction(event -> generatePDFReport());

        HBox hbox = new HBox(btnGeneratePDF);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(btnGeneratePDF, Priority.ALWAYS);
        setBottom(hbox);
    }

    protected void generatePDFReport() {
        try {
            String destFolder = "results"; // Carpeta de destino para los archivos PDF
            File folder = new File(destFolder);
            folder.mkdirs(); // Crear la carpeta si no existe

            Graficos graficos = new Graficos();
            GraficosPdf graficosPdf = new GraficosPdf(graficos);
            graficosPdf.generateAndSaveChartsPDF("results");


            openFile(new File(folder, "Graficos.pdf")); // Abrir el PDF generado
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openFile(File file) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    public BarChart<String, Number> createProductosMasVendidosPanel() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Obtener la lista de productos más vendidos desde la base de datos
        OrdenDAO ordenDAO = new OrdenDAO();
        for (OrdenDAO orden : ordenDAO.obtenerProductosMasVendidos()) {
            // Obtener el nombre del producto (comida o bebida)
            String nombreProducto = "";
            if (orden.getIdComida() != 0) {
                // Si es comida
                nombreProducto = orden.getNombreComidaPorId(orden.getIdComida());
            } else if (orden.getIdBebida() != 0) {
                // Si es bebida
                nombreProducto = orden.getNombreBebidaPorId(orden.getIdBebida());
            }

            // Agregar el nombre del producto y su cantidad vendida a la serie del gráfico
            series.getData().add(new XYChart.Data<>(nombreProducto, orden.getCantidad()));
        }

        chart.getData().add(series);
        chart.setTitle("Productos Más Vendidos");
        xAxis.setLabel("Productos");
        yAxis.setLabel("Cantidad Vendida");

        return chart;
    }

    public BarChart<String, Number> createVentasPorDiaPanel() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);

        // Obtener la lista de ventas por día desde la base de datos
        OrdenDAO ordenDAO = new OrdenDAO();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (OrdenDAO venta : ordenDAO.obtenerVentasPorDia()) {
            // Crear un nuevo objeto Data con la fecha y la cantidad vendida
            XYChart.Data<String, Number> data = new XYChart.Data<>(venta.getFecha(), venta.getCantidad());
            // Agregar el objeto Data a la serie
            series.getData().add(data);
        }

        // Agregar la serie al gráfico
        chart.getData().add(series);

        chart.setTitle("Ventas por Día");
        xAxis.setLabel("Día");
        yAxis.setLabel("Cantidad Vendida");

        return chart;
    }

    public BarChart<String, Number> createEmpleadoMasVentasPanel() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Obtener el empleado con más ventas realizadas desde la base de datos
        OrdenDAO ordenDAO = new OrdenDAO();
        ObservableList<String> empleadoMasVentas = ordenDAO.obtenerEmpleadoMasVentas();

        // Agregar los datos al gráfico
        for (String empleado : empleadoMasVentas) {
            String[] datosEmpleado = empleado.split(":");
            String nombreEmpleado = datosEmpleado[0];
            int ventasRealizadas = Integer.parseInt(datosEmpleado[1].trim());
            series.getData().add(new XYChart.Data<>(nombreEmpleado, ventasRealizadas));
        }

        chart.getData().add(series);
        chart.setTitle("Empleado con más ventas realizadas");
        xAxis.setLabel("Empleado");
        yAxis.setLabel("Ventas realizadas");

        return chart;
    }
}
