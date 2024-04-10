package com.example.proyectotaqueria.modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.Statement;

public class CategoriaDAO {

    private int idCategoria;
    private String nombre;

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void insertar() {
        String query = "INSERT INTO categoria(nombre) VALUES('" + nombre + "')";
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizar() {
        String query = "UPDATE categoria SET nombre='" + nombre + "' WHERE id_Categoria=" + idCategoria;
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        String query = "DELETE FROM categoria WHERE id_Categoria=" + idCategoria;
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<CategoriaDAO> consultar() {
        ObservableList<CategoriaDAO> listaCategorias = FXCollections.observableArrayList();

        String query = "SELECT * FROM categoria";
        try {
            Statement stmt = Conexion.connection.createStatement();
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                CategoriaDAO categoria = new CategoriaDAO();
                categoria.setIdCategoria(res.getInt("id_Categoria"));
                categoria.setNombre(res.getString("nombre"));
                listaCategorias.add(categoria);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCategorias;
    }

    // Método toString para ComboBox
    @Override
    public String toString() {
        return nombre;
    }
}
