package com.example.proyectotaqueria.modelos;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    static private String DB = "Tacos_de_perro";
    static private String USER = "admintacos";
    static private String PASSWORD = "123";
    static public Connection connection;
    public static void crearConexion(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3307/"+DB+"?allowPublicKeyRetrieval=true&useSSL=false",USER,PASSWORD);
            System.out.println("conexion establecida con exito :D");
        }catch (Exception e){
            System.out.println("Error al establecer la conexión:");
            e.printStackTrace();
        }
    }

}
