package org.example;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccesoBBDD {
    private Connection connection;
    private static AccesoBBDD instance;

    private AccesoBBDD(){
    }

    public static AccesoBBDD getInstance() {
        if (instance == null)
            instance = new AccesoBBDD();
        return instance;
    }

    public void setConnection(Connection connection){
        this.connection = connection;
    }

    public ResultSet getResultSetFrom(TablesNames name){
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM estudiante")){

            return resultSet;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error fetching data from the database",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return null;
    }

}
