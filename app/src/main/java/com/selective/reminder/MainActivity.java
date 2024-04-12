package com.selective.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {

    Button login;
    Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login=findViewById(R.id.go_login);
        home=findViewById(R.id.go_home);

        login.setOnClickListener(view -> startActivityC(com.selective.reminder.UI.login.class));
        home.setOnClickListener(view -> startActivityC(com.selective.reminder.UI.main_ui_activity.class));
    }

    public void startActivityC(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}