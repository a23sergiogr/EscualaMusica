package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Navegacion extends JPanel {
    public DynamicDatabaseInterface panel;

    public DynamicDatabaseInterface getPanel() {
        return panel;
    }

    public Navegacion(BorderLayout borderLayout) {
        super();
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        this.setSize(super.getSize());
        this.setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        // Create the side navigation panel (buttons and content below them)
        JPanel sideNavPanel = new JPanel();
        sideNavPanel.setLayout(new BoxLayout(sideNavPanel, BoxLayout.Y_AXIS));
        sideNavPanel.setPreferredSize(new Dimension(400, getHeight()));
        sideNavPanel.setBackground(new Color(0xf5f5f5));

        // Create the main content panel (shows below the buttons)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Create buttons and their corresponding content panels
        JButton btnProfesorado = new JButton("Profesorado");
        JPanel headerContent = createContentPanel("This is the Profesorado content", new Color(0xf5f5f5));
        btnProfesorado.setBackground(new Color(0xf5f5f5));
        btnProfesorado.setForeground(new Color(0x6a4d8c));
        btnProfesorado.setFont(new Font("Times", Font.BOLD, 18));

        JButton btnAlumnado = new JButton("Alumnado");
        JPanel articleContent = createContentPanel("This is the Alumnado content", new Color(0xf5f5f5));
        btnAlumnado.setBackground(new Color(0xf5f5f5));
        btnAlumnado.setForeground(new Color(0x6a4d8c));
        btnAlumnado.setFont(new Font("Times", Font.BOLD, 18));
        btnAlumnado.setPreferredSize(new Dimension(400, 50));

        JButton btnFooter = new JButton("Footer");
        JPanel footerContent = createContentPanel("This is the Footer content", new Color(0xf5f5f5));
        btnFooter.setBackground(new Color(0xf5f5f5));
        btnFooter.setForeground(new Color(0x6a4d8c));
        btnFooter.setFont(new Font("Times", Font.BOLD, 18));

        // Initially hide all content sections
        headerContent.setVisible(false);
        articleContent.setVisible(false);
        footerContent.setVisible(false);

        // Add buttons and content panels to the side navigation panel
        sideNavPanel.add(btnProfesorado);
        sideNavPanel.add(headerContent);
        sideNavPanel.add(btnAlumnado);
        sideNavPanel.add(articleContent);
        sideNavPanel.add(btnFooter);
        sideNavPanel.add(footerContent);

        // Add action listeners for the buttons to show content below them
        btnProfesorado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleContentVisibility(headerContent);
                articleContent.setVisible(false);
                footerContent.setVisible(false);
            }
        });

        btnAlumnado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleContentVisibility(articleContent);
                headerContent.setVisible(false);
                footerContent.setVisible(false);
            }
        });

        btnFooter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleContentVisibility(footerContent);
                headerContent.setVisible(false);
                articleContent.setVisible(false);
            }
        });

        // Add the side navigation and content panel to the main panel
        this.add(sideNavPanel, BorderLayout.WEST);
        this.add(contentPanel, BorderLayout.CENTER);
    }

    // Helper method to create content panels with a label and background color
    private JPanel createContentPanel(String labelText, Color color) {
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(color);
        contentPanel.add(new JLabel(labelText));
        contentPanel.setSize(new Dimension(400, 100)); // ensure a fixed height for content
        return contentPanel;
    }

    // Helper method to toggle content visibility
    private void toggleContentVisibility(JPanel panel) {
        panel.setVisible(!panel.isVisible());
    }
}