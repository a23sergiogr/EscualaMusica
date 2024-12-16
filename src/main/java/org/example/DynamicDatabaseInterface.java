package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class DynamicDatabaseInterface extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel formPanel;
    private List<JTextField> fields;
    private List<String> columnNames;
    private JButton btnAdd, btnEdit, btnDelete, btnPDF;
    private static DynamicDatabaseInterface instance;

    public static DynamicDatabaseInterface getInstance(){
        if(instance == null){
            instance = new DynamicDatabaseInterface();
        }
        return instance;
    }

    private DynamicDatabaseInterface() {

        // Configurar el layout principal
        setLayout(new GridLayout(0, 2, 10, 10));

        JPanel agrupacion = new JPanel(new BorderLayout());

        // Crear el modelo de tabla
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        agrupacion.add(scrollPane, BorderLayout.CENTER);
        add(agrupacion, BorderLayout.CENTER);

        // Crear el panel de botones
        JPanel buttonPanel = new JPanel(new GridLayout(0, 5));
        btnAdd = new JButton("Add Record");
        btnEdit = new JButton("Edit Record");
        btnDelete = new JButton("Delete Record");
        btnPDF = new JButton("Export to Pdf");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(new JPanel());
        buttonPanel.add(btnPDF);
        agrupacion.add(buttonPanel, BorderLayout.SOUTH);

        // Listeners para los botones
        btnAdd.addActionListener(e -> addRecord());
        btnEdit.addActionListener(e -> editRecord());
        btnDelete.addActionListener(e -> deleteRecord());


        formPanel = new JPanel(new GridLayout(0, 2, 30, 10)); // Formulario con dos columnas (etiqueta y campo)
        fields = new ArrayList<>(); // Inicializa la lista de campos

        add(formPanel, BorderLayout.EAST); // Añade el panel de formulario al lado derecho

    }

    public void loadData(String tableName) {
        AccesoBBDD accesoBBDD = AccesoBBDD.getInstance();
        accesoBBDD.showTable(tableName, resultSet -> {
            try {
                tableModel.setRowCount(0);
                tableModel.setColumnCount(0);

                var metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Configurar columnas dinámicamente
                columnNames = new ArrayList<>(); // Inicializa los nombres de columnas
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    tableModel.addColumn(columnName);
                    columnNames.add(columnName); // Almacena el nombre de la columna
                }

                // Construir el formulario con estas columnas
                buildForm(columnNames);

                // Llenar filas
                while (resultSet.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = resultSet.getObject(i);
                    }
                    tableModel.addRow(row);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }


    public void loadData() {
        AccesoBBDD accesoBBDD = AccesoBBDD.getInstance();
        accesoBBDD.showTable(resultSet -> {
            try {
                tableModel.setRowCount(0);
                tableModel.setColumnCount(0);

                var metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Configurar columnas dinámicamente
                for (int i = 1; i <= columnCount; i++) {
                    tableModel.addColumn(metaData.getColumnName(i));
                }

                // Llenar filas
                while (resultSet.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = resultSet.getObject(i);
                    }
                    tableModel.addRow(row);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void addRecord() {
        AccesoBBDD accesoBBDD = AccesoBBDD.getInstance();
        accesoBBDD.addValue(columnNames, getFieldValues());
        loadData(); // Recargar datos después de añadir
    }

    private void editRecord() {
        AccesoBBDD accesoBBDD = AccesoBBDD.getInstance();
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
        accesoBBDD.updateValue(id, columnNames, getFieldValues());
        loadData(); // Recargar datos después de actualizar
    }

    private void deleteRecord() {
        AccesoBBDD accesoBBDD = AccesoBBDD.getInstance();
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
        accesoBBDD.deleteValue(id);
        loadData(); // Recargar datos después de eliminar
    }

    private List<String> getFieldValues() {
        // Extraer valores de los campos del formulario
        return fields.stream().map(JTextField::getText).toList();
    }

    public void buildForm(List<String> columnNames) {
        formPanel.removeAll(); // Limpia cualquier configuración previa del formulario
        fields.clear(); // Limpia la lista de campos anteriores
        formPanel.setLayout(new GridLayout(12, 2, 5, 20));
        this.columnNames = columnNames; // Almacena los nombres de las columnas

        for (String columnName : columnNames) {
            JPanel edit = new JPanel(new GridLayout(0, 2));
            JLabel label = new JLabel(columnName + ":");
            JTextField textField = new JTextField();
            edit.add(label); // Añade la etiqueta al formulario
            edit.add(textField); // Añade el campo de texto al formulario
            formPanel.add(edit);
            fields.add(textField); // Guarda el campo de texto en la lista
        }

        formPanel.revalidate();
        formPanel.repaint();
    }

}
