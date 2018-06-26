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
import com.inducesmile.androidmusicplayer.model.GlobalData;
import com.inducesmile.androidmusicplayer.model.HandlerAsync;
import com.inducesmile.androidmusicplayer.model.Partido;
import com.inducesmile.androidmusicplayer.ui.CrearPartidoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class PartidosFragment extends Fragment {

    private String TAG = PartidosFragment.class.getSimpleName();
    AlertDialog levelDialog;
    final CharSequence[] items = {" Crear Nuevo "," Encontrar Existente "};

    private static String urlPartidosPublicos = "/px/futappApi/v1/partidos_publicos";
    private String urlMethod = "POST";

    private ArrayList<Partido> listPartidos;

    ArrayList<HashMap<String, String>> mapListPartidos;
    private ListView listViewMisPartidos;
    private Dialog pDialog;

    public PartidosFragment() {
        // Required empty public constructor
        Context context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_partidos, container, false);
        listViewMisPartidos = (ListView) view.findViewById(R.id.listViewMisPartidos);
        mapListPartidos = new ArrayList<HashMap<String, String>>();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabCrearPartido);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),CrearPartidoActivity.class));
            }
        });
        StartHandlerPartidosPublicos();
        return view;
    }

    private void StartHandlerPartidosPublicos() {
        String url = ((GlobalData) getActivity().getApplication()).getBaseUrl() + urlPartidosPublicos;
        String email = ((GlobalData) getActivity().getApplication()).getCorreo();

        HashMap<String,String> partidos = new HashMap<String,String>();
        partidos.put("email",email);

        HandlerAsync partidosPublicosHandler = new HandlerAsync( getActivity(), url, urlMethod, new HandlerAsync.TaskListener() {
            @Override
            public void onFinished(String result) {
                listPartidos = ParsePartidos(result);
                DisplayPartidosPublicos(result);
            }
        });
        partidosPublicosHandler.execute(partidos);
    }

    private ArrayList<Partido> ParsePartidos(String result) {

        Log.d(TAG, "SERVER RESPONSE:" + result);
        ArrayList<Partido> listaPartidos = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray jsonPartidos = jsonObj.getJSONArray("partidos");

            for (int i = 0; i < jsonPartidos.length(); i++) {
                JSONObject j = jsonPartidos.getJSONObject(i);

                String p_nombre = j.getString("nombre_equipo");
                String p_creador = j.getString("creador_equipo");
                String p_fecha = j.getString("fecha");
                String p_hora = j.getString("hora");
                String p_estilo = j.getString("estilo");

                HashMap<String, String> mapPartido = new HashMap<>();

                mapPartido.put("nombre", p_nombre);
                mapPartido.put("creador", p_creador);
                mapPartido.put("fecha", p_fecha);
                mapPartido.put("hora", p_hora);
                mapPartido.put("estilo", p_estilo);


                Partido partidos = new Partido(p_nombre,null,p_fecha,p_hora,p_estilo);
                mapListPartidos.add(mapPartido);
                listaPartidos.add(partidos);
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }

        return listaPartidos;
    }

    private void DisplayPartidosPublicos(String result) {
        Log.d(TAG,result);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListAdapter adapter = new SimpleAdapter(
                        getActivity(), mapListPartidos,
                        R.layout.single_publico_partido, new String[]{
                        "nombre",
                        "fecha",
                        "hora",
                        "estilo"
                }, new int[]{
                        R.id.txtNombrePartido,
                        R.id.txtFechaPartido,
                        R.id.txtHoraPartido,
                        R.id.txtEstiloPartido
                });

                listViewMisPartidos.setAdapter(adapter);
            }
        });
    }

}
