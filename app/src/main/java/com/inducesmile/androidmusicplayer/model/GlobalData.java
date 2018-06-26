package com.inducesmile.androidmusicplayer.model;

import android.app.Application;

/**
 * Created by themachine on 29-06-2017.
 */
public class GlobalData extends Application {

//    private String baseUrl="http://10.0.2.2";
    private String baseUrl="http://45.55.236.102";
    private String usuario = "n";
    private String correo = "n@gmail.com";
    private String nombre = "n";
    private String apellido = "n";

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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
}
