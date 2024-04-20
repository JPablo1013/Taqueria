package com.example.proyectotaqueria.modelos;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OrdenTemporal {
    private int idEmpleado;
    private ComidaDAO comida;
    private SimpleStringProperty nombreComida;
    private SimpleFloatProperty precio;
    private SimpleIntegerProperty cantidad;
    private int noMesa;

    public OrdenTemporal(int idEmpleado, ComidaDAO comida, float precio, int cantidad, int noMesa) {
        this.idEmpleado = idEmpleado;
        this.comida = comida;
        this.nombreComida = new SimpleStringProperty(comida.getNombre());
        this.precio = new SimpleFloatProperty(precio);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.noMesa = noMesa;
    }

    public String getNombreComida() {
        return nombreComida.get();
    }

    public SimpleStringProperty nombreComidaProperty() {
        return nombreComida;
    }

    public float getPrecio() {
        return precio.get();
    }

    public SimpleFloatProperty precioProperty() {
        return precio;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getIdComida() {
        return comida.getIdComida();
    }

    public int getCantidad() {
        return cantidad.get();
    }

    public SimpleIntegerProperty cantidadProperty() {
        return cantidad;
    }

    public int getNoMesa() {
        return noMesa;
    }

    public void setNoMesa(int noMesa) {
        this.noMesa = noMesa;
    }
}
