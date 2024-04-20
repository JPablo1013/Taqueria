package com.example.proyectotaqueria.modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.HashMap;
import java.util.Map;

public class OrdenesManager {
    private static OrdenesManager instance;
    private Map<Integer, ObservableList<OrdenTemporal>> ordenesPorMesa;

    private OrdenesManager() {
        this.ordenesPorMesa = new HashMap<>();
    }

    public static OrdenesManager getInstance() {
        if (instance == null) {
            instance = new OrdenesManager();
        }
        return instance;
    }

    public void insertarOrdenTemporal(OrdenTemporal ordenTemporal) {
        int noMesa = ordenTemporal.getNoMesa();
        if (!ordenesPorMesa.containsKey(noMesa)) {
            ordenesPorMesa.put(noMesa, FXCollections.observableArrayList());
        }
        ordenesPorMesa.get(noMesa).add(ordenTemporal);
    }

    public ObservableList<OrdenTemporal> getOrdenesTemporalesPorMesa(int noMesa) {
        return ordenesPorMesa.getOrDefault(noMesa, FXCollections.observableArrayList());
    }

    public void procesarOrdenTemporal(OrdenTemporal ordenTemporal) {
        int noMesa = ordenTemporal.getNoMesa();
        if (ordenesPorMesa.containsKey(noMesa)) {
            ordenesPorMesa.get(noMesa).remove(ordenTemporal);
        }
    }

    public void limpiarOrdenesTemporales() {
        ordenesPorMesa.clear();
    }
}
