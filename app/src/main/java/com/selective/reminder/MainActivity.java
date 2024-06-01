package com.selective.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {

    Button login;
    Button home;
    Button go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login=findViewById(R.id.go_login);
        home=findViewById(R.id.go_home);
        go = findViewById(R.id.go_confirm);
        login.setOnClickListener(view -> startActivityC(com.selective.reminder.UI.login.class));
        home.setOnClickListener(view -> startActivityC(com.selective.reminder.UI.main_ui_activity.class));
        go.setOnClickListener(view -> startActivityC(com.selective.reminder.UI.introActivity.class));
    }

    public void startActivityC(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}