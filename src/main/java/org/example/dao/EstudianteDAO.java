package org.example.dao;

import org.example.clases.Estudiante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO implements DAO<Estudiante> {

    private Connection connection;

    public EstudianteDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Estudiante get(long id) {
        Estudiante estudiante = null;
        String query = "SELECT * FROM Estudiante WHERE id_persona = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                estudiante = new Estudiante();
                estudiante.setIdPersona(rs.getInt("id_persona"));
                estudiante.setFechaNac(rs.getDate("fecha_nac").toLocalDate());  // Convertimos SQL Date a LocalDate
                estudiante.setDireccion(rs.getString("direccion"));
                estudiante.setNumMatricula(rs.getInt("num_matricula"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return estudiante;
    }

    @Override
    public List<Estudiante> getAll() {
        List<Estudiante> estudiantes = new ArrayList<>();
        String query = "SELECT * FROM Estudiante";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setIdPersona(rs.getInt("id_persona"));
                estudiante.setFechaNac(rs.getDate("fecha_nac").toLocalDate());  // Convertimos SQL Date a LocalDate
                estudiante.setDireccion(rs.getString("direccion"));
                estudiante.setNumMatricula(rs.getInt("num_matricula"));
                estudiantes.add(estudiante);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return estudiantes;
    }

    @Override
    public boolean save(Estudiante estudiante) {
        String query = "INSERT INTO Estudiante (id_persona, fecha_nac, direccion, num_matricula) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, estudiante.getIdPersona());
            stmt.setDate(2, estudiante.getSqlFechaNac());  // Convertimos LocalDate a SQL Date
            stmt.setString(3, estudiante.getDireccion());
            stmt.setInt(4, estudiante.getNumMatricula());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(Estudiante estudiante) {
        String query = "UPDATE Estudiante SET fecha_nac = ?, direccion = ?, num_matricula = ? WHERE id_persona = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, estudiante.getSqlFechaNac());  // Convertimos LocalDate a SQL Date
            stmt.setString(2, estudiante.getDireccion());
            stmt.setInt(3, estudiante.getNumMatricula());
            stmt.setInt(4, estudiante.getIdPersona());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(Estudiante estudiante) {
        String query = "DELETE FROM Estudiante WHERE id_persona = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, estudiante.getIdPersona());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteById(long id) {
        String query = "DELETE FROM Estudiante WHERE id_persona = ?";

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
        String query = "SELECT id_persona FROM Estudiante";

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
        String query = "DELETE FROM Estudiante";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
