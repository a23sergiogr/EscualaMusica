package org.example;

import org.example.dao.DAO;
import org.example.dao.EstudianteDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        EscolaMusicaConnectionManager connectionManager = EscolaMusicaConnectionManager.getInstance();

        Connection connection = connectionManager.getConnection();

        DAO estudiante = new EstudianteDAO(connection);
        System.out.println(estudiante.getAll());
    }
}