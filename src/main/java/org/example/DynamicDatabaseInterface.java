package org.example;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;


public class DynamicDatabaseInterface extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel formPanel;
    private List<JTextField> fields;
    private List<String> columnNames;
    private JButton btnAdd, btnEdit, btnDelete, btnPDF;
    private static DynamicDatabaseInterface instance;
    private String tableName;

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
        btnPDF.addActionListener(e -> exportToPdf());


        formPanel = new JPanel(new GridLayout(0, 2, 30, 10)); // Formulario con dos columnas (etiqueta y campo)
        fields = new ArrayList<>(); // Inicializa la lista de campos

        add(formPanel, BorderLayout.EAST); // Añade el panel de formulario al lado derecho

    }

    public void loadData(String tableName) {
        AccesoBBDD accesoBBDD = AccesoBBDD.getInstance();
        this.tableName = tableName;
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

    public void exportToPdf() {
        String outputPath = "pdfs\\" + tableName + "_reporte_datos.pdf";
        Document document = new Document();

        try {
            // Crear el escritor para el documento PDF
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));

            // Abrir el documento
            document.open();

            // Crear fuentes para los textos
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            // 1. Título del informe
            Paragraph title = new Paragraph("Informe de Datos", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n")); // Espacio en blanco

            // 2. Fecha de generación
            String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            Paragraph date = new Paragraph("Fecha de generación: " + currentDate, subtitleFont);
            date.setAlignment(Element.ALIGN_RIGHT);
            document.add(date);

            document.add(new Paragraph("\n")); // Espacio en blanco

            // 3. Introducción
            Paragraph introduction = new Paragraph(
                    "Este informe presenta los datos recopilados de la tabla proporcionada. El objetivo es analizar y " +
                            "mostrar la información de manera clara y organizada.",
                    normalFont
            );
            introduction.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(introduction);

            document.add(new Paragraph("\n")); // Espacio en blanco

            // 4. Datos
            Paragraph dataSection = new Paragraph("Datos de la Tabla", subtitleFont);
            dataSection.setAlignment(Element.ALIGN_LEFT);
            document.add(dataSection);

            document.add(new Paragraph("\n")); // Espacio en blanco

            // Crear una tabla PDF con tantas columnas como la JTable
            int columnCount = table.getColumnCount();
            PdfPTable pdfTable = new PdfPTable(columnCount);
            pdfTable.setWidthPercentage(100); // Ajustar al ancho del documento

            // Agregar encabezados al PDF
            for (int col = 0; col < columnCount; col++) {
                String header = table.getColumnName(col);
                PdfPCell cell = new PdfPCell(new Phrase(header, subtitleFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                pdfTable.addCell(cell);
            }

            // Agregar los datos de la JTable al PDF
            int rowCount = table.getRowCount();
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < columnCount; col++) {
                    Object cellValue = table.getValueAt(row, col);
                    String cellText = cellValue == null ? "" : cellValue.toString();
                    PdfPCell cell = new PdfPCell(new Phrase(cellText, normalFont));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfTable.addCell(cell);
                }
            }

            // Añadir la tabla PDF al documento
            document.add(pdfTable);

            System.out.println("PDF creado exitosamente en: " + outputPath);
        } catch (DocumentException | IOException e) {
            System.out.println("Error al crear el PDF: " + e.getMessage());
        } finally {
            // Cerrar el documento
            document.close();
        }
    }
}
