package com.inducesmile.androidmusicplayer.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.inducesmile.androidmusicplayer.R;
import com.inducesmile.androidmusicplayer.model.Campeonato;
import com.inducesmile.androidmusicplayer.model.GlobalData;
import com.inducesmile.androidmusicplayer.model.HandlerAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MisCampeonatosActivity extends AppCompatActivity {


    private String TAG = MisCampeonatosActivity.class.getSimpleName();
    AlertDialog levelDialog;
    final CharSequence[] items = {" Crear Nuevo "," Encontrar Existente "};

    private static String urlMisCampeonatos = "/px/futappApi/v1/mis_campeonatos";
    private String urlMethod = "POST";

    private ArrayList<Campeonato> listCampeonatos;

    ArrayList<HashMap<String, String>> mapListCampeonatos;
    private ListView listViewMisCampeonatos;
    private Dialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_campeonatos);

        listViewMisCampeonatos = (ListView) findViewById(R.id.listViewMisCampeonatos);
        mapListCampeonatos = new ArrayList<HashMap<String, String>>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MisCampeonatosActivity.this,CrearCampeonatoActivity.class));
            }
        });

        StartHandlerMisCampeonatos();
    }

    private void StartHandlerMisCampeonatos() {
        String url = ((GlobalData) this.getApplication()).getBaseUrl() + urlMisCampeonatos;
        String email = ((GlobalData)getApplication()).getCorreo();
        HashMap<String,String> campeonatos = new HashMap<String,String>();
        campeonatos.put("email",email);

        HandlerAsync misCampeonatosHandler = new HandlerAsync(this, url, urlMethod, new HandlerAsync.TaskListener() {
            @Override
            public void onFinished(String result) {
                listCampeonatos = ParseCampeonatos(result);
                DisplayMisCampeonatos(result);
            }
        });
        misCampeonatosHandler.execute(campeonatos);
    }

    private ArrayList<Campeonato> ParseCampeonatos(String result) {

        Log.d(TAG, "SERVER RESPONSE:" + result);
        ArrayList<Campeonato> listaCampeonatos = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray jsonEquipos = jsonObj.getJSONArray("campeonatos");

            for (int i = 0; i < jsonEquipos.length(); i++) {
                JSONObject j = jsonEquipos.getJSONObject(i);

                //int e_id = Integer.parseInt(j.getString("id"));
                String e_nombre = j.getString("nombre");
                String e_edicion = j.getString("edicion");
                String e_fecha = j.getString("fecha_inicio");

                HashMap<String, String> mapCampeonato = new HashMap<>();

                mapCampeonato.put("nombre", e_nombre);
                mapCampeonato.put("edicion", e_edicion);
                mapCampeonato.put("fecha_inicio", e_fecha);

                Campeonato campeonato = new Campeonato(e_nombre,e_fecha,Integer.parseInt(e_edicion));
                mapListCampeonatos.add(mapCampeonato);
                listaCampeonatos.add(campeonato);
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }

        return listaCampeonatos;
    }

    private void DisplayMisCampeonatos(String result) {
        Log.d(TAG,result);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListAdapter adapter = new SimpleAdapter(
                        MisCampeonatosActivity.this, mapListCampeonatos,
                        R.layout.single_mi_campeonato, new String[]{
                        "nombre",
                        "edicion",
                        "fecha_inicio"
                        }, new int[]{
                        R.id.txtCampeonatoNombre,
                        R.id.txtCampeonatoEdicion,
                        R.id.txtCampeonatoFecha
                });
                listViewMisCampeonatos.setAdapter(adapter);
            }
        });
    }


    public void CreateAlertDialogWithRadioButtonGroup(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo Equipo");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                        startActivity(new Intent(getBaseContext(),CrearEquipoActivity.class));
                        break;
                    case 1:
                        Toast.makeText(MisCampeonatosActivity.this,"No Implementado Aun", Toast.LENGTH_SHORT).show();
                        break;

                }
                levelDialog.dismiss();
            }
        });
        levelDialog = builder.create();
        levelDialog.show();

    }
}
