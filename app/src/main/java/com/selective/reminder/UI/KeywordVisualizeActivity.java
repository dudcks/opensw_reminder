package com.selective.reminder.UI;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.selective.reminder.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class KeywordVisualizeActivity extends AppCompatActivity {

    private TextView textViewKeywords;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_visualize);
        textViewKeywords = findViewById(R.id.textViewKeywords);
        pieChart = findViewById(R.id.piechart); // XML 레이아웃에 PieChart 추가 후 id 설정 필요

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = login.mainurl + "/api/user/keyword";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                ArrayList<PieEntry> entries = new ArrayList<>();
                Iterator<String> keys = response.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    int value = response.getInt(key);
                    entries.add(new PieEntry(value, key));
                }
                PieDataSet dataSet = new PieDataSet(entries, "");
                dataSet.setValueTextSize(20);
                PieData data = new PieData(dataSet);

                // 값 포맷을 정수형으로 설정하는 부분
                data.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        // 실수형 값을 정수형으로 변환하여 반환
                        return String.valueOf((int) value);
                    }
                });

                // 차트 스타일링
                dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
                pieChart.setData(data);
                pieChart.invalidate(); // 차트 갱신

                // 파이 차트에 대한 추가적인 스타일링
                //pieChart.setUsePercentValues(true); // 값을 퍼센트로 표시
                pieChart.setEntryLabelTextSize(16); // 라벨 텍스트 크기
                pieChart.setEntryLabelColor(Color.BLACK);
                pieChart.setCenterText("키워드 분석 결과");
                pieChart.setCenterTextSize(20);
                pieChart.setDescription(null); // 설명문 제거
                Legend legend = pieChart.getLegend(); // 범례 스타일링
                legend.setTextSize(12); // 범례 글씨크기 조정
                legend.setXEntrySpace(20); // 범례 간의 수평 간격 조정
            } catch (JSONException e) {
                e.printStackTrace();
                textViewKeywords.setText("JSON parse error");
            }
        }, error -> textViewKeywords.setText("That didn't work!")) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                SharedPreferences tokensp = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                String token = tokensp.getString("token", null);
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }
}