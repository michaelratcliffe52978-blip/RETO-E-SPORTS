package org.example.programacion.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Configuración de la base de datos
    private static final String URL = "jdbc:oracle:thin:@//172.20.225.114:1521/ORCLPDB1";
    private static final String USER = "eqdaw04";
    private static final String PASS = "eqdaw04";

    /**
     * Método estático para obtener la conexión.
     * @return Connection objeto de conexión abierto.
     * @throws SQLException si hay un error al conectar.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}