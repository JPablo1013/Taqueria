package com.example.proyectotaqueria.modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.Statement;

public class UsuarioDAO {

    private int idUsuario;
    private String usuario;
    private String contrasena;
    private int idEmpleado;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public void insertar() {
        String query = "INSERT INTO usuario(usuario, contrasena, id_Empleado) " +
                "VALUES('" + usuario + "','" + contrasena + "'," + idEmpleado + ")";
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizar() {
        String query = "UPDATE usuario SET usuario='" + usuario + "', contrasena='" + contrasena + "', id_Empleado=" +
                idEmpleado + " WHERE id_Usuario=" + idUsuario;
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        String query = "DELETE FROM usuario WHERE id_Usuario=" + idUsuario;
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<UsuarioDAO> consultar() {
        ObservableList<UsuarioDAO> listaUsuarios = FXCollections.observableArrayList();

        String query = "SELECT * FROM usuario";
        try {
            Statement stmt = Conexion.connection.createStatement();
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                UsuarioDAO usuario = new UsuarioDAO();
                usuario.setIdUsuario(res.getInt("id_Usuario"));
                usuario.setUsuario(res.getString("usuario"));
                usuario.setContrasena(res.getString("contrasena"));
                usuario.setIdEmpleado(res.getInt("id_Empleado"));
                listaUsuarios.add(usuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaUsuarios;
    }
}
