package com.inducesmile.androidmusicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.inducesmile.androidmusicplayer.R;
import com.inducesmile.androidmusicplayer.model.Jugador;
import com.inducesmile.androidmusicplayer.ui.CrearEquipoActivity;

import java.util.List;


/**
 * Created by joaquin on 28-06-2017.
 */

public class JugadorAdapter extends ArrayAdapter<Jugador> {

    private List<Jugador> jugadores;
    private Context context;

    public JugadorAdapter(List<Jugador> jugadores, Context context){
        super(context, R.layout.equipo_nuevo_jugador, jugadores);

        this.context = context;
        this.jugadores = jugadores;

    }

    private static class JugadorHolder{
        public TextView nombreApellido;
        public CheckBox checkBox;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        JugadorHolder holder = new JugadorHolder();

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.equipo_nuevo_jugador, null);

            holder.nombreApellido = (TextView) v.findViewById(R.id.txtMiembroNuevo);
            holder.checkBox = (CheckBox) v.findViewById(R.id.chbIncluir);
            v.setTag(holder);
            holder.checkBox.setOnCheckedChangeListener((CrearEquipoActivity) context);
           // holder.nombreApellido.
        }else{
            holder = (JugadorHolder) v.getTag();
        }

        Jugador jn = jugadores.get(position);
        holder.nombreApellido.setText(jn.getNombre() +" " +jn.getApellido());
        holder.checkBox.setChecked(jn.isSelected());
        holder.checkBox.setTag(jn);
        return v;
    }
}
