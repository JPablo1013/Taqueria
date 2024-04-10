package com.example.proyectotaqueria.modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.Statement;

public class TipoPagoDAO {

    private int idTipoPago;
    private String nombre;

    public int getIdTipoPago() {
        return idTipoPago;
    }

    public void setIdTipoPago(int idTipoPago) {
        this.idTipoPago = idTipoPago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void insertar() {
        String query = "INSERT INTO tipo_pago(id_TipoPago, nombre) VALUES(" + idTipoPago + ",'" + nombre + "')";
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizar() {
        String query = "UPDATE tipo_pago SET nombre='" + nombre + "' WHERE id_TipoPago=" + idTipoPago;
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        String query = "DELETE FROM tipo_pago WHERE id_TipoPago=" + idTipoPago;
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<TipoPagoDAO> consultar() {
        ObservableList<TipoPagoDAO> listaTiposPago = FXCollections.observableArrayList();

        String query = "SELECT * FROM tipo_pago";
        try {
            Statement stmt = Conexion.connection.createStatement();
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                TipoPagoDAO tipoPago = new TipoPagoDAO();
                tipoPago.setIdTipoPago(res.getInt("id_TipoPago"));
                tipoPago.setNombre(res.getString("nombre"));
                listaTiposPago.add(tipoPago);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTiposPago;
    }
}
