package com.example.proyectotaqueria;

import com.example.proyectotaqueria.modelos.Conexion;
import com.example.proyectotaqueria.modelos.UsuarioDAO;
import com.example.proyectotaqueria.vistas.ComidaTaqueria;
import com.example.proyectotaqueria.vistas.EmpleadoTaqueria;
import com.example.proyectotaqueria.vistas.Mesas;
import com.mysql.cj.conf.ConnectionUrlParser;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private BorderPane bdpPanel;


    @Override
    public void start(Stage stage) throws IOException {
        bdpPanel = new BorderPane();

        crearMenu();

        Scene scene = new Scene(bdpPanel);
        scene.getStylesheets().add(getClass().getResource("/estilos/styles.css").toString());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        Conexion.crearConexion();
    }

    private void crearMenu() {
        TabPane tabPane = new TabPane();

        Tab tab1 = new Tab("Pestaña 1");
        tab1.setContent(new Mesas(0));

        Tab tab2 = new Tab("Pestaña 2");
        VBox tab2Content = new VBox();
        tab2.setContent(tab2Content);
        tabPane.getTabs().add(tab2);
        tab2.setOnSelectionChanged(event -> {
            if (tab2.isSelected()) {
                if (verificarCredenciales()) {
                    tab2Content.getChildren().add(new EmpleadoTaqueria());
                } else {
                    tabPane.getSelectionModel().selectFirst();
                }
            }
        });

        Tab tab3 = new Tab("Pestaña 3");
        VBox tab3Content = new VBox();
        tab3.setContent(tab3Content);
        tabPane.getTabs().add(tab3);
        tab3.setOnSelectionChanged(event -> {
            if (tab3.isSelected()) {
                if (verificarCredenciales()) {
                    tab3Content.getChildren().add(new ComidaTaqueria());
                } else {
                    tabPane.getSelectionModel().selectFirst();
                }
            }
        });

        tabPane.getTabs().add(tab1);
        bdpPanel.setCenter(tabPane);
    }

    private boolean verificarCredenciales() {
        // Mostrar un cuadro de diálogo para ingresar usuario y contraseña
        Dialog<ConnectionUrlParser.Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Iniciar sesión");
        dialog.setHeaderText("Por favor, ingresa tu usuario y contraseña.");

        // Botones de aceptar y cancelar
        ButtonType loginButtonType = new ButtonType("Iniciar sesión", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Campos de usuario y contraseña
        TextField username = new TextField();
        username.setPromptText("Usuario");
        PasswordField password = new PasswordField();
        password.setPromptText("Contraseña");

        // Agregar campos al cuadro de diálogo
        dialog.getDialogPane().setContent(new VBox(10, username, password));

        // Habilitar el botón de inicio de sesión cuando se ingresen datos
        Button loginButton = (Button) dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Validar campos de usuario y contraseña
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        // Obtener los resultados del diálogo
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new ConnectionUrlParser.Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        // Mostrar el cuadro de diálogo y esperar a que el usuario ingrese las credenciales
        dialog.showAndWait();

        // Verificar las credenciales ingresadas con las de la base de datos
        String usuario = username.getText();
        String contraseña = password.getText();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        for (UsuarioDAO usuarioDB : usuarioDAO.consultar()) {
            if (usuarioDB.getUsuario().equals(usuario) && usuarioDB.getContrasena().equals(contraseña)) {
                return true; // Credenciales válidas
            }
        }
        return false; // Credenciales inválidas
    }

    public static void main(String[] args) {
        launch();
    }
}
