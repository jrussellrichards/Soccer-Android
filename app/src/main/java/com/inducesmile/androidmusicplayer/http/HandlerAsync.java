package com.inducesmile.androidmusicplayer.http;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by joaquin on 23-06-2017.
 */

public class HandlerAsync extends AsyncTask< HashMap<String,String>, Void, String>{

    private static final String TAG = MyHttpConnect.class.getSimpleName();
    private AlertDialog alertDialog;
    private Context ctx;

    public interface TaskListener {
        public void onFinished(String result);
    }
    private String url,method;
    private final TaskListener taskListener;

    public HandlerAsync(Context ctx, String url, String method, TaskListener taskListener){
        this.ctx = ctx;
        this.url = url;
        this.method = method;
        this.taskListener = taskListener;
    }

    @Override
    protected String doInBackground(HashMap<String,String>... params) {
            MyHttpConnect httpConnect = new MyHttpConnect(method);
            String result = httpConnect.Connect(url, params[0]);
            return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Login Information....");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(final String result) {
        Log.v("SERVER_RESPONSE",result);
        if(result.equals("")){
            Toast.makeText(ctx, "Problemas con la conexion al servido", Toast.LENGTH_LONG).show();
        }
        try {
            JSONObject jsonObj = new JSONObject(result);
            if( jsonObj.has("message")){
                String message = jsonObj.getString("message");
                Log.e("ERRR", message);
                Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
            }

            boolean error = jsonObj.getBoolean("error");

            if (!error){
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(Toast.LENGTH_LONG); // As I am using LENGTH_LONG in Toast
                            taskListener.onFinished(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                thread.start();
            }


        } catch(JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());

        }
    }
}
