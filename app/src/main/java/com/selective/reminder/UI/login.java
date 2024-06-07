package com.selective.reminder.UI;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.selective.reminder.MainActivity;
import com.selective.reminder.R;
import com.android.volley.Request;
import com.selective.reminder.Util.NetworkStatus;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    public static final String mainurl = "http://10.0.2.2:8080";
    Button btn_login;
    EditText id;
    EditText pw;
    TextView name;
    TextView singupbtn;

    TextView skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        name=findViewById(R.id.main_name);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_upward);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.fade_in_upward);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.fade_in_upward);
        Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.fade_in_upward);
        Animation animation4 = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        animation.setDuration(1800);
        animation1.setDuration(1500);
        animation2.setDuration(1600);
        animation3.setDuration(2000);
        animation4.setDuration(8000);
        btn_login = (Button) findViewById(R.id.login_button);
        id = (EditText) findViewById(R.id.input_id);
        pw=(EditText) findViewById(R.id.input_pwd);
        singupbtn = (TextView) findViewById(R.id.sing_up_btn);
        skip = (TextView) findViewById(R.id.skip);

        name.setAnimation(animation);
        id.setAnimation(animation1);
        pw.setAnimation(animation2);
        btn_login.setAnimation(animation3);
        singupbtn.setAnimation(animation4);
        skip.setAnimation(animation4);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.cpb).setVisibility(View.VISIBLE);
                String input_id =  id.getText().toString().trim();
                String input_pwd = pw.getText().toString().trim();
                login_(input_id,input_pwd);
            }
        });

        singupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityC(signup.class);
                overridePendingTransition(R.anim.page_in_r,R.anim.none);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences tokensp = getSharedPreferences("skip_login", Activity.MODE_PRIVATE);
                SharedPreferences.Editor spEdit = tokensp.edit();
                spEdit.putBoolean("skip",true);
                spEdit.apply();
                startActivityC(main_ui_activity.class);
                //overridePendingTransition(R.anim.alp_up, R.anim.none);
                finish();
            }
        });

    }

    protected void login_(String id, String pw) {
        int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
        if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

            if(!id.isEmpty() && !pw.isEmpty()) {
                String login_url = mainurl + "/api/authenticate";
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("username", id);
                    jsonBody.put("password", pw);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "json 오류", Toast.LENGTH_SHORT).show();
                }

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, login_url, jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                findViewById(R.id.cpb).setVisibility(View.GONE);
                                try {
                                    // "token" 키를 사용하여 토큰 추출
                                    String token = response.getString("token");
                                    SharedPreferences tokensp = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor spEdit = tokensp.edit();
                                    spEdit.putString("token",token);
                                    spEdit.putString("id",id);
                                    spEdit.putString("pwd",pw);
                                    spEdit.apply();

                                    startActivityC(home.class);
                                    overridePendingTransition(R.anim.alp_up,R.anim.none);
                                    finish();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    // JSON 파싱 중 예외 발생시 사용자에게 알림
                                    Toast.makeText(getApplicationContext(), "서버 응답 처리 중 오류 발생", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                findViewById(R.id.cpb).setVisibility(View.GONE);
                                // 에러 로깅
                                Log.e("LoginError", "Volley error: " + error.toString());
                                // Network Response를 확인
                                SharedPreferences sp = getSharedPreferences("autoLogin", 0);
                                SharedPreferences.Editor spEdit = sp.edit();
                                spEdit.clear();
                                spEdit.apply();

                                Toast.makeText(getApplicationContext(), "아아디 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
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
                findViewById(R.id.cpb).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "입력 안된 칸이 있습니다.", Toast.LENGTH_SHORT).show();
            }
        }else {
            findViewById(R.id.cpb).setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
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


