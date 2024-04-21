package com.example.proyectotaqueria.modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public ObservableList<OrdenDAO> obtenerProductosMasVendidos() {
        ObservableList<OrdenDAO> listaProductosMasVendidos = FXCollections.observableArrayList();

        String query = "SELECT id_Comida AS id_Producto, 'Comida' AS tipo, COUNT(id_Comida) AS cantidad_ventas " +
                "FROM orden GROUP BY id_Comida " +
                "UNION " +
                "SELECT id_Bebida AS id_Producto, 'Bebida' AS tipo, COUNT(id_Bebida) AS cantidad_ventas " +
                "FROM orden GROUP BY id_Bebida";

        try {
            Statement stmt = Conexion.connection.createStatement();
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                OrdenDAO producto = new OrdenDAO();
                producto.setIdComida(res.getInt("id_Producto"));
                producto.setIdBebida(res.getInt("id_Producto"));
                producto.setCantidad(res.getInt("cantidad_ventas"));
                //producto.setTipo(res.getString("tipo"));
                listaProductosMasVendidos.add(producto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaProductosMasVendidos;
    }

    public ObservableList<String> obtenerEmpleadoMasVentas() {
        ObservableList<String> empleadoMasVentas = FXCollections.observableArrayList();

        String query = "SELECT e.nomEmpleado, COUNT(*) as ventas_realizadas " +
                "FROM orden o " +
                "JOIN empleado e ON o.id_Empleado = e.id_Empleado " +
                "GROUP BY e.nomEmpleado " +
                "ORDER BY ventas_realizadas DESC " +
                "LIMIT 1";

        try {
            Statement stmt = Conexion.connection.createStatement();
            ResultSet res = stmt.executeQuery(query);
            if (res.next()) {
                String nombreEmpleado = res.getString("nomEmpleado");
                int ventasRealizadas = res.getInt("ventas_realizadas");
                empleadoMasVentas.add(nombreEmpleado + ":" + ventasRealizadas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return empleadoMasVentas;
    }

    public ObservableList<OrdenDAO> obtenerVentasPorDia() {
        ObservableList<OrdenDAO> ventasPorDia = FXCollections.observableArrayList();

        String query = "SELECT DATE(fecha) AS fecha, COUNT(*) AS cantidad_ventas FROM orden GROUP BY DATE(fecha)";

        try {
            Statement stmt = Conexion.connection.createStatement();
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                OrdenDAO venta = new OrdenDAO();
                venta.setFecha(res.getString("fecha"));
                venta.setCantidad(res.getInt("cantidad_ventas"));
                ventasPorDia.add(venta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ventasPorDia;
    }



    public String getNombreComidaPorId(int idComida) {
        String nombreComida = null;
        String query = "SELECT nombre FROM comida WHERE id_Comida = ?";
        try (PreparedStatement stmt = Conexion.connection.prepareStatement(query)) {
            stmt.setInt(1, idComida);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                nombreComida = res.getString("nombre");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombreComida;
    }

    public String getNombreBebidaPorId(int idBebida) {
        String nombreBebida = null;
        String query = "SELECT nombre FROM bebida WHERE id_Bebida = ?";
        try (PreparedStatement stmt = Conexion.connection.prepareStatement(query)) {
            stmt.setInt(1, idBebida);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                nombreBebida = res.getString("nombre");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombreBebida;
    }

}
