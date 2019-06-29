package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;

public class SceenTrait  extends AsyncTask<String, String, String> {

    private HttpURLConnection _conn=null;
    private Context _context=null;
    //private LinearLayout _root_view=null;
    public AsyncResponse delegate = null;

    public SceenTrait(Context context){
       this._context=context;
     //  this._root_view=rootview;

    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL("http://192.168.0.16/books/test_book.php");
            this._conn = (HttpURLConnection) url.openConnection();
            this._conn.setReadTimeout(15000 /* milliseconds */);
            this._conn.setConnectTimeout(15000 /* milliseconds */);
            this._conn.setRequestMethod("POST");
            this._conn.setDoInput(true);
            this._conn.setDoOutput(true);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
        //return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        delegate.processFinish(result);
       // Log.d("ScreenTraitResponse", "Response for screen :: " + result);

    }


}
