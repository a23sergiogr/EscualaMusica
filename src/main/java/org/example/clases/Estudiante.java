package org.example.clases;

import java.sql.Date;
import java.time.LocalDate;

public class Estudiante {
    private int idPersona;
    private LocalDate fechaNac;
    private String direccion;
    private int numMatricula;

    // Constructor vacío
    public Estudiante() {}

    // Getters y setters
    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public LocalDate getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(LocalDate fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getNumMatricula() {
        return numMatricula;
    }

    public void setNumMatricula(int numMatricula) {
        this.numMatricula = numMatricula;
    }

    // Método para convertir LocalDate a java.sql.Date
    public Date getSqlFechaNac() {
        if (fechaNac != null) {
            return Date.valueOf(fechaNac);  // Convierte LocalDate a java.sql.Date
        }
        return null;
    }
}
