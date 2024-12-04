package org.example.clases;

public class Clase {
    private int codClase;
    private String fechaClase;
    private String horaClase;
    private String instrumento;

    // Constructor vacío
    public Clase() {}

    // Getters y setters
    public int getCodClase() {
        return codClase;
    }

    public void setCodClase(int codClase) {
        this.codClase = codClase;
    }

    public String getFechaClase() {
        return fechaClase;
    }

    public void setFechaClase(String fechaClase) {
        this.fechaClase = fechaClase;
    }

    public String getHoraClase() {
        return horaClase;
    }

    public void setHoraClase(String horaClase) {
        this.horaClase = horaClase;
    }

    public String getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(String instrumento) {
        this.instrumento = instrumento;
    }
}
