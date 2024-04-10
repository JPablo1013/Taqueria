package com.example.proyectotaqueria.components;

import com.example.proyectotaqueria.modelos.EmpleadosDAO;
import com.example.proyectotaqueria.vistas.EmpleadosForm;
import javafx.scene.control.*;

import java.util.Optional;

public class ButtonCell extends TableCell<EmpleadosDAO,String> {
    Button btnCelda;
    int opc;
    EmpleadosDAO objEmp;

    public ButtonCell(int opc){
        this.opc = opc;
        String txtButton = (opc == 1) ? "Editar" : "Eliminar";
        btnCelda = new Button(txtButton);
        btnCelda.setOnAction(event -> AccionBoton(opc));
    }

    private void AccionBoton(int opc){
        TableView<EmpleadosDAO> tbcEmpleados = ButtonCell.this.getTableView();
        objEmp = tbcEmpleados.getItems().get(ButtonCell.this.getIndex());
        if(opc == 1){
            new EmpleadosForm(tbcEmpleados, objEmp);

        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deseas Continuar");
            alert.setHeaderText("¿Seguro, ya preguntaste?");
            alert.setContentText("mejor peguntale a "+ objEmp.getNomEmpleado() + " si quiere ser borrado");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                objEmp.ELIMINAR();
                tbcEmpleados.setItems(objEmp.CONSULTAR());
                tbcEmpleados.refresh();
            }
        }
    }

    @Override
    protected void updateItem(String item, boolean empty){
        super.updateItem(item, empty);
        if(!empty)
            this.setGraphic(btnCelda);
    }
}
