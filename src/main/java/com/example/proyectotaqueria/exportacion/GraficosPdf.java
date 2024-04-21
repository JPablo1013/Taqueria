package com.example.proyectotaqueria.exportacion;

import com.example.proyectotaqueria.vistas.Graficos;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GraficosPdf {
    private Graficos graficos;

    public GraficosPdf(Graficos graficos) {
        this.graficos = graficos;
    }

    public void generateAndSaveChartsPDF(String destFolder) {
        try {
            BarChart<String, Number> productosMasVendidosChart = graficos.createProductosMasVendidosPanel();
            BarChart<String, Number> ventasPorDiaChart = graficos.createVentasPorDiaPanel();
            BarChart<String, Number> empleadoMasVentasChart = graficos.createEmpleadoMasVentasPanel();

            guardarGraficoComoPDF(productosMasVendidosChart, destFolder + "/productos_mas_vendidos.pdf");
            guardarGraficoComoPDF(ventasPorDiaChart, destFolder + "/ventas_por_dia.pdf");
            guardarGraficoComoPDF(empleadoMasVentasChart, destFolder + "/empleado_mas_ventas.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void guardarGraficoComoPDF(BarChart<String, Number> chart, String filePath) {
        try {
            // Inicializar el escritor de PDF
            PdfWriter writer = new PdfWriter(filePath);

            // Inicializar el documento PDF
            PdfDocument pdf = new PdfDocument(writer);

            // Inicializar el documento iText
            Document document = new Document(pdf, PageSize.A4.rotate());
            document.setMargins(20, 20, 20, 20);

            // Convertir el gráfico de JavaFX a una imagen
            WritableImage chartImage = chart.snapshot(new SnapshotParameters(), null);

            // Crear un objeto Image iText a partir de la imagen del gráfico
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(chartImage, null);
            com.itextpdf.io.image.ImageData imageData = ImageDataFactory.create(bufferedImage, null);
            Image img = new Image(imageData);

            // Añadir la imagen al documento PDF
            document.add(img);

            // Cerrar el documento
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
