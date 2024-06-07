package com.selective.reminder.UI;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.selective.reminder.R;
import com.selective.reminder.Util.NetworkStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    EditText regi_username;
    EditText regi_password;
    Button sumit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        regi_username=findViewById(R.id.regi_username);
        regi_password=findViewById(R.id.regi_password);

        sumit = findViewById(R.id.btn_sumit);

        sumit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    if(!regi_username.getText().toString().trim().isEmpty() && !regi_password.getText().toString().trim().isEmpty()) {
                        findViewById(R.id.cpb1).setVisibility(View.VISIBLE);
                        String regi_url = login.mainurl+"/api/signup ";
                        JSONObject jsonBody = new JSONObject();
                        try {
                            jsonBody.put("username", regi_username.getText().toString().trim());
                            jsonBody.put("password", regi_password.getText().toString().trim());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "json 오류", Toast.LENGTH_SHORT).show();
                        }

                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, regi_url, jsonBody,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        findViewById(R.id.cpb1).setVisibility(View.GONE);
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
                                                Toast.makeText(getApplicationContext(), "이미 사용인 아이디 입니다.", Toast.LENGTH_LONG).show();
                                            } else {
                                                // 기타 다른 상태 코드인 경우
                                                Toast.makeText(getApplicationContext(), "잠시후 다시 시도해 주세요.", Toast.LENGTH_LONG).show();
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
                    }else {
                        Toast.makeText(getApplicationContext(), "입력 안된 칸이 있습니다.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });


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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.none,R.anim.page_out_r);
    }
}