package org.example;

public class Main {
    public static void main(String[] args) {
        AccesoBBDD accesoBBDD = AccesoBBDD.getInstance();
        accesoBBDD.showTable(TablesNames.PERSONA);
    }
}