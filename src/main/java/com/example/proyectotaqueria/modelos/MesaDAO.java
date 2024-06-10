package com.example.proyectotaqueria.modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.Statement;

public class MesaDAO {

    private int noMesa;
    private boolean ocupada;

    public MesaDAO() {
    }

    public int getNoMesa() {
        return noMesa;
    }

    public void setNoMesa(int noMesa) {
        this.noMesa = noMesa;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public void insertar() {
        String query = "INSERT INTO mesas(no_mesa, ocupada) " +
                "VALUES(" + noMesa + "," + (ocupada ? 1 : 0) +  ")";
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizar() {
        String query = "UPDATE mesas SET ocupada=" + (ocupada ? 1 : 0) + " WHERE no_mesa=" + noMesa;
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        String query = "DELETE FROM mesas WHERE no_mesa=" + noMesa;
        try {
            Statement stmt = Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<MesaDAO> consultar() {
        ObservableList<MesaDAO> listaMesas = FXCollections.observableArrayList();

        String query = "SELECT * FROM mesas";
        try {
            Statement stmt = Conexion.connection.createStatement();
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                MesaDAO mesa = new MesaDAO();
                mesa.setNoMesa(res.getInt("no_mesa"));
                mesa.setOcupada(res.getBoolean("ocupada"));
                listaMesas.add(mesa);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaMesas;
    }

    public static MesaDAO obtenerMesaPorNumero(int numeroMesa) {
        String query = "SELECT * FROM mesas WHERE no_mesa = " + numeroMesa;
        MesaDAO mesa = null;
        try {
            Statement stmt = Conexion.connection.createStatement();
            ResultSet res = stmt.executeQuery(query);
            if (res.next()) {
                mesa = new MesaDAO();
                mesa.setNoMesa(res.getInt("no_mesa"));
                mesa.setOcupada(res.getBoolean("ocupada"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mesa == null) {
            throw new RuntimeException("No se pudo encontrar la mesa con el número: " + numeroMesa);
        }
        return mesa;
    }
}
