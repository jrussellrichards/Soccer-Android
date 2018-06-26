package com.inducesmile.androidmusicplayer.http;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by joaquin on 23-06-2017.
 */

public class MyHttpConnect {

    private static final String TAG = MyHttpConnect.class.getSimpleName();
    private String method;

    public MyHttpConnect(String method){
        this.method = method;
    }


    public String Connect(String connectionUrl, HashMap<String, String> map){

        String response ="There was an error";
            URL url = null;
            boolean testConnection= false;

//        if(!isConnectedToServer(connectionUrl)){
//            return "";
//        }
            try {
                url = new URL(connectionUrl);
                testConnection =isConnectedToServer(connectionUrl);
                Log.e("CONECTION",testConnection + "");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("CONECTION",testConnection + "");
            }
            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpURLConnection.setRequestMethod(this.method);
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = null;
            try {
                outputStream = httpURLConnection.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

        }

        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(outputStreamWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String data = PrepareData(map);

        Log.e("DATA", data);
        try {
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


            // Recibiendo datos desde el servidor.
        InputStream in = null;
        try {
            in = new BufferedInputStream(httpURLConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        response = convertStreamToString(in);

        httpURLConnection.disconnect();
        Log.e("RESPONSE", response);
        return response;
    }

    private boolean isConnectedToServer(String url) {
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(10);
            connection.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private String PrepareData(HashMap<String, String> map) {
        String data ="";
        int count = 0;
        try {
            for (String key : map.keySet()) {
                if (count == 0) {
                    data += URLEncoder.encode("" + key, "UTF-8") + "=" + URLEncoder.encode(map.get(key), "UTF-8");
                } else {
                    data += "&";
                    data += URLEncoder.encode("" + key, "UTF-8") + "=" + URLEncoder.encode(map.get(key), "UTF-8");
                }
                count++;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }
}
