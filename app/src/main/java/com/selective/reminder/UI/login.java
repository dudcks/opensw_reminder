package com.selective.reminder.UI;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.selective.reminder.R;
import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    public static String mainurl = "http://localhost:8080/";
    Button btn_login;
    EditText id;
    EditText pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        login(id, pw);
    }




    protected void login(String id, String password) {
        String url = mainurl + "/api/authenticate ";

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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonbody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                    },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(request);


    }
}