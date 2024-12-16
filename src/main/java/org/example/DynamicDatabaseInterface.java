package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DynamicDatabaseInterface extends JPanel {
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
        // Configurar el layout principal
        setLayout(new BorderLayout());

        // Crear el modelo de tabla sin columnas por defecto
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Crear el panel de formulario
        formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2)); // Layout dinámico para los campos
        fields = new ArrayList<>();
        columnNames = new ArrayList<>();
        columnTypes = new ArrayList<>();
        add(formPanel, BorderLayout.NORTH);

        // Crear el panel de botones
        JPanel buttonPanel = new JPanel();
        btnAdd = new JButton("Add Record");
        btnEdit = new JButton("Edit Record");
        btnDelete = new JButton("Delete Record");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.SOUTH);

        // Listeners para los botones
        btnAdd.addActionListener(e -> addRecord());
        btnEdit.addActionListener(e -> editRecord());
        btnDelete.addActionListener(e -> deleteRecord());
    }

    public void loadData(ResultSet resultSet) throws SQLException {
        // Limpiar el modelo de la tabla y las listas dinámicas
        tableModel.setRowCount(0);  // Limpiar las filas
        tableModel.setColumnCount(0);  // Limpiar las columnas
        fields.clear();
        columnNames.clear();
        columnTypes.clear();

        // Obtener los metadatos del ResultSet
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Configurar columnas dinámicamente
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
            columnTypes.add(metaData.getColumnTypeName(i));
            tableModel.addColumn(metaData.getColumnName(i));
        }

        // Cargar las filas del ResultSet en la tabla
        while (resultSet.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = resultSet.getObject(i + 1);
            }
            tableModel.addRow(row);
        }

        // Crear campos de formulario dinámicamente
        createFormFields();

        // Añadir listener para seleccionar filas
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                selectedId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
                for (int i = 0; i < columnNames.size(); i++) {
                    fields.get(i).setText(table.getValueAt(selectedRow, i).toString());
                }
                isEditing = true; // Modo edición activado
            }
        });

        // Refrescar la vista de la tabla
        tableModel.fireTableStructureChanged();
        table.revalidate();
        table.repaint();
    }

    // Crear campos dinámicamente basados en los metadatos de la tabla
    private void createFormFields() {
        // Limpia el panel de formulario y las listas de campos
        formPanel.removeAll();
        fields.clear();

        // Verifica si hay columnas para evitar errores
        if (columnNames.isEmpty()) {
            System.out.println("No se encontraron columnas para crear campos.");
            return;
        }

        // Ajusta el diseño del panel para adaptarse al número de columnas
        formPanel.setLayout(new GridLayout(columnNames.size(), 2));

        // Agrega campos dinámicamente
        for (int i = 0; i < columnNames.size(); i++) {
            String columnName = columnNames.get(i);
            JLabel label = new JLabel(columnName + ": ");
            formPanel.add(label);

            JTextField textField = new JTextField(20);
            fields.add(textField);
            formPanel.add(textField);
        }

        // Forzar la actualización de la interfaz
        formPanel.revalidate();
        formPanel.repaint();
        this.revalidate();
        this.repaint();

        System.out.println("Campos creados: " + fields.size());
    }

    private void addRecord() {
        try {
            // Verificar conexión a la base de datos
            EscolaMusicaConnectionManager connectionManager = EscolaMusicaConnectionManager.getInstance();
            Connection connection = connectionManager.getConnection();

            // Crear un SQL dinámico basado en los nombres de las columnas
            StringBuilder sql = new StringBuilder("INSERT INTO estudiante (");
            sql.append(String.join(", ", columnNames)); // Agregar nombres de columnas
            sql.append(") VALUES (");
            sql.append("?,".repeat(columnNames.size()));
            sql.setLength(sql.length() - 1); // Eliminar la última coma
            sql.append(")");

            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());

            // Establecer los valores de los campos dinámicos
            for (int i = 0; i < fields.size(); i++) {
                String value = fields.get(i).getText();
                preparedStatement.setString(i + 1, value); // Aquí puedes ajustar según el tipo de dato
            }

            // Ejecutar la consulta
            preparedStatement.executeUpdate();

            // Actualizear la tabla cargando los datos nuevamente
            refreshTable();

            // Limpiar los campos
            clearFormFields();
            JOptionPane.showMessageDialog(this, "Registro añadido exitosamente.");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al añadir el registro: " + ex.getMessage());
        }
    }

    private void editRecord() {
        if (!isEditing || selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un registro para editar.");
            return;
        }

        try {
            // Verificar conexión a la base de datos
            EscolaMusicaConnectionManager connectionManager = EscolaMusicaConnectionManager.getInstance();
            Connection connection = connectionManager.getConnection();

            // Crear un SQL dinámico para la actualización
            StringBuilder sql = new StringBuilder("UPDATE estudiante SET ");
            for (int i = 1; i < columnNames.size(); i++) { // Saltar la primera columna (ID)
                sql.append(columnNames.get(i)).append(" = ?, ");
            }
            sql.setLength(sql.length() - 2); // Eliminar la última coma y espacio
            sql.append(" WHERE ").append(columnNames.get(0)).append(" = ?");

            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());

            // Establecer los valores de los campos dinámicos
            for (int i = 1; i < fields.size(); i++) {
                String value = fields.get(i).getText();
                preparedStatement.setString(i, value);
            }

            // Agregar el ID al final para el WHERE
            preparedStatement.setInt(fields.size(), selectedId);

            // Ejecutar la consulta
            preparedStatement.executeUpdate();

            // Actualizar la tabla cargando los datos nuevamente
            refreshTable();

            // Limpiar los campos y salir del modo edición
            clearFormFields();
            isEditing = false;
            selectedId = -1;
            JOptionPane.showMessageDialog(this, "Registro actualizado exitosamente.");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar el registro: " + ex.getMessage());
        }
    }

    private void deleteRecord() {
        if (!isEditing || selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un registro para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar este registro?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            // Verificar conexión a la base de datos
            EscolaMusicaConnectionManager connectionManager = EscolaMusicaConnectionManager.getInstance();
            Connection connection = connectionManager.getConnection();

            // Actualizar la tabla cargando los datos nuevamente
            refreshTable();

            // Limpiar los campos y salir del modo edición
            clearFormFields();
            isEditing = false;
            selectedId = -1;
            JOptionPane.showMessageDialog(this, "Registro eliminado exitosamente.");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar el registro: " + ex.getMessage());
        }
    }

    private void refreshTable() throws SQLException {
        AccesoBBDD accesoBBDD = AccesoBBDD.getInstance();
        accesoBBDD.showTable(TablesNames.ESTUDIANTE);
    }

    private void clearFormFields() {
        for (JTextField field : fields) {
            field.setText("");
        }
    }
}