package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class CurlApi extends AsyncTask<String, String, String> {

    private HttpURLConnection _conn=null;
    private String _email_str = "";
    private String _passwrd_str = "";
    private String _device_id_str = "";
    private Context _context=null;

    public CurlApi(Context context,String email_str,String passwrd_str,String device_str) {
        //set context variables if required
        this._context=context;
        this._email_str=email_str;
        this._passwrd_str=passwrd_str;
        this._device_id_str=device_str;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        //HttpURLConnection conn = null;
        try {
            URL url = new URL("http://192.168.0.16/books/test_book.php");
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("email", this._email_str);
            postDataParams.put("password", this._passwrd_str);
            postDataParams.put("device_id", this._device_id_str);
            Log.e("params", postDataParams.toString());
            this._conn = (HttpURLConnection) url.openConnection();
            this._conn.setReadTimeout(15000 /* milliseconds */);
            this._conn.setConnectTimeout(15000 /* milliseconds */);
            this._conn.setRequestMethod("POST");
            this._conn.setDoInput(true);
            this._conn.setDoOutput(true);
            String paramsString = postDataParams.toString();

            DataOutputStream wr = new DataOutputStream(this._conn.getOutputStream());
            wr.writeBytes(paramsString);
            wr.flush();
            wr.close();
        } catch (MalformedURLException ex) {
            //e.printStackTrace();
            Log.e("MalformedURLException",ex.toString());
           // android.widget.Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        } catch (JSONException ex) {
            Log.e("JSONException",ex.toString());
           // android.widget.Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        } catch (ProtocolException ex) {
            //Log.e("ProtocolException",ex.toString());
            ex.printStackTrace();
        } catch (IOException ex) {
         //   Log.e("IOException",ex.toString());
            ex.printStackTrace();
        }


        try {
            InputStream in = new BufferedInputStream(this._conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }


            return result.toString();
        } catch (IOException e) {
            //Log.e("IOException",e.getCause().toString());
            return new String("Exception: " + e.getMessage());
        } finally {
            if (this._conn != null) {
                this._conn.disconnect();
            }
        }


    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

      //  Toast.makeText(getApplicationContext(), result,
          //      Toast.LENGTH_LONG).show();
        Log.d("test", "result from server: " + result);
    }
}


