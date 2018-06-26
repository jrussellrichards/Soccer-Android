package com.inducesmile.androidmusicplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.inducesmile.androidmusicplayer.R;
import com.inducesmile.androidmusicplayer.model.GlobalData;
import com.inducesmile.androidmusicplayer.model.HandlerAsync;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private String TAG = LoginActivity.class.getSimpleName();
    Button btnOmitir, btnIngresar, btnRegistro;
    EditText edtCorreo, edtClave;
    String urlLogin;
    String urlMethod = "POST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        urlLogin = ((GlobalData) this.getApplication()).getBaseUrl() + "/px/futappApi/v1/login";
        edtCorreo = (EditText) findViewById(R.id.edtLoginCorreo);
        edtClave = (EditText) findViewById(R.id.edtLoginClave);

        btnOmitir = (Button) findViewById(R.id.btnLoginOmitir);
        btnIngresar = (Button) findViewById(R.id.btnLoginIngresar);
        btnRegistro = (Button) findViewById(R.id.btnLoginRegistro);
        btnOmitir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GoToPrincipal();            }
        });
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), RegisterActivity.class));
            }
        });
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

    }

    private void Login() {
        String email,clave;

        email = edtCorreo.getText().toString();
        clave = edtClave.getText().toString();

        if(!email.equals("") && !clave.equals("")){
            HashMap<String,String> login_jugador = new HashMap<String, String>();
            login_jugador.put("email",email);
            login_jugador.put("clave",clave);
            HandlerAsync loginHandler = new HandlerAsync(this, urlLogin, urlMethod, new HandlerAsync.TaskListener() {
                @Override
                public void onFinished(String result) {
                    LoginTerminado(result);
                }
            });
            loginHandler.execute(login_jugador);
        }else{
            Toast.makeText(this,"Introduce los campos",Toast.LENGTH_SHORT).show();
        }

    }

    private void LoginTerminado(String result) {
        Log.d(TAG, result);

        String idNro="No Definido",usuario="No Definido",nombre="No Definido",apellido="No Definido",email="No Definido";
        try {
            JSONObject jsonObj = new JSONObject(result);

            usuario = jsonObj.getString("usuario");
            nombre = jsonObj.getString("nombre");
            apellido = jsonObj.getString("apellido");
            email = jsonObj.getString("email");

            ((GlobalData)getApplication()).setCorreo(email);
            ((GlobalData)getApplication()).setUsuario(usuario);
            ((GlobalData)getApplication()).setNombre(nombre);
            ((GlobalData)getApplication()).setApellido(apellido);

        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }

        GoToPrincipal(idNro,email,usuario,nombre,apellido);
    }

    private void GoToPrincipal(){
        Intent intent = new Intent(getBaseContext(),PrincipalActivity.class);

        startActivity(intent);
    }
    private void GoToPrincipal(String idNro,String email,String usuario, String nombre,String apellido) {
        Intent intent =new Intent(getBaseContext(),PrincipalActivity.class);
//        intent.putExtra("IDNRO",idNro);
//        intent.putExtra("EMAIL",email);
//        intent.putExtra("USUARIO",usuario);
//        intent.putExtra("NOMBRE",nombre);
//        intent.putExtra("APELLIDO",apellido);
        startActivity(intent);
    }
}
