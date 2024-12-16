package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        for (TablesNames value : TablesNames.values()) {
            JButton btnProfesorado = new JButton(value.name().toUpperCase());
            btnProfesorado.setBackground(new Color(0xf5f5f5));
            btnProfesorado.setForeground(new Color(0x6a4d8c));
            btnProfesorado.setFont(new Font("Times", Font.BOLD, 18));

            sideNavPanel.add(btnProfesorado);

            // Add action listeners for the buttons to show content below them
            btnProfesorado.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                }
            });
        }

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
}