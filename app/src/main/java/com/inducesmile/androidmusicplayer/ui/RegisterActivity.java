package com.inducesmile.androidmusicplayer.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.inducesmile.androidmusicplayer.R;
import com.inducesmile.androidmusicplayer.model.GlobalData;
import com.inducesmile.androidmusicplayer.model.HandlerAsync;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private String TAG = RegisterActivity.class.getSimpleName();
    Button btnRegistro, btnBack;
    EditText edtUsuario, edtNombre, edtApellido, edtCorreo, edtClave;


    private static String urlItinerariosAdd;
    private String urlMethod = "POST";
    private ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> listaItinerarios;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.urlItinerariosAdd =((GlobalData) this.getApplication()).getBaseUrl()+"/px/futappApi/v1/register";

        edtUsuario = (EditText) findViewById(R.id.edtRegUsuario);
        edtNombre = (EditText) findViewById(R.id.edtRegNombre);
        edtApellido = (EditText) findViewById(R.id.edtRegApellido);
        edtCorreo = (EditText) findViewById(R.id.edtRegCorreo);
        edtClave = (EditText) findViewById(R.id.edtRegClave);

        btnRegistro = (Button) findViewById(R.id.btnRegConfirm);
        btnBack = (Button) findViewById(R.id.btnRegBack);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registrar();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
            }
        });
    }

    private void Registrar() {
        String email, usuario, nombre, apellido,clave;

        HashMap<String, String> nuevo_jugador = new HashMap<String, String>();

        email = edtCorreo.getText().toString();
        usuario = edtUsuario.getText().toString();
        nombre = edtNombre.getText().toString();
        apellido = edtApellido.getText().toString();
        clave = edtClave.getText().toString();

        nuevo_jugador.put("email",email);
        nuevo_jugador.put("usuario",usuario);
        nuevo_jugador.put("nombre",nombre);
        nuevo_jugador.put("apellido",apellido);
        nuevo_jugador.put("clave",clave);

        HandlerAsync backgroundTask = new HandlerAsync(this, urlItinerariosAdd, urlMethod, new HandlerAsync.TaskListener() {
            @Override
            public void onFinished(String result) {
                RegistroTerminado();
            }
        });


        backgroundTask.execute(nuevo_jugador);

    }


    private void RegistroTerminado(){
        finish();
    }
}
