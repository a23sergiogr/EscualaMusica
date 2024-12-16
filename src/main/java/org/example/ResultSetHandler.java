package org.example;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetHandler {
    void handle(ResultSet resultSet) throws SQLException;
}
