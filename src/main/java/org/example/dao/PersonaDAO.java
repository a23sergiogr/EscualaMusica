package org.example.dao;

import org.example.clases.Persona;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO implements DAO<Persona> {

    private Connection connection;

    // Constructor para inicializar la conexión
    public PersonaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Persona get(long id) {
        Persona persona = null;
        String query = "SELECT * FROM Persona WHERE id_persona = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                persona = new Persona();
                persona.setIdPersona(rs.getInt("id_persona"));
                persona.setNombre(rs.getString("nombre"));
                persona.setDni(rs.getString("DNI"));
                persona.setApellidoUno(rs.getString("apellidoUno"));
                persona.setApellidoDos(rs.getString("apellidoDos"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return persona;
    }

    @Override
    public List<Persona> getAll() {
        List<Persona> personas = new ArrayList<>();
        String query = "SELECT * FROM Persona";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Persona persona = new Persona();
                persona.setIdPersona(rs.getInt("id_persona"));
                persona.setNombre(rs.getString("nombre"));
                persona.setDni(rs.getString("DNI"));
                persona.setApellidoUno(rs.getString("apellidoUno"));
                persona.setApellidoDos(rs.getString("apellidoDos"));
                personas.add(persona);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return personas;
    }

    @Override
    public boolean save(Persona persona) {
        String query = "INSERT INTO Persona (nombre, DNI, apellidoUno, apellidoDos) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getDni());
            stmt.setString(3, persona.getApellidoUno());
            stmt.setString(4, persona.getApellidoDos());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(Persona persona) {
        String query = "UPDATE Persona SET nombre = ?, DNI = ?, apellidoUno = ?, apellidoDos = ? WHERE id_persona = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getDni());
            stmt.setString(3, persona.getApellidoUno());
            stmt.setString(4, persona.getApellidoDos());
            stmt.setInt(5, persona.getIdPersona());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(Persona persona) {
        return deleteById(persona.getIdPersona());
    }

    @Override
    public boolean deleteById(long id) {
        String query = "DELETE FROM Persona WHERE id_persona = ?";

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
        String query = "SELECT id_persona FROM Persona";

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
        String query = "DELETE FROM Persona";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
