package org.example;

import javax.swing.*;
import java.sql.*;
import java.util.List;

public class AccesoBBDD {
    private final Outer ventana;
    private final DynamicDatabaseInterface databaseInterface;
    private final Connection connection;
    private static AccesoBBDD instance;
    private static String tableName;

    private AccesoBBDD(){
        this.connection = EscolaMusicaConnectionManager.getInstance().getConnection();
        ventana = new Outer();
        ventana.setVisible(true);
        databaseInterface = ventana.databaseInterface; // Obtén la misma instancia que se usa en la GUI
    }

    public static AccesoBBDD getInstance() {
        if (instance == null)
            instance = new AccesoBBDD();
        return instance;
    }

    public void showTable(String tableName, ResultSetHandler handler) {
        this.tableName = tableName;
        String query = "SELECT * FROM " + tableName;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            handler.handle(resultSet); // Pasar el ResultSet a un manejador definido por la interfaz
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showTable(ResultSetHandler handler) {
        String query = "SELECT * FROM " + tableName;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            handler.handle(resultSet); // Pasar el ResultSet a un manejador definido por la interfaz
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addValue(List<String> columns, List<String> values) {
        StringBuilder query = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
        query.append(String.join(", ", columns)).append(") VALUES (");
        query.append("?,".repeat(columns.size()));
        query.setLength(query.length() - 1); // Eliminar última coma
        query.append(")");

        try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < values.size(); i++) {
                preparedStatement.setString(i + 1, values.get(i));
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding value: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateValue(int id, List<String> columns, List<String> values) {
        StringBuilder query = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        for (String column : columns) {
            query.append(column).append(" = ?, ");
        }
        query.setLength(query.length() - 2); // Eliminar última coma
        query.append(" WHERE id_persona = ?");

        try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
            int index = 1;
            for (String value : values) {
                preparedStatement.setString(index++, value);
            }
            preparedStatement.setInt(index, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating value: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteValue(int id) {
        String query = "DELETE FROM " + tableName + " WHERE id_persona = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting value: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
