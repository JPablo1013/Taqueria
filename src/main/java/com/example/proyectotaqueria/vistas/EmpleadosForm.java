package com.example.proyectotaqueria.vistas;

import com.example.proyectotaqueria.modelos.EmpleadosDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EmpleadosForm extends Stage {
    private TableView<EmpleadosDAO> tblEmpleados;
    private EmpleadosDAO objEmp;
    String[] arPromts ={"Nombre del Empleado", "Telefono", "Direccion", "RFC", "Salario"};
    private Scene escena;
    private TextField[] arTxtCampos = new TextField[5];
    private Button btnGuardar;
    private VBox vbxPrincipal;

    public EmpleadosForm(TableView<EmpleadosDAO> tbvEmp, EmpleadosDAO ojbEmp) {
        tblEmpleados = tbvEmp;
        this.objEmp = (objEmp == null ) ? new EmpleadosDAO() : objEmp;
        CrearUi();
        this.setTitle("Inserciones de Usuario");
        this.setScene(escena);
        this.show();
    }

    private void CrearUi() {
        vbxPrincipal = new VBox();
        vbxPrincipal.setPadding(new Insets(10));
        vbxPrincipal.setSpacing(20);
        vbxPrincipal.setAlignment(Pos.CENTER);

        for (int i = 0; i < arTxtCampos.length; i++) {
            arTxtCampos[i] = new TextField();
            arTxtCampos[i].setPromptText(arPromts[i]);
            vbxPrincipal.getChildren().add(arTxtCampos[i]);
        }
        LlenarForm();
        btnGuardar = new Button("Guardar");
        btnGuardar.setOnAction(event -> GuardarEmpleado());
        vbxPrincipal.getChildren().add(btnGuardar);
        escena = new Scene(vbxPrincipal, 200, 300);
    }

    private void LlenarForm() {
        arTxtCampos[0].setText(objEmp.getNomEmpleado());
        arTxtCampos[1].setText(String.valueOf(objEmp.getTelefono()));
        arTxtCampos[2].setText(objEmp.getDireccion());
        arTxtCampos[3].setText(objEmp.getRfc());
        arTxtCampos[4].setText(objEmp.getSalario() + "");

        // Verificar si el id_Empleado es mayor que 0 para determinar si se está editando un empleado existente
        if (objEmp.getId_Empleado() > 0) {
            // Si se está editando un empleado existente, deshabilitar la edición del id_Empleado
            arTxtCampos[0].setDisable(true);
        }
    }

    private void GuardarEmpleado(){
        objEmp.setNomEmpleado(arTxtCampos[0].getText());
        objEmp.setTelefono(Integer.parseInt(arTxtCampos[1].getText()));
        objEmp.setDireccion(arTxtCampos[2].getText());
        objEmp.setRfc(arTxtCampos[3].getText());
        objEmp.setSalario(Float.parseFloat(arTxtCampos[4].getText()));
        if(objEmp.getId_Empleado()<0) {
            objEmp.INSERTAR();
        }
        else {
            objEmp.ACTUALIZAR();
        }
        tblEmpleados.setItems(objEmp.CONSULTAR());
        tblEmpleados.refresh();

        arTxtCampos[0].clear();
        arTxtCampos[1].clear();
        arTxtCampos[2].clear();
        arTxtCampos[3].clear();
        arTxtCampos[4].clear();
    }
}