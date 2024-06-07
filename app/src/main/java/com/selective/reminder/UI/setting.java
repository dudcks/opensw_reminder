package com.selective.reminder.UI;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import com.selective.reminder.R;

public class setting extends Fragment {
    Button login;
    Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        login = view.findViewById(R.id.login);
        logout = view.findViewById(R.id.logout);

        SharedPreferences skip = requireContext().getSharedPreferences("skip_login", Activity.MODE_PRIVATE);
        if (skip.getBoolean("skip", false)) {
            login.setVisibility(View.VISIBLE);
        }
        else{
            logout.setVisibility(View.VISIBLE);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),com.selective.reminder.UI.login.class);
                startActivity(intent,null);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences tokensp = requireContext().getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                SharedPreferences.Editor spEdit = tokensp.edit();
                spEdit.clear();
                spEdit.apply();
                Intent intent = new Intent(requireContext(),com.selective.reminder.MainActivity.class);
                startActivity(intent,null);
            }
        });

        return view;
    }
}