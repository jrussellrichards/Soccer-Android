package com.inducesmile.androidmusicplayer.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.inducesmile.androidmusicplayer.R;
import com.inducesmile.androidmusicplayer.model.Campeonato;
import com.inducesmile.androidmusicplayer.model.GlobalData;
import com.inducesmile.androidmusicplayer.model.HandlerAsync;
import com.inducesmile.androidmusicplayer.ui.CrearCampeonatoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class CampeonatosFragment extends Fragment {

    private String TAG = CampeonatosFragment.class.getSimpleName();
    AlertDialog levelDialog;
    final CharSequence[] items = {" Crear Nuevo "," Encontrar Existente "};

    private static String urlCampeonatosPublicos = "/px/futappApi/v1/campeonatos_publicos";
    private String urlMethod = "POST";

    private ArrayList<Campeonato> listCampeonatos;

    ArrayList<HashMap<String, String>> mapListCampeonatos;
    private ListView listViewMisCampeonatos;
    private Dialog pDialog;

    public CampeonatosFragment() {
        // Required empty public constructor
        Context context = getContext();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_partidos, container, false);
        listViewMisCampeonatos = (ListView) view.findViewById(R.id.listViewMisPartidos);
        mapListCampeonatos = new ArrayList<HashMap<String, String>>();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabCrearPartido);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),CrearCampeonatoActivity.class));
            }
        });

        StartHandlerMisCampeonatos();
        return view;
    }

    private void StartHandlerMisCampeonatos() {
        String url = ((GlobalData) getActivity().getApplication()).getBaseUrl() + urlCampeonatosPublicos;
        String email  =((GlobalData) getActivity().getApplication()).getCorreo();
        HashMap<String,String> Campeonatos = new HashMap<String,String>();

        Campeonatos.put("email",email);

        HandlerAsync misCampeonatosHandler = new HandlerAsync( getActivity(), url, urlMethod, new HandlerAsync.TaskListener() {
            @Override
            public void onFinished(String result) {
                listCampeonatos = ParseCampeonatos(result);
                DisplayMisCampeonatos(result);
            }
        });
        misCampeonatosHandler.execute(Campeonatos);
    }

    private ArrayList<Campeonato> ParseCampeonatos(String result) {

        Log.d(TAG, "SERVER RESPONSE:" + result);
        ArrayList<Campeonato> listaCampeonatos = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray jsonCampeonatos = jsonObj.getJSONArray("campeonatos");

            for (int i = 0; i < jsonCampeonatos.length(); i++) {
                JSONObject j = jsonCampeonatos.getJSONObject(i);

                String c_creador = j.getString("creador");
                String c_nombre = j.getString("nombre");
                String c_edicion = j.getString("edicion");
                String c_estilo = j.getString("estilo");
                String c_fecha_inicio = j.getString("fecha_inicio");
                String c_descripcion = j.getString("descripcion");
                String c_cupos_max = j.getString("cupos_max");
                String c_cupos_inscritos = j.getString("cupos_inscritos");
                String c_lugar = j.getString("lugar");


                HashMap<String, String> mapCampeonato = new HashMap<>();

                mapCampeonato.put("creador", c_creador);
                mapCampeonato.put("nombre", c_nombre);
                mapCampeonato.put("edicion", c_edicion);
                mapCampeonato.put("estilo", c_estilo);
                mapCampeonato.put("fecha_inicio", c_fecha_inicio);
                mapCampeonato.put("descripcion", c_descripcion);
                mapCampeonato.put("cupos_max", c_cupos_max);
                mapCampeonato.put("cupos_inscritos", c_cupos_inscritos);
                mapCampeonato.put("lugar", c_lugar);


                Campeonato Campeonatos = new Campeonato(c_creador,c_nombre,c_fecha_inicio,c_descripcion,
                        c_lugar,Integer.parseInt(c_edicion),Integer.parseInt(c_cupos_max),Integer.parseInt(c_cupos_inscritos));

                mapListCampeonatos.add(mapCampeonato);
                listaCampeonatos.add(Campeonatos);
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }

        return listaCampeonatos;
    }

    private void DisplayMisCampeonatos(String result) {
        Log.d(TAG,result);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListAdapter adapter = new SimpleAdapter(
                        getActivity(), mapListCampeonatos,
                        R.layout.single_publico_campeonato, new String[]{
                        "nombre",
                        "edicion",
                        "fecha",
                        "cupos_inscritos",
                        "cupos_max",
                        "estilo"
                }, new int[]{
                        R.id.txtCampeonatoNombre,
                        R.id.txtCampeonatoEdicion,
                        R.id.txtCampeonatoFecha,
                        R.id.txtCuposInscritos,
                        R.id.txtCuposMax,
                        R.id.txtEstiloCampeonato
                });

                listViewMisCampeonatos.setAdapter(adapter);
            }
        });
    }

}
