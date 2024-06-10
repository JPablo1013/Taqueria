package com.example.proyectotaqueria.exportacion;

import com.example.proyectotaqueria.modelos.Conexion;
import com.example.proyectotaqueria.modelos.OrdenDAO;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TicketClass {

    private OrdenDAO ordenDAO = new OrdenDAO();

    public void createPdf(String dest) throws IOException {
        try {
            // Initialize PDF writer
            PdfWriter writer = new PdfWriter(dest);

            // Initialize PDF document
            PdfDocument pdf = new PdfDocument(writer);

            // Initialize document
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(20, 20, 20, 20);

            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            // Retrieve the last order
            OrdenDAO ultimaOrden = obtenerUltimaOrden();
            if (ultimaOrden != null) {
                // Create table for ticket
                Table table = new Table(UnitValue.createPercentArray(new float[]{2, 5}))
                        .useAllAvailableWidth();

                // Add ticket details
                table.addCell(new Cell().add(new Paragraph("ID Orden:").setFont(bold)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(ultimaOrden.getIdOrden())).setFont(font)));
                table.addCell(new Cell().add(new Paragraph("ID Empleado:").setFont(bold)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(ultimaOrden.getIdEmpleado())).setFont(font)));
                table.addCell(new Cell().add(new Paragraph("ID Pago:").setFont(bold)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(ultimaOrden.getIdPago())).setFont(font)));
                table.addCell(new Cell().add(new Paragraph("Fecha:").setFont(bold)));
                table.addCell(new Cell().add(new Paragraph(ultimaOrden.getFecha()).setFont(font)));
                table.addCell(new Cell().add(new Paragraph("Comida:").setFont(bold)));
                table.addCell(new Cell().add(new Paragraph(ordenDAO.getNombreComidaPorId(ultimaOrden.getIdComida())).setFont(font)));
                //table.addCell(new Cell().add(new Paragraph("Cantidad:").setFont(bold)));
                //table.addCell(new Cell().add(new Paragraph(String.valueOf(ultimaOrden.getCantidad())).setFont(font)));
                table.addCell(new Cell().add(new Paragraph("No. Mesa:").setFont(bold)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(ultimaOrden.getNoMesa())).setFont(font)));

                document.add(table);
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OrdenDAO obtenerUltimaOrden() {
        OrdenDAO ultimaOrden = null;
        String query = "SELECT * FROM orden ORDER BY id_Orden DESC LIMIT 1";
        try {
            Statement stmt = Conexion.connection.createStatement();
            ResultSet res = stmt.executeQuery(query);
            if (res.next()) {
                ultimaOrden = new OrdenDAO();
                ultimaOrden.setIdOrden(res.getInt("id_Orden"));
                ultimaOrden.setIdEmpleado(res.getInt("id_Empleado"));
                ultimaOrden.setIdPago(res.getInt("id_Pago"));
                ultimaOrden.setFecha(res.getString("fecha"));
                ultimaOrden.setIdComida(res.getInt("id_Comida"));
                //ultimaOrden.setCantidad(res.getInt("cantidad"));
                ultimaOrden.setNoMesa(res.getInt("no_mesa"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ultimaOrden;
    }
}
