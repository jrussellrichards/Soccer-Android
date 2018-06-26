package com.inducesmile.androidmusicplayer.model;

/**
 * Created by joaquin on 28-06-2017.
 */

public class Jugador {

    private String correo,usuario,nombre,apellido;
    private boolean isSelected = false;

    public Jugador(String correo, String usuario, String nombre, String apellido){
        this.correo = correo;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.isSelected = false;
    }
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }
}
