package com.selective.reminder.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.selective.reminder.R;

public class input_memo extends AppCompatActivity {

    EditText icon_type;
    EditText text;
    Button btn_save;

    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_memo);

        timePicker = (TimePicker) findViewById(R.id.simple_timepicker);
        timePicker.setIs24HourView(true);

        icon_type = (EditText) findViewById(R.id.input_icon_type);
        text = (EditText) findViewById(R.id.input_text);
        btn_save = (Button) findViewById(R.id.save_btn);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour =0;
                int minute =0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hour = timePicker.getHour();
                } else {
                    hour = timePicker.getCurrentHour();
                }
                minute =0 ;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    minute = timePicker.getMinute();
                } else {
                    minute = timePicker.getCurrentMinute();
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("icon_type", Integer.valueOf(icon_type.getText().toString()));
                resultIntent.putExtra("memo_content", text.getText().toString());
                resultIntent.putExtra("hour", hour);
                resultIntent.putExtra("minute", minute);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}