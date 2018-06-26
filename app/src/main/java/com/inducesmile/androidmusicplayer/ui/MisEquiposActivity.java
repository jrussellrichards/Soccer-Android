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
import com.inducesmile.androidmusicplayer.model.Equipo;
import com.inducesmile.androidmusicplayer.model.GlobalData;
import com.inducesmile.androidmusicplayer.model.HandlerAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MisEquiposActivity extends AppCompatActivity {


    private String TAG = MisEquiposActivity.class.getSimpleName();
    AlertDialog levelDialog;
    final CharSequence[] items = {" Crear Nuevo "," Encontrar Existente "};

    private static String urlMisEquipos = "/px/futappApi/v1/mis_equipos";
    private String urlMethod = "POST";

    private ArrayList<Equipo> listEquipos;

    ArrayList<HashMap<String, String>> mapListEquipos;
    private ListView listViewMisEquipos;
    private Dialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_equipos);

        listViewMisEquipos = (ListView) findViewById(R.id.listViewMisPartidos);
        mapListEquipos = new ArrayList<HashMap<String, String>>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAlertDialogWithRadioButtonGroup();
            }
        });

        StartHandlerMisEquipos();
    }

    private void StartHandlerMisEquipos() {
        String url = ((GlobalData) this.getApplication()).getBaseUrl() + urlMisEquipos;
        String email = ((GlobalData)getApplication()).getCorreo();
        HashMap<String,String> equipos = new HashMap<String,String>();
        equipos.put("email",email);

        HandlerAsync misEquiposHandler = new HandlerAsync(this, url, urlMethod, new HandlerAsync.TaskListener() {
            @Override
            public void onFinished(String result) {
                listEquipos = ParseEquipos(result);
                DisplayMisEquipos(result);
            }
        });
        misEquiposHandler.execute(equipos);
    }

    private ArrayList<Equipo> ParseEquipos(String result) {

        Log.d(TAG, "SERVER RESPONSE:" + result);
        ArrayList<Equipo> listaEquipos = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray jsonEquipos = jsonObj.getJSONArray("equipos");

            for (int i = 0; i < jsonEquipos.length(); i++) {
                JSONObject j = jsonEquipos.getJSONObject(i);

                int e_id = Integer.parseInt(j.getString("id"));
                String e_nombre = j.getString("nombre");
                String e_creador = j.getString("creador_email");
                String e_capitan = j.getString("capitan");

                HashMap<String, String> mapEquipo = new HashMap<>();

                mapEquipo.put("nombre", e_nombre);

                Equipo equipos = new Equipo(e_id,e_nombre,e_creador,e_capitan);
                mapListEquipos.add(mapEquipo);
                listaEquipos.add(equipos);
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }

        return listaEquipos;
    }

    private void DisplayMisEquipos(String result) {
        Log.d(TAG,result);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                if (pDialog.isShowing())
//                    pDialog.dismiss();
                /**
                 * Updating parsed JSON data into ListView
                 * */
                ListAdapter adapter = new SimpleAdapter(
                        MisEquiposActivity.this, mapListEquipos,
                        R.layout.single_mi_equipo, new String[]{
                        "nombre",
                        }, new int[]{
                        R.id.txtNombrePartido,
                });

                listViewMisEquipos.setAdapter(adapter);
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
                        Toast.makeText(MisEquiposActivity.this,"No Implementado Aun", Toast.LENGTH_SHORT).show();
                        break;

                }
                levelDialog.dismiss();
            }
        });
        levelDialog = builder.create();
        levelDialog.show();

    }
}
