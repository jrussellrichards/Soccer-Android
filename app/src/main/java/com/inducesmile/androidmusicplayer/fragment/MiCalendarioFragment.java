package com.inducesmile.androidmusicplayer.fragment;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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
import com.inducesmile.androidmusicplayer.model.Equipo;
import com.inducesmile.androidmusicplayer.model.GlobalData;
import com.inducesmile.androidmusicplayer.model.HandlerAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MiCalendarioFragment extends Fragment {

    private String TAG = MiCalendarioFragment.class.getSimpleName();
    AlertDialog levelDialog;
    final CharSequence[] items = {" Crear Nuevo "," Encontrar Existente "};

    private static String urlPartidosPublicos = "/px/futappApi/v1/mis_equipos";
    private String urlMethod = "POST";

    private ArrayList<Equipo> listEquipos;

    ArrayList<HashMap<String, String>> mapListEquipos;
    private ListView listViewMisEquipos;
    private Dialog pDialog;

    public MiCalendarioFragment() {
        // Required empty public constructor
        Context context = getContext();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_partidos, container, false);
        listViewMisEquipos = (ListView) view.findViewById(R.id.listViewMisPartidos);
        mapListEquipos = new ArrayList<HashMap<String, String>>();

        //StartHandlerMisEquipos();
        return view;
    }

    private void StartHandlerMisEquipos() {
        String url = ((GlobalData) getActivity().getApplication()).getBaseUrl() + urlPartidosPublicos;
        String email  =((GlobalData) getActivity().getApplication()).getCorreo();

        HashMap<String,String> equipos = new HashMap<String,String>();
        equipos.put("email",email);

        HandlerAsync misEquiposHandler = new HandlerAsync( getActivity(), url, urlMethod, new HandlerAsync.TaskListener() {
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

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListAdapter adapter = new SimpleAdapter(
                        getActivity(), mapListEquipos,
                        R.layout.single_publico_partido, new String[]{
                        "nombre",
                }, new int[]{
                        R.id.txtNombrePartido,
                });

                listViewMisEquipos.setAdapter(adapter);
            }
        });
    }

}
