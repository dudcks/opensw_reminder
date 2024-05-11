package com.selective.reminder.UI;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.selective.reminder.R;
import com.google.gson.*;

import org.json.JSONException;
import org.json.JSONObject;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    protected void login(String id, String password) {
        JSONObject jsonbody = new JSONObject();
        // {
        //    "username":"admin",
        //    "password":"admin"
        //}
        try {
            jsonbody.put("username", id);
            jsonbody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "json 오류", Toast.LENGTH_SHORT).show();
        }

        RequestQueue queue = Volley.newRequestQueue (getApplicationContext());


    }
}