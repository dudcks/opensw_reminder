package com.selective.reminder.UI;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.selective.reminder.R;
import com.selective.reminder.Util.memoItem;

import java.util.ArrayList;
import java.util.List;

public class home extends Fragment {

    private ImageButton add_memo;
    private RecyclerView memo_list;
    private EditText memo_text;
    private MemoAdapter memoAdapter;
    private List<memoItem> memoItems;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        SharedPreferences memos = requireContext().getSharedPreferences("memo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = memos.edit();
        spEdit.apply();

        add_memo = (ImageButton) view.findViewById(R.id.add_memo);
        memo_list = (RecyclerView) view.findViewById(R.id.memo_list);
        memo_text = (EditText) view.findViewById(R.id.memo_input);

        memoItems = new ArrayList<>();
        memoAdapter = new MemoAdapter(memoItems);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        memo_list.setLayoutManager(layoutManager);
        memo_list.setAdapter(memoAdapter);

        add_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //새 엑티비티에서 입력값 받아오기.
                //로컬 저장소에 추가하기
                memoItem newItem = new memoItem();
                newItem.setMemo("테스트중임다.");
                memoItems.add(newItem);
                memoAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    public static class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder>{
        private List<memoItem> memoItems;

        public MemoAdapter(List<memoItem> memoItems) {
            this.memoItems = memoItems;
        }

        @NonNull
        @Override
        public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_memo, parent, false);
            return new MemoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {
            int currentPosition = holder.getAdapterPosition();

            memoItem item = memoItems.get(position);
        }

        @Override
        public int getItemCount() {
            return memoItems.size();
        }

        public class MemoViewHolder extends RecyclerView.ViewHolder {
            ImageView icon;
            EditText memo_edit; //나중에 그냥 텍스트로
            EditText memo_time; //나중에 그냥 텍스트로
            CheckBox is_done;


            public MemoViewHolder(@NonNull View itemView) {
                super(itemView);
                icon= (ImageView) itemView.findViewById(R.id.icon_type);
                memo_edit = (EditText) itemView.findViewById(R.id.memo_edit);
                memo_time = (EditText) itemView.findViewById(R.id.memo_time);
                is_done = (CheckBox) itemView.findViewById(R.id.is_done);
            }
        }
    }
}