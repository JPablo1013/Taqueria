package com.example.proyectotaqueria.modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PagoDAO {

    private int idPago;
    private String nombrePago;
    private double total;
    private int idTipoPago;
    private String descripcion;

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public String getNombrePago() {
        return nombrePago;
    }

    public void setNombrePago(String nombrePago) {
        this.nombrePago = nombrePago;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getIdTipoPago() {
        return idTipoPago;
    }

    public void setIdTipoPago(int idTipoPago) {
        this.idTipoPago = idTipoPago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int insertar() {
        String query = "INSERT INTO pago(nombre_Pago, total, id_TipoPago, descripcion) " +
                "VALUES('" + nombrePago + "'," + total + "," + idTipoPago + ",'" + descripcion + "')";
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                idPago = generatedKeys.getInt(1);
                return idPago; // Devolver el ID generado
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // Devolver 0 si no se pudo obtener el ID generado
    }



    public void actualizar() {
        String query = "UPDATE pago SET nombre_Pago='" + nombrePago + "', total=" + total + ", id_TipoPago=" +
                idTipoPago + ", descripcion='" + descripcion + "' WHERE id_Pago=" + idPago;
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        String query = "DELETE FROM pago WHERE id_Pago=" + idPago;
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<PagoDAO> consultar() {
        ObservableList<PagoDAO> listaPagos = FXCollections.observableArrayList();

        String query = "SELECT * FROM pago";
        try {
            Statement stmt = Conexion.connection.createStatement();
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                PagoDAO pago = new PagoDAO();
                //pago.setIdPago(res.getInt("id_Pago"));
                pago.setNombrePago(res.getString("nombre_Pago"));
                pago.setTotal(res.getDouble("total"));
                pago.setIdTipoPago(res.getInt("id_TipoPago"));
                pago.setDescripcion(res.getString("descripcion"));
                listaPagos.add(pago);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPagos;
    }

    public PreparedStatement obtenerPreparedStatement() throws SQLException {
        String query = "INSERT INTO pago(nombre_Pago, total, id_TipoPago, descripcion) " +
                "VALUES(?, ?, ?, ?)";
        PreparedStatement pstmt = Conexion.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, nombrePago);
        pstmt.setDouble(2, total);
        pstmt.setInt(3, idTipoPago);
        pstmt.setString(4, descripcion);
        return pstmt;
    }

    public int insertarPago(int idTipoPago, double total, String descripcion) {
        try {
            PagoDAO pago = new PagoDAO();
            pago.setNombrePago("Pago de orden");
            pago.setTotal(total);
            pago.setIdTipoPago(idTipoPago);
            pago.setDescripcion(descripcion);
            pago.insertar();

            // Obtener el ID generado después de la inserción
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate("SELECT LAST_INSERT_ID()");
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0; // Si no se obtiene el ID, devolvemos 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
