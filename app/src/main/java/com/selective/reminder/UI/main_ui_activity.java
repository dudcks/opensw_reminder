package com.selective.reminder.UI;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.selective.reminder.R;

public class main_ui_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ui);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.main_frame, new home())
                //.addToBackStack(null)
                .commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.setting) {
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame, new setting())
                        //.addToBackStack(null)
                        .commit();
            }
            else if(item.getItemId()==R.id.home){
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame, new home())
                        //.addToBackStack(null)
                        .commit();
            }
            else if(item.getItemId()==R.id.view){
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame, new search())
                        //.addToBackStack(null)
                        .commit();
            }
            return true;
        });
    }
}