package org.example.programacion.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Esta clase es la encargada de establecer el puente entre nuestra aplicación Java
 * y el servidor de base de datos Oracle.
 * Centraliza la configuración de red y credenciales para que el resto de DAOs
 * puedan obtener una conexión activa de forma sencilla.
 * * @author equipo4
 * @version 1.0
 */
public class ConexionBD {

    /** * URL de conexión JDBC para Oracle.
     * Incluye el protocolo thin, la IP del servidor, el puerto (1521) y el Service Name (ORCLPDB1).
     */
    private static final String URL = "jdbc:oracle:thin:@//172.20.225.114:1521/ORCLPDB1";

    /** Usuario de la base de datos asignado al grupo */
    private static final String USER = "eqdaw04";

    /** Contraseña de acceso para el usuario eqdaw04 */
    private static final String PASS = "eqdaw04";

    /**
     * Método estático que solicita y devuelve una conexión abierta con la base de datos.
     * Es utilizado por todos los métodos de los DAOs mediante el bloque try-with-resources
     * para asegurar que los datos fluyan correctamente.
     * * @return Connection Un objeto de conexión listo para ejecutar sentencias SQL.
     * @throws SQLException Si los datos de red son incorrectos o el servidor no responde.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}