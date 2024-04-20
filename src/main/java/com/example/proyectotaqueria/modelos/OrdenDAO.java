package com.example.proyectotaqueria.modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class OrdenDAO {

    private int idOrden;
    private int idEmpleado;
    private int idPago;
    private String fecha;
    private int idComida;
    private int idBebida;
    private int cantidad;
    private int noMesa;

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdComida() {
        return idComida;
    }

    public void setIdComida(int idComida) {
        this.idComida = idComida;
    }

    public int getIdBebida() {
        return idBebida;
    }

    public void setIdBebida(int idBebida) {
        this.idBebida = idBebida;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getNoMesa() {
        return noMesa;
    }

    public void setNoMesa(int noMesa) {
        this.noMesa = noMesa;
    }

    /*public void insertar() {
        String query = "INSERT INTO orden(id_Empleado, id_Pago, fecha, id_Comida, id_Bebida, cantidad, no_mesa) " +
                "VALUES(" + idEmpleado + "," + idPago + ",'" + fecha + "'," + idComida + "," + idBebida + "," +
                cantidad + "," + noMesa + ")";
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void insertar() {
        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Crear la consulta SQL con la fecha actual
        String query = "INSERT INTO orden(id_Empleado, id_Pago, fecha, id_Comida, id_Bebida, cantidad, no_mesa) " +
                "VALUES(" + idEmpleado + "," + idPago + ",'" + fechaActual + "'," + idComida + "," + idBebida + "," +
                cantidad + "," + noMesa + ")";

        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizar() {
        String query = "UPDATE orden SET id_Empleado=" + idEmpleado + ", id_Pago=" + idPago + ", fecha='" + fecha + "', " +
                "id_Comida=" + idComida + ", id_Bebida=" + idBebida + ", cantidad=" + cantidad + ", no_mesa=" + noMesa +
                " WHERE id_Orden=" + idOrden;
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        String query = "DELETE FROM orden WHERE id_Orden=" + idOrden;
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<OrdenDAO> consultar() {
        ObservableList<OrdenDAO> listaOrdenes = FXCollections.observableArrayList();

        String query = "SELECT * FROM orden";
        try {
            Statement stmt = Conexion.connection.createStatement();
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                OrdenDAO orden = new OrdenDAO();
                orden.setIdOrden(res.getInt("id_Orden"));
                orden.setIdEmpleado(res.getInt("id_Empleado"));
                orden.setIdPago(res.getInt("id_Pago"));
                orden.setFecha(res.getString("fecha"));
                orden.setIdComida(res.getInt("id_Comida"));
                orden.setIdBebida(res.getInt("id_Bebida"));
                orden.setCantidad(res.getInt("cantidad"));
                orden.setNoMesa(res.getInt("no_mesa"));
                listaOrdenes.add(orden);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaOrdenes;
    }
}
