package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class EscolaMusicaConnectionManager {
    private static volatile EscolaMusicaConnectionManager instance;
    private Connection connection;

    // Usando ruta relativa para la base de datos
    private final String URL = "jdbc:sqlite:src\\main\\resources\\EscuelaMusicaDos.db";
    private final String USER = "";  // SQLite no requiere usuario ni contraseña
    private final String PASSWORD = ""; // Si fuera necesario

    // Constructor privado para el patrón Singleton
    private EscolaMusicaConnectionManager() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión establecida con éxito.");
        } catch (SQLException e) {
            System.err.println("Error al establecer la conexión: " + e.getMessage());
        }
    }

    // Método para obtener la instancia única
    public static EscolaMusicaConnectionManager getInstance() {
        if (instance == null) {
            synchronized (EscolaMusicaConnectionManager.class) {
                if (instance == null) {
                    instance = new EscolaMusicaConnectionManager();
                }
            }
        }
        return instance;
    }

    // Método para obtener la conexión
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                synchronized (EscolaMusicaConnectionManager.class) {
                    if (connection == null || connection.isClosed()) {
                        connection = DriverManager.getConnection(URL, USER, PASSWORD);
                        System.out.println("Conexión reabierta con éxito.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al reabrir la conexión: " + e.getMessage());
        }
        return connection;
    }

    // Método para cerrar la conexión
    public void endConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión cerrada con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
