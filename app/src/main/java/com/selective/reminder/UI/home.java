package com.selective.reminder.UI;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.SharedPreferences;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.selective.reminder.R;

public class home extends Fragment {

    private Button add_memo;
    private RecyclerView memo_list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences memos = requireContext().getSharedPreferences("memo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = memos.edit();
        spEdit.apply();

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}