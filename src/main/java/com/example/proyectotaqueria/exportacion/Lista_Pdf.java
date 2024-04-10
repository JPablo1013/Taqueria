package com.example.proyectotaqueria.exportacion;

import com.example.proyectotaqueria.modelos.EmpleadosDAO;
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
//import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.property.UnitValue;
import javafx.collections.ObservableList;

import java.io.IOException;

public class Lista_Pdf {

    private EmpleadosDAO empleadosDAO = new EmpleadosDAO();

    public void createPdf(String dest) throws IOException {
        try {
            // Initialize PDF writer
            PdfWriter writer = new PdfWriter(dest);

            // Initialize PDF document
            PdfDocument pdf = new PdfDocument(writer);

            // Initialize document
            Document document = new Document(pdf, PageSize.A4.rotate());
            document.setMargins(20, 20, 20, 20);

            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            Table table = new Table(UnitValue.createPercentArray(new float[]{4, 6, 4}))
                    .useAllAvailableWidth();

            process(table, null, bold, true);
            ObservableList<EmpleadosDAO> listEmpleado = empleadosDAO.CONSULTAR();
            for (EmpleadosDAO lista : listEmpleado) {
                process(table, lista, font, false);
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void process(Table table, Object data, PdfFont font, boolean isHeader) {
        if (isHeader) {
            table.addHeaderCell(new Cell().add(new Paragraph("id_Empleado").setFont(font)));
            table.addHeaderCell(new Cell().add(new Paragraph("nomEmpleado").setFont(font)));
            table.addHeaderCell(new Cell().add(new Paragraph("telefono").setFont(font)));
            table.addHeaderCell(new Cell().add(new Paragraph("direccion").setFont(font)));
            table.addHeaderCell(new Cell().add(new Paragraph("rfc").setFont(font)));
            table.addHeaderCell(new Cell().add(new Paragraph("salario").setFont(font)));
            table.addHeaderCell(new Cell().add(new Paragraph("id_Usuario").setFont(font)));
            table.addHeaderCell(new Cell().add(new Paragraph("no_mesa").setFont(font)));
        } else {
            if (data != null) {
                EmpleadosDAO empleado = (EmpleadosDAO) data;
                table.addCell(new Cell().add(new Paragraph(String.valueOf(empleado.getId_Empleado())).setFont(font)));
                table.addCell(new Cell().add(new Paragraph(empleado.getNomEmpleado()).setFont(font)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(empleado.getTelefono())).setFont(font)));
                table.addCell(new Cell().add(new Paragraph(empleado.getDireccion()).setFont(font)));
                table.addCell(new Cell().add(new Paragraph(empleado.getRfc()).setFont(font)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(empleado.getSalario())).setFont(font)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(empleado.getId_Usuario())).setFont(font)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(empleado.getNo_mesa())).setFont(font)));
            }
        }
    }
}
