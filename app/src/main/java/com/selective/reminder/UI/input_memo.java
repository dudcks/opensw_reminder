package com.selective.reminder.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.selective.reminder.R;

public class input_memo extends AppCompatActivity {

    EditText icon_type;
    EditText text;
    EditText hour;
    EditText minute;
    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_memo);

        icon_type = (EditText) findViewById(R.id.input_icon_type);
        text = (EditText) findViewById(R.id.input_text);
        hour = (EditText) findViewById(R.id.input_hour);
        minute = (EditText) findViewById(R.id.input_minute);
        btn_save = (Button) findViewById(R.id.save_btn);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("icon_type", Integer.valueOf(icon_type.getText().toString()));
                resultIntent.putExtra("memo_content", text.getText().toString());
                resultIntent.putExtra("hour", Integer.valueOf(hour.getText().toString()));
                resultIntent.putExtra("minute", Integer.valueOf(minute.getText().toString()));
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}