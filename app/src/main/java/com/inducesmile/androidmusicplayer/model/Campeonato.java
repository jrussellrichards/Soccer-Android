package com.inducesmile.androidmusicplayer.model;

/**
 * Created by joaquin on 25-06-2017.
 */

public class Campeonato {
    String creador,nombre,fecha_inicio,descripcion,lugar;
    int edicion,cupos_max,cupos_inscritos;

    public Campeonato(String nombre, String fecha_inicio, int edicion ){
        this.nombre = nombre;
        this.edicion = edicion;
        this.fecha_inicio = fecha_inicio;
    }

    public Campeonato(String creador, String nombre, String fecha_inicio, String descripcion, String lugar, int edicion, int cupos_max, int cupos_inscritos) {
        this.creador = creador;
        this.nombre = nombre;
        this.fecha_inicio = fecha_inicio;
        this.descripcion = descripcion;
        this.lugar = lugar;
        this.edicion = edicion;
        this.cupos_max = cupos_max;
        this.cupos_inscritos = cupos_inscritos;
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public int getEdicion() {
        return edicion;
    }

    public void setEdicion(int edicion) {
        this.edicion = edicion;
    }

    public int getCupos_max() {
        return cupos_max;
    }

    public void setCupos_max(int cupos_max) {
        this.cupos_max = cupos_max;
    }

    public int getCupos_inscritos() {
        return cupos_inscritos;
    }

    public void setCupos_inscritos(int cupos_inscritos) {
        this.cupos_inscritos = cupos_inscritos;
    }
}
