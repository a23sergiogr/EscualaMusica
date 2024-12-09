package org.example;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException {
        EscolaMusicaConnectionManager connectionManager = EscolaMusicaConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();

        // Instancia de Outer que ya contiene la tabla
        Outer ventana = new Outer();
        DynamicDatabaseInterface databaseInterface = ventana.databaseInterface; // Obtén la misma instancia que se usa en la GUI

        ventana.setVisible(true);

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM estudiante")) {
            databaseInterface.loadData(resultSet); // Carga los datos en la tabla visible
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error fetching data from the database",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}