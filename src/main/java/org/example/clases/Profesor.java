package org.example.clases;

public class Profesor {
    private int idPersona;
    private String especialidad;
    private int anhosExperiencia;

    // Constructor vacío
    public Profesor() {}

    // Getters y setters
    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public int getAnhosExperiencia() {
        return anhosExperiencia;
    }

    public void setAnhosExperiencia(int anhosExperiencia) {
        this.anhosExperiencia = anhosExperiencia;
    }
}
