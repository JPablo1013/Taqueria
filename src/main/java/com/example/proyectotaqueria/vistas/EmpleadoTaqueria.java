package com.example.proyectotaqueria.vistas;

import com.example.proyectotaqueria.components.ButtonCell;
import com.example.proyectotaqueria.exportacion.Lista_Pdf;
import com.example.proyectotaqueria.modelos.Conexion;
import com.example.proyectotaqueria.modelos.EmpleadosDAO;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

//import static jdk.jpackage.internal.WixAppImageFragmentBuilder.ShortcutsFolder.Desktop;

public class EmpleadoTaqueria extends Pane {
    private Panel pnlPrincipal;
    private BorderPane bpnPrincipal;
    private VBox vbxPrincipal;
    private ToolBar tblMenu;
    private Scene escena;
    private TableView<EmpleadosDAO> tblEmpleados;
    private Button btnAddEmpleado;
    private Button btnGeneratePDF; // Nuevo botón para generar el PDF

    public static final String DEST1 = "results/Lista_de_tareas.pdf";

    protected void onViewReportPDF() {
        try {
            File file = new File(DEST1);
            file.getParentFile().mkdirs();
            new Lista_Pdf().createPdf(DEST1);
            openFile(DEST1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void openFile(String filename) {
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(filename);
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public EmpleadoTaqueria(){
        CrearUI();
    }

    private void CrearUI() {
        Conexion.crearConexion();

        ImageView imvempleado = new ImageView(getClass().getResource("/images/employee.png").toString());
        imvempleado.setFitHeight(50);
        imvempleado.setFitWidth(50);

        btnAddEmpleado = new Button();
        btnAddEmpleado.setOnAction(event -> new EmpleadosForm(tblEmpleados, null));
        btnAddEmpleado.setPrefSize(50, 50);
        btnAddEmpleado.setGraphic(imvempleado);

        // Botón para generar el PDF
        btnGeneratePDF = new Button("Generar PDF");
        btnGeneratePDF.setOnAction(event -> onViewReportPDF());

        tblMenu = new ToolBar(btnAddEmpleado, btnGeneratePDF);

        CreateTable();

        bpnPrincipal = new BorderPane();
        bpnPrincipal.setTop(tblMenu);
        bpnPrincipal.setCenter(tblEmpleados);

        pnlPrincipal = new Panel("Taqueria");
        pnlPrincipal.setBody(bpnPrincipal);
        pnlPrincipal.getStyleClass().add("panel-info");

        escena = new Scene(pnlPrincipal, 600, 400);
        escena.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        this.getChildren().add(pnlPrincipal);
    }

    private void CreateTable(){
        EmpleadosDAO objEmp = new EmpleadosDAO();
        tblEmpleados = new TableView<>();

        TableColumn<EmpleadosDAO, String> tbcNomEmp = new TableColumn<>("Empleado");
        tbcNomEmp.setCellValueFactory(new PropertyValueFactory<>("nomEmpleado"));

        TableColumn<EmpleadosDAO, String> tbcRFC = new TableColumn<>("RFC");
        tbcRFC.setCellValueFactory(new PropertyValueFactory<>("rfc"));

        TableColumn<EmpleadosDAO, Float> tbsSueldo = new TableColumn<>("SALARIO");
        tbsSueldo.setCellValueFactory(new PropertyValueFactory<>("salario"));

        TableColumn<EmpleadosDAO, String> tbcTelefono = new TableColumn<>("TELEFONO");
        tbcTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        TableColumn<EmpleadosDAO, String> tbcDireccion = new TableColumn<>("DIRECCION");
        tbcDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        TableColumn<EmpleadosDAO, String> tbcEditar = new TableColumn<>("Editar");
        tbcEditar.setCellFactory(
                new Callback<TableColumn<EmpleadosDAO, String>, TableCell<EmpleadosDAO, String>>() {
                    @Override
                    public TableCell<EmpleadosDAO, String> call(TableColumn<EmpleadosDAO, String> empleadosDAOStringTableColumn) {
                        return new ButtonCell(1);
                    }
                }
        );

        TableColumn<EmpleadosDAO, String> tbcEliminar = new TableColumn<>("ELIMINAR");
        tbcEliminar.setCellFactory(
                new Callback<TableColumn<EmpleadosDAO, String>, TableCell<EmpleadosDAO, String>>() {
                    @Override
                    public TableCell<EmpleadosDAO, String> call(TableColumn<EmpleadosDAO, String> empleadosDAOStringTableColumn) {
                        return new ButtonCell(2);
                    }
                }
        );

        tblEmpleados.getColumns().addAll(tbcNomEmp, tbcRFC, tbsSueldo, tbcTelefono, tbcDireccion, tbcEditar, tbcEliminar);
        tblEmpleados.setItems(objEmp.CONSULTAR());
    }
}
