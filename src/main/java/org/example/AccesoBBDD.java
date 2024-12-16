package org.example;

import javax.swing.*;
import java.sql.*;

public class AccesoBBDD {
    private final Outer ventana;
    private final DynamicDatabaseInterface databaseInterface;
    private final Connection connection;
    private static AccesoBBDD instance;

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

    public void showTable(TablesNames name) {
        String query = "SELECT * FROM " + name.name();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
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

    public void addValue(PreparedStatement statement){

    }

    public void deleteValue(TablesNames name, Integer id){
        try {
            String query = "DELETE FROM " + name.name() + " WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Record deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No record found with ID: " + id, "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error deleting data: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void updateValue(TablesNames name, String values){

    }

}
