package com.inducesmile.androidmusicplayer.ui;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.inducesmile.androidmusicplayer.R;
import com.inducesmile.androidmusicplayer.model.Equipo;
import com.inducesmile.androidmusicplayer.model.GlobalData;
import com.inducesmile.androidmusicplayer.model.HandlerAsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrearCampeonatoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final String TAG = CrearCampeonatoActivity.class.getSimpleName();

    private EditText edtNombreCampenato,edtEdicion,edtCupos,edtDescripcion, edtDate,edtLugar;
    private Button btnDatePicker, btnCrearCampeonato;
    private int mYear, mMonth, mDay;

    private Spinner spnEstilos;

    private static String urlCrearampeonato = "/px/futappApi/v1/crear_campeonato";
    private String urlMethod = "POST";

    private ArrayList<Equipo> listEquipos = new ArrayList<Equipo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_campeonato);

        edtNombreCampenato = (EditText) findViewById(R.id.edtNombreCampeonato);
        edtEdicion = (EditText) findViewById(R.id.edtEdicion);
        edtCupos = (EditText) findViewById(R.id.edtCupos);
        edtDescripcion = (EditText) findViewById(R.id.edtDescripcion);
        edtDate = (EditText) findViewById(R.id.edtDate);
        edtLugar = (EditText) findViewById(R.id.edtLugar);

        btnCrearCampeonato = (Button) findViewById(R.id.btnCrearCampeonato);
        btnDatePicker = (Button) findViewById(R.id.btn_date);

        btnCrearCampeonato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearCampeonato();
            }
        });
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyClick(v);
            }
        });

        CreateEstiloSpinner();

    }

    public void CreateEstiloSpinner(){
        // Spinner element
        spnEstilos = (Spinner) findViewById(R.id.spnEstiloPartido);

        // Spinner click listener
        spnEstilos.setOnItemSelectedListener(CrearCampeonatoActivity.this);

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

    public void MyClick(View v) {
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

                                edtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public void CrearCampeonato(){
        String email,nombre,edicion,fecha,estilo,descripcion,cupos,lugar;
        email = ((GlobalData)getApplication()).getCorreo();
        nombre = edtNombreCampenato.getText().toString();
        edicion = edtEdicion.getText().toString();
        fecha = edtDate.getText().toString();
        cupos = edtCupos.getText().toString();
        lugar = edtLugar.getText().toString();
        estilo = spnEstilos.getSelectedItem().toString();
        descripcion = edtDescripcion.getText().toString();

        HashMap<String,String> mapCampeonato = new HashMap<String, String>();

        mapCampeonato.put("email",email);
        mapCampeonato.put("nombre",nombre);
        mapCampeonato.put("edicion",edicion);
        mapCampeonato.put("fecha",fecha);
        mapCampeonato.put("cupos",cupos);
        mapCampeonato.put("estilo",estilo);
        mapCampeonato.put("lugar",lugar);
        mapCampeonato.put("descripcion",descripcion);

        String url = ((GlobalData) this.getApplication()).getBaseUrl() + urlCrearampeonato;


        HandlerAsync partidoHandler = new HandlerAsync(this, url, urlMethod, new HandlerAsync.TaskListener() {
            @Override
            public void onFinished(String result) {
                Toast.makeText(CrearCampeonatoActivity.this,result,Toast.LENGTH_SHORT);
            }
        });

        partidoHandler.execute(mapCampeonato);
    }
}
