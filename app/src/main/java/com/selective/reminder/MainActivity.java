package com.selective.reminder;

import static com.selective.reminder.UI.login.mainurl;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 권한 체크
            checkPermission();
        } else {
            // 알람 권한 설정 api 31 이상
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // 알람 권한 설정 확인
                checkAlarmPermission(0); // checkAlarmPermission(0)
            } else {
                goMain();
            }
        }

    }

    public void startActivityC(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void checkPermission() {
        // 권한 체크
        Dexter.withContext(this)
                .withPermission(Manifest.permission.POST_NOTIFICATIONS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        // 권한 허용

                        // 알람 권한 설정 api 31 이상
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            // 알람 권한 설정 확인
                            checkAlarmPermission(0);
                        } else {
                            // 메인으로 이동
                            goMain();
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        // 권한 거부
                        Toast.makeText(getApplicationContext(), "앱을 이용하기 위해 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        // 권한 거부시 설정 다이얼로그 보여주기
                        showPermissionRationale(permissionToken);
                    }
                })
                .withErrorListener(dexterError -> {
                    // 권한설정 오류
                    Toast.makeText(this, "오류", Toast.LENGTH_SHORT).show();
                }).check();
    }

    /* 만약 권한을 거절했을 경우, 다이얼로그 보여주기 */
    private void showPermissionRationale(PermissionToken token) {
        new AlertDialog.Builder(this)
                .setPositiveButton("수락", (dialog, which) -> {
                    // 권한 요청 허용
                    token.continuePermissionRequest();
                })
                .setNegativeButton("거부", (dialog, which) -> {
                    // 권한 요청 취소
                    // 다시 권한요청 후 거부했을 경우 (onPermissionRationaleShouldBeShown) 메서드가 다시 실행 안됨 (권한 설정 못함) -> 앱 설정에서 직접 권한설정을 해야함
                    token.cancelPermissionRequest();
                })
                .setCancelable(false)
                .setMessage("앱을 이용하기 위해 권한이 필요합니다.")
                .show();
    }

    /* 알람 권한 설정 api 31 이상 */
    @RequiresApi(api = Build.VERSION_CODES.S)
    private void checkAlarmPermission(int flag) {
        // 알람 권한 허용 여부
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager.canScheduleExactAlarms()) {
            // 권한 허용이면 메인으로 이동
            goMain();
        } else {
            // 권한이 거부됨
            if (flag == 0) {
                // (알람 권한 요청)
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                this.activityLauncher.launch(intent);
            } else {
                Toast.makeText(this, "앱을 이용하기 위해 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /* 메인으로 이동 */
    private void goMain() {
        SharedPreferences skip = getSharedPreferences("skip_login", Activity.MODE_PRIVATE);
        if (skip.getBoolean("skip", false)) {
            startActivityC(com.selective.reminder.UI.home.class);
            overridePendingTransition(R.anim.alp_up, R.anim.none);
            finish();
        }


        SharedPreferences tokensp = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);

        String id = tokensp.getString("id",null);
        String pwd = tokensp.getString("pwd",null);
        if(id != null && pwd != null){
            login_(id,pwd);
        }
        else{
            SharedPreferences sp = getSharedPreferences("autoLogin", 0);
            SharedPreferences.Editor spEdit = sp.edit();
            spEdit.clear();
            spEdit.apply();

            Intent intent = new Intent(getApplicationContext(),com.selective.reminder.UI.login.class);
            startActivity(intent);
            finish();
        }
    }

    /* ActivityForResult 알람 권한 요청 후 결과 */
    private final ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // api 31 이상
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    // 알람 권한 설정 확인
                    checkAlarmPermission(1);
                }
            });



    protected void login_(String id, String pw) {
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
                                try {
                                    // "token" 키를 사용하여 토큰 추출
                                    String token = response.getString("token");
                                    SharedPreferences tokensp = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor spEdit = tokensp.edit();
                                    spEdit.putString("token", token);
                                    spEdit.putString("id", id);
                                    spEdit.putString("pwd", pw);
                                    spEdit.apply();

                                    startActivityC(com.selective.reminder.UI.home.class);
                                    overridePendingTransition(R.anim.alp_up, R.anim.none);
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
                                // 에러 로깅
                                Log.e("LoginError", "Volley error: " + error.toString());
                                // Network Response를 확인
                                SharedPreferences sp = getSharedPreferences("autoLogin", 0);
                                SharedPreferences.Editor spEdit = sp.edit();
                                spEdit.clear();
                                spEdit.apply();

                                Intent intent = new Intent(getApplicationContext(),com.selective.reminder.UI.login.class);
                                startActivity(intent);
                                finish();
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