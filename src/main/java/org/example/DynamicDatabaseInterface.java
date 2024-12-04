package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DynamicDatabaseInterface extends JPanel {

    // JDBC connection parameters
    private static final String JDBC_URL = "jdbc:sqlite:D:\\MaquinasVirtuais\\a23sergiogr\\AD_BD\\h2biblioteca\\EscuelaMusica.db";
    private static final String JDBC_USER = "";
    private static final String JDBC_PASSWORD = "";

    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel formPanel;
    private List<JTextField> fields;
    private List<String> columnNames;
    private List<String> columnTypes;
    private JButton btnAdd, btnEdit, btnDelete;
    private boolean isEditing = false; // Flag to check if editing mode is active
    private int selectedId = -1; // Store the ID of the selected record for editing

    public DynamicDatabaseInterface() {
        // Set up the JFrame

        // Set up the layout
        setLayout(new BorderLayout());

        // Create the table to display database data
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Age"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create the input form panel
        formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2)); // Dynamic grid layout to accommodate fields
        fields = new ArrayList<>();
        columnNames = new ArrayList<>();
        columnTypes = new ArrayList<>();

        // Create buttons for adding, updating, and deleting records
        JPanel buttonPanel = new JPanel();
        btnAdd = new JButton("Add Record");
        btnEdit = new JButton("Edit Record");
        btnDelete = new JButton("Delete Record");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.SOUTH);

        add(formPanel, BorderLayout.NORTH); // Add form panel at the top

        // Add listeners for button actions
        btnAdd.addActionListener(e -> addRecord());
        btnEdit.addActionListener(e -> editRecord());
        btnDelete.addActionListener(e -> deleteRecord());

        // Load data from the database to display in the table
        loadData();

        // Add selection listener for the table
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                // Get data from the selected row
                selectedId = (int) table.getValueAt(selectedRow, 0);
                for (int i = 0; i < columnNames.size(); i++) {
                    fields.get(i).setText(table.getValueAt(selectedRow, i + 1).toString());
                }
                isEditing = true; // Set editing mode to true
            }
        });
    }

    // Load data from the database and display it in the table
    private void loadData() {
        // Clear the existing data in the table
        tableModel.setRowCount(0);
        fields.clear(); // Clear the form fields

        EscolaMusicaConnectionManager connectionManager = EscolaMusicaConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        // Get column names and types dynamically from the database
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM estudiante")) {

            // Retrieve the metadata to get column names and types
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Clear previous column names and types
            columnNames.clear();
            columnTypes.clear();

            // Set up the columns dynamically based on the metadata
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
                columnTypes.add(metaData.getColumnTypeName(i));
                tableModel.addColumn(metaData.getColumnName(i));
            }

            // Now load all the records into the table
            try (ResultSet rs = statement.executeQuery("SELECT * FROM estudiante")) {
                while (rs.next()) {
                    Object[] row = new Object[columnCount + 1]; // 1 extra for ID column
                    row[0] = rs.getInt("id_persona"); // Assuming "id" is the first column
                    for (int i = 1; i <= columnCount; i++) {
                        row[i] = rs.getObject(i);
                    }
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data from the database", "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        // Now dynamically create form fields for each column
        createFormFields();
    }

    // Create input fields based on the column names and types
    private void createFormFields() {
        formPanel.removeAll(); // Clear previous fields
        fields.clear();

        // Loop through column names and types to create input fields
        for (int i = 1; i < columnNames.size(); i++) { // Skip the "ID" column
            String columnName = columnNames.get(i);
            JLabel label = new JLabel(columnName + ": ");
            formPanel.add(label);

            JTextField textField = new JTextField(20);
            fields.add(textField);
            formPanel.add(textField);
        }

        formPanel.revalidate(); // Revalidate the form panel to show the new fields
        formPanel.repaint(); // Repaint to reflect changes
    }

    // Add a new record to the database
    private void addRecord() {
        StringBuilder query = new StringBuilder("INSERT INTO estudiante (");
        StringBuilder values = new StringBuilder(" VALUES (");

        // Collect field values for insertion
        List<String> fieldValues = new ArrayList<>();
        for (JTextField field : fields) {
            fieldValues.add("'" + field.getText() + "'");
        }

        // Construct the query
        for (String column : columnNames.subList(1, columnNames.size())) { // Skip "ID"
            query.append(column).append(",");
            values.append("?").append(",");
        }

        query.deleteCharAt(query.length() - 1); // Remove last comma
        values.deleteCharAt(values.length() - 1); // Remove last comma
        query.append(")").append(values).append(")");

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query.toString())) {

            // Add values to the query
            for (int i = 0; i < fields.size(); i++) {
                stmt.setString(i + 1, fields.get(i).getText());
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Record added successfully!");
                loadData(); // Reload data to reflect the new record
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding record to the database", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Edit the selected record in the database
    private void editRecord() {
        if (!isEditing) {
            JOptionPane.showMessageDialog(this, "Please select a record to edit", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder query = new StringBuilder("UPDATE students SET ");

        // Collect field values for updating
        List<String> fieldValues = new ArrayList<>();
        for (int i = 0; i < fields.size(); i++) {
            query.append(columnNames.get(i + 1)).append(" = ?, ");
        }

        query.delete(query.length() - 2, query.length()); // Remove the last comma

        query.append(" WHERE id = ?");

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query.toString())) {

            // Add values to the query
            for (int i = 0; i < fields.size(); i++) {
                stmt.setString(i + 1, fields.get(i).getText());
            }

            stmt.setInt(fields.size() + 1, selectedId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Record updated successfully!");
                loadData(); // Reload data to reflect the updated record
                isEditing = false; // Reset the editing flag
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating record in the database", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Delete the selected record from the database
    private void deleteRecord() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {
            int id = (int) table.getValueAt(selectedRow, 0);

            String query = "DELETE FROM students WHERE id = ?";

            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, id);

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Record deleted successfully!");
                    loadData(); // Reload data to reflect the deleted record
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting record from the database", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a record to delete", "Selection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DynamicDatabaseInterface().setVisible(true);
        });
    }
}