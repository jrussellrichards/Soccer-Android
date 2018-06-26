package com.inducesmile.androidmusicplayer.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by joaquin on 25-06-2017.
 */

public class Usuario {
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String prefId = "id";
    public static final String prefUsuario = "usuario";
    public static final String prefNombre = "nombre";
    public static final String prefApellido = "apellido";
    public static final String prefEmail = "email";

    private String idNro;
    private String correo;
    private String usuario;
    private String nombre;
    private String apellido;

    private SharedPreferences sharedpreferences;

    public Usuario(String id, String correo, String usuario, String nombre, String apellido, SharedPreferences sharedpreferences) {
        this.idNro = id;
        this.correo = correo;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.sharedpreferences = sharedpreferences;
        LoadUser();
    }

    private void LoadUser(){
        try {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(prefId, this.idNro);
            editor.putString(prefUsuario, this.usuario);
            editor.putString(prefNombre, this.nombre);
            editor.putString(prefApellido, this.apellido);
            editor.putString(prefEmail, this.correo);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getIdNro() {
        return idNro;
    }

    public String getCorreo() {
        return correo;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void WelcomeUser(Context ctx){
        String toasWelcomeMessage = "Bienvenido " + nombre +" "+apellido;
        Toast.makeText(ctx,toasWelcomeMessage,Toast.LENGTH_LONG).show();
    }
}
