package com.inducesmile.androidmusicplayer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import com.inducesmile.androidmusicplayer.http.MyHttpConnect;
import com.inducesmile.androidmusicplayer.R;
import com.inducesmile.androidmusicplayer.adapter.JugadorAdapter;
import com.inducesmile.androidmusicplayer.model.GlobalData;
import com.inducesmile.androidmusicplayer.model.HandlerAsync;
import com.inducesmile.androidmusicplayer.model.Jugador;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CrearEquipoActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = MyHttpConnect.class.getSimpleName();
    private String urlInsertarJugadorEquipo = "http://45.55.236.102/px/futappApi/v1/insertar_jugador";
    private String urlGetJugadores = "http://45.55.236.102/px/futappApi/v1/jugadores";
    private String urlCrearEquipo = "http://45.55.236.102/px/futappApi/v1/crear_equipo";
    private String urlMethod = "POST";


    private Button btnCrearEquipo;
    private EditText edtNombreEquipo;
    private String nombreEquipo;

    private ListView listViewJugador;
    private ArrayList<Jugador> listJugadoresNuevos;
    private JugadorAdapter jugadorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_equipo);

        edtNombreEquipo = (EditText) findViewById(R.id.edtNombreEquipo);
        listViewJugador = (ListView) findViewById(R.id.listViewJugadores);
        btnCrearEquipo = (Button) findViewById(R.id.btnCrearEquipo);
        btnCrearEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearNuevoEquipo();
            }
        });

        HandlerAsync jugadoreHandlerAsync = new HandlerAsync(this, urlGetJugadores, urlMethod, new HandlerAsync.TaskListener() {
            @Override
            public void onFinished(String result) {
                listJugadoresNuevos = getJugadores(result);
                displayJugadores();
            }
        });

        HashMap<String,String> myMap = new HashMap<String,String>();
        jugadoreHandlerAsync.execute(myMap);
    }

    private void CrearNuevoEquipo() {

        String nombre;
        String email = ((GlobalData)getApplication()).getCorreo();
        HashMap<String, String> equipo = new HashMap<String, String>();
        nombre = edtNombreEquipo.getText().toString();
        nombreEquipo = edtNombreEquipo.getText().toString();

        equipo.put("email",email);
        equipo.put("nombre",nombre);

        HandlerAsync crearEquipoHandlerAsync = new HandlerAsync(this, urlCrearEquipo, urlMethod, new HandlerAsync.TaskListener() {
            @Override
            public void onFinished(String result) {
                Log.d(TAG, result);
                InsertarEquipos();
            }
        });
        crearEquipoHandlerAsync.execute(equipo);
    }

    private void InsertarEquipos(){
        for (int i = 0; i< listJugadoresNuevos.size(); i++){
            Jugador jn = listJugadoresNuevos.get(i);
            if(jn.isSelected()){
                InsertarEquipo(jn);
            }

        }
    }

    private void InsertarEquipo(Jugador jn){

        Log.d(TAG,"SEND: "+ jn.getNombre() + " " + jn.getApellido());

        String email_jugador = jn.getCorreo();
        String nombre_equipo = nombreEquipo;
        HashMap<String, String> insertar_jugador = new HashMap<String, String>();
        insertar_jugador.put("email",email_jugador);
        insertar_jugador.put("equipo",nombre_equipo);

        HandlerAsync jugadorAsyn = new HandlerAsync(this, urlInsertarJugadorEquipo, urlMethod, new HandlerAsync.TaskListener() {
            @Override
            public void onFinished(String result) {
                Log.d(TAG,result);
            }
        });

        jugadorAsyn.execute(insertar_jugador);
    }

    private void displayJugadores() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                jugadorAdapter = new JugadorAdapter(listJugadoresNuevos,CrearEquipoActivity.this);
                ListView listView = (ListView) findViewById(R.id.listViewJugadores);
                listView.setAdapter(jugadorAdapter);
            }
        });
    }

    private ArrayList<Jugador> getJugadores(String result){
        Log.d(TAG, "SERVER RESPONSE:" + result);
        ArrayList<Jugador> listaJugadores = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray jsonJugadores = jsonObj.getJSONArray("jugadores");

            for (int i = 0; i < jsonJugadores.length(); i++) {
                JSONObject j = jsonJugadores.getJSONObject(i);

                String jg_email = j.getString("email");
                String jg_nombre = j.getString("nombre");
                String jg_apellido = j.getString("apellido");
                String jg_usuario = j.getString("usuario");

                HashMap<String, String> jugador = new HashMap<>();

                Jugador jugadorNuevo = new Jugador(jg_email,jg_usuario,jg_nombre,jg_apellido);

                jugador.put("nombre", jg_nombre);
                jugador.put("apellido", jg_apellido);
                jugador.put("usuario", jg_usuario);
                jugador.put("email", jg_email);

                listaJugadores.add(jugadorNuevo);
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }

        return listaJugadores;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        int pos = listViewJugador.getPositionForView(buttonView);
        Log.d(TAG, "toogle checked: " + pos+ "IP: " + ListView.INVALID_POSITION);

        if (pos!=ListView.INVALID_POSITION){
            Jugador j = listJugadoresNuevos.get(pos);
            j.setSelected(isChecked);

            Log.d(TAG, "Clicked on Jugador:" + j.getNombre() + " " + j.getApellido() + " checked: " + j.isSelected());
        }
    }
}
