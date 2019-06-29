package com.example.myapplication;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


public class MainActivity extends AppCompatActivity implements AsyncResponse {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button button_signup = findViewById(R.id.sign_up);
        final  EditText email=findViewById(R.id.editText2);
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setColor(Color.RED);
        shape.getPaint().setStyle(Paint.Style.STROKE);
        shape.getPaint().setStrokeWidth(3);
        SceenTrait sceenTrait=new SceenTrait(getApplicationContext());
        sceenTrait.delegate=this;
        sceenTrait.execute();
        // Assign the created border to EditText widget
        email.setBackground(shape);
        final EditText password=findViewById(R.id.editText3);
        final EditText confirm_password=findViewById(R.id.editText4);
        button_signup.setX(20);

        button_signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               String passwrd_str=password.getText().toString();
               String confrm_psswd_str=confirm_password.getText().toString();
               Log.e("Password ::",passwrd_str);
                Log.e("Confirm Passeord ::",confrm_psswd_str);
               if(!(passwrd_str.trim() .equals(confrm_psswd_str.trim()))){
                   Toast toast=android.widget.Toast.makeText(getApplicationContext(),"Passwords did not  match",Toast.LENGTH_SHORT);
                   TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
                   toastMessage.setTextColor(Color.RED);
                   toast.show();
               }else{
                   String android_id = Secure.getString(getApplicationContext().getContentResolver(),
                                  Secure.ANDROID_ID);
                   android.widget.Toast.makeText(getApplicationContext(), "Email ::"+email.getText().toString()+"Password ::"+passwrd_str, Toast.LENGTH_SHORT).show();
                   CurlApi curlApi=new CurlApi(getApplicationContext(),email.getText().toString(),passwrd_str,android_id);
                   curlApi.execute();
               }

            }
        });

        final Button button_cancel = findViewById(R.id.cancel);
        button_cancel.setX(20);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                finish();
            }
        });

            }

    @Override
    public void processFinish(String result) {

      //  EditText editText=(EditText) findViewById(R.id.editText2);
        //editText.setX(150);
        try {
            JSONObject screen_obj = new JSONObject(result);
            //String panel_str=response_obj.getString("Panel1");
            Iterator<String> json_itr = screen_obj.keys();
            RelativeLayout panel_obj;
            boolean is_panel=false;
            //Log.d("Panel1",panel_str);
            while (json_itr.hasNext()){

                String panel = json_itr.next();
                String value =screen_obj.getString(panel);
                if(isJSONValid(value)){
                    Log.d("JsonSense :: ",screen_obj.getString(panel));
                    //if(panel=="panels"){
                     //   panel_obj=findViewById(getResources().getIdentifier(panel, "id", getPackageName()));
                  //  }else{

                  //  }
                    JSONObject prop_obj=new JSONObject(value);
                    Iterator<String> nested_itr=prop_obj.keys();
                   // this.processFinish(value);
                   this.processJsonStr(value,panel);
                }else{

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void processJsonStr(String json_str,String parent_key) throws JSONException {
        if(isJSONValid(json_str)){
            JSONObject  panel_json_obj=new JSONObject(json_str);
            Iterator<String> json_itr = panel_json_obj.keys();

            while (json_itr.hasNext()){
                String panel_str_id=json_itr.next();
                String prop_str =panel_json_obj.getString(panel_str_id);
                Log.d("processJsonStr :: ",prop_str);
                Log.e("Key process ::",panel_str_id);
                if(parent_key.equals("panels")){
                    if(isJSONValid(prop_str)){
                        RelativeLayout panel_obj=findViewById(getResources().getIdentifier(panel_str_id, "id", getPackageName()));
                        if(panel_obj!=null){
                            Log.d("jsonprocess ::",panel_str_id);
                            this.setPropOnElem(prop_str,panel_str_id,panel_obj);
                        }

                    }
                }else if(parent_key.equals("textboxes")){
                    EditText editTextobj=findViewById(getResources().getIdentifier(panel_str_id, "id", getPackageName()));
                    Log.e("TextBoxes ::",panel_str_id);
                    this.setPropOnElem(prop_str,panel_str_id,editTextobj);
                }else{
                    RelativeLayout mother_obj=(RelativeLayout) findViewById(R.id.parent_screen);
                    this.setPropOnElem(prop_str,"parent_screen",mother_obj);
                    Log.d("mother screen props ::",prop_str);
                }

            }
        }
    }

    public  void setPropOnElem(String jsonStr,String parentKey,View rel_obj) throws JSONException {
        JSONObject  panel_json_obj=new JSONObject(jsonStr);
        Iterator<String> json_itr = panel_json_obj.keys();
        int r=-1,g=-1,b=-1,width,height,x,y;
        Log.e("X prop applying on",parentKey);

        while (json_itr.hasNext()){
            String prop_key=json_itr.next();
            String prop_val=panel_json_obj.getString(prop_key);
            Log.d("setPropOnElem :: ",prop_val);

            if(isJSONValid(prop_val)){
                 this.setPropOnElem(prop_val,prop_key,rel_obj);
            }else{
                if(prop_key.equals("r")){
                    r=Integer.parseInt(prop_val);
                }else if(prop_key.equals("g")){
                    g=Integer.parseInt(prop_val);
                }else if(prop_key.equals("b")){
                    b=Integer.parseInt(prop_val);
                }else if(prop_key.equals("x")){

                   rel_obj.setX(Integer.parseInt(prop_val));
                }else if(prop_key.equals("y")){
                  //  rel_obj.setY(Integer.parseInt(prop_val));
                }
                if(r!=-1 && g!=-1 && b!=-1){
                    rel_obj.setBackgroundColor(Color.rgb(r,g,b));
                }
               Log.d("value ::",prop_val);
            }
        }
    }



        public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}

