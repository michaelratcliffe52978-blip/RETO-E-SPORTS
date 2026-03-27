package org.example.programacion.Utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BDConnection {
    //AQUI SE HACEN LAS CONEXIONES A LA BASE DE DATOS
    private static final String URL = "jdbc:mysql://localhost:3306/esport";
    private static final String USER = "root";
    private static final String PASSWORD = "usbw";
    private static Connection conn = null;

    public static Connection getConnection() throws Exception {
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
        return conn;
    }
    public static void closeConnection() throws SQLException {
        conn.close();
    }

    //LA IDENTIFICACION DEL DRIVER QUE HAY QUE UTILIZAR
    static {
        //EN JAVA MODERNO NO HACE FALTA
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver de MySQL: " + e.getMessage());
        }
    }


}