package com.selective.reminder.UI;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import com.selective.reminder.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class signup extends AppCompatActivity {
    EditText regi_username;
    EditText regi_password;
    EditText regi_nickname;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        regi_username=findViewById(R.id.regi_username);
        regi_password=findViewById(R.id.regi_password);

        submit = findViewById(R.id.btn_sumit);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(regi_username.getText().toString().trim().length() > 0 || regi_nickname.getText().toString().trim().length() > 0 || regi_password.getText().toString().trim().length() > 0 ) {
                    String url = login.mainurl + "/api/signup ";
                    JSONObject jsonBody = new JSONObject();
                    try {
                        jsonBody.put("username", regi_username.getText().toString().trim());
                        jsonBody.put("password", regi_password.getText().toString().trim());
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "json 오류", Toast.LENGTH_SHORT).show();
                    }

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();

                                    finish();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    findViewById(R.id.cpb1).setVisibility(View.GONE);

                                    if (error.networkResponse != null) {
                                        int statusCode = error.networkResponse.statusCode;
                                        if (statusCode == 409) {
                                            // 409 Conflict 상태 코드인 경우
                                            Toast.makeText(getApplicationContext(), "이미 사용 중입니다.", Toast.LENGTH_LONG).show();
                                        } else {
                                            // 기타 다른 상태 코드인 경우
                                            Toast.makeText(getApplicationContext(), "다시 시도하세요.", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        // 네트워크 응답 자체가 없는 경우 (예: 네트워크 연결 불가)
                                        Toast.makeText(getApplicationContext(), "네트워크 오류가 발생했습니다. 연결을 확인하세요.", Toast.LENGTH_LONG).show();
                                    }
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
                else{
                    Toast.makeText(getApplicationContext(), "입력 안된 칸이 있습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}