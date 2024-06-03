package com.selective.reminder.UI;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.selective.reminder.MainActivity;
import com.selective.reminder.R;
import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    public static final String mainurl = "http://10.0.2.2:8080";
    Button btn_login;
    EditText id;
    EditText pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = findViewById(R.id.login_button);
        id = findViewById(R.id.input_id);
        pw = findViewById(R.id.input_pwd);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = id.getText().toString();
                String upw = pw.getText().toString();
                //Toast.makeText(getApplicationContext(),"id:"+uid+"//"+"pwd:"+upw,Toast.LENGTH_SHORT).show();
                login_(uid, upw);
            }
        });
    }




    protected void login_(String id, String password) {
        String url = mainurl + "/api/authenticate";

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
                        Toast.makeText(getApplicationContext(),"성공",Toast.LENGTH_SHORT).show();
                        try{
                            String token = response.getString("token");
                            SharedPreferences tokensp = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor spEdit = tokensp.edit();
                            spEdit.putString("token",token);
                            spEdit.putString("id",id);
                            spEdit.putString("pwd",password);
                            spEdit.apply();
                            startActivityC(MainActivity.class);
                            finish();
                        } catch(JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "서버 응답 처리 중 오류 발생", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"실패",Toast.LENGTH_SHORT).show();
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
    public void startActivityC(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //Log.i("login_log", "이벤트 발생");
        View focusView = getCurrentFocus();
        if (focusView != null) {
            //Log.i("login_log", "포커스가 있다면");
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                // 터치위치가 태그위치에 속하지 않으면 키보드 내리기
                //Log.i("login_log", "터치위치가 태그위치에 속하지 않으면 키보드 내리기");
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}


