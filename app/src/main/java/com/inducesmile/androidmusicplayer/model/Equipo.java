package com.inducesmile.androidmusicplayer.model;

/**
 * Created by joaquin on 25-06-2017.
 */

public class Equipo {

    private int id;
    private String nombre;
    private String creador;
    private String capitan;
    private Jugador equipo[];

    public Equipo(int id, String nombre, String creador, String capitan){
        this.id = id;
        this.nombre = nombre;
        this.creador = creador;
        this.capitan = capitan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public String getCapitan() {
        return capitan;
    }

    public void setCapitan(String capitan) {
        this.capitan = capitan;
    }

    public Jugador[] getEquipo() {
        return equipo;
    }

    public void setEquipo(Jugador[] equipo) {
        this.equipo = equipo;
    }
}
