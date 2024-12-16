package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Outer extends JFrame {
    public DynamicDatabaseInterface databaseInterface; // Define como atributo de clase

    public Outer() {
        super();
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        this.setTitle("Ventana Básica");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(new Color(0xf5f5f5));
    }

    private void inicializarComponentes() {
        JPanel header = new JPanel(new BorderLayout());
        JPanel aside = new JPanel(new BorderLayout());
        JPanel nav = new Navegacion(new BorderLayout());
        JPanel section = new JPanel(new BorderLayout());
        JPanel footer = new JPanel(new BorderLayout());

        // Inicializar la instancia compartida
        databaseInterface = DynamicDatabaseInterface.getInstance();

        JLabel hh = new JLabel("Escolas FunkRock", SwingConstants.LEFT);
        hh.setFont(new Font("Arial", Font.BOLD, 40));
        hh.setForeground(Color.WHITE);
        JLabel fh = new JLabel("Copy", SwingConstants.RIGHT);
        fh.setFont(new Font("Arial", Font.BOLD, 20));
        fh.setForeground(Color.WHITE);

        header.setBackground(new Color(0xb3a1e0));
        section.setBackground(new Color(0xf5f5f5));
        footer.setBackground(new Color(0xf5f5f5));
        aside.setBackground(new Color(0xf5f5f5));

        header.setPreferredSize(new Dimension(720, 100));
        section.setPreferredSize(new Dimension(350, 500));
        footer.setPreferredSize(new Dimension(720, 50));
        aside.setPreferredSize(new Dimension(350, 450));

        aside.setBorder(new EmptyBorder(10, 10, 10, 10));
        section.setBorder(new EmptyBorder(10, 10, 10, 10));

        header.add(hh, BorderLayout.CENTER);
        section.add(databaseInterface, BorderLayout.CENTER); // Añadir la tabla al centro de 'section'
        footer.add(fh, BorderLayout.CENTER);
        aside.add(nav, BorderLayout.NORTH);

        this.add(header, BorderLayout.NORTH);
        this.add(nav, BorderLayout.WEST);
        this.add(section, BorderLayout.CENTER);
        this.add(footer, BorderLayout.SOUTH);
    }
}