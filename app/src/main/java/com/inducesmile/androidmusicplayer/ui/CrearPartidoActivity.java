package com.inducesmile.androidmusicplayer.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
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
import java.util.List;

public class CrearPartidoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = CrearPartidoActivity.class.getSimpleName();
    Button btnDatePicker, btnTimePicker, btnCrearPartido;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Spinner spnEquipos, spnEstilos;

    private static String urlMisEquipos = "/px/futappApi/v1/mis_equipos";
    private static String urlInsertarPartido = "/px/futappApi/v1/insertar_partido";
    private String urlMethod = "POST";

    ArrayList<HashMap<String, String>> mapListEquipos;
    private ListView listViewMisEquipos;
    private ArrayList<Equipo> listEquipos = new ArrayList<Equipo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_partido);

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        btnCrearPartido = (Button) findViewById(R.id.btnCrearPartido);
        txtDate=(EditText)findViewById(R.id.edtDate);
        txtTime=(EditText)findViewById(R.id.in_time);

        btnCrearPartido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearPartido();
            }
        });
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyClick(v);
            }
        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyClick(v);
            }
        });

        CreateEstiloSpinner();


        StartHandlerEquipos();
    }

    private void StartHandlerEquipos() {
        String url = ((GlobalData) this.getApplication()).getBaseUrl() + urlMisEquipos;
        String email = ((GlobalData)getApplication()).getCorreo();
        HashMap<String,String> equipos = new HashMap<String,String>();
        equipos.put("email",email);

        HandlerAsync misEquiposHandler = new HandlerAsync(this, url, urlMethod, new HandlerAsync.TaskListener() {
            @Override
            public void onFinished(String result) {
                listEquipos = ParseEquipos(result);
                //DisplayMisEquipos(result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CreateEquipoSpinner();
                    }
                });
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
                //mapListCampeonatos.add(mapEquipo);
                listaEquipos.add(equipos);
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }

        return listaEquipos;
    }

    public void MyClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    public void CreateEquipoSpinner(){
        // Spinner element
        spnEquipos = (Spinner) findViewById(R.id.spnEquipos);

        // Spinner click listener
        spnEquipos.setOnItemSelectedListener(CrearPartidoActivity.this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();

        for (int i =0; i < listEquipos.size(); i++){
            categories.add(listEquipos.get(i).getNombre());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        //ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spnEquipos.setAdapter(dataAdapter);
    }

    public void CreateEstiloSpinner(){
        // Spinner element
        spnEstilos = (Spinner) findViewById(R.id.spnEstiloPartido);

        // Spinner click listener
        spnEstilos.setOnItemSelectedListener(CrearPartidoActivity.this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("5 vs 5");
        categories.add("7 vs 7");
        categories.add("11 vs 11");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spnEstilos.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void CrearPartido(){
        Log.d(TAG,
                "Date: "+mDay+"-"+mMonth+"-"+mYear+"\n "
                +"Time: "+ mHour +":"+mMinute + " \n"
                +"Equipo: " + spnEquipos.getSelectedItem().toString() + "\n"
                +"Estilo: "+ spnEstilos.getSelectedItem().toString()
        );

        String url = ((GlobalData) this.getApplication()).getBaseUrl() + urlInsertarPartido;
        HashMap<String,String> mapPartido = new HashMap<String, String>();

        String equipo,fecha,hora,estilo;
        //bla
        equipo = spnEquipos.getSelectedItem().toString();
        fecha = +mYear+"-"+mMonth+"-"+mDay;
        hora = mHour +":"+mMinute+":00";
        estilo = spnEstilos.getSelectedItem().toString();

        mapPartido.put("equipo",equipo);
        mapPartido.put("fecha", fecha);
        mapPartido.put("hora", hora);
        mapPartido.put("estilo", estilo);

        HandlerAsync partidoHandler = new HandlerAsync(this, url, urlMethod, new HandlerAsync.TaskListener() {
            @Override
            public void onFinished(String result) {
                Toast.makeText(CrearPartidoActivity.this,result,Toast.LENGTH_SHORT);
            }
        });

        partidoHandler.execute(mapPartido);
    }
}
