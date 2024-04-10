package com.example.proyectotaqueria.modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.Statement;

public class EmpleadosDAO {

    private int id_Empleado;
    private int telefono;
    private String direccion;
    private String rfc;
    private float salario;
    private int id_Usuario;
    private int no_mesa;
    private String nomEmpleado;

    public int getId_Empleado() {
        return id_Empleado;
    }

    public void setId_Empleado(int id_Empleado) {
        this.id_Empleado = id_Empleado;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public float getSalario() {
        return salario;
    }

    public void setSalario(float salario) {
        this.salario = salario;
    }

    public int getId_Usuario() {
        return id_Usuario;
    }

    public void setId_Usuario(int id_Usuario) {
        this.id_Usuario = id_Usuario;
    }

    public int getNo_mesa() {
        return no_mesa;
    }

    public void setNo_mesa(int no_mesa) {
        this.no_mesa = no_mesa;
    }

    public String getNomEmpleado() {
        return nomEmpleado;
    }

    public void setNomEmpleado(String nomEmpleado) {
        this.nomEmpleado = nomEmpleado;
    }



    public void INSERTAR() {
        String query = "INSERT INTO empleado(nomEmpleado, rfc, salario, direccion, telefono) " +
                "VALUES('" + nomEmpleado + "','" + rfc + "'," + salario + ",'" + direccion + "','" + telefono + "')";
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ACTUALIZAR() {
        String query = "UPDATE empleado SET nomEmpleado='" + nomEmpleado + "'," +
                "rfc='" + rfc + "', salario=" + salario + "," +
                "direccion='" + direccion + "',telefono='" + telefono + "'" +
                " WHERE id_Empleado=" + id_Empleado;
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void ELIMINAR() {
        String query = "DELETE FROM empleado WHERE id_Empleado=" + id_Empleado;
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<EmpleadosDAO> CONSULTAR() {
        ObservableList<EmpleadosDAO> listaEmp = FXCollections.observableArrayList();

        String query = "SELECT * FROM empleado";
        try {
            Statement stmt = Conexion.connection.createStatement();
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                EmpleadosDAO objEmp = new EmpleadosDAO();
                objEmp.setId_Empleado(res.getInt("id_Empleado"));
                objEmp.setNomEmpleado(res.getString("nomEmpleado"));
                objEmp.setTelefono(res.getInt("telefono"));
                objEmp.setDireccion(res.getString("direccion"));
                objEmp.setRfc(res.getString("rfc"));
                objEmp.setSalario(res.getFloat("salario"));
                objEmp.setId_Usuario(res.getInt("id_Usuario"));
                objEmp.setNo_mesa(res.getInt("no_mesa"));
                listaEmp.add(objEmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmp;
    }
}