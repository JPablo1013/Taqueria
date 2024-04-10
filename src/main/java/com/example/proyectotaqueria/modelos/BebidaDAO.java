package com.example.proyectotaqueria.modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.Statement;

public class BebidaDAO {

    private int idBebida;
    private String nombre;
    private int cantidad;
    private float precio;


    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getIdBebida() {
        return idBebida;
    }

    public void setIdBebida(int idBebida) {
        this.idBebida = idBebida;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void insertar() {
        String query = "INSERT INTO bebida(nombre, cantidad, precio) " +
                "VALUES('" + nombre + "'," + cantidad + "," + precio + ")";
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void actualizar() {
        String query = "UPDATE bebida SET nombre='" + nombre + "', " +
                "cantidad=" + cantidad + ", " +
                "precio=" + precio + " " +
                "WHERE id_Bebida=" + idBebida;
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void eliminar() {
        String query = "DELETE FROM bebida WHERE id_Bebida=" + idBebida;
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<BebidaDAO> consultar() {
        ObservableList<BebidaDAO> listaBebidas = FXCollections.observableArrayList();

        String query = "SELECT * FROM bebida";
        try {
            Statement stmt = Conexion.connection.createStatement();
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                BebidaDAO bebida = new BebidaDAO();
                bebida.setIdBebida(res.getInt("id_Bebida"));
                bebida.setNombre(res.getString("nombre"));
                bebida.setCantidad(res.getInt("cantidad"));
                bebida.setPrecio(res.getFloat("precio"));
                listaBebidas.add(bebida);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaBebidas;
    }
}
