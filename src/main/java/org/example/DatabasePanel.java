package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DatabasePanel extends JPanel {

    public DatabasePanel() {
        super(new BorderLayout());
        // Create the table to display the database data
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);

        // Fetch data from the database and set it to the table
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Nombre", "Apellido1", "Apellido2", "Code", "Date", "Street"});

        // Set the table model
        table.setModel(model);

        // Fetch data from the database and populate the table
        fetchDataAndPopulateTable(model);

        // Add the scroll pane (with the table) to the JPanel
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void fetchDataAndPopulateTable(DefaultTableModel model) {
        // Database connection information (you'll need to update with your actual database details)
        String jdbcUrl = "jdbc:mysql://localhost:3306/your_database_name"; // Example: replace with your database URL
        String jdbcUser = "";
        String jdbcPassword = "";
        String sqlQuery = "SELECT nombre, apellido1, apellido2, code, date, street FROM estudiante"; // Replace with your table name

        EscolaMusicaConnectionManager connectionManager = EscolaMusicaConnectionManager.getInstance();
        Connection conn = connectionManager.getConnection();

        // JDBC connection and query execution
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery)) {

            // Fetch data and add to the table model
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String apellido1 = rs.getString("apellido1");
                String apellido2 = rs.getString("apellido2");
                String code = rs.getString("code");
                String date = rs.getString("date"); // Make sure the date is stored as a string or format as needed
                String street = rs.getString("street");

                // Add each row to the table model
                model.addRow(new Object[]{nombre, apellido1, apellido2, code, date, street});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data from the database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}