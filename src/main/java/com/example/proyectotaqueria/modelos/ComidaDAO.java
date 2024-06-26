package com.example.proyectotaqueria.modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ComidaDAO {

    private int idComida;
    private String nombre;
    private int idCategoria;
    private float precio;

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    private String foto;

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getIdComida() {
        return idComida;
    }

    public void setIdComida(int idComida) {
        this.idComida = idComida;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    /*public void insertar() {
        String query = "INSERT INTO comida(nombre, precio, id_Categoria) VALUES(?, ?, ?)";
        try (PreparedStatement stmt = Conexion.connection.prepareStatement(query)) {
            stmt.setString(1, nombre);
            stmt.setFloat(2, precio);
            stmt.setInt(3, idCategoria);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    public void insertar(String nombreImagen) {
        String query = "INSERT INTO comida(nombre, precio, id_Categoria, foto) VALUES(?, ?, ?, ?)";
        try (PreparedStatement stmt = Conexion.connection.prepareStatement(query)) {
            stmt.setString(1, nombre);
            stmt.setFloat(2, precio);
            stmt.setInt(3, idCategoria);
            stmt.setString(4, nombreImagen);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void actualizar() {
        String query = "UPDATE comida SET nombre=?, precio=?, id_Categoria=? WHERE id_Comida=?";
        try (PreparedStatement stmt = Conexion.connection.prepareStatement(query)) {
            stmt.setString(1, nombre);
            stmt.setFloat(2, precio);
            stmt.setInt(3, idCategoria);
            stmt.setInt(4, idComida);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        String query = "DELETE FROM comida WHERE id_Comida=?";
        try (PreparedStatement stmt = Conexion.connection.prepareStatement(query)) {
            stmt.setInt(1, idComida);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<ComidaDAO> consultar() {
        ObservableList<ComidaDAO> listaComidas = FXCollections.observableArrayList();

        String query = "SELECT * FROM comida";
        try (Statement stmt = Conexion.connection.createStatement();
             ResultSet res = stmt.executeQuery(query)) {
            while (res.next()) {
                ComidaDAO comida = new ComidaDAO();
                comida.setIdComida(res.getInt("id_Comida"));
                comida.setNombre(res.getString("nombre"));
                comida.setPrecio(res.getFloat("precio"));
                comida.setIdCategoria(res.getInt("id_Categoria"));
                comida.setFoto(res.getString("foto"));
                listaComidas.add(comida);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaComidas;
    }

    public ComidaDAO obtenerComidaPorId(int idComida) {
        ComidaDAO comida = null;
        String query = "SELECT * FROM comida WHERE id_Comida = ?";
        try (PreparedStatement stmt = Conexion.connection.prepareStatement(query)) {
            stmt.setInt(1, idComida);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                comida = new ComidaDAO();
                comida.setIdComida(res.getInt("id_Comida"));
                comida.setNombre(res.getString("nombre"));
                comida.setPrecio(res.getFloat("precio"));
                comida.setIdCategoria(res.getInt("id_Categoria"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comida;
    }

    public ObservableList<ComidaDAO> consultarPorCategoria(String nombreCategoria) {
        ObservableList<ComidaDAO> listaComidas = FXCollections.observableArrayList();
        String query = "SELECT * FROM comida WHERE id_Categoria = ?";
        try (PreparedStatement stmt = Conexion.connection.prepareStatement(query)) {
            // Obtener el ID de la categoría
            int idCategoria = obtenerIdCategoria(nombreCategoria);

            // Establecer el ID de la categoría como parámetro en la consulta
            stmt.setInt(1, idCategoria);
            ResultSet res = stmt.executeQuery();

            // Iterar sobre los resultados y agregar las comidas a la lista
            while (res.next()) {
                ComidaDAO comida = new ComidaDAO();
                comida.setIdComida(res.getInt("id_Comida"));
                comida.setNombre(res.getString("nombre"));
                comida.setPrecio(res.getFloat("precio"));
                comida.setFoto(res.getString("foto"));
                comida.setIdCategoria(idCategoria);
                listaComidas.add(comida);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaComidas;
    }

    private int obtenerIdCategoria(String nombreCategoria) {
        String query = "SELECT id_Categoria FROM categoria WHERE nombre = ?";
        int idCategoria = 0;
        try (PreparedStatement stmt = Conexion.connection.prepareStatement(query)) {
            stmt.setString(1, nombreCategoria);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                idCategoria = res.getInt("id_Categoria");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idCategoria;
    }

    public boolean existeComida(int idComida) {
        String query = "SELECT COUNT(*) FROM comida WHERE id_Comida = " + idComida;
        try {
            Statement stmt = Conexion.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            int count = rs.getInt(1);
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void guardarImagen(String nombreImagen) {
        String query = "UPDATE comida SET foto = ? WHERE id_Comida = ?";
        try (PreparedStatement stmt = Conexion.connection.prepareStatement(query)) {
            stmt.setString(1, nombreImagen);
            stmt.setInt(2, idComida);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
