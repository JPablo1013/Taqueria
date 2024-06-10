package com.example.proyectotaqueria.modelos;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OrdenTemporal {
    private int idEmpleado;
    private Object item; // Puede ser ComidaDAO o BebidaDAO
    private SimpleStringProperty nombreItem;
    private SimpleFloatProperty precio;
    private SimpleIntegerProperty cantidad;
    private int noMesa;
    private int idComida;
    private int idBebida;

    public int getNumeroMesa() {
        return noMesa;
    }


    public OrdenTemporal(int idEmpleado, Object item, float precio, int cantidad, int noMesa) {
        this.idEmpleado = idEmpleado;
        this.item = item;
        this.nombreItem = new SimpleStringProperty(obtenerNombreItem());
        this.precio = new SimpleFloatProperty(precio);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.noMesa = noMesa;
        // Obtener los ID de la comida o bebida
        obtenerIdItem();
    }

    public String obtenerNombreItem() {
        if (item instanceof ComidaDAO) {
            return ((ComidaDAO) item).getNombre();
        } else if (item instanceof BebidaDAO) {
            return ((BebidaDAO) item).getNombre();
        }
        return "";
    }

    public void obtenerIdItem() {
        if (item instanceof ComidaDAO) {
            idComida = ((ComidaDAO) item).getIdComida();
            idBebida = 0; // No hay bebida asociada
        } else if (item instanceof BebidaDAO) {
            idComida = 0; // No hay comida asociada
            idBebida = ((BebidaDAO) item).getIdBebida();
        } else {
            throw new IllegalStateException("El objeto item no es ni una ComidaDAO ni una BebidaDAO.");
        }
    }

    public SimpleIntegerProperty cantidadProperty() {
        return cantidad;
    }

    public SimpleFloatProperty precioProperty() {
        return precio;
    }

    public SimpleStringProperty nombreItemProperty() {
        return nombreItem;
    }

    public Object getItem() {
        return item;
    }

    public void setIdComida(int idComida) {
        this.idComida = idComida;
    }

    public void setIdBebida(int idBebida) {
        this.idBebida = idBebida;
    }

    public String getNombreItem() {
        return nombreItem.get();
    }

    public float getPrecio() {
        return precio.get();
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getCantidad() {
        return cantidad.get();
    }

    public int getNoMesa() {
        return noMesa;
    }

    public void setNoMesa(int noMesa) {
        this.noMesa = noMesa;
    }

    public int getIdComida() {
        return idComida;
    }

    public int getIdBebida() {
        return idBebida;
    }
}
