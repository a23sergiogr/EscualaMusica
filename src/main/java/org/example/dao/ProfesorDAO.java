package org.example.dao;

import org.example.clases.Profesor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO implements DAO<Profesor> {

    private Connection connection;

    // Constructor para inicializar la conexión
    public ProfesorDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Profesor get(long id) {
        Profesor profesor = null;
        String query = "SELECT * FROM Profesor WHERE id_persona = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                profesor = new Profesor();
                profesor.setIdPersona(rs.getInt("id_persona"));
                profesor.setEspecialidad(rs.getString("especialidad"));
                profesor.setAnhosExperiencia(rs.getInt("anhos_experiencia"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return profesor;
    }

    @Override
    public List<Profesor> getAll() {
        List<Profesor> profesores = new ArrayList<>();
        String query = "SELECT * FROM Profesor";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Profesor profesor = new Profesor();
                profesor.setIdPersona(rs.getInt("id_persona"));
                profesor.setEspecialidad(rs.getString("especialidad"));
                profesor.setAnhosExperiencia(rs.getInt("anhos_experiencia"));
                profesores.add(profesor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return profesores;
    }

    @Override
    public boolean save(Profesor profesor) {
        String query = "INSERT INTO Profesor (id_persona, especialidad, anhos_experiencia) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, profesor.getIdPersona());
            stmt.setString(2, profesor.getEspecialidad());
            stmt.setInt(3, profesor.getAnhosExperiencia());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(Profesor profesor) {
        String query = "UPDATE Profesor SET especialidad = ?, anhos_experiencia = ? WHERE id_persona = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, profesor.getEspecialidad());
            stmt.setInt(2, profesor.getAnhosExperiencia());
            stmt.setInt(3, profesor.getIdPersona());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(Profesor profesor) {
        return deleteById(profesor.getIdPersona());
    }

    @Override
    public boolean deleteById(long id) {
        String query = "DELETE FROM Profesor WHERE id_persona = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<Integer> getAllIds() {
        List<Integer> ids = new ArrayList<>();
        String query = "SELECT id_persona FROM Profesor";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ids.add(rs.getInt("id_persona"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ids;
    }

    @Override
    public void deleteAll() {
        String query = "DELETE FROM Profesor";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
